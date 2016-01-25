/*
 * Créé le 14 janvier 2009
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author jje
 */
public class RFQd extends BEntity {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ANNEE_QD = "ENDAQD";
    public static final String FIELDNAME_CS_ETAT_QD = "ENTETA";
    public static final String FIELDNAME_CS_GENRE_QD = "ENTGQD";
    public static final String FIELDNAME_CS_SOURCE = "ENTSRC";
    public static final String FIELDNAME_CS_TYPE_QD = "ENTQDB";
    public static final String FIELDNAME_DATE_CREATION = "ENDCRE";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "ENIGES";
    public static final String FIELDNAME_ID_QD = "ENIQDB";
    public static final String FIELDNAME_IS_PLAFONNEE = "ENBPLA";
    public static final String FIELDNAME_LIMITE_ANNUELLE = "ENMLIA";
    public static final String FIELDNAME_MONTANT_CHARGE_RFM = "ENMCRF";
    public static final String FIELDNAME_MONTANT_INITIAL_CHARGE_RFM = "ENMICR";

    public static final String TABLE_NAME = "RFQDBAS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String anneeQd = "";
    private String csEtat = "";
    private String csGenreQd = "";
    private String csSource = "";
    // private String csTypeQd = "";
    private String dateCreation = "";
    private String idGestionnaire = "";
    private String idQd = "";
    private Boolean isPlafonnee = Boolean.TRUE;
    // private String dateDebut = "";
    // private String dateFin = "";
    private String limiteAnnuelle = "";
    private String montantChargeRfm = "";
    private String montantInitialChargeRfm = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQd.
     */
    public RFQd() {
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
        setIdQd(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table Qd de Base
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFQd.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table Qd de Base
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idQd = statement.dbReadNumeric(RFQd.FIELDNAME_ID_QD);
        dateCreation = statement.dbReadDateAMJ(RFQd.FIELDNAME_DATE_CREATION);
        limiteAnnuelle = statement.dbReadNumeric(RFQd.FIELDNAME_LIMITE_ANNUELLE);
        // this.csTypeQd = statement.dbReadNumeric(RFQd.FIELDNAME_CS_TYPE_QD);
        csGenreQd = statement.dbReadNumeric(RFQd.FIELDNAME_CS_GENRE_QD);
        isPlafonnee = statement.dbReadBoolean(RFQd.FIELDNAME_IS_PLAFONNEE);
        csEtat = statement.dbReadNumeric(RFQd.FIELDNAME_CS_ETAT_QD);
        csSource = statement.dbReadNumeric(RFQd.FIELDNAME_CS_SOURCE);
        idGestionnaire = statement.dbReadString(RFQd.FIELDNAME_ID_GESTIONNAIRE);
        montantChargeRfm = statement.dbReadNumeric(RFQd.FIELDNAME_MONTANT_CHARGE_RFM);
        anneeQd = statement.dbReadNumeric(RFQd.FIELDNAME_ANNEE_QD);
        montantInitialChargeRfm = statement.dbReadNumeric(RFQd.FIELDNAME_MONTANT_INITIAL_CHARGE_RFM);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table Qd de Base
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFQd.FIELDNAME_ID_QD, this._dbWriteNumeric(statement.getTransaction(), idQd, "idQd"));
    }

    /**
     * Méthode d'écriture des champs dans la table Qd de Base
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFQd.FIELDNAME_ID_QD, this._dbWriteNumeric(statement.getTransaction(), idQd, "idQd"));
        statement.writeField(RFQd.FIELDNAME_DATE_CREATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateCreation, "dateCreation"));
        statement.writeField(RFQd.FIELDNAME_LIMITE_ANNUELLE,
                this._dbWriteNumeric(statement.getTransaction(), limiteAnnuelle, "limiteAnnuelle"));
        // statement.writeField(RFQd.FIELDNAME_CS_TYPE_QD,
        // this._dbWriteNumeric(statement.getTransaction(), this.csTypeQd, "csTypeQd"));
        statement.writeField(RFQd.FIELDNAME_CS_GENRE_QD,
                this._dbWriteNumeric(statement.getTransaction(), csGenreQd, "csGenreQd"));
        statement.writeField(RFQd.FIELDNAME_IS_PLAFONNEE, this._dbWriteBoolean(statement.getTransaction(), isPlafonnee,
                BConstants.DB_TYPE_BOOLEAN_CHAR, "isPlafonnee"));
        statement.writeField(RFQd.FIELDNAME_CS_ETAT_QD,
                this._dbWriteNumeric(statement.getTransaction(), csEtat, "csEtat"));
        statement.writeField(RFQd.FIELDNAME_CS_SOURCE,
                this._dbWriteNumeric(statement.getTransaction(), csSource, "csSource"));
        statement.writeField(RFQd.FIELDNAME_ID_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(RFQd.FIELDNAME_MONTANT_CHARGE_RFM,
                this._dbWriteNumeric(statement.getTransaction(), montantChargeRfm, "montantChargeRfm"));
        statement.writeField(RFQd.FIELDNAME_ANNEE_QD,
                this._dbWriteNumeric(statement.getTransaction(), anneeQd, "anneeQd"));
        statement.writeField(RFQd.FIELDNAME_MONTANT_INITIAL_CHARGE_RFM,
                this._dbWriteNumeric(statement.getTransaction(), montantInitialChargeRfm, "montantInitialChargeRfm"));

    }

    public String getAnneeQd() {
        return anneeQd;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsGenreQd() {
        return csGenreQd;
    }

    public String getCsSource() {
        return csSource;
    }

    /*
     * public String getCsTypeQd() { return this.csTypeQd; }
     */

    public String getDateCreation() {
        return dateCreation;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdQd() {
        return idQd;
    }

    public Boolean getIsPlafonnee() {
        return isPlafonnee;
    }

    public String getLimiteAnnuelle() {
        return limiteAnnuelle;
    }

    public String getMontantChargeRfm() {
        return montantChargeRfm;
    }

    public String getMontantInitialChargeRfm() {
        return montantInitialChargeRfm;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setAnneeQd(String anneeQd) {
        this.anneeQd = anneeQd;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsGenreQd(String typeQd) {
        csGenreQd = typeQd;
    }

    public void setCsSource(String csSource) {
        this.csSource = csSource;
    }

    /*
     * public void setCsTypeQd(String csTypeQd) { this.csTypeQd = csTypeQd; }
     */

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdQd(String idQd) {
        this.idQd = idQd;
    }

    public void setIsPlafonnee(Boolean isPlafonnee) {
        this.isPlafonnee = isPlafonnee;
    }

    public void setLimiteAnnuelle(String limiteAnnuelle) {
        this.limiteAnnuelle = limiteAnnuelle;
    }

    public void setMontantChargeRfm(String montantChargeRfm) {
        this.montantChargeRfm = montantChargeRfm;
    }

    public void setMontantInitialChargeRfm(String montantInitialChargeRfm) {
        this.montantInitialChargeRfm = montantInitialChargeRfm;
    }

}
