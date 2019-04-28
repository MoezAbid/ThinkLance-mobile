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
public class Paiement {

    private String idPaiement;
    private Date dateHeure;
    private double montant;
    private int IdFreelancer;
    private int idEmployeur;
    private int projet;

    public Paiement(Date dateHeure, double montant, int IdFreelancer, int idEmployeur, int projet) {
        this.dateHeure = dateHeure;
        this.montant = montant;
        this.IdFreelancer = IdFreelancer;
        this.idEmployeur = idEmployeur;
        this.projet = projet;
    }

    public Paiement(String idPaiement, Date dateHeure, double montant, int IdFreelancer, int idEmployeur, int projet) {
        this.idPaiement = idPaiement;
        this.dateHeure = dateHeure;
        this.montant = montant;
        this.IdFreelancer = IdFreelancer;
        this.idEmployeur = idEmployeur;
        this.projet = projet;
    }

    public int getIdFreelancer() {
        return IdFreelancer;
    }

    public void setIdFreelancer(int IdFreelancer) {
        this.IdFreelancer = IdFreelancer;
    }

    public int getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(int idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public int getProjet() {
        return projet;
    }

    public void setProjet(int projet) {
        this.projet = projet;
    }

    public Paiement() {
    }

    public String getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(String idPaiement) {
        this.idPaiement = idPaiement;
    }

    public Date getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(Date dateHeure) {
        this.dateHeure = dateHeure;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "Paiement{" + "idPaiement=" + idPaiement + ", dateHeure=" + dateHeure + ", montant=" + montant + ", IdFreelancer=" + IdFreelancer + ", idEmployeur=" + idEmployeur + '}';
    }
}
