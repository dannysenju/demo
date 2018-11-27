package com.greenapps.demo.web.utils.emails;

import com.greenapps.demo.service.utils.exception.BusinessAppException;
import com.greenapps.demo.service.utils.security.utils.Constant;
import com.greenapps.demo.utils.constantview.ConstantView;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author danny
 */
public class EmailUtils {

    private EmailUtils() {
    }

    private static EmailUtils instance = new EmailUtils();

    public static EmailUtils getInstace() {
        return instance;
    }

    public boolean sendEmail(String receiver, String code, String msj) throws BusinessAppException {

        try {
            Email email = new SimpleEmail();
            email.setHostName(ConstantView.SMTP_GMAIL_PATH);
            email.setSslSmtpPort(ConstantView.SMTP_PORT_PATH);
            email.setSmtpPort(465);
            email.setAuthentication(Constant.NAME_MAIL, Constant.getInstance().getPASSWORD_EMAIL());
            email.setSSLOnConnect(true);
            email.setFrom(Constant.USERNAME_EMAIL);
            email.setSubject(msj);
            email.setMsg(msj.toUpperCase() + ": \n" + code);
            email.addTo(receiver);
            email.send();
            return true;
        } catch (EmailException ex) {
            throw new BusinessAppException("code.E1", ex.getMessage());
        }

    }

}
