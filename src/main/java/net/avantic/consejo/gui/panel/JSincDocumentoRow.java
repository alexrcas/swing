/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avantic.consejo.gui.panel;

import java.awt.Image;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.ImageIcon;
import net.avantic.consejo.model.Documento;

/**
 *
 * @author arodriguez
 */
public class JSincDocumentoRow extends javax.swing.JPanel {

    /**
     * Creates new form JSincDocumentoRow
     */
    public JSincDocumentoRow(JSincronizarDocumentosModal parent, Documento documento) {
        initComponents();
        
        String nombre = "#" + documento.getPunto().getNombre() + " - " + documento.getNombre();
        this.nombreLabel.setText(nombre);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        File fileOriginal = new File(documento.getRuta());
        File fileWorkingCopy = new File(documento.getRutaWorkingCopy());
        LocalDateTime fechaOriginal = LocalDateTime.ofInstant(Instant.ofEpochMilli(fileOriginal.lastModified()), TimeZone.getDefault().toZoneId());
        LocalDateTime fechaWorkingCopy = LocalDateTime.ofInstant(Instant.ofEpochMilli(fileWorkingCopy.lastModified()), TimeZone.getDefault().toZoneId());
        
        this.fechaDocumentoLabel.setText(fechaOriginal.format(formatter));
        this.fechaCopiaLocalLabel.setText(fechaWorkingCopy.format(formatter));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nombreLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        fechaCopiaLocalLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fechaDocumentoLabel = new javax.swing.JLabel();

        nombreLabel.setText("jLabel1");

        jLabel1.setText("Copia local");

        fechaCopiaLocalLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        fechaCopiaLocalLabel.setText("jLabel2");

        jLabel3.setText("Documento");

        fechaDocumentoLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        fechaDocumentoLabel.setText("jLabel4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nombreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fechaCopiaLocalLabel)
                .addGap(46, 46, 46)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fechaDocumentoLabel)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreLabel)
                    .addComponent(jLabel1)
                    .addComponent(fechaCopiaLocalLabel)
                    .addComponent(jLabel3)
                    .addComponent(fechaDocumentoLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fechaCopiaLocalLabel;
    private javax.swing.JLabel fechaDocumentoLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel nombreLabel;
    // End of variables declaration//GEN-END:variables
}
