package ch.globaz.orion.business.models.sdd;

public final class RecapSaisieDecompte {

    private final String numeroAffilie;
    private final String nomPrenom;
    private final String localite;
    private final String periode;
    private final String type;
    private final String dateSaisie;
    private final String erreur;

    public RecapSaisieDecompte(RecapSaisieDecompteBuilder builder) {
        numeroAffilie = builder.getNumeroAffilie();
        nomPrenom = builder.getNomPrenom();
        localite = builder.getLocalite();
        periode = builder.getPeriode();
        type = builder.getType();
        dateSaisie = builder.getDateSaisie();
        erreur = builder.getErreur();
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
