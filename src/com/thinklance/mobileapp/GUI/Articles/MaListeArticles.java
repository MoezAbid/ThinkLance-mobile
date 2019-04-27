/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Articles;

import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.FlowLayout;

/**
 *
 * @author Moez
 */
public class MaListeArticles {

    private Form formMaListeArticles = new Form("Mes Articles", new FlowLayout(CENTER, CENTER));
    private Label labelMaListeArticles = new Label("Liste de mes articles");

    public MaListeArticles() {
        formMaListeArticles.add(labelMaListeArticles);
        //Navigation
        formMaListeArticles.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeArticles listeArticles = new ListeArticles();
                listeArticles.getFormListeArticles().show();
            }
        });

        formMaListeArticles.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MaListeArticles maListeArticles = new MaListeArticles();
                maListeArticles.getFormMaListeArticles().show();
            }
        });

        formMaListeArticles.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Paiements");
            }
        });
    }

    public Form getFormMaListeArticles() {
        return formMaListeArticles;
    }

    public void setFormMaListeArticles(Form formMaListeArticles) {
        this.formMaListeArticles = formMaListeArticles;
    }
}
