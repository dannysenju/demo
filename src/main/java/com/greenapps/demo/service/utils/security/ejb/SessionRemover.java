package com.greenapps.demo.service.utils.security.ejb;

import java.util.concurrent.ConcurrentLinkedQueue;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpSession;

/**
 *
 * @author danny
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SessionRemover {

    private final ConcurrentLinkedQueue<HttpSession> transactions
            = new ConcurrentLinkedQueue<>();

    public void invalidateSession(HttpSession session) {
        transactions.add(session);
    }

    @Schedule(second = "*/10", hour = "*", minute = "*", persistent = false)
    private void invalidate() {
        HttpSession session;
        while ((session = transactions.poll()) != null) {
            try {
                session.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
