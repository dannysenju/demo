package com.greenapps.demo.web.utils.validators;

import com.greenapps.demo.service.utils.security.utils.Constant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author danny
 */
@FacesValidator("valEmail")
public class ValidadorEmail implements Validator {

    private final Pattern PATTERN;
    private Matcher matcher;

    public ValidadorEmail() {
        PATTERN = Pattern.compile(Constant.EMAIL_PATTERN);
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic,
            Object o) throws ValidatorException {

        matcher = PATTERN.matcher(o.toString());
        if (!matcher.matches()) {
            FacesMessage msg
                    = new FacesMessage(Constant.MSG_ERROR_VAL_EMAIL,
                            Constant.MSG_ERROR_EMAIL);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);

        }

    }

}
