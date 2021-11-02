/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avantic.consejo.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author arodriguez
 */
@Entity
@Table(name = "Portada")
public class Portada implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "NOMBRE", nullable = false, length = 64)
    private String nombre;
    
    @Column(name = "RUTA", nullable = false, length = 255)
    private String ruta;
    
    
    @Column(name = "RUTAWORKINGCOPY", nullable = false, length = 255)
    private String rutaWorkingCopy;

    
    public Portada() {
    }

    public Portada(String nombre, String ruta, String rutaWorkingCopy) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.rutaWorkingCopy = rutaWorkingCopy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRutaWorkingCopy() {
        return rutaWorkingCopy;
    }

    public void setRutaWorkingCopy(String rutaWorkingCopy) {
        this.rutaWorkingCopy = rutaWorkingCopy;
    }
    
    
    
    
}
