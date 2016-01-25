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
public class RFQdSoldeCharge extends BEntity {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CONCERNE = "ERLCON";
    public static final String FIELDNAME_DATE_MODIFICATION = "ERDMOD";
    public static final String FIELDNAME_ID_FAMILLE_MODIFICATION = "ERIFAM";
    public static final String FIELDNAME_ID_QD = "ERIQDB";
    public static final String FIELDNAME_ID_SOLDE_CHARGE = "ERISOC";
    public static final String FIELDNAME_ID_SOLDE_MODIFIE = "ERIMPS";
    public static final String FIELDNAME_MONTANT_SOLDE = "ERMSOC";
    public static final String FIELDNAME_REMARQUE = "ERLREM";
    public static final String FIELDNAME_TYPE_MODIF = "ERTMOD";
    public static final String FIELDNAME_VISA_GEST = "ERIGES";

    public static final String TABLE_NAME = "RFSOCHA";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String concerne = "";
    private String dateModification = "";
    protected String idFamilleModification = "";
    private String idQd = "";
    private String idSoldeCharge = "";
    private String idSoldeChargeModifie = "";
    private String montantSolde = "";
    private String remarque = "";
    private String typeModification = "";
    private String visaGestionnaire = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdSoldeCharge.
     */
    public RFQdSoldeCharge() {
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
     * génère une clause from avec un alias sur la table du solde de charge
     * 
     * @param aliasName
     *            nom de l'alias
     * @return String clause from
     */
    public String _getFromAlias(String aliasName) {
        return _getCollection() + RFQdSoldeCharge.TABLE_NAME + " as " + aliasName;
    }

    /**
     * getter pour le nom de la table du solde de charge
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFQdSoldeCharge.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table du solde de charge
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idSoldeCharge = statement.dbReadNumeric(RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE);
        idFamilleModification = statement.dbReadNumeric(RFQdSoldeCharge.FIELDNAME_ID_FAMILLE_MODIFICATION);
        dateModification = statement.dbReadDateAMJ(RFQdSoldeCharge.FIELDNAME_DATE_MODIFICATION);
        montantSolde = statement.dbReadNumeric(RFQdSoldeCharge.FIELDNAME_MONTANT_SOLDE);
        remarque = statement.dbReadString(RFQdSoldeCharge.FIELDNAME_REMARQUE);
        concerne = statement.dbReadString(RFQdSoldeCharge.FIELDNAME_CONCERNE);
        typeModification = statement.dbReadNumeric(RFQdSoldeCharge.FIELDNAME_TYPE_MODIF);
        visaGestionnaire = statement.dbReadString(RFQdSoldeCharge.FIELDNAME_VISA_GEST);
        idQd = statement.dbReadNumeric(RFQdSoldeCharge.FIELDNAME_ID_QD);
        idSoldeChargeModifie = statement.dbReadNumeric(RFQdSoldeCharge.FIELDNAME_ID_SOLDE_MODIFIE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table du solde de charge
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE,
                this._dbWriteNumeric(statement.getTransaction(), idSoldeCharge, "idSoldeCharge"));
    }

    /**
     * Méthode d'écriture des champs dans la table du solde de charge
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFQdSoldeCharge.FIELDNAME_ID_SOLDE_CHARGE,
                this._dbWriteNumeric(statement.getTransaction(), idSoldeCharge, "idSoldeCharge"));
        statement.writeField(RFQdSoldeCharge.FIELDNAME_ID_FAMILLE_MODIFICATION,
                this._dbWriteNumeric(statement.getTransaction(), idFamilleModification, "idFamilleModification"));
        statement.writeField(RFQdSoldeCharge.FIELDNAME_DATE_MODIFICATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateModification, "dateModification"));
        statement.writeField(RFQdSoldeCharge.FIELDNAME_MONTANT_SOLDE,
                this._dbWriteNumeric(statement.getTransaction(), montantSolde, "montantSolde"));
        statement.writeField(RFQdSoldeCharge.FIELDNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, "remarque"));
        statement.writeField(RFQdSoldeCharge.FIELDNAME_CONCERNE,
                this._dbWriteString(statement.getTransaction(), concerne, "concerne"));
        statement.writeField(RFQdSoldeCharge.FIELDNAME_TYPE_MODIF,
                this._dbWriteNumeric(statement.getTransaction(), typeModification, "typeModification"));
        statement.writeField(RFQdSoldeCharge.FIELDNAME_VISA_GEST,
                this._dbWriteString(statement.getTransaction(), visaGestionnaire, "visaGestionnaire"));
        statement.writeField(RFQdSoldeCharge.FIELDNAME_ID_QD,
                this._dbWriteNumeric(statement.getTransaction(), idQd, "idQd"));
        statement.writeField(RFQdSoldeCharge.FIELDNAME_ID_SOLDE_MODIFIE,
                this._dbWriteNumeric(statement.getTransaction(), idSoldeChargeModifie, "idSoldeChargeModifie"));
    }

    /**
     * @return the concerne
     */
    public String getConcerne() {
        return concerne;
    }

    /**
     * @return the dateModification
     */
    public String getDateModification() {
        return dateModification;
    }

    /**
     * @return the idFamilleModification
     */
    public String getIdFamilleModification() {
        return idFamilleModification;
    }

    /**
     * @return the idQd
     */
    public String getIdQd() {
        return idQd;
    }

    /**
     * @return the idQdHistorique
     */
    public String getIdQdHistorique() {
        return idSoldeCharge;
    }

    /**
     * @return the idSoldeCharge
     */
    public String getIdSoldeChargeModifie() {
        return idSoldeChargeModifie;
    }

    /**
     * @return the montantSolde
     */
    public String getMontantSolde() {
        return montantSolde;
    }

    /**
     * @return the remarque
     */
    public String getRemarque() {
        return remarque;
    }

    /**
     * @return the typeModification
     */
    public String getTypeModification() {
        return typeModification;
    }

    /**
     * @return the visaGestionnaire
     * @throws Exception
     */
    public String getVisaGestionnaire() throws Exception {
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

    /**
     * @param concerne
     *            the concerne to set
     */
    public void setConcerne(String concerne) {
        this.concerne = concerne;
    }

    /**
     * @param dateModification
     *            the dateModification to set
     */
    public void setDateModification(String dateModification) {
        this.dateModification = dateModification;
    }

    /**
     * @param idFamilleModification
     *            the idFamilleModification to set
     */
    public void setIdFamilleModification(String idFamilleModification) {
        this.idFamilleModification = idFamilleModification;
    }

    /**
     * @param idQd
     *            the idQd to set
     */
    public void setIdQd(String idQd) {
        this.idQd = idQd;
    }

    /**
     * @param idQdHistorique
     *            the idQdHistorique to set
     */
    public void setIdQdHistorique(String idQdHistorique) {
        idSoldeCharge = idQdHistorique;
    }

    /**
     * @param idSoldeCharge
     *            the idSoldeCharge to set
     */
    public void setIdSoldeChargeModifie(String idSoldeCharge) {
        idSoldeChargeModifie = idSoldeCharge;
    }

    /**
     * @param montantSolde
     *            the montantSolde to set
     */
    public void setMontantSolde(String montantSolde) {
        this.montantSolde = montantSolde;
    }

    /**
     * @param remarque
     *            the remarque to set
     */
    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    /**
     * @param typeModification
     *            the typeModification to set
     */
    public void setTypeModification(String typeModification) {
        this.typeModification = typeModification;
    }

    /**
     * @param visaGestionnaire
     *            the visaGestionnaire to set
     */
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
    //
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
    // throw new Exception("RFQdSoldeCharge.updateMontantResiduelQd(): Erreur inattendue");
    // }
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
