/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Authentification;

import com.thinklance.mobileapp.GUI.PageAccueil;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Form;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.FlowLayout;

/**
 *
 * @author Moez
 */
public class PageLogin {

    private Form formLogin  = new Form("Login", new FlowLayout(CENTER, CENTER));
    private TextField tfName;
    private TextField tfPassword;
    private Button btnConnexion;

    public PageLogin() {
        tfName = new TextField();
        tfPassword = new TextField();
        tfName.setHint("E-mail");
        tfPassword.setHint("Mot de passe");
        btnConnexion = new Button("Connexion");
        //Ajout des composants
        formLogin.add(tfName);
        formLogin.add(tfPassword);
        formLogin.add(btnConnexion);
        formLogin.show();
        //Bouton connexion
        btnConnexion.addActionListener((e)->{
            PageAccueil pageAccueil = new PageAccueil();
            pageAccueil.getFormAccueil().show();
        });
    }

    public Form getFormLogin() {
        return formLogin;
    }

    public void setFormLogin(Form formLogin) {
        this.formLogin = formLogin;
    }

    public TextField getTfName() {
        return tfName;
    }

    public void setTfName(TextField tfName) {
        this.tfName = tfName;
    }

    public TextField getTfPassword() {
        return tfPassword;
    }

    public void setTfPassword(TextField tfPassword) {
        this.tfPassword = tfPassword;
    }

    public Button getBtnConnexion() {
        return btnConnexion;
    }

    public void setBtnConnexion(Button btnConnexion) {
        this.btnConnexion = btnConnexion;
    }

}
