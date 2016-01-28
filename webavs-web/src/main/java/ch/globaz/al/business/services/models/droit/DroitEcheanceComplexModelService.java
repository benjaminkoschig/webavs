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
 * Classe de gestion des services li�s au mod�le DroitEcheanceComplexModel
 * 
 * @author PTA
 * 
 */
public interface DroitEcheanceComplexModelService extends JadeApplicationService {

    /**
     * R�cup�re les donn�es correspondant au droit <code>idDroit</code>
     * 
     * @param idDroit
     *            Id du droit � charger
     * @return Le mod�le du droit charg�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @see ch.globaz.al.business.models.droit.DroitComplexModel
     */
    public DroitEcheanceComplexModel read(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode de recherce des droits et �ch�ances
     * 
     * @param droitEcheanceSearch
     *            mod�le complexe des droits dont l'�ch�ance est � signaler
     * @return DroitEcheanceComplexSearchModel
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public DroitEcheanceComplexSearchModel search(DroitEcheanceComplexSearchModel droitEcheanceSearch)
            throws JadeApplicationException, JadePersistenceException;

}
