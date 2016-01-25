package ch.globaz.corvus.business.services.models.rentesaccordees;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.exceptions.models.RentesAccordeesException;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabiliteSearch;

public interface SimpleInformationsComptabiliteService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws InformationsComptabiliteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleInformationsComptabiliteSearch search) throws RentesAccordeesException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité InformationsComptabilite. Le InformationsComptabilite doit avoir l'id d'une
     * demande de prestation associée qui existe et qui n'est pas déjà associée à un autre InformationsComptabilite,
     * sinon une exception est levée.
     * 
     * @param InformationsComptabilite
     *            Le InformationsComptabilite à créer
     * @return Le InformationsComptabilite créé
     * @throws InformationsComptabiliteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleInformationsComptabilite create(SimpleInformationsComptabilite simpleInformationsComptabilite)
            throws RentesAccordeesException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité InformationsComptabilite
     * 
     * @param InformationsComptabilite
     *            Le InformationsComptabilite à supprimer
     * @return Le InformationsComptabilite supprimé
     * @throws InformationsComptabiliteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleInformationsComptabilite delete(SimpleInformationsComptabilite simpleInformationsComptabilite)
            throws RentesAccordeesException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un InformationsComptabilite
     * 
     * @param idInformationsComptabilite
     *            L'identifiant du InformationsComptabilite à charger en mémoire
     * @return Le InformationsComptabilite chargé en mémoire
     * @throws InformationsComptabiliteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleInformationsComptabilite read(String idInformationsComptabilite) throws RentesAccordeesException,
            JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité InformationsComptabilite
     * 
     * @param InformationsComptabilite
     *            Le InformationsComptabilite à mettre à jour
     * @return Le InformationsComptabilite mis à jour
     * @throws InformationsComptabiliteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleInformationsComptabilite update(SimpleInformationsComptabilite simpleInformationsComptabilite)
            throws RentesAccordeesException, JadePersistenceException;

}
