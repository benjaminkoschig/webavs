/*
 * Créé le 06 janvier 2010
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author jje
 */
public class RFQdAssure extends RFQd {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEBUT = "EPDDEB";
    public static final String FIELDNAME_DATE_FIN = "EPDFIN";
    public static final String FIELDNAME_ID_FAMILLLE_AUGMENTATION = "EPIAFA";
    public static final String FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC = "EPIGSF";
    public static final String FIELDNAME_ID_POT_SOUS_TYPE_DE_SOIN = "EPIPST";
    public static final String FIELDNAME_ID_QD_ASSURE = "EPIQDA";
    public static final String FIELDNAME_ID_QD_PRINCIPALE = "EPIQDP";
    public static final String FIELDNAME_ID_SOUS_TYPE_DE_SOIN = "EPISTS";

    public static final String TABLE_NAME = "RFQDASS";
    // private String idQdPrincipale = "";
    private String dateDebut = "";
    private String dateFin = "";
    private boolean hasCreationSpy = false;
    private boolean hasSpy = false;
    private String idFamilleAugmentation = "";
    private String idGesModSoldeExcedentAugmentationQdPreDec = "";
    private String idPotSousTypeDeSoin = "";
    private String idQdAssure = "";
    private String idSousTypeDeSoin = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdAssure.
     */
    public RFQdAssure() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
        // setId(_incCounter(transaction, "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        String getFrom = "";

        getFrom += _getCollection();
        getFrom += RFQd.TABLE_NAME;
        getFrom += " INNER JOIN ";
        getFrom += _getCollection();
        getFrom += RFQdAssure.TABLE_NAME;
        getFrom += " ON ";
        getFrom += RFQdAssure.FIELDNAME_ID_QD_ASSURE;
        getFrom += "=";
        getFrom += RFQd.FIELDNAME_ID_QD;

        return getFrom;
    }

    /**
     * getter pour le nom de la table QdAssuré
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFQdAssure.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table QdAssuré
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idQdAssure = statement.dbReadNumeric(RFQdAssure.FIELDNAME_ID_QD_ASSURE);
        idSousTypeDeSoin = statement.dbReadNumeric(RFQdAssure.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
        idPotSousTypeDeSoin = statement.dbReadNumeric(RFQdAssure.FIELDNAME_ID_POT_SOUS_TYPE_DE_SOIN);
        dateDebut = statement.dbReadDateAMJ(RFQdAssure.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(RFQdAssure.FIELDNAME_DATE_FIN);
        idFamilleAugmentation = statement.dbReadNumeric(RFQdAssure.FIELDNAME_ID_FAMILLLE_AUGMENTATION);
        idGesModSoldeExcedentAugmentationQdPreDec = statement
                .dbReadString(RFQdAssure.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table QdAssuré
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(RFQdAssure.FIELDNAME_ID_QD_ASSURE,
                this._dbWriteNumeric(statement.getTransaction(), idQdAssure, "idQdAssure"));

    }

    /**
     * Méthode d'écriture des champs dans la table QdAssuré
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFQdAssure.FIELDNAME_ID_QD_ASSURE,
                this._dbWriteNumeric(statement.getTransaction(), idQdAssure, "idQdAssure"));
        statement.writeField(RFQdAssure.FIELDNAME_ID_POT_SOUS_TYPE_DE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), idPotSousTypeDeSoin, "idSousTypeDeSoin"));
        statement.writeField(RFQdAssure.FIELDNAME_ID_SOUS_TYPE_DE_SOIN,
                this._dbWriteNumeric(statement.getTransaction(), idSousTypeDeSoin, "idSousTypeDeSoin"));
        // statement.writeField(FIELDNAME_ID_QD_PRINCIPALE,
        // _dbWriteNumeric(statement.getTransaction(),idQdPrincipale,"idQdPrincipale"));
        statement.writeField(RFQdAssure.FIELDNAME_DATE_DEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebutPetiteQd"));
        statement.writeField(RFQdAssure.FIELDNAME_DATE_FIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFinPetiteQd"));
        statement.writeField(RFQdAssure.FIELDNAME_ID_FAMILLLE_AUGMENTATION,
                this._dbWriteNumeric(statement.getTransaction(), idFamilleAugmentation, "idFamilleAugmentation"));
        statement.writeField(RFQdAssure.FIELDNAME_ID_GES_MOD_SOLDE_EXCEDENT_AUGMENTATION_QD_PRE_DEC, this
                ._dbWriteString(statement.getTransaction(), idGesModSoldeExcedentAugmentationQdPreDec,
                        "idGesModSoldeExcedentAugmentationQdPreDec"));
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdFamilleAugmentation() {
        return idFamilleAugmentation;
    }

    public String getIdGesModSoldeExcedentAugmentationQdPreDec() {
        return idGesModSoldeExcedentAugmentationQdPreDec;
    }

    public String getIdPotSousTypeDeSoin() {
        return idPotSousTypeDeSoin;
    }

    public String getIdQdAssure() {
        return idQdAssure;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    @Override
    public boolean hasCreationSpy() {
        return hasCreationSpy;
    }

    @Override
    public boolean hasSpy() {
        return hasSpy;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setHasCreationSpy(boolean hasCreationSpy) {
        this.hasCreationSpy = hasCreationSpy;
    }

    public void setHasSpy(boolean hasSpy) {
        this.hasSpy = hasSpy;
    }

    public void setIdFamilleAugmentation(String idFamilleAugmentation) {
        this.idFamilleAugmentation = idFamilleAugmentation;
    }

    public void setIdGesModSoldeExcedentAugmentationQdPreDec(String idGesModSoldeExcedentAugmentationQdPreDec) {
        this.idGesModSoldeExcedentAugmentationQdPreDec = idGesModSoldeExcedentAugmentationQdPreDec;
    }

    public void setIdPotSousTypeDeSoin(String idPotSousTypeDeSoin) {
        this.idPotSousTypeDeSoin = idPotSousTypeDeSoin;
    }

    public void setIdQdAssure(String idQdAssure) {
        this.idQdAssure = idQdAssure;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

}