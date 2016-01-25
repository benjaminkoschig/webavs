/*
 * Créé le 27 oct. 05
 */
package globaz.externe;

/**
 * @author dvh
 */
public interface ISFConstantesExternes {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // Branche économique agricole
    public static final String BE_AGRICOLE = "805001";

    /**
     */
    public static final String OSIRIS_CS_CATEGORIE_SECTION_APG = "227016";

    /**
     */
    public static final String OSIRIS_CS_CATEGORIE_SECTION_IJAI = "227021";

    /**
     */
    public static final String OSIRIS_CS_CATEGORIE_SECTION_RESTITUTION = "227026";

    /**
     */
    public static final String OSIRIS_CS_CODE_ISO_MONNAIE_CHF = "215001";
    /**
     * @deprecated
     */
    @Deprecated
    public static final String OSIRIS_ID_ROLE_AFFILIE = "517002";

    /**
     */
    public static final String OSIRIS_ID_ROLE_APG = "517033";

    /**
     */
    public static final String OSIRIS_ID_ROLE_IJAI = "517034";

    /** Rôle d'un tiers */
    public final static String OSIRIS_ID_ROLE_NON_AFFILIE = "517037";

    /**
     */
    public static final String OSIRIS_ID_TYPE_SECTION_APG = "16";

    /**
     */
    public static final String OSIRIS_ID_TYPE_SECTION_IJAI = "21";

    public static final String OSIRIS_ID_TYPE_SECTION_RESTITUTION_APG = "26";

    public static final String OSIRIS_ID_TYPE_SECTION_RESTITUTION_IJAI = "26";

    public static final String TIERS_CS_DOMAINE_APPLICATION_APG = "519002";
    /*
     * Domaine application Tiers (utilisé pour les domaines d'adresse de paiements)
     */
    public static final String TIERS_CS_DOMAINE_APPLICATION_DEFAULT = "519004";
    public static final String TIERS_CS_DOMAINE_APPLICATION_IJAI = "519009";
    public static final String TIERS_CS_DOMAINE_MATERNITE = "519003";

}
