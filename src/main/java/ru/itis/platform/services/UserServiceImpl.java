package ru.itis.platform.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.platform.dto.TokenDto;
import ru.itis.platform.dto.UserDto;
import ru.itis.platform.models.TokenStatus;
import ru.itis.platform.models.User;
import ru.itis.platform.repositories.UserRepository;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${secret}")
    private String key;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void signup(UserDto dto) {
        if (!exists(dto.getLogin())) {
            String hashPassword = passwordEncoder.encode(dto.getPassword());
            User newUser = User.builder()
                    .login(dto.getLogin())
                    .password(hashPassword)
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
}
