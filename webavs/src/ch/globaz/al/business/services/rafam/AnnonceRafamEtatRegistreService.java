package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service de gestion de l'état du registre du RAFam
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamEtatRegistreService extends JadeApplicationService {

    /**
     * Exécute le traitement d'un état du registre pour un recordNumber.
     * 
     * @param recordNumber
     *            record number pour lequel le traitement doit être exécuté
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void cleanWithEtatRegistre(String recordNumber) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces de type état du registre pour un record number
     * 
     * @param recordNumber
     *            Record number pour lequel supprimer les annonces
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void deleteAnnonceEtatRegistre(String recordNumber) throws JadeApplicationException,
            JadePersistenceException;
}