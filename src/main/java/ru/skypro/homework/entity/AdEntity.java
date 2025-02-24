package ru.skypro.homework.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ads")
@Data
@Accessors(chain = true)
public class AdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    private int price;
    private String title;
    private String description;

    @OneToOne(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @OneToMany(mappedBy = "ad")
    @ToString.Exclude
    private List<CommentEntity> comments;
}


