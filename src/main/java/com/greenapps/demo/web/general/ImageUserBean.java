/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.web.general;

import com.greenapps.demo.service.UserEJB;
import javax.ejb.EJB;
import javax.inject.Named;
import org.omnifaces.cdi.GraphicImageBean;
/**
 *
 * @author danny
 */
@Named(value = "imageUserBean")
@GraphicImageBean
public class ImageUserBean {

    /**
     * Creates a new instance of ImageUserBean
     */
    public ImageUserBean() {
    }

    @EJB // pone a dispocision Servicio
    UserEJB userEJB;

    public byte[] getImage(int id) {
        return userEJB.getUserById(id).getImagen();
    }

}
