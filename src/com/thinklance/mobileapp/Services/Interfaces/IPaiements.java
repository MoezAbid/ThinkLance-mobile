/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Services.Interfaces;

import com.thinklance.mobileapp.Entities.Paiement;
import java.util.ArrayList;

/**
 *
 * @author Moez
 */
public interface IPaiements {

    //public void ajouterPaiement(Paiement nouveauPaiement);
    public void supprimerPaiement(String idPaiement);

    public void rembourserPaiement(String idPaiement, Paiement paiement);

    //public ArrayList<Paiement> getListePaiements();
    public Paiement getPaiementSpecifique(String idPaiement);

    public ArrayList<Paiement> getPaiementsFreelancer();

    public ArrayList<Paiement> getPaiementsEmployeur();

    //public ArrayList<Paiement> getPaiementsUtilisateurSpecifique(int idUser);
    public void effectuerPaiement(int idProjet);

    //public ArrayList<Paiement> recherchePaiementFreelancerParId(String idMotCle);
    //public ArrayList<Paiement> recherchePaiementEmployeurParId(String idMotCle);
    //public ArrayList<Paiement> rechercheAllPaiementParId(String idMotCle);
    //public ArrayList<Paiement> rechercheAllPaiementParDates(String debut, String fin);
}
