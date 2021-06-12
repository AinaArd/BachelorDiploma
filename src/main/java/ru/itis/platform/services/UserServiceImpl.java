package ru.itis.platform.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.platform.dto.EditUser;
import ru.itis.platform.dto.NewUserDto;
import ru.itis.platform.dto.TokenDto;
import ru.itis.platform.dto.UserDto;
import ru.itis.platform.models.Course;
import ru.itis.platform.models.Role;
import ru.itis.platform.models.TokenStatus;
import ru.itis.platform.models.User;
import ru.itis.platform.repositories.CourseRepository;
import ru.itis.platform.repositories.UserRepository;
import ru.itis.platform.security.details.UserDetailsImpl;
import ru.itis.platform.security.util.JwtTokenUtil;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CourseRepository coursesRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.secret}")
    private String key;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository,
                           CourseRepository coursesRepository, JwtTokenUtil jwtTokenUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.coursesRepository = coursesRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void signup(NewUserDto dto) {
        if (!exists(dto.getLogin())) {
            String hashPassword = passwordEncoder.encode(dto.getPassword());
            User newUser = User.builder()
                    .login(dto.getLogin())
                    .password(hashPassword)
                    .fullName(dto.getFullName())
                    .role(Role.STUDENT)
                    .build();
            userRepository.save(newUser);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean exists(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    @Override
    public TokenDto login(UserDto dto) {
        return userRepository.findByLogin(dto.getLogin())
                .stream().findAny()
                .map(this::createToken)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        if (checkForUniqueness(login)) {
            return Optional.empty();
        }
        return userRepository.findByLogin(login);
    }

    @Override
    public Optional<User> getCurrentUser(Authentication authentication) {
        return Optional.of(((UserDetailsImpl) authentication.getPrincipal()).getUser());
    }

    @Override
    public User updateUserInfo(String login, EditUser userDto) {
        User user = findByLogin(login).get();
//        TODO: add new options to update
        user.setFullName(userDto.getFullName());
        return userRepository.save(user);
    }

    @Override
    public User changeUserPassword(String login, String password) {
        User user = findByLogin(login).get();
        String hashPassword = passwordEncoder.encode(password);
        user.setPassword(hashPassword);
        return userRepository.save(user);
    }

    @Override
    public boolean signUpUserForCourse(User user, Long courseId) {
        boolean isAlreadyJoined = user.getCourses().stream()
                .anyMatch(c -> c.getId().equals(courseId));
        Course course = coursesRepository.findById(courseId).orElseThrow(IllegalArgumentException::new);
        if (!isAlreadyJoined) {
            user.getCourses().add(course);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Optional<User> getUserByToken(String token) {
        Optional<User> userCandidate = findByLogin(jwtTokenUtil.getUsernameFromToken(token));
        return userCandidate;
    }

    private TokenDto createToken(User user) {
        String value = Jwts.builder()
                .claim("login", user.getLogin())
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return TokenDto.builder()
                .value(value)
                .status(TokenStatus.VALID)
                .build();
    }

    private boolean checkForUniqueness(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        return user.isEmpty();
    }
}
