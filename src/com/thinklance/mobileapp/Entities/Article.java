/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Entities;

import java.util.Date;

/**
 *
 * @author Moez
 */
public class Article {

    private int id;
    private int utilisateur;
    private String titre;
    private String description;
    private Date dateHeure;
    private String texte;
    private String photoArticle;
    private int type;

    public Article() {
    }

    public Article(int id, int utilisateur, String titre, String description, Date dateHeure, String texte, String photoArticle, int type) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.titre = titre;
        this.description = description;
        this.dateHeure = dateHeure;
        this.texte = texte;
        this.photoArticle = photoArticle;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(int utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(Date dateHeure) {
        this.dateHeure = dateHeure;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getPhotoArticle() {
        return photoArticle;
    }

    public void setPhotoArticle(String photoArticle) {
        this.photoArticle = photoArticle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Article{" + "id=" + id + ", utilisateur=" + utilisateur + ", titre=" + titre + ", description=" + description + ", dateHeure=" + dateHeure + ", texte=" + texte + ", photoArticle=" + photoArticle + ", type=" + type + '}';
    }
}
