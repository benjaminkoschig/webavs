package ch.globaz.pegasus.business.services.models.monnaieetrangere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.monnaieetrangere.SimpleMonnaieEtrangere;
import ch.globaz.pegasus.business.models.monnaieetrangere.SimpleMonnaieEtrangereSearch;

public interface SimpleMonnaieEtrangereService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws MonnaieEtrangereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleMonnaieEtrangereSearch search) throws MonnaieEtrangereException, JadePersistenceException;

    /**
     * Permet la création d'une entité simplePlanDeCalcul
     * 
     * @param simpleMonnaieEtrangere
     *            La monnaie etrangere (modele simple)
     * @return La monnaie etrangere (modele simple) créé
     * @throws SimpleMonnaieetrangereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleMonnaieEtrangere create(SimpleMonnaieEtrangere simpleMonnaieEtrangere)
            throws MonnaieEtrangereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simplePlanDeCalcul
     * 
     * @param simplePlanDeCalcul
     *            La monnaie etrangere (modele simple) à supprimer
     * @return La monnaie etrangere (modele simple) supprimé
     * @throws MonnaieEtrangereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleMonnaieEtrangere delete(SimpleMonnaieEtrangere simpleMonnaieEtrangere)
            throws MonnaieEtrangereException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un simplePlanDeCalcul
     * 
     * @param idSimpleMonnaieEtrangere
     *            L'identifiant du simpleMonnaieEtrangere à charger en mémoire
     * @return La monnaie etrangere (modele simple) chargé en mémoire
     * @throws MonnaieEtrangereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleMonnaieEtrangere read(String idSimpleMonnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException;

    /**
     * Permet de chercher des simplePlanDeCalcul selon un modèle de critères.
     * 
     * @param simplePlanDeCalculSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MonnaieEtrangereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleMonnaieEtrangereSearch search(SimpleMonnaieEtrangereSearch simpleMonnaieEtrangereSearch)
            throws JadePersistenceException, MonnaieEtrangereException;

    /**
     * Permet la mise à jour d'une entité simplePlanDeCalcul
     * 
     * @param simpleMonnaieEtrangere
     *            Le simpleMonnaieEtrangere à mettre à jour
     * @return Le simpleMonnaieEtrangere mis à jour
     * @throws MonnaieEtrangereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleMonnaieEtrangere update(SimpleMonnaieEtrangere simpleMonnaieEtrangere)
            throws MonnaieEtrangereException, JadePersistenceException;

}
