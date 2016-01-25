package globaz.lynx.db.facture;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.db.ventilation.LXVentilationViewBean;
import globaz.lynx.utils.LXConstants;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXFacture extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String IDEXTERNEFOURNISSEUR = "IDEXTERNEFOURNISSEUR";
    public static final String IDTIERSFOURNISSEUR = "IDTIERSFOURNISSEUR";

    private String csCodeIsoMonnaie;
    private String csCodeTVA;
    private String csEtat;
    private String csMotifBlocage;
    private String csTypeOperation;
    private String dateEcheance = "";
    private String dateFacture = "";
    private Boolean estBloque = new Boolean(false);
    private Boolean forceEstBloqueUpdate = new Boolean(false);
    private String idAdressePaiement = "0";
    private String idExterne = "";

    private String idExterneFournisseur = "";
    private String idFournisseur;
    // Champs sélectionnés par utilisateurs pour ajout
    private String idJournal;
    private String idOperation = "";
    private String idOperationLiee;
    private String idOperationSrc;
    private String idOrdreGroupe;
    private String idOrganeExecution;
    // Champs lus pour affichage rcListe
    private String idSection = "";
    private String idSociete;
    private String idTiersFournisseur = null;
    private String libelle = "";
    private String montant = "";
    private String motif = "";
    private String nomFournisseur = "";
    private String numeroTransaction;
    private String referenceBVR = "";
    private String referenceExterne = "";

    private String tauxEscompte = "";

    private ArrayList<LXVentilationViewBean> ventilations = new ArrayList<LXVentilationViewBean>();

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;// unused
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdSection(statement.dbReadNumeric(LXSection.FIELD_IDSECTION));
        setIdOperation(statement.dbReadNumeric(LXOperation.FIELD_IDOPERATION));
        setDateFacture(statement.dbReadDateAMJ(LXOperation.FIELD_DATEOPERATION));
        setDateEcheance(statement.dbReadDateAMJ(LXOperation.FIELD_DATEECHEANCE));
        setIdExterne(statement.dbReadString(LXSection.FIELD_IDEXTERNE));
        setIdExterneFournisseur(statement.dbReadString(LXFacture.IDEXTERNEFOURNISSEUR));
        setIdTiersFournisseur(statement.dbReadString(LXFacture.IDTIERSFOURNISSEUR));
        setReferenceExterne(statement.dbReadString(LXOperation.FIELD_REFERENCEEXTERNE));
        setMontant(statement.dbReadNumeric(LXOperation.FIELD_MONTANT, 2));
        setMotif(statement.dbReadString(LXOperation.FIELD_MOTIF));
        setLibelle(statement.dbReadString(LXOperation.FIELD_LIBELLE));
        setCsEtat(statement.dbReadNumeric(LXOperation.FIELD_CSETATOPERATION));
        setEstBloque(statement.dbReadBoolean(LXOperation.FIELD_ESTBLOQUE));
        try {
            // Recuperation du nom et complement sur la table des tiers
            setNomFournisseur(LXUtils.getNomComplet(statement.dbReadString("HTLDE1"), statement.dbReadString("HTLDE2")));
        } catch (Exception e) {
            // nothing
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // unused
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // unused
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // unused
    }

    public String getCsCodeIsoMonnaie() {
        return csCodeIsoMonnaie;
    }

    public String getCsCodeTVA() {
        return csCodeTVA;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getCsEtat() {
        return csEtat;
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

    public String getDateFacture() {
        return dateFacture;
    }

    public Boolean getEstBloque() {
        return estBloque;
    }

    public Boolean getForceEstBloqueUpdate() {
        return forceEstBloqueUpdate;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdExterneFournisseur() {
        return idExterneFournisseur;
    }

    public String getIdFournisseur() {
        return idFournisseur;
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

    public String getIdSociete() {
        return idSociete;
    }

    /**
     * @return the idTiersFournisseur
     */
    public String getIdTiersFournisseur() {
        return idTiersFournisseur;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantFormatted() {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(getMontant()), true, true, false, 2);
    }

    public String getMontantFormattedPositif() {
        String montant = LXUtils.getMontantPositif(getMontant());
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(montant), true, true, false, 2);
    }

    public String getMotif() {
        return motif;
    }

    public String getNomFournisseur() {
        return nomFournisseur;
    }

    public String getNumeroTransaction() {
        return numeroTransaction;
    }

    public String getReferenceBVR() {
        return referenceBVR;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public String getTauxEscompte() {
        return tauxEscompte;
    }

    public ArrayList<LXVentilationViewBean> getVentilations() {
        return ventilations;
    }

    public boolean isCsCodeIsoMonnaieEtranger() {
        return (!JadeStringUtil.isIntegerEmpty(getCsCodeIsoMonnaie()) && !LXConstants.CODE_ISO_CHF
                .equals(getCsCodeIsoMonnaie()));
    }

    public void setCsCodeIsoMonnaie(String csCodeIsoMonnaie) {
        this.csCodeIsoMonnaie = csCodeIsoMonnaie;
    }

    public void setCsCodeTVA(String csCodeTVA) {
        this.csCodeTVA = csCodeTVA;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
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

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    public void setForceEstBloqueUpdate(Boolean forceEstBloqueUpdate) {
        this.forceEstBloqueUpdate = forceEstBloqueUpdate;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdExterneFournisseur(String idExterneFournisseur) {
        this.idExterneFournisseur = idExterneFournisseur;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
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

    public void setIdSection(String idFacture) {
        idSection = idFacture;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    /**
     * @param idTiersFournisseur
     *            the idTiersFournisseur to set
     */
    public void setIdTiersFournisseur(String idTiersFournisseur) {
        this.idTiersFournisseur = idTiersFournisseur;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }

    public void setNumeroTransaction(String numeroTransaction) {
        this.numeroTransaction = numeroTransaction;
    }

    public void setReferenceBVR(String referenceBVR) {
        this.referenceBVR = referenceBVR;
    }

    public void setReferenceExterne(String refFactureFournisseur) {
        referenceExterne = refFactureFournisseur;
    }

    public void setTauxEscompte(String tauxEscompte) {
        this.tauxEscompte = tauxEscompte;
    }

    public void setVentilations(ArrayList<LXVentilationViewBean> ventilations) {
        this.ventilations = ventilations;
    }

}
