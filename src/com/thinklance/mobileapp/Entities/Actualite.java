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
public class Actualite {

    private String image;
    private String titre;
    private String auteur;
    private String description;
    private Date datePublication;
    private String urlToAcualite;

    public Actualite() {
    }

    public Actualite(String image, String titre, String auteur, String description, Date datePublication, String urlToActualite) {
        this.image = image;
        this.titre = titre;
        this.auteur = auteur;
        this.description = description;
        this.datePublication = datePublication;
        this.urlToAcualite = urlToActualite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }

    @Override
    public String toString() {
        return "Actualite{" + "image=" + image + ", titre=" + titre + ", auteur=" + auteur + ", description=" + description + ", datePublication=" + datePublication + '}';
    }

    public String getUrlToAcualite() {
        return urlToAcualite;
    }

    public void setUrlToAcualite(String urlToAcualite) {
        this.urlToAcualite = urlToAcualite;
    }
}
