/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.web.controller;

import com.greenapps.demo.domain.entities.Modulo;
import com.greenapps.demo.domain.entities.Usuario;
import com.greenapps.demo.service.ModuloEJB;
import com.greenapps.demo.service.UserEJB;
import com.greenapps.demo.service.utils.exception.BusinessAppException;
import com.greenapps.demo.service.utils.security.utils.UtilsEncrypt;
import com.greenapps.demo.web.general.GeneralBean;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.DualListModel;

/**
 *
 * @author danny
 */
@Named(value = "adminBean")
@ViewScoped
public class AdminUserBean extends GeneralBean implements Serializable {

    private Usuario newUser;
    private String pass;
    private String fullNameUser;
    private List<Modulo> availableModules;
    private DualListModel<Modulo> modules;
    private List<Usuario> listUsers;
    private Usuario userModule;

    @EJB // pone a dispocision Servicio
    UserEJB userEJB;

    @Inject // pone a dispocision Servicio
    ModuloEJB moduloEJB;

    public AdminUserBean() {
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            setPermisos(context.getExternalContext().getSessionMap().get("rol").toString());
            String idString = context.getExternalContext().getSessionMap().get("userId").toString();
            newUser = new Usuario();
            fullNameUser = userEJB.getFullName(Integer.parseInt(idString));
            listUsers = userEJB.getAllActiveUsers();
            userModule = listUsers.size() > 0 ? listUsers.get(0) : new Usuario();
            initPickList(userModule.getModuloList());

        } catch (BusinessAppException ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acceso denegado contacte al administrador", null));
            navigate("/views/error/not-access.xhtml");
        }
    }

    private void initPickList(List<Modulo> moduleTarget) {
        availableModules = moduloEJB.getAllAvailableModules();
        availableModules.removeAll(new HashSet(moduleTarget));
        modules = new DualListModel<>(availableModules, moduleTarget);
    }

    public void createUser() {
        FacesContext context = FacesContext.getCurrentInstance();

        this.newUser.setPassword(UtilsEncrypt.getInstance().encryptPassword(pass));

        try {
            userEJB.createUser(this.newUser);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Creación exitosa de: " + this.newUser.getNombreCompleto(), null));

            this.newUser = new Usuario();

        } catch (SQLException ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un Error al momento de crear el usuario, contactese con el administrador", null));
        }
    }

    public void assignModules() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (userEJB.assignModules(this.userModule, this.modules)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modulos asignados exitosamente a: " + this.userModule.getNombreCompleto(), null));
            initPickList(userEJB.getUserById(this.userModule.getIdusuario()).getModuloList());
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se agrego ningún módulo", null));
            initPickList(userEJB.getUserById(this.userModule.getIdusuario()).getModuloList());
        }

    }

    public void changeUserSelection(ValueChangeEvent evt) {
        Usuario u = (Usuario) evt.getNewValue();
        initPickList(u.getModuloList());
    }

    public Usuario getUser(Integer id) {
        return userEJB.getUserById(id);
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFullNameUser() {
        return fullNameUser;
    }

    public void setFullNameUser(String fullNameUser) {
        this.fullNameUser = fullNameUser;
    }

    public Usuario getNewUser() {
        return newUser;
    }

    public void setNewUser(Usuario newUser) {
        this.newUser = newUser;
    }

    public List<Modulo> getAvailableModules() {
        return availableModules;
    }

    public void setAvailableModules(List<Modulo> availableModules) {
        this.availableModules = availableModules;
    }

    public DualListModel<Modulo> getModules() {
        return modules;
    }

    public void setModules(DualListModel<Modulo> modules) {
        this.modules = modules;
    }

    public List<Usuario> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<Usuario> listUsers) {
        this.listUsers = listUsers;
    }

    public Usuario getUserModule() {
        return userModule;
    }

    public void setUserModule(Usuario userModule) {
        this.userModule = userModule;
    }

}
