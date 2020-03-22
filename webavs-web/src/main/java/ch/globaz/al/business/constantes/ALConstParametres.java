package ch.globaz.al.business.constantes;

/**
 * Constantes li�es au param�tres de l'application (table ALPARAM). La valeur de chaque constante correspond au nom du
 * param�tre dans la table et non � sa valeur r�elle
 * 
 * @author jts
 * 
 */
public interface ALConstParametres {

    /**
     * Indique si les prestations suisses sont g�n�r�es automatiquement ou non dans le calcul ADI
     */
    public static final String ADI_AUTO_PRESTATIONS_CH = "ADI_AUTO_PRESTATIONS_CH";
    /**
     * parm�tre indiquant s'il faut afficher ou non le champ attestation � l'allocataire
     */

    public static final String AFFICHAGE_ATTESTATION_ALLOC = "declarationVersementAlloc";
    /**
     * Nom de l'application
     */
    public static final String APPNAME = "WEBAF";
    /**
     * param�tre indiquant la valeur par d�faut � mettre dans le champ attestation � l'allocataire
     */

    public static final String ATTEST_ALLOC_VAL = "declarationVersementAllocValeur";

    /**
     * Cat�gorie de tarif de la caisse (code syst�me)
     */
    public static final String CAT_TARIF_CAISSE = "CATARCAIS";

    /**
     * Nom du param�tre indiquant s'il faut v�rifier que l'allocataire PSA touche des PC
     */
    public static final String CHECK_PC_FAMILLE = "CHECK_PC_FAMILLE";
    /**
     * Nom du param�tre indiquant le / les �tats dossier ne bloquant pas la g�n�ration globale
     */
    public static final String DOSSIER_ETAT_GEN_GLOBALE = "DOSSIER_ETAT_GEN_GLOBALE";
    /**
     * Nom du param�tre indiquant le / les �tats dossier engendrant une journalisation lors du changement d'�tat
     */
    public static final String DOSSIER_ETAT_JOURNALISE = "DOSSIER_ETAT_JOURNALISE";
    /**
     * Nom du param�tre indiquant le / les �tats dossier engendrant la gestion des annonces RAFAM lors de mutation au
     * sein d'un dossier
     */
    public static final String DOSSIER_ETAT_RAFAM = "DOSSIER_ETAT_RAFAM";
    /**
     * Nom du param�tre indiquant si la vue v2 de la liste des droits est activ�e
     */
    public static final String DROITS_VIEW_EXTENDED = "DROITS_VIEW_EXTENDED";

    /**
     * Nom du param�tre indiquant si le suppl�ment FNB est activ�
     */
    public static final String FNB_IS_ENABLED = "FNB_IS_ENABLED";
    /**
     * Indique si les documents impliquant les types d'activit�s d'allocataires doivent �tre imprim�s s�par�ment ou
     * group�s
     */
    public static final String IMPRIMER_ACTIVITES_SEP = "IMPRIMER_ACTIVITES_SEP";
    /**
     * Incr�ment pour la g�n�ration de num�ros de facture
     */
    public static final String INCREMENT_FACTURE = "INCREMENT_FACTURE";
    public static final String LOCK_DOSSIER_AFFILIE = "LOCK_DOSSIER_AFFILIE";
    /**
     * Mode de calcul � utiliser
     */
    public static final String MODE_CALCUL = "CALCMODE";
    /**
     * Mode caisse AF (utilisation du tarif de la caisse dans tous les cas)
     */
    public static final String MODE_CALCUL_CAISSEAF = "CAISSEAF";

    /**
     * Mode Canton de l'employeur (utilisation du tarif de la caisse dans tous les cas)
     */
    public static final String MODE_CALCUL_CANTEMPL = "CANTEMPL";
    /**
     * Mode comparaison de droit (comparaison des tarif)
     */
    public static final String MODE_CALCUL_COMPDROIT = "COMPDROIT";
    /**
     * Mode paiement direct par d�faut
     */
    public static final String MODE_PAIEMENT_DIRECT = "PMTDIRECT";
    /**
     * Nombre d'heure dans un mois. Utilis� pour la g�n�ration de prestation unitaire � l'heure
     */
    public static final String NBR_HEURES_PAR_MOIS = "NBHEURMOIS";
    /**
     * Nombre de jours dans un mois. Utilis� pour la g�n�ration de prestation unitaire au jour
     */
    public static final String NBR_JOURS_PAR_MOIS = "NBJOURMOIS";
    /**
     * Nom de la caisse
     */
    public static final String NOM_CAISSE = "NOMCAISSE";
    /**
     * Num�ro OFAS de la caisse
     */
    public static final String NUMERO_CAISSE = "caisse.no";

