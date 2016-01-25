package ch.globaz.al.business.constantes;

/**
 * Constantes liées aux documents
 * 
 * @author PTA
 * 
 */

public interface ALConstDocument {
    /**
     * entete de document décision AGLS
     */
    public static final String AGLS_ENTETE_DECISION = "ENTETE_AGLS_DECISION";
    /**
     * entete de document AGLS
     */
    public static final String AGLS_ENTETE_STANDARD = "ENTETE_AGLS_STANDARD";

    /**
     * entete de document CCJU caisse AF
     */
    public static final String CCJU_ENTETE_CAISSE_AF = "CCJU_Caisse";
    /**
     * entete de document CCJU
     */
    public static final String CCJU_ENTETE_CAISSE_COMPENS = "CCJU_Agricole";
    /**
     * entete de document CCJU protocole AF
     */
    public static final String CCJU_ENTETE_PROTOCOLE_AF = "CCJU_Protocole";
    /**
     * entete document CCVD entete caisse AF
     */
    public static final String CCVD_ENTETE_CAISSE_AF = "ENTETE_CCVD_ALLOC_FAMI";
    /**
     * entente document CCVD entete caisse Compensation
     */
    public static final String CCVD_ENTETE_CAISSE_COMPENS = "ENTETE_CCVD_CAI_COMPENS";
    /**
     * entete de document de la cicicam
     */
    public static final String CICICAM_STANDARD = "ENTETE_CICI_STANDARD";
    /**
     * numéro inforom pour les récaps
     */
    public final static String DOC_TYPE_NUMBER_INFOROM_ECHEANCE_AFFILIE = "3007WAF";
    /**
     * numéro inforom pour les récaps
     */
    public final static String DOC_TYPE_NUMBER_INFOROM_ECHEANCE_ALLOCATAIRE = "3009WAF";

    /**
     * numéro inforom pour les récaps
     */
    public final static String DOC_TYPE_NUMBER_INFOROM_RECAP = "3004WAF";

    /**
     * type de document decision
     */
    public static final String DOCUMENT_DECISION = "DECISION";
    /**
     * type de document déclaration versment
     */
    public static final String DOCUMENT_DECLARATION_VERSEMENT = "DECLARATION";
    /**
     * type de document échéance destination affilié
     */
    public static final String DOCUMENT_ECHEANCE_AFFILIE = "ECHEANCE_AFFILIE";

    /**
     * type de document échéance destination alllocataire direct
     */
    public static final String DOCUMENT_ECHEANCE_ALLOC_DIR = "ECHEANCE_ALLOC_INDIRECT";
    /**
     * type de document échéance destination allocataire indirect
     */
    public static final String DOCUMENT_ECHEANCE_ALLOC_IND = "ECHEANCE_ALLOC_INDIRECT";

    /**
     * document de type 'protocole'
     */
    public static final String DOCUMENT_PROTOCOLE = "PROTOCOLE";

    /**
     * entete de document FVER
     */

    public static final String FVE_ENTETE_STANDARD = "ENTETE_FVE_STANDARD";

    /**
     * libellé pour le gestionaire de dossier
     */
    public static final String GESTION_DOSSIER = "gestionnaire_libelle";
    /**
     * ente de document H510 en allemand
     */
    public static final String H510_ENTETE_STANDARD_DE = "ENTETE_H510_STANDARD_DE";
    /**
     * ente de document H510 en français
     */
    public static final String H510_ENTETE_STANDARD_FR = "ENTETE_H510_STANDARD_FR";

    /**
     * entete de document standard h513
     */
    public static final String H513_ENTETE_STANDARD_FR = "ENTETE_H513_STANDARD_FR";
    /**
     * entete de document h514 en français
     */
    public static final String H514_ENTETE_STANDARD_DE = "ENTETE_H514_STANDARD_DE";
    /**
     * entete de document h514 en français
     */
    public static final String H514_ENTETE_STANDARD_FR = "ENTETE_H514_STANDARD_FR";

    /**
     * entete de document h515 Allemand
     */
    public static final String H515_ENTETE_STANDARD_DE = "ENTETE_H515_STANDARD_DE";
    /**
     * ente de document H515 en français
     */
    public static final String H515_ENTETE_STANDARD_FR = "ENTETE_H515_STANDARD_FR";

