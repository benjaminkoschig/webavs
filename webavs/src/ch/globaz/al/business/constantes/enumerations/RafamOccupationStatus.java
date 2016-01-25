package ch.globaz.al.business.constantes.enumerations;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * 
 * @author jts
 * 
 */
public enum RafamOccupationStatus {
    AGRICULTEUR("05", "61330005"),
    BENEFICIAIRE_IJAI("08", "61330008"),
    CHOMEUR("04", "61330004"),
    COLLAB_AGRICOLE("06", "61330006"),
    INDEPENDANT("02", "61330002"),
    NON_ACTIF("03", "61330003"),
    SALARIE("01", "61330001"),
    TRAVAILLEUR_AGRICOLE("07", "61330007"),
    TSE("09", "61330009");

    /**
     * Retourne une instance de l'enum en fonction du statut d'occupation
     * 
     */
    public static RafamOccupationStatus getOccupationStatus(String code) throws JadeApplicationException {

        switch (Integer.parseInt(code)) {
            case 1:
                return SALARIE;
            case 2:
                return INDEPENDANT;
            case 3:
                return NON_ACTIF;
            case 4:
                return CHOMEUR;
            case 5:
                return AGRICULTEUR;
            case 6:
                return COLLAB_AGRICOLE;
            case 7:
                return TRAVAILLEUR_AGRICOLE;
            case 8:
                return BENEFICIAIRE_IJAI;
            case 9:
                return TSE;

            default:
                throw new ALRafamException("RafamOccupationStatus#getOccupationStatus : this type is not supported");
        }
    }

    public static RafamOccupationStatus getOccupationStatusCS(String cs) throws JadeApplicationException {

        if (AGRICULTEUR.getCS().equals(cs) || ALCSDossier.ACTIVITE_AGRICULTEUR.equals(cs)
                || ALCSDossier.ACTIVITE_PECHEUR.equals(cs) || ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE.equals(cs)) {
            return AGRICULTEUR;
        } else if (SALARIE.getCS().equals(cs) || ALCSDossier.ACTIVITE_SALARIE.equals(cs)) {
            return SALARIE;
        } else if (COLLAB_AGRICOLE.getCS().equals(cs) || ALCSDossier.ACTIVITE_COLLAB_AGRICOLE.equals(cs)) {
            return COLLAB_AGRICOLE;
        } else if (NON_ACTIF.getCS().equals(cs) || ALCSDossier.ACTIVITE_NONACTIF.equals(cs)) {
            return NON_ACTIF;
        } else if (TRAVAILLEUR_AGRICOLE.getCS().equals(cs) || ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE.equals(cs)) {
            return TRAVAILLEUR_AGRICOLE;
        } else if (INDEPENDANT.getCS().equals(cs) || ALCSDossier.ACTIVITE_INDEPENDANT.equals(cs)) {
            return INDEPENDANT;
        } else if (TSE.getCS().equals(cs) || ALCSDossier.ACTIVITE_TSE.equals(cs)) {
            return TSE;
        } else {
            throw new ALRafamException("RafamOccupationStatus#getOccupationStatusForCS : this type is not supported");
        }
    }

    /** Code de la centrale */
    private String codeCentrale;

    /** Code système du type de prestation (RAFam) dans WebAF */
    private String cs;

    RafamOccupationStatus(String codeCentrale, String cs) {
        this.cs = cs;
        this.codeCentrale = codeCentrale;
    }

    public String getCodeCentrale() {
        return codeCentrale;
    }

    public String getCS() {
        return cs;
    }
}