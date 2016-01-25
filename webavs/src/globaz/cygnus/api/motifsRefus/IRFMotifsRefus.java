/*
 * Créé le 10 novembre 2010
 */
package globaz.cygnus.api.motifsRefus;

/**
 * <H1>Description</H1>
 * 
 * @author JJE
 */
public interface IRFMotifsRefus {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CS_GROUPE_MOTIFS_REFUS_SYSTEME = "RFMORESY";

    // CONSTANTES POUR LES MOTIFS DE REFUS DETECTE PAR LE SYSTEME
    /*
     * public static final String DELAI_15_MOIS_DEPASSE = "66000710"; public static final String DELAI_DECES_DEPASSE =
     * "66000711"; public static final String ENFANT_EXCLU_PC = "66000712"; public static final String PAS_DROIT_A_LA_PC
     * = "66000713"; public static final String DROIT_ACQUIS_ETRANGER = "66000714"; public static final String
     * DROIT_ACQUIS_COMMUNAUTE_RELIGIEUSE = "66000715"; public static final String PAS_DE_DOCUMENTS_CALCUL_PC =
     * "66000716"; public static final String DECOMPTE_FACTURE_DEJA_REMBOURSE = "66000717"; public static final String
     * MAXIMUM_UN_TIERS_CONTRIBUTION_AVSAI = "66000718"; public static final String NON_CAR_HOME = "66000719"; public
     * static final String MAXIMUM_N_FRANC_PAR_ASSURE = "66000720"; public static final String
     * PRIX_PENSION_SUP_MAX_CANTONAL = "66000721"; public static final String FRQP_DEJA_REBOURSE = "66000722";
     */

    public static final String ID_ATTESTATION_NON_TROUVEE = "ATTESTATION_NON_TROUVEE";
    public static final String ID_CONVENTION_NON_TROUVEE = "CONVENTION_NON_TROUVEE";
    public static final String ID_DELAI_15_MOIS_DEPASSE = "DELAI_15_MOIS_DEPASSE";
    public static final String ID_DELAI_DECES_DEPASSE = "DELAI_DECES_DEPASSE";
    public static final String ID_DROIT_ACQUIS_COMMUNAUTE_RELIGIEUSE = "DROIT_ACQUIS_COMMUNAUTE_RELIGIEUSE";
    public static final String ID_DROIT_ACQUIS_ETRANGERS = "DROIT_ACQUIS_ETRANGERS";
    public static final String ID_ENFANT_EXCLUS_PC = "ENFANT_EXCLUS_PC";
    public static final String ID_FINANCEMENT_NON_CAR_DOMICILE = "FINANCEMENT_NON_CAR_DOMICILE";
    public static final String ID_FRAIS_DE_LIVRAISON_FOURNISSEUR_NON_CONVENTIONNE = "FRAIS_DE_LIVRAISON_FOURNISSEUR_NON_CONVENTIONNE";
    public static final String ID_FRQP_DEJA_REMBOURSEES = "FRQP_DEJA_REMBOURSEES";
    public static final String ID_FRQP_MAXIMUM_N_FRANC_PAR_ASSURE = "FRQP_MAXIMUM_N_FRANC_PAR_ASSURE";
    public static final String ID_MAXIMUM_N_FRANC_PAR_ANNEE = "MAXIMUM_N_FRANC_PAR_ANNEE";
    public static final String ID_MAXIMUM_N_FRANC_PAR_ANNEE_CONVENTION = "MAXIMUM_N_FRANC_PAR_ANNEE_CONVENTION";
    public static final String ID_MAXIMUM_N_FRANC_PAR_ANNEE_PETITE_QD = "MAXIMUM_N_FRANC_PAR_ANNEE_PETITE_QD";
    public static final String ID_MENAGE_NON_CAR_HOME = "MENAGE_NON_CAR_HOME";
    public static final String ID_MENAGE_TIERS_MAXIMUM_N_FRANC = "MENAGE_TIERS_MAXIMUM_N_FRANC";
    public static final String ID_MOYEN_AUXILIAIRE_MAXIMUM_1_3_CONTRIBUTION_AVS_AI = "MOYEN_AUXILIAIRE_MAXIMUM_1_3_CONTRIBUTION_AVS_AI";
    public static final String ID_MOYEN_AUXILIAIRE_NON_CAR_HOME = "MOYEN_AUXILIAIRE_NON_CAR_HOME";
    public static final String ID_PAS_DE_DOCUMENTS_POUR_CALCULER_LA_PC = "PAS_DE_DOCUMENTS_POUR_CALCULER_LA_PC";
    public static final String ID_PAS_DROIT_A_LA_PC = "PAS_DROIT_A_LA_PC";
    public static final String ID_PRIX_DE_PENSION_SUPERIEUR_AU_MAXIMUM_CANTONAL = "PRIX_DE_PENSION_SUPERIEUR_AU_MAXIMUM_CANTONAL";
    public static final String ID_SOLDE_EXECEDENT_DE_REVENU = "SOLDE_EXCEDENT_DE_REVENU";

}
