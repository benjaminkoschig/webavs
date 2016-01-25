/**
 * 
 */
package ch.globaz.amal.business.services.models.caissemaladie;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.amal.business.exceptions.models.CaisseMaladieException;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieGroupeRCListeSearch;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;

/**
 * @author cbu
 * 
 */
public interface CaisseMaladieService extends JadeApplicationService {
    /**
     * Retourne le nombre de CaisseMaladie trouvé
     * 
     * @param caisseMaladieSearch
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public int count(CaisseMaladieSearch caisseMaladieSearch) throws CaisseMaladieException, JadePersistenceException;

    /**
     * Crée et retourne le fichier cosama des éléments annoncés pour une date donnée, par caisse maladie
     * 
     * @param dateAnnonce
     *            date de l'annonce au format jj.mm.yyyy
     * @param idTiersCM
     *            idTiers de la caisse maladie concernée
     * @return
     *         URL du fichier généré
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public String createFichierCosamaAnnonce(String dateAnnonce, String idTiersCM) throws CaisseMaladieException,
            JadePersistenceException;

    /**
     * Crée et retourne une liste des éléments annoncés pour une date donnée, par caisse maladie
     * 
     * @param dateAnnonce
     *            date de l'annonce au format jj.mm.yyyy
     * @param idTiersCM
     *            idTiers de la caisse maladie concernée
     * @return
     *         URL du fichier généré
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public String createFichierListeAnnonce(String dateAnnonce, String idTiersCM) throws CaisseMaladieException,
            JadePersistenceException;

    /**
     * Permet la lecture d'une entité CaisseMaladie
     * 
     * @param idCaisseMaladie
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public CaisseMaladie read(String idCaisseMaladie) throws CaisseMaladieException, JadePersistenceException;

    /**
     * Permet la recherche d'une entité CaisseMaladie
     * 
     * @param caisseMaladieSearch
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public CaisseMaladieSearch search(CaisseMaladieSearch caisseMaladieSearch) throws CaisseMaladieException,
            JadePersistenceException;

    /**
     * Permet la recherche des groupes de caisse maladie
     * 
     * @param caisseMaladieSearch
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public CaisseMaladieGroupeRCListeSearch searchGroupe(
            CaisseMaladieGroupeRCListeSearch caisseMaladieGroupeRCListeSearch) throws CaisseMaladieException,
            JadePersistenceException;

}
