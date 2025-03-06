package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Image;

/**
 * Репозиторий для работы с таблицей IMAGES в базе данных.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
