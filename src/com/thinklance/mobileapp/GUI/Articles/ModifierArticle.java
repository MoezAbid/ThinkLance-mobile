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
import com.codename1.ui.TextField;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.thinklance.mobileapp.Entities.Article;
import com.thinklance.mobileapp.Services.Implementation.ArticlesService;
import com.thinklance.mobileapp.Utils.MoezUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Moez
 */
public class ModifierArticle {

    private Resources theme;
    private boolean selectedAnImage = false;
    private int idArticleAModifier = 1;
    private Form formModifierArticle = new Form("Modifier article", new BoxLayout(BoxLayout.Y_AXIS));
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

    public ModifierArticle() {
        //Navigation
        formModifierArticle.getToolbar().addCommandToSideMenu("Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListeArticles listeArticles = new ListeArticles();
                listeArticles.getFormListeArticles().show();
            }
        });

        formModifierArticle.getToolbar().addCommandToSideMenu("Mes Articles", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                MaListeArticles maListeArticles = new MaListeArticles();
                maListeArticles.getFormMaListeArticles().show();
            }
        });

        formModifierArticle.getToolbar().addCommandToSideMenu("Paiements", null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Paiements");
            }
        });
        formModifierArticle.getToolbar().addCommandToRightBar("Retour", null, e -> {
            ListeArticles lart = new ListeArticles();
            MaListeArticles mart = new MaListeArticles();
            mart.getFormMaListeArticles().show();
        });
        //Bouton enregistrer
        FloatingActionButton fabAjout = FloatingActionButton.createFAB(FontImage.MATERIAL_SAVE);
        fabAjout.addActionListener(e -> {
            String titre = titreTextField.getText();
            String description = descFieldTextField.getText();
            String texte = texteTextField.getText();
            if (titre.equals("") || description.equals("") || texte.equals("")) {
                Dialog.show("Erreur", "Veuillez vérifier les champs saisis, ils doivent être remplis.", null, "OK");
            } else {
                modifierArticle();
                Dialog.show("Succés", "Article modifié", null, "OK");
            }

        });
        fabAjout.bindFabToContainer(formModifierArticle.getContentPane());
        //Picker
        typeArticlePicker.setType(Display.PICKER_TYPE_STRINGS);
        ArrayList<String> strs = artServ.getListeNomsTypesArticles();
        String[] pickerData = new String[strs.size()];
        String[] pickerStringsArray = strs.toArray(pickerData);
        typeArticlePicker.setStrings(pickerStringsArray);
        typeArticlePicker.setSelectedString(artServ.getNomOfTypeArticle(1));
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
        formModifierArticle.add(titreTextField);
        formModifierArticle.add(cnDescTextField);
        //formAjouterArticle.add(descFieldTextField);
        formModifierArticle.add(texteTextField);
        formModifierArticle.add(typeArticlePicker);
        formModifierArticle.add(cnImagEPreview);
        formModifierArticle.add(choisirImageArticleBtn);
    }

    private void modifierArticle() {
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
        artServ.modifierArticle(this.idArticleAModifier, newArticle);
        System.out.println("Article modifié dans l'interface : " + newArticle);
    }

    public int getIdArticleAModifier() {
        return idArticleAModifier;
    }

    public void setIdArticleAModifier(int idArticleAModifier) {
        this.idArticleAModifier = idArticleAModifier;
    }

    public String getPhotoArticleUrl() {
        return photoArticleUrl;
    }

    public void setPhotoArticleUrl(String photoArticleUrl) {
        this.photoArticleUrl = photoArticleUrl;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public TextField getTitreTextField() {
        return titreTextField;
    }

    public Form getFormModifierArticle() {
        return formModifierArticle;
    }

    public void setFormModifierArticle(Form formModifierArticle) {
        this.formModifierArticle = formModifierArticle;
    }

    public void setTitreTextField(String titreTextField) {
        this.titreTextField.setText(titreTextField);
    }

    public TextField getDescFieldTextField() {
        return descFieldTextField;
    }

    public void setDescFieldTextField(String descFieldTextField) {
        this.descFieldTextField.setText(descFieldTextField);
    }

    public TextField getTexteTextField() {
        return texteTextField;
    }

    public void setTexteTextField(String texteTextField) {
        this.texteTextField.setText(texteTextField);
    }

    public void setImagePreview(String urlPhotoArticle) {
        String urlImage = MoezUtils.urlPhotosArticle + "uploads/articlesPhotos/" + urlPhotoArticle;
        Image img = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
        this.imageViewer = new ImageViewer(img.scaled(80, 60));
    }
}
