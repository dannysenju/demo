/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenapps.demo.domain.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author danny
 */
@Entity
@Table(name = "modulo", catalog = "demos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modulo.findAll", query = "SELECT m FROM Modulo m")
    , @NamedQuery(name = "Modulo.findByIdmodulo", query = "SELECT m FROM Modulo m WHERE m.idmodulo = :idmodulo")
    , @NamedQuery(name = "Modulo.findByNombre", query = "SELECT m FROM Modulo m WHERE m.nombre = :nombre")
    , @NamedQuery(name = "Modulo.findByDescripcion", query = "SELECT m FROM Modulo m WHERE m.descripcion = :descripcion")
    , @NamedQuery(name = "Modulo.findByActivo", query = "SELECT m FROM Modulo m WHERE m.activo = :activo")})
public class Modulo implements Serializable {

    @Size(max = 45)
    @Column(name = "icono")
    private String icono;

    @Size(max = 45)
    @Column(name = "url")
    private String url;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "module_seq")
    @SequenceGenerator(name = "module_seq", sequenceName = "module_seq", initialValue = 3, allocationSize = 100)
    @Column(name = "idmodulo")
    @Basic(optional = false)
    private Integer idmodulo;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private int activo;
    @JoinTable(name = "modulo_usuario", joinColumns = {
        @JoinColumn(name = "id_modulo", referencedColumnName = "idmodulo")}, inverseJoinColumns = {
        @JoinColumn(name = "id_usuario", referencedColumnName = "idusuario")})
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Usuario> usuarioList;

    public Modulo() {
    }

    public Modulo(Integer idmodulo) {
        this.idmodulo = idmodulo;
    }

    public Modulo(Integer idmodulo, String nombre, int activo) {
        this.idmodulo = idmodulo;
        this.nombre = nombre;
        this.activo = activo;
    }

    public Integer getIdmodulo() {
        return idmodulo;
    }

    public void setIdmodulo(Integer idmodulo) {
        this.idmodulo = idmodulo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmodulo != null ? idmodulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modulo)) {
            return false;
        }
        Modulo other = (Modulo) object;
        if ((this.idmodulo == null && other.idmodulo != null) || (this.idmodulo != null && !this.idmodulo.equals(other.idmodulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Modulo[ idmodulo=" + idmodulo + " ]";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

}
