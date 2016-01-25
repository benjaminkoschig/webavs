package ch.globaz.prestation.business.services.models.recap;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.prestation.business.exceptions.PrestationCommonException;
import ch.globaz.prestation.business.models.recap.RecapitulationPcRfm;

public interface RecapitulationPcRfmService extends JadeApplicationService {

    /**
     * <p>
     * Retourne la récapitulation du paiement mensuel des PC/RFM pour le mois donnée en paramètre.
     * </p>
     * <p>
     * Retournera <code>null</code> si aucune récapitulation n'est faite pour ce mois
     * </p>
     * 
     * @param date
     *            un mois de paiement au format "MM.AAAA"
     * @return la récapitulation du paiement mensuel pour le mois donnée, ou <code>null</code> si elle n'existe pas en
     *         base de données
     * @throws JadePersistenceException
     *             si un problème survient lors des accès à la base de données
     * @throws PrestationCommonException
     *             si la date n'est pas au bon format
     */
    public RecapitulationPcRfm findInfoRecapByDate(String date) throws JadePersistenceException,
            PrestationCommonException;
}
