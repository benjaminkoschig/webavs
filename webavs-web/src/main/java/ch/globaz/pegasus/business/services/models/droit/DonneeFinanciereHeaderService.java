/**
 * 
 */
package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;

/**
 * @author SCE
 * 
 *         8 juil. 2010
 */
public interface DonneeFinanciereHeaderService extends JadeApplicationService {
    /**
     * Copy toute les donn�es fianci�res sur le droit donn�e en param�tre a condition qu'il avait un encien droit pour
     * le requ�rant
     * 
     * @param newDroit
     * @throws DonneeFinanciereException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public void copyDonneeFinanciere(Droit newDroit, Droit oldDroit,
            DroitMembreFamilleSearch newdroitMembreFamilleSearch) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param sdfSearch
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(DonneeFinanciereHeaderSearch sdfSearch) throws DroitException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� donneeFinanciereHeader
     * 
     * @param donneeFinanciereHeader
     *            L'entit� donneeFinanciereHeader � supprimer
     * @return L'entit� donneeFinanciereHeader supprim�
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public DonneeFinanciereHeader delete(DonneeFinanciereHeader donneeFinanciereHeader) throws DroitException,
            JadePersistenceException;

    /**
     * Supprimer les donn�es fianci�re pour une version de droit
     * 
     * @param idVersionDroit
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     * @throws JadeApplicationServiceNotAvailableException
     */

    public void deleteDonneFinancierByIdVersionDroit(String idVersionDroit) throws JadePersistenceException,
            DonneeFinanciereException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire une entit� donneeFinanciereHeader
     * 
     * @param iddonneeFinanciereHeader
     *            L'identifiant de la donneeFinanciereHeader � charger en m�moire
     * @return La donneeFinanciereHeader charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DroutException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DonneeFinanciereHeader read(String idDonneeFinanciereHeader) throws JadePersistenceException, DroitException;

    public DonneeFinanciereHeaderSearch search(DonneeFinanciereHeaderSearch search) throws DonneeFinanciereException,
            JadePersistenceException;

    /**
     * Permet de setter les valeurs g�n�riques pour les donn�es financi�re lors de la cr�ation.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param simpleDonneeFinanciereHeader
     * @return SimpleDonneeFinanciereHeader
     * @throws JadePersistenceException
     */
    public SimpleDonneeFinanciereHeader setDonneeFinanciereHeaderForCreation(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader)
            throws JadePersistenceException;

    /**
     * Permet de d�finir si on veut que la donn�e financi�res soit prise oui ou non dans le calcule On passe l'idVersion
     * droit pour v�rifi� que l'on trait sur le bon droit dans un bon �tat.
     * 
     * @param idDonneeFinanciereHeader
     * @param idVersionDroit
     * @return
     * @throws DonneeFinanciereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public SimpleDonneeFinanciereHeader toggleTookInCalculating(String idDonneeFinanciereHeader, String idVersionDroit)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DroitException;

    /**
     * 
     * Permet la mise � jour d'une entit� donneeFinanciereHeader
     * 
     * @param donneeFinanciereHeader
     *            La donneeFinanciereHeader � mettre � jour
     * @return La ddonneeFinanciereHeader mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DroitException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DossierException
     */
    public DonneeFinanciereHeader update(DonneeFinanciereHeader donneeFinanciereHeader)
            throws JadePersistenceException, DroitException;

}
