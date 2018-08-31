package ch.globaz.vulpecula.facturation;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.musca.api.IFAPassage;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.ap.DocumentFactureAPPrinter;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.association.FactureAssociation;

/**
 * Impression des factures d'associations professionnelles
 * 
 * @since WebBMS 2.1
 */
public class PTImpressionFactureAssociationsProfImpl extends PTImpression {

    @Override
    public boolean comptabiliser(IFAPassage passage, BProcess context) throws Exception {
        boolean success = true;
        String errorMessage = "";

        try {
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(context.getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            List<FactureAssociation> listFactureAssociation = VulpeculaRepositoryLocator
                    .getFactureAssociationRepository().findByIdPassageFacturation(passage.getIdPassage()).getFactures();

            if (!listFactureAssociation.isEmpty()) {
                DocumentFactureAPPrinter printer = new DocumentFactureAPPrinter();
                printer.setParentWithCopy(context);
                printer.setEMailAddress(context.getEMailAddress());
                printer.setIds(DocumentPrinter.getIds(listFactureAssociation));
                printer.executeProcess();
            }

        } catch (Exception e) {
            success = false;
            errorMessage = e.toString();
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        if (!success || context.isAborted() || context.isOnError() || context.getSession().hasErrors()) {
            success = false;

            // permet de ne pas envoyer le mail contenant le pdf avec l'ensemble des décisions
            context.getAttachedDocuments().clear();
            context.getMemoryLog().logMessage(errorMessage, FWMessage.FATAL, this.getClass().getName());
        }

        return success;
    }
}
