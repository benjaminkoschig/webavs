package ch.globaz.corvus.process.attestationsfiscales;

import globaz.jade.client.util.JadePeriodWrapper;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RERentePourAttestationsFiscales implements Serializable, Comparable<RERentePourAttestationsFiscales> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codePrestation;
    private String dateDebutDroit;
    private String dateDecision;
    private String dateFinDroit;
    private String fractionRente;
    private String idRenteAccordee;
    private String idTiersAdressePaiement;
    private String idTiersBeneficiaire;
    private Boolean isRenteBloquee;
    private String montantPrestation;
    private Map<String, RERetenuePourAttestationsFiscales> retenues;

    public RERentePourAttestationsFiscales() {
        super();

        codePrestation = "";
        dateDebutDroit = "";
        dateDecision = "";
        dateFinDroit = "";
        fractionRente = "";
        idRenteAccordee = "";
        idTiersAdressePaiement = "";
        idTiersBeneficiaire = "";
        isRenteBloquee = Boolean.FALSE;
        montantPrestation = "";
        retenues = new HashMap<String, RERetenuePourAttestationsFiscales>();
    }

    @Override
    public int compareTo(RERentePourAttestationsFiscales uneAutreRente) {

        int compareCodePrestation = codePrestation.compareTo(uneAutreRente.codePrestation);
        if (compareCodePrestation != 0) {
            return compareCodePrestation;
        }

        JadePeriodWrapper periodeDroit = new JadePeriodWrapper(dateDebutDroit, dateFinDroit);
        JadePeriodWrapper periodeDroitAutreRente = new JadePeriodWrapper(uneAutreRente.dateDebutDroit,
                uneAutreRente.dateFinDroit);

        int compareDates = periodeDroit.compareTo(periodeDroitAutreRente);
        if (compareDates != 0) {
            return compareDates;
        }

        return idRenteAccordee.compareTo(uneAutreRente.idRenteAccordee);
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public Boolean getIsRenteBloquee() {
        return isRenteBloquee;
    }

    Map<String, RERetenuePourAttestationsFiscales> getMapRetenues() {
        return retenues;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public Collection<RERetenuePourAttestationsFiscales> getRetenues() {
        return retenues.values();
    }

    public boolean isRenteBloquee() {
        return isRenteBloquee.booleanValue();
    }

    void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    void setIsRenteBloquee(Boolean isRenteBloquee) {
        this.isRenteBloquee = isRenteBloquee;
    }

    void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }
}
