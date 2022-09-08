package ch.globaz.eform.business.services;

import ch.globaz.common.validation.ValidationResult;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public interface GFDaDossierDBService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     *
     * @param search modèle de recherche
     * @return nombre d'enregistrements trouvés
     */
    int count(GFDaDossierSearch search) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la création d'une entité Prestation
     *
     * @param gfDaDossierModel Le formulaireModel à créer
     * @return formulaireModel créé
     */
    GFDaDossierModel create(GFDaDossierModel gfDaDossierModel) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la création d'une entité Prestation
     *
     * @param gfDaDossierModel Le formulaireModel à créer
     * @param validationResult permet de remonter la validation des informations
     * @return formulaireModel créé
     */
    GFDaDossierModel create(GFDaDossierModel gfDaDossierModel, ValidationResult validationResult) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simplePresation
     *
     * @param id L'identifiant du formulaire à supprimer
     * @return formulaireModel supprimé
     */
    boolean delete(String id) throws JadePersistenceException;

    int delete(GFDaDossierSearch gfDaDossierSearch) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une formulaireModel
     *
     * @param id L'identifiant du formulaireModel à charger en mémoire
     * @return formulaireModel chargée
     */
    GFDaDossierModel read(String id) throws JadePersistenceException;

    /**
     * Permet de chercher des formulaires selon un modèle de critères.
     *
     * @param gfeFormSearch Le modèle de critères
     * @return Le modèle de critère avec les résultats
     */
    GFDaDossierSearch search(GFDaDossierSearch gfeFormSearch) throws JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité Prestation
     *
     * @param gfDaDossierModel Le modèle à mettre à jour
     * @return simplePresation mis à jour
     */
    GFDaDossierModel update(GFDaDossierModel gfDaDossierModel) throws JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité Prestation
     *
     * @param gfDaDossierModel Le modèle à mettre à jour
     * @param validationResult permet de remonter la validation des informations
     * @return simplePresation mis à jour
     */
    GFDaDossierModel update(GFDaDossierModel gfDaDossierModel, ValidationResult validationResult) throws JadePersistenceException;

}
