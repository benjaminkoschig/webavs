package ch.globaz.pegasus.business.models.creancier;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleCreancierHystorique extends JadeSimpleModel {

    private static final long serialVersionUID = 1L;
    private String id = null;
    /**
     * Créancier
     */
    private String csEtat = null;
    private String csTypeCreance = null;
    private String idAffilieAdressePaiment = null;
    private String idCreancier = null;
    private String idDemande = null;
    private String idDomaineApplicatif = null;
    private String idTiers = null;
    private String idTiersAdressePaiement = null;
    private String idTiersRegroupement = null;
    private Boolean isBloque = false;
    private String montantCreancier = null;
    private String referencePaiement = null;
    private Boolean isCalcule = false;
    private Boolean isHome = false;
    private String pspyCreancier = "";
    private String cspyCreancier = "";
    /**
     * Créance accordée
     */
    private String idCreanceAccordee = null;
    private String idOrdreVersement = null;
    private String idPCAccordee = null;
    private String montantCreancieAccordee = null;
    private String pspyCreanceAccordee  = "";
    private String cspyCreanceAccordee  = "";
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
    public String getCsEtat() {
        return csEtat;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public String getCsTypeCreance() {
        return csTypeCreance;
    }

    public void setCsTypeCreance(String csTypeCreance) {
        this.csTypeCreance = csTypeCreance;
    }

    public String getIdAffilieAdressePaiment() {
        return idAffilieAdressePaiment;
    }

    public void setIdAffilieAdressePaiment(String idAffilieAdressePaiment) {
        this.idAffilieAdressePaiment = idAffilieAdressePaiment;
    }

    public String getIdCreancier() {
        return idCreancier;
    }

    public void setIdCreancier(String idCreancier) {
        this.idCreancier = idCreancier;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getIdDomaineApplicatif() {
        return idDomaineApplicatif;
    }

    public void setIdDomaineApplicatif(String idDomaineApplicatif) {
        this.idDomaineApplicatif = idDomaineApplicatif;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public String getIdTiersRegroupement() {
        return idTiersRegroupement;
    }

    public void setIdTiersRegroupement(String idTiersRegroupement) {
        this.idTiersRegroupement = idTiersRegroupement;
    }

    public Boolean getIsBloque() {
        return isBloque;
    }

    public void setIsBloque(Boolean bloque) {
        isBloque = bloque;
    }

    public String getMontantCreancier() {
        return montantCreancier;
    }

    public void setMontantCreancier(String montantCreancier) {
        this.montantCreancier = montantCreancier;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public Boolean getIsCalcule() {
        return isCalcule;
    }

    public void setIsCalcule(Boolean calcule) {
        isCalcule = calcule;
    }

    public String getIdCreanceAccordee() {
        return idCreanceAccordee;
    }

    public void setIdCreanceAccordee(String idCreanceAccordee) {
        this.idCreanceAccordee = idCreanceAccordee;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public String getMontantCreancieAccordee() {
        return montantCreancieAccordee;
    }

    public void setMontantCreancieAccordee(String montantCreancieAccordee) {
        this.montantCreancieAccordee = montantCreancieAccordee;
    }

    public String getPspyCreancier() {
        return pspyCreancier;
    }

    public void setPspyCreancier(String pspyCreancier) {
        this.pspyCreancier = pspyCreancier;
    }

    public String getCspyCreancier() {
        return cspyCreancier;
    }

    public void setCspyCreancier(String cspyCreancier) {
        this.cspyCreancier = cspyCreancier;
    }

    public String getPspyCreanceAccordee() {
        return pspyCreanceAccordee;
    }

    public void setPspyCreanceAccordee(String pspyCreanceAccordee) {
        this.pspyCreanceAccordee = pspyCreanceAccordee;
    }

    public String getCspyCreanceAccordee() {
        return cspyCreanceAccordee;
    }

    public void setCspyCreanceAccordee(String cspyCreanceAccordee) {
        this.cspyCreanceAccordee = cspyCreanceAccordee;
    }
    public Boolean getIsHome() {
        return isHome;
    }

    public void setIsHome(Boolean home) {
        isHome = home;
    }
}
