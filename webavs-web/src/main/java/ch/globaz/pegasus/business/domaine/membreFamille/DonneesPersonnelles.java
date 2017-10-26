package ch.globaz.pegasus.business.domaine.membreFamille;

public class DonneesPersonnelles {

    private StatusRefugieApatride statusRefugieApatride;
    private String noCaisseAvs;
    private String idDernierDomicileLegale;
    private Boolean isRepresentantLegal;

    public StatusRefugieApatride getStatusRefugieApatride() {
        return statusRefugieApatride;
    }

    public void setStatusRefugieApatride(StatusRefugieApatride statusRefugieApatride) {
        this.statusRefugieApatride = statusRefugieApatride;
    }

    public String getNoCaisseAvs() {
        return noCaisseAvs;
    }

    public void setNoCaisseAvs(String noCaisseAvs) {
        this.noCaisseAvs = noCaisseAvs;
    }

    public String getIdDernierDomicileLegale() {
        return idDernierDomicileLegale;
    }

    public void setIdDernierDomicileLegale(String idDernierDomicileLegale) {
        this.idDernierDomicileLegale = idDernierDomicileLegale;
    }

    public Boolean getIsRepresentantLegal() {
        return isRepresentantLegal;
    }

    public void setIsRepresentantLegal(Boolean isRepresentantLegal) {
        this.isRepresentantLegal = isRepresentantLegal;
    }

}
