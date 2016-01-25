package ch.globaz.al.business.constantes;

/**
 * Codes syst�mes et constante utile pour la g�n�ration de prestations
 * 
 * @author jts
 */
public interface ALConstPrestations {

    /**
     * Type de g�n�ration. Il est utiliser par les contexts de g�n�ration pour conna�tre le mode de fonctionnement en
     * cours d'ex�cution. Il permet par exemple de conna�tre la classe "GenPrestation*" qui a �t� ex�cut�e
     * 
     * @author jts
     * 
     */
    public enum TypeGeneration {
        /**
         * Utilis� pour la g�n�ration d'une prestation d�finitive ADI ( <code>GenPrestationADI</code>)
         */
        ADI_DEFINITIF,

        /**
         * Utilis� pour la g�n�ration d'un prestation de travail ADI ( <code>GenPrestationADITemporaire</code>)
         */
        ADI_TEMPORAIRE,

        /**
         * Cas standard
         */
        STANDARD
    }

    public enum TypeMontantForce {
        /** Aucun montant forc� */
        MONTANT_FORCE_AUCUN,
        /** Montant forc� au niveau du dossier */
        MONTANT_FORCE_DOSSIER,
        /** Montant forc� au moment de la g�n�ration */
        MONTANT_FORCE_GEN
    }

    /**
     * Id de r�cap � utiliser pour une prestation de travail ADI
     */
    public static final String ID_RECAP_ADI_TMP = "0";

    /**
     * Nom de la d�finition de recherche pour la totalit� des dossiers (directs et indirects)
     */
    public static final String SEARCH_DEFINITION_GENERATION = "generation";
    /**
     * Nom de la d�finition de recherche pour les dossiers en paiement direct
     */
    public static final String SEARCH_DEFINITION_GENERATION_DIRECT = "generationDirect";
    /**
     * Nom de la d�finition de recherche pour les dossiers en paiement indirect
     */
    public static final String SEARCH_DEFINITION_GENERATION_INDIRECT = "generationIndirect";
    /**
     * Nom de la d�finition de recherche pour les dossiers en paiement indirect paritaire
     */
    public static final String SEARCH_DEFINITION_GENERATION_INDIRECT_PAR = "generationIndirectPar";
    /**
     * Nom de la d�finition de recherche pour les dossiers en paiement indirect personnels
     */
    public static final String SEARCH_DEFINITION_GENERATION_INDIRECT_PERS = "generationIndirectPers";
    /**
     * Constantes de tol�rance de comparaison de montant
     */
    public static final Float TOLERANCE_COMPARAISON_MONTANT = (float) 0.50;

    /**
     * traitement r�capitulatif par un num�ro de lot
     */
    public static final String TRAITEMENT_NO_LOT = "5";

    /**
     * traitement r�capitulatif par un num�ro de r�cap
     */
    public static final String TRAITEMENT_NO_RECAP = "6";

    /**
     * traitement r�capitulatif par id processus
     */
    public static final String TRAITEMENT_NUM_PROCESSUS = "7";

    /**
     * Code pour le type d'assurance ind�termin�e pour les r�caps
     */
    public static final String TYPE_ASS_INDETERMINE = "99999999";
    /**
     * G�n�ration/compensation pour les cot. par.
     */
    public static final String TYPE_COT_PAR = "2";

    /**
     * G�n�ration/compensation pour les cot. pers.
     */
    public static final String TYPE_COT_PERS = "3";

    /**
     * G�n�ration/paiement directs
     */
    public static final String TYPE_DIRECT = "4";
    /**
     * G�n�ration/compensation group�es (cot. pers. et cot. par)
     */
    public static final String TYPE_INDIRECT_GROUPE = "1";

    public static final String PREST_GEN_MAX_THREADS = "prestations.generation.maxThreads";
}
