package ch.globaz.corvus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.exceptions.models.SimpleRetenuePayementException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayementSearch;

public interface SimpleRetenuePayementService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws SimpleRetenuePayementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleRetenuePayementSearch search) throws SimpleRetenuePayementException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleRetenuePayement.
     * 
     * @param SimpleRetenuePayement
     *            Le SimpleRetenuePayement à créer
     * @return Le SimpleRetenuePayement créé
     * @throws SimpleRetenuePayementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleRetenuePayement create(SimpleRetenuePayement simpleRetenuePayement)
            throws SimpleRetenuePayementException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité SimpleRetenuePayement
     * 
     * @param SimpleRetenuePayement
     *            Le SimpleRetenuePayement à supprimer
     * @return Le SimpleRetenuePayement supprimé
     * @throws SimpleRetenuePayementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleRetenuePayement delete(SimpleRetenuePayement simpleRetenuePayement)
            throws SimpleRetenuePayementException, JadePersistenceException;

    /**
     * Permet la suppression d'entité SimpleRetenuePayement par idPrestationAccordee
     * 
     * @param SimpleRetenuePayement
     *            Le SimpleRetenuePayement à supprimer
     * @return Le SimpleRetenuePayement supprimé
     * @throws SimpleRetenuePayementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public void deleteByIdPrestationAccordee(String idPrestationAccordee) throws SimpleRetenuePayementException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un SimpleRetenuePayement
     * 
     * @param idSimpleRetenuePayement
     *            L'identifiant du InformationsComptabilite à charger en mémoire
     * @return Le SimpleRetenuePayement chargé en mémoire
     * @throws SimpleRetenuePayement
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleRetenuePayement read(String idSimpleRetenuePayement) throws SimpleRetenuePayementException,
            JadePersistenceException;

    public SimpleRetenuePayementSearch search(SimpleRetenuePayementSearch search) throws JadePersistenceException,
            SimpleRetenuePayementException;

    /**
     * Permet la mise à jour d'une entité InformationsComptabilite
     * 
     * @param InformationsComptabilite
     *            Le InformationsComptabilite à mettre à jour
     * @return Le InformationsComptabilite mis à jour
     * @throws InformationsComptabiliteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRetenuePayement update(SimpleRetenuePayement simpleRetenuePayement)
            throws SimpleRetenuePayementException;

}
