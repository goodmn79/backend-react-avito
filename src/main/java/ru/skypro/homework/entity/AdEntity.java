package ru.skypro.homework.entity;



import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ads")
@Data
public class AdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pk; // id объявления
    private String image;
    private int price;
    private String title;
    private String description; //поле из CreateOrUpdateAd с описанием объявления
}

/*  //Связь с таблицей UserEntity
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false) // каждое объявление должно иметь автора
    private UserEntity author;  // id автора объявления

 */


