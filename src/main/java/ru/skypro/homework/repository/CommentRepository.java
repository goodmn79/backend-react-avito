package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

/**
 * Репозиторий для работы с таблицей COMMENTS в базе данных.
 * <p>
 * Этот класс предоставляет методы для получения комментариев.
 * </p>
 *
 * @author Powered by ©AYE.team
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    /**
     * Получение всех комментариев объявления.
     *
     * @param id идентификатор объявления
     * @return список комментариев {@link CommentEntity}
     */
    List<CommentEntity> findAllByAdPk(int id);
}
