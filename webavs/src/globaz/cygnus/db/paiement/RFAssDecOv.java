/*
 * Créé le 15 février 2012
 */
package globaz.cygnus.db.paiement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author mbo
 */
public class RFAssDecOv extends BEntity {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_ASS_DEC_OV = "GCIDTA";
    public static final String FIELDNAME_ID_DECISION = "GCIDDE";
    public static final String FIELDNAME_ID_OV = "GCIDOV";
    public static final String FIELDNAME_NO_RESTITUTION = "GCNORE";

    public static final String TABLE_NAME = "RFDECOV";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusque dans les tiers (Nom et AVS)
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        // String leftJoin = " LEFT JOIN ";
        // String innerJoin = " INNER JOIN ";
        // String on = " ON ";
        // String point = ".";
        // String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssDecOv.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private String idDecision = "";
    private String idDecOv = "";
    private String idOv = "";
    private String numeroRestitution = "";

    /**
     * Crée une nouvelle instance de la classe RFAssDecOv
     */
    public RFAssDecOv() {
        super();
    }

    /**
     * Méthode avant l'ajout, incrémentant la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdDecOv(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table association Decision - Ov
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFAssDecOv.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table association Decision - Ov
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idDecOv = statement.dbReadNumeric(RFAssDecOv.FIELDNAME_ID_ASS_DEC_OV);
        idDecision = statement.dbReadNumeric(RFAssDecOv.FIELDNAME_ID_DECISION);
        idOv = statement.dbReadNumeric(RFAssDecOv.FIELDNAME_ID_OV);
        numeroRestitution = statement.dbReadNumeric(RFAssDecOv.FIELDNAME_NO_RESTITUTION);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table association Decision - Ov
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFAssDecOv.FIELDNAME_ID_ASS_DEC_OV,
                this._dbWriteNumeric(statement.getTransaction(), idDecOv, "idDecOv"));
    }

    /**
     * Méthode d'écriture des champs dans la table association Decision - Ov
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFAssDecOv.FIELDNAME_ID_ASS_DEC_OV,
                this._dbWriteNumeric(statement.getTransaction(), idDecOv, "idDecOv"));
        statement.writeField(RFAssDecOv.FIELDNAME_ID_DECISION,
                this._dbWriteNumeric(statement.getTransaction(), idDecision, "idDecision"));
        statement
                .writeField(RFAssDecOv.FIELDNAME_ID_OV, this._dbWriteNumeric(statement.getTransaction(), idOv, "idOv"));
        statement.writeField(RFAssDecOv.FIELDNAME_NO_RESTITUTION,
                this._dbWriteNumeric(statement.getTransaction(), numeroRestitution, "numeroRestitution"));

    }

    public String getIdDecision() {
        return idDecision;
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public String getIdDecOv() {
        return idDecOv;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getIdOv() {
        return idOv;
    }

    public String getNumeroRestitution() {
        return numeroRestitution;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDecOv(String idDecOv) {
        this.idDecOv = idDecOv;
    }

    public void setIdOv(String idOv) {
        this.idOv = idOv;
    }

    public void setNumeroRestitution(String numeroRestitution) {
        this.numeroRestitution = numeroRestitution;
    }

}