package ch.globaz.hera.business.services.models.famille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.hera.business.exceptions.models.RelationConjointException;
import ch.globaz.hera.business.models.famille.DateNaissanceConjoint;
import ch.globaz.hera.business.models.famille.RelationConjoint;
import ch.globaz.hera.business.models.famille.RelationConjointSearch;

public interface RelationConjointService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws RelationConjointException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(RelationConjointSearch search) throws RelationConjointException, JadePersistenceException;

    public String getDateNaissanceConjointForDate(String nSSRequerant, String dateDebut)
            throws RelationConjointException, JadePersistenceException, Exception;

    /**
     * Charge une instance de <code>RelationConjoint</code> pour un idMembreFamille durant une date donn�e
     * 
     * @param idMembreFamille
     * @param dateDebut
     * @return
     * @throws JadePersistenceException
     * @throws RelationConjointException si plus d'une relation trouv� ou pas de relation
     */
    public RelationConjoint readRelationForIdMembreFamilleByDate(String idConjoint, String dateDebut)
            throws JadePersistenceException, RelationConjointException;

    public DateNaissanceConjoint readByNss(String nss) throws JadePersistenceException, RelationConjointException;

    /**
     * Permet de charger en m�moire une relation entre conjoints
     * 
     * @param idRelationconjoint
     *            L'identifiant de la relation entre conjoints � charger en m�moire
     * @return La relation entre conjoints charg�e en m�moire
     * @throws RelationConjointException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public RelationConjoint read(String idRelationconjoint) throws JadePersistenceException, RelationConjointException;

    /**
     * Permet de chercher des relations entre conjoints selon un mod�le de crit�res.
     * 
     * @param search
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RelationConjointException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RelationConjointSearch search(RelationConjointSearch search) throws JadePersistenceException,
            RelationConjointException;

}
