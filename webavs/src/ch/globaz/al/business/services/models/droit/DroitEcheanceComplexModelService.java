/**
 * 
 */
package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexSearchModel;

/**
 * Classe de gestion des services liés au modèle DroitEcheanceComplexModel
 * 
 * @author PTA
 * 
 */
public interface DroitEcheanceComplexModelService extends JadeApplicationService {

    /**
     * Récupère les données correspondant au droit <code>idDroit</code>
     * 
     * @param idDroit
     *            Id du droit à charger
     * @return Le modèle du droit chargé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitEcheanceComplexModel read(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode de recherce des droits et échéances
     * 
     * @param droitEcheanceSearch
     *            modèle complexe des droits dont l'échéance est à signaler
     * @return DroitEcheanceComplexSearchModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public DroitEcheanceComplexSearchModel search(DroitEcheanceComplexSearchModel droitEcheanceSearch)
            throws JadeApplicationException, JadePersistenceException;

}
