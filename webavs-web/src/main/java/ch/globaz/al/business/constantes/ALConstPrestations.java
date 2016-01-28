package ch.globaz.al.business.constantes;

/**
 * Codes systèmes et constante utile pour la génération de prestations
 * 
 * @author jts
 */
public interface ALConstPrestations {

    /**
     * Type de génération. Il est utiliser par les contexts de génération pour connaître le mode de fonctionnement en
     * cours d'exécution. Il permet par exemple de connaître la classe "GenPrestation*" qui a été exécutée
     * 
     * @author jts
     * 
     */
    public enum TypeGeneration {
        /**
         * Utilisé pour la génération d'une prestation définitive ADI ( <code>GenPrestationADI</code>)
         */
        ADI_DEFINITIF,

        /**
         * Utilisé pour la génération d'un prestation de travail ADI ( <code>GenPrestationADITemporaire</code>)
         */
        ADI_TEMPORAIRE,

        /**
         * Cas standard
         */
        STANDARD
    }

    public enum TypeMontantForce {
        /** Aucun montant forcé */
        MONTANT_FORCE_AUCUN,
        /** Montant forcé au niveau du dossier */
        MONTANT_FORCE_DOSSIER,
        /** Montant forcé au moment de la génération */
        MONTANT_FORCE_GEN
    }

    /**
     * Id de récap à utiliser pour une prestation de travail ADI
     */
    public static final String ID_RECAP_ADI_TMP = "0";

    /**
     * Nom de la définition de recherche pour la totalité des dossiers (directs et indirects)
     */
    public static final String SEARCH_DEFINITION_GENERATION = "generation";
    /**
     * Nom de la définition de recherche pour les dossiers en paiement direct
     */
    public static final String SEARCH_DEFINITION_GENERATION_DIRECT = "generationDirect";
    /**
     * Nom de la définition de recherche pour les dossiers en paiement indirect
     */
    public static final String SEARCH_DEFINITION_GENERATION_INDIRECT = "generationIndirect";
    /**
     * Nom de la définition de recherche pour les dossiers en paiement indirect paritaire
     */
    public static final String SEARCH_DEFINITION_GENERATION_INDIRECT_PAR = "generationIndirectPar";
    /**
     * Nom de la définition de recherche pour les dossiers en paiement indirect personnels
     */
    public static final String SEARCH_DEFINITION_GENERATION_INDIRECT_PERS = "generationIndirectPers";
    /**
     * Constantes de tolérance de comparaison de montant
     */
    public static final Float TOLERANCE_COMPARAISON_MONTANT = (float) 0.50;

    /**
     * traitement récapitulatif par un numéro de lot
     */
    public static final String TRAITEMENT_NO_LOT = "5";

    /**
     * traitement récapitulatif par un numéro de récap
     */
    public static final String TRAITEMENT_NO_RECAP = "6";

    /**
     * traitement récapitulatif par id processus
     */
    public static final String TRAITEMENT_NUM_PROCESSUS = "7";

    /**
     * Code pour le type d'assurance indéterminée pour les récaps
     */
    public static final String TYPE_ASS_INDETERMINE = "99999999";
    /**
     * Génération/compensation pour les cot. par.
     */
    public static final String TYPE_COT_PAR = "2";

    /**
     * Génération/compensation pour les cot. pers.
     */
    public static final String TYPE_COT_PERS = "3";

    /**
     * Génération/paiement directs
     */
    public static final String TYPE_DIRECT = "4";
    /**
     * Génération/compensation groupées (cot. pers. et cot. par)
     */
    public static final String TYPE_INDIRECT_GROUPE = "1";

    public static final String PREST_GEN_MAX_THREADS = "prestations.generation.maxThreads";
}
