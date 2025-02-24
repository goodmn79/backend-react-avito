package ru.skypro.homework.dto.comment;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    private long author;
    private String authorImage;
    private String authorFirstName;
    private int cratedAt;
    private String text;
}
