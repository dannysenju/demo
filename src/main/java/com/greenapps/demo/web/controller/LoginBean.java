package com.greenapps.demo.web.controller;

import com.greenapps.demo.domain.entities.Modulo;
import com.greenapps.demo.domain.entities.Usuario;
import com.greenapps.demo.domain.entities.dto.UserSessionInfoDTO;
import com.greenapps.demo.service.UserEJB;
import com.greenapps.demo.service.utils.security.ejb.IAuthBean;
import com.greenapps.demo.service.utils.security.ejb.UniqueSessionBean;
import com.greenapps.demo.web.general.GeneralBean;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.NoSuchEJBException;
import javax.enterprise.context.SessionScoped;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author danny
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean extends GeneralBean implements Serializable {

    private String username;
    private String oldPassword;
    private String password;
    private String passwordConfirm;
    private List<Modulo> menuModulesList;
    private String actionMessage;
    private String sessionId;
    private String invalidateSession;
    private String fullName;
    private String rol;
    private int id;

    @Inject
    IAuthBean authentication;

    @Inject
    transient UniqueSessionBean uniqueSession;

    @Inject
    UserEJB userEJB;

    /**
     * Inicializa el bean de login
     */
    @PostConstruct
    void init() {
        username = "";
        password = "";

    }

    public LoginBean() {
    }

    /**
     * get current authenticated user
     *
     * @return
     */
    public String getAuthenticatedUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        String usuario = (String) context.getExternalContext().getSessionMap().get("user");
        return usuario;
    }

    /**
     * Realiza el proceso del login del usuario con su contraseña
     *
     * @return
     */
    public String login() {
        boolean isLoginSuccessful = false;
        String page = "";
        try {
            isLoginSuccessful = authentication.login(username.toUpperCase(), password);
        } catch (LoginException e) {
            page = processLoginExceptions(e);
        } catch (NoSuchEJBException e) {
            ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isLoginSuccessful) {
            page = loginSuccessful();
        }
        return page;
    }

    /**
     * Este método solo se debe ejecutar al completar el proceso de login de
     * forma satisfactoria
     *
     * @return
     */
    private String loginSuccessful() {
        /**
         * CONSULTAR LA LISTA DE MODULOS QUE TIENE ESE USUARIO
         */
        menuModulesList = new ArrayList<>();

        menuModulesList = authentication.getUserModulos(username.toUpperCase());

        Usuario u = userEJB.getUserbyName(username.toUpperCase());

        this.fullName = u.getNombreCompleto();
        this.rol = u.getRol().toUpperCase();
        this.id = u.getIdusuario();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        //CREANDO LAS 3 VARIABLES DE SESION: username, rol userId
        facesContext.getExternalContext().getSessionMap().put("username", username.toUpperCase());
        facesContext.getExternalContext().getSessionMap().put("userId", u.getIdusuario());
        facesContext.getExternalContext().getSessionMap().put("rol", u.getRol());

        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        sessionId = session.getId();

        if (!uniqueSession.containUser(username.toUpperCase())) {
            uniqueSession.setUsers(username.toUpperCase(), session);
        } else {
            uniqueSession.invalidate(username.toUpperCase());
            uniqueSession.setUsers(username.toUpperCase(), session);
        }

        actionMessage = "";

        UserSessionInfoDTO userSessionInfoDTO = new UserSessionInfoDTO();
        userSessionInfoDTO.setUser(username.toUpperCase());
        Usuario loggedUser = authentication.getUser(username.toUpperCase());
        userSessionInfoDTO.setRol(loggedUser.getRol());
        userSessionInfoDTO.setNombreCompleto(loggedUser.getNombreCompleto());

        facesContext.getExternalContext().getSessionMap().put("userSessionInfo", userSessionInfoDTO);

        String msg = "Usuario logeado con exito" + username;
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));

        return this.rol.equals("ADMIN") ? "admin/admin.xhtml?faces-redirect=true" : "dashboard.xhtml?faces-redirect=true";
    }

    /**
     * Si en el proceso de login básico (user/pass) ocurre una excepción
     * {@link LoginException} entonces debe ser dirigido a este método
     *
     * @param e
     * @return
     */
    private String processLoginExceptions(LoginException e) {
        actionMessage = e.getMessage().toUpperCase();
        if (e.getMessage().contains("Usuario Bloqueado")) {
            return "/views/login/activate.xhtml?faces-redirect=true";
        }
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, actionMessage, null));
        password = "";
        return "logOn.xhtml";
    }

    /**
     * perform logout operations
     *
     * @return
     */
    public void logout() {
        uniqueSession.removeUser(username.toUpperCase());
        menuModulesList = null;
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.invalidateSession();
        navigate("/views/logOn.xhtml");
    }

    public IAuthBean getAuthentication() {
        return authentication;
    }

    public void setAuthentication(IAuthBean authentication) {
        this.authentication = authentication;
    }

    public UniqueSessionBean getUniqueSession() {
        return uniqueSession;
    }

    public void setUniqueSession(UniqueSessionBean uniqueSession) {
        this.uniqueSession = uniqueSession;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public List<Modulo> getMenuModulesList() {
        return menuModulesList;
    }

    public void setMenuModulesList(List<Modulo> menuModulesList) {
        this.menuModulesList = menuModulesList;
    }

    public String getActionMessage() {
        return actionMessage;
    }

    public void setActionMessage(String actionMessage) {
        this.actionMessage = actionMessage;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getInvalidateSession() {
        return invalidateSession;
    }

    public void setInvalidateSession(String invalidateSession) {
        this.invalidateSession = invalidateSession;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
