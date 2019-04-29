/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinklance.mobileapp.Services.Interfaces;

import com.thinklance.mobileapp.Entities.Actualite;
import java.util.ArrayList;

/**
 *
 * @author Moez
 */
public interface IActualites {

    public ArrayList<Actualite> getTechnologies();

    public ArrayList<Actualite> getProgrammationEtApplications();

    public ArrayList<Actualite> getFreelanceActualites();
}
