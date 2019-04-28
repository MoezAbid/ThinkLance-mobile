/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Services.Interfaces;

import com.thinklance.mobileapp.Entities.Article;
import com.thinklance.mobileapp.Entities.TypeArticle;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Moez
 */
public interface IArticles {

    public void ajouterArticle(Article nouvelArticle);

    public void modifierArticle(int id, Article articleModifie);

    public void supprimerArticle(int id);

    public List<Article> getListeAllArticles();
    
    public List<Article> getListeMesArticles(int idUtilisateur);

    public Article getArticleSpecifique(int idArticle);

    public List<Article> rechercheArticles(String motCle);

    //public List<Article> rechercheDansMesArticles(String motCle, int idUtilisateur);
    public String getTypeArticle(int idArticle);

    public ArrayList<String> getListeNomsTypesArticles();

    public int getIdOfTypeArticle(String nomType);

    public String getNomOfTypeArticle(int idType);

}
