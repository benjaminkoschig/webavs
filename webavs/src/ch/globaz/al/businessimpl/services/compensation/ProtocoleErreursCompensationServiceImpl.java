package ch.globaz.al.businessimpl.services.compensation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.compensation.ProtocoleErreursCompensationService;
import ch.globaz.al.businessimpl.documents.AbstractProtocoleErreurs;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service de génération de protocole d'erreurs de compensation de prestations
 * 
 * @author jts
 * 
 */
public class ProtocoleErreursCompensationServiceImpl extends AbstractProtocoleErreurs implements
        ProtocoleErreursCompensationService {

    @Override
    public DocumentData getDocumentData(ProtocoleLogger logger, HashMap<String, String> params)
            throws JadePersistenceException, JadeApplicationException {

        return this.init(logger, params);
    }

    @Override
    public String getErrorListItemLabelId() {
        return "al.protocoles.compensation.diagnostique.affilie.label";
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("ProtocoleErreursCompensationServiceImpl#setIdDocument : document is null");
        }

        document.addData("AL_idDocument", "AL_protocoleErreursCompensation");
    }

    @Override
    protected void setStatsLabels(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("ProtocoleGenerationServiceImpl#setStatsLabels : document is null");
        }

        super.setStatsLabels(document);

        document.addData("protocole_stats_nbinfo_label", this.getText("al.protocoles.compensation.stats.nbinfo.label"));
    }

    @Override
    protected void setTitles(DocumentData document) throws JadeApplicationException {

        if (document == null) {
            throw new ALProtocoleException("AbstractProtocoleErreurs#setTitles : document is null");
        }

        super.setTitles(document);

        document.addData("protocole_erreurs_titre",
                this.getText("al.protocoles.compensation.titre.diagnostique.erreurs"));
    }
}
