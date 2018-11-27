/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.web.general;

import com.greenapps.demo.utils.constantview.ConstantView;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * @author danny
 */
public class UtilsMessage {

    /**
     * Metodo que traduce una etiqueta
     *
     * @param etq Etiqueta
     * @param packageName Nombre del Paquete
     * @param paramValue parametro si lo hay
     * @return
     */
    public static String translate(String etq, String packageName, String[] paramValue) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String messageBundleName = ConstantView.RESOURCE_BUNDLE_PATH + packageName;
        Locale locale = facesContext.getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);

        String msgValue = bundle.getString(etq);
        MessageFormat messageFormat = new MessageFormat(msgValue);
        Object[] msgArgs = new Object[paramValue.length];
        int indx = 0;
        for (String string : paramValue) {
            if (string.equals("")) {
                return msgValue;
            }
            msgArgs[indx] = string;
            indx++;
        }

        return messageFormat.format(msgArgs);

    }
}
