/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Utils;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.thinklance.mobileapp.Entities.Article;
import com.thinklance.mobileapp.Services.Implementation.ArticlesService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.simple.JSONObject;

/**
 *
 * @author Moez
 */
public class MoezUtils {

    public static final String urlWebService = "http://127.0.0.1/ThinkLance/web/app_dev.php/";
    public static final String urlPhotosArticle = "http://127.0.0.1/ThinkLance/web/";
    public static final String urlFacture = "http://127.0.0.1/ThinkLance/web/app_dev.php/paiements/facture_java_fx/";
    public static final String staticUrlImageArticlePlaceHolder = "file:///C:/wamp64/www/ThinkLance/web/uploads/articlesPhotos/ImageArticlePlaceHolder.png";
    private ArticlesService artServ = new ArticlesService();
    private String nomUser = "";
    private String nomProjet = "";
    private int nbMissions = 0;
    private double prixProjet = 0;

    public MoezUtils() {
    }

    public String getUrlWebService() {
        return urlWebService;
    }

    private static int getUserMoezConnecte() {
        return 12;
    }

    public static int getIdUserConnecte() {
        return getUserMoezConnecte();
    }

    public static String getTypeUserConnecte() {
        return "ROLE_EMPLOYEUR";
    }

    public static String raccourcirString(String s) {
        if (s.length() > 30) {
            s = s.substring(0, 10) + "...";
        }
        return s;
    }

    public String getNomUserForArticles(int idArticle) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "articles/article_api/" + idArticle);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String result = "";
                String nomUserFromJson = "";
                result = new String(con.getResponseData());
                org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(result);
                    //Auteur
                    org.json.JSONObject auteurArticle = jsonObject.getJSONObject("utilisateur");
                    nomUserFromJson = auteurArticle.getString("username");
                    nomUser = nomUserFromJson;
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return nomUser;
    }

    public String getNomUserFromId(int idUser) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "paiements/get_nom_user_from_id_api/" + idUser);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String result = "";
                String nomUserFromJson = "";
                result = new String(con.getResponseData());
                org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(result);
                    nomUserFromJson = jsonObject.getString("username");
                    nomUser = nomUserFromJson;
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return nomUser;
    }

    public String getNomProjetFromId(int idProjet) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "paiements/get_nom_projet_from_id_api/" + idProjet);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String result = "";
                String nomProjetFromJson = "";
                result = new String(con.getResponseData());
                org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(result);
                    nomProjetFromJson = jsonObject.getString("titreProjet");
                    nomProjet = nomProjetFromJson;
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return nomProjet;
    }

    public static void naviguerVersFreelancer(int idFreelancer) {
        System.out.println("Navigation vers freelancer : " + idFreelancer);
    }

    public static void naviguerVersEmployeur(int idEmployeur) {
        System.out.println("Navigation vers employeur : " + idEmployeur);
    }

    public static void naviguerVersProjet(int idProjet) {
        System.out.println("Navigation vers projet : " + idProjet);
    }

    public static int paiementDaysBetween(Date d1, Date d2) {

        int DAY = 24 * 60 * 60 * 1000;
        return (int) (Math.abs(d1.getTime() - d2.getTime()) / DAY);
    }

    public int getNombreMissionsDeLemployeurDuProjet(int idProjetConcerne) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "paiements/get_nom_projet_from_id_api/" + idProjetConcerne);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String result = "";
                int nbMissionsFromJson = 0;
                result = new String(con.getResponseData());
                org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(result);
                    org.json.JSONObject employeurObject = jsonObject.getJSONObject("employeur");
                    System.out.println("EMOPLOYEUR OBJECT. " + employeurObject);
                    nbMissionsFromJson = (int) Math.round((int) employeurObject.get("nbrMission"));
                    nbMissions = nbMissionsFromJson;
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return nbMissions;
    }

    public double getPrixDuProjet(int idProjetConcerne) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "paiements/get_nom_projet_from_id_api/" + idProjetConcerne);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String result = "";
                double prixProjetFromJson = 0;
                result = new String(con.getResponseData());
                org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(result);
                    prixProjetFromJson = jsonObject.getDouble("prix");
                    prixProjet = prixProjetFromJson;
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return prixProjet;
    }
}
