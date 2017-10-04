/**
 * 
 */
package globaz.corvus.db.ventilation.constantes;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

/**
 * @author est
 * 
 */
public enum REVentilationType implements CodeSystemEnum<REVentilationType> {
    PART_CANTONALE("52865001");

    private String csCode;

    REVentilationType(String csCode) {
        this.csCode = csCode;
    }

    public boolean isPartCantonale() {
        return PART_CANTONALE.equals(this);
    }

    public static REVentilationType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, REVentilationType.class);
    }

    @Override
    public String getValue() {
        return csCode;
    }

}
