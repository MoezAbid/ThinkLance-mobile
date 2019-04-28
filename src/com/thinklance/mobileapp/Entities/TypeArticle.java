/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Entities;

/**
 *
 * @author Moez
 */
public class TypeArticle {

    private int id;
    private String nom;

    public TypeArticle() {
    }

    public TypeArticle(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "TypeArticle{" + "id=" + id + ", nom=" + nom + '}';
    }
}
