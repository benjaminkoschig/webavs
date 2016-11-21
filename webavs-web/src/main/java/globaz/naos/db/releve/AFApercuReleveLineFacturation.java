/*
 * Créé le 18 avr. 05
 */
package globaz.naos.db.releve;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Class utilisée pour enregistré les données d'une cotisation de l'entité Relevé.
 * 
 * @author jts, sau 18 avr. 05 13:49:52
 */
public class AFApercuReleveLineFacturation implements Cloneable {

    // taux caché
    private Boolean afficheTaux = new Boolean(true);
    private String assuranceId = null;
    private String assuranceLibelleAl = null;
    private String assuranceLibelleFr = null;
    private String assuranceLibelleIt = null;
    private String assuranceReferenceId = null;
    private String assuranceRubriqueId = null;
    private String assuranceCanton = null;
    private Boolean blocageEnvoi = new Boolean(false);
    private String cotisationId = null;
    private String debutPeriode = null;
    private String domaineCourrier = new String();
    private String domaineRecouvrement = new String();
    private String domaineRemboursement = new String();
    private String finPeriode = null;
    private double fraction = 0.0;
    private String genreAssurance = null;
    private String idCaisse = null;
    private String idModFacturation = null;
    // plan
    private String idPlan = null;
    private String langue = null;
    private String libelle = null;
    private String libelleInfoRom280 = new String();
    private String libellePlan = new String();
    private double masse = 0.0;
    // masse annuelle pour les affiliés aux acomptes (rectificatifs/compléments)
    private String masseAnnuelle = null;
    private boolean masseVide = false;
    private double montantCalculer = 0.0;
    private String natureRubrique = null;
    // période annuelle (date début et fin non transmise en facturation)
    private boolean periodeFactuAnnuelle = false;
    private double taux = 0.0;
    // genre taux
    private String tauxGenre = null;
    private String tauxType = null;
    private String typeCalcul = null;
    private String typeAssurance = null;
    private String periodiciteCoti = null;

    // *******************************************************
    // Getter
    // *******************************************************

    /*
     * private AFApercuReleveLineFacturation cloneLine() { AFApercuReleveLineFacturation line; line = new
     * AFApercuReleveLineFacturation(); line.setGenreAssurance(getGenreAssurance());
     * line.setAssuranceReferenceId(getAssuranceReferenceId()); line.setMasse(); line.setM
     * line.setTypeTaux(getTypeTaux()); line.setTaux(getTaux()); line.setFraction(getFraction()); line.set line.set
     * line.set line.set line.set line.set return line; }
     */
    public AFApercuReleveLineFacturation cloneLine() throws CloneNotSupportedException {
        return (AFApercuReleveLineFacturation) super.clone();
    }

    public Boolean getAfficheTaux() {
        return afficheTaux;
    }

    public String getAssuranceId() {
        return assuranceId;
    }

    public String getAssuranceLibelle(BSession session) {
        String langue = session.getIdLangueISO();
        if (JACalendar.LANGUAGE_DE.equals(langue)) {
            return getAssuranceLibelleAl();
        } else if (JACalendar.LANGUAGE_IT.equals(langue)) {
            return getAssuranceLibelleIt();
        }
        return getAssuranceLibelleFr();
    }

    public String getAssuranceLibelleAl() {
        return assuranceLibelleAl;
    }

    public String getAssuranceLibelleFr() {
        return assuranceLibelleFr;
    }

    public String getAssuranceLibelleIt() {
        return assuranceLibelleIt;
    }

    public String getAssuranceReferenceId() {
        return assuranceReferenceId;
    }

    public String getAssuranceRubriqueId() {
        return assuranceRubriqueId;
    }

    public Boolean getBlocageEnvoi() {
        return blocageEnvoi;
    }

    public String getCotisationId() {
        return cotisationId;
    }

    public String getDebutPeriode() {
        return debutPeriode;
    }

    public String getDomaineCourrier() {
        return domaineCourrier;
    }

    public String getDomaineRecouvrement() {
        return domaineRecouvrement;
    }

    public String getDomaineRemboursement() {
        return domaineRemboursement;
    }

    public String getFinPeriode() {
        return finPeriode;
    }

    public double getFraction() {
        return fraction;
    }

    public String getGenreAssurance() {
        return genreAssurance;
    }

    /**
     * @return
     */
    public String getIdCaisse() {
        return idCaisse;
    }

    public String getIdModFacturation() {
        return idModFacturation;
    }

    /**
     * @return
     */
    public String getIdPlan() {
        return idPlan;
    }

