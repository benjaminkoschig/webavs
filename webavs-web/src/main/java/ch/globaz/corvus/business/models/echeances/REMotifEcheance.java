package ch.globaz.corvus.business.models.echeances;

/**
 * Motifs contenu dans les r�ponses ({@link REReponseModuleAnalyseEcheance}) des modules d'analyse d'�ch�ances (
 * {@link REModuleAnalyseEcheance}) servant � d�finir pour quelle raison une �ch�ance se trouve dans la liste imprim�e.<br/>
 * <br/>
 * Contient un ID de label correspondant � une entr�e dans le fichier de propri�t�s <code>CORVUSLabel.properties</code><br/>
 * <br/>
 * Certains motifs sont internes au fonctionnement des modules, et par cons�quent n'auront pas d'ID de label (
 * <code>null</code>).<br/>
 * Ils seront reconnaissables au pr�fixe <code>Interne_</code> pr�c�dant leur nom.
 * 
 * @author PBA
 */
public enum REMotifEcheance {

    /**
     * Ajournement standard : Age vieillesse (selon le sexe) + 5 ans sans tenir compte de la date d'�ch�ance dans la
     * rente ajourn�e
     * 
     * @see REModuleEcheanceAjournement
     */
    Ajournement("MOTIF_ECHEANCE_AJOURNEMENT"),
    /**
     * L'�ge vieillesse (selon le sexe) + 5ans a �t� d�pass�
     * 
     * @see REModuleEcheanceAjournement
     */
    AjournementDepasse("MOTIF_ECHEANCE_AJOURNEMENT_DEPASSE"),
    /**
     * Ajournement avec dur�e d�finie : Lorsque la personne est en age AVS et qu'une date de r�vocation (de
     * l'ajournement) est renseign�e dans la demande
     */
    AjournementRevocationDemandee("MOTIF_ECHEANCE_REVOCATION_DEMANDEE"),
    /**
     * Ajournement avec dur�e d�finie : Lorsque la personne est en age AVS et qu'une date de r�vocation (de
     * l'ajournement) est renseign�e dans la demande
     */
    AjournementRevocationDemandeeDepassee("MOTIF_ECHEANCE_REVOCATION_DEMANDEE_DEPASSEE"),
    /**
     * Une p�riode de certificat de vie du tiers se termine dans le mois courant
     * 
     * @see REModuleEcheanceCertificatDeVie
     */
    CertificatDeVie("MOTIF_ECHEANCE_CERTIFICAT_DE_VIE"),
    /**
     * Le conjoint du tiers est d�j� � l'�ge de vieillesse dans le mois courant, mais n'a pas de rente vieillesse en
     * cours dans le mois suivant 0 * @see REModuleEcheanceFemmeAgeAvs
     * 
     * @see REModuleEcheanceHommeAgeAvs
     */
    ConjointAgeAvsDepasse("MOTIF_ECHEANCE_CONJOINT_DEJA_AGE_AVS_SANS_RENTE"),
    /**
     * Le conjoint du tiers arrive � l'�ge de vieillesse dans le mois courant
     * 
     * @see REModuleEcheanceFemmeAgeAvs
     * @see REModuleEcheanceHommeAgeAvs
     */
    ConjointArrivantAgeAvs("MOTIF_ECHEANCE_CONJOINT_AGE_AVS"),
    /**
     * Le tiers, au b�n�fice d'une rente compl�mentaire, aura 18 ans dans le mois courant, et aucune p�riode d'�tudes
     * n'a �t� d�finie pour le mois suivant
     * 
     * @see REModuleEcheance18Ans
     */
    Echeance18ans("MOTIF_ECHEANCE_18_ANS_ETUDES_NON_DEFINIES"),
    /**
     * Le tiers, au b�n�fice d'une rente compl�mentaire pour enfant, aura 25 ans dans le mois courant
     * 
     * @see REModuleEcheance25Ans
     */
    Echeance25ans("MOTIF_ECHEANCE_25_ANS"),
    /**
     * Le tiers a d�j� 25 ans et est toujours au b�n�fice d'une rente compl�mentaire d'enfant
     * 
     * @see REModuleEcheance25Ans
     */
    Echeance25ansDepassee("MOTIF_ECHEANCE_25_ANS_DEPASSEE"),
    /**
     * Le tiers, au b�n�fice d'une rente compl�mentaire pour enfant, aura 25 ans dans le mois courant, mais la rente est
     * bloqu�e
     * 
     * @see REModuleEcheance25Ans
     */
    Echeance25ansRenteBloquee("MOTIF_ECHEANCE_25_ANS_RENTE_BLOQUEE"),
    /**
     * Le tiers a entre 18 et 24 ans, est au b�n�fice d'une rente pour enfant, mais n'a aucune p�riode d'�tude d�finie
     * (d�s ses 18 ans)
     * 
     * @see REModuleEcheanceEtude
     */
    EcheanceEtudesAucunePeriode("MOTIF_ECHEANCE_ETUDES_TERMINEES_SANS_DATE_FIN"),
    /**
     * Le tiers a entre 18 et 24 ans, est au b�n�fice d'une rente pour enfant, et a une p�riode d'�tudes finissant dans
     * le mois courant sans p�riode reprenant le mois suivant
     * 
     * @see REModuleEcheanceEtude
     */
    EcheanceFinEtudes("MOTIF_ECHEANCE_ETUDES_STANDARD"),
    /**
     * Le tiers a entre 18 et 24 ans, est au b�n�fice d'une rente pour enfant, et a une p�riode d'�tude finissant avant
     * le mois courant, et aucune p�riode d'�tude n'est en cours.<br/>
     * <br/>
     * Ajouter la date de la fin de la p�riode pour l'utilisateur
     * 
     * @see REModuleEcheanceEtude
     */
    EcheanceFinEtudesDepassees("MOTIF_ECHEANCE_ETUDES_FIN_ETUDE"),
    /**
     * La date d'�ch�ance a �t� rentr�e � la main pour cette rente
     * 
     * @see REModuleEcheancesForcees
     */
    EcheanceForcee("MOTIF_ECHEANCE_ECHEANCE_FORCEE"),
    /**
     * Le tier � plus de 18 ans et est au b�n�fice d'une rente mais sa p�riode d'�tude est �chue et la date d'�ch�ance
     * est dans le mois courant Poursuite de la formation sans attestation d��tudes, la p�riode d��tudes n�est pas mise
     * � jour dans la situation familiale, seul la date d��ch�ance de la rente accord�e est mise � jour.
     */
    EnqueteIntermediaire("MOTIF_ENQUETE_INTERMEDIAIRE"),
    /**
     * Le tiers, de sexe f�minin, est d�j� � l'�ge AVS dans le mois courant, et est toujours au b�n�fice d'une rente
     * anticip�e
     */
    FemmeAgeAvsAnticipationDepassee("MOTIF_ECHEANCE_FEMME_AGE_AVS_ANTICIPATION_DEPASSEE"),
    /**
     * Le tiers, de sexe f�minin, est d�j� � l'�ge AVS dans le mois courant, mais n'a pas de rente vieillesse en cours
     * 
     * @see REModuleEcheanceFemmeAgeAvs
     */
    FemmeAgeAvsDepasse("MOTIF_ECHEANCE_FEMME_AGE_AVS_DEPASSE"),
    /**
     * Le tiers, de sexe f�minin, arrive � l'�ge de vieillesse dans le mois courant
     * 
     * @see REModuleEcheanceFemmeAgeAvs
     */
    FemmeArrivantAgeAvs("MOTIF_ECHEANCE_FEMME_AGE_AVS"),
    /**
     * Le tiers, de sexe f�minin, arrive � l'�ge de vieillesse dans le mois courant et est au b�n�fice d'une rente API
     * 
     * @see REModuleEcheanceFemmeAgeAvs
     */
    FemmeArrivantAgeAvsAvecApiAi("MOTIF_ECHEANCE_FEMME_AGE_AVS_AVEC_API"),
    /**
     * Le tiers, de sexe f�minin, arrive � l'�ge de vieillesse dans le mois courant et est au b�n�fice d'une rente
     * anticip�e
     * 
     * @see REModuleEcheanceFemmeAgeAvs
     */
    FemmeArrivantAgeAvsRenteAnticipee("MOTIF_ECHEANCE_FEMME_AGE_AVS_RENTE_ANTICIPEE"),
    /**
     * Le tiers, de sexe masculin, est d�j� � l'�ge AVS dans le mois courant, et est toujours au b�n�fice d'une rente
     * anticip�e
     */
    HommeAgeAvsAnticipationDepassee("MOTIF_ECHEANCE_HOMME_AGE_AVS_ANTICIPATION_DEPASSEE"),
    /**
     * Le tiers, de sexe masculin, est d�j� � l'�ge AVS dans le mois courant, mais n'a pas de rente vieillesse en cours
     * 
     * @see REModuleEcheanceHommeAgeAvs
     */
    HommeAgeAvsDepasse("MOTIF_ECHEANCE_HOMME_AGE_AVS_DEPASSE"),
    /**
     * Le tiers, de sexe masculin, arrive � l'�ge de vieillesse dans le mois courant
     * 
     * @see REModuleEcheanceHommeAgeAvs
     */
    HommeArrivantAgeAvs("MOTIF_ECHEANCE_HOMME_AGE_AVS"),
    /**
     * Le tiers, de sexe masculin, arrive � l'�ge de vieillesse dans le mois courant et est au b�n�fice d'une rente API
     * 
     * @see REModuleEcheanceHommeAgeAvs
     */
    HommeArrivantAgeAvsAvecApiAi("MOTIF_ECHEANCE_HOMME_AGE_AVS_AVEC_API"),
    /**
     * Le tiers, de sexe masculin, arrive � l'�ge de vieillesse dans le mois courant et est au b�n�fice d'une rente
     * anticip�e
     * 
     * @see REModuleEcheanceHommeAgeAvs
     */
    HommeArrivantAgeAvsRenteAnticipee("MOTIF_ECHEANCE_HOMME_AGE_AVS_RENTE_ANTICIPEE"),
    /**
     * L'�ge voulu est atteint dans le mois courant
     */
    Interne_AgeVouluDansMoisCourant(null),
    /**
     * L'�ge voulu est d�j� atteint avant le mois courant
     */
    Interne_AgeVouluDepasseDansMoisCourant(null),
    /**
     * Le tiers n'as pas encore atteint, dans le mois courant, l'�ge voulu
     * 
     * @see REModuleEcheanceAgeAvs
     */
    Interne_AgeVouluPasEncoreAtteint(null),
    /**
     * L'ajournement n'est pas encore termin�
     * 
     * @see REModuleEcheanceAjournement
     * @see REModuleAnalyseEcheanceUtils#isRenteAjournee(globaz.corvus.db.echeances.RERenteJoinDemandeEcheance, String,
     *      String, String)
     */
    Interne_AjournementEnCours(null),
    /**
     * Motif par d�faut pour une r�ponse
     */
    Interne_NonDefini(null),
    /**
     * Le tiers est d�j� b�n�ficiaire d'une rente de vieillesse
     */
    Interne_RenteVieilesseDejaEnCours(null),
    /**
     * Le tier n'est pas du bon sexe pour le module
     * 
     * @see REModuleEcheanceAgeAvs
     */
    Interne_SexeNonPrisEnCharge(null),
    /**
     * Le tiers, de sexe masculin, est au b�n�fice d'une rente de veuf.<br/>
     * Son dernier enfant (le plus jeune), b�n�ficiant d'une compl�mentaire de la rente de veuf, aura 18 ans dans le
     * mois courant
     * 
     * @see REModuleEcheanceRenteDeVeuf
     */
    RenteDeVeuf("MOTIF_ECHEANCE_RENTE_DE_VEUF"),
    /**
     * Le tiers, de sexe masculin, est au b�n�fice d'une rente de veuf alors qu'il n'a pas/plus d'enfants � charge
     */
    RenteDeVeufSansEnfant("MOTIF_ECHEANCE_RENTE_DE_VEUF_SANS_ENFANTS"),

    /**
     * Tous les b�n�ficiaires ayant un enfant recueilli gratuitement ou un enfant recueilli gratuitement par le conjoint, et dont
     * la date de d�but de prestation est �gal au mois en cours
     * ou qu'il y a 12 mois entre la date du jour et la date de d�but de la derni�re prestation.
     */
    EcheanceEnfantRecueilliGratuitement("MOTIF_ECHEANCE_ENFANT_RECUEILLI_GRATUITEMENT");

    private String idLabelMotif;

    private REMotifEcheance(String idLabelMotif) {
        this.idLabelMotif = idLabelMotif;
    }

    public String getIdLabelMotif() {
        return idLabelMotif;
    }
}
