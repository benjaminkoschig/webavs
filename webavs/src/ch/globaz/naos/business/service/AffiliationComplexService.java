package ch.globaz.naos.business.service;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.naos.business.model.AffiliationComplexModel;
import ch.globaz.naos.business.model.AffiliationComplexModelSearch;
import ch.globaz.naos.exception.NaosException;

public interface AffiliationComplexService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * 
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws Exception
     * @throws NaosException
     */
    public int count(AffiliationComplexModelSearch search) throws JadePersistenceException, Exception;

    /**
     * Permet de charger en mémoire une AffiliationComplexService PC
     * 
     * @param idnameParam
     *            L'identifiant de la variableMetier à charger en mémoire
     * @return nameParam chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws Exception
     */
    public AffiliationComplexModel read(String idAffiliationComplexService) throws JadePersistenceException, Exception;

    /**
     * Permet de chercher des AffiliationSearchComplexModel selon un modèle de critères.
     * 
     * @param AffiliationComplexModelSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws Exception
     */
    public AffiliationComplexModelSearch search(AffiliationComplexModelSearch nameParamSearch)
            throws JadePersistenceException, Exception;

}