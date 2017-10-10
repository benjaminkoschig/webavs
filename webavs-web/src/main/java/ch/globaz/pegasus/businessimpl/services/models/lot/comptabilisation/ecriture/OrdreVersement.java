package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;

public class OrdreVersement {
    private String csType = null;

    private String csTypeDomaine = null;
    private String id = null;
    private String idDomaineApplication = null;
    private String idDomaineApplicationConjoint;
    private String idSectionDetteEnCompta = null;
    private String idTiers = null;
    private String idTiersAdressePaiement = null;
    private String idTiersAdressePaiementConjoint = null;
    private String idTiersConjoint = null;
    private String idTiersOwnerDetteCreance = null;
    private BigDecimal montant = null;
    private String sousTypeGenrePrestation = null;
    private String refPaiement;
    private boolean isPartCantonale;

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

    public OrdreVersement() {

    }

    public OrdreVersement(String id, String csType, String csTypeDomaine, String idSectionDetteEnCompta,
            String idTiers, String idTiersAdressePaiement, String idTiersAdressePaiementConjoint,
            String idTiersOwnerDetteCreance, String montant, String sousTypeGenrePrestation,
            String idDomaineApplication, String idDomaineApplicationConjoint, String idTiersConjoint, String refPaiement) {

        this(id, csType, csTypeDomaine, idSectionDetteEnCompta, idTiers, idTiersAdressePaiement,
                idTiersAdressePaiementConjoint, idTiersOwnerDetteCreance, montant, sousTypeGenrePrestation,
                idDomaineApplication, idDomaineApplicationConjoint, idTiersConjoint, refPaiement, false);
    }

    public OrdreVersement(String id, String csType, String csTypeDomaine, String idSectionDetteEnCompta,
            String idTiers, String idTiersAdressePaiement, String idTiersAdressePaiementConjoint,
            String idTiersOwnerDetteCreance, String montant, String sousTypeGenrePrestation,
            String idDomaineApplication, String idDomaineApplicationConjoint, String idTiersConjoint,
            String refPaiement, boolean isPartCantonale) {
        super();
        this.csType = csType;
        this.csTypeDomaine = csTypeDomaine;
        this.idSectionDetteEnCompta = idSectionDetteEnCompta;
        this.idTiers = idTiers;
        this.idTiersAdressePaiement = idTiersAdressePaiement;
        this.idTiersAdressePaiementConjoint = idTiersAdressePaiementConjoint;
        this.idTiersOwnerDetteCreance = idTiersOwnerDetteCreance;
        this.montant = new BigDecimal(montant);
        this.sousTypeGenrePrestation = sousTypeGenrePrestation;
        this.id = id;
        this.idDomaineApplication = idDomaineApplication;
        this.idDomaineApplicationConjoint = idDomaineApplicationConjoint;
        this.idTiersConjoint = idTiersConjoint;
        this.refPaiement = refPaiement;
        this.isPartCantonale = isPartCantonale;

    }

    public String getCsType() {
        return csType;
    }

    public String getCsTypeDomaine() {
        return csTypeDomaine;
    }

    public String getId() {
        return id;
    }

    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    public String getIdDomaineApplicationConjoint() {
        return idDomaineApplicationConjoint;
    }

    public String getIdSectionDetteEnCompta() {
        return idSectionDetteEnCompta;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdTiersAdressePaiementConjoint() {
        return idTiersAdressePaiementConjoint;
    }

    public String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    public String getIdTiersOwnerDetteCreance() {
        return idTiersOwnerDetteCreance;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public String getSousTypeGenrePrestation() {
        return sousTypeGenrePrestation;
    }

    public boolean isPartCantonale() {
        return isPartCantonale;
    }

    public boolean isDom2R() {
        return !(JadeStringUtil.isBlankOrZero(getIdTiersAdressePaiementConjoint()));
    }

    public boolean isPositif() {
        return montant.signum() == 1;
    }

    public void setCsType(String csType) {
        this.csType = csType;
    }

    public void setCsTypeDomaine(String csTypeDomaine) {
        this.csTypeDomaine = csTypeDomaine;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    public void setIdDomaineApplicationConjoint(String idDomaineApplicationConjoint) {
        this.idDomaineApplicationConjoint = idDomaineApplicationConjoint;
    }

    public void setIdSectionDetteEnCompta(String idSectionDetteEnCompta) {
        this.idSectionDetteEnCompta = idSectionDetteEnCompta;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiementConjoint(String idTiersAdressePaiementConjoint) {
        this.idTiersAdressePaiementConjoint = idTiersAdressePaiementConjoint;
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    public void setIdTiersOwnerDetteCreance(String idTiersOwnerDetteCreance) {
        this.idTiersOwnerDetteCreance = idTiersOwnerDetteCreance;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public void setSousTypeGenrePrestation(String sousTypeGenrePrestation) {
        this.sousTypeGenrePrestation = sousTypeGenrePrestation;
    }

    public void setPartCantonale(boolean isPartCantonale) {
        this.isPartCantonale = isPartCantonale;
    }

    @Override
    public String toString() {
        return "OrdreVersement [csType=" + csType + ", csTypeDomaine=" + csTypeDomaine + ", id=" + id
                + ", idDomaineApplication=" + idDomaineApplication + ", idSectionDetteEnCompta="
                + idSectionDetteEnCompta + ", idTiers=" + idTiers + ", idTiersAdressePaiement="
                + idTiersAdressePaiement + ", idTiersAdressePaiementConjoint=" + idTiersAdressePaiementConjoint
                + ", idTiersOwnerDetteCreance=" + idTiersOwnerDetteCreance + ", montant=" + montant
                + ", sousTypeGenrePrestation=" + sousTypeGenrePrestation + "]";
    }
}
