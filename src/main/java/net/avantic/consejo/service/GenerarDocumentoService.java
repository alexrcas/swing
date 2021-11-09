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
import static java.util.Comparator.comparingInt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.collectingAndThen;
import net.avantic.consejo.MainForm;
import net.avantic.consejo.model.ActaConsejoAnterior;
import net.avantic.consejo.model.Documento;
import net.avantic.consejo.model.Indice;
import net.avantic.consejo.model.Portada;
import net.avantic.consejo.model.Punto;
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
    
    private PuntoService puntoService;

    public GenerarDocumentoService() {
        
        this.documentoService = new DocumentoService();
        this.puntoService = new PuntoService();
       
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
        
        this.adjuntarActaConsejoAnterior(nombreDocumentoGenerado);
        
        this.addPageNumber(nombreDocumentoGenerado, documentosList);

        this.crearCaratulas(nombreDocumentoGenerado, documentosList);
  
        this.crearIndice(nombreDocumentoGenerado, documentosList);
        
        this.addPortada(nombreDocumentoGenerado);
        
        this.visualizarDocumentoGenerado(nombreDocumentoGenerado);
    }
    
    
    private void crearCaratulas(String pdfPath, ArrayList<Documento> documentosList) throws IOException {
        
        Map<Long, Long> indiceMap = new HashMap<>();
        int paginaComienzoDocumento = 1;
        
        for (Documento documento : documentosList) {
            
            PDDocument pDDocument = PDDocument.load(new File(documento.getRuta()));
            indiceMap.put(documento.getId(), new Long(paginaComienzoDocumento));
            paginaComienzoDocumento += pDDocument.getNumberOfPages();
            pDDocument.close();
        }
        
        File file = new File(pdfPath);
        PDDocument documentoGenerado = PDDocument.load(file);
        
        this.puntoService.listPuntos().stream()
                .map(this.documentoService::listDocumentosByPuntoOrderByPosicion)
                .forEach(docList -> {
                    
            try {
                
                Documento primerDocumentoPunto = docList.get(0);
                
                if (primerDocumentoPunto.getPunto().isGeneraCaratula()) {
                    Long paginaCaratula = indiceMap.get(primerDocumentoPunto.getId());

                    PDPage caratulaPage = new PDPage();
                    PDPageContentStream stream = new PDPageContentStream(documentoGenerado, caratulaPage, PDPageContentStream.AppendMode.APPEND, false, true);

                    stream.beginText();
                    stream.setFont(PDType1Font.COURIER_BOLD, 12);
                    stream.newLineAtOffset(10, caratulaPage.getMediaBox().getHeight() - 20 * 5);

                    String textoEntradaActa = "Caratula";
                    stream.showText(textoEntradaActa);
                    stream.endText();

                    stream.close();


                    PDPageTree allPages = documentoGenerado.getDocumentCatalog().getPages();
                    allPages.insertBefore(caratulaPage, allPages.get(paginaCaratula.intValue()));

                    documentoGenerado.save(file);
                }
            } catch (IOException ex) {
                Logger.getLogger(GenerarDocumentoService.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
                });

        documentoGenerado.close();
    }
    
    
    private void addPortada(String nombreDocumentoGenerado) throws FileNotFoundException, IOException {
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        pdfMergerUtility.setDestinationFileName(nombreDocumentoGenerado);
        
        Portada portada = documentoService.findPortada().get();
        
        pdfMergerUtility.addSource(new File(portada.getRutaWorkingCopy()));
        pdfMergerUtility.addSource(new File(nombreDocumentoGenerado));
        
        pdfMergerUtility.mergeDocuments();
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
    
    
    private void adjuntarIndice(String nombreDocumentoGenerado) throws FileNotFoundException, IOException {
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        pdfMergerUtility.setDestinationFileName(nombreDocumentoGenerado);
        
        Indice indice = documentoService.findIndice().get();
        
        pdfMergerUtility.addSource(new File(indice.getRutaWorkingCopy()));
        pdfMergerUtility.addSource(new File(nombreDocumentoGenerado));
        
        pdfMergerUtility.mergeDocuments();
    }
    
    
    private void adjuntarActaConsejoAnterior(String nombreDocumentoGenerado) throws FileNotFoundException, IOException {
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        pdfMergerUtility.setDestinationFileName(nombreDocumentoGenerado);
        
        ActaConsejoAnterior actaConsejoAnterior = documentoService.findActaConsejoAnterior().get();
        
        pdfMergerUtility.addSource(new File(actaConsejoAnterior.getRutaWorkingCopy()));
        pdfMergerUtility.addSource(new File(nombreDocumentoGenerado));
        
        pdfMergerUtility.mergeDocuments();
    }
    
    
    private void crearIndice(String pdfPath, ArrayList<Documento> documentosList) throws IOException {
        
        
        Optional<Indice> indiceOpt = this.documentoService.findIndice();
        if (indiceOpt.isPresent()) {
            this.adjuntarIndice(pdfPath);
            return;
        }
        
        File mergePpdfFile = new File(pdfPath);
        PDDocument documentoPdf = PDDocument.load(mergePpdfFile);
        
        Map<Long, Long> indiceMap = new HashMap<>();
        
        int paginaComienzoDocumento = 1;
        
        ActaConsejoAnterior actaConsejoAnterior = this.documentoService.findActaConsejoAnterior().get();
        PDDocument pdDocument = PDDocument.load(new File(actaConsejoAnterior.getRuta()));
        paginaComienzoDocumento += pdDocument.getNumberOfPages();
        
        
        PDPage indicePage = new PDPage();  
        PDPageContentStream stream = new PDPageContentStream(documentoPdf, indicePage, PDPageContentStream.AppendMode.APPEND, false, true);
        stream.setNonStrokingColor(Color.BLACK);
        

        for(Documento documento : documentosList) {
            PDDocument pDDocument = PDDocument.load(new File(documento.getRuta()));
            indiceMap.put(documento.getId(), new Long(paginaComienzoDocumento));
            paginaComienzoDocumento += pDDocument.getNumberOfPages();
        }

        
        int linea = 1;

        PDAnnotationLink linkActa = new PDAnnotationLink();
        PDPageDestination destinationActa = new PDPageFitWidthDestination();
        PDActionGoTo actionActa = new PDActionGoTo();

        int destinationPageActa = 0;
        destinationActa.setPage(documentoPdf.getPage(destinationPageActa));
        actionActa.setDestination(destinationActa);
        linkActa.setAction(actionActa);
        linkActa.setPage(indicePage);


        PDRectangle positionActa = new PDRectangle();
        positionActa.setLowerLeftX(10);
        positionActa.setLowerLeftY(indicePage.getMediaBox().getHeight() - 20 * linea); 
        positionActa.setUpperRightX(300); 
        positionActa.setUpperRightY(positionActa.getLowerLeftY() - 10f); 

        linkActa.setRectangle(positionActa);
        indicePage.getAnnotations().add(linkActa);


        stream.beginText();
        stream.setFont(PDType1Font.COURIER_BOLD, 12);
        stream.newLineAtOffset(10, indicePage.getMediaBox().getHeight() - 20 * linea);

        String textoEntradaActa = "#Acta del consejo anterior" + " - Acta" + "..........." + (destinationPageActa + 1);
        stream.showText(textoEntradaActa);
        stream.endText();

        linea++;
        
        
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
