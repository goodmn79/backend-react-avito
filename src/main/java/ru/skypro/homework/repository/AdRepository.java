package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;


/**
 * Репозиторий для работы с таблицей ADS в базе данных.
 * <p>
 * Этот класс предоставляет методы для получения данных об объявлениях.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface AdRepository extends JpaRepository<AdEntity, Integer> {
    /**
     * Получение данных об объявлении по его автору.
     *
     * @param author идентификатор автора объявления
     * @return список объявлений {@link AdEntity}
     */
    List<AdEntity> findByAuthor(UserEntity author);
}

