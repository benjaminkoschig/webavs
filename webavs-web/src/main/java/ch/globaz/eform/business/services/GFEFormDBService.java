package ch.globaz.eform.business.services;

import ch.globaz.eform.business.models.GFEFormModel;
import ch.globaz.eform.business.search.GFEFormSearch;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public interface GFEFormDBService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     *
     * @param search modèle de recherche
     * @return nombre d'enregistrements trouvés
     */
    int count(GFEFormSearch search) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la création d'une entité Prestation
     *
     * @param gfeFormModel Le formulaireModel à créer
     * @return formulaireModel créé
     */
    GFEFormModel create(GFEFormModel gfeFormModel) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simplePresation
     *
     * @param id L'identifiant du formulaire à supprimer
     * @return formulaireModel supprimé
     */
    boolean delete(String id) throws JadePersistenceException;

    int delete(GFEFormSearch gfeFormSearch) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une formulaireModel
     *
     * @param id L'identifiant du formulaireModel à charger en mémoire
     * @return formulaireModel chargée
     */
    GFEFormModel read(String id) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une formulaireModel avec les données blob
     *
     * @param id L'identifiant du formulaireModel à charger en mémoire
     * @return formulaireModel chargée
     */
    GFEFormModel readWithBlobs(String id) throws JadePersistenceException;

    /**
     * Permet de chercher des formulaires selon un modèle de critères.
     *
     * @param gfeFormSearch Le modèle de critères
     * @return Le modèle de critère avec les résultats
     */
    GFEFormSearch search(GFEFormSearch gfeFormSearch) throws JadePersistenceException;

    /**
     * Permet de chercher des formulaires selon un modèle de critères et de récupérer les données blob.
     *
     * @param gfeFormSearch Le modèle de critères
     * @return Le modèle de critère avec les résultats
     */
    GFEFormSearch searchWithBlobs(GFEFormSearch gfeFormSearch) throws JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité Prestation
     *
     * @param gfeFormModel Le modèle à mettre à jour
     * @return simplePresation mis à jour
     */
    GFEFormModel update(GFEFormModel gfeFormModel) throws JadePersistenceException;

}
