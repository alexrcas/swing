/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avantic.consejo.model;

import java.io.Serializable;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
/**
 *
 * @author alexr
 */
@Entity
@Table(name = "Punto")
public class Punto implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSICION", nullable = true)
    private Long posicion;
    
    @Column(name = "NOMBRE", nullable = false, length = 64)
    private String nombre;
    
    @Column(name = "DESCRIPCION", nullable = false, length = 255)
    private String descripcion;
    
    @Column(name = "GENERACARATULA", nullable = false)
    private boolean generaCaratula;

    
    public Punto() {
    }

    public Punto(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.generaCaratula = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPosicion() {
        return posicion;
    }

    public void setPosicion(Long posicion) {
        this.posicion = posicion;
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

    public boolean isGeneraCaratula() {
        return generaCaratula;
    }

    public void setGeneraCaratula(boolean generaCaratula) {
        this.generaCaratula = generaCaratula;
    }

    @Override
    public String toString() {
        return "Punto{" + "id=" + id + ", posicion=" + posicion + ", nombre=" + nombre + ", descripcion=" + descripcion + ", generaCaratula=" + generaCaratula + '}';
    }
        
    
}
