package com.greenapps.demo.web.utils.validators;

import com.greenapps.demo.service.UserEJB;
import com.greenapps.demo.service.utils.security.utils.Constant;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author danny
 */
@Named(value = "valExistUser")
@RequestScoped
public class ValidadorExistUser implements Validator {

    @Inject
    UserEJB userDao;

    @Override
    public void validate(FacesContext fc, UIComponent uic,
            Object o) throws ValidatorException {

        String username = o.toString();

        if (!userDao.countUsername(username)) {
            FacesMessage msg
                    = new FacesMessage(Constant.MSG_ERROR_USERNAME_EXIST,
                            Constant.MSG_ERROR_USERNAME_EXIST);
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(msg);
        }

    }

}
