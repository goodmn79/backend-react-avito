package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурационный класс безопасности приложения.
 * Используется для настройки Spring Security, включая аутентификацию, авторизацию и CORS.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig implements WebMvcConfigurer {
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * Настройка цепочки фильтров безопасности.
     *
     * @param http объект конфигурации безопасности
     * @return сконфигурированный {@link SecurityFilterChain}
     * @throws Exception в случае ошибки конфигурации
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.warn("Configuring SecurityFilterChain...");
        http
                .csrf()
                .disable()
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("script-src 'self' 'unsafe-eval';")))
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST).permitAll()
                                        .mvcMatchers("/users/**")
                                        .authenticated()
                                        .mvcMatchers("/ads/**", "/comments/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        log.info("SecurityFilterChain configured");
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
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
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
