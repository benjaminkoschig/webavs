package globaz.lynx.db.operation;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.utils.LXConstants;
import java.math.BigDecimal;

public class LXOperation extends BEntity implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Code systeme ETAT (LXETATOPER)
    public static final String CS_ETAT = "LXETATOPER";

    public static final String CS_ETAT_ANNULE = "7800003";
    public static final String CS_ETAT_COMPTABILISE = "7800004";
    public static final String CS_ETAT_OUVERT = "7800001";
    public static final String CS_ETAT_PREPARE = "7800005";
    public static final String CS_ETAT_SOLDE = "7800006";
    public static final String CS_ETAT_TRAITEMENT = "7800002";

    // Code systeme TYPE (LXTYPEOPER)
    public static final String CS_TYPE = "LXTYPEOPER";
    public static final String CS_TYPE_ESCOMPTE = "7700003";
    public static final String CS_TYPE_EXTOURNE = "7700015";
    public static final String CS_TYPE_FACTURE_BVR_ORANGE = "7700001";
    public static final String CS_TYPE_FACTURE_BVR_ROUGE = "7700006";
    public static final String CS_TYPE_FACTURE_CAISSE = "7700009";
    public static final String CS_TYPE_FACTURE_LSV = "7700008";
    public static final String CS_TYPE_FACTURE_VIREMENT = "7700007";
    public static final String CS_TYPE_NOTEDECREDIT_DEBASE = "7700010";
    public static final String CS_TYPE_NOTEDECREDIT_ENCAISSEE = "7700005";
    public static final String CS_TYPE_NOTEDECREDIT_LIEE = "7700004";
    public static final String CS_TYPE_PAIEMENT_BVR_ORANGE = "7700002";
    public static final String CS_TYPE_PAIEMENT_BVR_ROUGE = "7700011";
    public static final String CS_TYPE_PAIEMENT_CAISSE = "7700014";
    public static final String CS_TYPE_PAIEMENT_LSV = "7700013";
    public static final String CS_TYPE_PAIEMENT_VIREMENT = "7700012";

    public static final String FIELD_COUNTPMT = "COUNTPMT";
    public static final String FIELD_COURSMONNAIE = "COURSMONNAIE";
    public static final String FIELD_CSCODEISOMONNAIE = "CSCODEISOMONNAIE";
    public static final String FIELD_CSCODETVA = "CSCODETVA";
    public static final String FIELD_CSETATOPERATION = "CSETATOPERATION";
    public static final String FIELD_CSMOTIFBLOCAGE = "CSMOTIFBLOCAGE";
    public static final String FIELD_CSTYPEOPERATION = "CSTYPEOPERATION";
    public static final String FIELD_DATEECHEANCE = "DATEECHEANCE";
    public static final String FIELD_DATEOPERATION = "DATEOPERATION";
    public static final String FIELD_ESTBLOQUE = "ESTBLOQUE";
    public static final String FIELD_IDADRESSEPMT = "IDADRESSEPMT";
    public static final String FIELD_IDJOURNAL = "IDJOURNAL";
    // Colonnes de la table
    public static final String FIELD_IDOPERATION = "IDOPERATION";
    public static final String FIELD_IDOPERATIONLIEE = "IDOPERATIONLIEE";
    public static final String FIELD_IDOPERATIONSRC = "IDOPERATIONSRC";
    public static final String FIELD_IDORDREGROUPE = "IDORDREGROUPE";
    public static final String FIELD_IDORGANEEXECUTION = "IDORGANEEXECUTION";
    public static final String FIELD_IDSECTION = "IDSECTION";
    public static final String FIELD_IDSECTIONLIEE = "IDSECTIONLIEE";
    public static final String FIELD_LIBELLE = "LIBELLE";
    public static final String FIELD_MONTANT = "MONTANT";
    public static final String FIELD_MONTANTMONNAIE = "MONTANTMONNAIE";
    public static final String FIELD_MOTIF = "MOTIF";
    public static final String FIELD_NUMTRANSACTION = "NUMTRANSACTION";
    public static final String FIELD_REFERENCEBVR = "REFERENCEBVR";
    public static final String FIELD_REFERENCEEXTERNE = "REFERENCEEXTERNE";
    public static final String FIELD_TAUXESCOMPTE = "TAUXESCOMPTE";

    // Nom de la table
    public static final String TABLE_LXOPERP = "LXOPERP";

    private String countPmt = "0";
    private String coursMonnaie = "";
    private String csCodeIsoMonnaie = "";
    private String csCodeTVA = "";
    private String csEtatOperation = "";
    private String csMotifBlocage = "";
    private String csTypeOperation = "";
    private String dateEcheance = "";
    private String dateOperation = "";
    private Boolean estBloque = new Boolean(false);
    private String idAdressePaiement = "";
    private String idJournal = "";
    private String idOperation = "";
    private String idOperationLiee = "";
    private String idOperationSrc = "";
    private String idOrdreGroupe = "";
    private String idOrganeExecution = "";
    private String idSection = "";
    private String idSectionLiee = "";
    private String libelle = "";
    private String montant = "";
    private String montantMonnaie = "";
    private String motif = "";
    private String numeroTransaction = "";
    private String referenceBVR = "";
    private String referenceExterne = "";
    private String tauxEscompte = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdOperation(_incCounter(transaction, idOperation));

        if (JadeStringUtil.isBlank(getDateOperation())) {
            setDateOperation(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_LXOPERP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdOperation(statement.dbReadNumeric(FIELD_IDOPERATION));
        setIdSection(statement.dbReadNumeric(FIELD_IDSECTION));
        setIdJournal(statement.dbReadNumeric(FIELD_IDJOURNAL));
        setIdOrdreGroupe(statement.dbReadNumeric(FIELD_IDORDREGROUPE));
        setIdSectionLiee(statement.dbReadNumeric(FIELD_IDSECTIONLIEE));
        setIdOperationLiee(statement.dbReadNumeric(FIELD_IDOPERATIONLIEE));
        setIdOperationSrc(statement.dbReadNumeric(FIELD_IDOPERATIONSRC));
        setNumeroTransaction(statement.dbReadNumeric(FIELD_NUMTRANSACTION));
        setCsTypeOperation(statement.dbReadNumeric(FIELD_CSTYPEOPERATION));
        setDateOperation(statement.dbReadDateAMJ(FIELD_DATEOPERATION));
        setLibelle(statement.dbReadString(FIELD_LIBELLE));
        setCsEtatOperation(statement.dbReadNumeric(FIELD_CSETATOPERATION));
        setMontant(statement.dbReadNumeric(FIELD_MONTANT, 2));
        setIdAdressePaiement(statement.dbReadNumeric(FIELD_IDADRESSEPMT));
        setIdOrganeExecution(statement.dbReadNumeric(FIELD_IDORGANEEXECUTION));
        setTauxEscompte(statement.dbReadNumeric(FIELD_TAUXESCOMPTE, 2));
        setCsMotifBlocage(statement.dbReadNumeric(FIELD_CSMOTIFBLOCAGE));
        setEstBloque(statement.dbReadBoolean(FIELD_ESTBLOQUE));
        setReferenceBVR(statement.dbReadString(FIELD_REFERENCEBVR));
        setDateEcheance(statement.dbReadDateAMJ(FIELD_DATEECHEANCE));
        setReferenceExterne(statement.dbReadString(FIELD_REFERENCEEXTERNE));
        setCsCodeTVA(statement.dbReadNumeric(FIELD_CSCODETVA));
        setCsCodeIsoMonnaie(statement.dbReadNumeric(FIELD_CSCODEISOMONNAIE));
        setMontantMonnaie(statement.dbReadNumeric(FIELD_MONTANTMONNAIE, 2));
        setCoursMonnaie(statement.dbReadNumeric(FIELD_COURSMONNAIE, 5));
        setMotif(statement.dbReadString(FIELD_MOTIF));
        setCountPmt(statement.dbReadNumeric(FIELD_COUNTPMT));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlank(getCsEtatOperation())) {
            setCsEtatOperation(LXOperation.CS_ETAT_OUVERT);
        }

        if (!JadeStringUtil.isBlank(getReferenceBVR())) {
            setReferenceBVR(JadeStringUtil.removeChar(getReferenceBVR(), ' '));
        }

        if (JadeStringUtil.isBlank(getCsCodeIsoMonnaie())) {
            setCsCodeIsoMonnaie(LXConstants.CODE_ISO_CHF);
        }

        if (!LXConstants.CODE_ISO_CHF.equals(getCsCodeIsoMonnaie())) {
            validateMontantEtranger();
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDOPERATION, _dbWriteNumeric(statement.getTransaction(), getIdOperation(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDOPERATION,
                _dbWriteNumeric(statement.getTransaction(), getIdOperation(), "idOperation"));
        statement.writeField(FIELD_IDSECTION, _dbWriteNumeric(statement.getTransaction(), getIdSection(), "idSection"));
        statement.writeField(FIELD_IDJOURNAL, _dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField(FIELD_IDORDREGROUPE,
                _dbWriteNumeric(statement.getTransaction(), getIdOrdreGroupe(), "idOrdreGroupe"));
        statement.writeField(FIELD_IDSECTIONLIEE,
                _dbWriteNumeric(statement.getTransaction(), getIdSectionLiee(), "idSectionLiee"));
        statement.writeField(FIELD_IDOPERATIONLIEE,
                _dbWriteNumeric(statement.getTransaction(), getIdOperationLiee(), "idOperationLiee"));
        statement.writeField(FIELD_IDOPERATIONSRC,
                _dbWriteNumeric(statement.getTransaction(), getIdOperationSrc(), "idOperationSrc"));
        statement.writeField(FIELD_NUMTRANSACTION,
                _dbWriteNumeric(statement.getTransaction(), getNumeroTransaction(), "numTransaction"));
        statement.writeField(FIELD_CSTYPEOPERATION,
                _dbWriteNumeric(statement.getTransaction(), getCsTypeOperation(), "csTypeOperation"));
        statement.writeField(FIELD_DATEOPERATION,
                _dbWriteDateAMJ(statement.getTransaction(), getDateOperation(), "dateOperation"));
        statement.writeField(FIELD_LIBELLE, _dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField(FIELD_MONTANT, _dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField(FIELD_CSETATOPERATION,
                _dbWriteNumeric(statement.getTransaction(), getCsEtatOperation(), "csEtatOperation"));
        statement.writeField(FIELD_IDADRESSEPMT,
                _dbWriteNumeric(statement.getTransaction(), getIdAdressePaiement(), "idAdressePaiement"));
        statement.writeField(FIELD_IDORGANEEXECUTION,
                _dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), "idOrganeExecution"));
        statement.writeField(FIELD_TAUXESCOMPTE,
                _dbWriteNumeric(statement.getTransaction(), getTauxEscompte(), "tauxEscompte"));
        statement.writeField(FIELD_CSMOTIFBLOCAGE,
                _dbWriteNumeric(statement.getTransaction(), getCsMotifBlocage(), "csMotifBlocage"));
        statement.writeField(
                FIELD_ESTBLOQUE,
                _dbWriteBoolean(statement.getTransaction(), getEstBloque(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "estBloque"));
        statement.writeField(FIELD_REFERENCEBVR,
                _dbWriteString(statement.getTransaction(), getReferenceBVR(), "referenceBVR"));
        statement.writeField(FIELD_DATEECHEANCE,
                _dbWriteDateAMJ(statement.getTransaction(), getDateEcheance(), "dateEcheance"));
        statement.writeField(FIELD_REFERENCEEXTERNE,
                _dbWriteString(statement.getTransaction(), getReferenceExterne(), "referenceExterne"));
        statement.writeField(FIELD_CSCODETVA, _dbWriteNumeric(statement.getTransaction(), getCsCodeTVA(), "csCodeTVA"));
        statement.writeField(FIELD_CSCODEISOMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getCsCodeIsoMonnaie(), "csCodeIsoMonnaie"));
        statement.writeField(FIELD_MONTANTMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getMontantMonnaie(), "montantMonnaie"));
        statement.writeField(FIELD_COURSMONNAIE,
                _dbWriteNumeric(statement.getTransaction(), getCoursMonnaie(), "coursMonnaie"));
        statement.writeField(FIELD_MOTIF, _dbWriteString(statement.getTransaction(), getMotif(), "motif"));
        statement.writeField(FIELD_COUNTPMT, _dbWriteNumeric(statement.getTransaction(), getCountPmt(), "countPmt"));
    }

    // *******************************************************
    // Clonage de l'object
    // *******************************************************
    @Override
    public Object clone() {
        LXOperation operationClone = null;
        try {
            operationClone = (LXOperation) super.clone();
        } catch (CloneNotSupportedException cnse) {
            _addError(getSession().getCurrentThreadTransaction(), getSession().getLabel("CLONE_ERROR"));
        }
        return operationClone;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getCountPmt() {
        return countPmt;
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

    public String getCsEtatOperation() {
        return csEtatOperation;
    }

    public String getCsMotifBlocage() {
        return csMotifBlocage;
    }

    public String getCsTypeOperation() {
        return csTypeOperation;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public Boolean getEstBloque() {
        return estBloque;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdOperation() {
        return idOperation;
    }

    public String getIdOperationLiee() {
        return idOperationLiee;
    }

    public String getIdOperationSrc() {
        return idOperationSrc;
    }

    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdSectionLiee() {
        return idSectionLiee;
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

    public String getNumeroTransaction() {
        return numeroTransaction;
    }

    public String getReferenceBVR() {
        return referenceBVR;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public String getTauxEscompte() {
        return tauxEscompte;
    }

    public void setCountPmt(String countPmt) {
        this.countPmt = countPmt;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setCoursMonnaie(String coursMonnaie) {
        this.coursMonnaie = coursMonnaie;
    }

    public void setCsCodeIsoMonnaie(String csCodeIsoMonnaie) {
        this.csCodeIsoMonnaie = csCodeIsoMonnaie;
    }

    public void setCsCodeTVA(String csCodeTVA) {
        this.csCodeTVA = csCodeTVA;
    }

    public void setCsEtatOperation(String csEtatOperation) {
        this.csEtatOperation = csEtatOperation;
    }

    public void setCsMotifBlocage(String csMotifBlocage) {
        this.csMotifBlocage = csMotifBlocage;
    }

    public void setCsTypeOperation(String csTypeOperation) {
        this.csTypeOperation = csTypeOperation;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }

    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdOperation(String idOperation) {
        this.idOperation = idOperation;
    }

    public void setIdOperationLiee(String idOperationLiee) {
        this.idOperationLiee = idOperationLiee;
    }

    public void setIdOperationSrc(String idOperationSrc) {
        this.idOperationSrc = idOperationSrc;
    }

    public void setIdOrdreGroupe(String idOrdreGroupe) {
        this.idOrdreGroupe = idOrdreGroupe;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdSectionLiee(String idSectionLiee) {
        this.idSectionLiee = idSectionLiee;
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

    public void setNumeroTransaction(String numeroTransaction) {
        this.numeroTransaction = numeroTransaction;
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

    /**
     * Pour une opération en monnaie étrangère, calcul de linformation manquante (montant chf, montant etr et cours).
     * 
     * @throws Exception
     */
    private void validateMontantEtranger() throws Exception {
        if (!JadeStringUtil.isDecimalEmpty(getMontant()) && !JadeStringUtil.isDecimalEmpty(getCoursMonnaie())
                && JadeStringUtil.isDecimalEmpty(getMontantMonnaie())) {
            BigDecimal montant = new BigDecimal(getMontant());
            montant = montant.setScale(5);
            BigDecimal cours = new BigDecimal(getCoursMonnaie());
            montant = montant.divide(cours, BigDecimal.ROUND_HALF_DOWN);

            setMontantMonnaie(new FWCurrency(JANumberFormatter.format(montant.toString(), 0.01, 2,
                    JANumberFormatter.NEAR)).toString());
        } else if (!JadeStringUtil.isDecimalEmpty(getMontant()) && JadeStringUtil.isDecimalEmpty(getCoursMonnaie())
                && !JadeStringUtil.isDecimalEmpty(getMontantMonnaie())) {
            BigDecimal montant1 = new BigDecimal(getMontant());
            BigDecimal montant2 = new BigDecimal(getMontantMonnaie());
            BigDecimal cours = montant1.divide(montant2, 5, BigDecimal.ROUND_HALF_EVEN);

            setCoursMonnaie(JANumberFormatter.format(cours.toString(), 0.00001, 5, JANumberFormatter.NEAR));
        } else if (JadeStringUtil.isDecimalEmpty(getMontant()) && !JadeStringUtil.isDecimalEmpty(getCoursMonnaie())
                && !JadeStringUtil.isDecimalEmpty(getMontantMonnaie())) {
            BigDecimal montantMonnaie = new BigDecimal(getMontantMonnaie());
            BigDecimal cours = new BigDecimal(getCoursMonnaie());
            montantMonnaie = montantMonnaie.multiply(cours);

            setMontant(new FWCurrency(JANumberFormatter.format(montantMonnaie.toString(), 0.01, 2,
                    JANumberFormatter.NEAR)).toString());
        }
    }
}
