package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.ForfaitsPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.models.parametre.ForfaitsPrimesAssuranceMaladieSearch;

public interface ForfaitsPrimesAssuranceMaladieService extends JadeApplicationService {
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
    public int count(ForfaitsPrimesAssuranceMaladieSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� ForfaitsPrimesAssuranceMaladie
     * 
     * @param ForfaitsPrimesAssuranceMaladie
     *            Lr forfaitsPrimesAssuranceMaladie � cr�er
     * @return forfaitsPrimesAssuranceMaladie cr��
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ForfaitsPrimesAssuranceMaladie create(ForfaitsPrimesAssuranceMaladie forfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� forfaitsPrimesAssuranceMaladie
     * 
     * @param ForfaitsPrimesAssuranceMaladie
     *            La forfaitsPrimesAssuranceMaladie m�tier � supprimer
     * @return supprim�
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ForfaitsPrimesAssuranceMaladie delete(ForfaitsPrimesAssuranceMaladie forfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire une forfaitsPrimesAssuranceMaladie PC
     * 
     * @param idforfaitsPrimesAssuranceMaladie
     *            L'identifiant de forfaitsPrimesAssuranceMaladie � charger en m�moire
     * @return forfaitsPrimesAssuranceMaladie charg�e en m�moire
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ForfaitsPrimesAssuranceMaladie read(String idForfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * Permet de chercher des ForfaitsPrimesAssuranceMaladie selon un mod�le de crit�res.
     * 
     * @param forfaitsPrimesAssuranceMaladieSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ForfaitsPrimesAssuranceMaladieSearch search(
            ForfaitsPrimesAssuranceMaladieSearch forfaitsPrimesAssuranceMaladieSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� ForfaitsPrimesAssuranceMaladie
     * 
     * @param ForfaitsPrimesAssuranceMaladie
     *            Le modele � mettre � jour
     * @return forfaitsPrimesAssuranceMaladie mis � jour
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ForfaitsPrimesAssuranceMaladie update(ForfaitsPrimesAssuranceMaladie forfaitsPrimesAssuranceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}