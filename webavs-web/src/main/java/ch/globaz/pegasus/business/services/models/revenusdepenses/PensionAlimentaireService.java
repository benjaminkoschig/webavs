package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.PensionAlimentaireException;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaire;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaireSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface PensionAlimentaireService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws PensionAlimentaireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(PensionAlimentaireSearch search) throws PensionAlimentaireException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� PensionAlimentaire
     * 
     * @param PensionAlimentaire
     *            L'entit� PensionAlimentaire � cr�er
     * @return L'entit� PensionAlimentaire cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PensionAlimentaire create(PensionAlimentaire pensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� PensionAlimentaire
     * 
     * @param PensionAlimentaire
     *            L'entit� PensionAlimentaire � supprimer
     * @return L'entit� PensionAlimentaire supprim�
     * @throws PensionAlimentaireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public PensionAlimentaire delete(PensionAlimentaire pensionAlimentaire) throws PensionAlimentaireException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� PensionAlimentaire
     * 
     * @param idPensionAlimentaire
     *            L'identifiant de l'entit� PensionAlimentaire � charger en m�moire
     * @return L'entit� PensionAlimentaire charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PensionAlimentaire read(String idPensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException;

    /**
     * Chargement d'une PensionAlimentaireSearch via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws PensionAlimentaireSearch
     *             Exception
     * @throws JadePersistenceException
     */
    public PensionAlimentaire readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws PensionAlimentaireException, JadePersistenceException;

    /**
     * Permet de chercher des PensionAlimentaire selon un mod�le de crit�res.
     * 
     * @param PensionAlimentaireSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PensionAlimentaireSearch search(PensionAlimentaireSearch pensionAlimentaireSearch)
            throws JadePersistenceException, PensionAlimentaireException;

    /**
     * 
     * Permet la mise � jour d'une entit� PensionAlimentaire
     * 
     * @param PensionAlimentaire
     *            L'entit� PensionAlimentaire � mettre � jour
     * @return L'entit� PensionAlimentaire mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public PensionAlimentaire update(PensionAlimentaire pensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException, DonneeFinanciereException;
}