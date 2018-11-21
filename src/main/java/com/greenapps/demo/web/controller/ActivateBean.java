/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.web.controller;

import com.greenapps.demo.domain.entities.Usuario;
import com.greenapps.demo.service.UserEJB;
import com.greenapps.demo.web.general.GeneralBean;
import com.greenapps.demo.web.utils.emails.EmailUtils;
import com.greenapps.demo.web.utils.emails.JsfUtilities;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

/**
 *
 * @author danny
 */
@Named(value = "activateBean")
@ViewScoped
public class ActivateBean extends GeneralBean implements Serializable {

    private Usuario usuario;
    private String email;
    private String username;
    private JsfUtilities jsfUtilities;
    private boolean isBlockUser;
    private String codActivate;
    private int id;

    @EJB // pone a dispocision Servicio
    UserEJB userEJB;

    @PostConstruct
    public void init() {
        jsfUtilities = JsfUtilities.getInstance();
        isBlockUser = true;
    }

    public void createCode() {
        String code = jsfUtilities.getCode();
        updateUserCod(code);
        if (EmailUtils.getInstace().sendEmail(email, code, "Código de Activación")) {
            isBlockUser = false;
        }
        RequestContext.getCurrentInstance().execute("PF('dlg2').show();");
    }

    private void updateUserCod(String code) {
        Usuario u = userEJB.getUserbyName(this.username.toUpperCase());
        this.id = u.getIdusuario();
        userEJB.updateCode(code, this.id);
    }

    public void createPass() {
        FacesContext context = FacesContext.getCurrentInstance();
        String code = jsfUtilities.getCode();
        if (EmailUtils.getInstace().sendEmail(email, code, "Nueva Contraseña")) {
            Usuario u = userEJB.getUserbyName(this.username.toUpperCase());
            this.id = u.getIdusuario();
            if (userEJB.updatePass(code, this.id)) {
                navigate("/views/logOn.xhtml");
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo actualizar la contraseña, vuelva a intetarlo más tarde", null));
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El correo no se pudo enviar, vuelva a intentarlo más tarde", null));
        }
    }

    public void activateUser() {
        if (userEJB.activateUserByCode(this.codActivate, this.id)) {
            navigate("/views/logOn.xhtml");
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JsfUtilities getJsfUtilities() {
        return jsfUtilities;
    }

    public void setJsfUtilities(JsfUtilities jsfUtilities) {
        this.jsfUtilities = jsfUtilities;
    }

    public boolean isIsBlockUser() {
        return isBlockUser;
    }

    public void setIsBlockUser(boolean isBlockUser) {
        this.isBlockUser = isBlockUser;
    }

    public String getCodActivate() {
        return codActivate;
    }

    public void setCodActivate(String codActivate) {
        this.codActivate = codActivate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
