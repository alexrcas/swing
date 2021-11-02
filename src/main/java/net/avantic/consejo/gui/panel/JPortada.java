/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avantic.consejo.gui.panel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.avantic.consejo.MainForm;
import net.avantic.consejo.model.Documento;
import net.avantic.consejo.model.Portada;
import net.avantic.consejo.model.Punto;
import net.avantic.consejo.service.DocumentoService;
import net.avantic.consejo.service.PuntoService;

/**
 *
 * @author alexr
 */
public class JPortada extends javax.swing.JPanel {
    
    private MainForm parent;
    
    private DocumentoService documentoService;
    


    /**
     * Creates new form Punto
     */

    public JPortada(MainForm parent) {
        
        initComponents();
        
        this.parent = parent;

        this.documentoService = new DocumentoService();

        this.portadaPanel.setLayout(new BoxLayout(this.portadaPanel, BoxLayout.Y_AXIS));
        
        this.actualizarPanel();
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addPortadaButton = new javax.swing.JButton();
        documentoLabel = new javax.swing.JLabel();
        nombreLabel = new javax.swing.JLabel();
        portadaPanel = new javax.swing.JPanel();
        generarPortadaButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(141, 178, 214));
        setMaximumSize(new java.awt.Dimension(32767, 10000));

        addPortadaButton.setText("Añadir");
        addPortadaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPortadaButtonActionPerformed(evt);
            }
        });

        documentoLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        documentoLabel.setText("Documento de portada");

        nombreLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        nombreLabel.setText("Portada");

        portadaPanel.setToolTipText("");

        javax.swing.GroupLayout portadaPanelLayout = new javax.swing.GroupLayout(portadaPanel);
        portadaPanel.setLayout(portadaPanelLayout);
        portadaPanelLayout.setHorizontalGroup(
            portadaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        portadaPanelLayout.setVerticalGroup(
            portadaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        generarPortadaButton.setText("Generar");
        generarPortadaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarPortadaButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(portadaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nombreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 458, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(documentoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addPortadaButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(generarPortadaButton)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nombreLabel)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(documentoLabel)
                    .addComponent(addPortadaButton)
                    .addComponent(generarPortadaButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(portadaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    private void addPortadaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPortadaButtonActionPerformed
        JSeleccionarPortadaModal modal = new JSeleccionarPortadaModal();
        int seleccion = JOptionPane.showConfirmDialog(this, modal, "Seleccionar Documento", JOptionPane.OK_CANCEL_OPTION);
        if (seleccion == 0) {
            try {
                this.documentoService.crearPortada(modal.getNombre(), modal.getRuta());
                this.actualizarPanel();
            } catch (IOException ex) {
                Logger.getLogger(JPunto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_addPortadaButtonActionPerformed

    private void generarPortadaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarPortadaButtonActionPerformed
        try {
            this.documentoService.generarPortada();
            this.actualizarPanel();
        } catch (IOException ex) {
            Logger.getLogger(JPortada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_generarPortadaButtonActionPerformed

    
    public void eliminarPortada(Portada portada) {
        this.documentoService.eliminarPortada(portada);
        this.portadaPanel.removeAll();
        this.repaint();
        this.revalidate();
    }

    
    private void actualizarPanel() {
        this.portadaPanel.removeAll();
        
        Optional<Portada> portadaOpt = documentoService.findPortada();
        
        if (!portadaOpt.isPresent()) {
            return;
        }
        
        Portada portada = portadaOpt.get();
        
        JPortadaRow jPortadaRow = new JPortadaRow(this, portada);
        this.portadaPanel.add(jPortadaRow);
        
        this.repaint();
        this.revalidate();
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPortadaButton;
    private javax.swing.JLabel documentoLabel;
    private javax.swing.JButton generarPortadaButton;
    private javax.swing.JLabel nombreLabel;
    private javax.swing.JPanel portadaPanel;
    // End of variables declaration//GEN-END:variables
}
