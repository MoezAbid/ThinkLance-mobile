/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Paiements;

import com.codename1.charts.util.ColorUtil;
import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.notifications.LocalNotification;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.layouts.BoxLayout;
import com.thinklance.mobileapp.GUI.Actualites.ListeActualites;
import com.thinklance.mobileapp.GUI.Articles.ListeArticles;
import com.thinklance.mobileapp.GUI.Articles.MaListeArticles;
import com.thinklance.mobileapp.Services.Implementation.PaiementsService;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;

/**
 *
 * @author Moez
 */
public class FormPaiement {

    private Form formPaiement = new Form("Payer projet", new BoxLayout(BoxLayout.Y_AXIS));
    private ImageViewer imageViewer = null;
    private Image image;
    private EncodedImage enc;
    private PaiementsService payServ = new PaiementsService();
    private int idProjetConcerne = 1;
    private TextField numeroCarteTextField = new TextField("");
    private TextField moisTextField = new TextField("");
    private TextField anneeTextField = new TextField("");
    private TextField cvcPasswordTextField = new TextField("");
    private SpanLabel lblMainPaiement = new SpanLabel("");
    private Button btnPayer = new Button("Payer");
    private MoezUtils mu = new MoezUtils();

    public FormPaiement() {
        setMainLabelText();
        //Navigation
        formPaiement.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeArticles listeArticles = new ListeArticles();
                listeArticles.getFormListeArticles().show();
            }
        });

        formPaiement.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MaListeArticles maListeArticles = new MaListeArticles();
                maListeArticles.getFormMaListeArticles().show();
            }
        });

        formPaiement.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListePaiements listePaiements = new ListePaiements();
                listePaiements.getFormListePaiements().show();
            }
        });

        formPaiement.getToolbar().addCommandToSideMenu("Actualites", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeActualites listAcu = new ListeActualites();
                listAcu.getFormListeActualites().show();
            }
        });

        formPaiement.getToolbar().addCommandToRightBar("Retour", null, e -> {
            ListeArticles lart = new ListeArticles();
            lart.getFormListeArticles().show();
        });

        //TextFields Contraintes
        this.cvcPasswordTextField.setConstraint(TextField.PASSWORD | TextField.NUMERIC);
        this.numeroCarteTextField.setConstraint(TextField.NUMERIC);
        this.moisTextField.setConstraint(TextField.NUMERIC);
        this.anneeTextField.setConstraint(TextField.NUMERIC);
        //TextFields Hints
        this.cvcPasswordTextField.setHint("CVC : 456");
        this.numeroCarteTextField.setHint("Num : 4560 3659 1458 1667");
        this.moisTextField.setHint("MM : 09");
        this.anneeTextField.setHint("YY : 20");

        //Chargement de l'image par défaut
        try {
            String urlImage = "/PaiementEnUnClick.png";
            enc = EncodedImage.create("/load.png");
            image = Image.createImage(urlImage);
            imageViewer = new ImageViewer(image.scaled(120, 120));
            imageViewer.getAllStyles().setMarginLeft(45);
            formPaiement.add(imageViewer);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        //End Chargement de l'image par défaut

        btnPayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String numCarte = numeroCarteTextField.getText();
                String moisCarte = moisTextField.getText();
                String anneeCarte = anneeTextField.getText();
                String cvcCarte = cvcPasswordTextField.getText();
                if (numCarte.equals("") || moisCarte.equals("") || anneeCarte.equals("") || cvcCarte.equals("")) {
                    Dialog.show("Champs invalides", "Veuillez vérifier la validité des coordonnées de la carte. Les champs doivent être correctement remplis.", "OK", null);
                } else {
                    System.out.println("Nombre missions de l'utilisateur : " + mu.getNombreMissionsDeLemployeurDuProjet(idProjetConcerne));
                    System.out.println("Prix du projet concerne : " + mu.getPrixDuProjet(idProjetConcerne));
                    System.out.println("Titre du projet concerne : " + mu.getNomProjetFromId(idProjetConcerne));
                    confirmerPaiement();
                    Dialog.show("Succés de l'opération", "Votre paiement a été effectué correctement, vérifiez votre email.", "OK", null);
                    LocalNotification ln = new LocalNotification();
                    ln.setId("LnMessage");
                    ln.setAlertTitle("Welcome");
                    ln.setAlertBody("Thanks for arriving!");
                    Display.getInstance().scheduleLocalNotification(ln, System.currentTimeMillis() + 1000, LocalNotification.REPEAT_NONE);
                }
            }
        });

        formPaiement.add(lblMainPaiement);
        formPaiement.add(numeroCarteTextField);
        Container cnDate = new Container(new BoxLayout(BoxLayout.X_AXIS));
        cnDate.add(moisTextField);
        formPaiement.add(anneeTextField);
        formPaiement.add(cnDate);
        formPaiement.add(cvcPasswordTextField);
        formPaiement.add(btnPayer);
    }

    public Form getFormPaiement() {
        return formPaiement;
    }

    public void setFormPaiement(Form formPaiement) {
        this.formPaiement = formPaiement;
    }

    public int getIdProjetConcerne() {
        return idProjetConcerne;
    }

    public void setIdProjetConcerne(int idProjetConcerne) {
        this.idProjetConcerne = idProjetConcerne;
    }

    public void setMainLabelText() {
        int nbrMissions = mu.getNombreMissionsDeLemployeurDuProjet(idProjetConcerne);
        //Ceci est au fait l'ancien prix du projet
        double prixApresPromotion = mu.getPrixDuProjet(idProjetConcerne);
        String nomProjet = mu.getNomProjetFromId(idProjetConcerne);
        if (nbrMissions < 10) {
            //Ne rien faire
        } else if (nbrMissions >= 10 && nbrMissions < 20) {
            prixApresPromotion = prixApresPromotion * 0.99;
        } else if (nbrMissions >= 20 && nbrMissions < 30) {
            prixApresPromotion = prixApresPromotion * 0.95;
        } else if (nbrMissions >= 30) {
            prixApresPromotion = prixApresPromotion * 0.9;
        }

        lblMainPaiement.setText("Vous avec " + nbrMissions + " missions. Vous allez donc payer : " + prixApresPromotion + " € pour le projet : " + nomProjet + ". Veuillez insérer vos coordonnées bancaires ci-dessous :");
    }

    public void confirmerPaiement() {
        payServ.effectuerPaiement(idProjetConcerne);
    }

}
