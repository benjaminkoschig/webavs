package ch.globaz.hera.business.services.models.famille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.hera.business.exceptions.models.RelationConjointException;
import ch.globaz.hera.business.models.famille.DateNaissanceConjoint;
import ch.globaz.hera.business.models.famille.RelationConjoint;
import ch.globaz.hera.business.models.famille.RelationConjointSearch;

public interface RelationConjointService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws RelationConjointException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(RelationConjointSearch search) throws RelationConjointException, JadePersistenceException;

    public String getDateNaissanceConjointForDate(String nSSRequerant, String dateDebut)
            throws RelationConjointException, JadePersistenceException, Exception;

    /**
     * Charge une instance de <code>RelationConjoint</code> pour un idMembreFamille durant une date donnée
     * 
     * @param idMembreFamille
     * @param dateDebut
     * @return
     * @throws JadePersistenceException
     * @throws RelationConjointException si plus d'une relation trouvé ou pas de relation
     */
    public RelationConjoint readRelationForIdMembreFamilleByDate(String idConjoint, String dateDebut)
            throws JadePersistenceException, RelationConjointException;

    public DateNaissanceConjoint readByNss(String nss) throws JadePersistenceException, RelationConjointException;

    /**
     * Permet de charger en mémoire une relation entre conjoints
     * 
     * @param idRelationconjoint
     *            L'identifiant de la relation entre conjoints à charger en mémoire
     * @return La relation entre conjoints chargée en mémoire
     * @throws RelationConjointException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RelationConjoint read(String idRelationconjoint) throws JadePersistenceException, RelationConjointException;

    /**
     * Permet de chercher des relations entre conjoints selon un modèle de critères.
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RelationConjointException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RelationConjointSearch search(RelationConjointSearch search) throws JadePersistenceException,
            RelationConjointException;

}
