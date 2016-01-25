package ch.globaz.pegasus.business.services.models.annonce.annoncelaprams;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.annonce.SequenceException;
import ch.globaz.pegasus.business.models.annonce.SimpleSequence;
import ch.globaz.pegasus.business.models.annonce.SimpleSequenceSearch;

public interface SimpleSequenceService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws SequenceException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleSequenceSearch search) throws SequenceException, JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleSequence
     * 
     * @param SimpleSequence
     *            La simpleSequence à créer
     * @return simpleSequence créé
     * @throws SequenceException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleSequence create(SimpleSequence simpleSequence) throws SequenceException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleSequence
     * 
     * @param SimpleSequence
     *            La simpleSequence à supprimer
     * @return supprimé
     * @throws SequenceException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleSequence delete(SimpleSequence simpleSequence) throws SequenceException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleSequence selon un modèle de critères.
     * 
     * @param simpleSequenceSearch
     *            Le modèle de critères
     * @return Une liste typé
     * @throws SequenceException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public List<SimpleSequence> find(SimpleSequenceSearch simpleSequenceSearch) throws SequenceException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire une simpleSequence PC
     * 
     * @param idsimpleSequence
     *            L'identifiant de simpleSequence à charger en mémoire
     * @return simpleSequence chargée en mémoire
     * @throws SequenceException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleSequence read(String idSimpleSequence) throws SequenceException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleSequence selon un modèle de critères.
     * 
     * @param simpleSequenceSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws SequenceException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleSequenceSearch search(SimpleSequenceSearch simpleSequenceSearch) throws SequenceException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleSequence
     * 
     * @param SimpleSequence
     *            Le modele à mettre à jour
     * @return simpleSequence mis à jour
     * @throws SequenceException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleSequence update(SimpleSequence simpleSequence) throws SequenceException, JadePersistenceException;
}
