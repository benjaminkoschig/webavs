package ch.globaz.vulpecula.documents.catalog;

import ch.globaz.common.util.GenerationQRCode;
import globaz.framework.printing.itext.api.FWIImporterInterface;
import globaz.framework.util.FWMessage;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import ch.globaz.vulpecula.application.ApplicationConstants;

/**
 * <p>
 * Classe abstraite permettant la génération de document iText pour le module Vulpecula.
 * 
 * <p>
 * 
 * <p>
 * La classe générique utilisée doit implémentée être serializable.
 * <p>
 * La classe concrète étendant cette classe doit implémenter deux constructeurs obligatoirement :
 * <ul>
 * <li>{@link #DocumentManager()} -> A ne pas utiliser mais nécessaire pour le serveur de job. Celle-ci doit
 * OBLIGATOIREMENT faire appel au constructeur ci-dessous this(null) !!!
 * <li>{@link #DocumentManager(List, String, String)} -> A utiliser pour créer un processus
 * </ul>
 * 
 * @author Arnaud Geiser (AGE) | Créé le 4 juin 2014
 * 
 */
public abstract class VulpeculaDocumentManager<T extends Serializable> extends DocumentManager<T> {

    private static final long serialVersionUID = 1L;

    public VulpeculaDocumentManager() throws Exception {
        super(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA, ApplicationConstants.APPLICATION_VULPECULA_REP);
    }

    public VulpeculaDocumentManager(final T element, final String documentName, final String numeroInforom)
            throws Exception {
        super(ApplicationConstants.DEFAULT_APPLICATION_VULPECULA, ApplicationConstants.APPLICATION_VULPECULA_REP,
                element, documentName, numeroInforom);
    }

    /**
     * Détermine le total de page d'un document afin de pouvoir gérer les X dans les BVR.
     * Pour cela, on construit en mémoire le document.
     */
    protected void computeTotalPage() {
        int nbPages = 0;
        FWIImporterInterface importDoc = super.getImporter();
        try {
            String sourceFilename = importDoc.getImportPath() + "/" + getJasperTemplate() + importDoc.getImportType();

            // On construit le document pour connaitre le nb de page total
            JasperPrint m_document = JasperFillManager.fillReport(sourceFilename, importDoc.getParametre(),
                    getDataSource());
            if ((m_document != null)) {
                nbPages = m_document.getPages().size();
            }

            // On recharge le data source
            createDataSource();
        } catch (Exception e) {
            getMemoryLog().logMessage("Problème pour déterminer le nb de page total du document : " + e.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        // On passe le nb de page au document
        setParametres("P_NOMBRE_PAGES", nbPages);
    }

    @Override
    public void afterExecuteReport() {
        super.afterExecuteReport();
        try {
            GenerationQRCode.deleteQRCodeImage();
        } catch (IOException e) {
            getMemoryLog().logMessage("Erreur lors de la suppression de l'image QR-Code : " + e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }
    }
}
