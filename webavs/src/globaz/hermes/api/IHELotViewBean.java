package globaz.hermes.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface d'API désignant un lot d'annonces
 * 
 * @author ado
 */
public interface IHELotViewBean extends BIEntity {
    //
    public final static String LOT_PTY_BASSE = "113001";
    public final static String LOT_PTY_HAUTE = "113002";
    // Constante codes système
    public final static String TYPE_ENVOI = "116001";

    // Constante codes système
    public final static String TYPE_RECEPTION = "116002";

    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Supprime l'enregistrement de la BD
     * 
     * @exception java.lang.Exception
     *                si la suppression a échouée
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /**
     * Renvoit le type du lot (Envoi/réception)
     * 
     * @return String le type
     */
    public String getCsTypeLibelle();

    /**
     * Renvoit la date d'envoi
     * 
     * @return String la date
     */
    public String getDateEnvoi();

    /**
     * Renvoit l'heure d'envoi
     * 
     * @return String l'heure
     */
    public String getHeureEnvoi();

    /**
     * Retourne la PK du lot
     * 
     * @return String la PK
     */
    public String getIdLot();

    /**
     * Renvoit la valeur de la quittance o/n
     * 
     * @return String Oui ou Non
     */
    public String getQuittance();

    /**
     * Renvoit le type du lot (Envoi/réception)
     * 
     * @return String le type
     */
    public String getType();

    /**
     * Renvoit le nom de l'utilisateur
     * 
     * @return String l'utilisateur
     */
    public String getUtilisateur();

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Fixe la date d'envoi
     * 
     * @param String
     *            la nouvelle date
     */
    public void setDateEnvoi(String newDateEnvoi);

    /**
     * Fixe l'heure d'envoi
     * 
     * @param String
     *            la nouvelle heure
     */
    public void setHeureEnvoi(String newHeureEnvoi);

    /**
     * Fixe la pk du lot
     * 
     * @param String
     *            la nouvelle pk
     */
    public void setIdLot(String newIdLot);

    /**
     * Fixe la quittance
     * 
     * @param String
     *            la nouvelle quittance
     */
    public void setQuittance(String newQuittance);

    /**
     * Fixe le type (Envoi/Réception)
     * 
     * @param String
     *            le type
     */
    public void setType(String newType);

    /**
     * Fixe l'utilisateur
     * 
     * @param String
     *            l'utilisateur
     */
    public void setUtilisateur(String newUtilisateur);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
