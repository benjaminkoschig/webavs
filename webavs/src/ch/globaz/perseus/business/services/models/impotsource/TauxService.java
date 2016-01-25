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
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(TauxSearchModel search) throws TauxException, JadePersistenceException;

    /**
     * Permet la création d'une entité demande
     * 
     * @param taux
     *            Le taux à créer
     * @return Le taux créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Taux create(Taux taux) throws JadePersistenceException, TauxException;

    /**
     * Permet la suppression d'une entité taux PC Famille
     * 
     * @param taux
     *            Le taux à supprimer
     * @return Le taux supprimé
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Taux delete(Taux taux) throws JadePersistenceException, TauxException;

    /**
     * Permet de retrouver le taux d'imposition à la source d'une personne sur la base de son salaire brut et du nombre
     * de personnes dans le foyer
     * 
     * @param salaireBrut
     * @param nbPersonne
     * @param annee
     * @param CsTypeBareme
     * @return taux d'imposition, null si aucun taux trouvé
     * @throws JadePersistenceException
     * @throws TauxException
     * @throws Exception
     */
    public Taux getTauxImpotSource(BigDecimal salaireBrut, int nbPersonne, String annee, String csTypeBareme)
            throws JadePersistenceException, TauxException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire un taux PC Familles
     * 
     * @param idTaux
     *            L'identifiant du taux à charger en mémoire
     * @return La Taux chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Taux read(String idTaux) throws JadePersistenceException, TauxException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de chercher des taux selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public TauxSearchModel search(TauxSearchModel searchModel) throws JadePersistenceException, TauxException;

    /**
     * 
     * Permet la mise à jour d'une entité Taux
     * 
     * @param taux
     *            Le Taux PC Familles à mettre à jour
     * @return Le taux PC Familles mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TauxException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Taux update(Taux taux) throws JadePersistenceException, TauxException;

}
