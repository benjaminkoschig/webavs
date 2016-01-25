package ch.globaz.al.business.services.models.tarif;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.tarif.CategorieTarifComplexSearchModel;

/**
 * Service de gestion de persistance des données de
 * {@link ch.globaz.al.business.models.tarif.CategorieTarifComplexModel}
 * 
 * @author jts
 */
public interface CategorieTarifComplexModelService extends JadeApplicationService {

    /**
     * Recherche d'une catégorie tarif selon le modèle passé en paramètre
     * 
     * @param searchModel
     *            modèle contenant les critères de recherche
     * @return modèle contenant le résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public CategorieTarifComplexSearchModel search(CategorieTarifComplexSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;
}
