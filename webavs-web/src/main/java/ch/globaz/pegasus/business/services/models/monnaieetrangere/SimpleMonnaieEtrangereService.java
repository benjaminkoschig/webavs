package ch.globaz.pegasus.business.services.models.monnaieetrangere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.monnaieetrangere.SimpleMonnaieEtrangere;
import ch.globaz.pegasus.business.models.monnaieetrangere.SimpleMonnaieEtrangereSearch;

public interface SimpleMonnaieEtrangereService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws MonnaieEtrangereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleMonnaieEtrangereSearch search) throws MonnaieEtrangereException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� simplePlanDeCalcul
     * 
     * @param simpleMonnaieEtrangere
     *            La monnaie etrangere (modele simple)
     * @return La monnaie etrangere (modele simple) cr��
     * @throws SimpleMonnaieetrangereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleMonnaieEtrangere create(SimpleMonnaieEtrangere simpleMonnaieEtrangere)
            throws MonnaieEtrangereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simplePlanDeCalcul
     * 
     * @param simplePlanDeCalcul
     *            La monnaie etrangere (modele simple) � supprimer
     * @return La monnaie etrangere (modele simple) supprim�
     * @throws MonnaieEtrangereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleMonnaieEtrangere delete(SimpleMonnaieEtrangere simpleMonnaieEtrangere)
            throws MonnaieEtrangereException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un simplePlanDeCalcul
     * 
     * @param idSimpleMonnaieEtrangere
     *            L'identifiant du simpleMonnaieEtrangere � charger en m�moire
     * @return La monnaie etrangere (modele simple) charg� en m�moire
     * @throws MonnaieEtrangereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleMonnaieEtrangere read(String idSimpleMonnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException;

    /**
     * Permet de chercher des simplePlanDeCalcul selon un mod�le de crit�res.
     * 
     * @param simplePlanDeCalculSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MonnaieEtrangereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleMonnaieEtrangereSearch search(SimpleMonnaieEtrangereSearch simpleMonnaieEtrangereSearch)
            throws JadePersistenceException, MonnaieEtrangereException;

    /**
     * Permet la mise � jour d'une entit� simplePlanDeCalcul
     * 
     * @param simpleMonnaieEtrangere
     *            Le simpleMonnaieEtrangere � mettre � jour
     * @return Le simpleMonnaieEtrangere mis � jour
     * @throws MonnaieEtrangereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleMonnaieEtrangere update(SimpleMonnaieEtrangere simpleMonnaieEtrangere)
            throws MonnaieEtrangereException, JadePersistenceException;

}