    /**
     * entete de document h517 Allemand
     */
    public static final String H517_ENTETE_STANDARD_DE = "ENTETE_H517_STANDARD_DE";
    /**
     * ente de document H517 en français
     */
    public static final String H517_ENTETE_STANDARD_FR = "ENTETE_H517_STANDARD_FR";
    /**
     * entete de document H51X décision en allemand
     */
    public static final String H51X_ENTETE_DECISION_DE = "ENTETE_H51X_DECISION_DE";
    /**
     * entete de document H51X décision en français
     */
    public static final String H51X_ENTETE_DECISION_FR = "ENTETE_H51X_DECISION_FR";
    /**
     * entete de document H51X décision en allemand
     */
    public static final String H51X_ENTETE_STANDARD_DE = "ENTETE_H51X_STANDARD_DE";
    /**
     * entete de document H51X décision en français
     */
    public static final String H51X_ENTETE_STANDARD_FR = "ENTETE_H51X_STANDARD_FR";
    /**
     * constante langue affilié
     */

    public static final String LANGUE_AFFIL = "LANGUE_AFFIL";

    /**
     * constante langue allocataire
     */

    public static final String LANGUE_ALLOC = "LANGUE_ALLOC";

    /**
     * type de document lettre accompagnement
     */
    public static final String LETTRE_ACCOMPAGNEMENT = "LETTRE_ACCOMPAGNEMENT";
    /**
     * libellé du document contenant la valeur pour le courriel
     */

    public static final String MAIL_VALUE = "email_valeur";
    /**
     * libellé du document contenant la valeur pour le nom
     */
    public static final String NAME_VALUE = "name_valeur";
    /**
     * libellé du du document contenant la valeur pour le télphone
     */
    public static final String PHONE_VALUE = "phone_valeur";
    /**
     * type de document récapitulatif
     */
    public static final String RECAPITULATIF_DOCUMENT = "RECAPITULATIF";
    /**
     * constante salarié
     */
    public static final String SALARIE = "salarié";
    /**
     * signature de la caisse AGLS
     */
    public static final String SIGNATURE_CAISSE_AGLS = "al.decision.signature.agls";
    /**
     * Signature caisse alloc familiales CCJU
     */
    public static final String SIGNATURE_CAISSE_ALLOC_CCJU = "al.decision.signature.ccju.allocFami";
    /**
     * signature caisse alloc CCVD
     */
    public static final String SIGNATURE_CAISSE_CCVD = "al.decision.signature.ccvd";
    /**
     * Signature de la caisse CICICAM/CINALFA pour les décisions et documents autres que les échéances
     */
    public static final String SIGNATURE_CAISSE_CICICAM = "al.decision.signature.cicicam";
    /**
     * signature de la caisse CICICAM/CINALFA pour les échéances (pas de signature)
     */
    public static final String SIGNATURE_CAISSE_CICICAM_ECHEANCE = "";
    /**
     * Signature caisse compensation CCJU
     */
    public static final String SIGNATURE_CAISSE_COMP_CCJU = "al.decision.signature.ccju.caisseCompens";
    /**
     * Signature de la caisse CVCI
     */
    public static final String SIGNATURE_CAISSE_CVCI = "al.decision.signature.cvci";
    /**
     * signature de la FPV
     */
    public static final String SIGNATURE_CAISSE_FPV = "al.decision.signature.fpv";

    /**
     * signature de la caisse FVE
     */

    public static final String SIGNATURE_CAISSE_FVE = "al.decision.signature.fve";

    /**
     * signature caisse H510
     */
    public static final String SIGNATURE_CAISSE_H510 = "al.decision.signature.h510";

    /**
     * signature caisse H513
     */

    public static final String SIGNATURE_CAISSE_H513 = "al.decision.signature.h513";

    /**
     * signature caisse H514
     */

    public static final String SIGNATURE_CAISSE_H514 = "al.decision.signature.h514";
    /**
     * signature caisse H515
     */
    public static final String SIGNATURE_CAISSE_H515 = "al.decision.signature.h515";
    /**
     * signature caisse H517
     */
    public static final String SIGNATURE_CAISSE_H517 = "al.decision.signature.h517";
    /**
     * signature caisse H51X
     */
    public static final String SIGNATURE_CAISSE_H51X = "al.decision.signature.h5110";
    /**
     * signature caisse BMS
     */
    public static final String SIGNATURE_CAISSE_BMS = "al.decision.signature.bms";

    /**
     * statutDossier à vide
     */

    public static final String STATUT_DOSSIER_VIDE = "";
    /**
     * mail de l'utilisateur
     */
    public static final String USER_MAIL = "USER_MAIL";

    /**
     * nom et prénom de l'utilisateur
     */
    public static final String USER_NAME = "USER_NAME";

    /**
     * téléphone de l'utilisateur
     */
    public static final String USER_PHONE = "USER_PHONE";

    /**
     * visa de l'utilisateur
     */
    public static final String USER_VISA = "USER_VISA";

}
