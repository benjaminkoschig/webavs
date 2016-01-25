package ch.globaz.pegasus.business.services.models.monnaieetrangere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangere;
import ch.globaz.pegasus.business.models.monnaieetrangere.MonnaieEtrangereSearch;

public interface MonnaieEtrangereService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws MonnaieEtrangereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(MonnaieEtrangereSearch search) throws MonnaieEtrangereException, JadePersistenceException;

    /**
     * Permet la création d'une entité monnaiesEtrangeres
     * 
     * @param monnaieEtrangere
     *            La monnaies à creer
     * @return La monnaies créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MonnaieEtrangereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MonnaieEtrangere create(MonnaieEtrangere monnaieEtrangere) throws JadePersistenceException,
            MonnaieEtrangereException;

    /**
     * Permet la suppression d'une entité monnaiesEtrangeres
     * 
     * @param monnaiesEtrangeres
     *            La demande PC à supprimer
     * @return La demande PC supprimé
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public MonnaieEtrangere delete(MonnaieEtrangere monnaieEtrangere) throws MonnaieEtrangereException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire une monnaiesEtrangeres
     * 
     * @param idMonnaieEtrangere
     *            L'identifiant de la monnaies à charger en mémoire
     * @return La monnaies chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MonnaieEtrangereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MonnaieEtrangere read(String idMonnaieEtrangere) throws JadePersistenceException, MonnaieEtrangereException;

    /**
     * Permet de chercher des monnaies selon un modèle de critères.
     * 
     * @param demandeSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MonnaiesEtrangeresException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MonnaieEtrangereSearch search(MonnaieEtrangereSearch monnaieEtrangereSearch)
            throws JadePersistenceException, MonnaieEtrangereException;

    /**
     * 
     * Permet la mise à jour d'une entité demande
     * 
     * @param monnaieEtrangeres
     *            La monnaie à mettre à jour
     * @return La monnaie mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MonnaieEtrangereException
     *             Levée en cas de problème métier dans l'exécution du service
     * 
     */
    public MonnaieEtrangere update(MonnaieEtrangere monnaieEtrangere) throws JadePersistenceException,
            MonnaieEtrangereException;

}
