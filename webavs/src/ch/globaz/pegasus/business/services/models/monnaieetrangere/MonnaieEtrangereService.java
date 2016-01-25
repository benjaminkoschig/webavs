package ch.globaz.pegasus.business.services.models.monnaieetrangere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangereSearch;

public interface MonnaieEtrangereService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws MonnaieEtrangereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(MonnaieEtrangereSearch search) throws MonnaieEtrangereException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� monnaiesEtrangeres
     * 
     * @param monnaieEtrangere
     *            La monnaies � creer
     * @return La monnaies cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MonnaieEtrangereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public MonnaieEtrangere create(MonnaieEtrangere monnaieEtrangere) throws JadePersistenceException,
            MonnaieEtrangereException;

    /**
     * Permet la suppression d'une entit� monnaiesEtrangeres
     * 
     * @param monnaiesEtrangeres
     *            La demande PC � supprimer
     * @return La demande PC supprim�
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public MonnaieEtrangere delete(MonnaieEtrangere monnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire une monnaiesEtrangeres
     * 
     * @param idMonnaieEtrangere
     *            L'identifiant de la monnaies � charger en m�moire
     * @return La monnaies charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MonnaieEtrangereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public MonnaieEtrangere read(String idMonnaieEtrangere) throws JadePersistenceException, MonnaieEtrangereException;

    /**
     * Permet de chercher des monnaies selon un mod�le de crit�res.
     * 
     * @param demandeSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MonnaiesEtrangeresException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public MonnaieEtrangereSearch search(MonnaieEtrangereSearch monnaieEtrangereSearch)
            throws JadePersistenceException, MonnaieEtrangereException;

    /**
     * 
     * Permet la mise � jour d'une entit� demande
     * 
     * @param monnaieEtrangeres
     *            La monnaie � mettre � jour
     * @return La monnaie mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MonnaieEtrangereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * 
     */
    public MonnaieEtrangere update(MonnaieEtrangere monnaieEtrangere) throws JadePersistenceException,
            MonnaieEtrangereException;

}
