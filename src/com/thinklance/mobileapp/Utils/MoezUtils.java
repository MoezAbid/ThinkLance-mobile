/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Utils;

/**
 *
 * @author Moez
 */
public class MoezUtils {

    public static final String urlWebService = "http://127.0.0.1/ThinkLance/web/app_dev.php/";

    public MoezUtils() {
    }

    public String getUrlWebService() {
        return urlWebService;
    }

    public int getUserMoezConnecte() {
        return 14;
    }

    public int getIdUserConnecte() {
        return getUserMoezConnecte();
    }

    public static String raccourcirString(String s) {
        if (s.length() > 30) {
            s = s.substring(0, 10) + "...";
        }
        return s;
    }

    public static String getNomUserForArticles(int idArticle) {
        String nomUser = "";
            
        return nomUser;
    }
}
