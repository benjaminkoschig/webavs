package ch.globaz.pegasus.business.services.models.variablemetier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.pegasus.business.models.variablemetier.SimpleVariableMetier;
import ch.globaz.pegasus.business.models.variablemetier.SimpleVariableMetierSearch;

public interface SimpleVariableMetierService extends JadeCrudService<SimpleVariableMetier, SimpleVariableMetierSearch> {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws VariableMetierException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    @Override
    public int count(SimpleVariableMetierSearch search) throws VariableMetierException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� variableMetier
     * 
     * @param variableMetier La variable m�tier � cr�er
     * @return La variable m�tier cr��
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws VariableMetierException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    @Override
    public SimpleVariableMetier create(SimpleVariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� variable m�tier
     * 
     * @param variablemetier La variable m�tier � supprimer
     * @return La variable m�tier supprim�
     * @throws VariableMetierException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     */
    @Override
    public SimpleVariableMetier delete(SimpleVariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire une variableMetier PC
     * 
     * @param idVariableMetier L'identifiant de la variableMetier � charger en m�moire
     * @return La variableMetier charg�e en m�moire
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws VariableMetierException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    @Override
    public SimpleVariableMetier read(String idVariableMetier) throws VariableMetierException, JadePersistenceException;

    /**
     * Permet de chercher des variableMetier selon un mod�le de crit�res.
     * 
     * @param variableMetierSearch Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrixChambreException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    @Override
    public SimpleVariableMetierSearch search(SimpleVariableMetierSearch simpleVariableMetierSearch)
            throws VariableMetierException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� variableMetier
     * 
     * @param variableMetier La variableMetier � mettre � jour
     * @return La variableMetier mis � jour
     * @throws JadePersistenceException Lev�e en cas de probl�me dans la couche de persistence
     * @throws VariableMetierException Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    @Override
    public SimpleVariableMetier update(SimpleVariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException;

}
