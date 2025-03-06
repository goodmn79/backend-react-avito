package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурационный класс безопасности приложения.
 * Используется для настройки Spring Security, включая аутентификацию, авторизацию и CORS.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    /**
     * Сервис для загрузки пользовательских данных при аутентификации.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Массив путей, доступных без аутентификации.
     * Включает Swagger-документацию и эндпоинты регистрации и входа.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    /**
     * Настройка цепочки фильтров безопасности.
     *
     * @param http объект конфигурации безопасности
     * @return сконфигурированный {@link SecurityFilterChain}
     * @throws Exception в случае ошибки конфигурации
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST).permitAll()
                                        .mvcMatchers(HttpMethod.GET, "/ads", "/comments").permitAll()
                                        .mvcMatchers("/users/**").authenticated()
                                        .mvcMatchers(HttpMethod.POST, "/ads", "/comments")
                                        .authenticated()
                                        .mvcMatchers(HttpMethod.PATCH, "/ads/**", "/comments/**")
                                        .authenticated()
                                        .mvcMatchers(HttpMethod.GET, "/ads/{id}", "/ads/me")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Конфигурация менеджера аутентификации.
     *
     * @param http объект конфигурации безопасности
     * @return сконфигурированный {@link AuthenticationManager}
     * @throws Exception в случае ошибки конфигурации
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    /**
     * Конфигурация кодировщика паролей.
     * Используется BCrypt для безопасного хеширования паролей.
     *
     * @return экземпляр {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
