package ch.globaz.prestation.business.services.models.recap;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;
import ch.globaz.prestation.business.models.recap.RecapitulationPcRfm;

public interface RecapitulationPcRfmService extends JadeApplicationService {

    /**
     * <p>
     * Retourne la r�capitulation du paiement mensuel des PC/RFM pour le mois donn�e en param�tre.
     * </p>
     * <p>
     * Retournera <code>null</code> si aucune r�capitulation n'est faite pour ce mois
     * </p>
     * 
     * @param date
     *            un mois de paiement au format "MM.AAAA"
     * @return la r�capitulation du paiement mensuel pour le mois donn�e, ou <code>null</code> si elle n'existe pas en
     *         base de donn�es
     * @throws JadePersistenceException
     *             si un probl�me survient lors des acc�s � la base de donn�es
     * @throws PrestationCommonException
     *             si la date n'est pas au bon format
     */
    public RecapitulationPcRfm findInfoRecapByDate(String date) throws JadePersistenceException,
            PrestationCommonException;
}
