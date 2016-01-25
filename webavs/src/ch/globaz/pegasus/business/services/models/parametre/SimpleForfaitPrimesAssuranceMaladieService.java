package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladieSearch;

public interface SimpleForfaitPrimesAssuranceMaladieService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleForfaitPrimesAssuranceMaladieSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� simpleForfaitAssMal
     * 
     * @param SimplePrimeFofaiteAssurenceMaladie
     *            La simpleForfaitAssMal m�tier � cr�er
     * @return simpleForfaitAssMal cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleForfaitPrimesAssuranceMaladie create(SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� simpleForfaitAssMal
     * 
     * @param SimplePrimeFofaiteAssurenceMaladie
     *            le simpleForfaitPrimesAssuranceMaladie � supprimer
     * @return supprim�
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleForfaitPrimesAssuranceMaladie delete(SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une simpleForfaitAssMal PC
     * 
     * @param idsimpleForfaitAssMal
     *            L'identifiant simpleForfaitPrimesAssuranceMaladie � charger en m�moire
     * @return SimpleForfaitPrimesAssuranceMaladie charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleForfaitPrimesAssuranceMaladie read(String idSimplePrimeFofaiteAssurenceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * Permet de chercher des simpleForfaitAssMal selon un mod�le de crit�res.
     * 
     * @param variableMetierSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleForfaitPrimesAssuranceMaladieSearch search(
            SimpleForfaitPrimesAssuranceMaladieSearch simpleForfaitAssMalSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� simpleForfaitAssMal
     * 
     * @param SimpleForfaitPrimesAssuranceMaladie
     *            SimpleForfaitPrimesAssuranceMaladie � mettre � jour
     * @return SimpleForfaitPrimesAssuranceMaladie mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleForfaitPrimesAssuranceMaladie update(SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}