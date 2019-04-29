/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Articles;

import com.codename1.components.ImageViewer;
import com.codename1.ui.Container;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.thinklance.mobileapp.Entities.Article;
import com.thinklance.mobileapp.GUI.Actualites.ListeActualites;
import com.thinklance.mobileapp.Services.Implementation.ArticlesService;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;

/**
 *
 * @author Moez
 */
public class LireArticle {

    private Article articleConcerne;
    private Form formLireArticle = new Form("Lire Article", new BoxLayout(BoxLayout.Y_AXIS));
    private ImageViewer imageViewer = null;
    private Image image;
    private EncodedImage enc;

    public LireArticle(Article art) {
        //this.formLireArticle.setTitle(this.articleConcerne.getTitre());
        this.articleConcerne = art;
        System.out.println("ARTICLE RECU : " + this.articleConcerne);
        //Contenu de l'article
        afficherContenuArticle();

        //Navigation
        formLireArticle.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeArticles listeArticles = new ListeArticles();
                listeArticles.getFormListeArticles().show();
            }
        });

        formLireArticle.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MaListeArticles maListeArticles = new MaListeArticles();
                maListeArticles.getFormMaListeArticles().show();
            }
        });
        
        formLireArticle.getToolbar().addCommandToSideMenu("Actualites", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeActualites listAcu = new ListeActualites();
                listAcu.getFormListeActualites().show();
            }
        });

        formLireArticle.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Paiements");
            }
        });
        ListeArticles listeArticles = new ListeArticles();
        formLireArticle.getToolbar().addCommandToOverflowMenu("Retour", null, e -> {
            listeArticles.getFormListeArticles().showBack();
        });
    }

//    public LireArticle(Article articleConcerne) {
//        this.articleConcerne = articleConcerne;
//    }

    public Article getArticleConcerne() {
        return articleConcerne;
    }

    public void setArticleConcerne(Article articleConcerne) {
        this.articleConcerne = articleConcerne;
    }

    public Form getFormLireArticle() {
        return formLireArticle;
    }

    public void setFormLireArticle(Form formLireArticle) {
        this.formLireArticle = formLireArticle;
    }

    public void afficherContenuArticle() {
        //Affichage de l'image
        try {
            String urlImage = MoezUtils.urlWebService + "/uploads/articlesPhotos/" + this.articleConcerne.getPhotoArticle();
            enc = EncodedImage.create("/load.png");
            image = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
            imageViewer = new ImageViewer(image.scaled(80, 60));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        //Afficahge des détails
        Container cnDetails = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        ArticlesService artServ = new ArticlesService();
        //cnDetails.add(new Label(artServ.getArticleSpecifique(this.articleConcerne.getId()).toString()));
        //cnDetails.add(new Label(MoezUtils.getNomUserForArticles(this.articleConcerne.getId())));
    }
}
