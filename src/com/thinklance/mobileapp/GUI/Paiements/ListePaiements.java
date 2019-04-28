/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Paiements;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.ToastBar;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.thinklance.mobileapp.Entities.Paiement;
import com.thinklance.mobileapp.GUI.Articles.ListeArticles;
import com.thinklance.mobileapp.GUI.Articles.MaListeArticles;
import com.thinklance.mobileapp.Services.Implementation.PaiementsService;
import com.thinklance.mobileapp.Utils.MoezUtils;

/**
 *
 * @author Moez
 */
public class ListePaiements {

    private Form formListePaiements = new Form("Mes Paiements", new BoxLayout(BoxLayout.Y_AXIS));
    private Container cnListe = new Container(new BoxLayout(BoxLayout.Y_AXIS));

    public ListePaiements() {
        //Ajout du bouton de paiement
        FloatingActionButton fabAjout = FloatingActionButton.createFAB(FontImage.MATERIAL_MONEY_OFF);
        fabAjout.addActionListener(e -> {
            FormPaiement formPaiement = new FormPaiement();
            formPaiement.getFormListeArticles().show();
        });
        fabAjout.bindFabToContainer(formListePaiements.getContentPane());
        //Recuperation des données des paiements selon le type du user connecté : 
        if (MoezUtils.getTypeUserConnecte().equals("ROLE_EMPLOYEUR")) {
            afficherDonneesEmployeur();
        } else if (MoezUtils.getTypeUserConnecte().equals("ROLE_FREELANCER")) {
            afficherDonneesFreelancer();
        }
        //Refresh
        formListePaiements.getContentPane().addPullToRefresh(() -> {
            if (MoezUtils.getTypeUserConnecte().equals("ROLE_EMPLOYEUR")) {
                afficherDonneesEmployeur();
            } else if (MoezUtils.getTypeUserConnecte().equals("ROLE_FREELANCER")) {
                afficherDonneesFreelancer();
            }
            ToastBar.showErrorMessage("Liste des articles rafraichie.");
        });
        formListePaiements.add(cnListe);
        //Navigation
        formListePaiements.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeArticles listeArticles = new ListeArticles();
                listeArticles.getFormListeArticles().show();
            }
        });

        formListePaiements.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MaListeArticles maListeArticles = new MaListeArticles();
                maListeArticles.getFormMaListeArticles().show();
            }
        });

        formListePaiements.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Paiements");
            }
        });

        //Recherche
        formListePaiements.getToolbar().addSearchCommand(e -> {
            String text = (String) e.getSource();
            if (text == null || text.length() == 0) {
                // clear search
                System.out.println("Affichage standard des articles");
                if (MoezUtils.getTypeUserConnecte().equals("ROLE_EMPLOYEUR")) {
                    afficherDonneesEmployeur();
                } else if (MoezUtils.getTypeUserConnecte().equals("ROLE_FREELANCER")) {
                    afficherDonneesFreelancer();
                }
//                for (Component cmp : formListeArticles.getContentPane()) {
//                    cmp.setHidden(false);
//                    cmp.setVisible(true);
//                }
                formListePaiements.getContentPane().animateLayout(150);
            } else {
                text = text.toLowerCase();
                afficherResultatRecherche(text);
                formListePaiements.getContentPane().animateLayout(150);
            }
        }, 4);
    }

    private void afficherDonneesEmployeur() {
         cnListe.removeAll();
        PaiementsService payServ = new PaiementsService();
        for (Paiement pay : payServ.getPaiementsEmployeur()) {
            Container articleItemContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Container titreEtDescriptionItemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));

            articleItemContainer.add(imageViewer);
            titreEtDescriptionItemContainer.add(pay.getTitre());
            titreEtDescriptionItemContainer.add(MoezUtils.raccourcirString(pay.getDescription()));
            articleItemContainer.add(titreEtDescriptionItemContainer);
            imageViewer.addPointerReleasedListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    Form formLireArticle = new Form(pay.getTitre());
                    //Affichage du contenu de l'article
                    //Affichage de l'image
                    try {
                        String urlImage = MoezUtils.urlPhotosArticle + "/uploads/articlesPhotos/" + pay.getPhotoArticle();
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
                    Label lblAuteur = new Label("Auteur : " + mu.getNomUserForArticles(pay.getId()));
                    cnDetails.add(lblAuteur);
                    //Ajout de la date
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    cnDetails.add(new Label("à " + format.format(pay.getDateHeure())));
                    //Ajout du type
                    Label lblTypeArticle = new Label(payServ.getTypeArticle(pay.getId()));
                    switch (pay.getType() % 2) {
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
                    contenuContainer.add(new SpanLabel(pay.getTexte()));
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

    private void afficherDonneesFreelancer() {
        
    }

    public Form getFormListePaiements() {
        return formListePaiements;
    }

    public void setFormListePaiements(Form formListePaiements) {
        this.formListePaiements = formListePaiements;
    }

    private void afficherResultatRecherche(String text) {
        //Implement me
        System.out.println("à implementer");
    }
}
