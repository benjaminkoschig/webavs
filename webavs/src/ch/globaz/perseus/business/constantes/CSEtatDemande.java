/**
 * 
 */
package ch.globaz.perseus.business.constantes;

/**
 * @author DDE
 * 
 */
public enum CSEtatDemande {

    CALCULE("55008002"),
    ENREGISTRE("55008001"),
    VALIDE("55008003");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSEtatDemande getEnumFromCodeSystem(String codeSystem) {
        CSEtatDemande[] allValues = CSEtatDemande.values();
        for (int i = 0; i < allValues.length; i++) {
            CSEtatDemande vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSEtatDemande(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
