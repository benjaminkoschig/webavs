package ch.globaz.al.impotsource.domain;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

public enum TypeListeAF {
    TOUS(0),
    LR_RETENUES_PAR_CAF(68025001),
    LR_RETENUES_PAR_CAF_FISC(68025002);

    private int codeSysteme;

    private TypeListeAF(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    public static TypeListeAF fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return TypeListeAF.TOUS;
        }
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (TypeListeAF typeListeAF : TypeListeAF.values()) {
            if (typeListeAF.codeSysteme == intValue) {
                return typeListeAF;
            }
        }

        throw new GlobazTechnicalException(ExceptionMessage.UNKNOWN_VALUE_CODE_SYSTEME);
    }

    public String getValue() {
        return String.valueOf(codeSysteme);
    }

}
