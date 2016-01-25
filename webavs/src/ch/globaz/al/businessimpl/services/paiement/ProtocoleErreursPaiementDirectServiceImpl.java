package ch.globaz.al.businessimpl.services.paiement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.business.services.paiement.ProtocoleErreursPaiementDirectService;
import ch.globaz.al.businessimpl.documents.AbstractProtocoleErreurs;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe d'implémentation du service de génération de protocole d'erreurs de des protocoles des simulations de paiement
 * direct
 * 
 * @author PTA
 * 
 */
public class ProtocoleErreursPaiementDirectServiceImpl extends AbstractProtocoleErreurs implements
        ProtocoleErreursPaiementDirectService {

    @Override
    public DocumentDataContainer getDocumentData(ProtocoleLogger logger, HashMap params)
            throws JadePersistenceException, JadeApplicationException {

        DocumentDataContainer container = new DocumentDataContainer();
        container.setProtocoleLogger(logger);
        container.setDocument(this.init(logger, params));

        return container;
    }

    @Override
    public String getErrorListItemLabelId() {
        return "al.protocoles.paiementDirect.diagnostique.dossier.label";
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        // contrôle des paramètres
        if (document == null) {
            throw new ALProtocoleException("ProtocoleErreursPaiementDirectServiceImpl#setIdDocument");
        }
        document.addData("AL_idDocument", "AL_protocoleErreursPaiementDirect");

    }

    @Override
    public void setStatsLabels(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException(
                    "ProtocoleErreursPaiementDirectServiceImpl#setStatsLabels : document is null");
        }

        super.setStatsLabels(document);

        document.addData("protocole_stats_nbinfo_label",
                this.getText("al.protocoles.paiementdirect.stats.nbpaiement.label"));
    }

    @Override
    protected void setTitles(DocumentData document) throws JadeApplicationException {

        if (document == null) {
            throw new ALProtocoleException("ProtocoleErreursPaiementDirectServiceImpl#setTitles : document is null");
        }

        super.setTitles(document);

        document.addData("protocole_erreurs_titre",
                this.getText("al.protocoles.paiementDirect.titre.diagnostique.erreurs"));
    }

}
