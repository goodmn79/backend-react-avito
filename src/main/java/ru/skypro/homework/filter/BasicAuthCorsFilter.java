package ru.skypro.homework.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для обработки CORS (Cross-Origin Resource Sharing) и поддержки авторизации через Basic Auth.
 *
 * @author Powered by ©AYE.team, sazonovfm, skypro-backend
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * Обрабатывает HTTP-запросы, добавляя заголовок для поддержки авторизации через куки.
     *
     * @param httpServletRequest  HTTP-запрос, который фильтруется.
     * @param httpServletResponse HTTP-ответ, в который добавляется заголовок.
     * @param filterChain         Цепочка фильтров для продолжения обработки запроса.
     * @throws ServletException Если возникает ошибка при обработке запроса.
     * @throws IOException      Если возникает ошибка ввода/вывода при обработке запроса.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
