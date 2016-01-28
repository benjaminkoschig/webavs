package ch.globaz.perseus.business.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.Taux;
import ch.globaz.perseus.business.models.impotsource.TauxSearchModel;

public interface TauxService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(TauxSearchModel search) throws TauxException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� demande
     * 
     * @param taux
     *            Le taux � cr�er
     * @return Le taux cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Taux create(Taux taux) throws JadePersistenceException, TauxException;

    /**
     * Permet la suppression d'une entit� taux PC Famille
     * 
     * @param taux
     *            Le taux � supprimer
     * @return Le taux supprim�
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public Taux delete(Taux taux) throws JadePersistenceException, TauxException;

    /**
     * Permet de retrouver le taux d'imposition � la source d'une personne sur la base de son salaire brut et du nombre
     * de personnes dans le foyer
     * 
     * @param salaireBrut
     * @param nbPersonne
     * @param annee
     * @param CsTypeBareme
     * @return taux d'imposition, null si aucun taux trouv�
     * @throws JadePersistenceException
     * @throws TauxException
     * @throws Exception
     */
    public Taux getTauxImpotSource(BigDecimal salaireBrut, int nbPersonne, String annee, String csTypeBareme)
            throws JadePersistenceException, TauxException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire un taux PC Familles
     * 
     * @param idTaux
     *            L'identifiant du taux � charger en m�moire
     * @return La Taux charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Taux read(String idTaux) throws JadePersistenceException, TauxException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de chercher des taux selon un mod�le de crit�res.
     * 
     * @param searchModel
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public TauxSearchModel search(TauxSearchModel searchModel) throws JadePersistenceException, TauxException;

    /**
     * 
     * Permet la mise � jour d'une entit� Taux
     * 
     * @param taux
     *            Le Taux PC Familles � mettre � jour
     * @return Le taux PC Familles mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Taux update(Taux taux) throws JadePersistenceException, TauxException;

}
