package com.berry.security;

import java.util.Collections;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.berry.entity.User;
import com.berry.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Пользователь не найден с email: " + email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getType().name())));
    }

    // загружаем пользователя по user id
    public User loadUserById(@NonNull Long userId) throws UsernameNotFoundException {
        return userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("Пользователь не найден с id: " + userId));
    }
}
