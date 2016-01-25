/*
 * Crée le 31 octobre 2006
 */
package globaz.lyra.db.historique;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author hpe
 * 
 *         BEntity de l'historique
 */

public class LYHistorique extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String FIELDNAME_DATEEXECUTION = "WODEXE";

    public static final String FIELDNAME_ETAT = "WOTETA";
    public static final String FIELDNAME_IDECHEANCE = "WOIECH";
    public static final String FIELDNAME_IDHISTORIQUE = "WOIHIS";
    public static final String FIELDNAME_IDLOG = "WOILOG";
    public static final String FIELDNAME_VISA = "WOLVIS";
    private static final long serialVersionUID = -3596831635218157070L;

    public static final String TABLE_NAME = "LYHISTOR";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateExecution = "";
    private String etat = "";
    private String idEcheance = "";
    private String idHistorique = "";
    private String idLog = "";
    private String visa = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LYHistorique.
     */
    public LYHistorique() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdHistorique(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des historique
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des historique
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idHistorique = statement.dbReadNumeric(FIELDNAME_IDHISTORIQUE);
        idEcheance = statement.dbReadNumeric(FIELDNAME_IDECHEANCE);
        dateExecution = statement.dbReadDateAMJ(FIELDNAME_DATEEXECUTION);
        visa = statement.dbReadString(FIELDNAME_VISA);
        etat = statement.dbReadNumeric(FIELDNAME_ETAT);
        idLog = statement.dbReadNumeric(FIELDNAME_IDLOG);

    }

    /**
     * Méthode de validation des échéances
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * Définition de la clé primaire de la table des historique
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(FIELDNAME_IDHISTORIQUE,
                _dbWriteNumeric(statement.getTransaction(), idHistorique, "idHistorique"));

    }

    /**
     * Méthode d'écriture des champs dans la table des historique
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_IDHISTORIQUE,
                _dbWriteNumeric(statement.getTransaction(), idHistorique, "idHistorique"));
        statement.writeField(FIELDNAME_IDECHEANCE,
                _dbWriteNumeric(statement.getTransaction(), idEcheance, "idEcheance"));
        statement.writeField(FIELDNAME_DATEEXECUTION,
                _dbWriteDateAMJ(statement.getTransaction(), dateExecution, "dateExecution"));
        statement.writeField(FIELDNAME_VISA, _dbWriteString(statement.getTransaction(), visa, "visa"));
        statement.writeField(FIELDNAME_ETAT, _dbWriteNumeric(statement.getTransaction(), etat, "etat"));
        statement.writeField(FIELDNAME_IDLOG, _dbWriteNumeric(statement.getTransaction(), idLog, "idLog"));

    }

    /**
     * getter pour l'attribut dateExecution
     * 
     * @return la valeur courante de l'attribut dateExecution
     */
    public String getDateExecution() {
        return dateExecution;
    }

    /**
     * getter pour l'attribut etat
     * 
     * @return la valeur courante de l'attribut etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * getter pour l'attribut idEcheance
     * 
     * @return la valeur courante de l'attribut idEcheance
     */
    public String getIdEcheance() {
        return idEcheance;
    }

    /**
     * getter pour l'attribut idHistorique
     * 
     * @return la valeur courante de l'attribut idHistorique
     */
    public String getIdHistorique() {
        return idHistorique;
    }

    /**
     * getter pour l'attribut idLog
     * 
     * @return la valeur courante de l'attribut idLog
     */
    public String getIdLog() {
        return idLog;
    }

    /**
     * getter pour l'attribut visa
     * 
     * @return la valeur courante de l'attribut visa
     */
    public String getVisa() {
        return visa;
    }

    /**
     * setter pour l'attribut dateExecution
     * 
     * @param dateExecution
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateExecution(String dateExecution) {
        this.dateExecution = dateExecution;
    }

    /**
     * setter pour l'attribut etat
     * 
     * @param etat
     *            une nouvelle valeur pour cet attribut
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * setter pour l'attribut idEcheance
     * 
     * @param idEcheance
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEcheance(String idEcheance) {
        this.idEcheance = idEcheance;
    }

    /**
     * setter pour l'attribut idHistorique
     * 
     * @param idHistorique
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdHistorique(String idHistorique) {
        this.idHistorique = idHistorique;
    }

    /**
     * setter pour l'attribut idLog
     * 
     * @param idLog
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    /**
     * setter pour l'attribut visa
     * 
     * @param visa
     *            une nouvelle valeur pour cet attribut
     */
    public void setVisa(String visa) {
        this.visa = visa;
    }

}
