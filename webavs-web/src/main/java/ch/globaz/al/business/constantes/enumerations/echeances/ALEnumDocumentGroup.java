package ch.globaz.al.business.constantes.enumerations.echeances;

import ch.globaz.al.business.exceptions.echeances.ALEcheancesException;
import globaz.jade.exception.JadeApplicationException;

/**
 * 
 * @author ebko
 * 
 */

public enum ALEnumDocumentGroup {
    
    AUCUN("0"),
    AFFILLIE("2"),
    PAYS("3");
    
    public static ALEnumDocumentGroup fromValue(String value) throws JadeApplicationException {
        if (AUCUN.getValue().equals(value)) {
            return AUCUN;
        } else if (AFFILLIE.getValue().equals(value)) {
            return AFFILLIE;
        } else if (PAYS.getValue().equals(value)) {
            return PAYS;
        } else {
            throw new ALEcheancesException("ALEnumDocumentGroup#getFromValue : this type is not supported");
        }
    }
    
    String value;
    
    ALEnumDocumentGroup(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }

}
