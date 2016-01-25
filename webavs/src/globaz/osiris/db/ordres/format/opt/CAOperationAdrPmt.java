package globaz.osiris.db.ordres.format.opt;

import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.Map;

public class CAOperationAdrPmt extends TIAdressePaiementData {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String categorieSection = ""; // ! n'est pas chargé que pour la liste.

    private String codeIsoMonnaieBonification = "";
    private String codeIsoMonnaieDepot = "";
    private String codeSwift = ""; // ! n'est pas chargé que pour la liste.
    private String dateDebutSection = ""; // ! n'est pas chargé que pour la liste.
    private String dateFinSection = ""; // ! n'est pas chargé que pour la liste.

    private String descriptionCompteAnnexe = ""; // ! n'est pas chargé que pour la liste.
    private String idExterneRole = ""; // ! n'est pas chargé que pour la liste.
    private String idExterneSection = ""; // ! n'est pas chargé que pour la liste.
    private String idPaysBanque = "";
    private String idTypeSection = ""; // ! n'est pas chargé que pour la liste.
    private String montant = "";
    private String motifOrdreVersement = "";

    private String nomBanque1 = ""; // ! n'est pas chargé que pour la liste.

    private String nomBanque2 = ""; // ! n'est pas chargé que pour la liste.
    private String numeroTransaction = "";
    private String numMiseAjour = "";
    private String referenceBvr = "";

    private String roleCompteAnnexe = ""; // ! n'est pas chargé que pour la liste.

    public static CAOperationAdrPmt adapt(Map<String, String> row) {
        CAOperationAdrPmt data = new CAOperationAdrPmt();
        data.setCompte(row.get("ADRPMTCOMPTE"));
        data.setCcp(row.get("ADRPMTCCP"));
        data.setIdTiersBanque(row.get("BQIDTIERS"));
        data.setIdPaysBanque(row.get("BQIDPAYS"));
        data.setClearing(row.get("BQCLEARING"));
        data.setNumMiseAjour(row.get("BQNUMMISEAJOUR"));
        data.setDesignation1_tiers(row.get("BENEDESI1"));
        data.setDesignation2_tiers(row.get("BENEDESI2"));
        data.setDesignation3_tiers(row.get("BENEDESI3"));
        data.setLangue(row.get("BENELANGUE"));
        data.setDesignation1_adr(row.get("ADRLIGNE1"));
        data.setDesignation2_adr(row.get("ADRLIGNE2"));
        data.setRue(row.get("ADRRUE"));
        data.setNumero(row.get("ADRNUMRUE"));
        data.setCasePostale(row.get("ADRCP"));
        data.setNpa(row.get("LOCHJNPA"));
        data.setLocalite(row.get("LOCHJLOCA"));
        data.setPaysIso(row.get("PAYSISO"));
        data.setMontant(row.get("OPMONTANT").replace(',', '.'));
        data.setCodeIsoMonnaieBonification(row.get("OVCODEISOMONBON"));
        data.setCodeIsoMonnaieDepot(row.get("OVCODEISOMONDEP"));
        data.setNumeroTransaction(row.get("OVNUMTRANSACTION"));
        data.setMotifOrdreVersement(row.get("OVMOTIF"));
        data.setReferenceBvr(row.get("OVREFERENCEBVR"));

        // pour la liste uniquement
        data.setIdExterneRole(row.get("IDEXTERNEROLE"));
        data.setRoleCompteAnnexe(row.get("ROLECA"));
        data.setDescriptionCompteAnnexe(row.get("DESCRIPTION"));
        data.setCodeSwift(row.get("SWIFT"));
        data.setNomBanque1(row.get("BQHTLDE1"));
        data.setNomBanque2(row.get("BQHTLDE2"));
        data.setCategorieSection(row.get("CATEGORIESECTION"));

        data.setIdExterneSection(row.get("IDEXTERNESECTION"));
        data.setDateDebutSection(row.get("DATEDEBUTPERIODE"));
        data.setDateFinSection(row.get("DATEFINPERIODE"));
        data.setIdTypeSection(row.get("IDTYPESECTION"));

        return data;
    }

    public String getCategorieSection() {
        return categorieSection;
    }

    public String getCodeIsoMonnaieBonification() {
        return codeIsoMonnaieBonification;
    }

    public String getCodeIsoMonnaieDepot() {
        return codeIsoMonnaieDepot;
    }

    public String getCodeSwift() {
        return codeSwift;
    }

    public String getDateDebutSection() {
        return dateDebutSection;
    }

    public String getDateFinSection() {
        return dateFinSection;
    }

    public String getDescriptionCompteAnnexe() {
        return descriptionCompteAnnexe;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public String getIdExterneSection() {
        return idExterneSection;
    }

    public String getIdPaysBanque() {
        return idPaysBanque;
    }

    public String getIdTypeSection() {
        return idTypeSection;
    }

    public String getMontant() {
        return montant;
    }

    public String getMotifOrdreVersement() {
        return motifOrdreVersement;
    }

    public String getNomBanque1() {
        return nomBanque1;
    }

    public String getNomBanque2() {
        return nomBanque2;
    }

    public String getNumeroTransaction() {
        return numeroTransaction;
    }

    public String getNumMiseAjour() {
        return numMiseAjour;
    }

    /**
     * @return the referenceBvr
     */
    public String getReferenceBvr() {
        return referenceBvr;
    }

    public String getRoleCompteAnnexe() {
        return roleCompteAnnexe;
    }

    public void setCategorieSection(String categorieSection) {
        this.categorieSection = categorieSection;
    }

    public void setCodeIsoMonnaieBonification(String codeIsoMonnaieBonification) {
        this.codeIsoMonnaieBonification = codeIsoMonnaieBonification;
    }

    public void setCodeIsoMonnaieDepot(String codeIsoMonnaieDepot) {
        this.codeIsoMonnaieDepot = codeIsoMonnaieDepot;
    }

    public void setCodeSwift(String codeSwift) {
        this.codeSwift = codeSwift;
    }

    public void setDateDebutSection(String dateDebutSection) {
        this.dateDebutSection = dateDebutSection;
    }

    public void setDateFinSection(String dateFinSection) {
        this.dateFinSection = dateFinSection;
    }

    public void setDescriptionCompteAnnexe(String descriptionCompteAnnexe) {
        this.descriptionCompteAnnexe = descriptionCompteAnnexe;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public void setIdExterneSection(String idExterneSection) {
        this.idExterneSection = idExterneSection;
    }

    public void setIdPaysBanque(String idPaysBanque) {
        this.idPaysBanque = idPaysBanque;
    }

    public void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMotifOrdreVersement(String motifOrdreVersement) {
        this.motifOrdreVersement = motifOrdreVersement;
    }

    public void setNomBanque1(String nomBanque1) {
        this.nomBanque1 = nomBanque1;
    }

    public void setNomBanque2(String nomBanque2) {
        this.nomBanque2 = nomBanque2;
    }

    public void setNumeroTransaction(String numeroTransaction) {
        this.numeroTransaction = numeroTransaction;
    }

    public void setNumMiseAjour(String numMiseAjour) {
        this.numMiseAjour = numMiseAjour;
    }

    /**
     * @param referenceBvr
     *            the referenceBvr to set
     */
    public void setReferenceBvr(String referenceBvr) {
        this.referenceBvr = referenceBvr;
    }

    public void setRoleCompteAnnexe(String roleCompteAnnexe) {
        this.roleCompteAnnexe = roleCompteAnnexe;
    }

}
