/**
 * 
 */
package ch.globaz.perseus.business.constantes;

/**
 * @author DDE
 * 
 */
public enum CSEtatRentePont {

    ANNULE("55029003"),
    ENREGISTRE("55029001"),
    VALIDE("55029002");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSEtatRentePont getEnumFromCodeSystem(String codeSystem) {
        CSEtatRentePont[] allValues = CSEtatRentePont.values();
        for (int i = 0; i < allValues.length; i++) {
            CSEtatRentePont vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSEtatRentePont(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
