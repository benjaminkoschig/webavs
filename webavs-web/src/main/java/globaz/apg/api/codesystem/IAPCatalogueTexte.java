package globaz.apg.api.codesystem;

public interface IAPCatalogueTexte {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CS_APG = "52018001";
    public static final String CS_ATTESTATION_APG = "52019005";

    public static final String CS_ATTESTATION_FISCALE_APG = "52019007";
    public static final String CS_ATTESTATION_FISCALE_MAT = "52021007";
    public static final String CS_ATTESTATION_FISCALE_PAN = "52031007";
    public static final String CS_ATTESTATION_MAT = "52021005";
    public static final String CS_COMMUNICATION_APG = "52019003";
    public static final String CS_COMMUNICATION_MAT = "52021003";
    public static final String CS_DECISION_APG = "52019001";
    public static final String CS_DECISION_MAT = "52021001";
    public static final String CS_DECISION_REFUS_MAT = "52021009";
    public static final String CS_DECOMPTE_APG = "52019002";

    public static final String CS_DECOMPTE_MAT = "52021002";
    // Domaines pour les catalogues des texte APG
    public static final String CS_GROUPE_DOMAINES_APG = "APDOMAINES";
    // Domaines pour les catalogues des texte MAT
    public static final String CS_GROUPE_DOMAINES_MAT = "MATDOMAINE";

    // types de documents pour les catalogues de texte APG
    public static final String CS_GROUPE_TYPES_DOCUMENTS_APG = "APTYPES";
    // types de documents pour les catalogues de texte MAT
    public static final String CS_GROUPE_TYPES_DOCUMENTS_MAT = "MATTYPES";

    public static final String CS_LETTRE_ENTETE_APG = "52019008";
    public static final String CS_LETTRE_ENTETE_MAT = "52021008";
    public static final String CS_MATERNITE = "52020001";
    public static final String CS_RECAPITULATION_APG = "52019006";
    public static final String CS_RECAPITULATION_MAT = "52021006";
    public static final String CS_RESTITUTION_APG = "52019004";
    public static final String CS_RESTITUTION_MAT = "52021004";
    public String CS_ACTIF_APG = "";
    public String CS_ACTIF_MAT = "";
    // Etats des documents des catalogues de texte APG
    public String CS_GROUPE_ETAT_CATALOGUES_APG = "";

    // Etats des documents des catalogues de texte MAT
    public String CS_GROUPE_ETAT_CATALOGUES_MAT = "";
    public String CS_INACTIF_APG = "";
    public String CS_INACTIF_MAT = "";

    public static final String CS_PANDEMIE = "52031001";
}
