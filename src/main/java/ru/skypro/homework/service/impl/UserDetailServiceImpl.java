package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация сервиса для загрузки данных пользователя по имени пользователя.
 * <br> Этот класс реализует интерфейс {@link UserDetailsService} и используется для аутентификации пользователя
 * в системе, предоставляя его данные, такие как имя пользователя, пароль и роли.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Primary
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Загрузка данных пользователя по имени пользователя.
     *
     * @param username Имя пользователя
     * @return объект {@link UserDetails}, содержащий данные пользователя
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                mapRolesToAuthorities(userEntity.getRole())
        );
    }

    /**
     * Преобразование ролей пользователя в список авторитетов.
     *
     * @param role Роль пользователя, которая будет преобразована в {@link GrantedAuthority}
     * @return коллекция авторитетов ({@link GrantedAuthority}), ассоциированных с ролью пользователя
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role) {
        return Stream.of(role.name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
