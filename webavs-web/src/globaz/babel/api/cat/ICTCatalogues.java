package globaz.babel.api.cat;

/**
 * @author vre
 */
public interface ICTCatalogues {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // // Domaines pour les catalogues des texte
    // public static final String CS_GROUPE_DOMAINES = "PRDOMAINES";
    // public static final String CS_APG = "52203001";
    // public static final String CS_MATERNITE = "52203002";
    // public static final String CS_IJ = "52203003";
    //
    // // types de documents pour les catalogues de texte
    // public static final String CS_GROUPE_TYPES_DOCUMENTS = "PRTYPES";
    // public static final String CS_DECISION = "52204001";
    // public static final String CS_DECOMPTE = "52204002";
    // public static final String CS_COMMUNICATION = "52204003";
    // public static final String CS_RESTITUTION = "52204004";
    // public static final String CS_ATTESTATION = "52204005";
    // public static final String CS_RECAPITULATION = "52204006";
    // public static final String CS_ATTESTATION_FISCALE = "52204007";
    // public static final String CS_LETTRE_ENTETE = "52204008";

    // // Etats des documents des catalogues de texte
    // public String CS_GROUPE_ETAT_CATALOGUES = "PRETACTXT";
    // public String CS_ACTIF = "52205001";
    // public String CS_INACTIF = "52205002";

    /**
     * @deprecated, repmlacé par IAPCatalogueTexte
     */
    public static final String CS_DOCUMENT_DOMAINE_APG = "52018001";
    /**
     * @deprecated, repmlacé par IIJCatalogueTexte
     */
    public static final String CS_DOCUMENT_DOMAINE_IJ = "52425001";

    /**
     * @deprecated, repmlacé par IAPCatalogueTexte
     */
    public static final String CS_DOCUMENT_DOMAINE_MATERNITE = "52020001";

}
