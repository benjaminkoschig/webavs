package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;

/**
 * Interface pour les annonces de type RECEPTION (*Handler)
 * 
 * @author cbu
 * 
 */
public interface IAnnonceRPHandler {
    /**
     * Exécute le traitement de l'annonce
     * 
     * @param params
     * @return l'annonce
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public SimpleAnnonceSedex execute() throws JadeApplicationException, JadePersistenceException;
}
