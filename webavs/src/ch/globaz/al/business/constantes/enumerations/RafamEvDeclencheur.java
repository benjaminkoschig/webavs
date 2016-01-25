package ch.globaz.al.business.constantes.enumerations;

import globaz.jade.exception.JadeApplicationException;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * 
 * @author jts
 * 
 */
public enum RafamEvDeclencheur {
    ANNULATION("61390001"),
    CREATION("61390002"),
    ETAT_REGISTRE("61390007"),
    MODIF_ALLOCATAIRE("61390009"),
    MODIF_DOSSIER("61390011"),
    MODIF_DROIT("61390005"),
    MODIF_ENFANT("61390010"),
    MODIF_ENFANT_DOSSIER_RADIE("61390012"),
    MODIF_ALLOCATAIRE_DOSSIER_RADIE("61390013"),
    NOTICE("61390008"),
    RADIATION("61390004"),
    UPDATE_UPI("61390003");

    /** code système */
    private String cs;

    /**
     * Constructeur
     * 
     * @param cs
     */
    RafamEvDeclencheur(String cs) {
        this.cs = cs;
    }

    public String getCS() {
        return cs;
    }

    public RafamEvDeclencheur getRafamEvDeclencheur(String cs) throws JadeApplicationException {

        if (ANNULATION.cs.equals(cs)) {
            return ANNULATION;
        } else if (CREATION.cs.equals(cs)) {
            return CREATION;
        } else if (ETAT_REGISTRE.cs.equals(cs)) {
            return ETAT_REGISTRE;
        } else if (MODIF_ALLOCATAIRE.cs.equals(cs)) {
            return MODIF_ALLOCATAIRE;
        } else if (MODIF_ALLOCATAIRE_DOSSIER_RADIE.cs.equals(cs)) {
            return MODIF_ALLOCATAIRE_DOSSIER_RADIE;
        } else if (MODIF_DOSSIER.cs.equals(cs)) {
            return MODIF_DOSSIER;
        } else if (MODIF_DROIT.cs.equals(cs)) {
            return MODIF_DROIT;
        } else if (MODIF_ENFANT.cs.equals(cs)) {
            return MODIF_ENFANT;
        } else if (NOTICE.cs.equals(cs)) {
            return NOTICE;
        } else if (RADIATION.cs.equals(cs)) {
            return RADIATION;
        } else if (UPDATE_UPI.cs.equals(cs)) {
            return UPDATE_UPI;
        } else {
            throw new ALRafamException("RafamEvDeclencheur#getRafamEvDeclencheur : this type is not supported : " + cs);
        }

    }
}
