package ch.globaz.al.business.constantes;

/**
 * Codes système liés aux tarifs
 * 
 * <ul>
 * <li>catégorie de tarif</li>
 * <li>critères de sélection</li>
 * <li>législations</li>
 * <li>type de résident</li>
 * </ul>
 * 
 * @author jts
 * 
 */
public interface ALCSTarif {

    /*
     * Catégories de tarif
     */
    /**
     * CS : catégorie de tarif du canton "Argovie"
     */
    public static final String CATEGORIE_AG = "61190001";
    /**
     * CS : catégorie de tarif de la caisse "Agence de Lausanne"
     */
    public static final String CATEGORIE_AGLS = "61190030";
    /**
     * CS : catégorie de tarif du canton "Appenzell int."
     */
    public static final String CATEGORIE_AI = "61190002";
    /**
     * CS : catégorie de tarif de la caisse "ALKO"
     */
    public static final String CATEGORIE_ALKO = "61190031";
    /**
     * CS : catégorie de tarif du canton "Appenzell ext."
     */
    public static final String CATEGORIE_AR = "61190003";
    /**
     * CS : catégorie de tarif du canton "Berne"
     */
    public static final String CATEGORIE_BE = "61190004";
    /**
     * CS : catégorie de tarif du canton "Bâle Campagne"
     */
    public static final String CATEGORIE_BL = "61190005";
    /**
     * CS : catégorie de tarif du canton "Bâle Ville"
     */
    public static final String CATEGORIE_BS = "61190006";
    /**
     * CS: catégorie de tarif "CATMP" cvci temporaire
     */
    public static final String CATEGORIE_CATMP = "61190049";
    /**
     * CS : catégorie de tarif de la caisse "CCJU"
     */
    public static final String CATEGORIE_CCJU = "61190032";
    /**
     * CS : catégorie de tarif de la caisse "CCVD"
     */
    public static final String CATEGORIE_CCVD = "61190033";
    /**
     * CS : catégorie de tarif de la caisse "CICICAM"
     */
    public static final String CATEGORIE_CICI = "61190034";
    /**
     * CS : catégorie de tarif de la caisse "CVCI"
     */
    public static final String CATEGORIE_CVCI = "61190035";
    /**
     * CS : catégorie de tarif de la caisse "FACO"
     */
    public static final String CATEGORIE_FACO = "61190036";
    /**
     * CS : catégorie de tarif "fédéral"
     */
    public static final String CATEGORIE_FED = "61190027";
    /**
     * CS: catégorie de tarid de la caisse "FPV"
     */
    public static final String CATEGORIE_FPV = "61190048";
    /**
     * CS : catégorie de tarif FPV CAF des AFIT
     */
    public static final String CATEGORIE_FPV_AFIT = "61190067";
    /**
     * CS : catégorie de tarif FPV CAF des agents d'affaires
     */
    public static final String CATEGORIE_FPV_AGA = "61190066";
    /**
     * CS : catégorie de tarif FPV CAF des at
     */
    public static final String CATEGORIE_FPV_AT = "61190060";
    /**
     * CS : catégorie de tarif FPV CAF des avocats
     */
    public static final String CATEGORIE_FPV_AVO = "61190054";
    /**
     * CS : catégorie de tarif FPV_CAF_BANQUES
     */
    public static final String CATEGORIE_FPV_BANQUES = "61190051";
    /**
     * CS : catégorie de tarif FPV CAF de ciraf
     */
    public static final String CATEGORIE_FPV_CIRAF = "61190062";
    /**
     * CS : catégorie de tarif FPV CAF des cliniques
     */
    public static final String CATEGORIE_FPV_CLIN = "61190055";
    /**
     * CS : catégorie de tarif FPV CAF des écoles privées
     */
    public static final String CATEGORIE_FPV_EPRIV = "61190058";
    /**
     * CS : catégorie de tarif FPV CAF des garages
     */
    public static final String CATEGORIE_FPV_GAR = "61190056";
    /**
     * CS : catégorie de tarif FPV CAF des géomètres
     */
    public static final String CATEGORIE_FPV_GEOM = "61190057";
    /**
     * CS : catégorie de tarif FPV_CAF_IAV
     */
    public static final String CATEGORIE_FPV_IAV = "61190052";
    /**
     * CS : catégorie de tarif FPV CAF de immobilier
     */
    public static final String CATEGORIE_FPV_IMM = "61190064";
    /**
     * CS : catégorie de tarif FPV CAF des indépendants
     */
    public static final String CATEGORIE_FPV_IND = "61190068";
    /**
     * CS : catégorie de tarif FPV_CAF_INTER
     */
    public static final String CATEGORIE_FPV_INTER = "61190050";
    /**
     * CS : catégorie de tarif FPV CAF des libraires
     */
    public static final String CATEGORIE_FPV_LIBR = "61190059";
    /**
     * CS : catégorie de tarif FPV CAF des médecins
     */
    public static final String CATEGORIE_FPV_MED = "61190061";
    /**
     * CS : catégorie de tarif FPV CAF des médecins dentistes
     */
    public static final String CATEGORIE_FPV_MED_DEN = "61190053";
    /**
     * CS : catégorie de tarif FPV CAF des notaires
     */
    public static final String CATEGORIE_FPV_NOT = "61190065";
    /**
     * CS : catégorie de tarif FPV CAF des pharmaciens
     */
    public static final String CATEGORIE_FPV_PHA = "611900563";

