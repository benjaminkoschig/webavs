package ch.globaz.perseus.business.constantes;

public enum CSTypeLot {
    LOT_DECISION("55011002", "PF - Décision PCF $0 / Lot $1"),
    LOT_DECISION_RP("55011005", "PF - Décision RP $0 / Lot $1"),
    LOT_FACTURES("55011003", "PF - Facture PCF $0 / Lot $1"),
    LOT_FACTURES_RP("55011006", "PF - Facture RP $0 / Lot $1"),
    LOT_MENSUEL("55011001", "PF - Paiement mensuel PCF $0"),
    LOT_MENSUEL_RP("55011004", "PF - Paiement mensuel RP $0");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTypeLot getEnumFromCodeSystem(String codeSystem) {
        CSTypeLot[] allValues = CSTypeLot.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeLot vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;
    private String description;

    private CSTypeLot(String codeSystem, String description) {
        this.codeSystem = codeSystem;
        this.description = description;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * Retourne la description standard du lot
     * 
     * @param params
     *            [0] Mois au format mm.yyyy, params[1] N° de lot
     * @return the description
     */
    public String getDescription(String[] params) {
        String param0 = "";
        String param1 = "";
        if (params.length > 0) {
            param0 = params[0];
        }
        if (params.length > 1) {
            param1 = params[1];
        }
        String descriptionModifiee = description;
        descriptionModifiee = descriptionModifiee.replace("$0", param0);
        descriptionModifiee = descriptionModifiee.replace("$1", param1);

        return descriptionModifiee;
    }

}
