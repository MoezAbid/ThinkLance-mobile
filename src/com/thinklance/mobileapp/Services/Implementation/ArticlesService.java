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
import com.thinklance.mobileapp.Entities.Article;
import com.thinklance.mobileapp.Services.Interfaces.IArticles;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author Moez
 */
public class ArticlesService implements IArticles {

    private String result = "";

    @Override
    public List<Article> getListeAllArticles() {
        ArrayList<Article> listeArticles = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "articles/liste_api");
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json
                    Map<String, Object> articles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) articles.get("root");
                    //Parcourir la liste des tâches Json
                    for (Map<String, Object> obj : list) {
                        //Création des tâches et récupération de leurs données
                        Article article = new Article();
                        //Id
                        float id = Float.parseFloat(obj.get("id").toString());
                        //Auteur
                        JSONObject auteurArticle = new JSONObject((Map) obj.get("utilisateur"));
                        int idAuteur = (int) Math.round((double) auteurArticle.get("id"));
                        //Type Article
                        JSONObject typeArticle = new JSONObject((Map) obj.get("type"));
                        int idType = (int) Math.round((double) typeArticle.get("id"));
                        //Date Article
                        JSONObject dateArticle = new JSONObject((Map) obj.get("dateHeure"));
                        int dateHeureInt = (int) Math.round((double) dateArticle.get("timestamp"));
                        Date dateArticleDate = new Date(Long.parseLong(String.valueOf(dateHeureInt)));

                        article.setId((int) id);
                        article.setDescription(obj.get("description").toString());
                        article.setTexte(obj.get("texte").toString());
                        article.setTitre(obj.get("titre").toString());
                        article.setPhotoArticle(obj.get("photoArticle").toString());
                        article.setUtilisateur(idAuteur);
                        article.setType(idType);
                        article.setDateHeure(dateArticleDate);
                        listeArticles.add(article);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listeArticles;
    }

    @Override
    public Article getArticleSpecifique(int idArticle) {
        Article articleSpecifique = new Article();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "articles/article_api/" + idArticle);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json
                    Map<String, Object> articleJson = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) articleJson.get("root");
                    //Parcourir la liste des tâches Json
                    for (Map<String, Object> obj : list) {
                        //Création des tâches et récupération de leurs données
                        Article article = new Article();
                        //Id
                        float id = Float.parseFloat(obj.get("id").toString());
                        //Auteur
                        JSONObject auteurArticle = new JSONObject((Map) obj.get("utilisateur"));
                        int idAuteur = (int) Math.round((double) auteurArticle.get("id"));
                        //Type Article
                        JSONObject typeArticle = new JSONObject((Map) obj.get("type"));
                        int idType = (int) Math.round((double) typeArticle.get("id"));
                        //Date Article
                        JSONObject dateArticle = new JSONObject((Map) obj.get("dateHeure"));
                        int dateHeureInt = (int) Math.round((double) dateArticle.get("timestamp"));
                        Date dateArticleDate = new Date(Long.parseLong(String.valueOf(dateHeureInt)));

                        article.setId((int) id);
                        article.setDescription(obj.get("description").toString());
                        article.setTexte(obj.get("texte").toString());
                        article.setTitre(obj.get("titre").toString());
                        article.setPhotoArticle(obj.get("photoArticle").toString());
                        article.setUtilisateur(idAuteur);
                        article.setType(idType);
                        article.setDateHeure(dateArticleDate);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        System.out.println("ARTICLE SPECIFIQUE : "
                + articleSpecifique);
        return articleSpecifique;
    }
}
