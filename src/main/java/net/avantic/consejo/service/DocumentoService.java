/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avantic.consejo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;
import net.avantic.consejo.model.Documento;
import net.avantic.consejo.model.Portada;
import net.avantic.consejo.model.Punto;
import net.avantic.consejo.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


/**
 *
 * @author alexr
 */
public class DocumentoService {
    
    private final String workingCopyPath = "workingCopy";

    
    public DocumentoService() {
        
        File directorioWorkingCopy = new File(workingCopyPath);
        if (!directorioWorkingCopy.exists()) {
            directorioWorkingCopy.mkdir();
        }
    }
    
    
    
    public void crearDocumento(Punto punto, String nombre, String ruta) throws IOException {
        
        Path rutaOrigen = Paths.get(ruta);
        String nombreFichero = rutaOrigen.getFileName().toString();
        
        Documento documento = new Documento(punto, nombre, ruta, Paths.get(this.workingCopyPath, nombreFichero).toString());
        Files.copy(rutaOrigen, Paths.get(this.workingCopyPath, nombreFichero), StandardCopyOption.REPLACE_EXISTING);

        this.saveOrUpdate(documento);

    }
    
    
    public void generarPortada() throws IOException {
        
        Path ruta = Paths.get(this.workingCopyPath, "portada.pdf");
        File portadaFile = new File(ruta.toString());
        portadaFile.createNewFile();
        
        Optional<Portada> portadaOpt = this.findPortada();
        if (portadaOpt.isPresent()) {
            this.eliminarPortada(portadaOpt.get());
        }
        
        Portada portada = new Portada("portada.pdf", ruta.toString(), ruta.toString());
        this.saveOrUpdate(portada);
    }
    
    
    public void crearPortada(String nombre, String ruta) throws IOException {
        
        Optional<Portada> portadaOpt = this.findPortada();
        if (portadaOpt.isPresent()) {
            this.eliminarPortada(portadaOpt.get());
        }
        
        Path rutaOrigen = Paths.get(ruta);
        String nombreFichero = rutaOrigen.getFileName().toString();
        
        Portada portada = new Portada(nombre, ruta, Paths.get(this.workingCopyPath, nombreFichero).toString());
        Files.copy(rutaOrigen, Paths.get(this.workingCopyPath, nombreFichero), StandardCopyOption.REPLACE_EXISTING);
        
        this.saveOrUpdate(portada);
    }
    
   
    public Optional<Portada> findPortada() {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Portada> portadaList = (ArrayList<Portada>) session.createCriteria(Portada.class)
                .addOrder(Order.desc("id"))
                .list();
        
        if (portadaList.isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.of(portadaList.get(0));
    }
    
    
    public void eliminarDocumento(Documento documento) {
        String rutaWorkingCopy = documento.getRutaWorkingCopy();
        File file = new File(rutaWorkingCopy);
        file.delete();
        
        this.delete(documento);
    }
    
    
    public void eliminarPortada(Portada portada) {
        String rutaWorkingCopy = portada.getRutaWorkingCopy();
        File file = new File(rutaWorkingCopy);
        file.delete(); 
        
        this.delete(portada);
    }

    
    public ArrayList<Documento> listDocumentos() {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Documento> documentosList = (ArrayList<Documento>) session.createCriteria(Documento.class).list();
        
        if (session.isOpen()) {
            session.close();
        }
        return documentosList;
    }
    
    
    public ArrayList<Documento> listDocumentosByPunto(Punto punto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        ArrayList<Documento> documentosList = (ArrayList<Documento>) session.createCriteria(Documento.class)
                .add(Restrictions.eq("punto", punto))
                .addOrder(Order.asc("id"))
                .list();
        
        if (session.isOpen()) {
            session.close();
        }
        return documentosList;
    }
    
        public ArrayList<Documento> listDocumentosByPuntoOrderByPosicion(Punto punto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        ArrayList<Documento> documentosList = (ArrayList<Documento>) session.createCriteria(Documento.class)
                .add(Restrictions.eq("punto", punto))
                .addOrder(Order.asc("posicion"))
                .list();
        
        if (session.isOpen()) {
            session.close();
        }
        return documentosList;
    }
    
    
    public void saveOrUpdate(Documento documento) {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.saveOrUpdate(documento);
        session.getTransaction().commit();
        
        if (session.isOpen()) {
            session.close();
        }
    }
    
    
    public void saveOrUpdate(Portada portada) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.saveOrUpdate(portada);
        session.getTransaction().commit();
        
        if (session.isOpen()) {
            session.close();
        }
    }
    
    
    public void delete(Documento documento) {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.delete(documento);
        session.getTransaction().commit();
        
        if (session.isOpen()) {
            session.close();
        }
    }
    
    
    public void delete (Portada portada) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.delete(portada);
        session.getTransaction().commit();
        
        if (session.isOpen()) {
            session.close();
        }
    }
    
}
