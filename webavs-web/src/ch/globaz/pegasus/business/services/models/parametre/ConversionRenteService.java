package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.models.parametre.ConversionRente;
import ch.globaz.pegasus.business.models.parametre.ConversionRenteSearch;

public interface ConversionRenteService extends JadeApplicationService {
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
    public int count(ConversionRenteSearch search) throws ConversionRenteException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� variableMetier
     * 
     * @param ConversionRente
     *            La conversionRente m�tier � cr�er
     * @return conversionRente cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ConversionRente create(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� variable m�tier
     * 
     * @param ConversionRente
     *            La conversionRente m�tier � supprimer
     * @return supprim�
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ConversionRente delete(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire une conversionRente PC
     * 
     * @param idconversionRente
     *            L'identifiant de la variableMetier � charger en m�moire
     * @return conversionRente charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ConversionRente read(String idConversionRente) throws ConversionRenteException, JadePersistenceException;

    /**
     * Permet de chercher des variableMetier selon un mod�le de crit�res.
     * 
     * @param variableMetierSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ConversionRenteSearch search(ConversionRenteSearch conversionRenteSearch) throws ConversionRenteException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� variableMetier
     * 
     * @param ConversionRente
     *            La variableMetier � mettre � jour
     * @return conversionRente mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ConversionRenteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ConversionRente update(ConversionRente conversionRente) throws ConversionRenteException,
            JadePersistenceException;

}
