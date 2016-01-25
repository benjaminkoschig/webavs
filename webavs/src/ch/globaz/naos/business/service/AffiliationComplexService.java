package ch.globaz.naos.business.service;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.naos.business.model.AffiliationComplexModel;
import ch.globaz.naos.business.model.AffiliationComplexModelSearch;
import ch.globaz.naos.exception.NaosException;

public interface AffiliationComplexService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * 
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws Exception
     * @throws NaosException
     */
    public int count(AffiliationComplexModelSearch search) throws JadePersistenceException, Exception;

    /**
     * Permet de charger en m�moire une AffiliationComplexService PC
     * 
     * @param idnameParam
     *            L'identifiant de la variableMetier � charger en m�moire
     * @return nameParam charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws Exception
     */
    public AffiliationComplexModel read(String idAffiliationComplexService) throws JadePersistenceException, Exception;

    /**
     * Permet de chercher des AffiliationSearchComplexModel selon un mod�le de crit�res.
     * 
     * @param AffiliationComplexModelSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws Exception
     */
    public AffiliationComplexModelSearch search(AffiliationComplexModelSearch nameParamSearch)
            throws JadePersistenceException, Exception;

}