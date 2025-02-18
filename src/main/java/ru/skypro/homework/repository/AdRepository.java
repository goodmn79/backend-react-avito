package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Long> {
    List<AdEntity> findByAuthor(UserEntity author);

    // Остальные методы репозитория стандартные
}

