/**
 * 
 */
package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnellesSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;

/**
 * @author BSC
 * 
 */
public interface DonneesPersonnellesService extends JadeApplicationService {

    /**
     * Copy les donnéesPersonne d'un ancien droit dans le nouveaus passée en paramétre
     * 
     * @param newDroit
     * @param oldDroit
     * @param newdroitMembreFamilleSearch
     * @throws DonneesPersonnellesException
     * @throws JadePersistenceException
     */
    public void copyAllDonneesPersonelleByDroit(Droit newDroit, Droit oldDroit,
            DroitMembreFamilleSearch newdroitMembreFamilleSearch) throws DonneesPersonnellesException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité données personnelles
     * 
     * @param donneesPersonnelles
     *            L'entité donneesPersonnelles à créer
     * @return L'entité donneesPersonnelles créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DonneesPersonnellesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DossierException
     */
    public DonneesPersonnelles create(DonneesPersonnelles donneesPersonnelles) throws JadePersistenceException,
            DonneesPersonnellesException;

    /**
     * Permet la suppression d'une entité donneesPersonnelles
     * 
     * @param donneesPersonnelles
     *            L'entité donneesPersonnelles à supprimer
     * @return L'entité donneesPersonnelles supprimé
     * @throws DonneesPersonnellesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public DonneesPersonnelles delete(DonneesPersonnelles donneesPersonnelles) throws DonneesPersonnellesException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire une entité donneesPersonnelles
     * 
     * @param idDonneesPersonnelles
     *            L'identifiant de la donneesPersonnelles à charger en mémoire
     * @return La donneesPersonnelles chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DonneesPersonnellesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DonneesPersonnelles read(String idDonneesPersonnelles) throws JadePersistenceException,
            DonneesPersonnellesException;

    public DonneesPersonnellesSearch search(DonneesPersonnellesSearch search) throws DonneesPersonnellesException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité donneesPersonnelles
     * 
     * @param donneesPersonnelles
     *            La donneesPersonnelles à mettre à jour
     * @return La donneesPersonnelles mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DonneesPersonnellesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DossierException
     */
    public DonneesPersonnelles update(DonneesPersonnelles donneesPersonnelles) throws JadePersistenceException,
            DonneesPersonnellesException, DossierException;
}
