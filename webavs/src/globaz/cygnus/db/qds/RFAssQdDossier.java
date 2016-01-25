/*
 * Créé le 20 janvier 2009
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFAssQdDossier extends BEntity {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_ASS_QD_DOSSIER = "EQIAQD";
    public static final String FIELDNAME_ID_DOSSIER = "EQIDOS";
    public static final String FIELDNAME_ID_QD = "EQIQDB";
    public static final String FIELDNAME_IS_COMPRIS_DANS_CALCUL = "EQBCCA";
    public static final String FIELDNAME_TYPE_RELATION = "EQTREL";

    public static final String TABLE_NAME = "RFAQPDO";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idAssQdDossier = "";
    private String idDossier = "";
    private String idQd = "";
    private Boolean isComprisDansCalcul = Boolean.FALSE;
    private String typeRelation = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFAssQdPrincipaleDossier
     */
    public RFAssQdDossier() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Méthode avant l'ajout, incrémentant la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdAssQdDossier(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table association dossier Qd
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFAssQdDossier.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table association dossier Qd
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idAssQdDossier = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_ID_ASS_QD_DOSSIER);
        idQd = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_ID_QD);
        idDossier = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_ID_DOSSIER);
        typeRelation = statement.dbReadNumeric(RFAssQdDossier.FIELDNAME_TYPE_RELATION);
        isComprisDansCalcul = statement.dbReadBoolean(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL);

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table association dossier Qd
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFAssQdDossier.FIELDNAME_ID_ASS_QD_DOSSIER,
                this._dbWriteNumeric(statement.getTransaction(), idAssQdDossier, "idAssQDPrincipaleDossier"));
    }

    /**
     * Méthode d'écriture des champs dans la table association dossier Qd
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFAssQdDossier.FIELDNAME_ID_ASS_QD_DOSSIER,
                this._dbWriteNumeric(statement.getTransaction(), idAssQdDossier, "idAssQdDossier"));
        statement.writeField(RFAssQdDossier.FIELDNAME_ID_QD,
                this._dbWriteNumeric(statement.getTransaction(), idQd, "idQd"));
        statement.writeField(RFAssQdDossier.FIELDNAME_ID_DOSSIER,
                this._dbWriteNumeric(statement.getTransaction(), idDossier, "idDossier"));
        statement.writeField(RFAssQdDossier.FIELDNAME_TYPE_RELATION,
                this._dbWriteNumeric(statement.getTransaction(), typeRelation, "typeRelation"));
        statement.writeField(RFAssQdDossier.FIELDNAME_IS_COMPRIS_DANS_CALCUL,
                this._dbWriteBoolean(statement.getTransaction(), isComprisDansCalcul, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isComprisDansCalcul"));

    }

    public String getIdAssQdDossier() {
        return idAssQdDossier;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdQd() {
        return idQd;
    }

    public Boolean getIsComprisDansCalcul() {
        return isComprisDansCalcul;
    }

    public String getTypeRelation() {
        return typeRelation;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setIdAssQdDossier(String idAssQdPrincipaleDossier) {
        idAssQdDossier = idAssQdPrincipaleDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdQd(String idQd) {
        this.idQd = idQd;
    }

    public void setIsComprisDansCalcul(Boolean isComprisDansCalcul) {
        this.isComprisDansCalcul = isComprisDansCalcul;
    }

    public void setTypeRelation(String typeRelation) {
        this.typeRelation = typeRelation;
    }

}