package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.pegasus.business.models.parametre.SimpleConversionRente;
import ch.globaz.pegasus.business.models.parametre.SimpleConversionRenteSearch;

public interface SimpleConversionRenteService extends JadeApplicationService {

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
    public int count(SimpleConversionRenteSearch search) throws ConversionRenteException, JadePersistenceException;

    /**
     * Permet la création d'une entité
     * 
     * @param SimpleConversionRente
     *            La variable métier à créer
     * @return créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ConversionRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleConversionRente create(SimpleConversionRente simpleConversionRente) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité
     * 
     * @param SimpleConversionRente
     *            La variable métier à supprimer
     * @return supprimé
     * @throws VariableMetierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws ConversionRenteException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleConversionRente delete(SimpleConversionRente simpleConversionRente) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire
     * 
     * @param id
     *            L'identifiant de la variableMetier à charger en mémoire
     * @return chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ConversionRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */

    public SimpleConversionRente read(String id) throws ConversionRenteException, JadePersistenceException;

    /**
     * Permet de chercher des simpleVariableMetierSearch selon un modèle de critères.
     * 
     * @param simpleVariableMetierSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ConversionRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleConversionRenteSearch search(SimpleConversionRenteSearch search) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une
     * 
     * @param SimpleConversionRente
     *            La variableMetier à mettre à jour
     * @return mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ConversionRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleConversionRente update(SimpleConversionRente simpleConversionRente) throws ConversionRenteException,
            JadePersistenceException;

}
