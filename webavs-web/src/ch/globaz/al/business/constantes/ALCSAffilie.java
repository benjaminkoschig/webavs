package ch.globaz.al.business.constantes;

/**
 * Codes systèmes liés aux affiliations
 * 
 * <ul>
 * <li>Genre d'assurance (paritaire, ...)</li>
 * <li>Code du type d'assurance "AF"</li>
 * <li>Périodicité de l'affilié</li>
 * </ul>
 * 
 * @author jts
 * 
 */
public interface ALCSAffilie {

    /**
     * Code système de l'attribut avis échéance à l'affilié (liste récapitulative + lettre)
     */
    public static final String ATTRIBUT_AVIS_ECH_AFFILIE = "61280002";
    /**
     * Code système de l'attribut avis échéance à l'allocataire (lettre allocataire)
     */
    public static final String ATTRIBUT_AVIS_ECH_ALLOCATAIRE = "61280003";
    /**
     * code système pour l'envoi de la recap par courrier
     */
    public static final String ATTRIBUT_RECAP_ENVOI_COURRIER = "61280006";
    /**
     * code système pour l'envoi de la recap via l'e-business
     */
    public static final String ATTRIBUT_RECAP_ENVOI_EBUSINESS = "61280007";
    /**
     * Code système de l'attribut pour recap au format csv uniquement à l'affilié
     */
    public static final String ATTRIBUT_RECAP_FORMAT_CSV = "61280004";
    /**
     * * Code système de l'attribut pour recap au format pdf à l'affilié
     */
    public static final String ATTRIBUT_RECAP_FORMAT_PDF = "61280008";

    /**
     * * Code système de l'attribut pour recap au format pdf et csv à l'affilié
     */
    public static final String ATTRIBUT_RECAP_FORMAT_PDF_CSV = "61280005";

    /**
     * Code système de l'attribut sans avis échéance (lettre récapitulative seul.)
     */
    public static final String ATTRIBUT_SANS_AVIS_ECH = "61280001";
    /**
     * Code système d'une assurance de genre indépendant
     */
    public static final String GENRE_ASSURANCE_INDEP = "801002";
    /**
     * Code système d'une assurance de genre paritaire
     */
    public static final String GENRE_ASSURANCE_PARITAIRE = "801001";

    /**
     * CS : groupe "attribut entité affilie"
     * 
     */
    public static final String GROUP_ATTRIBUT_AFFILIE = "60280000";
    /**
     * Groupe genre d'assurance
     * 
     * @see ALCSAffilie#GENRE_ASSURANCE_INDEP
     * @see ALCSAffilie#GENRE_ASSURANCE_PARITAIRE
     */
    public static final String GROUP_GENRE_ASSURANCE = "10800001";
    /**
     * Groupe périodicité
     * 
     * @see ALCSAffilie#PERIODICITE_ANN
     * @see ALCSAffilie#PERIODICITE_MEN
     * @see ALCSAffilie#PERIODICITE_TRI
     */
    // TODO (lot 2) à compléter
    public static final String GROUP_PERIODICITE = "";
    /**
     * Périodicité annuelle
     */
    public static final String PERIODICITE_ANN = "802004";

    /**
     * Périodicité mensuelle
     */
    public static final String PERIODICITE_MEN = "802001";

    /**
     * Périodicité trimestrielle
     */
    public static final String PERIODICITE_TRI = "802002";
    /**
     * Code système identifiant une assurance AF
     */
    public static final String TYPE_ASSURANCE_AF = "812002";
    /**
     * Code système définissant un lien sur une agence communale
     */
    public static final String TYPE_LIEN_AGENCE_COMMUNALE = "507007";
}
