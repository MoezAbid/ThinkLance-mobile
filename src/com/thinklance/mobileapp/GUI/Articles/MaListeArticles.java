/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Articles;

import com.codename1.charts.util.ColorUtil;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
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
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.thinklance.mobileapp.Entities.Article;
import com.thinklance.mobileapp.Services.Implementation.ArticlesService;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Moez
 */
public class MaListeArticles {
    
    private Form formListeArticles = new Form("Mes Articles", new BoxLayout(BoxLayout.Y_AXIS));
    private Container cnListe = new Container(new BoxLayout(BoxLayout.Y_AXIS));
    private ImageViewer imageViewer = null;
    private Image image;
    private EncodedImage enc;
    private ArticlesService artServ = new ArticlesService();
    
    public MaListeArticles() {
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
                System.out.println("Paiements");
            }
        });
    }
    
    public Form getFormMaListeArticles() {
        return formListeArticles;
    }
    
    public void setFormMaListeArticles(Form formMaListeArticles) {
        this.formListeArticles = formMaListeArticles;
    }
    
    private void afficherDonnees() {
        cnListe.removeAll();
        ArticlesService artServ = new ArticlesService();
        for (Article art : artServ.getListeAllArticles()) {
            Container articleItemContainerWithButtons = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            Container articleItemContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Container titreEtDescriptionItemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            Container buttonsContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Label lSeparator = new Label("_____________________________________________________________________");
            lSeparator.setUIID("Separator");
            buttonsContainer.getAllStyles().setMarginLeft(30);
            try {
                String urlImage = MoezUtils.urlPhotosArticle + "uploads/articlesPhotos/" + art.getPhotoArticle();
                enc = EncodedImage.create("/load.png");
                image = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
                imageViewer = new ImageViewer(image.scaled(80, 60));
                imageViewer.getAllStyles().setMarginLeft(6);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            Button modifierButton = new Button("Modifier");
            //Boutton choix de l'image de l'article
            Style sModif = UIManager.getInstance().getComponentStyle("Button");
            Image iconModif = FontImage.createMaterial(FontImage.MATERIAL_EDIT, sModif);
            modifierButton.setIcon(iconModif);
            modifierButton.getAllStyles().setFgColor(ColorUtil.WHITE);
            Button supprimerButton = new Button("Supprimer");
            Style sSuppr = UIManager.getInstance().getComponentStyle("Button");
            Image iconSupp = FontImage.createMaterial(FontImage.MATERIAL_DELETE, sSuppr);
            supprimerButton.setIcon(iconSupp);
            supprimerButton.getAllStyles().setFgColor(ColorUtil.WHITE);
            buttonsContainer.add(modifierButton);
            buttonsContainer.add(supprimerButton);
            //Image
            articleItemContainer.add(imageViewer);
            //Titre & Description
            titreEtDescriptionItemContainer.add(art.getTitre());
            titreEtDescriptionItemContainer.add(MoezUtils.raccourcirString(art.getDescription()));
            articleItemContainer.add(titreEtDescriptionItemContainer);
            //Boutons
            articleItemContainerWithButtons.add(articleItemContainer);
            articleItemContainerWithButtons.add(buttonsContainer);
            //Separator
            articleItemContainerWithButtons.add(lSeparator);
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
                        MaListeArticles lart = new MaListeArticles();
                        lart.getFormMaListeArticles().show();
                    });
                    formLireArticle.show();
                }
            });
            //Bouton modifier traitement
            modifierButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    ModifierArticle modifArtc = new ModifierArticle();
                    modifArtc.setIdArticleAModifier(art.getId());
                    modifArtc.setDescFieldTextField(art.getDescription());
                    modifArtc.setTexteTextField(art.getTexte());
                    modifArtc.setTitreTextField(art.getTitre());
                    modifArtc.setPhotoArticleUrl(art.getPhotoArticle());
                    modifArtc.setImagePreview(art.getPhotoArticle());
                    modifArtc.getFormModifierArticle().show();
                    modifArtc.setPickerDefault(artServ.getTypeArticle(art.getType()));
                }
            });
            //Bouton supprimer traitement
            supprimerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (Dialog.show("Confirmer suppression", "Voulez-vous vraiment supprimer cet article ?", "OK", "Annuler")) {
                        artServ.supprimerArticle(art.getId());
                        ToastBar.showErrorMessage("Article supprimé.");
                    } else {
                        ToastBar.showErrorMessage("Action annulée.");
                    }
                    afficherDonnees();
                }
            });
            cnListe.add(articleItemContainerWithButtons);
        }
    }
}
