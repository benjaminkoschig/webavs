package ch.globaz.corvus.business.models.echeances;

/**
 * Motifs contenu dans les réponses ({@link REReponseModuleAnalyseEcheance}) des modules d'analyse d'échéances (
 * {@link REModuleAnalyseEcheance}) servant à définir pour quelle raison une échéance se trouve dans la liste imprimée.<br/>
 * <br/>
 * Contient un ID de label correspondant à une entrée dans le fichier de propriétés <code>CORVUSLabel.properties</code><br/>
 * <br/>
 * Certains motifs sont internes au fonctionnement des modules, et par conséquent n'auront pas d'ID de label (
 * <code>null</code>).<br/>
 * Ils seront reconnaissables au préfixe <code>Interne_</code> précédant leur nom.
 * 
 * @author PBA
 */
public enum REMotifEcheance {

    /**
     * Ajournement standard : Age vieillesse (selon le sexe) + 5 ans sans tenir compte de la date d'échéance dans la
     * rente ajournée
     * 
     * @see REModuleEcheanceAjournement
     */
    Ajournement("MOTIF_ECHEANCE_AJOURNEMENT"),
    /**
     * L'âge vieillesse (selon le sexe) + 5ans a été dépassé
     * 
     * @see REModuleEcheanceAjournement
     */
    AjournementDepasse("MOTIF_ECHEANCE_AJOURNEMENT_DEPASSE"),
    /**
     * Ajournement avec durée définie : Lorsque la personne est en age AVS et qu'une date de révocation (de
     * l'ajournement) est renseignée dans la demande
     */
    AjournementRevocationDemandee("MOTIF_ECHEANCE_REVOCATION_DEMANDEE"),
    /**
     * Ajournement avec durée définie : Lorsque la personne est en age AVS et qu'une date de révocation (de
     * l'ajournement) est renseignée dans la demande
     */
    AjournementRevocationDemandeeDepassee("MOTIF_ECHEANCE_REVOCATION_DEMANDEE_DEPASSEE"),
    /**
     * Une période de certificat de vie du tiers se termine dans le mois courant
     * 
     * @see REModuleEcheanceCertificatDeVie
     */
    CertificatDeVie("MOTIF_ECHEANCE_CERTIFICAT_DE_VIE"),
    /**
     * Le conjoint du tiers est déjà à l'âge de vieillesse dans le mois courant, mais n'a pas de rente vieillesse en
     * cours dans le mois suivant 0 * @see REModuleEcheanceFemmeAgeAvs
     * 
     * @see REModuleEcheanceHommeAgeAvs
     */
    ConjointAgeAvsDepasse("MOTIF_ECHEANCE_CONJOINT_DEJA_AGE_AVS_SANS_RENTE"),
    /**
     * Le conjoint du tiers arrive à l'âge de vieillesse dans le mois courant
     * 
     * @see REModuleEcheanceFemmeAgeAvs
     * @see REModuleEcheanceHommeAgeAvs
     */
    ConjointArrivantAgeAvs("MOTIF_ECHEANCE_CONJOINT_AGE_AVS"),
    /**
     * Le tiers, au bénéfice d'une rente complémentaire, aura 18 ans dans le mois courant, et aucune période d'études
     * n'a été définie pour le mois suivant
     * 
     * @see REModuleEcheance18Ans
     */
    Echeance18ans("MOTIF_ECHEANCE_18_ANS_ETUDES_NON_DEFINIES"),
    /**
     * Le tiers, au bénéfice d'une rente complémentaire pour enfant, aura 25 ans dans le mois courant
     * 
     * @see REModuleEcheance25Ans
     */
    Echeance25ans("MOTIF_ECHEANCE_25_ANS"),
    /**
     * Le tiers a déjà 25 ans et est toujours au bénéfice d'une rente complémentaire d'enfant
     * 
     * @see REModuleEcheance25Ans
     */
    Echeance25ansDepassee("MOTIF_ECHEANCE_25_ANS_DEPASSEE"),
    /**
     * Le tiers, au bénéfice d'une rente complémentaire pour enfant, aura 25 ans dans le mois courant, mais la rente est
     * bloquée
     * 
     * @see REModuleEcheance25Ans
     */
    Echeance25ansRenteBloquee("MOTIF_ECHEANCE_25_ANS_RENTE_BLOQUEE"),
    /**
     * Le tiers a entre 18 et 24 ans, est au bénéfice d'une rente pour enfant, mais n'a aucune période d'étude définie
     * (dès ses 18 ans)
     * 
     * @see REModuleEcheanceEtude
     */
    EcheanceEtudesAucunePeriode("MOTIF_ECHEANCE_ETUDES_TERMINEES_SANS_DATE_FIN"),
    /**
     * Le tiers a entre 18 et 24 ans, est au bénéfice d'une rente pour enfant, et a une période d'études finissant dans
     * le mois courant sans période reprenant le mois suivant
     * 
     * @see REModuleEcheanceEtude
     */
    EcheanceFinEtudes("MOTIF_ECHEANCE_ETUDES_STANDARD"),
    /**
     * Le tiers a entre 18 et 24 ans, est au bénéfice d'une rente pour enfant, et a une période d'étude finissant avant
     * le mois courant, et aucune période d'étude n'est en cours.<br/>
     * <br/>
     * Ajouter la date de la fin de la période pour l'utilisateur
     * 
     * @see REModuleEcheanceEtude
     */
    EcheanceFinEtudesDepassees("MOTIF_ECHEANCE_ETUDES_FIN_ETUDE"),
    /**
     * La date d'échéance a été rentrée à la main pour cette rente
     * 
     * @see REModuleEcheancesForcees
     */
    EcheanceForcee("MOTIF_ECHEANCE_ECHEANCE_FORCEE"),
    /**
     * Le tier à plus de 18 ans et est au bénéfice d'une rente mais sa période d'étude est échue et la date d'échéance
     * est dans le mois courant Poursuite de la formation sans attestation d’études, la période d’études n’est pas mise
     * à jour dans la situation familiale, seul la date d’échéance de la rente accordée est mise à jour.
     */
    EnqueteIntermediaire("MOTIF_ENQUETE_INTERMEDIAIRE"),
    /**
     * Le tiers, de sexe féminin, est déjà à l'âge AVS dans le mois courant, et est toujours au bénéfice d'une rente
     * anticipée
     */
    FemmeAgeAvsAnticipationDepassee("MOTIF_ECHEANCE_FEMME_AGE_AVS_ANTICIPATION_DEPASSEE"),
    /**
     * Le tiers, de sexe féminin, est déjà à l'âge AVS dans le mois courant, mais n'a pas de rente vieillesse en cours
     * 
     * @see REModuleEcheanceFemmeAgeAvs
     */
    FemmeAgeAvsDepasse("MOTIF_ECHEANCE_FEMME_AGE_AVS_DEPASSE"),
    /**
     * Le tiers, de sexe féminin, arrive à l'âge de vieillesse dans le mois courant
     * 
     * @see REModuleEcheanceFemmeAgeAvs
     */
    FemmeArrivantAgeAvs("MOTIF_ECHEANCE_FEMME_AGE_AVS"),
    /**
     * Le tiers, de sexe féminin, arrive à l'âge de vieillesse dans le mois courant et est au bénéfice d'une rente API
     * 
     * @see REModuleEcheanceFemmeAgeAvs
     */
    FemmeArrivantAgeAvsAvecApiAi("MOTIF_ECHEANCE_FEMME_AGE_AVS_AVEC_API"),
    /**
     * Le tiers, de sexe féminin, arrive à l'âge de vieillesse dans le mois courant et est au bénéfice d'une rente
     * anticipée
     * 
     * @see REModuleEcheanceFemmeAgeAvs
     */
    FemmeArrivantAgeAvsRenteAnticipee("MOTIF_ECHEANCE_FEMME_AGE_AVS_RENTE_ANTICIPEE"),
    /**
     * Le tiers, de sexe masculin, est déjà à l'âge AVS dans le mois courant, et est toujours au bénéfice d'une rente
     * anticipée
     */
    HommeAgeAvsAnticipationDepassee("MOTIF_ECHEANCE_HOMME_AGE_AVS_ANTICIPATION_DEPASSEE"),
    /**
     * Le tiers, de sexe masculin, est déjà à l'âge AVS dans le mois courant, mais n'a pas de rente vieillesse en cours
     * 
     * @see REModuleEcheanceHommeAgeAvs
     */
    HommeAgeAvsDepasse("MOTIF_ECHEANCE_HOMME_AGE_AVS_DEPASSE"),
    /**
     * Le tiers, de sexe masculin, arrive à l'âge de vieillesse dans le mois courant
     * 
     * @see REModuleEcheanceHommeAgeAvs
     */
    HommeArrivantAgeAvs("MOTIF_ECHEANCE_HOMME_AGE_AVS"),
    /**
     * Le tiers, de sexe masculin, arrive à l'âge de vieillesse dans le mois courant et est au bénéfice d'une rente API
     * 
     * @see REModuleEcheanceHommeAgeAvs
     */
    HommeArrivantAgeAvsAvecApiAi("MOTIF_ECHEANCE_HOMME_AGE_AVS_AVEC_API"),
    /**
     * Le tiers, de sexe masculin, arrive à l'âge de vieillesse dans le mois courant et est au bénéfice d'une rente
     * anticipée
     * 
     * @see REModuleEcheanceHommeAgeAvs
     */
    HommeArrivantAgeAvsRenteAnticipee("MOTIF_ECHEANCE_HOMME_AGE_AVS_RENTE_ANTICIPEE"),
    /**
     * L'âge voulu est atteint dans le mois courant
     */
    Interne_AgeVouluDansMoisCourant(null),
    /**
     * L'âge voulu est déjà atteint avant le mois courant
     */
    Interne_AgeVouluDepasseDansMoisCourant(null),
    /**
     * Le tiers n'as pas encore atteint, dans le mois courant, l'âge voulu
     * 
     * @see REModuleEcheanceAgeAvs
     */
    Interne_AgeVouluPasEncoreAtteint(null),
    /**
     * L'ajournement n'est pas encore terminé
     * 
     * @see REModuleEcheanceAjournement
     * @see REModuleAnalyseEcheanceUtils#isRenteAjournee(globaz.corvus.db.echeances.RERenteJoinDemandeEcheance, String,
     *      String, String)
     */
    Interne_AjournementEnCours(null),
    /**
     * Motif par défaut pour une réponse
     */
    Interne_NonDefini(null),
    /**
     * Le tiers est déjà bénéficiaire d'une rente de vieillesse
     */
    Interne_RenteVieilesseDejaEnCours(null),
    /**
     * Le tier n'est pas du bon sexe pour le module
     * 
     * @see REModuleEcheanceAgeAvs
     */
    Interne_SexeNonPrisEnCharge(null),
    /**
     * Le tiers, de sexe masculin, est au bénéfice d'une rente de veuf.<br/>
     * Son dernier enfant (le plus jeune), bénéficiant d'une complémentaire de la rente de veuf, aura 18 ans dans le
     * mois courant
     * 
     * @see REModuleEcheanceRenteDeVeuf
     */
    RenteDeVeuf("MOTIF_ECHEANCE_RENTE_DE_VEUF"),
    /**
     * Le tiers, de sexe masculin, est au bénéfice d'une rente de veuf alors qu'il n'a pas/plus d'enfants à charge
     */
    RenteDeVeufSansEnfant("MOTIF_ECHEANCE_RENTE_DE_VEUF_SANS_ENFANTS");

    private String idLabelMotif;

    private REMotifEcheance(String idLabelMotif) {
        this.idLabelMotif = idLabelMotif;
    }

    public String getIdLabelMotif() {
        return idLabelMotif;
    }
}
