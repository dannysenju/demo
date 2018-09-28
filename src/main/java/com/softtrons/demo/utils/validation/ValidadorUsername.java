package com.softtrons.demo.utils.validation;

import com.softtrons.demo.service.EJBUserDao;
import com.softtrons.demo.utils.constant.Constant;
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
@Named(value = "valUser")
@RequestScoped
public class ValidadorUsername implements Validator {

    @Inject
    EJBUserDao userDao;

    @Override
    public void validate(FacesContext fc, UIComponent uic,
            Object o) throws ValidatorException {

        String username = o.toString();

        if (userDao.verifyUsername(username)) {
            FacesMessage msg
                    = new FacesMessage(Constant.MSG_ERROR_VAL_USERNAME,
                            Constant.MSG_ERROR_USERNAME);
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(msg);
        }

    }

}
