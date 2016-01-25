package ch.globaz.al.business.services.documents;

import globaz.jade.print.server.JadePrintDocumentContainer;
import java.util.ArrayList;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe conteneur permettant de stocker un <code>DocumentData</code> et un <code>ProtocoleLogger</code> ou un
 * <code>JadePrintDocumentContainer</code>
 * 
 * @author PTA
 * 
 */
public class DocumentDataContainer {

    /**
     * Conteneur de documents
     */
    private JadePrintDocumentContainer container = null;
    /**
     * Declaration de versement
     */
    private DocumentData document = null;
    /**
     * Document CSV
     */
    private ArrayList<String> documentCSV = null;

    /**
     * Protocole lié au declaration de versement
     */
    private ProtocoleLogger logger = null;

    /**
     * @return the container
     */
    public JadePrintDocumentContainer getContainer() {
        return container;
    }

    /**
     * @return the documentDeclaration
     */
    public DocumentData getDocument() {
        return document;
    }

    /**
     * Retourne le contenu du document CSV
     * 
     * @return contenu du document CSV
     */
    public ArrayList<String> getDocumentCSV() {
        return documentCSV;
    }

    /**
     * @return the protocoleDeclaration
     */
    public ProtocoleLogger getProtocoleLogger() {
        return logger;
    }

    /**
     * @param container
     *            the container to set
     */
    public void setContainer(JadePrintDocumentContainer container) {
        this.container = container;
    }

    /**
     * @param document
     *            the documentDeclaration to set
     */
    public void setDocument(DocumentData document) {
        this.document = document;
    }

    /**
     * Définit le contenu list docemnt CSV
     * 
     * @param documentCSV
     *            contenu du document CSV
     */
    public void setDocumentCSV(ArrayList<String> documentCSV) {
        this.documentCSV = documentCSV;
    }

    /**
     * @param logger
     *            the protocoleDeclaration to set
     */
    public void setProtocoleLogger(ProtocoleLogger logger) {
        this.logger = logger;
    }
}
