package ch.globaz.perseus.business.constantes;

public enum CSTauxOccupation {
    PLEIN_TEMPS(

    "01"),
    PLUSIEURS_POSTES_TEMPS_PARTIEL(

    "04"),
    POSTE_A_PLEIN_TEMPS_ET_TEMPS_PARTEL(

    "05"),
    POSTE_TEMPS_PATIEL(

    "06");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTauxOccupation getEnumFromCodeSystem(String value) {
        CSTauxOccupation[] allValues = CSTauxOccupation.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTauxOccupation vm = allValues[i];
            if (vm.value.equals(value)) {
                return vm;
            }
        }
        return null;
    }

    private String value;

    private CSTauxOccupation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
