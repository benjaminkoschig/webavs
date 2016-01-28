package ch.globaz.al.businessimpl.services.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.generation.prestations.ProtocoleErreursGenerationService;
import ch.globaz.al.businessimpl.documents.AbstractProtocoleErreurs;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service de génération de protocole d'erreurs de génération de prestations
 * 
 * @author jts
 * 
 */
public class ProtocoleErreursGenerationServiceImpl extends AbstractProtocoleErreurs implements
        ProtocoleErreursGenerationService {

    @Override
    public DocumentData getDocumentData(ProtocoleLogger logger, HashMap<String, String> params)
            throws JadePersistenceException, JadeApplicationException {

        return this.init(logger, params);
    }

    @Override
    public void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("ProtocoleErreursGenerationServiceImpl#setIdDocument : document is null");
        }

        document.addData("AL_idDocument", "AL_protocoleGenGlobale");
    }

    @Override
    public void setStatsLabels(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("ProtocoleErreursGenerationServiceImpl#setStatsLabels : document is null");
        }

        super.setStatsLabels(document);

        document.addData("protocole_stats_nbinfo_label", this.getText("al.protocoles.generation.stats.nbprest.label"));
    }

    @Override
    protected void setTitles(DocumentData document) throws JadeApplicationException {

        if (document == null) {
            throw new ALProtocoleException("ProtocoleErreursGenerationServiceImpl#setTitles : document is null");
        }

        super.setTitles(document);

        document.addData("protocole_erreurs_titre", this.getText("al.protocoles.generation.titre.diagnostique.erreurs"));
    }
}