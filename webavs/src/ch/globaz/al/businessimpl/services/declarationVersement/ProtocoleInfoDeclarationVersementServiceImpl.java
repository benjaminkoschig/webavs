package ch.globaz.al.businessimpl.services.declarationVersement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.declarationVersement.ProtocoleInfoDeclarationVersementService;
import ch.globaz.al.businessimpl.documents.AbstractProtocoleErreurs;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service de génération de protocole d'info des déclarations de versement
 * 
 * @author pta
 * 
 */
public class ProtocoleInfoDeclarationVersementServiceImpl extends AbstractProtocoleErreurs implements
        ProtocoleInfoDeclarationVersementService {

    @Override
    public DocumentData getDocumentData(ProtocoleLogger logger, HashMap params) throws JadePersistenceException,
            JadeApplicationException {

        return this.init(logger, params);
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException(
                    "ProtocoleInfoDeclarationVersementServiceImpl#setIdDocument : document is null");
        }
        document.addData("AL_idDocument", "AL_protocoleDeclarationVersement");

    }

    @Override
    protected void setStatsLabels(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException(
                    "ProtocoleInfoDeclarationVersementServiceImpl#setStatsLabels : document is null");
        }

        super.setStatsLabels(document);

        document.addData("protocole_stats_nbinfo_label",
                this.getText("al.protocoles.declarationVersement.stats.nbInfo.label"));
    }

}
