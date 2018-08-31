package globaz.aquila.db.rdp.cashin.importer;

import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;

public enum CashinTypePaiement {
     CAPITAL_CREANCE(-1),
     INTERETS(-2),
     FRAIS_ANTICHAMBRE(1),
     FRAIS_POURSUITE(2),
     FRAIS_FAILLITE(3),
     FRAIS_CONCORDAT_ANTICHAMBRE(4),
     FRAIS_CONCORDAT_POURSUITE(5),
     FRAIS_CONCORDAT_FAILLITE(6);

    private int value;

    private CashinTypePaiement(int value) {
        this.value = value;
    }

    /**
     * 
     * @return String Code propre à CashIn
     */
    public String getValue() {
        return String.valueOf(value);
    }

    /**
     * Construction de l'énumération à partir d'un code CashIn
     * 
     * @param value
     *            String représentant un code système
     * @return Un type de décompte {@link TypeDecompte}
     */
    public static CashinTypePaiement fromValue(String value) {
        Integer valueAsInt = null;
        try {
            valueAsInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La valeur doit correspondre à un entier représentant un type de paiement CashIn");
        }

        for (CashinTypePaiement t : CashinTypePaiement.values()) {
            if (valueAsInt == t.value) {
                return t;
            }
        }
        throw new IllegalArgumentException("La valeur ne correspond à aucun Type de paiement");
    }
}
