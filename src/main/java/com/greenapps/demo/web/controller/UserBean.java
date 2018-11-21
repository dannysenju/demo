/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.web.controller;

import com.greenapps.demo.domain.entities.Usuario;
import com.greenapps.demo.service.UserEJB;
import com.greenapps.demo.service.utils.exception.BusinessAppException;
import com.greenapps.demo.service.utils.security.utils.UtilsEncrypt;
import com.greenapps.demo.web.general.GeneralBean;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author danny
 */
@Named(value = "userBean")
@ViewScoped
public class UserBean extends GeneralBean implements Serializable {

    private Usuario usuario;
    private String username;
    private String pass;
    private String passConfirm;
    private String oldPass;
    private UploadedFile uploadedPicture;
    private boolean hasImage;
    private Date lastModified;
    private String fullNameUser;
    private String newPass;

    @EJB // pone a dispocision Servicio
    UserEJB userEJB;

    public UserBean() {
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String idString = context.getExternalContext().getSessionMap().get("userId").toString();
        usuario = userEJB.getUserById(Integer.parseInt(idString));
        hasImage = false;
        lastModified = new Date();
        fullNameUser = userEJB.getFullName(usuario.getIdusuario());
    }

    public void fileUploadListener(FileUploadEvent e) {
        FacesContext context = FacesContext.getCurrentInstance();
        // Get uploaded file from the FileUploadEvent
        this.uploadedPicture = e.getFile();
        byte[] content;
        try {
            content = IOUtils.toByteArray(uploadedPicture.getInputstream());
            if (userEJB.updateImage(content, usuario.getIdusuario())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Imagen subida con exito", null));
                hasImage = true;
                lastModified = Calendar.getInstance().getTime();
                LoginBean login = (LoginBean) context.getApplication().getVariableResolver().resolveVariable(context, "loginBean");
                login.setHasImage(true);
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo actualizar su foto, contacte al administrador", null));
            }
        } catch (IOException ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error del servidor, contacte al administrador", null));
        }
    }

    public void updateBasicData() {
        FacesContext context = FacesContext.getCurrentInstance();
        Usuario newInfo = new Usuario();

        newInfo.setUsername(this.usuario.getUsername().toUpperCase());
        newInfo.setNombreCompleto(this.usuario.getNombreCompleto());
        newInfo.setPassword(pass);
        try {
            if (userEJB.updateBasicData(newInfo, this.usuario.getIdusuario())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualización exitosa", null));
                fullNameUser = this.usuario.getNombreCompleto();
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error del servidor, contacte al administrador", null));
            }
        } catch (SQLException ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error del servidor, contacte al administrador", null));
        } catch (BusinessAppException ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getCode() + ": " + ex.getMsj(), null));
        }
    }

    public void updatePassword() {
        FacesContext context = FacesContext.getCurrentInstance();

        String oldCon = UtilsEncrypt.getInstance().encryptPassword(this.oldPass);

        if (UtilsEncrypt.getInstance().comparePassword(oldCon, this.usuario.getPassword())) {
            if (!this.pass.equals("") && this.pass != null) {
                if (userEJB.updatePass(pass, this.usuario.getIdusuario())) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualización exitosa", null));
                    navigate("/views/logOn.xhtml");
                    ExternalContext c = FacesContext.getCurrentInstance().getExternalContext();
                    c.invalidateSession();
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al actualizar, contacte al administrador", null));
                }
            }
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseña Anterior no coincide", null));
        }

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPassConfirm() {
        return passConfirm;
    }

    public void setPassConfirm(String passConfirm) {
        this.passConfirm = passConfirm;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public UploadedFile getUploadedPicture() {
        return uploadedPicture;
    }

    public void setUploadedPicture(UploadedFile uploadedPicture) {
        this.uploadedPicture = uploadedPicture;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getFullNameUser() {
        return fullNameUser;
    }

    public void setFullNameUser(String fullNameUser) {
        this.fullNameUser = fullNameUser;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

}
