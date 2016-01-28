/*
 * Créé le 15 avril 2010
 */
package globaz.cygnus.api.attestations;

/**
 * @author fha
 */
public interface IRFAttestations {
    public static final String ATTESTATION_AVS = "66001008";
    public static final String BON_MOYENS_AUXILIAIRES = "66001007";

    public static final String CERTIFICAT_MOYENS_AUXILIAIRES = "66001004";
    // CONSTANTES POUR LE TYPE D'ATTESTATION
    public static final String COTISATION_PARITAIRE = "66001001";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String CS_TYPE_ATTESTATION = "RFTYPEAT";
    public static final String DECISION_MOYENS_AUXILIAIRES = "66001005";
    public static final String FRAIS_LIVRAISON = "66001006";
    public static final int INT_ATTESTATION_AVS = 66001008;
    public static final int INT_BON_MOYENS_AUXILIAIRES = 66001007;
    public static final int INT_CERTIFICAT_MOYENS_AUXILIAIRES = 66001004;

    // CONSTANTES INT POUR LE TYPE D'ATTESTATION
    // public static final int INT_COTISATION_PARITAIRE = 66001001;
    public static final int INT_DECISION_MOYENS_AUXILIAIRES = 66001005;
    public static final int INT_FRAIS_LIVRAISON = 66001006;
    public static final int INT_MAINTIEN_DOMICILE = 66001003;
    public static final int INT_REGIME_ALIMENTAIRE = 66001002;
    public static final String MAINTIEN_DOMICILE = "66001003";

    public static final String NIVEAU_AVERTISSEMENT_AVERTISSEMENT = "avertissement";
    public static final String NIVEAU_AVERTISSEMENT_REFUS = "refus";

    public static final String REGIME_ALIMENTAIRE = "66001002";
    public static final String TYPE_ATTESTATION_AVS = "attestationAVS";

    public static final String TYPE_ATTESTATION_DEFAUT = "defaut";
    public static final String TYPE_ATTESTATION_FRAIS_LIVRAISON9 = "fraisLivraison";

    public static final String TYPE_ATTESTATION_MAINTIEN_DOMICILE13 = "maintienDomicile";
    public static final String TYPE_ATTESTATION_MOYENS_AUX_BON11 = "moyensAuxiliaireBons";

    public static final String TYPE_ATTESTATION_MOYENS_AUX_CERTIFICAT3 = "moyensAuxiliaireCertificat";
    public static final String TYPE_ATTESTATION_MOYENS_AUX_DECISION5 = "moyensAuxiliaireDecision";
    public static final String TYPE_ATTESTATION_REGIME_ALI2 = "regimeAlimentaire";
}