    /** CS : FPV Visana Argovie */
    public static final String CATEGORIE_FPV_VISANA_AG = "61190070";
    /** CS : FPV Visana Appenzell Rhodes-Intérieures */
    public static final String CATEGORIE_FPV_VISANA_AI = "61190071";
    /** CS : FPV Visana Appenzell Rhodes-Extérieures */
    public static final String CATEGORIE_FPV_VISANA_AR = "61190072";
    /** CS : FPV Visana Berne */
    public static final String CATEGORIE_FPV_VISANA_BE = "61190073";
    /** CS : FPV Visana Bâle campagne */
    public static final String CATEGORIE_FPV_VISANA_BL = "61190074";
    /** CS : FPV Visana Bâle ville */
    public static final String CATEGORIE_FPV_VISANA_BS = "61190075";
    /** CS : FPV Visana Fribourg */
    public static final String CATEGORIE_FPV_VISANA_FR = "61190076";
    /** CS : FPV Visana Genève */
    public static final String CATEGORIE_FPV_VISANA_GE = "61190077";
    /** CS : FPV Visana Glaris */
    public static final String CATEGORIE_FPV_VISANA_GL = "61190078";
    /** CS : FPV Visana Grisons */
    public static final String CATEGORIE_FPV_VISANA_GR = "61190079";
    /** CS : FPV Visana Jura */
    public static final String CATEGORIE_FPV_VISANA_JU = "61190080";
    /** CS : FPV Visana Lucerne */
    public static final String CATEGORIE_FPV_VISANA_LU = "61190081";
    /** CS : FPV Visana Neuchâtel */
    public static final String CATEGORIE_FPV_VISANA_NE = "61190082";
    /** CS : FPV Visana Nidwald */
    public static final String CATEGORIE_FPV_VISANA_NW = "61190083";
    /** CS : FPV Visana Obwald */
    public static final String CATEGORIE_FPV_VISANA_OW = "61190084";
    /** CS : FPV Visana Saint-Gall */
    public static final String CATEGORIE_FPV_VISANA_SG = "61190085";
    /** CS : FPV Visana Schaffhouse */
    public static final String CATEGORIE_FPV_VISANA_SH = "61190086";
    /** CS : FPV Visana Soleure */
    public static final String CATEGORIE_FPV_VISANA_SO = "61190087";
    /** CS : FPV Visana Schwytz */
    public static final String CATEGORIE_FPV_VISANA_SZ = "61190088";
    /** CS : FPV Visana Thurgovie */
    public static final String CATEGORIE_FPV_VISANA_TG = "61190089";
    /** CS : FPV Visana Tessin */
    public static final String CATEGORIE_FPV_VISANA_TI = "61190090";
    /** CS : FPV Visana Uri */
    public static final String CATEGORIE_FPV_VISANA_UR = "61190091";
    /** CS : FPV Visana Vaud */
    public static final String CATEGORIE_FPV_VISANA_VD = "61190092";
    /** CS : FPV Visana Valais */
    public static final String CATEGORIE_FPV_VISANA_VS = "61190093";
    /** CS : FPV Visana Zoug */
    public static final String CATEGORIE_FPV_VISANA_ZG = "61190094";
    /** CS : FPV Visana Zürich */
    public static final String CATEGORIE_FPV_VISANA_ZH = "61190095";
    /**
     * CS : catégorie de tarif du canton "Fribourg"
     */
    public static final String CATEGORIE_FR = "61190007";
    /**
     * CS : catégorie de tarif de la caisse FVE
     */
    public static final String CATEGORIE_FVE = "61190069";
    /**
     * CS : catégorie de tarif du canton "Genève"
     */
    public static final String CATEGORIE_GE = "61190008";
    /**
     * CS : catégorie de tarif du canton "Glaris"
     */
    public static final String CATEGORIE_GL = "61190009";
    /**
     * CS : catégorie de tarif du canton "Grisons"
     */
    public static final String CATEGORIE_GR = "61190010";
    /**
     * CS : catégorie de tarif de la caisse "H51.0"
     */
    public static final String CATEGORIE_H510 = "61190037";
    /**
     * CS : catégorie de tarif de la caisse "H51.3"
     */
    public static final String CATEGORIE_H513 = "61190039";
    /**
     * CS : catégorie de tarif de la caisse "H51.4"
     */
    public static final String CATEGORIE_H514 = "61190040";

