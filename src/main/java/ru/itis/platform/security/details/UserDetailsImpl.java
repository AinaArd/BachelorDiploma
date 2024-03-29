package ru.itis.platform.security.details;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.itis.platform.models.User;
import ru.itis.platform.repositories.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Data
@Component
public class UserDetailsImpl implements UserDetails, UserDetailsService {

    private User user;
    private String token;
    private UserRepository userRepository;

    UserDetailsImpl(User user) {
        this.user = user;
    }

    @Autowired
    public UserDetailsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userRole = user.getRole().name();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole);
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with login: " + login);
        }
        return new UserDetailsImpl(user.get());
    }
}
