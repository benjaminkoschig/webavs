package ch.globaz.al.business.constantes.enumerations.droit;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * 
 * @author jts
 * 
 */
public enum ALEnumSexe {

    F(ALCSTiers.SEXE_FEMME),
    H(ALCSTiers.SEXE_HOMME);

    public static ALEnumSexe getSexe(String cs) throws JadeApplicationException {
        if (H.getCS().equals(cs)) {
            return H;
        } else if (F.getCS().equals(cs)) {
            return F;
        } else {
            throw new ALRafamException("ALEnumSexe#getSexe : this type is not supported");
        }
    }

    private String cs;

    ALEnumSexe(String cs) {
        this.cs = cs;
    }

    public String getCS() {
        return cs;
    }
}
