package ch.globaz.al.business.constantes;

/**
 * Codes système liés aux processus / traitements
 * 
 * @author gmo
 * 
 */
public interface ALCSProcessus {
    /**
     * Etat du processus / traitement en attente
     */
    public static final String ETAT_ATTENTE = "61300005";
    /**
     * Etat du processus / traitement en cours
     */
    public static final String ETAT_ENCOURS = "61300004";
    /**
     * Etat du processus / traitement erreur
     */
    public static final String ETAT_ERREUR = "61300003";
    /**
     * Etat du processus / traitement ouvert
     */
    public static final String ETAT_OUVERT = "61300001";

    /**
     * Etat du processus / traitement terminé
     */
    public static final String ETAT_TERMINE = "61300002";

    /**
     * CS : groupe "état processus"
     * 
     * @see ALCSProcessus#ETAT_ERREUR
     * @see ALCSProcessus#ETAT_OUVERT
     * @see ALCSProcessus#ETAT_TERMINE
     */
    public static final String GROUP_ETAT_PROCESSUS = "60300000";

    /**
     * CS : groupe "nom des processus"
     */

    public static final String GROUP_NAME_PROCESSUS_TRAITEMENT = "60290000";
    /**
     * CS : groupe "nom des template de configuration processus"
     */
    public static final String GROUP_NAME_TEMPLATE_CONFIG = "60400000";
    /** CS : Nom du processus de compensation */
    public static final String NAME_PROCESSUS_COMPENSATION = "61290001";
    /** CS : Nom du processus de compensation (paritaires) */
    public static final String NAME_PROCESSUS_COMPENSATION_PAR = "61290002";

    /** CS : Nom du processus de compensation (personnel) */
    public static final String NAME_PROCESSUS_COMPENSATION_PERS = "61290003";
    /** CS: Nom du processus de compensation (avec récap provisoire) */
    public static final String NAME_PROCESSUS_COMPENSATION_RECAP_PROV = "61290015";

    /** CS : Nom du processus de paiement direct avec génération fictive */
    public static final String NAME_PROCESSUS_DIRECT_GENERATION_FICTIVE = "61290023";

    /** CS : Nom du processus de génération fictive */
    public static final String NAME_PROCESSUS_DIRECT_GENERATION_FICTIVE_RECAP = "61290025";
    /**
     * CS : Nom du processus de paiements directs génération globale seul (horlogères)
     */
    public static final String NAME_PROCESSUS_DIRECT_GENERATION_GLOBALE = "61290019";
    /** CS : Nom du processus de gestion des échéances */

    public static final String NAME_PROCESSUS_ECHEANCES = "61290005";
    /**
     * CS : Nom du processus de compensation génération globale seul. (horlogères)
     */
    public static final String NAME_PROCESSUS_FACTURATION_HORLO = "61290018";
    public static final String NAME_PROCESSUS_FACTURATION_HORLO_PAR = "61290020";
    public static final String NAME_PROCESSUS_FACTURATION_HORLO_PERS = "61290021";
    /** CS : Nom du processus de paiement direct */
    public static final String NAME_PROCESSUS_PAIEMENT_DIRECT = "61290004";
    /** CS: Nom du processus de paiement directs (avec récap provisoire) */
    public static final String NAME_PROCESSUS_PAIEMENT_DIRECT_RECAP_PROV = "61290017";
    /** CS : Nom du processus de paiement direct (sans récap) */
    public static final String NAME_PROCESSUS_PAIEMENT_DIRECT_SANS_RECAP = "61290022";
    /** CS : Nom de la template AGLS */
    public static final String NAME_TEMPLATE_AGLS = "61400006";
    /** CS : Nom de la template CCVD */
    public static final String NAME_TEMPLATE_CCVD = "61400004";
    /** CS : Nom de la template CICI */
    public static final String NAME_TEMPLATE_CICI = "61400008";
    /** CS : Nom de la template CVCI */
    public static final String NAME_TEMPLATE_CVCI = "61400002";
    /** CS : Nom de la template par défaut */
    public static final String NAME_TEMPLATE_DEFAULT = "61400001";
    /** CS : Nom de la template FPV */
    public static final String NAME_TEMPLATE_FPV = "61400007";
    /** CS : Nom de la template h51X */
    public static final String NAME_TEMPLATE_H51X = "61400005";
    /** CS : Nom de la template Horlo */
    public static final String NAME_TEMPLATE_HORLO = "61400003";
    /**
     * Compensation des prestations
     */
    public static final String NAME_TRAITEMENT_COMPENSATION = "61290014";
    /** CS : Nom du traitement de génération fictive */
    public static final String NAME_TRAITEMENT_GENERATION_FICTIVE = "61290024";

    /** CS : Nom du traitement de génération globale */
    public static final String NAME_TRAITEMENT_GENERATION_GLOBALE = "61290006";
    /** CS : Nom du traitement d'impression des déclarations de versement */
    public static final String NAME_TRAITEMENT_IMPRESSION_DECLARATION_VERSEMENT = null;
    /** CS : Nom du traitement d'impression des échéances */
    public static final String NAME_TRAITEMENT_IMPRESSION_ECHEANCES = "61290011";
    /** CS : Nom du traitement d'impression des récaps */
    public static final String NAME_TRAITEMENT_IMPRESSION_RECAP = "61290008";
    /** CS : Nom du traitement d'impression des récaps provisoires */
    public static final String NAME_TRAITEMENT_IMPRESSION_RECAP_PROV = "61290016";

    /**
     * Préparation de la compensation des prestations
     */
    public static final String NAME_TRAITEMENT_PREPARATION_COMPENSATION = "61290009";
    /**
     * Préparation du versement des prestations
     */
    public static final String NAME_TRAITEMENT_PREPARATION_VERSEMENT_DIRECTS = "61290012";
    /**
     * Simulation de la compensation des prestations
     */
    public static final String NAME_TRAITEMENT_SIMULATION_COMPENSATION = "61290007";

    /**
     * Simulation du versement des prestations
     */
    public static final String NAME_TRAITEMENT_SIMULATION_VERSEMENT_DIRECTS = "61290010";
    /**
     * Traitement définitif du versement des prestations
     */
    public static final String NAME_TRAITEMENT_VERSEMENT_DIRECTS = "61290013";
}
