package ch.globaz.al.business.constantes;

/**
 * Codes syst�mes li�s aux affiliations
 * 
 * <ul>
 * <li>Genre d'assurance (paritaire, ...)</li>
 * <li>Code du type d'assurance "AF"</li>
 * <li>P�riodicit� de l'affili�</li>
 * </ul>
 * 
 * @author jts
 * 
 */
public interface ALCSAffilie {

    /**
     * Code syst�me de l'attribut avis �ch�ance � l'affili� (liste r�capitulative + lettre)
     */
    public static final String ATTRIBUT_AVIS_ECH_AFFILIE = "61280002";
    /**
     * Code syst�me de l'attribut avis �ch�ance � l'allocataire (lettre allocataire)
     */
    public static final String ATTRIBUT_AVIS_ECH_ALLOCATAIRE = "61280003";
    /**
     * code syst�me pour l'envoi de la recap par courrier
     */
    public static final String ATTRIBUT_RECAP_ENVOI_COURRIER = "61280006";
    /**
     * code syst�me pour l'envoi de la recap via l'e-business
     */
    public static final String ATTRIBUT_RECAP_ENVOI_EBUSINESS = "61280007";
    /**
     * Code syst�me de l'attribut pour recap au format csv uniquement � l'affili�
     */
    public static final String ATTRIBUT_RECAP_FORMAT_CSV = "61280004";
    /**
     * * Code syst�me de l'attribut pour recap au format pdf � l'affili�
     */
    public static final String ATTRIBUT_RECAP_FORMAT_PDF = "61280008";

    /**
     * * Code syst�me de l'attribut pour recap au format pdf et csv � l'affili�
     */
    public static final String ATTRIBUT_RECAP_FORMAT_PDF_CSV = "61280005";

    /**
     * Code syst�me de l'attribut sans avis �ch�ance (lettre r�capitulative seul.)
     */
    public static final String ATTRIBUT_SANS_AVIS_ECH = "61280001";
    /**
     * Code syst�me d'une assurance de genre ind�pendant
     */
    public static final String GENRE_ASSURANCE_INDEP = "801002";
    /**
     * Code syst�me d'une assurance de genre paritaire
     */
    public static final String GENRE_ASSURANCE_PARITAIRE = "801001";

    /**
     * CS : groupe "attribut entit� affilie"
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
     * Groupe p�riodicit�
     * 
     * @see ALCSAffilie#PERIODICITE_ANN
     * @see ALCSAffilie#PERIODICITE_MEN
     * @see ALCSAffilie#PERIODICITE_TRI
     */
    // TODO (lot 2) � compl�ter
    public static final String GROUP_PERIODICITE = "";
    /**
     * P�riodicit� annuelle
     */
    public static final String PERIODICITE_ANN = "802004";

    /**
     * P�riodicit� mensuelle
     */
    public static final String PERIODICITE_MEN = "802001";

    /**
     * P�riodicit� trimestrielle
     */
    public static final String PERIODICITE_TRI = "802002";
    /**
     * Code syst�me identifiant une assurance AF
     */
    public static final String TYPE_ASSURANCE_AF = "812002";
    /**
     * Code syst�me d�finissant un lien sur une agence communale
     */
    public static final String TYPE_LIEN_AGENCE_COMMUNALE = "507007";
}
