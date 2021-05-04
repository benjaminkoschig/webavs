package globaz.apg.api.annonces;

/**
 * Descpription
 * 
 * @author scr Date de création 25 mai 05
 */
public interface IAPAnnonce {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     */
    public static final String CS_APGREVISION1999 = "52012002";

    /**
     */
    public static final String CS_APGREVISION2005 = "52012003";

    public static final String CS_APGSEDEX = "52012004";

    /** Contenu_d_une_annonce_APG */
    public static final String CS_DEMANDE_ALLOCATION = "52000001";

    /**
     */
    public static final String CS_DUPLICATA = "52000002";

    /**
     */
    public static final String CS_ENVOYE = "52013003";

    /**
     */
    public static final String CS_ERRONE = "52013004";

    /** Nom du groupe des codes système des révision APG */
    public static final String CS_GROUPE_CONTENU_ANNONCES_APG = "APANNONCON";

    /**
     */
    public static final String CS_GROUPE_ETAT_ANNONCE = "APETATANNO";

    /**
     */
    public static final String CS_GROUPE_TYPE_ANNONCE = "APTYPEANNO";

    /**
     */
    public static final String CS_MATERNITE = "52012001";

    public static final String CS_PATERNITE = "52012005";

    public static final String CS_PROCHE_AIDANT = "52012006";

    /**
     */
    public static final String CS_OUVERT = "52013001";

    /**
     */
    public static final String CS_PAIEMENT_RETROACTIF = "52000003";

    /**
     */
    public static final String CS_RESTITUTION = "52000004";

    /**
     */
    public static final String CS_VALIDE = "52013002";

    /**
     * Comprend les états ouvert, valide et érrone
     */
    public static final String ETATS_NON_ENVOYE = "52013005";

    /**
     * Recherche avec tous les état possible
     */
    public static final String ETATS_TOUS = "TOUS";
}
