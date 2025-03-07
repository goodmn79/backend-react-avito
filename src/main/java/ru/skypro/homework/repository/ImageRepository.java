package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Image;

import java.util.Optional;

/**
 * Репозиторий для работы с таблицей IMAGES в базе данных.
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    /**
     * Находит изображение по его идентификатору.
     *
     * @param id идентификатор изображения (не может быть null)
     * @return {@link Optional}, содержащий изображение, если оно найдено, иначе пустой {@link Optional}
     */
    Optional<Image> findById(Image id);

    /**
     * Сохраняет изображение в базе данных.
     * Если изображение новое (идентификатор не задан), создает новую запись.
     * Если изображение уже существует, обновляет данные.
     *
     * @param image изображение для сохранения (не может быть null)
     * @return сохраненное изображение (никогда не null)
     */
    <S extends Image> S save(S image);


    /**
     * Удаляет изображение по его идентификатору.
     *
     * @param id идентификатор изображения для удаления (не может быть null)
     * @throws javax.persistence.EntityNotFoundException если изображение с указанным ID не найдено
     */
    void delete(Image id);

}

