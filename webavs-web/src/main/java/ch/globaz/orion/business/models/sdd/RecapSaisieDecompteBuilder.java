package ch.globaz.orion.business.models.sdd;

public final class RecapSaisieDecompteBuilder {

    private String numeroAffilie;
    private String nomPrenom;
    private String localite;
    private String periode;
    private String type;
    private String dateSaisie;
    private String erreur;

    public RecapSaisieDecompteBuilder() {
        super();
    }

    public RecapSaisieDecompteBuilder withNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
        return this;
    }

    public RecapSaisieDecompteBuilder withNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
        return this;
    }

    public RecapSaisieDecompteBuilder withLocalite(String localite) {
        this.localite = localite;
        return this;
    }

    public RecapSaisieDecompteBuilder withPeriode(String periode) {
        this.periode = periode;
        return this;
    }

    public RecapSaisieDecompteBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public RecapSaisieDecompteBuilder withDateSaisie(String dateSaisie) {
        this.dateSaisie = dateSaisie;
        return this;
    }

    public RecapSaisieDecompteBuilder withErreur(String erreur) {
        this.erreur = erreur;
        return this;
    }

    public RecapSaisieDecompte build() {
        return new RecapSaisieDecompte(this);
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getLocalite() {
        return localite;
    }

    public String getPeriode() {
        return periode;
    }

    public String getType() {
        return type;
    }

    public String getDateSaisie() {
        return dateSaisie;
    }

    public String getErreur() {
        return erreur;
    }
}