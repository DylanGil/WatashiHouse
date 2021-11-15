#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
# Table: Categorie
#------------------------------------------------------------

CREATE TABLE Categorie(
        id_categorie Int  Auto_increment  NOT NULL ,
        nom          Varchar (30) NOT NULL
	,CONSTRAINT Categorie_PK PRIMARY KEY (id_categorie)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Article
#------------------------------------------------------------

CREATE TABLE Article(
        id_article   Int  Auto_increment  NOT NULL ,
        nom          Varchar (100) NOT NULL ,
        description  Varchar (500) NOT NULL ,
        images       Varchar (500) NOT NULL ,
        categorie    Varchar (50) NOT NULL ,
        collection   Varchar (50) NOT NULL ,
        couleur      Varchar (30) NOT NULL ,
        prix         Float NOT NULL ,
        nbAvis       Int NOT NULL ,
        note         Int NOT NULL ,
        stock        Int NOT NULL ,
        id_categorie Int NOT NULL
	,CONSTRAINT Article_PK PRIMARY KEY (id_article)

	,CONSTRAINT Article_Categorie_FK FOREIGN KEY (id_categorie) REFERENCES Categorie(id_categorie)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Utilisateur
#------------------------------------------------------------

CREATE TABLE Utilisateur(
        id_utilisateur      Int  Auto_increment  NOT NULL ,
        civilite            Varchar (20) NOT NULL ,
        nom                 Varchar (30) NOT NULL ,
        prenom              Varchar (30) NOT NULL ,
        email               Varchar (50) NOT NULL ,
        mdp                 Varchar (50) NOT NULL ,
        tel                 Varchar (10) NOT NULL ,
        adresse_livraison   Varchar (200) NOT NULL ,
        adresse_facturation Varchar (200) NOT NULL ,
        code_postale        Varchar (5) NOT NULL ,
        type                Enum ("client","administrateur") NOT NULL
	,CONSTRAINT Utilisateur_PK PRIMARY KEY (id_utilisateur)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: carte_de_paiement
#------------------------------------------------------------

CREATE TABLE carte_de_paiement(
        id_carte         Int  Auto_increment  NOT NULL ,
        numero           Int NOT NULL ,
        cvc              Int NOT NULL ,
        date_expiration  Date NOT NULL ,
        id_utilisateur   Int NOT NULL
	,CONSTRAINT carte_de_paiement_PK PRIMARY KEY (id_carte)

	,CONSTRAINT carte_de_paiement_Utilisateur_FK FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id_utilisateur)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Commandes
#------------------------------------------------------------

CREATE TABLE Commandes(
        id_commande    Int  Auto_increment  NOT NULL ,
        numero         Varchar (40) NOT NULL ,
        date_livraison Date NOT NULL ,
        prix_tot       Float NOT NULL ,
        lien_vers      Varchar (150) NOT NULL ,
        id_utilisateur Int NOT NULL
	,CONSTRAINT Commandes_PK PRIMARY KEY (id_commande)

	,CONSTRAINT Commandes_Utilisateur_FK FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id_utilisateur)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Acheter
#------------------------------------------------------------

CREATE TABLE Acheter(
        id_article     Int NOT NULL ,
        id_utilisateur Int NOT NULL
	,CONSTRAINT Acheter_PK PRIMARY KEY (id_article,id_utilisateur)

	,CONSTRAINT Acheter_Article_FK FOREIGN KEY (id_article) REFERENCES Article(id_article)
	,CONSTRAINT Acheter_Utilisateur0_FK FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id_utilisateur)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Contenir
#------------------------------------------------------------

CREATE TABLE Contenir(
        id_article  Int NOT NULL ,
        id_commande Int NOT NULL
	,CONSTRAINT Contenir_PK PRIMARY KEY (id_article,id_commande)

	,CONSTRAINT Contenir_Article_FK FOREIGN KEY (id_article) REFERENCES Article(id_article)
	,CONSTRAINT Contenir_Commandes0_FK FOREIGN KEY (id_commande) REFERENCES Commandes(id_commande)
)ENGINE=InnoDB;

