/*
 * Créé le 26 janvier 2012
 */
package globaz.cygnus.db.statistiques;

import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;

/**
 * 
 * @author mbo
 * 
 */
public class RFSousTypeSoinsJointTypeSoinsStatistiques extends RFTypeDeSoin {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête
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
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);

        // jointure entre la table des sous types de soins et la table des types de soins
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        return fromClauseBuffer.toString();
    }

    private String codeSousType = "";
    private String idRubriqueComptableAI = "";
    private String idRubriqueComptableAVS = "";
    private String idRubriqueComptableInvalidite = "";
    private String idSousTypeSoin = "";

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idSousTypeSoin = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);
        codeSousType = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);
        idRubriqueComptableAI = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_AI);
        idRubriqueComptableAVS = statement.dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_AVS);
        idRubriqueComptableInvalidite = statement
                .dbReadNumeric(RFSousTypeDeSoin.FIELDNAME_ID_RUBRIQUE_COMPTABLE_INVALIDITE);
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

    public String getCodeSousType() {
        return codeSousType;
    }

    public String getIdRubriqueComptableAI() {
        return idRubriqueComptableAI;
    }

    public String getIdRubriqueComptableAVS() {
        return idRubriqueComptableAVS;
    }

    public String getIdRubriqueComptableInvalidite() {
        return idRubriqueComptableInvalidite;
    }

    public String getIdSousTypeSoin() {
        return idSousTypeSoin;
    }

    public void setCodeSousType(String codeSousType) {
        this.codeSousType = codeSousType;
    }

    public void setIdRubriqueComptableAI(String idRubriqueComptableAI) {
        this.idRubriqueComptableAI = idRubriqueComptableAI;
    }

    public void setIdRubriqueComptableAVS(String idRubriqueComptableAVS) {
        this.idRubriqueComptableAVS = idRubriqueComptableAVS;
    }

    public void setIdRubriqueComptableInvalidite(String idRubriqueComptableInvalidite) {
        this.idRubriqueComptableInvalidite = idRubriqueComptableInvalidite;
    }

    public void setIdSousTypeSoin(String idSousTypeSoin) {
        this.idSousTypeSoin = idSousTypeSoin;
    }

}
