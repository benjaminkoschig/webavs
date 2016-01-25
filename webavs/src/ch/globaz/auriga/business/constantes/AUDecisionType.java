package ch.globaz.auriga.business.constantes;

import java.util.ArrayList;
import java.util.List;

public enum AUDecisionType {

    DEFINITIVE("950003"),

    DEFINITIVE_RECTIFICATIVE("950005"),

    PROVISOIRE("950002"),

    PROVISOIRE_DEFINITIVE("950006"),

    PROVISOIRE_RECTIFICATIVE("950004");

    private String codeSystem;

    public static AUDecisionType getEnumFromCodeSystem(String codeSystem) {

        for (AUDecisionType type : AUDecisionType.values()) {
            if (type.getCodeSystem().equals(codeSystem)) {
                return type;
            }
        }

        return null;

    }

    /**
     * Retourne une liste contenant le type définitive et le type provisoire
     * 
     * @return
     */
    public static List<String> getListTypeNonRectif() {
        List<String> listType = new ArrayList<String>();

        listType.add(AUDecisionType.DEFINITIVE.getCodeSystem());
        listType.add(AUDecisionType.PROVISOIRE.getCodeSystem());

        return listType;
    }

    /**
     * Retourne une liste contenant le type definitive_rectificative, provisoire_rectificative et provisoire_definitive
     * 
     * @return
     */
    public static List<String> getListTypeRectif() {
        List<String> listType = new ArrayList<String>();

        listType.add(AUDecisionType.DEFINITIVE_RECTIFICATIVE.getCodeSystem());
        listType.add(AUDecisionType.PROVISOIRE_RECTIFICATIVE.getCodeSystem());
        listType.add(AUDecisionType.PROVISOIRE_DEFINITIVE.getCodeSystem());

        return listType;
    }

    private AUDecisionType(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public boolean equals(String codeSystem) {
        return this.equals(AUDecisionType.getEnumFromCodeSystem(codeSystem));
    }

    public String getCodeSystem() {
        return codeSystem;
    }
}
