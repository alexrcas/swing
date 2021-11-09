/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avantic.consejo.service;

import java.util.ArrayList;
import net.avantic.consejo.model.Documento;
import net.avantic.consejo.model.Punto;
import net.avantic.consejo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


/**
 *
 * @author alexr
 */
public class PuntoService {

    public ArrayList<Punto> listPuntos() {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Punto> puntosList = (ArrayList<Punto>) session.createCriteria(Punto.class)
                .addOrder(Order.asc("posicion"))
                .list();

        if (session.isOpen()) {
            session.close();
        }
        return puntosList;
    }
    
    
    
    public void saveOrUpdate(Punto punto) {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.saveOrUpdate(punto);
        session.getTransaction().commit();
        
        if (session.isOpen()) {
            session.close();
        }
    }
    
    
    public void delete(Punto punto) {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        session.delete(punto);
        session.getTransaction().commit();
        
        if (session.isOpen()) {
            session.close();
        }
    }
    
    
}
