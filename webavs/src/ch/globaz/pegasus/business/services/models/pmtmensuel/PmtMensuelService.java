package ch.globaz.pegasus.business.services.models.pmtmensuel;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;

public interface PmtMensuelService extends JadeApplicationService {

    /**
     * Donne la date du dernier paiement mensuel
     * 
     * @return date au format mm.aaaa
     */
    public String getDateDernierPmt() throws PmtMensuelException;

    /**
     * Donne la date du prochain paiement mensuel
     * 
     * @return date au format mm.aaaa
     */
    public String getDateProchainPmt() throws PmtMensuelException;

    /**
     * 
     * @return true si la validation des décisions est autorisee, sinon false.
     */
    public boolean isValidationDecisionAuthorise() throws PmtMensuelException;

}
