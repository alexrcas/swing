/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.avantic.consejo.service;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.avantic.consejo.MainForm;
import net.avantic.consejo.model.Documento;
import net.avantic.consejo.model.Portada;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination;

/**
 *
 * @author alexr
 */
public class GenerarDocumentoService {
    
    private DocumentoService documentoService;

    public GenerarDocumentoService() {
        
        this.documentoService = new DocumentoService();
       
    }

    public void generarDocumento(ArrayList<Documento> documentosList) throws IOException {
        
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        LocalDateTime now = LocalDateTime.now();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        
        String nombreDocumentoGenerado = "DocumentoGenerado_" + now.format(formatter) + ".pdf";
        
        pdfMergerUtility.setDestinationFileName(nombreDocumentoGenerado);

        documentosList.stream()
                .map(documento -> new File(documento.getRutaWorkingCopy()))
                .forEach(file -> {
            try {
                pdfMergerUtility.addSource(file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GenerarDocumentoService.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        });
        
        try {
            pdfMergerUtility.mergeDocuments();
        } catch (IOException ex) {
            Logger.getLogger(GenerarDocumentoService.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        
        this.addPageNumber(nombreDocumentoGenerado, documentosList);
        this.crearIndice(nombreDocumentoGenerado, documentosList);
        this.crearPortada(nombreDocumentoGenerado);
        
        this.visualizarDocumentoGenerado(nombreDocumentoGenerado);
    }
    
    
    private void visualizarDocumentoGenerado(String ruta) {
                if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(ruta));
            } catch (IOException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    private void crearPortada(String pdfPath) throws IOException {
        
        File documentoGenerado = new File(pdfPath);
        
        Portada portada = documentoService.findPortada().get();
        File portadaFile = new File(portada.getRutaWorkingCopy());
        
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        pdfMergerUtility.setDestinationFileName(pdfPath);
        
        pdfMergerUtility.addSource(portadaFile);
        pdfMergerUtility.addSource(documentoGenerado);
        pdfMergerUtility.mergeDocuments();
    }
    
    
    private void crearIndice(String pdfPath, ArrayList<Documento> documentosList) throws IOException{
        
        File mergePpdfFile = new File(pdfPath);
        PDDocument documentoPdf = PDDocument.load(mergePpdfFile);
        
        Map<Long, Long> indiceMap = new HashMap<>();
        
        int paginaComienzoDocumento = 1;
        for(Documento documento : documentosList) {
            PDDocument pDDocument = PDDocument.load(new File(documento.getRuta()));
            indiceMap.put(documento.getId(), new Long(paginaComienzoDocumento));
            paginaComienzoDocumento += pDDocument.getNumberOfPages();
        }
         
        
        PDPage indicePage = new PDPage();
        
        PDPageContentStream stream = new PDPageContentStream(documentoPdf, indicePage, PDPageContentStream.AppendMode.APPEND, false, true);
        stream.setNonStrokingColor(Color.BLACK);
        
        
        int linea = 1;
        for (Documento documento : documentosList) {
            
            PDAnnotationLink link = new PDAnnotationLink();
            PDPageDestination destination = new PDPageFitWidthDestination();
            PDActionGoTo action = new PDActionGoTo();

            int destinationPage = indiceMap.get(documento.getId()).intValue() - 1;
            destination.setPage(documentoPdf.getPage(destinationPage));
            action.setDestination(destination);
            link.setAction(action);
            link.setPage(indicePage);


            PDRectangle position = new PDRectangle();
            position.setLowerLeftX(10);
            position.setLowerLeftY(indicePage.getMediaBox().getHeight() - 20 * linea); 
            position.setUpperRightX(300); 
            position.setUpperRightY(position.getLowerLeftY() - 10f); 

            link.setRectangle(position);
            indicePage.getAnnotations().add(link);


            stream.beginText();
            stream.setFont(PDType1Font.COURIER_BOLD, 12);
            stream.newLineAtOffset(10, indicePage.getMediaBox().getHeight() - 20 * linea);
            
            String textoEntrada = "#" + documento.getPunto().getNombre() + " - " + documento.getNombre() + "..........." + (destinationPage + 1);
            stream.showText(textoEntrada);
            stream.endText();
            
            linea++;
        }

        stream.close();
      
        PDPageTree allPages = documentoPdf.getDocumentCatalog().getPages();
        allPages.insertBefore(indicePage, allPages.get(0));
        
        documentoPdf.save(pdfPath);
    }
    
    
    public void addPageNumber(String pdfPath, ArrayList<Documento> documentosList) throws IOException {
    File mergePpdfFile = new File(pdfPath);
    PDDocument document = PDDocument.load(mergePpdfFile);
    int totalPage = document.getNumberOfPages();
    for(int i=0; i<totalPage; i++) {
        PDPage page = document.getPage(i);

        PDPageContentStream stream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false, true);

        stream.setNonStrokingColor(Color.WHITE);
        stream.fillRect(0, 0, page.getMediaBox().getWidth(), 100);
        
        stream.setNonStrokingColor(Color.BLACK);
        stream.beginText();
        stream.setFont(PDType1Font.COURIER_BOLD, 12);
        stream.newLineAtOffset((page.getMediaBox().getWidth() / 2) - 2, 50);
        stream.showText(Integer.toString(i+1));
        stream.endText();
        
        stream.close();
    }
    
    document.save(pdfPath);
    document.close();
}
    
}
