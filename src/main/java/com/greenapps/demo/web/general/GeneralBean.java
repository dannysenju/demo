/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.web.general;

import com.greenapps.demo.service.utils.exception.BusinessAppException;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author danny
 */
public class GeneralBean {

    protected final String HOME_ACTION = "/demo/login/menu.xhtml?faces-redirect=true";

    public void navigate(String newPage) {
        FacesContext fc = FacesContext.getCurrentInstance();
        String contextPath = fc.getExternalContext().getRequestContextPath();
        try {
            fc.getExternalContext().redirect(contextPath + newPage);
        } catch (IOException ex) {
            addErrorMessage(UtilsMessage.translate("failed", "general.general", new String[]{"navegación"}));
        }
    }

    public void setPermisos(String rol) throws BusinessAppException {
        if (!rol.equals("ADMIN")) {
            throw new BusinessAppException("1002", "Acceso no permitido");
        }
    }

    /**
     * Agrega un mensaje de información general a la cola de mensajes del
     * FacesContext, los cuales se mostrarán en la vista
     *
     * @param message
     */
    protected void addInfoMessage(String message) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "", message);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    /**
     * Agrega un mensaje de advertencia a la cola de mensajes del FacesContext,
     * los cuales se mostrarán en la vista
     *
     * @param message
     */
    protected void addWarnMessage(String message) {
        message = fixMessage(message);
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, "", message);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    protected void addErrorMessage(String message) {
        message = fixMessage(message);
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", message);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    private String fixMessage(String msg) {
        if (msg.length() > 150) {
            msg = msg.substring(0, 150);
        }
        String newMsg = "";
        for (char c : msg.toCharArray()) {
            switch (c) {
                case '\r':
                case '\n':
                    newMsg += " ";
                    break;
                default:
                    newMsg += c;
                    break;
            }
        }
        return newMsg;
    }

}
