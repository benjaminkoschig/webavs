package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependanteSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface RevenuActiviteLucrativeDependanteService extends JadeApplicationService,
        AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RevenuActiviteLucrativeDependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(RevenuActiviteLucrativeDependanteSearch search) throws RevenuActiviteLucrativeDependanteException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� RevenuActiviteLucrativeDependante
     * 
     * @param RevenuActiviteLucrativeDependante
     *            L'entit� RevenuActiviteLucrativeDependante � cr�er
     * @return L'entit� RevenuActiviteLucrativeDependante cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuActiviteLucrativeDependante create(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� RevenuActiviteLucrativeDependante
     * 
     * @param RevenuActiviteLucrativeDependante
     *            L'entit� RevenuActiviteLucrativeDependante � supprimer
     * @return L'entit� RevenuActiviteLucrativeDependante supprim�
     * @throws RevenuActiviteLucrativeDependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public RevenuActiviteLucrativeDependante delete(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            RevenuActiviteLucrativeDependanteException;

    /**
     * Permet de charger en m�moire d'une entit� RevenuActiviteLucrativeDependante
     * 
     * @param idRevenuActiviteLucrativeDependante
     *            L'identifiant de l'entit� RevenuActiviteLucrativeDependante � charger en m�moire
     * @return L'entit� RevenuActiviteLucrativeDependante charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuActiviteLucrativeDependante read(String idRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException;

    /**
     * Chargement d'une RevenuActiviteLucrativeDependante via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RevenuActiviteLucrativeDependanteException
     * @throws JadePersistenceException
     */
    public RevenuActiviteLucrativeDependante readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException;

    /**
     * Permet de chercher des RevenuActiviteLucrativeDependante selon un mod�le de crit�res.
     * 
     * @param RevenuActiviteLucrativeDependanteSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuActiviteLucrativeDependanteSearch search(
            RevenuActiviteLucrativeDependanteSearch revenuActiviteLucrativeDependanteSearch)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException;

    /**
     * 
     * Permet la mise � jour d'une entit� RevenuActiviteLucrativeDependante
     * 
     * @param RevenuActiviteLucrativeDependante
     *            L'entit� RevenuActiviteLucrativeDependante � mettre � jour
     * @return L'entit� RevenuActiviteLucrativeDependante mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public RevenuActiviteLucrativeDependante update(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException, DonneeFinanciereException;
}