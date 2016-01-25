package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service de gestion de l'�tat du registre du RAFam
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamEtatRegistreService extends JadeApplicationService {

    /**
     * Ex�cute le traitement d'un �tat du registre pour un recordNumber.
     * 
     * @param recordNumber
     *            record number pour lequel le traitement doit �tre ex�cut�
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void cleanWithEtatRegistre(String recordNumber) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces de type �tat du registre pour un record number
     * 
     * @param recordNumber
     *            Record number pour lequel supprimer les annonces
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void deleteAnnonceEtatRegistre(String recordNumber) throws JadeApplicationException,
            JadePersistenceException;
}