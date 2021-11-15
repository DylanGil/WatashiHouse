package com.projetb3.watashi_house.model;

import lombok.Data;
import org.hibernate.annotations.CollectionId;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="carte_de_paiement")
public class CarteDePaiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_carte")
    private int id_carte;

    @Column(name="numero")
    private int numero;

    @Column(name="cvc")
    private int cvc;

    //importation de date =~ probleme
    @Column(name="date_expiration")
    private Date date_expiration;
}
