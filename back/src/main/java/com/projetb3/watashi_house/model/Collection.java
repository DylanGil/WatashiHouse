package com.projetb3.watashi_house.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="Collection")
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_collection")
    private int id_collection;

    @Column(name="nom")
    private String nom;
}
