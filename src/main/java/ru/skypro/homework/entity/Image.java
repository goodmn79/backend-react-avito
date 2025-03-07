package ru.skypro.homework.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

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

    @Column(name = "mediaType")
    private String mediaType;

    @Transient
    private byte[] data;
}
