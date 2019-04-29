/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Services.Implementation;

import com.codename1.components.ToastBar;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.events.ActionListener;
import com.stripe.Stripe;
import com.thinklance.mobileapp.Entities.Paiement;
import com.thinklance.mobileapp.Services.Interfaces.IPaiements;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.json.JSONException;
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
    public void rembourserPaiement(String idPaiement, Paiement paiement) {
        MoezUtils mu = new MoezUtils();
        System.out.println("number of days between : " + mu.paiementDaysBetween(new Date(), paiement.getDateHeure()));
        if (mu.paiementDaysBetween(new Date(), paiement.getDateHeure()) < 3) {
            ConnectionRequest con = new ConnectionRequest();
            con.setUrl(MoezUtils.urlWebService + "paiements/liste_api/supprimer/" + idPaiement);
            NetworkManager.getInstance().addToQueueAndWait(con);
            ToastBar.showMessage("Paiement remboursé.", FontImage.MATERIAL_DONE);
        } else {
            Dialog.show("Opération impossible", "Vous ne pouvez pas rembourser un paiement qui date de plus de 3 jours.", "OK", "");
        }
    }

    @Override
    public Paiement getPaiementSpecifique(String idPaiement) {
        Paiement paiementSpecifique = new Paiement();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "paiements/get_paiement_specifique_api/" + idPaiement);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(result);
                    String titre = (String) jsonObject.getString("titre");
                    //Id
                    String id = jsonObject.get("idPaiement").toString();
                    //Projet
                    JSONObject projet = new JSONObject((Map) jsonObject.get("projet"));
                    int idProjet = (int) Math.round((double) projet.get("idProjet"));
                    //Employeur
                    JSONObject employeur = new JSONObject((Map) jsonObject.get("employeur"));
                    int idEmployeur = (int) Math.round((double) employeur.get("id"));
                    //Date Paiement
                    JSONObject dateHeurePaiement = new JSONObject((Map) jsonObject.get("dateHeurePaiement"));
                    int dateHeureInt = (int) Math.round((double) dateHeurePaiement.get("timestamp"));
                    Date datePaiementDate = new Date(Long.parseLong(String.valueOf(dateHeureInt)) * 1000);

                    paiementSpecifique.setIdPaiement(id);
                    paiementSpecifique.setDateHeure(datePaiementDate);
                    paiementSpecifique.setProjet(idProjet);
                    paiementSpecifique.setMontant((double) jsonObject.get("montant"));
                    paiementSpecifique.setIdEmployeur(idEmployeur);
                    paiementSpecifique.setDateHeure(datePaiementDate);
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return paiementSpecifique;
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
                        Date datePaiementDate = new Date(Long.parseLong(String.valueOf(dateHeureInt)) * 1000);

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
        con.setUrl(MoezUtils.urlWebService + "paiements/liste_employeur_api/" + MoezUtils.getIdUserConnecte());
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
                        Date datePaiementDate = new Date(Long.parseLong(String.valueOf(dateHeureInt)) * 1000);

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