    /**
     * Num�ro de succursale de la caisse
     */
    public static final String NUMERO_SUCCURSALE_CAISSE = "caisse.no.succursale";
    /**
     * Pr�cision � utilis� pour le calcul d'un dossier � l'heure
     */
    public static final String PRECISION_UNITE_HEURE = "PRECISION_UNITE_HEURE";
    /**
     * Pr�cision � utilis� pour le calcul d'un dossier au jour
     */
    public static final String PRECISION_UNITE_JOUR = "PRECISION_UNITE_JOUR";
    /**
     * Pr�cision � utilis� pour le calcul d'un dossier au mois
     */
    public static final String PRECISION_UNITE_MOIS = "PRECISION_UNITE_MOIS";
    /**
     * protocole liste affili� en CSV pour la compensation
     */
    public static final String PROTOCOLE_CSV_COMPENSATION_LISTE_AFFILIE = "protocoleCSVCompensationListeAffilie";
    /**
     * Param�tre indiquant s'il faut g�n�rer un protocole CSV lors de la simulation des paiement direct pour les
     * prestation d�passant un montant sup �
     */
    public static final String PROTOCOLE_CSV_SIMU_PAIEMENT_DIRECT = "protocoleCSVSimuPaiementDirect";
    /**
     * param�tre si g�n�rer protocole csv listeAllocataire pour g�n�er la simulation des paiements direct
     */
    public static final String PROTOCOLE_CSV_SIMU_PAIEMENT_DIRECT_DETAIL_ALLOC = "protocoleCSVSimuPaiementDirectListeAlloc";
    /**
     * Mode d'�dition des annonces RAFAM
     */
    public static final String RAFAM_ADMIN_EDITION = "RAFAM_ADMIN_EDITION";
    /**
     * Nom du param�tre indiquant si le NSS doit �tre reformatt� sur le recap CSV
     */
    public static final String RECAP_FORMAT_NSS = "al.recapFormatNss";
    /**
     * param�tre pour affichages de toutes les annonces rafam d'an droit ou seulement
     */
    public static final String RAFAM_AFFICHAGE_ANNONCE = "AF_ANNONCES_RAFAM_LIST";
    /**
     * Mode d'envoi des annonces RAFAM
     */
    public static final String RAFAM_ENVOI_AUTO = "RAFAM_ENVOI_AUTO";
    /**
     * Taux de change euros pour le calcul ADI
     */
    public static final String TAUX_EURO = "TAUX_EURO";
    /**
     * Nom du param�tre pour M�thode de r�cup�ration du taux selon la date
     */
    public static final String TAUX_METHOD_DATE = "TAUX_METHOD_DATE";
    /**
     * Valeur pour le param�tre TAUX_METHOD_DATE
     */
    public static final String TAUX_METHOD_JOUR = "JOUR";

    /**
     * Valeur pour le param�tre TAUX_METHOD_DATE
     */
    public static final String TAUX_METHOD_PERIODEAF = "PERIODAF";

    /**
     * Nom du param�tre pour le template de configuration des processus AF
     */
    public static final String TEMPLATE_PROCESSUS = "TMPPROC";

    /**
     * Nom du param�tre indiquant si Tucana est Actif
     */
    public static final String TUCANA_IS_ENABLED = "TUCANA_IS_ENABLED";

    /** Param�tre d�finissant le type de copie pour les non actifs */
    public static final String TYPE_COPIE_DECISION_NONACTIF = "typeCopieDecisionNonActif";

    public static final String WARN_RETRO_BEFORE = "WARN_RETRO_BEFORE";

    /**
     * Age et date du d�but de droit de formation
     */
    public static final String DEBUT_DROIT_FORMATION = "DEBUT_DROIT_FORMATION";
}
