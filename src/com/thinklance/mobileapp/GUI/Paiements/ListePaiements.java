/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Paiements;

import com.codename1.charts.util.ColorUtil;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.ImageViewer;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
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
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.thinklance.mobileapp.Entities.Paiement;
import com.thinklance.mobileapp.GUI.Actualites.ListeActualites;
import com.thinklance.mobileapp.GUI.Articles.ListeArticles;
import com.thinklance.mobileapp.GUI.Articles.MaListeArticles;
import com.thinklance.mobileapp.Services.Implementation.PaiementsService;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Moez
 */
public class ListePaiements {

    private Form formListePaiements = new Form("Mes Paiements", new BoxLayout(BoxLayout.Y_AXIS));
    private Container cnListe = new Container(new BoxLayout(BoxLayout.Y_AXIS));
    private ImageViewer imageViewer = null;
    private Image image;
    private EncodedImage enc;

    public ListePaiements() {
        //Ajout du bouton de paiement
        if (MoezUtils.getTypeUserConnecte().equals("ROLE_EMPLOYEUR")) {
            FloatingActionButton fabAjout = FloatingActionButton.createFAB(FontImage.MATERIAL_DONE);
            fabAjout.addActionListener(e -> {
                FormPaiement formPaiement = new FormPaiement();
                formPaiement.setIdProjetConcerne(1);
                formPaiement.getFormPaiement().show();
            });
            fabAjout.bindFabToContainer(formListePaiements.getContentPane());
        } else if (MoezUtils.getTypeUserConnecte().equals("ROLE_FREELANCER")) {
            //Ne rien faire
        }
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
            ToastBar.showErrorMessage("Liste des paiements rafraichie.");
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
                ListePaiements listePaiements = new ListePaiements();
                listePaiements.getFormListePaiements().show();
            }
        });

        formListePaiements.getToolbar().addCommandToSideMenu("Actualites", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeActualites listAcu = new ListeActualites();
                listAcu.getFormListeActualites().show();
            }
        });

        //Recherche
        formListePaiements.getToolbar().addSearchCommand(e -> {
            String text = (String) e.getSource();
            if (text == null || text.length() == 0) {
                // clear search
                System.out.println("Affichage standard des paiements");
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
            Container paiementItemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            Container buttonsContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Container idDateMontantPaiement = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            buttonsContainer.getAllStyles().setMarginLeft(37);
            Label lSeparator = new Label("_____________________________________________________________________");
            lSeparator.setUIID("Separator");
            //Boutons
            Button btnRembouser = new Button("Rembour.");
            Style sRembou = UIManager.getInstance().getComponentStyle("Button");
            Image iconRembour = FontImage.createMaterial(FontImage.MATERIAL_MONEY_OFF, sRembou);
            btnRembouser.setIcon(iconRembour);
            Button btnFacture = new Button("Facture");
            Style sFacture = UIManager.getInstance().getComponentStyle("Button");
            Image iconFacture = FontImage.createMaterial(FontImage.MATERIAL_ARCHIVE, sFacture);
            btnFacture.setIcon(iconFacture);

            btnRembouser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    rembourser(pay.getIdPaiement(), pay);
                }
            });

            btnFacture.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    afficherFacture(pay.getIdPaiement());
                }
            });

            buttonsContainer.add(btnRembouser);
            buttonsContainer.add(btnFacture);
            //Données paiement
            //Ajout de la photo
            Label lblIdPaiement = new Label(pay.getIdPaiement());
            idDateMontantPaiement.add(lblIdPaiement);
            idDateMontantPaiement.add(new Label("Montant : " + Double.toString(pay.getMontant()) + " €"));
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            idDateMontantPaiement.add(new Label("Date : " + format.format(pay.getDateHeure())));

            lblIdPaiement.addPointerReleasedListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    //Affichage des détails du paiement
                    Form formDetailsPaiement = new Form("Détails");
                    //Navigation
                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            ListeArticles listeArticles = new ListeArticles();
                            listeArticles.getFormListeArticles().show();
                        }
                    });

                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            MaListeArticles maListeArticles = new MaListeArticles();
                            maListeArticles.getFormMaListeArticles().show();
                        }
                    });

                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            ListePaiements listePaiements = new ListePaiements();
                            listePaiements.getFormListePaiements().show();
                        }
                    });
                    formDetailsPaiement.getToolbar().addCommandToRightBar("Retour", null, e -> {
                        ListePaiements lsp = new ListePaiements();
                        lsp.getFormListePaiements().show();
                    });
                    //End Navigation
                    //Ajout du bouton de remboursement
                    FloatingActionButton fabAjout = FloatingActionButton.createFAB(FontImage.MATERIAL_MONEY_OFF);
                    fabAjout.addActionListener(e -> {
                        rembourser(pay.getIdPaiement(), pay);
                    });
                    fabAjout.bindFabToContainer(formDetailsPaiement.getContentPane());
                    Container cnDetails = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                    //Ajout de la photo
                    try {
                        String urlImage = "/IconePaiementsMoez.png";
                        enc = EncodedImage.create("/load.png");
                        image = Image.createImage(urlImage);
                        imageViewer = new ImageViewer(image.scaled(120, 120));
                        imageViewer.getAllStyles().setMarginLeft(70);
                        cnDetails.add(imageViewer);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    //Affichage de l'id
                    cnDetails.add(new Label("ID Paiement : "));
                    cnDetails.add(new Label(pay.getIdPaiement()));
                    //Affichage du montant
                    cnDetails.add(new Label("Montant : " + pay.getMontant() + " €"));
                    //Affichage de la date
                    cnDetails.add(new Label("Date : " + format.format(pay.getDateHeure())));
                    //Affichage de l'autre utilisateur
                    MoezUtils mu = new MoezUtils();
                    Label lblAutreUtilisateur = new Label("Freelancer : " + mu.getNomUserFromId(pay.getIdFreelancer()));
                    lblAutreUtilisateur.addPointerReleasedListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            MoezUtils.naviguerVersFreelancer(pay.getIdFreelancer());
                        }
                    });
                    cnDetails.add(lblAutreUtilisateur);
                    //Affichage du projet utilisateur
                    Label lblProjet = new Label("Projet : " + mu.getNomProjetFromId(pay.getProjet()));
                    lblProjet.addPointerReleasedListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            MoezUtils.naviguerVersProjet(pay.getProjet());
                        }
                    });
                    cnDetails.add(lblProjet);

                    //Navigation
                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            ListeArticles listeArticles = new ListeArticles();
                            listeArticles.getFormListeArticles().show();
                        }
                    });

                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            MaListeArticles maListeArticles = new MaListeArticles();
                            maListeArticles.getFormMaListeArticles().show();
                        }
                    });

                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            ListePaiements listePaiements = new ListePaiements();
                            listePaiements.getFormListePaiements().show();
                        }
                    });
                    formDetailsPaiement.getToolbar().addCommandToRightBar("Retour", null, e -> {
                        ListePaiements lsp = new ListePaiements();
                        lsp.getFormListePaiements().show();
                    });
                    //End Navigation
                    formDetailsPaiement.add(cnDetails);
                    formDetailsPaiement.show();
                }
            });

            paiementItemContainer.add(idDateMontantPaiement);
            paiementItemContainer.add(buttonsContainer);
            paiementItemContainer.add(lSeparator);
            cnListe.add(paiementItemContainer);
        }
    }

    private void afficherDonneesFreelancer() {
        cnListe.removeAll();
        PaiementsService payServ = new PaiementsService();
        for (Paiement pay : payServ.getPaiementsFreelancer()) {
            Container paiementItemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            Container buttonsContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Container idDateMontantPaiement = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            buttonsContainer.getAllStyles().setMarginLeft(110);
            Label lSeparator = new Label("_____________________________________________________________________");
            lSeparator.setUIID("Separator");
            //Boutons
            Button btnFacture = new Button("Facture");
            Style sFacture = UIManager.getInstance().getComponentStyle("Button");
            Image iconFacture = FontImage.createMaterial(FontImage.MATERIAL_ARCHIVE, sFacture);
            btnFacture.setIcon(iconFacture);

            btnFacture.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    afficherFacture(pay.getIdPaiement());
                }
            });

            buttonsContainer.add(btnFacture);
            //Données paiement
            //Ajout de la photo
            Label lblIdPaiement = new Label(pay.getIdPaiement());
            idDateMontantPaiement.add(lblIdPaiement);
            idDateMontantPaiement.add(new Label("Montant : " + Double.toString(pay.getMontant()) + " €"));
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            idDateMontantPaiement.add(new Label("Date : " + format.format(pay.getDateHeure())));

            lblIdPaiement.addPointerReleasedListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    //Affichage des détails du paiement
                    Form formDetailsPaiement = new Form("Détails");
                    //Navigation
                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            ListeArticles listeArticles = new ListeArticles();
                            listeArticles.getFormListeArticles().show();
                        }
                    });

                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            MaListeArticles maListeArticles = new MaListeArticles();
                            maListeArticles.getFormMaListeArticles().show();
                        }
                    });

                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            ListePaiements listePaiements = new ListePaiements();
                            listePaiements.getFormListePaiements().show();
                        }
                    });
                    formDetailsPaiement.getToolbar().addCommandToRightBar("Retour", null, e -> {
                        ListePaiements lsp = new ListePaiements();
                        lsp.getFormListePaiements().show();
                    });
                    //End Navigation
                    //Ajout du bouton de remboursement
                    FloatingActionButton fabAjout = FloatingActionButton.createFAB(FontImage.MATERIAL_MONEY_OFF);
                    fabAjout.addActionListener(e -> {
                        rembourser(pay.getIdPaiement(), pay);
                    });
                    fabAjout.bindFabToContainer(formDetailsPaiement.getContentPane());
                    Container cnDetails = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                    //Ajout de la photo
                    try {
                        String urlImage = "/IconePaiementsMoez.png";
                        enc = EncodedImage.create("/load.png");
                        image = Image.createImage(urlImage);
                        imageViewer = new ImageViewer(image.scaled(120, 120));
                        imageViewer.getAllStyles().setMarginLeft(70);
                        cnDetails.add(imageViewer);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    //Affichage de l'id
                    cnDetails.add(new Label("ID Paiement : "));
                    cnDetails.add(new Label(pay.getIdPaiement()));
                    //Affichage du montant
                    cnDetails.add(new Label("Montant : " + pay.getMontant() + " €"));
                    //Affichage de la date
                    cnDetails.add(new Label("Date : " + format.format(pay.getDateHeure())));
                    //Affichage de l'autre utilisateur
                    MoezUtils mu = new MoezUtils();
                    Label lblAutreUtilisateur = new Label("Employeur : " + mu.getNomUserFromId(pay.getIdEmployeur()));
                    lblAutreUtilisateur.addPointerReleasedListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            MoezUtils.naviguerVersFreelancer(pay.getIdFreelancer());
                        }
                    });
                    cnDetails.add(lblAutreUtilisateur);
                    //Affichage du projet utilisateur
                    Label lblProjet = new Label("Projet : " + mu.getNomProjetFromId(pay.getProjet()));
                    lblProjet.addPointerReleasedListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            MoezUtils.naviguerVersProjet(pay.getProjet());
                        }
                    });
                    cnDetails.add(lblProjet);

                    //Navigation
                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            ListeArticles listeArticles = new ListeArticles();
                            listeArticles.getFormListeArticles().show();
                        }
                    });

                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            MaListeArticles maListeArticles = new MaListeArticles();
                            maListeArticles.getFormMaListeArticles().show();
                        }
                    });

                    formDetailsPaiement.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            ListePaiements listePaiements = new ListePaiements();
                            listePaiements.getFormListePaiements().show();
                        }
                    });
                    formDetailsPaiement.getToolbar().addCommandToRightBar("Retour", null, e -> {
                        ListePaiements lsp = new ListePaiements();
                        lsp.getFormListePaiements().show();
                    });
                    //End Navigation
                    formDetailsPaiement.add(cnDetails);
                    formDetailsPaiement.show();
                }
            });

            paiementItemContainer.add(idDateMontantPaiement);
            paiementItemContainer.add(buttonsContainer);
            paiementItemContainer.add(lSeparator);
            cnListe.add(paiementItemContainer);
        }
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

    private void rembourser(String idPaiement, Paiement paiement) {
        PaiementsService payServ = new PaiementsService();
        if (Dialog.show("Confirmer remboursement", "Voulez-vous vraiment rembourser ce paiement ?", "OK", "Annuler")) {
            payServ.rembourserPaiement(idPaiement, paiement);
            afficherDonneesEmployeur();
        } else {
            ToastBar.showErrorMessage("Action annulée.");
        }
    }

    public void afficherFacture(String idPaiemengt) {
        System.out.println("Afficher facture method a recu : " + idPaiemengt);
        FactureForm fForm = new FactureForm(idPaiemengt);
        fForm.setIdFactureConcernee(idPaiemengt);
        fForm.getFactureForm().show();
    }
}
