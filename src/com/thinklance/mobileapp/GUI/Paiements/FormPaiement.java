/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Paiements;

import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

/**
 *
 * @author Moez
 */
public class FormPaiement {

    private Form formListeArticles = new Form("Payer", new BoxLayout(BoxLayout.Y_AXIS));

    public FormPaiement() {
    }

    public Form getFormListeArticles() {
        return formListeArticles;
    }

    public void setFormListeArticles(Form formListeArticles) {
        this.formListeArticles = formListeArticles;
    }

}
