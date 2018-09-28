package com.softtrons.demo.utils.validation;

import com.softtrons.demo.utils.constant.Constant;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author danny
 */
@FacesValidator("valPass")
public class ValidadorPass implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent component,
            Object value) throws ValidatorException {

        String password = (String) value;
        String confirm = (String) component.getAttributes().get("confirm");

        if (password == null || confirm == null) {
            return; // Just ignore and let required="true" do its job.
        }

        if (!password.equals(confirm)) {
            FacesMessage msg
                        = new FacesMessage(Constant.MSG_ERROR_VAL_PASS,
                                Constant.MGS_ERROR_PASS);
                msg.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(msg);
        }

    }

}
