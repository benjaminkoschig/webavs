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
public class RFQdSoldeExcedentDeRevenu extends BEntity {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CONCERNE = "FTLCON";
    public static final String FIELDNAME_DATE_MODIFICATION = "FTDMOD";
    public static final String FIELDNAME_ID_FAMILLE_MODIFICATION = "FTIFAM";
    public static final String FIELDNAME_ID_QD = "FTIQDP";
    public static final String FIELDNAME_ID_SOLDE_EXCEDENT = "FTISEX";
    public static final String FIELDNAME_ID_SOLDE_EXCEDENT_MODIFIE = "FTIMPS";
    public static final String FIELDNAME_MONTANT_SOLDE_EXCEDENT = "FTMSEX";
    public static final String FIELDNAME_REMARQUE = "FTLREM";
    public static final String FIELDNAME_TYPE_MODIF = "FTTMOD";
    public static final String FIELDNAME_VISA_GEST = "FTIGES";

    public static final String TABLE_NAME = "RFSEXRE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String concerne = "";
    private String dateModification = "";
    protected String idFamilleModification = "";
    private String idQd = "";
    private String idSoldeExcedentModifie = "";
    private String idSoldeExcedentRevenu = "";
    private String montantSoldeExcedent = "";
    private String remarque = "";
    private String typeModification = "";
    private String visaGestionnaire = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFQdSoldeCharge.
     */
    public RFQdSoldeExcedentDeRevenu() {
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
        return _getCollection() + RFQdSoldeExcedentDeRevenu.TABLE_NAME + " as " + aliasName;
    }

    /**
     * getter pour le nom de la table du solde de charge
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFQdSoldeExcedentDeRevenu.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table du solde de charge
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idSoldeExcedentRevenu = statement.dbReadNumeric(RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_SOLDE_EXCEDENT);
        idFamilleModification = statement.dbReadNumeric(RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_FAMILLE_MODIFICATION);
        dateModification = statement.dbReadDateAMJ(RFQdSoldeExcedentDeRevenu.FIELDNAME_DATE_MODIFICATION);
        montantSoldeExcedent = statement.dbReadNumeric(RFQdSoldeExcedentDeRevenu.FIELDNAME_MONTANT_SOLDE_EXCEDENT);
        remarque = statement.dbReadString(RFQdSoldeExcedentDeRevenu.FIELDNAME_REMARQUE);
        concerne = statement.dbReadString(RFQdSoldeExcedentDeRevenu.FIELDNAME_CONCERNE);
        typeModification = statement.dbReadNumeric(RFQdSoldeExcedentDeRevenu.FIELDNAME_TYPE_MODIF);
        visaGestionnaire = statement.dbReadString(RFQdSoldeExcedentDeRevenu.FIELDNAME_VISA_GEST);
        idQd = statement.dbReadNumeric(RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_QD);
        idSoldeExcedentModifie = statement.dbReadNumeric(RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_SOLDE_EXCEDENT_MODIFIE);
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
        statement.writeKey(RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_SOLDE_EXCEDENT,
                this._dbWriteNumeric(statement.getTransaction(), idSoldeExcedentRevenu, "idSoldeCharge"));
    }

    /**
     * Méthode d'écriture des champs dans la table du solde de charge
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_SOLDE_EXCEDENT,
                this._dbWriteNumeric(statement.getTransaction(), idSoldeExcedentRevenu, "idSoldeExcedentRevenu"));
        statement.writeField(RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_FAMILLE_MODIFICATION,
                this._dbWriteNumeric(statement.getTransaction(), idFamilleModification, "idFamilleModification"));
        statement.writeField(RFQdSoldeExcedentDeRevenu.FIELDNAME_DATE_MODIFICATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateModification, "dateModification"));
        statement.writeField(RFQdSoldeExcedentDeRevenu.FIELDNAME_MONTANT_SOLDE_EXCEDENT,
                this._dbWriteNumeric(statement.getTransaction(), montantSoldeExcedent, "montantSoldeExcedent"));
        statement.writeField(RFQdSoldeExcedentDeRevenu.FIELDNAME_REMARQUE,
                this._dbWriteString(statement.getTransaction(), remarque, "remarque"));
        statement.writeField(RFQdSoldeExcedentDeRevenu.FIELDNAME_CONCERNE,
                this._dbWriteString(statement.getTransaction(), concerne, "concerne"));
        statement.writeField(RFQdSoldeExcedentDeRevenu.FIELDNAME_TYPE_MODIF,
                this._dbWriteNumeric(statement.getTransaction(), typeModification, "typeModification"));
        statement.writeField(RFQdSoldeExcedentDeRevenu.FIELDNAME_VISA_GEST,
                this._dbWriteString(statement.getTransaction(), visaGestionnaire, "visaGestionnaire"));
        statement.writeField(RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_QD,
                this._dbWriteNumeric(statement.getTransaction(), idQd, "idQd"));
        statement.writeField(RFQdSoldeExcedentDeRevenu.FIELDNAME_ID_SOLDE_EXCEDENT_MODIFIE,
                this._dbWriteNumeric(statement.getTransaction(), idSoldeExcedentModifie, "idSoldeExcedentModifie"));
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
        return idSoldeExcedentRevenu;
    }

    public String getIdSoldeExcedentModifie() {
        return idSoldeExcedentModifie;
    }

    /*
     * public String getIdSoldeExcedentRevenu() { return this.idSoldeExcedentRevenu; }
     */

    public String getMontantSoldeExcedent() {
        return montantSoldeExcedent;
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
        idSoldeExcedentRevenu = idQdHistorique;
    }

    public void setIdSoldeExcedentModifie(String idSoldeExcedentModifie) {
        this.idSoldeExcedentModifie = idSoldeExcedentModifie;
    }

    /*
     * public void setIdSoldeExcedentRevenu(String idSoldeExcedentRevenu) { this.idSoldeExcedentRevenu =
     * idSoldeExcedentRevenu; }
     */

    public void setMontantSoldeExcedent(String montantSoldeExcedent) {
        this.montantSoldeExcedent = montantSoldeExcedent;
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
    // qd.setSession(this.getSession());
    // qd.setIdQd(this.getIdQd());
    // qd.retrieve();
    //
    // if (!qd.isNew()) {
    //
    // qd.setMntResiduel(RFUtils.getMntResiduel(qd.getLimiteAnnuelle(),
    // RFUtils.getAugmentationQd(this.getIdQd(), this.getSession()),
    // RFUtils.getSoldeDeCharge(qd.getIdQd(), this.getSession()),
    // RFUtils.getChargeRFM(qd.getIdQd(), "", this.getSession())));
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
