package ru.skypro.homework.dto.comment;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pk;
    private long cratedAt;
    private String text;
}
