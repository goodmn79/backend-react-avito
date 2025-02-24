package ru.skypro.homework.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    private int cratedAt;
    private String text;
    @ManyToOne
    private UserEntity author;
}
