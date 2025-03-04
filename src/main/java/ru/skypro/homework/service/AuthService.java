package ru.skypro.homework.service;

import ru.skypro.homework.dto.user.Register;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(Register register);

    void clearSecurityContext(HttpServletResponse response, HttpServletRequest request);
}
