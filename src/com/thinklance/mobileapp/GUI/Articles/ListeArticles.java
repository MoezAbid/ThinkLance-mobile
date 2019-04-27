/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Articles;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.thinklance.mobileapp.Entities.Article;
import com.thinklance.mobileapp.Services.Implementation.ArticlesService;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;

/**
 *
 * @author Moez
 */
public class ListeArticles {

    private Form formListeArticles = new Form("Articles", new BoxLayout(BoxLayout.Y_AXIS));
    private Container cnListe = new Container(new BoxLayout(BoxLayout.Y_AXIS));
    private ImageViewer imageViewer = null;
    private Image image;
    private EncodedImage enc;

    public ListeArticles() {
        //Ajout du bouton d'ajout
        FloatingActionButton fabAjout = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        fabAjout.addActionListener(e -> System.out.println("Bouton ajout cliqué"));
        fabAjout.bindFabToContainer(formListeArticles.getContentPane());
        //Recuperation des données articles : 
        afficherDonnees();
        //Refresh
        formListeArticles.getContentPane().addPullToRefresh(() -> {
            afficherDonnees();
            ToastBar.showErrorMessage("Liste des articles rafraichie.");
        });
        formListeArticles.add(cnListe);

        //Navigation
        formListeArticles.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeArticles listeArticles = new ListeArticles();
                listeArticles.getFormListeArticles().show();
            }
        });

        formListeArticles.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MaListeArticles maListeArticles = new MaListeArticles();
                maListeArticles.getFormMaListeArticles().show();
            }
        });

        formListeArticles.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Paiements");
            }
        });
    }

    public Form getFormListeArticles() {
        return formListeArticles;
    }

    public void setFormListeArticles(Form formListeArticles) {
        this.formListeArticles = formListeArticles;
    }

    private void afficherDonnees() {
        ArticlesService artServ = new ArticlesService();
        for (Article art : artServ.getListeAllArticles()) {
            Container articleItemContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Container titreEtDescriptionItemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            try {
                String urlImage = MoezUtils.urlWebService + "uploads/articlesPhotos/" + art.getPhotoArticle();
                enc = EncodedImage.create("/load.png");
                image = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
                imageViewer = new ImageViewer(image.scaled(80, 60));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            articleItemContainer.add(imageViewer);
            titreEtDescriptionItemContainer.add(art.getTitre());
            titreEtDescriptionItemContainer.add(MoezUtils.raccourcirString(art.getDescription()));
            articleItemContainer.add(titreEtDescriptionItemContainer);
            imageViewer.addPointerReleasedListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    Form formLireArticle = new Form();

                    //Affichage du contenu de l'article
                    //Affichage de l'image
                    try {
                        String urlImage = MoezUtils.urlWebService + "/uploads/articlesPhotos/" + art.getPhotoArticle();
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
//                    lireArticle.setArticleConcerne(art);
//                    lireArticle.getFormLireArticle();
                }
            });
            cnListe.add(articleItemContainer);
        }
    }

    public void afficherContenuArticle(Article articleConcerne) {
        //Affichage de l'image
        try {
            String urlImage = MoezUtils.urlWebService + "/uploads/articlesPhotos/" + articleConcerne.getPhotoArticle();
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
