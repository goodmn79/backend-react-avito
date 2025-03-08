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
    private int id;
    private String path;
    private int size;
    private String mediaType;
    @Transient
    private byte[] data;
}
