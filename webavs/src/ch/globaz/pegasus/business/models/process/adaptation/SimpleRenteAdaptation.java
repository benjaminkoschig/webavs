package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.pegasus.business.constantes.EPCRenteAdaptation;

public class SimpleRenteAdaptation extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String ancienMontant;
    private String codeInfirmite;
    private String codeRetour;
    private String codeSurvivantInvalide;
    private String csTypdDonneeFinacire;
    private String dateRapport;
    private String debutDroit;
    private String degreInvalidite;
    private EPCRenteAdaptation etat;
    private String fraction;
    private String genre;
    private String idDemandeCentral;
    private String idDonneeFinanciereHeaderOld;
    private String idRenteAdaptation;
    private String nouveauMontant;
    private String nss; // non mapper dans la table
    private String observation;

    public String getAncienMontant() {
        return ancienMontant;
    }

    public String getCodeInfirmite() {
        return codeInfirmite;
    }

    public String getCodeRetour() {
        return codeRetour;
    }

    public String getCodeSurvivantInvalide() {
        return codeSurvivantInvalide;
    }

    public String getCsTypdDonneeFinacire() {
        return csTypdDonneeFinacire;
    }

    public String getDateRapport() {
        return dateRapport;
    }

    public String getDebutDroit() {
        return debutDroit;
    }

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    public EPCRenteAdaptation getEtat() {
        return etat;
    }

    public String getFraction() {
        return fraction;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String getId() {
        return idRenteAdaptation;
    }

    public String getIdDemandeCentral() {
        return idDemandeCentral;
    }

    public String getIdDonneeFinanciereHeaderOld() {
        return idDonneeFinanciereHeaderOld;
    }

    public String getIdRenteAdaptation() {
        return idRenteAdaptation;
    }

    public String getNouveauMontant() {
        return nouveauMontant;
    }

    public String getNss() {
        return nss;
    }

    public String getObservation() {
        return observation;
    }

    public void setAncienMontant(String ancienMontant) {
        this.ancienMontant = ancienMontant;
    }

    public void setCodeInfirmite(String codeInfirmite) {
        this.codeInfirmite = codeInfirmite;
    }

    public void setCodeRetour(String codeRetour) {
        this.codeRetour = codeRetour;
    }

    public void setCodeSurvivantInvalide(String codeSurvivantInvalide) {
        this.codeSurvivantInvalide = codeSurvivantInvalide;
    }

    public void setCsTypdDonneeFinacire(String csTypdDonneeFinacire) {
        this.csTypdDonneeFinacire = csTypdDonneeFinacire;
    }

    public void setDateRapport(String dateRapport) {
        this.dateRapport = dateRapport;
    }

    public void setDebutDroit(String debutDroit) {
        this.debutDroit = debutDroit;
    }

    public void setDegreInvalidite(String degreInvalidite) {
        this.degreInvalidite = degreInvalidite;
    }

    public void setEtat(EPCRenteAdaptation etat) {
        this.etat = etat;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public void setId(String id) {
        idRenteAdaptation = id;

    }

    public void setIdDemandeCentral(String idDemandeCentral) {
        this.idDemandeCentral = idDemandeCentral;
    }

    public void setIdDonneeFinanciereHeaderOld(String idDonneeFinanciereHeaderOld) {
        this.idDonneeFinanciereHeaderOld = idDonneeFinanciereHeaderOld;
    }

    public void setIdRenteAdaptation(String idRenteAdaptation) {
        this.idRenteAdaptation = idRenteAdaptation;
    }

    public void setNouveauMontant(String nouveauMontant) {
        this.nouveauMontant = nouveauMontant;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Override
    public String toString() {
        return "SimpleRenteAdaptation [ancienMontant=" + ancienMontant + ", codeInfirmite=" + codeInfirmite
                + ", codeRetour=" + codeRetour + ", codeSurvivantInvalide=" + codeSurvivantInvalide
                + ", csTypdDonneeFinacire=" + csTypdDonneeFinacire + ", dateRapport=" + dateRapport + ", debutDroit="
                + debutDroit + ", degreInvalidite=" + degreInvalidite + ", etat=" + etat + ", fraction=" + fraction
                + ", genre=" + genre + ", idDemandeCentral=" + idDemandeCentral + ", idDonneeFinanciereHeaderOld="
                + idDonneeFinanciereHeaderOld + ", idRenteAdaptation=" + idRenteAdaptation + ", nouveauMontant="
                + nouveauMontant + ", nss=" + nss + ", observation=" + observation + "]";
    }
}
