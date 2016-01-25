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
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(CompteBancaireCCPSearch search) throws CompteBancaireCCPException, JadePersistenceException;

    /**
     * Permet la création d'une entité CompteBancaireCCP
     * 
     * @param CompteBancaireCCP
     *            L'entité CompteBancaireCCP à créer
     * @return L'entité CompteBancaireCCP créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CompteBancaireCCP create(CompteBancaireCCP compteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité CompteBancaireCCP
     * 
     * @param CompteBancaireCCP
     *            L'entité CompteBancaireCCP à supprimer
     * @return L'entité CompteBancaireCCP supprimé
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CompteBancaireCCP delete(CompteBancaireCCP compteBancaireCCP) throws CompteBancaireCCPException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité CompteBancaireCCP
     * 
     * @param idCompteBancaireCCP
     *            L'identifiant de l'entité CompteBancaireCCP à charger en mémoire
     * @return L'entité CompteBancaireCCP chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
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
     * Permet de chercher des CompteBancaireCCP selon un modèle de critères.
     * 
     * @param CompteBancaireCCPSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public CompteBancaireCCPSearch search(CompteBancaireCCPSearch compteBancaireCCPSearch)
            throws JadePersistenceException, CompteBancaireCCPException;

    /**
     * 
     * Permet la mise à jour d'une entité CompteBancaireCCP
     * 
     * @param CompteBancaireCCP
     *            L'entité CompteBancaireCCP à mettre à jour
     * @return L'entité CompteBancaireCCP mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public CompteBancaireCCP update(CompteBancaireCCP compteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException, DonneeFinanciereException;
}