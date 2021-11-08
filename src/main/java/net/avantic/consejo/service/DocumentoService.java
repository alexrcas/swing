/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avantic.consejo.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;
import net.avantic.consejo.model.ActaConsejoAnterior;
import net.avantic.consejo.model.Documento;
import net.avantic.consejo.model.Indice;
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
    
    
    public void generarPortada(String fecha, String hora) throws IOException {
        
        Path rutaOutput = Paths.get(this.workingCopyPath, "portada_generada.pdf");
        Path rutaInput = Paths.get("portada_template.html");

        
        PrintWriter writer = new PrintWriter(rutaInput.toString(), "UTF-8");
        
        writer.print("<html lang='es'>\n" +
"<head>\n" +
"<style>\n" +
".row {\n" +
"  display: block;\n" +
"}\n" +
".text-center {\n" +
"  text-align: center;\n" +
"}\n" +
"\n" +
".w-50 {\n" +
"  width: 50%;\n" +
"  margin: auto;\n" +
"}\n" +
".mb-10p {\n" +
"  margin-bottom: 10px;\n" +
"}\n" +
".mt-20 {\n" +
"  margin-top: 20%;\n" +
"}\n" +
"</style>\n" +
"</head>\n" +
"<body>\n" +
"<div class='row text-center'>\n" +
"<h3>Autoridad Portuaria</h3>\n" +
"</div>\n" +
"<div class='mt-20'>\n" +
"<div class='row text-center'>\n" +
"<h4>SESIÓN ORDINARIA DEL CONSEJO DE ADMINISTRACIÓN</h4>\n" +
"</div>\n" +
"<div class='row text-center'>\n" +
"<h4>Fecha: " + fecha + "</h4>\n" +
"</div>\n" +
"<div class='row text-center'>\n" +
"<h4>HORA: " + hora + "</h4>\n" +
"</div>\n" +
"<div class='row text-center'>\n" +
"<h4>CONVOCATORIA</h4>\n" +
"</div>\n" +
"<div class='row text-center w-50'>\n" +
"  <div class='row text-center mb-10p'>\n" +
"    <span>\n" +
"      Siguiendo indicaciones del Sr. Presidente se le convoca, como miembro del \n" +
"      Órgano de Gobierno de la Autoridad Portuaria, a la sesión que se celebrará en \n" +
"      la fecha y hora que se indica, en primera cita y, en su caso, una hora más tarde \n" +
"      en segunda convocatoria, para tratar del Orden del Día que a continuación se \n" +
"      consigna.\n" +
"    </span>\n" +
"  </div>\n" +
"  <div class='row text-center'>\n" +
"    <span>Se ruega que, en su caso, comunique la imposibilidad de su asistencia a la sesión.</span>\n" +
"  </div>\n" +
"</div>\n" +
"</div>\n" +
"</body>\n" +
"</html>");
        
        writer.close();
        
        
        OutputStream os = new FileOutputStream(rutaOutput.toString());
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri(rutaInput.toUri().toString());
            builder.toStream(os);
            builder.run();
            os.close();
        

        Portada portada = new Portada("portada_generada.pdf", rutaOutput.toString(), Paths.get(this.workingCopyPath, "portada_generada.pdf").toString());
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
    
    
    public Optional<Indice> findIndice() {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Indice> indiceList = (ArrayList<Indice>) session.createCriteria(Indice.class)
                .addOrder(Order.desc("id"))
                .list();
        
        if (indiceList.isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.of(indiceList.get(0));
    }
    
    public Optional<ActaConsejoAnterior> findActaConsejoAnterior() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<ActaConsejoAnterior> actaConsejoAnteriorList = (ArrayList<ActaConsejoAnterior>) session.createCriteria(ActaConsejoAnterior.class)
                .addOrder(Order.desc("id"))
                .list();
        
        if (actaConsejoAnteriorList.isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.of(actaConsejoAnteriorList.get(0));
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
    
    public void eliminarIndice(Indice indice) {
        String rutaWorkingCopy = indice.getRutaWorkingCopy();
        File file = new File(rutaWorkingCopy);
        file.delete(); 
        
        this.delete(indice);
    }
    
    public void eliminarActaConsejoAnterior(ActaConsejoAnterior actaConsejoAnterior) {
        String rutaWorkingCopy = actaConsejoAnterior.getRutaWorkingCopy();
        File file = new File(rutaWorkingCopy);
        file.delete(); 
        
        this.delete(actaConsejoAnterior);
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
    
    
    public void saveOrUpdate(Indice indice) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.saveOrUpdate(indice);
        session.getTransaction().commit();
        
        if (session.isOpen()) {
            session.close();
        }
    }
    
    public void saveOrUpdate(ActaConsejoAnterior actaConsejoAnterior) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.saveOrUpdate(actaConsejoAnterior);
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
    
    
    public void delete (Indice indice) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.delete(indice);
        session.getTransaction().commit();
        
        if (session.isOpen()) {
            session.close();
        }
    }
    
    
    public void delete (ActaConsejoAnterior actaConsejoAnterior) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.delete(actaConsejoAnterior);
        session.getTransaction().commit();
        
        if (session.isOpen()) {
            session.close();
        }
    }

    public void crearIndice(String nombre, String ruta) throws IOException {
        
        Optional<Indice> indiceOpt = this.findIndice();
        if (indiceOpt.isPresent()) {
            this.eliminarIndice(indiceOpt.get());
        }
        
        Path rutaOrigen = Paths.get(ruta);
        String nombreFichero = rutaOrigen.getFileName().toString();
        
        Indice indice = new Indice(nombre, ruta, Paths.get(this.workingCopyPath, nombreFichero).toString());
        Files.copy(rutaOrigen, Paths.get(this.workingCopyPath, nombreFichero), StandardCopyOption.REPLACE_EXISTING);
        
        this.saveOrUpdate(indice);
    }

    public void crearActaConsejoAnterior(String nombre, String ruta) throws IOException {
        
        Optional<ActaConsejoAnterior> actaConsejoAnteriorOpt = this.findActaConsejoAnterior();
        if (actaConsejoAnteriorOpt.isPresent()) {
            this.eliminarActaConsejoAnterior(actaConsejoAnteriorOpt.get());
        }
        
        Path rutaOrigen = Paths.get(ruta);
        String nombreFichero = rutaOrigen.getFileName().toString();
        
        ActaConsejoAnterior actaConsejoAnterior = new ActaConsejoAnterior(nombre, ruta, Paths.get(this.workingCopyPath, nombreFichero).toString());
        Files.copy(rutaOrigen, Paths.get(this.workingCopyPath, nombreFichero), StandardCopyOption.REPLACE_EXISTING);
        
        this.saveOrUpdate(actaConsejoAnterior);
    }
    
}
