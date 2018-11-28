/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.service;

import com.greenapps.demo.domain.entities.Modulo;
import com.greenapps.demo.domain.entities.Usuario;
import com.greenapps.demo.service.utils.exception.BusinessAppException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.primefaces.model.DualListModel;

import com.greenapps.demo.service.utils.security.utils.UtilsEncrypt;
import java.util.Hashtable;

/**
 *
 * @author danny
 */
@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em; // pone a disposicion JPA

    public void createUser(Usuario u) throws SQLException {

        u.setUsername(u.getUsername().toUpperCase());
        u.setRol(u.getRol().toUpperCase());
        em.persist(u);

    }

    public boolean verifyUsername(String username) {
        return ((Usuario) em.createNamedQuery("Usuario.findByUsername", Usuario.class).setParameter("username", username).getSingleResult()).getIdusuario() > 0;
    }

    public boolean countUsername(String username) {
        Number n = (Number) em.createQuery("Select count(u.idusuario) from Usuario u where u.username= :username").setParameter("username", username).getSingleResult();
        return (n).intValue() > 0;
    }

    public boolean updateCode(String codActivacion, int id) {

        Query q = em.createQuery("Update Usuario u SET u.codActivacion = :cod WHERE u.idusuario= :id")
                .setParameter("cod", codActivacion)
                .setParameter("id", id);
        return q.executeUpdate() > 0;
    }

    public Usuario getUserbyName(String username) {
        return (Usuario) em.createNamedQuery("Usuario.findByUsername", Usuario.class).setParameter("username", username).getSingleResult();
    }

    public boolean activateUserByCode(String codActivate, int id) {

        Query q = em.createQuery("Update Usuario u Set u.activo = 1 WHERE u.idusuario= :id");
        q.setParameter("id", id);

        return q.executeUpdate() > 0;
    }

    public boolean updatePass(String code, int id) {

        String pass = UtilsEncrypt.getInstance().encryptPassword(code);

        Query q = em.createQuery("Update Usuario u SET u.password = :pass WHERE u.idusuario= :id")
                .setParameter("pass", pass)
                .setParameter("id", id);
        return q.executeUpdate() > 0;
    }

    public Usuario getUserById(int id) {
        return (Usuario) em.createNamedQuery("Usuario.findByIdusuario", Usuario.class).setParameter("idusuario", id).getSingleResult();
    }

    public boolean updateImage(byte[] image, int id) {
        Query q = em.createQuery("Update Usuario u SET u.imagen = :image WHERE u.idusuario= :id")
                .setParameter("image", image)
                .setParameter("id", id);
        return q.executeUpdate() > 0;
    }

    public boolean updateBasicData(Usuario newInfo, Integer idusuario) throws SQLException, BusinessAppException {

        Usuario oldUser = getUserById(idusuario);
        String fullName = newInfo.getNombreCompleto();
        String username = newInfo.getUsername();
        String email = newInfo.getEmail();
        String phone = newInfo.getTelefono();

        StringBuilder queryJpa = new StringBuilder("Update Usuario u SET ");

        String queryFullName = (oldUser.getNombreCompleto() != null && oldUser.getNombreCompleto().equals(fullName)) ? "" : " u.nombreCompleto = :fullname ";

        String queryUserName = "";
        if (!queryFullName.equals("")) {
            queryUserName = (oldUser.getUsername() != null && oldUser.getUsername().equals(username)) ? "" : " , u.username = :username";
        } else {
            queryUserName = (oldUser.getUsername() != null && oldUser.getUsername().equals(username)) ? "" : " u.username = :username";
        }
        String queryEmail = "";

        if ((!queryFullName.equals("") && !queryUserName.equals("")) || (queryFullName.equals("") && !queryUserName.equals(""))) {
            queryEmail = (oldUser.getEmail() != null && oldUser.getEmail().equals(email)) ? "" : " , u.email = :email";
        } else {
            queryEmail = (oldUser.getEmail() != null && oldUser.getEmail().equals(email)) ? "" : " u.email = :email";
        }

        String queryPhone = "";

        if ((!queryFullName.equals("") && !queryUserName.equals("") && !queryEmail.equals(""))
                || (queryFullName.equals("") && !queryUserName.equals("") && !queryEmail.equals(""))
                || (queryFullName.equals("") && queryUserName.equals("") && !queryEmail.equals(""))) {

            queryPhone = (oldUser.getTelefono() != null && oldUser.getTelefono().equals(phone)) ? "" : " , u.telefono = :telefono";
        } else {
            queryPhone = (oldUser.getTelefono() != null && oldUser.getTelefono().equals(phone)) ? "" : " u.telefono = :telefono";
        }

        queryJpa.append(queryFullName);
        queryJpa.append(queryUserName);
        queryJpa.append(queryEmail);
        queryJpa.append(queryPhone);
        queryJpa.append(" WHERE u.idusuario= :id ");

        Query q = em.createQuery(queryJpa.toString());

        q.setParameter("id", idusuario);

        if (!queryFullName.equals("")) {
            q.setParameter("fullname", fullName);
        }
        if (!queryUserName.equals("")) {
            q.setParameter("username", username);
        }
        if (!queryEmail.equals("")) {
            q.setParameter("email", email);
        }
        if (!queryPhone.equals("")) {
            q.setParameter("telefono", phone);
        }

        return q.executeUpdate() > 0;

    }

    public String getFullName(Integer idusuario) {
        return getUserById(idusuario).getNombreCompleto();
    }

    public List<Usuario> getAllActiveUsers() {
        List<Usuario> listAll = em.createNamedQuery("Usuario.findByActivo", Usuario.class).setParameter("activo", 1).getResultList();
        List<Usuario> listResult = new ArrayList<>();
        for (Usuario usuario : listAll) {
            if (!usuario.getRol().equals("ADMIN")) {
                listResult.add(usuario);
            }
        }
        return listResult;
    }

    public List<Usuario> getAllUsers() {
        List<Usuario> listAll = em.createNamedQuery("Usuario.findAll", Usuario.class).getResultList();
        List<Usuario> listResult = new ArrayList<>();
        for (Usuario usuario : listAll) {
            if (!usuario.getRol().equals("ADMIN")) {
                listResult.add(usuario);
            }
        }
        return listResult;
    }

    public Hashtable assignModules(Usuario userModule, DualListModel<Modulo> modules) {

        Hashtable results = new Hashtable();

        List<Modulo> target = modules.getTarget();
        target.removeAll(new HashSet(userModule.getModuloList()));
        String delete = "delete from modulo_usuario where (id_modulo = ? ) and (id_usuario = ?)";
        int countdeletes = 0;
        Query q1 = em.createNativeQuery(delete);

        for (Modulo modulo : modules.getSource()) {
            countdeletes++;
            q1.setParameter(1, modulo.getIdmodulo());
            q1.setParameter(2, userModule.getIdusuario());
            q1.executeUpdate();
        }

        results.put("deletes", countdeletes);

        String insert = "insert into modulo_usuario (id_modulo, id_usuario) values (?,?)";

        Query q2 = em.createNativeQuery(insert);

        int countInserts = 0;

        for (Modulo modulo : target) {
            countInserts++;
            q2.setParameter(1, modulo.getIdmodulo());
            q2.setParameter(2, userModule.getIdusuario());
            q2.executeUpdate();
        }
        results.put("inserts", countInserts);
        return results;
    }

    public boolean deleteUser(Usuario u) {
        Query q = em.createQuery("Update Usuario u SET u.activo = 0 WHERE u.idusuario= :id")
                .setParameter("id", u.getIdusuario());
        return q.executeUpdate() > 0;
    }

    public boolean activateUser(Usuario u) {
        Query q = em.createQuery("Update Usuario u SET u.activo = 1 WHERE u.idusuario= :id")
                .setParameter("id", u.getIdusuario());
        return q.executeUpdate() > 0;
    }

}
