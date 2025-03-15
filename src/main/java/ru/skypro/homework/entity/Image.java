package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Сущность, представляющая изображение.
 */
@Entity
@Table(name = "images")
@Data
@Accessors(chain = true)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "path")
    private String path;

    @Column(name = "size")
    private long size;

    @Column(name = "media_type")
    private String mediaType;

    @Transient
    private byte[] data;
}
