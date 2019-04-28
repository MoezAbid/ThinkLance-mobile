/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Services.Implementation;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.stripe.Stripe;
import com.thinklance.mobileapp.Entities.Paiement;
import com.thinklance.mobileapp.Services.Interfaces.IPaiements;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author Moez
 */
public class PaiementsService implements IPaiements {

    private String result = "";

    public PaiementsService() {
        Stripe.apiKey = "sk_test_pnfuYUNCotWhyq7uOfqDmuGE";
    }

    @Override
    public void supprimerPaiement(String idPaiement) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "paiements/liste_api/supprimer/" + idPaiement);
        NetworkManager.getInstance().addToQueueAndWait(con);
    }

    @Override
    public void rembourserPaiement(String idPaiement) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "paiements/liste_api/supprimer/" + idPaiement);
        NetworkManager.getInstance().addToQueueAndWait(con);
    }

    @Override
    public Paiement getPaiementSpecifique(String idPaiement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Paiement> getPaiementsFreelancer() {
        ArrayList<Paiement> listePaiements = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "paiements/liste_freelancer_api/" + MoezUtils.getIdUserConnecte());
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> articles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) articles.get("root");
                    for (Map<String, Object> obj : list) {
                        Paiement paiement = new Paiement();
                        //Id
                        String id = obj.get("idPaiement").toString();
                        //Projet
                        JSONObject projet = new JSONObject((Map) obj.get("projet"));
                        int idProjet = (int) Math.round((double) projet.get("idProjet"));
                        //Employeur
                        JSONObject employeur = new JSONObject((Map) obj.get("employeur"));
                        int idEmployeur = (int) Math.round((double) employeur.get("id"));
                        //Date Paiement
                        JSONObject dateHeurePaiement = new JSONObject((Map) obj.get("dateHeurePaiement"));
                        int dateHeureInt = (int) Math.round((double) dateHeurePaiement.get("timestamp"));
                        Date datePaiementDate = new Date(Long.parseLong(String.valueOf(dateHeureInt)));

                        paiement.setIdPaiement(id);
                        paiement.setDateHeure(datePaiementDate);
                        paiement.setProjet(idProjet);
                        paiement.setMontant((double) obj.get("montant"));
                        paiement.setIdEmployeur(idEmployeur);
                        paiement.setDateHeure(datePaiementDate);
                        listePaiements.add(paiement);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listePaiements;
    }

    @Override
    public ArrayList<Paiement> getPaiementsEmployeur() {
        ArrayList<Paiement> listePaiements = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "paiements/liste_freelancer_api/" + MoezUtils.getIdUserConnecte());
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> articles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) articles.get("root");
                    for (Map<String, Object> obj : list) {
                        Paiement paiement = new Paiement();
                        //Id
                        String id = obj.get("idPaiement").toString();
                        //Projet
                        JSONObject projet = new JSONObject((Map) obj.get("projet"));
                        int idProjet = (int) Math.round((double) projet.get("idProjet"));
                        //Employeur
                        JSONObject freelancer = new JSONObject((Map) obj.get("freelancer"));
                        int idFreelancer = (int) Math.round((double) freelancer.get("id"));
                        //Date Paiement
                        JSONObject dateHeurePaiement = new JSONObject((Map) obj.get("dateHeurePaiement"));
                        int dateHeureInt = (int) Math.round((double) dateHeurePaiement.get("timestamp"));
                        Date datePaiementDate = new Date(Long.parseLong(String.valueOf(dateHeureInt)));

                        paiement.setIdPaiement(id);
                        paiement.setDateHeure(datePaiementDate);
                        paiement.setProjet(idProjet);
                        paiement.setMontant((double) obj.get("montant"));
                        paiement.setIdFreelancer(idFreelancer);
                        paiement.setDateHeure(datePaiementDate);
                        listePaiements.add(paiement);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listePaiements;
    }

    @Override
    public void effectuerPaiement(int idProjet) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "paiements/payer_api/" + idProjet);
        NetworkManager.getInstance().addToQueueAndWait(con);
    }
}
