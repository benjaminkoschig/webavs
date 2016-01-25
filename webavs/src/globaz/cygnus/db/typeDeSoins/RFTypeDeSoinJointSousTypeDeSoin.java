/*
 * Créé le 11 janvier 2009
 */
package globaz.cygnus.db.typeDeSoins;

import globaz.globall.db.BStatement;

/**
 * @author jje
 * 
 *         Edité par fha le 13/04/2010 : ajout du codeSousTypeSoin pour la gestion des conventions (ajout couple
 *         fournisseur-type de soin)
 */
public class RFTypeDeSoinJointSousTypeDeSoin extends RFTypeDeSoin {

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

        return fromClauseBuffer.toString();
    }

    private String codeSousTypeSoin = "";

    private String idSousTypeSoin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFTypeDeSoinJointSousTypeDeSoin() {
        super();
    }

    /**
     * getter pour le nom de la table des sous types de soin
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFTypeDeSoin.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des sous types de soin
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idSousTypeSoin = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        codeSousTypeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    public String getCodeSousTypeSoin() {
        return codeSousTypeSoin;
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

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

}
