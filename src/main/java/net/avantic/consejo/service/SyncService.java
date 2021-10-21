/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avantic.consejo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.avantic.consejo.model.Documento;

/**
 *
 * @author arodriguez
 */
public class SyncService {
    
    private DocumentoService documentoService;

    
    public SyncService() {
        this.documentoService = new DocumentoService();
    }
    
    
    public boolean isNecesariaSincronizacion(Documento documento) {
        
        File documentoOriginalFile = new File(documento.getRuta());
        File documentoWorkingCopy = new File(documento.getRutaWorkingCopy());
        
        return documentoOriginalFile.lastModified() > documentoWorkingCopy.lastModified();
    }
    
    
    public ArrayList<Documento> listDocumentosNoSincronizados() {
        return this.documentoService.listDocumentos().stream()
                .filter(this::isNecesariaSincronizacion)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    
    public void sincronizarDocumentos() {
        this.documentoService.listDocumentos().stream()
                .forEach(this::sincronizar);
    }
    
    
    private void sincronizar(Documento documento) {
        
        File documentoOriginalFile = new File(documento.getRuta());
        File documentoWorkingCopy = new File(documento.getRutaWorkingCopy());
        
        if (documentoOriginalFile.lastModified() > documentoWorkingCopy.lastModified()) {
            try {
                Files.copy(Paths.get(documentoOriginalFile.getAbsolutePath()), Paths.get(documentoWorkingCopy.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(SyncService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
}
