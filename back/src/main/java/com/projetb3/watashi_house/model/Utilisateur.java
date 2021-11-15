package com.projetb3.watashi_house.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="Utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private int id_utilisateur;

    @Column(name = "civilite")
    private String civilite;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email")
    private String email;

    @Column(name = "mdp")
    private String mdp;

    @Column(name = "tel")
    private String tel;

    @Column(name = "adresse_livraison")
    private String adresse_livraison;

    @Column(name = "adresse_facturation")
    private String adresse_facturation;

    @Column(name = "code_postal")
    private String code_postal;

    @Column(name = "type")
    private String type;
}
