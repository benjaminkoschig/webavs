package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetique;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetiqueSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface RevenuHypothetiqueService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RevenuHypothetiqueException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(RevenuHypothetiqueSearch search) throws RevenuHypothetiqueException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� RevenuHypothetique
     * 
     * @param RevenuHypothetique
     *            L'entit� RevenuHypothetique � cr�er
     * @return L'entit� RevenuHypothetique cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuHypothetique create(RevenuHypothetique revenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� RevenuHypothetique
     * 
     * @param RevenuHypothetique
     *            L'entit� RevenuHypothetique � supprimer
     * @return L'entit� RevenuHypothetique supprim�
     * @throws RevenuHypothetiqueException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public RevenuHypothetique delete(RevenuHypothetique revenuHypothetique) throws RevenuHypothetiqueException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� RevenuHypothetique
     * 
     * @param idRevenuHypothetique
     *            L'identifiant de l'entit� RevenuHypothetique � charger en m�moire
     * @return L'entit� RevenuHypothetique charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuHypothetique read(String idRevenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException;

    /**
     * Chargement d'une RevenuHypothetique via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RevenuHypothetiqueException
     * @throws JadePersistenceException
     */
    public RevenuHypothetique readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws RevenuHypothetiqueException, JadePersistenceException;

    /**
     * Permet de chercher des RevenuHypothetique selon un mod�le de crit�res.
     * 
     * @param RevenuHypothetiqueSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuHypothetiqueSearch search(RevenuHypothetiqueSearch revenuHypothetiqueSearch)
            throws JadePersistenceException, RevenuHypothetiqueException;

    /**
     * 
     * Permet la mise � jour d'une entit� RevenuHypothetique
     * 
     * @param RevenuHypothetique
     *            L'entit� RevenuHypothetique � mettre � jour
     * @return L'entit� RevenuHypothetique mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuHypothetiqueException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public RevenuHypothetique update(RevenuHypothetique revenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException, DonneeFinanciereException;
}