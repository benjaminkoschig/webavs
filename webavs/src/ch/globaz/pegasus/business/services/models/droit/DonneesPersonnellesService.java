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
     * Copy les donn�esPersonne d'un ancien droit dans le nouveaus pass�e en param�tre
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
     * Permet la cr�ation d'une entit� donn�es personnelles
     * 
     * @param donneesPersonnelles
     *            L'entit� donneesPersonnelles � cr�er
     * @return L'entit� donneesPersonnelles cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DonneesPersonnellesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DossierException
     */
    public DonneesPersonnelles create(DonneesPersonnelles donneesPersonnelles) throws JadePersistenceException,
            DonneesPersonnellesException;

    /**
     * Permet la suppression d'une entit� donneesPersonnelles
     * 
     * @param donneesPersonnelles
     *            L'entit� donneesPersonnelles � supprimer
     * @return L'entit� donneesPersonnelles supprim�
     * @throws DonneesPersonnellesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public DonneesPersonnelles delete(DonneesPersonnelles donneesPersonnelles) throws DonneesPersonnellesException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire une entit� donneesPersonnelles
     * 
     * @param idDonneesPersonnelles
     *            L'identifiant de la donneesPersonnelles � charger en m�moire
     * @return La donneesPersonnelles charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DonneesPersonnellesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DonneesPersonnelles read(String idDonneesPersonnelles) throws JadePersistenceException,
            DonneesPersonnellesException;

    public DonneesPersonnellesSearch search(DonneesPersonnellesSearch search) throws DonneesPersonnellesException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� donneesPersonnelles
     * 
     * @param donneesPersonnelles
     *            La donneesPersonnelles � mettre � jour
     * @return La donneesPersonnelles mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DonneesPersonnellesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DossierException
     */
    public DonneesPersonnelles update(DonneesPersonnelles donneesPersonnelles) throws JadePersistenceException,
            DonneesPersonnellesException, DossierException;
}
