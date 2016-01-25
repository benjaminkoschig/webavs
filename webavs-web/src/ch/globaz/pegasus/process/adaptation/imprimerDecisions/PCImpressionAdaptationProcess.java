package ch.globaz.pegasus.process.adaptation.imprimerDecisions;

import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.pegasus.process.PCAbstractJob;
import java.util.List;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.adaptation.DefaultAdaptationBuilder;

/**
 * Modification correctfif bz 8296 --> v 1.12, 29.04.2013
 * 
 * @author sce
 * 
 */
public class PCImpressionAdaptationProcess extends PCAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    private List<CommunicationAdaptationElement> donneesContainer = null;

    /** Date sur document de décisions */
    private String dateSurDocument = null;

    @Override
    public String getDescription() {
        return "Process pour générer une décision d'adaptation";
    }

    public List<CommunicationAdaptationElement> getDonneesContainer() {
        return donneesContainer;
    }

    @Override
    public String getName() {
        return "PCImpressionAdaptationProcess";
    }

    @Override
    protected void process() throws Exception {
        try {

            // générer le document
            JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

            allDoc = new DefaultAdaptationBuilder().build(allDoc, donneesContainer, dateSurDocument);
            // Imprimer le document
            this.createDocuments(allDoc);

        } catch (Exception e) {
            JadeLogger.error("An error happened while generating the document of a Decision d'adaptation!", e);
        }
        ;
    }

    public void setDonneesContainer(List<CommunicationAdaptationElement> donneesContainer) {
        this.donneesContainer = donneesContainer;
    }

}
