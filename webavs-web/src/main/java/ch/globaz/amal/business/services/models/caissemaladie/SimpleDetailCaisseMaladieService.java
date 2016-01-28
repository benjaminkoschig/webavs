package ch.globaz.amal.business.services.models.caissemaladie;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.amal.business.exceptions.models.CaisseMaladieException;
import ch.globaz.amal.business.models.caissemaladie.SimpleDetailCaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.SimpleDetailCaisseMaladieSearch;

public interface SimpleDetailCaisseMaladieService extends JadeApplicationService {
    /**
     * Retourne le nombre de SimpleDetailCaisseMaladie trouvé
     * 
     * @param simpleDetailCaisseMaladieSearch
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public int count(SimpleDetailCaisseMaladieSearch simpleDetailCaisseMaladieSearch) throws CaisseMaladieException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleDetailCaisseMaladie
     * 
     * @param idSimpleDetailCaisseMaladie
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public SimpleDetailCaisseMaladie create(SimpleDetailCaisseMaladie simpleDetailCaisseMaladie)
            throws CaisseMaladieException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité SimpleDetailCaisseMaladie
     * 
     * @param idSimpleDetailCaisseMaladie
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public SimpleDetailCaisseMaladie delete(SimpleDetailCaisseMaladie simpleDetailCaisseMaladie)
            throws CaisseMaladieException, JadePersistenceException;

    /**
     * Permet la lecture d'une entité SimpleDetailCaisseMaladie
     * 
     * @param idSimpleDetailCaisseMaladie
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public SimpleDetailCaisseMaladie read(String idSimpleDetailCaisseMaladie) throws CaisseMaladieException,
            JadePersistenceException;

    /**
     * Permet la recherche d'une entité SimpleDetailCaisseMaladie
     * 
     * @param simpleDetailCaisseMaladieSearch
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public SimpleDetailCaisseMaladieSearch search(SimpleDetailCaisseMaladieSearch simpleDetailCaisseMaladieSearch)
            throws CaisseMaladieException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité SimpleDetailCaisseMaladie
     * 
     * @param idSimpleDetailCaisseMaladie
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public SimpleDetailCaisseMaladie update(SimpleDetailCaisseMaladie simpleDetailCaisseMaladie)
            throws CaisseMaladieException, JadePersistenceException;

}
