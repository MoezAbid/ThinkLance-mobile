/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Articles;

import com.codename1.charts.util.ColorUtil;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.ImageViewer;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.Component;
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
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.thinklance.mobileapp.Entities.Article;
import com.thinklance.mobileapp.GUI.Actualites.ListeActualites;
import com.thinklance.mobileapp.GUI.Paiements.ListePaiements;
import com.thinklance.mobileapp.Services.Implementation.ArticlesService;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
    private ArticlesService artServ = new ArticlesService();

    public ListeArticles() {
        //Ajout du bouton d'ajout
        FloatingActionButton fabAjout = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        fabAjout.addActionListener(e -> {
            AjouterArticle ajouterArticle = new AjouterArticle();
            ajouterArticle.getFormAjouterArticle().show();
        });
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
                ListePaiements listePaiements = new ListePaiements();
                listePaiements.getFormListePaiements().show();
            }
        });
        
        formListeArticles.getToolbar().addCommandToSideMenu("Actualites", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeActualites listAcu = new ListeActualites();
                listAcu.getFormListeActualites().show();
            }
        });

        //Recherche
        formListeArticles.getToolbar().addSearchCommand(e -> {
            String text = (String) e.getSource();
            if (text == null || text.length() == 0) {
                // clear search
                System.out.println("Affichage standard des articles");
                afficherDonnees();
//                for (Component cmp : formListeArticles.getContentPane()) {
//                    cmp.setHidden(false);
//                    cmp.setVisible(true);
//                }
                formListeArticles.getContentPane().animateLayout(150);
            } else {
                text = text.toLowerCase();
                afficherResultatRecherche(text);
                formListeArticles.getContentPane().animateLayout(150);
            }
        }, 4);
    }

    public Form getFormListeArticles() {
        return formListeArticles;
    }

    public void setFormListeArticles(Form formListeArticles) {
        this.formListeArticles = formListeArticles;
    }

    private void afficherDonnees() {
        cnListe.removeAll();
        ArticlesService artServ = new ArticlesService();
        for (Article art : artServ.getListeAllArticles()) {
            Container articleItemContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Container titreEtDescriptionItemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            try {
                String urlImage = MoezUtils.urlPhotosArticle + "uploads/articlesPhotos/" + art.getPhotoArticle();
                enc = EncodedImage.create("/load.png");
                image = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
                imageViewer = new ImageViewer(image.scaled(80, 60));
                imageViewer.getAllStyles().setMarginLeft(6);
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
                    Form formLireArticle = new Form(art.getTitre());
                    //Affichage du contenu de l'article
                    //Affichage de l'image
                    try {
                        String urlImage = MoezUtils.urlPhotosArticle + "/uploads/articlesPhotos/" + art.getPhotoArticle();
                        enc = EncodedImage.create("/load.png");
                        image = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
                        imageViewer = new ImageViewer(image.scaled(120, 120));
                        imageViewer.getAllStyles().setMarginLeft(13);
                        imageViewer.getAllStyles().setMarginTop(8);
                        formLireArticle.add(imageViewer);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    //Afficahge des détails
                    Container cnDetails = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                    //Ajout du nom de l'auteur
                    MoezUtils mu = new MoezUtils();
                    Label lblAuteur = new Label("Auteur : " + mu.getNomUserForArticles(art.getId()));
                    cnDetails.add(lblAuteur);
                    //Ajout de la date
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    cnDetails.add(new Label("à " + format.format(art.getDateHeure())));
                    //Ajout du type
                    Label lblTypeArticle = new Label(artServ.getTypeArticle(art.getId()));
                    switch (art.getType() % 2) {
                        case 0:
                            lblTypeArticle.getAllStyles().setFgColor(ColorUtil.GREEN);
                            break;
                        case 1:
                            lblTypeArticle.getAllStyles().setFgColor(ColorUtil.MAGENTA);
                            break;
                        default:
                            break;
                    }
                    cnDetails.add(lblTypeArticle);
                    Container contenuContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                    contenuContainer.add(new SpanLabel(art.getTexte()));
                    formLireArticle.add(cnDetails);
                    formLireArticle.add(contenuContainer);

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
                    formLireArticle.getToolbar().addCommandToRightBar("Retour", null, e -> {
                        ListeArticles lart = new ListeArticles();
                        lart.getFormListeArticles().show();
                    });
                    formLireArticle.show();
                }
            });
            cnListe.add(articleItemContainer);
        }
    }

    private void afficherResultatRecherche(String motCle) {
        cnListe.removeAll();
        ArticlesService artServ = new ArticlesService();
        for (Article art : artServ.rechercheArticles(motCle)) {
            Container articleItemContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Container titreEtDescriptionItemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            try {
                String urlImage = MoezUtils.urlPhotosArticle + "uploads/articlesPhotos/" + art.getPhotoArticle();
                enc = EncodedImage.create("/load.png");
                image = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
                imageViewer = new ImageViewer(image.scaled(80, 60));
                imageViewer.getAllStyles().setMarginLeft(6);
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
                    Form formLireArticle = new Form(art.getTitre());
                    //Affichage du contenu de l'article
                    //Affichage de l'image
                    try {
                        String urlImage = MoezUtils.urlPhotosArticle + "/uploads/articlesPhotos/" + art.getPhotoArticle();
                        enc = EncodedImage.create("/load.png");
                        image = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
                        imageViewer = new ImageViewer(image.scaled(120, 120));
                        imageViewer.getAllStyles().setMarginLeft(13);
                        imageViewer.getAllStyles().setMarginTop(8);
                        formLireArticle.add(imageViewer);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    //Afficahge des détails
                    Container cnDetails = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                    //Ajout du nom de l'auteur
                    MoezUtils mu = new MoezUtils();
                    Label lblAuteur = new Label("Auteur : " + mu.getNomUserForArticles(art.getId()));
                    cnDetails.add(lblAuteur);
                    //Ajout de la date
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    cnDetails.add(new Label("à " + format.format(art.getDateHeure())));
                    //Ajout du type
                    Label lblTypeArticle = new Label(artServ.getTypeArticle(art.getId()));
                    switch (art.getType() % 2) {
                        case 0:
                            lblTypeArticle.getAllStyles().setFgColor(ColorUtil.GREEN);
                            break;
                        case 1:
                            lblTypeArticle.getAllStyles().setFgColor(ColorUtil.MAGENTA);
                            break;
                        default:
                            break;
                    }
                    cnDetails.add(lblTypeArticle);
                    Container contenuContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                    contenuContainer.add(new SpanLabel(art.getTexte()));
                    formLireArticle.add(cnDetails);
                    formLireArticle.add(contenuContainer);

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
                    formLireArticle.getToolbar().addCommandToRightBar("Retour", null, e -> {
                        ListeArticles lart = new ListeArticles();
                        lart.getFormListeArticles().show();
                    });
                    formLireArticle.show();
                }
            });
            cnListe.add(articleItemContainer);
        }
    }
}
