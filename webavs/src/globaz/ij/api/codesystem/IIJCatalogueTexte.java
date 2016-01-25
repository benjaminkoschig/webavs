package globaz.ij.api.codesystem;

public interface IIJCatalogueTexte {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CS_ATTESTATION = "52426005";
    public static final String CS_ATTESTATION_FISCALE = "52426007";

    public static final String CS_COMMUNICATION = "52426003";
    public static final String CS_DECISION = "52426001";
    public static final String CS_DECOMPTE = "52426002";
    // Domaines pour les catalogues des texte IJ
    public static final String CS_GROUPE_DOMAINES = "IJDOMAINES";
    // types de documents pour les catalogues de texte IJ
    public static final String CS_GROUPE_TYPES_DOCUMENTS = "IJTYPES";
    public static final String CS_IJ = "52425001";
    public static final String CS_LETTRE_ENTETE = "52426008";
    public static final String CS_MOYENS_DROIT = "52426009";
    public static final String CS_RECAPITULATION = "52426006";
    public static final String CS_RESTITUTION = "52426004";

    public String CS_ACTIF = "";
    // Etats des documents des catalogues de texte IJ
    public String CS_GROUPE_ETAT_CATALOGUES = "";
    public String CS_INACTIF = "";

}
