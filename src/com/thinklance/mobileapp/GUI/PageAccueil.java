/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI;

import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.FlowLayout;
import com.thinklance.mobileapp.GUI.Articles.ListeArticles;
import com.thinklance.mobileapp.GUI.Articles.MaListeArticles;

/**
 *
 * @author Moez
 */
public class PageAccueil {

    private Form formAccueil = new Form("Accueil", new FlowLayout(CENTER, CENTER));
    private Label labelAccueil = new Label("Bienvenue");

    public PageAccueil() {
        formAccueil.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeArticles listeArticles = new ListeArticles();
                listeArticles.getFormListeArticles().show();
            }
        });

        formAccueil.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MaListeArticles maListeArticles = new MaListeArticles();
                maListeArticles.getFormMaListeArticles().show();
            }
        });

        formAccueil.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Paiements");
            }
        });
    }

    public Form getFormAccueil() {
        return formAccueil;
    }

    public void setFormAccueil(Form formAccueil) {
        this.formAccueil = formAccueil;
    }
}
