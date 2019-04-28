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
import java.util.Date;
import org.json.JSONException;

/**
 *
 * @author Moez
 */
public class MoezUtils {

    public static final String urlWebService = "http://127.0.0.1/ThinkLance/web/app_dev.php/";
    public static final String urlPhotosArticle = "http://127.0.0.1/ThinkLance/web/";
    public static final String staticUrlImageArticlePlaceHolder = "file:///C:/wamp64/www/ThinkLance/web/uploads/articlesPhotos/ImageArticlePlaceHolder.png";
    private ArticlesService artServ = new ArticlesService();
    private String nomUser = "";

    public MoezUtils() {
    }

    public String getUrlWebService() {
        return urlWebService;
    }

    private static int getUserMoezConnecte() {
        return 14;
    }

    public static int getIdUserConnecte() {
        return getUserMoezConnecte();
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
}
