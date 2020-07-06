package globaz.apg.enums;

public enum APPandemieServiceCalcul {
    INDEPENDANT_SANS_MANIFESTATION ("1"),
    INDEPENDANT_AVEC_MANIFESTATION ("2"),
    GARDE_PARENTAL("3"),
    GARDE_PARENTAL_HANDICAP("4"),
    INDEPENDANT_MANIFESTATION_ANNULEE("5"),
    INDEPENDANT_PERTE_DE_GAIN("6");

    String code;

    private APPandemieServiceCalcul(String code){
        this.code = code;
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
}