    /**
     * CS : catégorie de tarif de la caisse "H51.5"
     */
    public static final String CATEGORIE_H515 = "61190041";
    /**
     * CS : catégorie de tarif de la caisse "H51.7"
     */
    public static final String CATEGORIE_H517 = "61190042";
    /**
     * CS : catégorie de tarif de la caisse "H51.X"
     */
    public static final String CATEGORIE_H51X = "61190038";
    /**
     * CS : catégorie de tarif du canton "Jura"
     */
    public static final String CATEGORIE_JU = "61190011";
    /**
     * CS : catégorie de tarif "agricole de montagne"
     */
    public static final String CATEGORIE_LFM = "61190028";

    /**
     * CS : catégorie de tarif "agricole de montagne, taux 1/3"
     */
    public static final String CATEGORIE_LFM13 = "61190043";

    /**
     * CS : catégorie de tarif "agricole de montagne, taux 2/3"
     */
    public static final String CATEGORIE_LFM23 = "61190044";

    /**
     * CS : catégorie de tarif "agricole de plaine"
     */
    public static final String CATEGORIE_LFP = "61190029";

    /**
     * CS : catégorie de tarif "agricole de plaine, taux 1/3"
     */
    public static final String CATEGORIE_LFP13 = "61190045";

    /**
     * CS : catégorie de tarif "agricole de plaine, taux 2/3"
     */
    public static final String CATEGORIE_LFP23 = "61190046";

    /**
     * CS : catégorie de tarif "agriculture jurassienne"
     */
    public static final String CATEGORIE_LJU = "61190047";

    /**
     * CS : catégorie de tarif du canton "Lucerne"
     */
    public static final String CATEGORIE_LU = "61190012";

    /**
     * CS : catégorie de tarif du canton "Neuchâtel"
     */
    public static final String CATEGORIE_NE = "61190013";

