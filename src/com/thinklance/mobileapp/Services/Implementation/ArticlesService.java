/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Services.Implementation;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.thinklance.mobileapp.Entities.Article;
import com.thinklance.mobileapp.Entities.TypeArticle;
import com.thinklance.mobileapp.Services.Interfaces.IArticles;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.simple.JSONObject;

/**
 *
 * @author Moez
 */
public class ArticlesService implements IArticles {

    private String result = "";
    private String typeArticle = "";
    private int idTypeArticle = 1;
    private String nomTypeArticle = "General";

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
                    JSONParser j = new JSONParser();
                    Map<String, Object> articles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) articles.get("root");
                    for (Map<String, Object> obj : list) {
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
                org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(result);
                    String titre = (String) jsonObject.getString("titre");
                    //Id
                    int id = jsonObject.getInt("id");
                    //Auteur
                    org.json.JSONObject auteurArticle = jsonObject.getJSONObject("utilisateur");
                    int idAuteur = auteurArticle.getInt("id");
                    //Type Article
                    org.json.JSONObject typeArticle = jsonObject.getJSONObject("type");
                    int idType = typeArticle.getInt("id");
                    //Date Article
                    org.json.JSONObject dateArticle = jsonObject.getJSONObject("dateHeure");
                    int dateHeureInt = dateArticle.getInt("timestamp");
                    Date dateArticleDate = new Date(Long.parseLong(String.valueOf(dateHeureInt)));

                    articleSpecifique.setId((int) id);
                    articleSpecifique.setDescription(jsonObject.getString("description"));
                    articleSpecifique.setTexte(jsonObject.getString("texte"));
                    articleSpecifique.setTitre(titre);
                    articleSpecifique.setPhotoArticle(jsonObject.getString("photoArticle"));
                    articleSpecifique.setUtilisateur(idAuteur);
                    articleSpecifique.setType(idType);
                    articleSpecifique.setDateHeure(dateArticleDate);
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return articleSpecifique;
    }

    @Override
    public String getTypeArticle(int idArticle) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "articles/article_api/" + idArticle);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                String result = "";
                String typeArticleFromJson = "";
                result = new String(con.getResponseData());
                org.json.JSONObject jsonObject;
                try {
                    jsonObject = new org.json.JSONObject(result);
                    //Auteur
                    org.json.JSONObject typeArticleJson = jsonObject.getJSONObject("type");
                    typeArticleFromJson = typeArticleJson.getString("nom");
                    typeArticle = typeArticleFromJson;
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return typeArticle;
    }

    @Override
    public void ajouterArticle(Article nouvelArticle) {
        String url = MoezUtils.urlWebService + "articles/ajouter_article_api/" + MoezUtils.getIdUserConnecte();
        //ConnectionRequest cr = new ConnectionRequest(url);
        MultipartRequest cr = new MultipartRequest();
        cr.setUrl(url);
        cr.setPost(true);
        System.out.println("PATH RECEIVED : " + nouvelArticle.getPhotoArticle());
        try {
            cr.addArgumentNoEncoding("description", nouvelArticle.getDescription());
            cr.addArgumentNoEncoding("titre", nouvelArticle.getTitre());
            cr.addArgumentNoEncoding("texte", nouvelArticle.getTexte());
            cr.addArgumentNoEncoding("type", String.valueOf(nouvelArticle.getType()));
            cr.addData("photo", nouvelArticle.getPhotoArticle(), "text/plain");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    String res = new String(cr.getResponseData(), "utf-8");
                    System.out.println("RESPONSE FROM SERVER : " + res);
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(cr);
    }

    @Override
    public ArrayList<String> getListeNomsTypesArticles() {
        ArrayList<String> listeNomsTypesArticles = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "articles/lister_types_articles_api");
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> typesArticles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) typesArticles.get("root");
                    for (Map<String, Object> obj : list) {
                        //Type Article
                        listeNomsTypesArticles.add(obj.get("nom").toString());
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listeNomsTypesArticles;
    }

    @Override
    public int getIdOfTypeArticle(String nomType) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "articles/get_id_typearticle_specifique_api/" + nomType);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> typesArticles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) typesArticles.get("root");
                    for (Map<String, Object> obj : list) {
                        //Type Article
                        idTypeArticle = (int) Math.round((double) obj.get("id"));
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return idTypeArticle;
    }

    @Override
    public List<Article> rechercheArticles(String motCle) {
        ArrayList<Article> listeArticles = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "articles/search_article_nom_api/" + motCle);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> articles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) articles.get("root");
                    System.out.println("RECHERCHE API / " + list);
                    for (Map<String, Object> obj : list) {
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
                        //JSONObject dateArticle = new JSONObject((Map) obj.get("dateHeure"));
                        //int dateHeureInt = (int) Math.round((double) dateArticle.get("timestamp"));
                        //Date dateArticleDate = new Date(Long.parseLong(String.valueOf(dateHeureInt)));

                        article.setId((int) id);
                        article.setDescription(obj.get("description").toString());
                        article.setTexte(obj.get("texte").toString());
                        article.setTitre(obj.get("titre").toString());
                        article.setPhotoArticle(obj.get("photoArticle").toString());
                        article.setUtilisateur(idAuteur);
                        article.setType(idType);
                        //article.setDateHeure(dateArticleDate);
                        article.setDateHeure(new Date());
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
    public List<Article> getListeMesArticles(int idUtilisateur) {
        ArrayList<Article> listeArticles = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "articles/mes_articles_api/" + idUtilisateur);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> articles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) articles.get("root");
                    for (Map<String, Object> obj : list) {
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
    public String getNomOfTypeArticle(int idType) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "articles/search_type_article_id_api/" + idType);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> typesArticles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) typesArticles.get("root");
                    System.out.println("LIST : " + typesArticles);
                    //Type Article
                    nomTypeArticle = (String) typesArticles.get("nom");
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        System.out.println("Found it by id : " + nomTypeArticle);
        return nomTypeArticle;
    }

    @Override
    public void modifierArticle(int id, Article articleModifie) {
        System.out.println("Article modifié dans le service : " + articleModifie);
        String url = MoezUtils.urlWebService + "articles/modifier_article_api/" + id;
        //ConnectionRequest cr = new ConnectionRequest(url);
        MultipartRequest cr = new MultipartRequest();
        cr.setUrl(url);
        cr.setPost(true);
        cr.addArgumentNoEncoding("description", articleModifie.getDescription());
        cr.addArgumentNoEncoding("titre", articleModifie.getTitre());
        cr.addArgumentNoEncoding("texte", articleModifie.getTexte());
        cr.addArgumentNoEncoding("type", String.valueOf(articleModifie.getType()));
        System.out.println("PATH RECEIVED : " + articleModifie.getPhotoArticle());
        try {
            cr.addData("photo", articleModifie.getPhotoArticle(), "text/plain");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    String res = new String(cr.getResponseData(), "utf-8");
                    System.out.println("RESPONSE FROM SERVER : " + res);
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(cr);
    }

    @Override
    public void supprimerArticle(int id) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "articles/supprimer_article_api/" + id);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                System.out.println("Article supprimé." + id);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
    }
}
