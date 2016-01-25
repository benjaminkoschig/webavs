package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;

/**
 * Service de gestion de la persistance des d�tails de prestation
 * 
 * @author gmo
 * @see ch.globaz.al.business.models.prestation.DetailPrestationComplexModel
 */
public interface DetailPrestationComplexModelService extends JadeApplicationService {

    /**
     * Compte les d�tails de prestation correspondant au mod�le de recherche
     * <code>detailPrestationComplexSearchModel</code>
     * 
     * 
     * @param searchModel
     *            Mod�le de recherche de d�tail prestation contenant les crit�res de recherche souhait�s
     * @return le nombre d'enregistrement trouv�s
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(DetailPrestationComplexSearchModel searchModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Recherche les d�tails de prestation correspondant au mod�le de recherche
     * <code>detailPrestationComplexSearchModel</code>
     * 
     * @param detailPrestationComplexSearchModel
     *            Mod�le de recherche de d�tail prestation contenant les crit�res de recherche souhait�s
     * @return r�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see ch.globaz.al.business.models.prestation.DetailPrestationComplexModel
     */
    public DetailPrestationComplexSearchModel search(
            DetailPrestationComplexSearchModel detailPrestationComplexSearchModel) throws JadeApplicationException,
            JadePersistenceException;

}
