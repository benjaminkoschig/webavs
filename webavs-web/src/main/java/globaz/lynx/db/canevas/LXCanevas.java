package globaz.lynx.db.canevas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.utils.LXConstants;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXCanevas extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String IDEXTERNEFOURNISSEUR = "IDEXTERNEFOURNISSEUR";

    private String csCodeIsoMonnaie = "";
    private String csCodeTVA = "";
    private String csTypeOperation = "";
    private String idAdressePaiement = "0";
    private String idExterne = "";
    private String idExterneFournisseur = "";
    private String idFournisseur = "";
    private String idOperationCanevas = "";

    private String idOrganeExecution = "";
    // Champs lus pour affichage rcListe
    private String idSectionCanevas = "";
    // Champs sélectionnés par utilisateurs pour ajout
    private String idSociete = "";
    private String libelle = "";
    private String montant = "";
    private String motif = "";
    private String nomFournisseur = "";
    private String referenceBVR = "";
    private String referenceExterne = "";
    private String tauxEscompte = "";

    private ArrayList<LXCanevasVentilationViewBean> ventilations = new ArrayList<LXCanevasVentilationViewBean>();

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
        setIdSectionCanevas(statement.dbReadNumeric(LXCanevasSection.FIELD_IDSECTIONCANEVAS));
        setIdFournisseur(statement.dbReadNumeric(LXCanevasSection.FIELD_IDFOURNISSEUR));
        setIdOperationCanevas(statement.dbReadNumeric(LXCanevasOperation.FIELD_IDOPERATIONCANEVAS));
        setIdExterne(statement.dbReadString(LXCanevasSection.FIELD_IDEXTERNE));
        setIdExterneFournisseur(statement.dbReadString(LXCanevas.IDEXTERNEFOURNISSEUR));
        setIdSociete(statement.dbReadNumeric(LXSection.FIELD_IDSOCIETE));
        setReferenceExterne(statement.dbReadString(LXCanevasOperation.FIELD_REFERENCEEXTERNE));
        setMontant(statement.dbReadNumeric(LXCanevasOperation.FIELD_MONTANT, 2));
        setMotif(statement.dbReadString(LXCanevasOperation.FIELD_MOTIF));
        setLibelle(statement.dbReadString(LXCanevasOperation.FIELD_LIBELLE));
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

    // *******************************************************
    // Getter
    // *******************************************************

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

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdExterneFournisseur() {
        return idExterneFournisseur;
    }

    public String getIdFournisseur() {
        return idFournisseur;
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

    public String getIdSociete() {
        return idSociete;
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

    public String getMotif() {
        return motif;
    }

    public String getNomFournisseur() {
        return nomFournisseur;
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

    public ArrayList<LXCanevasVentilationViewBean> getVentilations() {
        return ventilations;
    }

    // *******************************************************
    // Setter
    // *******************************************************

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

    public void setCsTypeOperation(String csTypeOperation) {
        this.csTypeOperation = csTypeOperation;
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

    public void setIdOperationCanevas(String idOperationCanevas) {
        this.idOperationCanevas = idOperationCanevas;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setIdSectionCanevas(String idSectionCanevas) {
        this.idSectionCanevas = idSectionCanevas;
    }

    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
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

    public void setReferenceBVR(String referenceBVR) {
        this.referenceBVR = referenceBVR;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public void setTauxEscompte(String tauxEscompte) {
        this.tauxEscompte = tauxEscompte;
    }

    public void setVentilations(ArrayList<LXCanevasVentilationViewBean> ventilations) {
        this.ventilations = ventilations;
    }

}
