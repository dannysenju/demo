package com.greenapps.demo.service.utils.security.ejb;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

/**
 *
 * @author danny
 */
@Startup
@Singleton
public class UniqueSessionBean {

    @Inject
    private SessionRemover sessionRemover;

    private static final long serialVersionUID = 1L;
    private final Map<String, HttpSession> users = new HashMap<>();

    public UniqueSessionBean() {

    }

    /**
     *
     * @param user
     * @return
     */
    public HttpSession getUsers(String user) {
        return users.get(user);
    }

    /**
     *
     * @param user
     * @param sessionId
     */
    public void setUsers(String user, HttpSession sessionId) {
        this.users.put(user, sessionId);
    }

    /**
     * validate if user exist
     *
     * @param user
     * @return
     */
    public boolean containUser(String user) {
        return users.containsKey(user);
    }

    /**
     * validate if session exist
     *
     * @param sessionId
     * @return
     */
    public boolean containSession(String sessionId) {
        for (Map.Entry<String, HttpSession> entry : users.entrySet()) {
            HttpSession value = entry.getValue();
            if (value.getId().equals(sessionId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * remove user from list
     *
     * @param user
     */
    public void removeUser(String user) {
        users.remove(user);
    }

    /**
     * Invalida la sesión actual, debido a inconvenientes con la sesión actual
     * este proceso se hace de manera asíncrona
     *
     * @param user
     */
    public void invalidate(String user) {
        HttpSession value = users.get(user);
        try {
            sessionRemover.invalidateSession(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * remove object session by Id
     *
     * @param sessionId
     */
    public void removeSessionId(String sessionId) {
        Vector<String> keys = new Vector<>();
        for (Map.Entry<String, HttpSession> entry : users.entrySet()) {
            String key = entry.getKey();
            HttpSession value = entry.getValue();
            if (value.getId().equals(sessionId)) {
                keys.add(key);
            }
        }
        for (String key : keys) {
            users.remove(key);
        }
    }
}
