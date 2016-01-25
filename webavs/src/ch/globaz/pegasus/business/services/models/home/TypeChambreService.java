package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;

public interface TypeChambreService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(TypeChambreSearch search) throws TypeChambreException, JadePersistenceException;

    /**
     * Permet la création d'une entité typeChambre
     * 
     * @param typeChambre
     *            Le typeChambre à créer
     * @return Le typeChambre créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TypeChambre create(TypeChambre typeChambre) throws JadePersistenceException, TypeChambreException;

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
    public TypeChambre delete(TypeChambre typeChambre) throws TypeChambreException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un typeChambre
     * 
     * @param idTypeChambre
     *            L'identifiant du typeChambre à charger en mémoire
     * @return Le typeChambre chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TypeChambre read(String idTypeChambre) throws JadePersistenceException, TypeChambreException;

    /**
     * Permet de chercher des typeChambre selon un modèle de critères.
     * 
     * @param typeChambreSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TypeChambreSearch search(TypeChambreSearch typeChambreSearch) throws JadePersistenceException,
            TypeChambreException;

    /**
     * 
     * Permet la mise à jour d'une entité typeChambre
     * 
     * @param typeChambre
     *            Le typeChambre à mettre à jour
     * @return Le typeChambre mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TypeChambreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TypeChambre update(TypeChambre typeChambre) throws JadePersistenceException, TypeChambreException;
}
