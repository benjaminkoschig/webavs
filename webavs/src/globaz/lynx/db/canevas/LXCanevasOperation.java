package globaz.lynx.db.canevas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class LXCanevasOperation extends BEntity implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_COURSMONNAIE = "COURSMONNAIE";
    public static final String FIELD_CSCODEISOMONNAIE = "CSCODEISOMONNAIE";
    public static final String FIELD_CSCODETVA = "CSCODETVA";
    public static final String FIELD_CSTYPEOPERATION = "CSTYPEOPERATION";
    public static final String FIELD_IDADRESSEPMT = "IDADRESSEPMT";
    // Colonnes de la table
    public static final String FIELD_IDOPERATIONCANEVAS = "IDOPERATIONCANEVAS";
    public static final String FIELD_IDORGANEEXECUTION = "IDORGANEEXECUTION";
    public static final String FIELD_IDSECTIONCANEVAS = "IDSECTIONCANEVAS";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_MONTANT = "MONTANT";
    public static final String FIELD_MONTANTMONNAIE = "MONTANTMONNAIE";
    public static final String FIELD_MOTIF = "MOTIF";
    public static final String FIELD_REFERENCEBVR = "REFERENCEBVR";
    public static final String FIELD_REFERENCEEXTERNE = "REFERENCEEXTERNE";
    public static final String FIELD_TAUXESCOMPTE = "TAUXESCOMPTE";

    // Nom de la table
    public static final String TABLE_LXCANOP = "LXCANOP";

    private String coursMonnaie = "";
    private String csCodeIsoMonnaie = "";
    private String csCodeTVA = "";
    private String csTypeOperation = "";
    private String idAdressePaiement = "";
    private String idOperationCanevas = "";
    private String idOrganeExecution = "";
    private String idSectionCanevas = "";
    private String libelle = "";
    private String montant = "";
    private String montantMonnaie = "";
    private String motif = "";
    private String referenceBVR = "";
    private String referenceExterne = "";
    private String tauxEscompte = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdOperationCanevas(this._incCounter(transaction, idOperationCanevas));
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return LXCanevasOperation.TABLE_LXCANOP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdOperationCanevas(statement.dbReadNumeric(LXCanevasOperation.FIELD_IDOPERATIONCANEVAS));
        setIdSectionCanevas(statement.dbReadNumeric(LXCanevasOperation.FIELD_IDSECTIONCANEVAS));
        setCsTypeOperation(statement.dbReadNumeric(LXCanevasOperation.FIELD_CSTYPEOPERATION));
        setLibelle(statement.dbReadString(LXCanevasOperation.FIELD_LIBELLE));
        setMontant(statement.dbReadNumeric(LXCanevasOperation.FIELD_MONTANT, 2));
        setIdAdressePaiement(statement.dbReadNumeric(LXCanevasOperation.FIELD_IDADRESSEPMT));
        setIdOrganeExecution(statement.dbReadNumeric(LXCanevasOperation.FIELD_IDORGANEEXECUTION));
        setTauxEscompte(statement.dbReadNumeric(LXCanevasOperation.FIELD_TAUXESCOMPTE, 2));
        setReferenceBVR(statement.dbReadString(LXCanevasOperation.FIELD_REFERENCEBVR));
        setReferenceExterne(statement.dbReadString(LXCanevasOperation.FIELD_REFERENCEEXTERNE));
        setCsCodeTVA(statement.dbReadNumeric(LXCanevasOperation.FIELD_CSCODETVA));
        setCsCodeIsoMonnaie(statement.dbReadNumeric(LXCanevasOperation.FIELD_CSCODEISOMONNAIE));
        setMontantMonnaie(statement.dbReadNumeric(LXCanevasOperation.FIELD_MONTANTMONNAIE, 2));
        setCoursMonnaie(statement.dbReadNumeric(LXCanevasOperation.FIELD_COURSMONNAIE, 5));
        setMotif(statement.dbReadString(LXCanevasOperation.FIELD_MOTIF));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nothing
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(LXCanevasOperation.FIELD_IDOPERATIONCANEVAS,
                this._dbWriteNumeric(statement.getTransaction(), getIdOperationCanevas(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(LXCanevasOperation.FIELD_IDOPERATIONCANEVAS,
                this._dbWriteNumeric(statement.getTransaction(), getIdOperationCanevas(), "idOperationCanevas"));
        statement.writeField(LXCanevasOperation.FIELD_IDSECTIONCANEVAS,
                this._dbWriteNumeric(statement.getTransaction(), getIdSectionCanevas(), "idSectionCanevas"));
        statement.writeField(LXCanevasOperation.FIELD_CSTYPEOPERATION,
                this._dbWriteNumeric(statement.getTransaction(), getCsTypeOperation(), "csTypeOperation"));
        statement.writeField(LXCanevasOperation.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(LXCanevasOperation.FIELD_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField(LXCanevasOperation.FIELD_IDADRESSEPMT,
                this._dbWriteNumeric(statement.getTransaction(), getIdAdressePaiement(), "idAdressePaiement"));
        statement.writeField(LXCanevasOperation.FIELD_IDORGANEEXECUTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), "idOrganeExecution"));
        statement.writeField(LXCanevasOperation.FIELD_TAUXESCOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), getTauxEscompte(), "tauxEscompte"));
        statement.writeField(LXCanevasOperation.FIELD_REFERENCEBVR,
                this._dbWriteString(statement.getTransaction(), getReferenceBVR(), "referenceBVR"));
        statement.writeField(LXCanevasOperation.FIELD_REFERENCEEXTERNE,
                this._dbWriteString(statement.getTransaction(), getReferenceExterne(), "referenceExterne"));
        statement.writeField(LXCanevasOperation.FIELD_CSCODETVA,
                this._dbWriteNumeric(statement.getTransaction(), getCsCodeTVA(), "csCodeTVA"));
        statement.writeField(LXCanevasOperation.FIELD_CSCODEISOMONNAIE,
                this._dbWriteNumeric(statement.getTransaction(), getCsCodeIsoMonnaie(), "csCodeIsoMonnaie"));
        statement.writeField(LXCanevasOperation.FIELD_MONTANTMONNAIE,
                this._dbWriteNumeric(statement.getTransaction(), getMontantMonnaie(), "montantMonnaie"));
        statement.writeField(LXCanevasOperation.FIELD_COURSMONNAIE,
                this._dbWriteNumeric(statement.getTransaction(), getCoursMonnaie(), "coursMonnaie"));
        statement.writeField(LXCanevasOperation.FIELD_MOTIF,
                this._dbWriteString(statement.getTransaction(), getMotif(), "motif"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    // *******************************************************
    // Clonage de l'object
    // *******************************************************
    @Override
    public Object clone() {
        LXCanevasOperation operationClone = null;
        try {
            operationClone = (LXCanevasOperation) super.clone();
        } catch (CloneNotSupportedException cnse) {
            _addError(getSession().getCurrentThreadTransaction(), getSession().getLabel("CLONE_ERROR"));
        }
        return operationClone;
    }

    public String getCoursMonnaie() {
        return coursMonnaie;
    }

    public String getCsCodeIsoMonnaie() {
        return csCodeIsoMonnaie;
    }

    public String getCsCodeTVA() {
        return csCodeTVA;
    }

    public String getCsTypeOperation() {
        return csTypeOperation;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdOperationCanevas() {
        return idOperationCanevas;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getIdSectionCanevas() {
        return idSectionCanevas;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantMonnaie() {
        return montantMonnaie;
    }

    public String getMotif() {
        return motif;
    }

    public String getReferenceBVR() {
        return referenceBVR;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getTauxEscompte() {
        return tauxEscompte;
    }

    public void setCoursMonnaie(String coursMonnaie) {
        this.coursMonnaie = coursMonnaie;
    }

    public void setCsCodeIsoMonnaie(String csCodeIsoMonnaie) {
        this.csCodeIsoMonnaie = csCodeIsoMonnaie;
    }

    public void setCsCodeTVA(String csCodeTVA) {
        this.csCodeTVA = csCodeTVA;
    }

    public void setCsTypeOperation(String csTypeOperation) {
        this.csTypeOperation = csTypeOperation;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdOperationCanevas(String idOperationCanevas) {
        this.idOperationCanevas = idOperationCanevas;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setIdSectionCanevas(String idSectionCanevas) {
        this.idSectionCanevas = idSectionCanevas;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantMonnaie(String montantMonnaie) {
        this.montantMonnaie = montantMonnaie;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setReferenceBVR(String referenceBVR) {
        this.referenceBVR = referenceBVR;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public void setTauxEscompte(String tauxEscompte) {
        this.tauxEscompte = tauxEscompte;
    }
}
