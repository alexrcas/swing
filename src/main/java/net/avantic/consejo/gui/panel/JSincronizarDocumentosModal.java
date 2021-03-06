/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avantic.consejo.gui.panel;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import net.avantic.consejo.MainForm;
import net.avantic.consejo.model.Documento;

/**
 *
 * @author arodriguez
 */
public class JSincronizarDocumentosModal extends javax.swing.JPanel {
    
    public JSincronizarDocumentosModal(ArrayList<Documento> documentosNoSincronizados) {
        initComponents();
        this.documentosPanel.setLayout(new BoxLayout(this.documentosPanel, BoxLayout.Y_AXIS));
        
        if (documentosNoSincronizados.isEmpty()) {
            this.jLabel2.setText("Todos los documentos están sincronizados");
        }
        this.pintarDocumentos(documentosNoSincronizados);
    }
    
    
    private void pintarDocumentos(ArrayList<Documento> documentosList) {
        
        this.documentosPanel.removeAll();
        int cnt = 0;
        for (Documento documento : documentosList) {
            JSincDocumentoRow documentoRow = new JSincDocumentoRow(this, documento);
            if (cnt % 2 == 0) {
                documentoRow.setBackground(new Color(237, 242, 253));
            }
            else {
                documentoRow.setBackground(new Color(218, 228, 251));
            }
            cnt++;
            this.documentosPanel.add(documentoRow);
        }
        
        this.documentosPanel.revalidate();
        this.documentosPanel.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        documentosPanel = new javax.swing.JPanel();

        jLabel2.setText("Los siguientes documentos han sido modificados posteriormente y no se encuentran sincronizados con la copia actual de la aplicación. Pulse Aceptar para sincronizar los documentos");

        javax.swing.GroupLayout documentosPanelLayout = new javax.swing.GroupLayout(documentosPanel);
        documentosPanel.setLayout(documentosPanelLayout);
        documentosPanelLayout.setHorizontalGroup(
            documentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1119, Short.MAX_VALUE)
        );
        documentosPanelLayout.setVerticalGroup(
            documentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 269, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(documentosPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1054, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documentosPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel documentosPanel;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
