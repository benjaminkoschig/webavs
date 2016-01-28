package globaz.cygnus.db.demandes;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author jje
 */
public class RFDemandeFtd15 extends RFDemande {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_ID_DEMANDE_FTD15 = "FAIDEM";

    public static final String TABLE_NAME = "RFDFT15";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure depuis les demande jusqu'au demande 15
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);

        // jointure entre la table des demandes RFM et la table des demandes 15
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeFtd15.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemandeFtd15.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemandeFtd15.FIELDNAME_ID_DEMANDE_FTD15);

        return fromClauseBuffer.toString();
    }

    private boolean hasCreationSpy = false;
    private boolean hasSpy = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String idDemandeFtd15 = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeDev19
     */
    public RFDemandeFtd15() {
        super();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // super._beforeAdd(transaction);
    }

    /**
     * getter pour le nom de la table des demandes de devis
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFDemandeFtd15.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des demandes 15
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idDemandeFtd15 = statement.dbReadNumeric(RFDemandeFtd15.FIELDNAME_ID_DEMANDE_FTD15);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des demandes 15
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFDemandeFtd15.FIELDNAME_ID_DEMANDE_FTD15,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeFtd15, "idDemandeFtd15"));
    }

    /**
     * Méthode d'écriture des champs dans la table des demandes 15
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // super._writeProperties(statement);
        statement.writeField(RFDemandeFtd15.FIELDNAME_ID_DEMANDE_FTD15,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeFtd15, "idDemandeFtd15"));
    }

    public String getIdDemandeFtd15() {
        return idDemandeFtd15;
    }

    @Override
    public boolean hasCreationSpy() {
        return hasCreationSpy;
    }

    @Override
    public boolean hasSpy() {
        return hasSpy;
    }

    public void setHasCreationSpy(boolean hasCreationSpy) {
        this.hasCreationSpy = hasCreationSpy;
    }

    public void setHasSpy(boolean hasSpy) {
        this.hasSpy = hasSpy;
    }

    public void setIdDemandeFtd15(String idDemandeFtd15) {
        this.idDemandeFtd15 = idDemandeFtd15;
    }

}
