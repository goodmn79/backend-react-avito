package ru.skypro.homework.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Сущность, представляющая комментарий.
 */
@Data
@Entity
@Table(name = "comments")
@Accessors(chain = true)
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk")
    private int pk;

    @Column(name = "createdAt")
    private long createdAt;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_pk", nullable = false)
    private AdEntity ad;
}
