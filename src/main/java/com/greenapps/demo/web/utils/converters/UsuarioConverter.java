/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.web.utils.converters;

import com.greenapps.demo.domain.entities.Usuario;
import com.greenapps.demo.web.controller.AdminUserBean;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author danny
 */
@FacesConverter(forClass = com.greenapps.demo.domain.entities.Usuario.class, value = "usuarioConverter")
public class UsuarioConverter implements Converter {

    @Override
    public Usuario getAsObject(FacesContext context, UIComponent component, String value) {

        ValueExpression vex
                = context.getApplication().getExpressionFactory()
                        .createValueExpression(context.getELContext(),
                                "#{adminBean}", AdminUserBean.class);
        AdminUserBean adBean = (AdminUserBean) vex.getValue(context.getELContext());
        return adBean.getUser(Integer.valueOf(value));

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {

        try {
            return ((Usuario) o).getIdusuario().toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

}
