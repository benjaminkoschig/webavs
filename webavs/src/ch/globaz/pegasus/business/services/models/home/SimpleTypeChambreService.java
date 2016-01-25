package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.SimpleTypeChambre;
import ch.globaz.pegasus.business.models.home.SimpleTypeChambreSearch;

public interface SimpleTypeChambreService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleTypeChambreSearch search) throws TypeChambreException, JadePersistenceException;

    /**
     * Permet la création d'une entité typeChambre
     * 
     * @param typeChambre
     *            Le typeChambre à créer
     * @return Le typeChambre créé
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTypeChambre create(SimpleTypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité typeChambre
     * 
     * @param typeChambre
     *            Le typeChambre à supprimer
     * @return Le typeChambre supprimé
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTypeChambre delete(SimpleTypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un typeChambre
     * 
     * @param idTypeChambre
     *            L'identifiant du typeChambre à charger en mémoire
     * @return Le typeChambre chargé en mémoire
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTypeChambre read(String idTypeChambre) throws TypeChambreException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité typeChambre
     * 
     * @param typeChambre
     *            Le typeChambre à mettre à jour
     * @return Le typeChambre mis à jour
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTypeChambre update(SimpleTypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException;
}
