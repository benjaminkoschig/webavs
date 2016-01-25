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
     * Retourne le nombre de CaisseMaladie trouv�
     * 
     * @param caisseMaladieSearch
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public int count(CaisseMaladieSearch caisseMaladieSearch) throws CaisseMaladieException, JadePersistenceException;

    /**
     * Cr�e et retourne le fichier cosama des �l�ments annonc�s pour une date donn�e, par caisse maladie
     * 
     * @param dateAnnonce
     *            date de l'annonce au format jj.mm.yyyy
     * @param idTiersCM
     *            idTiers de la caisse maladie concern�e
     * @return
     *         URL du fichier g�n�r�
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public String createFichierCosamaAnnonce(String dateAnnonce, String idTiersCM) throws CaisseMaladieException,
            JadePersistenceException;

    /**
     * Cr�e et retourne une liste des �l�ments annonc�s pour une date donn�e, par caisse maladie
     * 
     * @param dateAnnonce
     *            date de l'annonce au format jj.mm.yyyy
     * @param idTiersCM
     *            idTiers de la caisse maladie concern�e
     * @return
     *         URL du fichier g�n�r�
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public String createFichierListeAnnonce(String dateAnnonce, String idTiersCM) throws CaisseMaladieException,
            JadePersistenceException;

    /**
     * Permet la lecture d'une entit� CaisseMaladie
     * 
     * @param idCaisseMaladie
     * @return
     * @throws CaisseMaladieException
     * @throws JadePersistenceException
     */
    public CaisseMaladie read(String idCaisseMaladie) throws CaisseMaladieException, JadePersistenceException;

    /**
     * Permet la recherche d'une entit� CaisseMaladie
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
