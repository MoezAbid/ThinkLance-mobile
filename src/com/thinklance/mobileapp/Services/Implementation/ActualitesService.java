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
import com.thinklance.mobileapp.Entities.Actualite;
import com.thinklance.mobileapp.Services.Interfaces.IActualites;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Moez
 */
public class ActualitesService implements IActualites {

    //2019-04-10T13:00:40Z
    private SimpleDateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private String result = "";
    private String urlImageArticlePlaceHolder = MoezUtils.urlPhotosArticle + "uploads/articlesPhotos/" +"ImageArticlePlaceHolder.png";

    @Override
    public ArrayList<Actualite> getTechnologies() {
        ArrayList<Actualite> listeDesActualites = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "actualites/tech_api");
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> articles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) articles.get("root");
                    for (Map<String, Object> obj : list) {
                        Actualite actualite = new Actualite();
                        actualite.setAuteur(obj.get("author").toString());
                        try {
                            actualite.setDatePublication(jsonDateFormat.parse(obj.get("publishedAt").toString()));
                        } catch (ParseException ex) {
                            System.out.println(ex.getMessage());
                        }
                        actualite.setDescription(obj.get("publishedAt").toString());
                        //actualite.setImage(obj.get("urlToImage").toString());
                        actualite.setImage(urlImageArticlePlaceHolder);
                        actualite.setTitre(obj.get("title").toString());
                        actualite.setUrlToAcualite(obj.get("url").toString());

                        listeDesActualites.add(actualite);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listeDesActualites;
    }

    @Override
    public ArrayList<Actualite> getProgrammationEtApplications() {
        ArrayList<Actualite> listeDesActualites = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "actualites/prog_api");
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> articles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) articles.get("root");
                    for (Map<String, Object> obj : list) {
                        Actualite actualite = new Actualite();
                        actualite.setAuteur(obj.get("author").toString());
                        try {
                            actualite.setDatePublication(jsonDateFormat.parse(obj.get("publishedAt").toString()));
                        } catch (ParseException ex) {
                            System.out.println(ex.getMessage());
                        }
                        actualite.setDescription(obj.get("publishedAt").toString());
                        //actualite.setImage(obj.get("urlToImage").toString());
                        actualite.setImage(urlImageArticlePlaceHolder);
                        actualite.setTitre(obj.get("title").toString());
                        actualite.setUrlToAcualite(obj.get("url").toString());

                        listeDesActualites.add(actualite);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listeDesActualites;
    }

    @Override
    public ArrayList<Actualite> getFreelanceActualites() {
        ArrayList<Actualite> listeDesActualites = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl(MoezUtils.urlWebService + "actualites/free_api");
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                result = new String(con.getResponseData());
                try {
                    JSONParser j = new JSONParser();
                    Map<String, Object> articles = j.parseJSON(new CharArrayReader(result.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) articles.get("root");
                    for (Map<String, Object> obj : list) {
                        Actualite actualite = new Actualite();
                        actualite.setAuteur(obj.get("author").toString());
                        try {
                            actualite.setDatePublication(jsonDateFormat.parse(obj.get("publishedAt").toString()));
                        } catch (ParseException ex) {
                            System.out.println(ex.getMessage());
                        }
                        actualite.setDescription(obj.get("publishedAt").toString());
                        //actualite.setImage(obj.get("urlToImage").toString());
                        actualite.setImage(urlImageArticlePlaceHolder);
                        actualite.setTitre(obj.get("title").toString());
                        actualite.setUrlToAcualite(obj.get("url").toString());

                        listeDesActualites.add(actualite);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listeDesActualites;
    }

}
