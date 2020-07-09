package globaz.apg.enums;

public enum APPandemieServiceCalcul {
    INDEPENDANT_SANS_MANIFESTATION ("1", APGenreServiceAPG.IndependantPandemie, false),
    INDEPENDANT_AVEC_MANIFESTATION ("2", APGenreServiceAPG.IndependantPandemie, true),
    GARDE_PARENTAL("3", APGenreServiceAPG.GardeParentale, null),
    GARDE_PARENTAL_HANDICAP("4", APGenreServiceAPG.GardeParentaleHandicap, null),
    INDEPENDANT_MANIFESTATION_ANNULEE("5", APGenreServiceAPG.IndependantManifAnnulee, null),
    INDEPENDANT_PERTE_DE_GAIN("6", APGenreServiceAPG.IndependantPerteGains, null),
    SALARIE_EVENEMENTIEL("7", APGenreServiceAPG.SalarieEvenementiel, null);

    String code;
    APGenreServiceAPG genre;
    Boolean avecManifestation;

    private APPandemieServiceCalcul(String code, APGenreServiceAPG genre, Boolean avecManifestation){
        this.code = code;
        this.genre = genre;
        this.avecManifestation = avecManifestation;
    }

    public String getValue() {
        return code;
    }

    public static APPandemieServiceCalcul resolveEnum(String code) {
        for(APPandemieServiceCalcul value: APPandemieServiceCalcul.values()){
            if(value.getValue().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public APGenreServiceAPG getGenre() {
        return genre;
    }

    public Boolean getAvecManifestation() {
        return avecManifestation;
    }
}
