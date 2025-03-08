package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с таблицей USERS в базе данных.
 * <p>
 * Этот класс предоставляет методы для получения информации о пользователе.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    /**
     * Получение пользователя по имени.
     *
     * @param username имя пользователя
     * @return сущность пользователя {@link UserEntity}
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Проверка существования пользователя в Базе данных по имени.
     *
     * @param username имя пользователя
     * @return `true`, если пользователь с таким именем уже существует, иначе `false`.
     */
    boolean existsByUsername(String username);
}
