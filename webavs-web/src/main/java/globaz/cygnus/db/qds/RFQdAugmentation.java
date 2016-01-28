/*
 * Créé le 23 mars 2010
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * 
 * @author jje
 */
public class RFQdAugmentation extends BEntity {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CONCERNE = "EVLCON";
    public static final String FIELDNAME_DATE_MODIFICATION = "EVDMOD";
    public static final String FIELDNAME_ID_AUGMENTATION_QD = "EVIAUQ";
    public static final String FIELDNAME_ID_AUGMENTATION_QD_MODIFE_PAR = "EVIMPA";
    public static final String FIELDNAME_ID_FAMILLE_MODIFICATION = "EVIFAM";
    public static final String FIELDNAME_ID_QD = "EVIQDB";
    public static final String FIELDNAME_MONTANT_AUGMENTATION_QD = "EVMAUQ";
    public static final String FIELDNAME_REMARQUE = "EVLREM";
    public static final String FIELDNAME_TYPE_MODIF = "EVTMOD";
    public static final String FIELDNAME_VISA_GEST = "EVIGES";

    public static final String TABLE_NAME = "RFAUGQD";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String concerne = "";
    private String dateModification = "";
    private String idAugmentationQd = "";
    private String idAugmentationQdModifiePar = "";
    protected String idFamilleModification = "";
    private String idQd = "";
    private String montantAugmentationQd = "";
    private String remarque = "";
    private String typeModification = "";
    private String visaGestionnaire = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdAugmentation
     */
    public RFQdAugmentation() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        // updateMontantResiduelQd(transaction);
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // updateMontantResiduelQd(transaction);
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        // updateMontantResiduelQd(transaction);
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdQdHistorique(this._incCounter(transaction, "0"));
    }

    /**
     * génère une clause from avec un alias sur la table augmentation de Qd
     * 
     * @param aliasName
     *            nom de l'alias
     * @return String clause from
     */
    public String _getFromAlias(String aliasName) {
        return _getCollection() + RFQdAugmentation.TABLE_NAME + " as " + aliasName;
    }

    /**
     * getter pour le nom de la table augmentation de Qd
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFQdAugmentation.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table augmentation de Qd
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idAugmentationQd = statement.dbReadNumeric(RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD);
        idFamilleModification = statement.dbReadNumeric(RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION);
        dateModification = statement.dbReadDateAMJ(RFQdAugmentation.FIELDNAME_DATE_MODIFICATION);
        montantAugmentationQd = statement.dbReadNumeric(RFQdAugmentation.FIELDNAME_MONTANT_AUGMENTATION_QD);
        remarque = statement.dbReadString(RFQdAugmentation.FIELDNAME_REMARQUE);
        concerne = statement.dbReadString(RFQdAugmentation.FIELDNAME_CONCERNE);
        typeModification = statement.dbReadNumeric(RFQdAugmentation.FIELDNAME_TYPE_MODIF);
        visaGestionnaire = statement.dbReadString(RFQdAugmentation.FIELDNAME_VISA_GEST);
        idQd = statement.dbReadNumeric(RFQdAugmentation.FIELDNAME_ID_QD);
        idAugmentationQdModifiePar = statement.dbReadNumeric(RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD_MODIFE_PAR);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table augmentation de Qd
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD,
                this._dbWriteNumeric(statement.getTransaction(), idAugmentationQd, "idAugmentationQd"));
    }

    /**
     * Méthode d'écriture des champs dans la table augmentation de Qd
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD,
                this._dbWriteNumeric(statement.getTransaction(), idAugmentationQd, "idAugmentationQd"));
        statement.writeField(RFQdAugmentation.FIELDNAME_ID_FAMILLE_MODIFICATION,
                this._dbWriteNumeric(statement.getTransaction(), idFamilleModification, "idFamilleModification"));
        statement.writeField(RFQdAugmentation.FIELDNAME_DATE_MODIFICATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateModification, "dateModification"));
        statement.writeField(RFQdAugmentation.FIELDNAME_MONTANT_AUGMENTATION_QD,
                this._dbWriteNumeric(statement.getTransaction(), montantAugmentationQd, "montantAugmentationQd"));
        statement.writeField(RFQdAugmentation.FIELDNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, "remarque"));
        statement.writeField(RFQdAugmentation.FIELDNAME_CONCERNE,
                this._dbWriteString(statement.getTransaction(), concerne, "concerne"));
        statement.writeField(RFQdAugmentation.FIELDNAME_TYPE_MODIF,
                this._dbWriteNumeric(statement.getTransaction(), typeModification, "typeModification"));
        statement.writeField(RFQdAugmentation.FIELDNAME_VISA_GEST,
                this._dbWriteString(statement.getTransaction(), visaGestionnaire, "visaGestionnaire"));
        statement.writeField(RFQdAugmentation.FIELDNAME_ID_QD,
                this._dbWriteNumeric(statement.getTransaction(), idQd, "idQd"));
        statement.writeField(RFQdAugmentation.FIELDNAME_ID_AUGMENTATION_QD_MODIFE_PAR, this._dbWriteNumeric(
                statement.getTransaction(), idAugmentationQdModifiePar, "idAugmentationQdModifiePar"));
    }

    public String getConcerne() {
        return concerne;
    }

    public String getDateModification() {
        return dateModification;
    }

    public String getIdAugmentationQdModifiePar() {
        return idAugmentationQdModifiePar;
    }

    public String getIdFamilleModification() {
        return idFamilleModification;
    }

    public String getIdQd() {
        return idQd;
    }

    public String getIdQdHistorique() {
        return idAugmentationQd;
    }

    public String getMontantAugmentationQd() {
        return montantAugmentationQd;
    }

    public String getRemarque() {
        return remarque;
    }

    public String getTypeModification() {
        return typeModification;
    }

    public String getVisaGestionnaire() {
        return visaGestionnaire;
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

    public void setDateModification(String dateModification) {
        this.dateModification = dateModification;
    }

    public void setIdAugmentationQdModifiePar(String idAugmentationQdModifiePar) {
        this.idAugmentationQdModifiePar = idAugmentationQdModifiePar;
    }

    public void setIdFamilleModification(String idFamilleModification) {
        this.idFamilleModification = idFamilleModification;
    }

    public void setIdQd(String idQd) {
        this.idQd = idQd;
    }

    public void setIdQdHistorique(String idAugmentationQd) {
        this.idAugmentationQd = idAugmentationQd;
    }

    public void setMontantAugmentationQd(String montantAugmentationQd) {
        this.montantAugmentationQd = montantAugmentationQd;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setTypeModification(String typeModification) {
        this.typeModification = typeModification;
    }

    public void setVisaGestionnaire(String visaGestionnaire) {
        this.visaGestionnaire = visaGestionnaire;
    }

    /**
     * Mise à jour du montant résiduel de la Qd correspondante
     * 
     * @param BITransaction
     * @throws Exception
     */
    // private void updateMontantResiduelQd(BITransaction transaction) throws Exception {
    // try {
    // RFQd qd = new RFQd();
    // qd.setSession(getSession());
    // qd.setIdQd(getIdQd());
    // qd.retrieve();
    //
    // if (!qd.isNew()) {
    //
    // qd.setMntResiduel(RFUtils.getMntResiduel(qd.getLimiteAnnuelle(), RFUtils.getAugmentationQd(getIdQd(),
    // getSession()), RFUtils.getSoldeDeCharge(qd.getIdQd(), getSession()), RFUtils.getChargeRFM(qd
    // .getIdQd(), "", getSession())));
    //
    // qd.update(transaction);
    //
    // } else {
    // throw new Exception("RFQdAugmentation.updateMontantResiduelQd(): Erreur inattendue");
    // }
    //
    // } catch (Exception e) {
    // if (transaction != null) {
    // transaction.setRollbackOnly();
    // }
    // throw e;
    // } finally {
    // if (transaction != null) {
    // try {
    // if (transaction.hasErrors() || transaction.isRollbackOnly()) {
    // transaction.rollback();
    // } else {
    // transaction.commit();
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // throw e;
    // } finally {
    // transaction.closeTransaction();
    // }
    // }
    // }
    // }

}
