package ch.globaz.eform.business.services;

import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.search.GFFormulaireSearch;
import ch.globaz.eform.business.search.GFStatistiqueSearch;
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
    int count(GFFormulaireSearch search) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la création d'une entité Prestation
     *
     * @param GFFormulaireModel Le formulaireModel à créer
     * @return formulaireModel créé
     */
    GFFormulaireModel create(GFFormulaireModel GFFormulaireModel) throws JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simplePresation
     *
     * @param id L'identifiant du formulaire à supprimer
     * @return formulaireModel supprimé
     */
    boolean delete(String id) throws JadePersistenceException;

    int delete(GFFormulaireSearch gfeFormSearch) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une formulaireModel
     *
     * @param id L'identifiant du formulaireModel à charger en mémoire
     * @return formulaireModel chargée
     */
    GFFormulaireModel read(String id) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une formulaireModel avec les données blob
     *
     * @param id L'identifiant du formulaireModel à charger en mémoire
     * @return formulaireModel chargée
     */
    GFFormulaireModel readWithBlobs(String id) throws JadePersistenceException;

    /**
     * Permet de chercher des formulaires selon un modèle de critères.
     *
     * @param gfeFormSearch Le modèle de critères
     * @return Le modèle de critère avec les résultats
     */
    GFFormulaireSearch search(GFFormulaireSearch gfeFormSearch) throws JadePersistenceException;

    /**
     * Permet de chercher des formulaires selon un modèle de critères et de récupérer les données blob.
     *
     * @param GFFormulaireSearch Le modèle de critères
     * @return Le modèle de critère avec les résultats
     */
    GFFormulaireSearch searchWithBlobs(GFFormulaireSearch GFFormulaireSearch) throws JadePersistenceException;

    /**
     * Permet de chercher des formulaires selon un modèle de critères.
     *
     * @param GFStatistiqueSearch Le modèle de critères
     * @return Le modèle de critère avec les résultats
     */
    GFStatistiqueSearch search(GFStatistiqueSearch GFStatistiqueSearch) throws JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité Prestation
     *
     * @param GFFormulaireModel Le modèle à mettre à jour
     * @return simplePresation mis à jour
     */
    GFFormulaireModel update(GFFormulaireModel GFFormulaireModel) throws JadePersistenceException;

}
