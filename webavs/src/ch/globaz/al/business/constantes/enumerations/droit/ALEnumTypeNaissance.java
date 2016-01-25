package ch.globaz.al.business.constantes.enumerations.droit;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * 
 * @author jts
 * 
 */
public enum ALEnumTypeNaissance {

    ACCE(ALCSDroit.NAISSANCE_TYPE_ACCE),
    AUCUNE(ALCSDroit.NAISSANCE_TYPE_AUCUNE),
    NAIS(ALCSDroit.NAISSANCE_TYPE_NAIS);

    public static ALEnumTypeNaissance getTypeNaissance(String cs) throws JadeApplicationException {
        if (ACCE.getCS().equals(cs)) {
            return ACCE;
        } else if (AUCUNE.getCS().equals(cs)) {
            return AUCUNE;
        } else if (NAIS.getCS().equals(cs)) {
            return NAIS;
        } else {
            throw new ALRafamException("ALEnumTypeNaissance#getTypeNaissance : this type is not supported");
        }
    }

    private String cs;

    ALEnumTypeNaissance(String cs) {
        this.cs = cs;
    }

    public String getCS() {
        return cs;
    }
}
