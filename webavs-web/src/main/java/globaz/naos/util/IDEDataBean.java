package globaz.naos.util;

public class IDEDataBean {
    private String numeroIDE = "";
    private String numeroIDERemplacement = "";
    private String raisonSociale = "";
    private String statut = "";
    private String adresse = "";
    private String canton = "";
    private String langue = "";
    private String localite = "";
    private String npa = "";
    private String rue = "";
    private String numeroRue = "";
    private String motifFin = "";
    private String personnaliteJuridique = "";
    private String brancheEconomique = "";
    private String typeAnnonceIde = "";
    private String careOf = "";
    // D0181
    private String naissance = "";
    private String activite = "";
    private String numeroAffilie = "";
    /**
     * code noga selon le registre != code noga dans l'affiliation
     */
    private String nogaCode = "";

    public String getNumeroIDERemplacement() {
        return numeroIDERemplacement;
    }

    public void setNumeroIDERemplacement(String numeroIDERemplacement) {
        this.numeroIDERemplacement = numeroIDERemplacement;
    }

    public String getTypeAnnonceIde() {
        return typeAnnonceIde;
    }

    public void setTypeAnnonceIde(String typeAnnonceIde) {
        this.typeAnnonceIde = typeAnnonceIde;
    }

    public String getPersonnaliteJuridique() {
        return personnaliteJuridique;
    }

    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    public void setBrancheEconomique(String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getMotifFin() {
        return motifFin;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getNumeroRue() {
        return numeroRue;
    }

    public void setNumeroRue(String numeroRue) {
        this.numeroRue = numeroRue;
    }

    /**
     * Care of / à l'attention de
     * 
     * @return
     */
    public String getCareOf() {
        return careOf;
    }

    public void setCareOf(String careOf) {
        this.careOf = careOf;
    }

    public String getNaissance() {
        return naissance;
    }

    public void setNaissance(String naissance) {
        this.naissance = naissance;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getNogaCode() {
        return nogaCode;
    }

    public void setNogaCode(String nogaCode) {
        this.nogaCode = nogaCode;
    }

    public String getNumeroIDE() {
        return numeroIDE;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public String getStatut() {
        return statut;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setNumeroIDE(String numeroIDE) {
        this.numeroIDE = numeroIDE;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    public void setPersonnaliteJuridique(String personnaliteJuridique) {
        this.personnaliteJuridique = personnaliteJuridique;
    }

}