/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.web.utils.converters;

import com.greenapps.demo.domain.entities.Modulo;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

/**
 *
 * @author danny
 */
@FacesConverter(forClass = com.greenapps.demo.domain.entities.Modulo.class, value = "moduloPickConverter")
public class ModuloPickConverter implements Converter {

    @Override
    public Modulo getAsObject(FacesContext context, UIComponent component, String value) {
        return getObjectFromUIPickListComponent(component, value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        String string;
        if (object == null) {
            string = "";
        } else {
            try {
                string = String.valueOf(((Modulo) object).getIdmodulo());
            } catch (ClassCastException cce) {
                throw new ConverterException(cce.getMessage());
            }
        }
        return string;
    }

    @SuppressWarnings("unchecked")
    private Modulo getObjectFromUIPickListComponent(UIComponent component, String value) {
        final DualListModel<Modulo> dualList;
        try {
            dualList = (DualListModel<Modulo>) ((PickList) component).getValue();
            Modulo m = getObjectFromList(dualList.getSource(), Integer.valueOf(value));
            if (m == null) {
                m = getObjectFromList(dualList.getTarget(), Integer.valueOf(value));
            }

            return m;
        } catch (ClassCastException cce) {
            throw new ConverterException(cce.getMessage());
        } catch (NumberFormatException nfe) {
            throw new ConverterException(nfe.getMessage());
        }
    }

    private Modulo getObjectFromList(final List<?> list, final Integer identifier) {
        for (final Object object : list) {
            final Modulo m = (Modulo) object;
            if (m.getIdmodulo().equals(identifier)) {
                return m;
            }
        }
        return null;
    }

}
