package com.projetb3.watashi_house.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="Commandes")
public class Commandes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_commande")
    private int id_commande;

    @Column(name="numero")
    private String numero;

    //importation de date =~ probleme
    @Column(name="date_livraison")
    private Date date_livraison;

    @Column(name="prix_tot")
    private float prix_tot;

    @Column(name="lien_vers")
    private String lien_vers;
}
