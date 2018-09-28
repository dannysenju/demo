package com.softtrons.demo.web;

import com.softtrons.demo.domain.entities.User;
import com.softtrons.demo.service.EJBUserDao;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

@Named
@RequestScoped
public class CreateController implements Serializable {

    private FacesContext context = FacesContext.getCurrentInstance();
    
    @Inject
    EJBUserDao userDao;

    private User newUser;
    
    private String pass;
    private String pass1;

    @PostConstruct
    public void init() {
        newUser = new User();
    }

    public void create() {
        try {
            userDao.createUser(newUser);
            String message = "Usuario creado " + newUser.getUsername();
            context.addMessage(null, new FacesMessage(message));
        } catch (Exception e) {
            String message = "An error has occured while creating the user (see log for details)";
            context.addMessage(null, new FacesMessage(message));
        }
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    public void validatePass() {
        System.out.println("--->" + newUser.getPassword());
        System.out.println("--->" + pass);
        System.out.println("--->" + pass1);
                
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }
    
    
    
}
