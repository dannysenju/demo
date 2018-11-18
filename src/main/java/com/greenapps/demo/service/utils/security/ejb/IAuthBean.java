package com.greenapps.demo.service.utils.security.ejb;

import com.greenapps.demo.domain.entities.Modulo;
import com.greenapps.demo.domain.entities.Usuario;
import com.greenapps.demo.service.utils.security.utils.Constant;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import com.greenapps.demo.service.utils.security.utils.UtilsEncrypt;

/**
 *
 * @author danny
 */
@Stateful
public class IAuthBean {

    /**
     * Contexto del login
     */
    private LoginContext loginContext;

    /**
     * Registra la cantidad de intentos fallidos de login de un usuario
     */
    private final Hashtable<String, Short> intentosUsuarios = new Hashtable<String, Short>();

    /**
     * Encargado de manejar la lógica de negocio de autenticación por base de
     * datos
     */
    @Inject
    private DatabaseAuthenticationEJB dbAuthentication;

    /**
     * Permite el acceso al origen de datos
     */
    @PersistenceContext
    EntityManager em;

    public boolean login(String user, String password) throws LoginException {
        String authenticationType;

        authenticationType = Constant.getInstance().getAUTHENTICATION_TYPE();

        if (authenticationType != null) {
            if ("BD".equals(authenticationType)) {
                return loginBD(user, password);
            }
        }
        return false;
    }

    /**
     * Metodo para autenticar el usuario contra la BD.
     *
     * @param username Id Usuario
     * @param password
     * @return <code>true</code> Usuario Valido <code>false</code> Usuario
     * Invalido
     * @throws LoginException
     */
    private boolean loginBD(final String username, final String password) throws LoginException {
        dbAuthentication.initDbLoginConfig(username, password);
        dbAuthentication.executeDbLogin(username);
        return true;
    }

    public void logout() {
        if (loginContext != null) {
            try {
                loginContext.logout();
            } catch (Exception e) {
                // nothing
                System.out.println("FIXME: LOGOUT ERROR");
                e.printStackTrace();
            }
        }
    }

    public String getAuthenticatedUser(String user) {
        Usuario loggedUser = null;
        loggedUser = getUser(user);
        if (loggedUser != null) {
            em.refresh(loggedUser);
            return loggedUser.getIdusuario().toString();
        } else {
            if (loginContext != null) {
                try {
                    Iterator<Principal> i1 = loginContext.getSubject().getPrincipals().iterator();
                    Principal principal = (Principal) i1.next();
                    return principal.getName();
                } catch (Exception e) {
                    loggedUser = null;
                }
            } else {
                loggedUser = null;
            }
        }
        return "NologgedUser";
    }

    public ArrayList<Modulo> getUserModulos(String user) {

        ArrayList<Modulo> listModulos = new ArrayList<Modulo>();
        Usuario loggedUser = null;
        em.flush();
        loggedUser = getUser(user);

        em.refresh(loggedUser);
        if (loggedUser != null) {

            for (Modulo modulo : loggedUser.getModuloList()) {

                if (!(modulo.getIcono() != null)) {
                    modulo.setIcono("fe fe-external-link");
                }

                if (modulo.getActivo() == 1) {
                    listModulos.add(modulo);
                }

            }
        }
        return listModulos;

    }

    public boolean isValidPassword(String user, String password) {
        Usuario usuario = getUser(user);
        if (usuario != null) {
            if (!usuario.getPassword().equals(password)) {
                em.refresh(usuario);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean changeUserPassword(String user, String password) {
        Usuario usuario = getUser(user);
        try {
            String newPassword = UtilsEncrypt.getInstance().encryptPassword(password);

            if (isValidPassword(user, newPassword) && isValidPasswordLength(password)) {
                usuario.setPassword(newPassword);
                em.persist(usuario);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidPasswordLength(String password) throws LoginException {
        return password.length() >= 7;
    }

    public Usuario getUser(String user) {
        return (Usuario) em.createNamedQuery("Usuario.findByUsername", Usuario.class)
                .setParameter("username", user).getSingleResult();
    }

}
