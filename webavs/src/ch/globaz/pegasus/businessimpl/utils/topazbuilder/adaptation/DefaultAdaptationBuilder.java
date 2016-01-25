package ch.globaz.pegasus.businessimpl.utils.topazbuilder.adaptation;

import globaz.externe.IPRConstantesExternes;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.util.PegasusPubInfoBuilder;
import ch.globaz.pegasus.process.adaptation.imprimerDecisions.CommunicationAdaptationElement;

public class DefaultAdaptationBuilder extends AbstractPegasusBuilder {

    Map<Langues, CTDocumentImpl> documentsBabel = new HashMap<Langues, CTDocumentImpl>();

    public JadePrintDocumentContainer build(JadePrintDocumentContainer allDoc,
            List<CommunicationAdaptationElement> donneesContainer, String dateSurDoc) throws TransfertDossierException {

        JadePublishDocumentInfo pubInfo = getMainPubInfo();
        allDoc.setMergedDocDestination(pubInfo);

        loadDBEntity(); // crée document

        for (CommunicationAdaptationElement source : donneesContainer) {

            allDoc = new SingleAdaptationBuilder().build(documentsBabel, allDoc, source, dateSurDoc);
        }

        return allDoc;
    }

    private JadePublishDocumentInfo getMainPubInfo() {
        JadePublishDocumentInfo pubInfo = new PegasusPubInfoBuilder().publish().getPubInfo();
        // String title = "Adaptation de fin d'année";

        pubInfo.setDocumentType(IPRConstantesExternes.PC_REF_INFOROM_COMMUNICATION_ADAPTATION);
        pubInfo.setDocumentTypeNumber(IPRConstantesExternes.PC_REF_INFOROM_COMMUNICATION_ADAPTATION);
        // pubInfo.setDocumentDate(JadeDateUtil.getFormattedDate(new Date()));

        // pubInfo.setDocumentTitle(title);
        // pubInfo.setDocumentSubject(title);
        return pubInfo;
    }

    private void loadDBEntity() throws TransfertDossierException {

        // Chargement Babel
        try {
            documentsBabel = BabelServiceLocator.getPCCatalogueTexteService().searchForAdaptationImpression();
        } catch (Exception e) {
            throw new TransfertDossierException("Error while loading catalogue Babel!", e);
        }

    }

}
