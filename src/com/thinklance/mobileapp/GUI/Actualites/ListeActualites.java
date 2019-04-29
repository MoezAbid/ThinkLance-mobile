/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Actualites;

import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Tabs;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.thinklance.mobileapp.Entities.Actualite;
import com.thinklance.mobileapp.GUI.Articles.ListeArticles;
import com.thinklance.mobileapp.GUI.Articles.MaListeArticles;
import com.thinklance.mobileapp.GUI.Paiements.ListePaiements;
import com.thinklance.mobileapp.Services.Implementation.ActualitesService;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;

/**
 *
 * @author Moez
 */
public class ListeActualites {

    private Form formListeActualites = new Form("Actualites", new BoxLayout(BoxLayout.Y_AXIS));
    private Container containerTech = BoxLayout.encloseY(new Label("Tech"));
    private Container containerProg = BoxLayout.encloseY(new Label("Prog"));
    private Container containerFree = BoxLayout.encloseY(new Label("Free"));
    private ImageViewer imageViewer = null;
    private Image image;
    private EncodedImage enc;

    public ListeActualites() {
        Tabs t = new Tabs();
        Style s = UIManager.getInstance().getComponentStyle("Tab");
        FontImage icon1 = FontImage.createMaterial(FontImage.MATERIAL_QUESTION_ANSWER, s);
        addTech();
        addProg();
        addFree();

        t.addTab("Techologies", icon1, containerTech);
        t.addTab("Programmation", icon1, containerProg);
        t.addTab("Freelance", icon1, containerFree);
        formListeActualites.add(t);

        //Navigation
        formListeActualites.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeArticles listeArticles = new ListeArticles();
                listeArticles.getFormListeArticles().show();
            }
        });

        formListeActualites.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MaListeArticles maListeArticles = new MaListeArticles();
                maListeArticles.getFormMaListeArticles().show();
            }
        });

        formListeActualites.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListePaiements listePaiements = new ListePaiements();
                listePaiements.getFormListePaiements().show();
            }
        });

        formListeActualites.getToolbar().addCommandToSideMenu("Actualites", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeActualites listAcu = new ListeActualites();
                listAcu.getFormListeActualites().show();
            }
        });
    }

    public Form getFormListeActualites() {
        return formListeActualites;
    }

    public void setFormListeActualites(Form formListeActualites) {
        this.formListeActualites = formListeActualites;
    }

    private void addTech() {
        containerTech.removeAll();
        ActualitesService artServ = new ActualitesService();
        for (Actualite act : artServ.getTechnologies()) {
            Container articleItemContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Container titreEtDescriptionItemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            try {
                String urlImage = act.getImage();
                enc = EncodedImage.create("/load.png");
                image = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
                imageViewer = new ImageViewer(image.scaled(80, 60));
                imageViewer.getAllStyles().setMarginLeft(6);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            articleItemContainer.add(imageViewer);
            titreEtDescriptionItemContainer.add(act.getTitre());
            titreEtDescriptionItemContainer.add(MoezUtils.raccourcirString(act.getDescription()));
            articleItemContainer.add(titreEtDescriptionItemContainer);
            imageViewer.addPointerReleasedListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    BrowserComponent browser = new BrowserComponent();
                    //browser.setURL(act.getUrlToAcualite());
                    //containerTech.add(browser);
                    Display.getInstance().execute(act.getUrlToAcualite());
                }
            });
            containerTech.add(articleItemContainer);
        }
    }

    private void addProg() {
        containerProg.removeAll();
        ActualitesService artServ = new ActualitesService();
        for (Actualite act : artServ.getProgrammationEtApplications()) {
            Container articleItemContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Container titreEtDescriptionItemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            try {
                String urlImage = act.getImage();
                enc = EncodedImage.create("/load.png");
                image = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
                imageViewer = new ImageViewer(image.scaled(80, 60));
                imageViewer.getAllStyles().setMarginLeft(6);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            articleItemContainer.add(imageViewer);
            titreEtDescriptionItemContainer.add(act.getTitre());
            titreEtDescriptionItemContainer.add(MoezUtils.raccourcirString(act.getDescription()));
            articleItemContainer.add(titreEtDescriptionItemContainer);
            imageViewer.addPointerReleasedListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    BrowserComponent browser = new BrowserComponent();
                    //browser.setURL(act.getUrlToAcualite());
                    //containerTech.add(browser);
                    Display.getInstance().execute(act.getUrlToAcualite());
                }
            });
            containerProg.add(articleItemContainer);
        }
    }

    private void addFree() {
        containerFree.removeAll();
        ActualitesService artServ = new ActualitesService();
        for (Actualite act : artServ.getFreelanceActualites()) {
            Container articleItemContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Container titreEtDescriptionItemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            try {
                String urlImage = act.getImage();
                enc = EncodedImage.create("/load.png");
                image = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
                imageViewer = new ImageViewer(image.scaled(80, 60));
                imageViewer.getAllStyles().setMarginLeft(6);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            articleItemContainer.add(imageViewer);
            titreEtDescriptionItemContainer.add(act.getTitre());
            titreEtDescriptionItemContainer.add(MoezUtils.raccourcirString(act.getDescription()));
            articleItemContainer.add(titreEtDescriptionItemContainer);
            imageViewer.addPointerReleasedListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    BrowserComponent browser = new BrowserComponent();
                    //browser.setURL(act.getUrlToAcualite());
                    //containerTech.add(browser);
                    Display.getInstance().execute(act.getUrlToAcualite());
                }
            });
            containerFree.add(articleItemContainer);
        }
    }

}
