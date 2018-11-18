package com.greenapps.demo.service.utils.security.ejb;

import com.greenapps.demo.domain.entities.Usuario;
import com.greenapps.demo.service.utils.security.login.JaasLoginConfig;
import com.greenapps.demo.service.utils.security.login.MyCallbackHandler;
import com.greenapps.demo.service.utils.security.utils.Constant;
import java.security.Principal;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
/**
 *
 * @author danny
 */
@Stateful
public class DatabaseAuthenticationEJB {

    /**
     * Contexto del login
     */
    private LoginContext loginContext;

    /**
     * Manejador de datos del login
     */
    private CallbackHandler callBackHandler;

    /**
     * Permite el acceso al origen de datos
     */
    @PersistenceContext
    EntityManager em;

    /**
     * Registra la cantidad de intentos fallidos de login de un usuario
     */
    private final Map<String, Short> intentosUsuarios = new HashMap<>();

    /**
     * Inicializa la configuración del proceso de login del usuario de BD
     *
     * @param username
     * @param password
     * @throws LoginException
     */
    public void initDbLoginConfig(final String username, final String password) throws LoginException {
        final Configuration jaasConfig = new JaasLoginConfig();
        callBackHandler = new MyCallbackHandler(username, password, false);
        loginContext = new LoginContext(Constant.getInstance().getJAAS_DB(), null, callBackHandler, jaasConfig);
    }

    /**
     * Ejecuta el login del usuario de base de datos
     *
     * @param username
     * @throws LoginException
     */
    public void executeDbLogin(final String username) throws LoginException {
        try {
            loginContext.login();
            intentosUsuarios.put(username, (short) 0);
        } catch (FailedLoginException e) {
            if (e.getMessage().contains("Contraseña incorrecta")) {
                addFailedLoginAttempt(username);
            }
            loginContext = null;
            throw new LoginException("Fallo Auth: " + e.getMessage());
        }
        final Iterator<Principal> i1 = loginContext.getSubject().getPrincipals().iterator();
        final Principal principal = (Principal) i1.next();
        final String name = principal.getName();
        Usuario loggedUser;
        try {
            loggedUser = (Usuario) em.createNamedQuery("Usuario.findByUsername", Usuario.class)
                    .setParameter("username", username).getSingleResult();
            validateUserLogin(loggedUser);
            loggedUser.setUltimoAcceso(new GregorianCalendar().getTime());
            em.persist(loggedUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void addFailedLoginAttempt(final String username) throws LoginException {
        if (intentosUsuarios.containsKey(username)
                && !Constant.getInstance().getUSER_ADMIN().equalsIgnoreCase(username)) {
            intentosUsuarios.put(username, (short) (intentosUsuarios.get(username) + 1));

            if (intentosUsuarios.get(username) == 3) {
                final Usuario tempUser = (Usuario) em.createNamedQuery("Usuario.findByUsername", Usuario.class)
                        .setParameter("username", username).getSingleResult();
                tempUser.setActivo((short) 0);
                em.persist(tempUser);
                intentosUsuarios.put(username, (short) 0);
                throw new LoginException("intentos excedidos");
            }
        } else {
            intentosUsuarios.put(username, (short) 1);
        }
    }

    private void validateUserLogin(final Usuario loggedUser) throws LoginException {
        em.refresh(loggedUser);
        final int estado = loggedUser.getActivo();
        if (estado != 1) {
            loginContext.logout();
            throw new LoginException("Usuario inactivo");
        }
    }

}
