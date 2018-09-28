package com.softtrons.demo.service;

import com.softtrons.demo.domain.entities.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class EJBUserDao {

    @PersistenceContext
    private EntityManager em;

    public User getForUsername(String username) {
        try {
            Query query = em.createQuery("select u from User u where u.username = :username");
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void createUser(User user) {
        em.persist(user);
    }

    public boolean verifyUsername(String username) {
        Query query = em.createQuery("select count(u) from User u where u.username = :username");
        query.setParameter("username", username);
        return ((long) query.getSingleResult() > 0);
    }

}
