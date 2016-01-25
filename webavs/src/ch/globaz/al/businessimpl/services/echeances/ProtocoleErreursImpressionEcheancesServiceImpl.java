package ch.globaz.al.businessimpl.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.echeances.ProtocoleErreursImpressionEcheancesService;
import ch.globaz.al.businessimpl.documents.AbstractProtocoleErreurs;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service de génération des erreurs pour l'impression des avis d'échéances
 * 
 * @author PTA
 * 
 */
public class ProtocoleErreursImpressionEcheancesServiceImpl extends AbstractProtocoleErreurs implements
        ProtocoleErreursImpressionEcheancesService {

    @Override
    public DocumentData getDocumentData(ProtocoleLogger logger, HashMap params) throws JadePersistenceException,
            JadeApplicationException {

        return this.init(logger, params);

    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        // contrôle paramètres
        if (document == null) {
            throw new ALProtocoleException(
                    "ProtocoleErreursImpressionEcheancesServiceImpl#setIdDocument: document is null");
        }
        document.addData("AL_idDocument", "AL_protocolesErreursImpressionEcheances");

    }

    @Override
    protected void setStatsLabels(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException(
                    "ProtocoleErreursImpressionEcheancesServiceImpl#setStatsLabels : document is null");
        }

        super.setStatsLabels(document);

        document.addData("protocole_stats_nbinfo_label",
                this.getText("al.protocoles.ImpressionAvisEcheances.stats.nbInfo.label"));
    }

}
