package ch.globaz.vulpecula.domain.models.listepersonnesavecage;

import ch.globaz.common.domaine.Date;

public class ListePersonnesAvecAge {

    private String numTravailleur;
    private String numAff;
    private String numAvs;
    private String nomTiers;
    private String prenomTiers;
    private Date dateNaissanceTiers;
    private String sexeTiers;
    private String codeSexeTiers;
    private String convention;

    public ListePersonnesAvecAge() {

    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    public Date getDateNaissanceTiers() {
        return dateNaissanceTiers;
    }

    public String getSexeTiers() {
        return sexeTiers;
    }

    public String getNumAff() {
        return numAff;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

    public void setDateNaissanceTiers(Date dateNaissanceTiers) {
        this.dateNaissanceTiers = dateNaissanceTiers;
    }

    public void setSexeTiers(String sexeTiers) {
        this.sexeTiers = sexeTiers;
    }

    public void setNumAff(String numAff) {
        this.numAff = numAff;
    }

    public String getConvention() {
        return convention;
    }

    public void setConvention(String convention) {
        this.convention = convention;
    }

    public String getNumTravailleur() {
        return numTravailleur;
    }

    public void setNumTravailleur(String numTravailleur) {
        this.numTravailleur = numTravailleur;
    }

    public String getCodeSexeTiers() {
        return codeSexeTiers;
    }

    public void setCodeSexeTiers(String codeSexeTiers) {
        this.codeSexeTiers = codeSexeTiers;
    }

}
