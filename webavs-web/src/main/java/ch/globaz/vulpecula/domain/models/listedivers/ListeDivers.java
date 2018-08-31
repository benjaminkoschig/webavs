package ch.globaz.vulpecula.domain.models.listedivers;

public class ListeDivers {

    private String noAffilie;
    private String raisonSocial;
    private String dateDebutAffiliation;
    private String dateFinAffiliation;
    private String motifRadiation;
    private String dateDebutParticularite;
    private String dateFinParticularite;
    private String convention;
    private String nbFrancais;
    private String nbAllemand;

    public ListeDivers() {

    }

    public String getDateDebutParticularite() {
        return dateDebutParticularite;
    }

    public String getDateFinParticularite() {
        return dateFinParticularite;
    }

    public void setDateDebutParticularite(String dateDebutParticularite) {
        this.dateDebutParticularite = dateDebutParticularite;
    }

    public void setDateFinParticularite(String dateFinParticularite) {
        this.dateFinParticularite = dateFinParticularite;
    }

    public String getNoAffilie() {
        return noAffilie;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getMotifRadiation() {
        return motifRadiation;
    }

    public void setMotifRadiation(String motifRadiation) {
        this.motifRadiation = motifRadiation;
    }

    public void setNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public void setDateDebutAffiliation(String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public String getConvention() {
        return convention;
    }

    public String getNbFrancais() {
        return nbFrancais;
    }

    public String getNbAllemand() {
        return nbAllemand;
    }

    public void setConvention(String convention) {
        this.convention = convention;
    }

    public void setNbFrancais(String nbFrancais) {
        this.nbFrancais = nbFrancais;
    }

    public void setNbAllemand(String nbAllemand) {
        this.nbAllemand = nbAllemand;
    }

}
