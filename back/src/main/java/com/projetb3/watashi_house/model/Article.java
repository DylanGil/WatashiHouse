package com.projetb3.watashi_house.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="Article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_article")
    private int id_article;

    @Column(name="nom")
    private int nom;

    @Column(name="description")
    private String description;

    //potentiel pb de type
    @Column(name="images")
    private int images;

    @Column(name="couleur")
    private String couleur;

    @Column(name="prix")
    private float prix;

    @Column(name="nbAvis")
    private int nbAvis;

    @Column(name="note")
    private int note;

    @Column(name="stock")
    private int stock;
}
