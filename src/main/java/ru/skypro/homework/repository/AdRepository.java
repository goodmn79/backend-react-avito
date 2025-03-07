package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;
import java.util.Optional;


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

    /**
     * Получение всех объявлений из базы данных.
     *
     * @return список всех объявлений {@link AdEntity}
     */
    List<AdEntity> findAll();

    /**
     * Сохраняет объявление в базе данных.
     * Если объявление новое (идентификатор не задан), создает новую запись.
     * Если объявление уже существует, обновляет данные.
     *
     * @param ad объявление для сохранения (не может быть null)
     * @return сохраненное объявление (никогда не null)
     */
    <S extends AdEntity> S save(S ad);

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления для удаления (не может быть null)
     * @throws org.springframework.dao.EmptyResultDataAccessException если объявление с указанным ID не найдено
     */
    void delete(AdEntity id);

    /**
     * Находит объявление по его идентификатору.
     *
     * @param id идентификатор объявления (не может быть null)
     * @return {@link Optional}, содержащий объявление, если оно найдено, иначе пустой {@link Optional}
     */
    Optional<AdEntity> findById(Integer id);
}

