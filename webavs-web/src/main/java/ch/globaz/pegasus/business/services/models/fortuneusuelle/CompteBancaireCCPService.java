package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CompteBancaireCCPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCPSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;
import ch.globaz.pegasus.business.vo.donneeFinanciere.IbanCheckResultVO;

public interface CompteBancaireCCPService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet la validation des iban CH (si l'iban est valide, la description de la banque liee est retournee)
     * 
     * @param chIban
     *            l'iban devant etre valide
     * @return IbanCheckResultVO
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public IbanCheckResultVO checkChIban(String chIban) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws CompteBancaireCCPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(CompteBancaireCCPSearch search) throws CompteBancaireCCPException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� CompteBancaireCCP
     * 
     * @param CompteBancaireCCP
     *            L'entit� CompteBancaireCCP � cr�er
     * @return L'entit� CompteBancaireCCP cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public CompteBancaireCCP create(CompteBancaireCCP compteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� CompteBancaireCCP
     * 
     * @param CompteBancaireCCP
     *            L'entit� CompteBancaireCCP � supprimer
     * @return L'entit� CompteBancaireCCP supprim�
     * @throws CompteBancaireCCPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public CompteBancaireCCP delete(CompteBancaireCCP compteBancaireCCP) throws CompteBancaireCCPException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� CompteBancaireCCP
     * 
     * @param idCompteBancaireCCP
     *            L'identifiant de l'entit� CompteBancaireCCP � charger en m�moire
     * @return L'entit� CompteBancaireCCP charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public CompteBancaireCCP read(String idCompteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException;

    /**
     * Chargement d'un CompteBancaireCPP via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws CompteBancaireCPPException
     * @throws JadePersistenceException
     */
    public CompteBancaireCCP readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws CompteBancaireCCPException, JadePersistenceException;

    /**
     * Permet de chercher des CompteBancaireCCP selon un mod�le de crit�res.
     * 
     * @param CompteBancaireCCPSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public CompteBancaireCCPSearch search(CompteBancaireCCPSearch compteBancaireCCPSearch)
            throws JadePersistenceException, CompteBancaireCCPException;

    /**
     * 
     * Permet la mise � jour d'une entit� CompteBancaireCCP
     * 
     * @param CompteBancaireCCP
     *            L'entit� CompteBancaireCCP � mettre � jour
     * @return L'entit� CompteBancaireCCP mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public CompteBancaireCCP update(CompteBancaireCCP compteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException, DonneeFinanciereException;
}