    /**
     * CS : catégorie de tarif du canton "Nidwald"
     */
    public static final String CATEGORIE_NW = "61190014";

    /**
     * CS : catégorie de tarif du canton "Obwald"
     */
    public static final String CATEGORIE_OW = "61190015";
    /**
     * CS : catégorie de tarif du canton "St-Gall"
     */
    public static final String CATEGORIE_SG = "61190016";

    /**
     * CS : catégorie de tarif du canton "Schaffouse"
     */
    public static final String CATEGORIE_SH = "61190017";

    /**
     * CS : catégorie de tarif du canton "Soleure"
     */
    public static final String CATEGORIE_SO = "61190018";

    /**
     * CS : catégorie de tarif du supplément horloger (dès 01.01.2012)
     */
    public static final String CATEGORIE_SUP_HORLO = "61190096";

    /**
     * CS : catégorie de tarif du canton "Schwytz"
     */
    public static final String CATEGORIE_SZ = "61190019";

    /**
     * CS : catégorie de tarif du canton "Turgovie"
     */
    public static final String CATEGORIE_TG = "61190020";

    /**
     * CS : catégorie de tarif du canton "Tessin"
     */
    public static final String CATEGORIE_TI = "61190021";

    /**
     * CS : catégorie de tarif du canton "Uri"
     */
    public static final String CATEGORIE_UR = "61190022";

    /**
     * CS : catégorie de tarif du canton d
     */
    public static final String CATEGORIE_VD = "61190023";

    /**
     * CS : catégorie de tarif du canton VD droit acquis
     */
    public static final String CATEGORIE_VD_DROIT_ACQUIS = "61190097";

    /**
     * CS : catégorie de tarif du canton "Valais"
     */
    public static final String CATEGORIE_VS = "61190024";

    /**
     * CS : catégorie de tarif du canton "Zoug"
     */
    public static final String CATEGORIE_ZG = "61190025";

    /**
     * CS : catégorie de tarif du canton "Zürich"
     */
    public static final String CATEGORIE_ZH = "61190026";

    /*
     * Critères de sélection des tarifs
     */
    /**
     * CS : critère de sélection de tarif "âge"
     */
    public static final String CRITERES_AGE = "61200001";
    /**
     * CS : critère de sélection de tarif "nombre"
     */
    public static final String CRITERES_NBR = "61200002";
    /**
     * CS : critère de sélection de tarif "revenu indépendant"
     */
    public static final String CRITERES_REVIND = "61200003";

