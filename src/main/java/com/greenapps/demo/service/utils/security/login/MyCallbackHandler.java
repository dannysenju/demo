package com.greenapps.demo.service.utils.security.login;

import com.greenapps.demo.service.utils.security.utils.UtilsEncrypt;
import com.greenapps.demo.web.general.UtilsMessage;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 *
 * @author danny
 */
public class MyCallbackHandler implements CallbackHandler {

    /**
     * Nombre del usuario que se está autenticando
     */
    private String user = "";

    /**
     * Contraseña suministrada y encriptada
     */
    private String pwd = "";

    /**
     *
     * @param user
     * @param password
     * @param isEncrypt indica si la contraseña suministrada está encriptada o
     * no
     */
    public MyCallbackHandler(final String user, final String password, final boolean isEncrypt) {
        this.user = user;
        if (isEncrypt) {
            this.pwd = password;
        } else {
            try {
                this.pwd = UtilsEncrypt.getInstance().encryptPassword(password);
            } catch (Exception e) {
                this.pwd = password;
            }
        }
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof TextOutputCallback) {
                final TextOutputCallback toc = (TextOutputCallback) callbacks[i];
                switch (toc.getMessageType()) {
                    case TextOutputCallback.INFORMATION:
                        break;
                    case TextOutputCallback.ERROR:
                        break;
                    case TextOutputCallback.WARNING:
                        break;
                    default:
                        throw new IOException( UtilsMessage.translate("code.NotSupported", "businesserrors.businesserrors",new String[]{""+toc.getMessageType()}));
                }
            } else if (callbacks[i] instanceof NameCallback) {
                final NameCallback nameCallback = (NameCallback) callbacks[i];
                nameCallback.setName(user);
            } else if (callbacks[i] instanceof PasswordCallback) {
                final PasswordCallback passwordCallback = (PasswordCallback) callbacks[i];
                passwordCallback.setPassword(pwd.toCharArray());
            } 
        }

    }

}
