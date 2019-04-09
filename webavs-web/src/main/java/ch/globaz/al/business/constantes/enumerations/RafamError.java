package ch.globaz.al.business.constantes.enumerations;

import globaz.jade.exception.JadeApplicationException;

/**
 * Enumère les codes d’erreur possibles pour un retour d'annonce RAFam
 * 
 * @author jts
 * 
 */
public enum RafamError {

    _101_DATE_DEBUT_INCOHERENTE("101", "61360101"),
    _102_DATE_FIN_INCOHERENTE("102", "61360102"),
    _103_BASE_LEGALE_INCOHERENTE("103", "61360103"),
    _104_STATUT_ACTIVITE_INCOHERANTE("104", "61360104"),
    _105_DATE_DEBUT_PLUS_GRAND_QUE_DATE_FIN("105", "61360105"),
    _106_NBEN_EGALE_NENF("106", "61360106"),
    _107_NO_CAF_ANNONCEUSE_CORRESPOND_PAS("107", "61360107"),
    _108_NO_CAF_NON_EXISTANT("108", "61360108"),
    _109_NO_CAISSE_JURIDIQUEMENT_NON_EXISTANT("109", "61360109"),
    _110_UTILISATION_CHAMPS_MOIS_ET_NB_JOURS_PAR_CAISSE_NON_CHOMAGE("110", "61360110"),
    _111_ALLOCATION_QUI_NEST_PAS_UNE_ALLOCATION_INTERNATIONALE_OU_ALLOCATION_UNIQUE("111", "61360111"),
    _112_ALLOCATION_DIFFERENTIELLE_NAISSANCE_AVEC_DATE("112", "61360112"),
    _113_ALLOCATION_FUTUR_PLUS_SIX_MOIS("113", "61360113"),
    _114_MUTATION_DANS_FURTUR_ANNONCE_EXISTANTE("114", "61360114"),
    _121_ALLOCATION_NAISSANCE_SANS_BASE_LEGALE_CANTONAL("121", "61360121"),
    _122_ALLOCATION_ADOPATION_SANS_BASE_LEGALE_CANTONAL("122", "61360122"),
    _131_CODE_PAYS_RESIDENCE_ENFANT_INVALIDE("131", "61360131"),
    _132_TYPE_ALLOCATION_BASE_LEGAL_INCOHERENT_PAYS_RESIDENCE_ENFANT("132", "61360132"),
    _141_IDE_INCORRECT("141", "61360141"),
    _201_NUMERO_DE_DROIT_DEJA_UTILISE("201", "61360201"),
    _202_TENTATIVE_MODIFICATION_ENREGISTREMENT_NON_CREE("202", "61360202"),
    _203_TENTATIVE_MODIFICATION_ENREGISTREMENT("203", "61360203"),
    _204_NUMERO_AVS_ENFANT_INCOHERENT_AVEC_ENREGISTREMENT("204", "61360204"),
    _205_TYPE_ALLOCATION_INCOHERENTE_AVEC_ENREGISTREMENT("205", "61360205"),
    _206_TENTATIVE_DOUBLE_MODIFICATION_MEME_JOUR_MEME_ALLOCATION("206", "61360206"),
    _207_MODIFICATION_SUR_ANNONCE_ANNULEE("207", "61360207"),
    _208_TENTATIVE_MODIF_ANNULE_ALLOCATION_EXPIRE("208", "61360208"),
    _209_TENTATIVE_CREER_ALLOCATION_EXPIRER_ARCHIVE("209", "61360209"),
    _210_ALLOCATION_ENFANT_OU_ADOPTION_DEJA_EXISTANTE("210", "61360210"),
    _211_ALLOCATION_ANNONCEE_SUR_PERIODE_CHEVAUCHANT_AUTRE_PERIODE("211", "61360211"),
    _212_ALLOCATION_DIFFERENTIELLE_SANS_ALLOCATION_DE_BASE_MEME_PERIODE("212", "61360212"),
    _213_ALLOCATION_DIFFERENTIELLE_AVEC_UNE_ALLOCATION_DE_BASE_MISE_EN_ERREUR("213", "61360213"),
    _214_ALLOCATION_DIFFERENTIELLE_SUR_ALLOCATION_DE_CHOMAGE("214", "61360214"),
    _301_NUMERO_AVS_ENFANT_INCONNU("301", "61360301"),
    _302_NUMERO_AVS_ENFANT_MODIFIE("302", "61360302"),
    _303_NUMERO_AVS_BENEFICIAIRE_INCONNU("303", "61360303"),
    _304_DONNEES_PERSONNELLES_ENFANT_BENEFICIAIRE_MODIFIEE("304", "61360304"),
    _305_DATE_NAISSANCE_UPI_INCOMPLETES("305", "61360305"),
    _306_ENFANT_DECEDEE_UPI("306", "61360306"),
    _307_BENEFICIAIRE_DECEDEE_UPI("307", "61360307"),
    _400_AUCUNE_ERREUR("400", "61360400"),
    _999_ERREUR_INCONNUE("999", "61360999");

