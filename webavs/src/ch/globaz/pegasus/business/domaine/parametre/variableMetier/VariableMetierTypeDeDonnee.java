package ch.globaz.pegasus.business.domaine.parametre.variableMetier;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum VariableMetierTypeDeDonnee implements CodeSystemEnum<VariableMetierTypeDeDonnee> {
    MONTANT("1"),
    TAUX("2"),
    FRACTION("3");

    private String value;

    VariableMetierTypeDeDonnee(String value) {
        this.value = value;
    }

    public static VariableMetierTypeDeDonnee fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, VariableMetierTypeDeDonnee.class);
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean isMontant() {
        return equals(MONTANT);
    }

    public boolean isTaux() {
        return equals(TAUX);
    }

    public boolean isFraction() {
        return equals(FRACTION);
    }
}
