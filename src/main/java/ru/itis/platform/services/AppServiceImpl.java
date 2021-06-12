package ru.itis.platform.services;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.platform.dto.AppDto;
import ru.itis.platform.dto.ClassDto;
import ru.itis.platform.models.App;
import ru.itis.platform.models.Course;
import ru.itis.platform.models.Status;
import ru.itis.platform.models.User;
import ru.itis.platform.repositories.AppRepository;
import ru.itis.platform.repositories.CourseRepository;
import ru.itis.platform.repositories.DocumentationRepository;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class AppServiceImpl implements AppService {
    public final static String source = "src\\main\\resources\\Demo.zip";
    public final static String destination = "src\\main\\resources";
    public final static String projectName = "Demo";
    public final static String pattern = "${}";

    private final CourseRepository courseRepository;
    private final AppRepository appRepository;
    private final DocumentationRepository documentationRepository;

    @Autowired
    public AppServiceImpl(CourseRepository courseRepository, AppRepository appRepository,
                          DocumentationRepository documentationRepository) {
        this.courseRepository = courseRepository;
        this.appRepository = appRepository;
        this.documentationRepository = documentationRepository;
    }

    @Override
    public void createAppInstanсe(User user, Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(IllegalArgumentException::new);
        String projectPath = unpackArchive(source, destination, course.getApp().getAppName());

        App appToUpdate = appRepository.findByAppNameAndCourse(course.getApp().getAppName(), course).orElseThrow(IllegalArgumentException::new);
        appToUpdate.setCreationDate(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        appToUpdate.setProjectPath(projectPath);
        appToUpdate.setUser(user);
        appToUpdate.setStatus(Status.CREATED);
        appRepository.save(appToUpdate);
    }

    @Override
    public void updateApp(List<String> words, String className) {
        Path file = findClass(className);
        fillInFile(file.toFile(), words);
    }

    @Override
    public App getAppById(Long appId) {
        return appRepository.findById(appId).orElseThrow(() -> new IllegalArgumentException("No app with such id"));
    }

    @Override
    public void finish(Long appId) {
        App app = appRepository.findById(appId).orElseThrow(IllegalArgumentException::new);
        app.setStatus(Status.FINISHED);
        appRepository.save(app);

        String sourceFile = destination + "\\" + app.getAppName();

        try (FileOutputStream fos = new FileOutputStream(destination + "\\" + "result.zip");
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            File fileToZip = new File(sourceFile);

            zipFile(fileToZip, fileToZip.getName(), zipOut);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ClassDto> getAppClasses(Long appId) {
        App app = getAppById(appId);
        List<ClassDto> javaFiles = new ArrayList<>();
        try {
            Files.find(Paths.get(app.getProjectPath()),
                    Integer.MAX_VALUE,
                    (filePath, fileAttr) -> fileAttr.isRegularFile())
                    .filter(fileCandidate -> fileCandidate.toFile().getName().endsWith(".java"))
                    .forEach(path -> addToMap(path, javaFiles));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return javaFiles;
    }

    @Override
    public Set<ClassDto> sort(List<ClassDto> classes) {
        classes.removeIf(classDto -> classDto.getClassName().equals("MavenWrapperDownloader.java"));
        classes.removeIf(classDto -> classDto.getClassName().equals("MvcConfig.java"));
        classes.removeIf(classDto -> classDto.getClassName().equals("DemoApplicationTests.java"));

        Set<ClassDto> sorted = new LinkedHashSet<>();
        sorted.add(findByName("User.java", classes));
        sorted.add(findByName("UserDto.java", classes));
        sorted.add(findByName("UserRepository.java", classes));
        sorted.add(findByName("UserService.java", classes));
        sorted.add(findByName("UserServiceImpl.java", classes));
        sorted.add(findByName("SingUpController.java", classes));
        sorted.add(findByName("SignInController.java", classes));
        sorted.add(findByName("ProfileController.java", classes));
        sorted.add(findByName("DemoApplication.java", classes));
        return sorted;

//        List<ClassDto> c = sortClasses(classes, "models");
//        List<ClassDto> cl = sortClasses(c, "dtos");
//        List<ClassDto> cla = sortClasses(cl, "repositories");
//        List<ClassDto> clas = sortClasses(cla, "services");
//        return new LinkedHashSet<>(sortClasses(clas, "controllers"));
    }

    @Override
    public void setInProgress(Long appId) {
        App app = appRepository.findById(appId).orElseThrow(IllegalArgumentException::new);
        app.setStatus(Status.IN_PROGRESS);
        appRepository.save(app);
    }

    private ClassDto findByName(String s, List<ClassDto> classes) {
        for (ClassDto classDto : classes) {
            if (classDto.getClassName().equals(s)) {
                return classDto;
            }
        }
        return null;
    }

    private void addToMap(Path path, List<ClassDto> javaFiles) {
        try {
            List<String> content = Files.readAllLines(path);
            ClassDto classDto = new ClassDto(path.toFile().getName(), content, getInfo(path.toFile().getName()));
            javaFiles.add(classDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getInfo(String name) {
        if (documentationRepository.findByClassName(name).isEmpty()) {
            return "";
        }
        return documentationRepository.findByClassName(name).get().getInfo();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    private String unpackArchive(String source, String destination, String appName) {
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destination);
        } catch (net.lingala.zip4j.exception.ZipException e) {
            e.printStackTrace();
        }
        return destination + "\\" + appName;
    }

//    private String findProjectName() {
//        File projectDir = new File(destination);
//        String[] pathNames = projectDir.list();
//        for(String path : pathNames) {
//            if (new File(projectDir + "\\" + path).isDirectory()) {
//                return destination + projectDir.getName();
//            }
//        }
//        return destination;
//    }

    private Path findClass(String className) {
        Path start = Paths.get(destination + "\\" + projectName);
        try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {

            Path result = stream
                    .filter(candidate -> String.valueOf(candidate.getFileName()).equals(className + ".java"))
                    .findFirst()
                    .get();

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private void fillInClasses() {
////        recursively for all files find ${} sign and replace
////        try {
//        Path dir = Paths.get(destination + "\\" + projectName);
////            Files.walk(dir).forEach(path -> fillInFile(path.toFile()));
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }

    @SneakyThrows
    private void fillInFile(File file, List<String> replaceable) {
        if (!file.isDirectory()) {
            StringBuilder sb = new StringBuilder();
            int i = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                for (String line; (line = br.readLine()) != null && replaceable.size() != 0; ) {

                    if (line.contains(pattern)) {
                        line = line.replace(pattern, replaceable.get(i));
                        replaceable.remove(i);
                    }

                    sb.append(line).append("\n");
                }
                FileUtils.write(file, sb, Charset.defaultCharset());
            }
            FileUtils.write(file, "}", Charset.defaultCharset(), true);
        }
    }
}