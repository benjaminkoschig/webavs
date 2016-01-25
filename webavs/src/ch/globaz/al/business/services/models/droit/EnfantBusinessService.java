package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service fournissant des méthodes métier liées à l'enfant
 * 
 * @author GMO
 */
public interface EnfantBusinessService extends JadeApplicationService {
    /**
     * Détermine si l'enfant est déjà lié à un droit actif ( donne déjà droit)
     * 
     * @param idEnfant
     *            id de l'enfant à vérifier
     * @return Nombre de droit actifs pour l'enfant passé en paramètre
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int getNombreDroitsActifs(String idEnfant) throws JadeApplicationException, JadePersistenceException;

    /**
     * Détermine si l'enfant est déjà lié à un droit actif ( donne déjà droit)
     * 
     * @param idEnfant
     *            id de l'enfant à vérifier
     * @return Nombre de droit actifs pour l'enfant passé en paramètre
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int getNombreDroitsFormationActifs(String idEnfant) throws JadeApplicationException,
            JadePersistenceException;
}
