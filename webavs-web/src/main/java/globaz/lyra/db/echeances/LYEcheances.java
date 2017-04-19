/*
 * Créé le 12 oct. 06
 */

package globaz.lyra.db.echeances;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author hpe
 * 
 *         BEntity des échéances
 */

public class LYEcheances extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String FIELDNAME_DESCRIPTION = "WMLDES";
    public static final String FIELDNAME_DESCRIPTION_FR = "WMLDESF";
    public static final String FIELDNAME_DESCRIPTION_DE = "WMLDESD";
    public static final String FIELDNAME_DESCRIPTION_IT = "WMLDESI";

    public static final String FIELDNAME_DOMAINE_APPLI = "WMTDAP";
    public static final String FIELDNAME_IDECHEANCES = "WMIECH";
    public static final String FIELDNAME_INTERFACE_ECH = "WMLCLI";
    private static final long serialVersionUID = -3596831635218157070L;

    public static final String TABLE_NAME = "LYECHEAN";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String descriptionEcheances = "";
    private String descriptionEcheances_fr = "";
    private String descriptionEcheances_de = "";
    private String descriptionEcheances_it = "";
    private String domaineApplicatif = "";
    private String idEcheances = "";
    private String processEcheance = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LYEcheances.
     */
    public LYEcheances() {
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
        setIdEcheances(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des échéances
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des échéances
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idEcheances = statement.dbReadNumeric(FIELDNAME_IDECHEANCES);
        domaineApplicatif = statement.dbReadNumeric(FIELDNAME_DOMAINE_APPLI);
        processEcheance = statement.dbReadString(FIELDNAME_INTERFACE_ECH);
        descriptionEcheances = statement.dbReadString(FIELDNAME_DESCRIPTION);
        descriptionEcheances_fr = statement.dbReadString(FIELDNAME_DESCRIPTION_FR);
        descriptionEcheances_de = statement.dbReadString(FIELDNAME_DESCRIPTION_DE);
        descriptionEcheances_it = statement.dbReadString(FIELDNAME_DESCRIPTION_IT);
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
     * Définition de la clé primaire de la table des échéances
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(FIELDNAME_IDECHEANCES,
                _dbWriteNumeric(statement.getTransaction(), idEcheances, "idEcheances"));

    }

    /**
     * Méthode d'écriture des champs dans la table des échéances
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_IDECHEANCES,
                _dbWriteNumeric(statement.getTransaction(), idEcheances, "idEcheances"));
        statement.writeField(FIELDNAME_DOMAINE_APPLI,
                _dbWriteNumeric(statement.getTransaction(), domaineApplicatif, "domaineApplicatif"));
        statement.writeField(FIELDNAME_INTERFACE_ECH,
                _dbWriteString(statement.getTransaction(), processEcheance, "processEcheance"));
        statement.writeField(FIELDNAME_DESCRIPTION,
                _dbWriteString(statement.getTransaction(), descriptionEcheances, "descriptionEcheances"));
        statement.writeField(FIELDNAME_DESCRIPTION_FR,
                _dbWriteString(statement.getTransaction(), descriptionEcheances_fr, "descriptionEcheances_fr"));
        statement.writeField(FIELDNAME_DESCRIPTION_DE,
                _dbWriteString(statement.getTransaction(), descriptionEcheances_de, "descriptionEcheances_de"));
        statement.writeField(FIELDNAME_DESCRIPTION_IT,
                _dbWriteString(statement.getTransaction(), descriptionEcheances_it, "descriptionEcheances_it"));

    }

    /**
     * getter pour l'attribut descriptionEcheances
     * 
     * @return la valeur courante de l'attribut descriptionEcheances
     */
    public String getDescriptionEcheances() {
        return descriptionEcheances;
    }

    /**
     * getter pour l'attribut domaineApplicatif
     * 
     * @return la valeur courante de l'attribut domaineApplicatif
     */
    public String getDomaineApplicatif() {
        return domaineApplicatif;
    }

    /**
     * getter pour l'attribut idEcheances
     * 
     * @return la valeur courante de l'attribut idEcheances
     */
    public String getIdEcheances() {
        return idEcheances;
    }

    /**
     * getter pour l'attribut process
     * 
     * @return la valeur courante de l'attribut process
     */
    public String getProcessEcheance() {
        return processEcheance;
    }

    /**
     * setter pour l'attribut descriptionEcheances
     * 
     * @param descriptionEcheances
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescriptionEcheances(String descriptionEcheances) {
        this.descriptionEcheances = descriptionEcheances;
    }

    /**
     * setter pour l'attribut domaineApplicatif
     * 
     * @param domaineApplicatif
     *            une nouvelle valeur pour cet attribut
     */
    public void setDomaineApplicatif(String domaineApplicatif) {
        this.domaineApplicatif = domaineApplicatif;
    }

    /**
     * setter pour l'attribut idEcheances
     * 
     * @param idEcheances
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEcheances(String idEcheances) {
        this.idEcheances = idEcheances;
    }

    /**
     * setter pour l'attribut processEcheance
     * 
     * @param processEcheance
     *            une nouvelle valeur pour cet attribut
     */
    public void setProcessEcheance(String processEcheance) {
        this.processEcheance = processEcheance;
    }

}
