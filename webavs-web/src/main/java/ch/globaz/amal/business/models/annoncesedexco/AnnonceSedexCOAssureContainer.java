/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import ch.globaz.common.domaine.Date;

/**
 * @author LBE
 *         Class utilisé pour le affichage des données
 */
public class AnnonceSedexCOAssureContainer {
    private String formattedNss;
    private String formattedName;
    private String formattedNameReprise;

    private Date daDebutPrime;
    private Date daFinPrime;
    private Date daDebutParticipation;
    private Date daFinParticipation;
    private String montantPrime;
    private String montantParticipation;

    /**
     * Default constructor
     */
    public AnnonceSedexCOAssureContainer() {
        formattedNss = "";
        formattedName = "";
        formattedNameReprise = "";
        montantPrime = "";
        montantParticipation = "";
    }

    /**
     * @return the formattedNss
     */
    public String getFormattedNss() {
        return formattedNss;
    }

    /**
     * @param formattedNss the formattedNss to set
     */
    public void setFormattedNss(String formattedNss) {
        this.formattedNss = formattedNss;
    }

    /**
     * @return the formattedName
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * @param formattedName the formattedName to set
     */
    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
    }

    /**
     * @return the formattedNameReprise
     */
    public String getFormattedNameReprise() {
        return formattedNameReprise;
    }

    /**
     * @param formattedNameReprise the formattedNameReprise to set
     */
    public void setFormattedNameReprise(String formattedNameReprise) {
        this.formattedNameReprise = formattedNameReprise;
    }

    /**
     * @return the daDebutPrime
     */
    public Date getDaDebutPrime() {
        return daDebutPrime;
    }

    /**
     * @param daDebutPrime the daDebutPrime to set
     */
    public void setDaDebutPrime(Date daDebutPrime) {
        this.daDebutPrime = daDebutPrime;
    }

    /**
     * @return the daFinPrime
     */
    public Date getDaFinPrime() {
        return daFinPrime;
    }

    /**
     * @param daFinPrime the daFinPrime to set
     */
    public void setDaFinPrime(Date daFinPrime) {
        this.daFinPrime = daFinPrime;
    }

    /**
     * @return the periodPrime
     */
    public String getPeriodPrime() {
        String result = "";
        if (daDebutPrime != null) {
            result = daDebutPrime.getSwissValue() + " - ";
            if (daFinPrime != null) {
                result += daFinPrime.getSwissValue();
            }
        }
        return result;
    }

    /**
     * @return the daDebutParticipation
     */
    public Date getDaDebutParticipation() {
        return daDebutParticipation;
    }

    /**
     * @param daDebutParticipation the daDebutParticipation to set
     */
    public void setDaDebutParticipation(Date daDebutParticipation) {
        this.daDebutParticipation = daDebutParticipation;
    }

    /**
     * @return the daFinParticipation
     */
    public Date getDaFinParticipation() {
        return daFinParticipation;
    }

    /**
     * @param daFinParticipation the daFinParticipation to set
     */
    public void setDaFinParticipation(Date daFinParticipation) {
        this.daFinParticipation = daFinParticipation;
    }

    /**
     * @return the periodParticipation
     */
    public String getPeriodParticipation() {
        String result = "";
        if (daDebutParticipation != null) {
            result = daDebutParticipation.getSwissValue() + " - ";
            if (daFinParticipation != null) {
                result += daFinParticipation.getSwissValue();
            }
        }
        return result;
    }

    /**
     * @return the montantPrime
     */
    public String getMontantPrime() {
        return montantPrime;
    }

    /**
     * @param montantPrime the montantPrime to set
     */
    public void setMontantPrime(String montantPrime) {
        this.montantPrime = montantPrime;
    }

    /**
     * @return the montantParticipation
     */
    public String getMontantParticipation() {
        return montantParticipation;
    }

    /**
     * @param montantParticipation the montantParticipation to set
     */
    public void setMontantParticipation(String montantParticipation) {
        this.montantParticipation = montantParticipation;
    }

}
