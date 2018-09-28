package com.softtrons.demo.utils.validation;

import java.util.List;
import javax.faces.component.UIComponent;

/**
 *
 * @author danny
 */
public class JsfUtilities {

    public static UIComponent findComponent(UIComponent component, String id) {
        if (component.getId().equals(id)) {
            return component;
        } else {
            List<UIComponent> children = component.getChildren();
            for (UIComponent myComponent : children) {
                UIComponent componentFound = findComponent(myComponent, id);
                if (componentFound != null) {
                    return componentFound;
                }
            }
            return null;
        }
    }

}
