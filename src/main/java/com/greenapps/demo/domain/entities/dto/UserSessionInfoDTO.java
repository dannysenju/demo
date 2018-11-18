/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.domain.entities.dto;

import com.greenapps.demo.domain.entities.Modulo;
import java.util.List;

/**
 *
 * @author danny
 */
public class UserSessionInfoDTO {

    private static final long serialVersionUID = 1L;

    /**
     * Usuario autenticado
     */
    private String user;

    private String rol;

    private String nombreCompleto;

    private List<Modulo> modulos;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

}