    public String getLangue() {
        return langue;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getLibelleInfoRom280() {
        return libelleInfoRom280;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    /**
     * @return
     */
    public String getLibellePlan() {
        return libellePlan;
    }

    public double getMasse() {
        return masse;
    }

    public String getMasseAnnuelle() {
        return masseAnnuelle;
    }

    public String getMasseString(boolean wantBlankIfZero) {
        return JANumberFormatter.fmt(Double.toString(masse), true, true, wantBlankIfZero, 2);
    }

    public double getMontantCalculer() {
        return montantCalculer;
    }

    public String getMontantCalculerString() {
        return JANumberFormatter.fmt(Double.toString(montantCalculer), true, true, false, 2);
    }

    public String getNatureRubrique() {
        return natureRubrique;
    }

    public double getTaux() {
        return taux;
    }

    public String getTauxGenre() {
        return tauxGenre;
    }

    public String getTauxString() {
        return JANumberFormatter.fmt(Double.toString(taux), true, true, true, 5);
    }

    public String getTauxType() {
        return tauxType;
    }

    /**
     * @return
     */
    public String getTypeCalcul() {
        return typeCalcul;
    }

    public boolean isMasseVide() {
        return masseVide;
    }

    public boolean isPeriodeFactuAnnuelle() {
        return periodeFactuAnnuelle;
    }

    public void setAfficheTaux(Boolean affiche) {
        afficheTaux = affiche;
    }

    public void setAssuranceId(String string) {
        assuranceId = string;
    }

    public void setAssuranceLibelleAl(String string) {
        assuranceLibelleAl = string;
    }

    public void setAssuranceLibelleFr(String string) {
        assuranceLibelleFr = string;
    }

    public void setAssuranceLibelleIt(String string) {
        assuranceLibelleIt = string;
    }

    public void setAssuranceReferenceId(String string) {
        assuranceReferenceId = string;
    }

    public void setAssuranceRubriqueId(String string) {
        assuranceRubriqueId = string;
    }

    public void setBlocageEnvoi(Boolean blocageEnvoi) {
        this.blocageEnvoi = blocageEnvoi;
    }

    public void setCotisationId(String string) {
        cotisationId = string;
    }

    public void setDebutPeriode(String string) {
        debutPeriode = string;
    }

    public void setDomaineCourrier(String domaineCourrier) {
        this.domaineCourrier = domaineCourrier;
    }

    public void setDomaineRecouvrement(String domaineRecouvrement) {
        this.domaineRecouvrement = domaineRecouvrement;
    }

    public void setDomaineRemboursement(String domaineRemboursement) {
        this.domaineRemboursement = domaineRemboursement;
    }

    public void setFinPeriode(String string) {
        finPeriode = string;
    }

    public void setFraction(double d) {
        fraction = d;
    }

    public void setGenreAssurance(String string) {
        genreAssurance = string;
    }

    /**
     * @param string
     */

    public void setIdCaisse(String string) {
        idCaisse = string;
    }

    public void setIdModFacturation(String string) {
        idModFacturation = string;
    }

    /**
     * @param string
     */
    public void setIdPlan(String string) {
        idPlan = string;
    }

    public void setLangue(String string) {
        langue = string;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setLibelleInfoRom280(String newLibelleInfoRom280) {
        libelleInfoRom280 = newLibelleInfoRom280;
    }

    /**
     * @param string
     */
    public void setLibellePlan(String string) {
        libellePlan = string;
    }

    public void setMasse(double d) {
        masse = d;
    }

    public void setMasse(String string) {
        if (!JadeStringUtil.isEmpty(string)) {
            masse = Double.parseDouble(JANumberFormatter.deQuote(string));
        } else {
            masse = 0.0;
        }
    };

    public void setMasseAnnuelle(String masseAnnuelle) {
        this.masseAnnuelle = masseAnnuelle;
    }

    public void setMasseVide(boolean b) {
        masseVide = b;
    }

    public void setMontantCalculer(double d) {
        montantCalculer = d;
    }

    public void setMontantCalculer(String string) {
        this.setMontantCalculer(string, true);
    }

    public void setMontantCalculer(String string, boolean arrondi) {
        if (!JadeStringUtil.isEmpty(string)) {
            montantCalculer = Double.parseDouble(JANumberFormatter.deQuote(string));
            if (arrondi) {
                montantCalculer = JANumberFormatter.round(montantCalculer, 0.05, 2, JANumberFormatter.NEAR);
            }
        } else {
            montantCalculer = 0.0;
        }
    }

    public void setNatureRubrique(String string) {
        natureRubrique = string;
    }

    public void setPeriodeFactuAnnuelle(boolean periodeFactuAnnuelle) {
        this.periodeFactuAnnuelle = periodeFactuAnnuelle;
    }

    public void setTaux(double d) {
        taux = d;
    }

    public void setTaux(String string) {
        if (!JadeStringUtil.isEmpty(string)) {
            taux = Double.parseDouble(JANumberFormatter.deQuote(string));
        } else {
            taux = 0.0;
        }
    }

    public void setTauxGenre(String tauxGenre) {
        this.tauxGenre = tauxGenre;
    }

    public void setTauxType(String tauxType) {
        this.tauxType = tauxType;
    }

    /**
     * @param string
     */
    public void setTypeCalcul(String string) {
        typeCalcul = string;
    }

    public String getTypeAssurance() {
        return typeAssurance;
    }

    public void setTypeAssurance(String typeAssurance) {
        this.typeAssurance = typeAssurance;
    }

    public String getAssuranceCanton() {
        return assuranceCanton;
    }

    public void setAssuranceCanton(String assuranceCanton) {
        this.assuranceCanton = assuranceCanton;
    }

    public String getPeriodiciteCoti() {
        return periodiciteCoti;
    }

    public void setPeriodiciteCoti(String periodiciteCoti) {
        this.periodiciteCoti = periodiciteCoti;
    }

}
