package ch.globaz.pegasus.businessimpl.services.annonce.communicationocc;

import globaz.babel.api.ICTDocument;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCC;
import ch.globaz.pegasus.business.services.annonce.communicationocc.CommunicationOCCBuilder;
import ch.globaz.pegasus.businessimpl.services.lot.AbstractLotBuilder;

public class CommunicationOCCDefaultBuilder extends AbstractLotBuilder implements CommunicationOCCBuilder {

    /**
     * Methode de construction du document Chargement de l'entité, chargement du catalogue
     * 
     * @param dateRapport
     * 
     * @throws Exception
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     */
    @Override
    public JadePrintDocumentContainer build(List<CommunicationOCC> communications, JadePublishDocumentInfo pubInfos,
            String mailGest, String dateRapport) throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception {
        // objet retourné
        JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();

        // Chargement Babel
        Map<Langues, CTDocumentImpl> documentsBabel = BabelServiceLocator.getPCCatalogueTexteService()
                .searchForCommunicationOCC();
        // La langue par défaut est forcée en Français pour les communications OCC
        ICTDocument babelDoc = documentsBabel.get(Langues.Francais);

        final String noSerie = getNoSerie(dateRapport);

        // Génération des documents
        for (CommunicationOCC communicationOCC : communications) {
            allDoc = new SingleCommOCCBuilder().build(babelDoc, allDoc, communicationOCC, noSerie);
        }

        BSession session = BSessionUtil.getSessionFromThreadContext();

        pubInfos.setDocumentTitle(session.getLabel("DOC_COMMUNICATION_OCC_EMAIL_TITRE"));
        pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, mailGest);
        pubInfos.setArchiveDocument(false);
        pubInfos.setPublishDocument(true);
        pubInfos.setDocumentType(IPRConstantesExternes.PC_REF_INFOROM_COMMUNICATION_OCC);
        pubInfos.setDocumentTypeNumber(IPRConstantesExternes.PC_REF_INFOROM_COMMUNICATION_OCC);
        pubInfos.setDocumentDate(dateRapport);
        pubInfos.setDocumentSubject(session.getLabel("DOC_COMMUNICATION_OCC_EMAIL_TITRE"));

        // fusion du doc original dans le conteneur
        allDoc.setMergedDocDestination(pubInfos);

        // on retourne le container
        return allDoc;
    }

    private String getNoSerie(String dateRapport) {
        Calendar cal = JadeDateUtil.getGlobazCalendar(dateRapport);
        return Integer.toString(cal.get(Calendar.YEAR)) + String.format("%02d", cal.get(Calendar.WEEK_OF_YEAR));
    }
}
