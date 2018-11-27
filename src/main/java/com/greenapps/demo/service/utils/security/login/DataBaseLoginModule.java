package com.greenapps.demo.service.utils.security.login;

import com.greenapps.demo.service.utils.security.utils.ConnectionHelper;
import com.greenapps.demo.service.utils.security.utils.Constant;
import com.greenapps.demo.web.general.UtilsMessage;
import service.utils.security.utils.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 *
 * @author danny
 */
public class DataBaseLoginModule implements LoginModule {

    // initial state
    private Subject subject;
    private CallbackHandler callbackHandler;

    // configurable option
    private boolean debug = false;

    // the authentication status
    private boolean succeeded = false;
    private boolean commitSucceeded = false;

    // username and password
    private String username;
    private char[] password;

    // testUser's SamplePrincipal
    private DataBasePrincipal userPrincipal;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        // initialize any configured options
        debug = "true".equalsIgnoreCase((String) options.get("debug"));

    }

    @Override
    public boolean login() throws LoginException {
        // prompt for a user name and password
        if (callbackHandler == null) {
            throw new LoginException(UtilsMessage.translate("code.callback", "businesserrors.businesserrors", new String[]{""}));
        }

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("user name: ");
        callbacks[1] = new PasswordCallback("password: ", false);

        try {
            callbackHandler.handle(callbacks);
            username = ((NameCallback) callbacks[0]).getName();
            char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
            if (tmpPassword == null) {
                // treat a NULL password as an empty password
                tmpPassword = new char[0];
            }
            password = new char[tmpPassword.length];

            System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);

            ((PasswordCallback) callbacks[1]).clearPassword();

        } catch (java.io.IOException ioe) {
            throw new LoginException(ioe.toString());
        } catch (UnsupportedCallbackException uce) {
            throw new LoginException(UtilsMessage.translate("code.callbackIna", "businesserrors.businesserrors", new String[]{uce.getCallback().toString()}));
        }
        // print debugging information
        if (debug) {
            System.out.println("\t\t[DatabaseLoginModule] user name: " + username);
            System.out.print("\t\t[DatabaseLoginModule] user password: ");
            for (int i = 0; i < password.length; i++) {
                System.out.print(password[i]);
            }
            System.out.println();
        }

        // verify the username/password
        boolean usernameCorrect = false;

        switch (isUserInBD(username)) {

            case 1:
                usernameCorrect = true;
                break;
            case -1:
                usernameCorrect = false;
                break;
            case 0:
                throw new FailedLoginException(UtilsMessage.translate("code.blockUser", "businesserrors.businesserrors", new String[]{""}));
            default:
                throw new FailedLoginException(UtilsMessage.translate("code.failed", "businesserrors.businesserrors", new String[]{""}));
        }

        if (isCorrectPassword(username, password)) {
            // authentication succeeded!!!
            System.out.println("\t\t[DatabaseLoginModule] " + "autenticado");
            succeeded = true;
            return true;
        } else {
            // authentication failed -- clean out state
            System.out.println("\t\t[DatabaseLoginModule] " + "FALLO AUNTENTICACION");
            succeeded = false;
            username = null;
            for (int i = 0; i < password.length; i++) {
                password[i] = ' ';
            }
            password = null;
            if (!usernameCorrect) {
                throw new FailedLoginException(UtilsMessage.translate("code.usernameFail", "businesserrors.businesserrors", new String[]{""}));
            } else {
                throw new FailedLoginException(UtilsMessage.translate("code.passFail", "businesserrors.businesserrors", new String[]{""}));
            }
        }
    }

    @Override
    public boolean commit() throws LoginException {
        if (succeeded == false) {
            return false;
        } else {
            // add a Principal (authenticated identity)
            // to the Subject
            // assume the user we authenticated is the SamplePrincipal
            userPrincipal = new DataBasePrincipal(username);
            if (!subject.getPrincipals().contains(userPrincipal)) {
                subject.getPrincipals().add(userPrincipal);
            }
            System.out.println("\t\t[DatabaseLoginModule] added SamplePrincipal to Subject");
            // in any case, clean out state
            username = null;
            for (int i = 0; i < password.length; i++) {
                password[i] = ' ';
            }
            password = null;

            commitSucceeded = true;
            return true;
        }
    }

    @Override
    public boolean abort() throws LoginException {
        if (succeeded == false) {
            return false;
        } else if (succeeded == true && commitSucceeded == false) {
            // login succeeded but overall authentication failed
            succeeded = false;
            username = null;
            if (password != null) {
                for (int i = 0; i < password.length; i++) {
                    password[i] = ' ';
                }
                password = null;
            }
            userPrincipal = null;
        } else {
            // overall authentication succeeded and commit succeeded,
            // but someone else's commit failed
            logout();
        }
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().remove(userPrincipal);
        succeeded = false;
        succeeded = commitSucceeded;
        username = null;
        if (password != null) {
            for (int i = 0; i < password.length; i++) {
                password[i] = ' ';
            }
            password = null;
        }
        userPrincipal = null;
        return true;
    }

    private boolean isCorrectPassword(String usernameSearch, char[] passCompare) {
        PreparedStatement ps = null;
        ResultSet rsOra = null;
        Connection conn = null;
        try {
            String dataSource = Resource.getDataSourceName();
            conn = ConnectionHelper.getConnDS(dataSource);
            ps = conn.prepareStatement(Constant.getInstance().getQUERY_PASS());
            ps.setString(1, usernameSearch);
            rsOra = ps.executeQuery();
            if (rsOra.next()) {
                String passwordString = rsOra.getString(Constant.FIELD_PASS);
                char[] passwordBD = passwordString.toCharArray();
                if (passwordBD.length == passCompare.length) {
                    for (int i = 0; i < passwordBD.length; i++) {
                        if (passwordBD[i] != passCompare[i]) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                rsOra.close();
                ps.close();
                conn.close();
            } catch (Exception e) {
                //nothing
            }
        }

    }

    private int isUserInBD(String usernameSearch) {
        PreparedStatement ps = null;
        ResultSet rsOra = null;
        Connection conn = null;
        try {
            String dataSource = Resource.getDataSourceName();
            conn = ConnectionHelper.getConnDS(dataSource);
            ps = conn.prepareStatement(Constant.getInstance().getQUERY_USERNAME());
            ps.setString(1, usernameSearch);
            rsOra = ps.executeQuery();
            if (rsOra.next()) {
                if (rsOra.getString(Constant.FIELD_ACTIVATE).equals(Constant.VALUE_FIELD_INACTIVATE)) {
                    return 0;
                }
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                rsOra.close();
                ps.close();
                conn.close();
            } catch (Exception e) {
                return -1;
            }
        }

    }

}
