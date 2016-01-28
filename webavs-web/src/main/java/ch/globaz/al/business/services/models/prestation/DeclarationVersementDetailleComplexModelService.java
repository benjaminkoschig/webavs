package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;

/**
 * Service de gestion de persistance des données du modèle <code>DeclarationVersementDetailleComplexModel</code>
 * 
 * @author PTA
 * 
 */
public interface DeclarationVersementDetailleComplexModelService extends JadeApplicationService {

    /**
     * Recherche sur les prestations correspondant aux critères de recherche d'une déclaration de versement détaillée
     * 
     * @param searchModel
     *            modèle de recherche pour une déclaration de versment détaillé
     * @return le modèle de recherche contenant les résultats de la recherche
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    public DeclarationVersementDetailleSearchComplexModel search(
            DeclarationVersementDetailleSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

}
