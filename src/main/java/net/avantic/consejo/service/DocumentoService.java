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
import net.avantic.consejo.model.Documento;
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
    
    
    public void eliminarDocumento(Documento documento) {
        String rutaWorkingCopy = documento.getRutaWorkingCopy();
        File file = new File(rutaWorkingCopy);
        file.delete();
        
        this.delete(documento);
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
    
    
    public void delete(Documento documento) {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.delete(documento);
        session.getTransaction().commit();
        
        if (session.isOpen()) {
            session.close();
        }
    }
    
}
