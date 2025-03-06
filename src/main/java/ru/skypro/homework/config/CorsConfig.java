package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурационный класс для настройки CORS (Cross-Origin Resource Sharing).
 * Позволяет фронтенду, запущенному на <a href="http://localhost:3000">...</a>, взаимодействовать с бэкендом.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Configuration
public class CorsConfig {

    /**
     * Определяет настройки CORS для всего приложения.
     *
     * @return {@link WebMvcConfigurer} с настройками CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * Настраивает разрешённые источники, методы и заголовки для CORS-запросов.
             *
             * @param registry Реестр CORS-настроек
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowCredentials(true);
            }
        };
    }
}
