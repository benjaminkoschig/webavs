package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.pegasus.business.models.parametre.SimpleConversionRente;
import ch.globaz.pegasus.business.models.parametre.SimpleConversionRenteSearch;

public interface SimpleConversionRenteService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleConversionRenteSearch search) throws ConversionRenteException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit�
     * 
     * @param SimpleConversionRente
     *            La variable m�tier � cr�er
     * @return cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleConversionRente create(SimpleConversionRente simpleConversionRente) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit�
     * 
     * @param SimpleConversionRente
     *            La variable m�tier � supprimer
     * @return supprim�
     * @throws VariableMetierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleConversionRente delete(SimpleConversionRente simpleConversionRente) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire
     * 
     * @param id
     *            L'identifiant de la variableMetier � charger en m�moire
     * @return charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */

    public SimpleConversionRente read(String id) throws ConversionRenteException, JadePersistenceException;

    /**
     * Permet de chercher des simpleVariableMetierSearch selon un mod�le de crit�res.
     * 
     * @param simpleVariableMetierSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleConversionRenteSearch search(SimpleConversionRenteSearch search) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une
     * 
     * @param SimpleConversionRente
     *            La variableMetier � mettre � jour
     * @return mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleConversionRente update(SimpleConversionRente simpleConversionRente) throws ConversionRenteException,
            JadePersistenceException;

}
