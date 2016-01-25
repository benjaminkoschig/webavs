package ch.globaz.pegasus.business.models.recap;

public enum RecapCategorie {

    ADAPTATION("_ADAPTATION", false),
    FUTUR("_FUTUR", true),
    NORMAL("", false);

    private String codeForListe = null;
    private Boolean isFutur = null;

    RecapCategorie(String codeForListe, Boolean isFutur) {
        this.codeForListe = codeForListe;
        this.isFutur = isFutur;
    }

    public String getCodeForListe() {
        return codeForListe;
    }

    public Boolean isFutur() {
        return isFutur;
    }

}
