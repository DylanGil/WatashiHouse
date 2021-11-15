package com.projetb3.watashi_house.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Data
@Table(name="Categorie")
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_categorie")
    private int id_categorie;

    @Column(name="nom")
    private String nom;
}
