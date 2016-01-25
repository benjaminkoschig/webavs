package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.CategorieTarifComplexSearchModel;

/**
 * Service de gestion de persistance des donn�es de
 * {@link ch.globaz.al.business.models.tarif.CategorieTarifComplexModel}
 * 
 * @author jts
 */
public interface CategorieTarifComplexModelService extends JadeApplicationService {

    /**
     * Recherche d'une cat�gorie tarif selon le mod�le pass� en param�tre
     * 
     * @param searchModel
     *            mod�le contenant les crit�res de recherche
     * @return mod�le contenant le r�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CategorieTarifComplexSearchModel search(CategorieTarifComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;
}
