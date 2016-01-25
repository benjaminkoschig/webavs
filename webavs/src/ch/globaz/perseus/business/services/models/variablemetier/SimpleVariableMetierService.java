package ch.globaz.perseus.business.services.models.variablemetier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.models.variablemetier.SimpleVariableMetier;
import ch.globaz.perseus.business.models.variablemetier.SimpleVariableMetierSearch;

public interface SimpleVariableMetierService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws VariableMetierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleVariableMetierSearch search) throws VariableMetierException, JadePersistenceException;

    /**
     * Permet la création d'une entité variableMetier
     * 
     * @param variableMetier
     *            La variable métier à créer
     * @return La variable métier créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws VariableMetierException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleVariableMetier create(SimpleVariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité variable métier
     * 
     * @param variablemetier
     *            La variable métier à supprimer
     * @return La variable métier supprimé
     * @throws VariableMetierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleVariableMetier delete(SimpleVariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire une variableMetier PC
     * 
     * @param idVariableMetier
     *            L'identifiant de la variableMetier à charger en mémoire
     * @return La variableMetier chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws VariableMetierException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleVariableMetier read(String idVariableMetier) throws VariableMetierException, JadePersistenceException;

    /**
     * Permet de chercher des variableMetier selon un modèle de critères.
     * 
     * @param variableMetierSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleVariableMetierSearch search(SimpleVariableMetierSearch simpleVariableMetierSearch)
            throws VariableMetierException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité variableMetier
     * 
     * @param variableMetier
     *            La variableMetier à mettre à jour
     * @return La variableMetier mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws VariableMetierException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleVariableMetier update(SimpleVariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException;

}
