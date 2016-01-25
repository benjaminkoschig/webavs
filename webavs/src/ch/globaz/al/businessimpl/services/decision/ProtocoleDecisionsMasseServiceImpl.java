package ch.globaz.al.businessimpl.services.decision;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.decision.ProtocoleDecisionsMasseService;
import ch.globaz.al.business.services.documents.DocumentDataContainer;
import ch.globaz.al.businessimpl.documents.AbstractProtocoleErreurs;
import ch.globaz.topaz.datajuicer.DocumentData;

public class ProtocoleDecisionsMasseServiceImpl extends AbstractProtocoleErreurs implements
        ProtocoleDecisionsMasseService {

    @Override
    public DocumentDataContainer getDocumentData(ProtocoleLogger logger, HashMap<String, String> params)
            throws JadePersistenceException, JadeApplicationException {

        DocumentDataContainer container = new DocumentDataContainer();
        container.setProtocoleLogger(logger);
        container.setDocument(this.init(logger, params));

        return container;
    }

    @Override
    public String getErrorListItemLabelId() {
        return "al.protocoles.impressionDecisionsMasse.diagnostique.dossier.label";
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("ProtocoleDecisionsMasseServiceImpl#setIdDocument");
        }

        document.addData("AL_idDocument", "AL_protocoleDecisionsMasse");
    }

    @Override
    protected void setStatsLabels(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("ProtocoleGenerationServiceImpl#setStatsLabels : document is null");
        }

        super.setStatsLabels(document);

        document.addData("protocole_stats_nbinfo_label",
                this.getText("al.protocoles.impressionDecisionsMasse.stats.nbinfo.label"));
    }

    // @Override
    // protected void setTitles(DocumentData document) throws JadeApplicationException {
    //
    // if (document == null) {
    // throw new ALProtocoleException("ProtocoleErreursPaiementDirectServiceImpl#setTitles : document is null");
    // }
    //
    // super.setTitles(document);
    //
    // document.addData("protocole_erreurs_titre",
    // this.getText("al.protocoles.paiementDirect.titre.diagnostique.erreurs"));
    // }
    /**
     * Ajoute les titres du protocole
     * 
     * @param document
     *            document dans lequel placer les données
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     */
    @Override
    protected void setTitles(DocumentData document) throws JadeApplicationException {

        super.setTitles(document);
        document.addData("protocole_titre_recap", this.getText("al.protocoles.erreurs.titre.recap"));

    }

}