    /**
     * CS : critère de sélection de tarif "revenu non-actif"
     */
    public static final String CRITERES_REVNAC = "61200004";
    /**
     * CS : critère de sélection de tarif "rang"
     */
    public static final String CRITERES_RNG = "61200005";
    /*
     * ========================================================================= groupes de codes système
     * =========================================================================
     */
    /**
     * CS : groupe "catégorie de tarif"
     * 
     * @see ALCSTarif#CATEGORIE_AG
     * @see ALCSTarif#CATEGORIE_AGLS
     * @see ALCSTarif#CATEGORIE_AI
     * @see ALCSTarif#CATEGORIE_ALKO
     * @see ALCSTarif#CATEGORIE_AR
     * @see ALCSTarif#CATEGORIE_BE
     * @see ALCSTarif#CATEGORIE_BL
     * @see ALCSTarif#CATEGORIE_BS
     * @see ALCSTarif#CATEGORIE_CCJU
     * @see ALCSTarif#CATEGORIE_CCVD
     * @see ALCSTarif#CATEGORIE_CICI
     * @see ALCSTarif#CATEGORIE_CVCI
     * @see ALCSTarif#CATEGORIE_FACO
     * @see ALCSTarif#CATEGORIE_FED
     * @see ALCSTarif#CATEGORIE_FR
     * @see ALCSTarif#CATEGORIE_GE
     * @see ALCSTarif#CATEGORIE_GL
     * @see ALCSTarif#CATEGORIE_GR
     * @see ALCSTarif#CATEGORIE_H510
     * @see ALCSTarif#CATEGORIE_H513
     * @see ALCSTarif#CATEGORIE_H514
     * @see ALCSTarif#CATEGORIE_H515
     * @see ALCSTarif#CATEGORIE_H517
     * @see ALCSTarif#CATEGORIE_H51X
     * @see ALCSTarif#CATEGORIE_JU
     * @see ALCSTarif#CATEGORIE_LFM
     * @see ALCSTarif#CATEGORIE_LFM13
     * @see ALCSTarif#CATEGORIE_LFM23
     * @see ALCSTarif#CATEGORIE_LFP
     * @see ALCSTarif#CATEGORIE_LFP13
     * @see ALCSTarif#CATEGORIE_LFP23
     * @see ALCSTarif#CATEGORIE_LJU
     * @see ALCSTarif#CATEGORIE_LU
     * @see ALCSTarif#CATEGORIE_NE
     * @see ALCSTarif#CATEGORIE_NW
     * @see ALCSTarif#CATEGORIE_OW
     * @see ALCSTarif#CATEGORIE_SG
     * @see ALCSTarif#CATEGORIE_SH
     * @see ALCSTarif#CATEGORIE_SO
     * @see ALCSTarif#CATEGORIE_SZ
     * @see ALCSTarif#CATEGORIE_TG
     * @see ALCSTarif#CATEGORIE_TI
     * @see ALCSTarif#CATEGORIE_UR
     * @see ALCSTarif#CATEGORIE_VD
     * @see ALCSTarif#CATEGORIE_VS
     * @see ALCSTarif#CATEGORIE_ZG
     * @see ALCSTarif#CATEGORIE_ZH
     */
    public static final String GROUP_CATEGORIE = "60190000";
    /**
     * CS : groupe "critère de sélection des tarifs"
     * 
     * @see ALCSTarif#CRITERES_AGE
     * @see ALCSTarif#CRITERES_NBR
     * @see ALCSTarif#CRITERES_REVIND
     * @see ALCSTarif#CRITERES_REVNAC
     * @see ALCSTarif#CRITERES_RNG
     */
    public static final String GROUP_CRITERE = "60200000";

    /**
     * CS : groupe "Législation"
     * 
     * @see ALCSTarif#LEGISLATION_AGRICOLE
     * @see ALCSTarif#LEGISLATION_CAISSE
     * @see ALCSTarif#LEGISLATION_CANTONAL
     * @see ALCSTarif#LEGISLATION_FEDERAL
     */
    public static final String GROUP_LEGISLATION = "60210000";
    /**
     * CS : groupe "Type de résident"
     * 
     * @see ALCSTarif#RESIDENT_CH
     * @see ALCSTarif#RESIDENT_ETR
     * @see ALCSTarif#RESIDENT_F
     */
    public static final String GROUP_RESIDENT = "60220000";
    /*
     * =========================================================================
     * =========================================================================
     */
    /*
     * Législation des tarifs
     */
    /**
     * CS : législation "agricole"
     */
    public static final String LEGISLATION_AGRICOLE = "61210001";
    /**
     * CS : législation "caisse"
     */
    public static final String LEGISLATION_CAISSE = "61210002";
    /**
     * CS : législation "cantonale"
     */
    public static final String LEGISLATION_CANTONAL = "61210003";
    /**
     * CS : législation "fédérale"
     */
    public static final String LEGISLATION_FEDERAL = "61210004";
    /*
     * Catégories de résidents
     */
    /**
     * CS : résident "Suisse"
     */
    public static final String RESIDENT_CH = "61220001";
    /**
     * CS : résident "Etranger"
     */
    public static final String RESIDENT_ETR = "61220002";
    /**
     * CS : résident "Frontalier"
     */
    public static final String RESIDENT_F = "61220003";
}