    public static RafamError getRafamError(String code) throws JadeApplicationException {

        switch (Integer.parseInt(code)) {
            case 101:
                return _101_DATE_DEBUT_INCOHERENTE;
            case 102:
                return _102_DATE_FIN_INCOHERENTE;
            case 103:
                return _103_BASE_LEGALE_INCOHERENTE;
            case 104:
                return _104_STATUT_ACTIVITE_INCOHERANTE;
            case 105:
                return _105_DATE_DEBUT_PLUS_GRAND_QUE_DATE_FIN;
            case 106:
                return _106_NBEN_EGALE_NENF;
            case 107:
                return _107_NO_CAF_ANNONCEUSE_CORRESPOND_PAS;
            case 108:
                return _108_NO_CAF_NON_EXISTANT;
            case 109:
                return _109_NO_CAISSE_JURIDIQUEMENT_NON_EXISTANT;
            case 110:
                return _110_UTILISATION_CHAMPS_MOIS_ET_NB_JOURS_PAR_CAISSE_NON_CHOMAGE;
            case 111:
                return _111_ALLOCATION_QUI_NEST_PAS_UNE_ALLOCATION_INTERNATIONALE_OU_ALLOCATION_UNIQUE;
            case 112:
                return _112_ALLOCATION_DIFFERENTIELLE_NAISSANCE_AVEC_DATE;
            case 113:
                return _113_ALLOCATION_FUTUR_PLUS_SIX_MOIS;
            case 114:
                return _114_MUTATION_DANS_FURTUR_ANNONCE_EXISTANTE;
            case 121:
                return _121_ALLOCATION_NAISSANCE_SANS_BASE_LEGALE_CANTONAL;
            case 122:
                return _122_ALLOCATION_ADOPATION_SANS_BASE_LEGALE_CANTONAL;
            case 131:
                return _131_CODE_PAYS_RESIDENCE_ENFANT_INVALIDE;
            case 132:
                return _132_TYPE_ALLOCATION_BASE_LEGAL_INCOHERENT_PAYS_RESIDENCE_ENFANT;
            case 141:
                return _141_IDE_INCORRECT;
            case 201:
                return _201_NUMERO_DE_DROIT_DEJA_UTILISE;
            case 202:
                return _202_TENTATIVE_MODIFICATION_ENREGISTREMENT_NON_CREE;
            case 203:
                return _203_TENTATIVE_MODIFICATION_ENREGISTREMENT;
            case 204:
                return _204_NUMERO_AVS_ENFANT_INCOHERENT_AVEC_ENREGISTREMENT;
            case 205:
                return _205_TYPE_ALLOCATION_INCOHERENTE_AVEC_ENREGISTREMENT;
            case 206:
                return _206_TENTATIVE_DOUBLE_MODIFICATION_MEME_JOUR_MEME_ALLOCATION;
            case 207:
                return _207_MODIFICATION_SUR_ANNONCE_ANNULEE;
            case 208:
                return _208_TENTATIVE_MODIF_ANNULE_ALLOCATION_EXPIRE;
            case 209:
                return _209_TENTATIVE_CREER_ALLOCATION_EXPIRER_ARCHIVE;
            case 210:
                return _210_ALLOCATION_ENFANT_OU_ADOPTION_DEJA_EXISTANTE;
            case 211:
                return _211_ALLOCATION_ANNONCEE_SUR_PERIODE_CHEVAUCHANT_AUTRE_PERIODE;
            case 212:
                return _212_ALLOCATION_DIFFERENTIELLE_SANS_ALLOCATION_DE_BASE_MEME_PERIODE;
            case 213:
                return _213_ALLOCATION_DIFFERENTIELLE_AVEC_UNE_ALLOCATION_DE_BASE_MISE_EN_ERREUR;
            case 214:
                return _214_ALLOCATION_DIFFERENTIELLE_SUR_ALLOCATION_DE_CHOMAGE;
            case 301:
                return _301_NUMERO_AVS_ENFANT_INCONNU;
            case 302:
                return _302_NUMERO_AVS_ENFANT_MODIFIE;
            case 303:
                return _303_NUMERO_AVS_BENEFICIAIRE_INCONNU;
            case 304:
                return _304_DONNEES_PERSONNELLES_ENFANT_BENEFICIAIRE_MODIFIEE;
            case 305:
                return _305_DATE_NAISSANCE_UPI_INCOMPLETES;
            case 306:
                return _306_ENFANT_DECEDEE_UPI;
            case 307:
                return _307_BENEFICIAIRE_DECEDEE_UPI;
            case 400:
            case 0:
                return _400_AUCUNE_ERREUR;
            default:
                return _999_ERREUR_INCONNUE;
        }
    }

    private String code;
    private String cs;

    RafamError(String code, String cs) {
        this.code = code;
        this.cs = cs;
    }

    public String getCode() {
        return code;
    }

    public String getCS() {
        return cs;
    }
}
