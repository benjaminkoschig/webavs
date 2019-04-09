package ch.globaz.al.business.constantes.enumerations;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * 
 * @author jts
 * 
 */
public enum RafamEtatAnnonce {
    A_TRANSMETTRE("61370006"),
    ENREGISTRE("61370001"),
    RECU("61370003"),
    SUSPENDU("61370005"),
    TRANSMIS("61370002"),
    VALIDE("61370004"),
    ARCHIVE("61370007");

    public static RafamEtatAnnonce getRafamEtatAnnonceCS(String cs) throws JadeApplicationException {
        if (ENREGISTRE.getCS().equals(cs)) {
            return ENREGISTRE;
        } else if (A_TRANSMETTRE.getCS().equals(cs)) {
            return A_TRANSMETTRE;
        } else if (TRANSMIS.getCS().equals(cs)) {
            return TRANSMIS;
        } else if (RECU.getCS().equals(cs)) {
            return RECU;
        } else if (SUSPENDU.getCS().equals(cs)) {
            return SUSPENDU;
        } else if (VALIDE.getCS().equals(cs)) {
            return VALIDE;
        } else if(ARCHIVE.getCS().equals(cs)) {
            return ARCHIVE;
        }else{
            throw new ALRafamException("RafamEtatAnnonce#getRafamEtatAnnonceCS : this type is not supported");
        }
    }

    private String cs;

    RafamEtatAnnonce(String cs) {
        this.cs = cs;
    }

    public String getCS() {
        return String.valueOf(cs);
    }
}
