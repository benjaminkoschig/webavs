/**
 * 
 */
package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;

/**
 * Service de gestion de persistance des donn�es du mod�le <code>RecapitulatifEntrepriseImpressionComplexModel</code>
 * 
 * @author PTA
 * 
 */
public interface RecapitulatifEntrepriseImpressionComplexModelService extends JadeApplicationService {

    /**
     * Recherche les r�capitulatifs des entreprise correspondant aux crit�res d�finis dans le mod�le de recherche
     * 
     * @param searchModel
     *            mod�le de recherche selon les crit�res d�fini
     * @return le mod�le de recherche contenant les r�sultats de la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public RecapitulatifEntrepriseImpressionComplexSearchModel search(
            RecapitulatifEntrepriseImpressionComplexSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

}
