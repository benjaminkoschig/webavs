package globaz.apg.api.droits;

/**
 * @author SCR
 */
public interface IAPDroitAPG {

    /** Nom du groupe des codes syst�me des r�vision APG */
    String CS_GROUPE_REVISION_APG = "APNOREVIS";

    String CS_IJ_ASSURANCE_ACCIDENT_OBLIGATOIRE = "52023003";
    String CS_IJ_ASSURANCE_CHOMAGE = "52023004";
    String CS_IJ_ASSURANCE_INVALIDITE = "52023001";
    String CS_IJ_ASSURANCE_MALADIE_OBLIGATOIRE = "52023002";
    String CS_IJ_ASSURANCE_MILITAIRE = "52023005";

    /** R�vision 1999! */
    String CS_REVISION_APG_1999 = "52002002";

    /** R�vision 2005 */
    String CS_REVISION_APG_2005 = "52002003";

    /** Maternit� */
    String CS_MATERNITE = "52002004";

    /**
     * R�vision standard (calcul�e automatiquement par rapport aux dates de service de l'APG)
     */
    String CS_REVISION_STANDARD = "52002001";

    String GROUPE_CS_PROVENANCE_DROIT_ACQUIS = "APPRDROIT";

}
