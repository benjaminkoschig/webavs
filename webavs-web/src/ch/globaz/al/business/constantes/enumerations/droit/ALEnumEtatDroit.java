package ch.globaz.al.business.constantes.enumerations.droit;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * 
 * @author jts
 * 
 */
public enum ALEnumEtatDroit {

    A(ALCSDroit.ETAT_A),
    G(ALCSDroit.ETAT_G),
    S(ALCSDroit.ETAT_S);

    public static ALEnumEtatDroit getEtatDroit(String cs) throws JadeApplicationException {
        if (A.getCS().equals(cs)) {
            return A;
        } else if (G.getCS().equals(cs)) {
            return G;
        } else if (S.getCS().equals(cs)) {
            return S;
        } else {
            throw new ALRafamException("ALEnumEtatDroit#getEtatDroit : this type is not supported");
        }
    }

    private String cs;

    ALEnumEtatDroit(String cs) {
        this.cs = cs;
    }

    public String getCS() {
        return cs;
    }
}
