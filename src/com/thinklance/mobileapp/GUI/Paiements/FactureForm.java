/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Paiements;

import com.codename1.components.ImageViewer;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.thinklance.mobileapp.GUI.Actualites.ListeActualites;
import com.thinklance.mobileapp.GUI.Articles.ListeArticles;
import com.thinklance.mobileapp.GUI.Articles.MaListeArticles;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;

/**
 *
 * @author Moez
 */
public class FactureForm {

    private Form factureForm = new Form("Facture");
    private ImageViewer imageViewer = null;
    private Image image;
    private EncodedImage enc;
    private String idFactureConcernee;

    public FactureForm(String idFactureConcernee) {
        //Navigation
        factureForm.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeArticles listeArticles = new ListeArticles();
                listeArticles.getFormListeArticles().show();
            }
        });

        factureForm.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MaListeArticles maListeArticles = new MaListeArticles();
                maListeArticles.getFormMaListeArticles().show();
            }
        });

        factureForm.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListePaiements listePaiements = new ListePaiements();
                listePaiements.getFormListePaiements().show();
            }
        });

        factureForm.getToolbar().addCommandToSideMenu("Actualites", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeActualites listAcu = new ListeActualites();
                listAcu.getFormListeActualites().show();
            }
        });

        factureForm.getToolbar().addCommandToRightBar("Retour", null, e -> {
            ListePaiements lsp = new ListePaiements();
            lsp.getFormListePaiements().show();
        });
        //End Navigation
        //Affichage de la facture
        BrowserComponent browser = new BrowserComponent();
        browser.setURL(MoezUtils.urlFacture + idFactureConcernee);
        factureForm.add(browser);
        Display.getInstance().execute(MoezUtils.urlFacture + idFactureConcernee);
    }

    public Form getFactureForm() {
        return factureForm;
    }

    public void setFactureForm(Form factureForm) {
        this.factureForm = factureForm;
    }

    public String getIdFactureConcernee() {
        return idFactureConcernee;
    }

    public void setIdFactureConcernee(String idFactureConcernee) {
        this.idFactureConcernee = idFactureConcernee;
    }
}
