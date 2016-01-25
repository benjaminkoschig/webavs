package ch.globaz.aries.business.constantes;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.aries.business.exceptions.AriesTechnicalException;

public enum ARDecisionType {

    DEFINITIVE("960003"),
    DEFINITIVE_RECTIFICATIVE("960005"),
    PROVISOIRE("960002"),

    PROVISOIRE_DEFINITIVE("960006"),

    PROVISOIRE_RECTIFICATIVE("960004");

    private String codeSystem;

    public static ARDecisionType getEnumFromCodeSystem(String codeSystem) {

        for (ARDecisionType type : ARDecisionType.values()) {
            if (type.getCodeSystem().equals(codeSystem)) {
                return type;
            }
        }

        throw new AriesTechnicalException(ARDecisionType.class.getName() + " : no enum founded for codeSystem("
                + codeSystem + ")");

    }

    /**
     * Retourne une liste contenant le type definitive et le type provisoire
     * 
     * @return
     */
    public static List<String> getListTypeNonRectif() {
        List<String> listType = new ArrayList<String>();

        listType.add(ARDecisionType.DEFINITIVE.getCodeSystem());
        listType.add(ARDecisionType.PROVISOIRE.getCodeSystem());

        return listType;
    }

    /**
     * Retourne une liste contenant le type definitive_rectificative, provisoire_rectificative et provisoire_definitive
     * 
     * @return
     */
    public static List<String> getListTypeRectif() {
        List<String> listType = new ArrayList<String>();

        listType.add(ARDecisionType.DEFINITIVE_RECTIFICATIVE.getCodeSystem());
        listType.add(ARDecisionType.PROVISOIRE_RECTIFICATIVE.getCodeSystem());
        listType.add(ARDecisionType.PROVISOIRE_DEFINITIVE.getCodeSystem());

        return listType;
    }

    private ARDecisionType(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public boolean equals(String codeSystem) {
        return this.equals(ARDecisionType.getEnumFromCodeSystem(codeSystem));
    }

    public String getCodeSystem() {
        return codeSystem;
    }
}
