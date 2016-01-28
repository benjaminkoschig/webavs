/*
 * Créé le 14 janvier 2009
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author jje
 */
public class RFPeriodeValiditeQdPrincipale extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CONCERNE = "FCLCON";
    public static final String FIELDNAME_DATE_DEBUT = "FCDDEB";
    public static final String FIELDNAME_DATE_FIN = "FCDFIN";
    public static final String FIELDNAME_DATE_MODIFICATION = "FCDMOD";
    public static final String FIELDNAME_ID_FAMILLE_MODIFICATION = "FCIFAM";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "FCIGES";
    public static final String FIELDNAME_ID_PERIODE_VAL_MODIFIE_PAR = "FCIMPQ";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String FIELDNAME_ID_PERIODE_VALIDITE = "FCIQDP";
    public static final String FIELDNAME_ID_QD_PRINCIPALE = "FCIQPR";
    public static final String FIELDNAME_REMARQUE = "FCLREM";
    public static final String FIELDNAME_TYPE_MODIFICATION = "FCTMOD";

    public static final String TABLE_NAME = "RFQDPVA";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String concerne = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String DateModification = "";
    protected String idFamilleModification = "";
    private String idGestionnaire = "";
    private String idPeriodeValidite = "";
    private String idPeriodeValModifiePar = "";
    private String idQd = "";
    private String remarque = "";
    private String typeModification = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQd.
     */
    public RFPeriodeValiditeQdPrincipale() {
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
        setIdPeriodeValidite(this._incCounter(transaction, "0"));
    }

    /**
     * génère une clause from avec un alias sur la table augmentation de Qd
     * 
     * @param aliasName
     *            nom de l'alias
     * @return String clause from
     */
    public String _getFromAlias(String aliasName) {
        return _getCollection() + RFPeriodeValiditeQdPrincipale.TABLE_NAME + " as " + aliasName;
    }

    /**
     * getter pour le nom de la table Qd de Base
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFPeriodeValiditeQdPrincipale.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table Qd de Base
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idPeriodeValidite = statement.dbReadNumeric(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE);
        idQd = statement.dbReadNumeric(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE);
        dateDebut = statement.dbReadDateAMJ(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN);
        idPeriodeValModifiePar = statement
                .dbReadNumeric(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VAL_MODIFIE_PAR);
        typeModification = statement.dbReadNumeric(RFPeriodeValiditeQdPrincipale.FIELDNAME_TYPE_MODIFICATION);
        DateModification = statement.dbReadDateAMJ(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_MODIFICATION);
        idFamilleModification = statement
                .dbReadNumeric(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION);
        remarque = statement.dbReadString(RFPeriodeValiditeQdPrincipale.FIELDNAME_REMARQUE);
        idGestionnaire = statement.dbReadString(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_GESTIONNAIRE);
        concerne = statement.dbReadString(RFPeriodeValiditeQdPrincipale.FIELDNAME_CONCERNE);
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
        statement.writeKey(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE,
                this._dbWriteNumeric(statement.getTransaction(), idPeriodeValidite, "idPeriodeValidite"));
    }

    /**
     * Méthode d'écriture des champs dans la table Qd de Base
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VALIDITE,
                this._dbWriteNumeric(statement.getTransaction(), idPeriodeValidite, "idPeriodeValidite"));
        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_QD_PRINCIPALE,
                this._dbWriteNumeric(statement.getTransaction(), idQd, "idQd"));
        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_DEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebutPeriodeValidite"));
        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_FIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFinPeriodeValidite"));
        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_PERIODE_VAL_MODIFIE_PAR,
                this._dbWriteNumeric(statement.getTransaction(), idPeriodeValModifiePar, "idPeriodeValModifiePar"));
        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_TYPE_MODIFICATION,
                this._dbWriteNumeric(statement.getTransaction(), typeModification, "typeModification"));
        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_DATE_MODIFICATION,
                this._dbWriteDateAMJ(statement.getTransaction(), DateModification, "DateModification"));
        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_FAMILLE_MODIFICATION,
                this._dbWriteNumeric(statement.getTransaction(), idFamilleModification, "idFamilleModification"));
        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, "remarque"));
        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_ID_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(RFPeriodeValiditeQdPrincipale.FIELDNAME_CONCERNE,
                this._dbWriteString(statement.getTransaction(), concerne, "concerne"));
    }

    public String getConcerne() {
        return concerne;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateModification() {
        return DateModification;
    }

    public String getIdFamilleModification() {
        return idFamilleModification;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdPeriodeValidite() {
        return idPeriodeValidite;
    }

    public String getIdPeriodeValModifiePar() {
        return idPeriodeValModifiePar;
    }

    public String getIdQd() {
        return idQd;
    }

    public String getRemarque() {
        return remarque;
    }

    public String getTypeModification() {
        return typeModification;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setConcerne(String concerne) {
        this.concerne = concerne;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateModification(String dateModification) {
        DateModification = dateModification;
    }

    public void setIdFamilleModification(String idFamilleModification) {
        this.idFamilleModification = idFamilleModification;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdPeriodeValidite(String idPeriodeValidite) {
        this.idPeriodeValidite = idPeriodeValidite;
    }

    public void setIdPeriodeValModifiePar(String idPeriodeValModifiePar) {
        this.idPeriodeValModifiePar = idPeriodeValModifiePar;
    }

    public void setIdQd(String idQd) {
        this.idQd = idQd;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setTypeModification(String typeModification) {
        this.typeModification = typeModification;
    }

}
