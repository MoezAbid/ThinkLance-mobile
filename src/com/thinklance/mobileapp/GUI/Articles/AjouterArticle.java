/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.GUI.Articles;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.ImageViewer;
import com.codename1.io.FileSystemStorage;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.thinklance.mobileapp.Entities.Article;
import com.thinklance.mobileapp.GUI.Actualites.ListeActualites;
import com.thinklance.mobileapp.GUI.Paiements.ListePaiements;
import com.thinklance.mobileapp.Services.Implementation.ArticlesService;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Moez
 */
public class AjouterArticle {

    private Resources theme;
    private boolean selectedAnImage = false;
    private Form formAjouterArticle = new Form("Ajouter article", new BoxLayout(BoxLayout.Y_AXIS));
    private String photoArticleUrl = MoezUtils.urlPhotosArticle + "uploads/articlesPhotos/" + "ImageArticlePlaceHolder.png";
    private ImageViewer imageViewer = null;
    private Image image;
    private EncodedImage enc;
    private ArticlesService artServ = new ArticlesService();
    private Button choisirImageArticleBtn = new Button("Choisir image");
    //TextFields
    private TextField titreTextField = new TextField();
    private TextField descFieldTextField = new TextField();
    private TextField texteTextField = new TextField();
    private Picker typeArticlePicker = new Picker();

    public AjouterArticle() {
        theme = UIManager.initFirstTheme("/theme");
        //Navigation
        formAjouterArticle.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeArticles listeArticles = new ListeArticles();
                listeArticles.getFormListeArticles().show();
            }
        });

        formAjouterArticle.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MaListeArticles maListeArticles = new MaListeArticles();
                maListeArticles.getFormMaListeArticles().show();
            }
        });

        formAjouterArticle.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListePaiements listePaiements = new ListePaiements();
                listePaiements.getFormListePaiements().show();
            }
        });
        
        formAjouterArticle.getToolbar().addCommandToSideMenu("Actualites", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeActualites listAcu = new ListeActualites();
                listAcu.getFormListeActualites().show();
            }
        });
        
        formAjouterArticle.getToolbar().addCommandToRightBar("Retour", null, e -> {
            ListeArticles lart = new ListeArticles();
            lart.getFormListeArticles().show();
        });
        //Init Photo avant selection
        FloatingActionButton fabAjout = FloatingActionButton.createFAB(FontImage.MATERIAL_SAVE);
        fabAjout.addActionListener(e -> {
            String titre = titreTextField.getText();
            String description = descFieldTextField.getText();
            String texte = texteTextField.getText();
            if (titre.equals("") || description.equals("") || texte.equals("")) {
                Dialog.show("Erreur", "Veuillez vérifier les champs saisis, ils doivent être remplis.", null, "OK");
            } else {
                ajouterArticle();
                Dialog.show("Succés", "Article ajouté", null, "OK");
            }

        });
        fabAjout.bindFabToContainer(formAjouterArticle.getContentPane());
        //Picker
        typeArticlePicker.setType(Display.PICKER_TYPE_STRINGS);
        ArrayList<String> strs = artServ.getListeNomsTypesArticles();
        String[] pickerData = new String[strs.size()];
        String[] pickerStringsArray = strs.toArray(pickerData);
        typeArticlePicker.setStrings(pickerStringsArray);
        typeArticlePicker.setSelectedString(pickerStringsArray[0]);
        //Textfields
        titreTextField.setHint("Titre");
        descFieldTextField.setHint("Description");
        texteTextField.setHint("Texte");
        Container cnDescTextField = new Container();
        cnDescTextField.setHeight(60);
        cnDescTextField.setWidth(60);
        cnDescTextField.add(descFieldTextField);
        //Image d'article par défaut 
        Container cnImagEPreview = new Container(new FlowLayout(CENTER, CENTER));
        try {
            enc = EncodedImage.create("/load.png");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        String urlImage = MoezUtils.urlPhotosArticle + "uploads/articlesPhotos/" + "ImageArticlePlaceHolder.png";
        //Boutton choix de l'image de l'article
        Style s = UIManager.getInstance().getComponentStyle("Button");
        Image icon = FontImage.createMaterial(FontImage.MATERIAL_CAMERA, s);
        choisirImageArticleBtn.setIcon(icon);
        choisirImageArticleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Display.getInstance().openImageGallery(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        if (ev != null && ev.getSource() != null) {
                            cnImagEPreview.removeAll();
                            String filePath = (String) ev.getSource();
                            int fileNameIndex = filePath.lastIndexOf("/") + 1;
                            String fileName = filePath.substring(fileNameIndex);

                            Image img = null;
                            try {
                                img = Image.createImage(FileSystemStorage.getInstance().openInputStream(filePath));
                                photoArticleUrl = filePath;
                                System.out.println("Article image selected : " + photoArticleUrl);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            imageViewer = new ImageViewer(img.scaled(130, 130));
                            imageViewer.getAllStyles().setMarginTop(6);
                            imageViewer.getAllStyles().setMarginBottom(6);
                            imageViewer.getAllStyles().setMarginLeft(6);
                            selectedAnImage = true;
                            cnImagEPreview.add(imageViewer);
                        }
                    }
                });
            }
        });
        formAjouterArticle.add(titreTextField);
        formAjouterArticle.add(cnDescTextField);
        //formAjouterArticle.add(descFieldTextField);
        formAjouterArticle.add(texteTextField);
        formAjouterArticle.add(typeArticlePicker);
        formAjouterArticle.add(cnImagEPreview);
        formAjouterArticle.add(choisirImageArticleBtn);
    }

    public Form getFormAjouterArticle() {
        return formAjouterArticle;
    }

    public void setFormAjouterArticle(Form formAjouterArticle) {
        this.formAjouterArticle = formAjouterArticle;
    }

    private void ajouterArticle() {
        String titre = titreTextField.getText();
        String description = descFieldTextField.getText();
        String texteString = texteTextField.getText();
        String urlImage;
        if (selectedAnImage) {
            urlImage = this.photoArticleUrl;
        } else {
            urlImage = MoezUtils.staticUrlImageArticlePlaceHolder;
        }
        int idTypeArticle = artServ.getIdOfTypeArticle(typeArticlePicker.getSelectedString().toString());
        Article newArticle = new Article(0, MoezUtils.getIdUserConnecte(), titre, description, new Date(), texteString, urlImage, idTypeArticle);
        artServ.ajouterArticle(newArticle);
    }
}
