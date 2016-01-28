package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.models.parametre.ConversionRente;
import ch.globaz.pegasus.business.models.parametre.ConversionRenteSearch;

public interface ConversionRenteService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws ConversionRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(ConversionRenteSearch search) throws ConversionRenteException, JadePersistenceException;

    /**
     * Permet la création d'une entité variableMetier
     * 
     * @param ConversionRente
     *            La conversionRente métier à créer
     * @return conversionRente créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ConversionRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ConversionRente create(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité variable métier
     * 
     * @param ConversionRente
     *            La conversionRente métier à supprimer
     * @return supprimé
     * @throws ConversionRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ConversionRente delete(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire une conversionRente PC
     * 
     * @param idconversionRente
     *            L'identifiant de la variableMetier à charger en mémoire
     * @return conversionRente chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ConversionRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ConversionRente read(String idConversionRente) throws ConversionRenteException, JadePersistenceException;

    /**
     * Permet de chercher des variableMetier selon un modèle de critères.
     * 
     * @param variableMetierSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ConversionRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ConversionRenteSearch search(ConversionRenteSearch conversionRenteSearch) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité variableMetier
     * 
     * @param ConversionRente
     *            La variableMetier à mettre à jour
     * @return conversionRente mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ConversionRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ConversionRente update(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException;

}
