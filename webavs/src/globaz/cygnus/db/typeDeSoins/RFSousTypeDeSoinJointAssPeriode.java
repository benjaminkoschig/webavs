/*
 * Cr�� le 11 janvier 2009
 */
package globaz.cygnus.db.typeDeSoins;

import globaz.globall.db.BStatement;

/**
 * @author jje
 * 
 *         Edit� par fha le 13/04/2010 : ajout du codeSousTypeSoin pour la gestion des conventions (ajout couple
 *         fournisseur-type de soin)
 */
public class RFSousTypeDeSoinJointAssPeriode extends RFTypeDeSoin {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);

        // jointure entre la table des sous-types de soin et la table des types
        // de soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        // jointure entre la table des sous-types de soin et la table de liaison
        // sts/pot
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssTypeDeSoinPotAssure.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssTypeDeSoinPotAssure.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssTypeDeSoinPotAssure.FIELDNAME_ID_SOUS_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);

        return fromClauseBuffer.toString();
    }

    private String codeSousTypeSoin = "";
    private String codeTypeSoin = "";

    private String dateDebut = "";
    private String dateFin = "";

    private String idPotAssure = "";
    private String idSoinPot = "";

    private String idSousTypeSoin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFSousTypeDeSoinJointAssPeriode() {
        super();
    }

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return TABLE_NAME;
    }

    /**
     * Lecture des propri�t�s dans les champs de la table des sous types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idSousTypeSoin = statement.dbReadNumeric(RFAssTypeDeSoinPotAssure.FIELDNAME_ID_SOUS_TYPE_SOIN);
        idPotAssure = statement.dbReadNumeric(RFAssTypeDeSoinPotAssure.FIELDNAME_ID_POT_ASSURE);
        idSoinPot = statement.dbReadNumeric(RFAssTypeDeSoinPotAssure.FIELDNAME_ID_SOIN_POT);
        codeTypeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_CODE);
        codeSousTypeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getCodeSousTypeSoin() {
        return codeSousTypeSoin;
    }

    public String getCodeTypeSoin() {
        return codeTypeSoin;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdPotAssure() {
        return idPotAssure;
    }

    public String getIdSoinPot() {
        return idSoinPot;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setCodeSousTypeSoin(String codeSousTypeSoin) {
        this.codeSousTypeSoin = codeSousTypeSoin;
    }

    public void setCodeTypeSoin(String codeTypeSoin) {
        this.codeTypeSoin = codeTypeSoin;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdPotAssure(String idPotAssure) {
        this.idPotAssure = idPotAssure;
    }

    public void setIdSoinPot(String idSoinPot) {
        this.idSoinPot = idSoinPot;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

}
