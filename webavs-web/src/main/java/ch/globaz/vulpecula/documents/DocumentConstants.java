package ch.globaz.vulpecula.documents;

/**
 * Classe regroupant les constantes relatives aux documents générés par Vulpecula.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 4 juin 2014
 * 
 */
public final class DocumentConstants {
    private DocumentConstants() {
        throw new UnsupportedOperationException();
    }

    public static final String EXTENSION_TXT = ".txt";
    public static final String EXTENSION_CSV = ".csv";

    public static final String HEADER_LANDSCAPE_BMS = "Header_landscape_BMS.jasper";
    public static final String HEADER_LANDSCAPE_BMS_EBUSINESS = "Header_landscape_BMS_Ebusiness.jasper";

    /* Valeurs relatives aux documents */
    public static final String DECOMPTE_VIDE_CONTRIB_TEMPLATE = "PT_DECOMPTE_VIDE_CONTRIB";
    public static final String LABEL_DOCUMENT_AF = "DOCUMENT_AF";
    public static final String LABEL_DOCUMENT_AC2 = "DOCUMENT_AC2";
    public static final String LABEL_DOCUMENT_AC = "DOCUMENT_AC";
    public static final String LABEL_DOCUMENT_AVS_AI_APG = "DOCUMENT_AVS_AI_APG";
    public static final String LABEL_DOCUMENT_CAISSES_SOCIALES = "DOCUMENT_CAISSES_SOCIALES";

    public static final String DECOMPTE_VIDE_PERIODIQUE_TEMPLATE = "PT_DECOMPTE_VIDE_PERIODIQUE";
    public static final String DECOMPTE_VIDE_PERIODIQUE = "decompteVidePeriodique";
    public static final String DECOMPTE_VIDE_PERIODIQUE_CT_NAME = "Décompte périodique";
    public static final String DECOMPTE_VIDE_PERIODIQUE_FILENAME = "DecomptePeriodique";
    public static final String DECOMPTE_VIDE_PERIODIQUE_TYPE_NUMBER = "0001PPT";
    public static final String DECOMPTE_VIDE_PERIODIQUE_SUBJECT = "Décompte périodique";
    public static final String DECOMPTE_VIDE_PERIODIQUE_DESCRIPTION = "";

    public static final String DECOMPTE_VIDE_COMPLEMENTAIRE_TEMPLATE = "PT_DECOMPTE_VIDE_COMPLEMENTAIRE";
    public static final String DECOMPTE_VIDE_COMPLEMENTAIRE = "decompteVideComplementaire";
    public static final String DECOMPTE_VIDE_COMPLEMENTAIRE_CT_NAME = "Décompte complémentaire";
    public static final String DECOMPTE_VIDE_COMPLEMENTAIRE_FILENAME = "DecompteComplementaire";
    public static final String DECOMPTE_VIDE_COMPLEMENTAIRE_TYPE_NUMBER = "0002PPT";
    public static final String DECOMPTE_VIDE_COMPLEMENTAIRE_SUBJECT = "Décompte complémentaire";
    public static final String DECOMPTE_VIDE_COMPLEMENTAIRE_DESCRIPTION = "";
    public static final String DECOMPTE_VIDE_DEFAULT_SUBJECT = "Décompte";

    public static final String DECOMPTE_BVR_TEMPLATE = "PT_DECOMPTE_BVR";
    public static final String DECOMPTE_BVR_CT_NAME = "Décompte BVR";
    public static final String DECOMPTE_BVR_FILENAME = "DecompteBVR";
    public static final String DECOMPTE_BVR_TYPE_NUMBER = "0003PPT";
    public static final String DECOMPTE_BVR_SUBJECT = "Décompte BVR";

    public static final String SOMMATION_TEMPLATE = "PT_SOMMATION";
    public static final String SOMMATION_CT_NAME = "Sommation";
    public static final String SOMMATION_TYPE_NUMBER = "0004PPT";
    public static final String SOMMATION_SUBJECT = "Sommation";

    public static final String TAXATION_OFFICE_TEMPLATE = "PT_TAXATION_OFFICE";
    public static final String TAXATION_OFFICE_CT_NAME = "Taxation d'office";
    public static final String TAXATION_OFFICE_TYPE_NUMBER = "0005PPT";
    public static final String TAXATION_OFFICE_SUBJECT = "Taxation d'office";

    public static final String RECTIFICATIF_TEMPLATE = "PT_RECTIFICATIF";
    public static final String RECTIFICATIF_CT_NAME = "Rectificatif";
    public static final String RECTIFICATIF_TYPE_NUMBER = "0006PPT";
    public static final String RECTIFICATIF_SUBJECT = "Rectificatif";

    public static final String LISTES_AJ_NAME = "Liste AJ";
    public static final String LISTES_AJ_DOC_NAME = "listeAJ";
    public static final String LISTES_AJ_TYPE_NUMBER = "0007PPT";

    public static final String LISTES_CP_NAME = "Liste CP";
    public static final String LISTES_CP_DOC_NAME = "listeCP";
    public static final String LISTES_CP_TYPE_NUMBER = "0008PPT";

    public static final String LISTES_SM_NAME = "Liste SM";
    public static final String LISTES_SM_DOC_NAME = "listeSM";
    public static final String LISTES_SM_TYPE_NUMBER = "0009PPT";

    public static final String LISTES_ENTREPRISES_NAME = "Liste Entreprises";
    public static final String LISTES_ENTREPRISES_DOC_NAME = "listeEntreprises";
    public static final String LISTES_ENTREPRISES_TYPE_NUMBER = "0010PPT";

    public static final String LISTES_SALAIRES_NAME = "Liste Salaires";
    public static final String LISTES_SALAIRES_DOC_NAME = "listeSalaires";
    public static final String LISTES_SALAIRES_FILE_NAME = "listeSalairesFile";
    public static final String LISTES_SALAIRES_TYPE_NUMBER = "0011PPT";

    public static final String LISTES_SALAIRES_RESOR_NAME = "Liste Salaires Resor";
    public static final String LISTES_SALAIRES_RESOR_DOC_NAME = "listeSalairesResor";
    public static final String LISTES_SALAIRES_RESOR_FILE_NAME = "listeSalairesResorFile";
    public static final String LISTES_SALAIRES_RESOR_TYPE_NUMBER = "0012PPT";

    public static final String LISTES_SALAIRES_RETAVAL_NAME = "Liste Salaires Retaval";
    public static final String LISTES_SALAIRES_RETAVAL_DOC_NAME = "listeSalairesRetaval";
    public static final String LISTES_SALAIRES_RETAVAL_FILE_NAME = "listeSalairesRetavalFile";
    public static final String LISTES_SALAIRES_RETAVAL_TYPE_NUMBER = "0012PPT";

    public static final String LISTES_ANNONCE_SALARIE_NAME = "Liste des travailleurs à annoncer à la MEROBA";
    public static final String LISTES_ANNONCE_SALARIE_DOC_NAME = "listeAnnonceSalaries";
    public static final String LISTES_ANNONCE_SALARIE = "0012PPT";

    public static final String LISTES_CAISSES_MALADIES_ADMISSION_NAME = "Liste d'admissions aux caisses maladies";
    public static final String LISTES_CAISSES_MALADIES_ADMISSION_NAME_NON_ANNONCE = "Liste de caisses maladies non annoncées";
    public static final String LISTES_CAISSES_MALADIES_ADMISSION_DOC_NAME = "listeCaisseMaladieAdmission";
    public static final String LISTES_CAISSES_MALADIES_ADMISSION = "0013PPT";

    public static final String LISTES_CAISSES_MALADIES_DEMISSION_NAME = "Liste de démissions aux caisses maladies";
    public static final String LISTES_CAISSES_MALADIES_DEMISSION_DOC_NAME = "listeCaisseMaladieDemission";
    public static final String LISTES_CAISSES_MALADIES_DEMISSION = "0014PPT";

    public static final String LISTES_SYNDICATS_TRAVAILLEURS_SYNDICAT_NAME = "Liste des travailleurs par syndicat";
    public static final String LISTES_SYNDICATS_TRAVAILLEURS_SYNDICAT_DOC_NAME = "listeTravailleursParSyndicat";
    public static final String LISTES_SYNDICATS_TRAVAILLEURS_SYNDICAT = "0015PPT";

    public static final String LISTES_SYNDICATS_TRAVAILLEURS_SALAIRE_SYNDICAT_NAME = "Liste des travailleurs avec salaire par syndicat";
    public static final String LISTES_SYNDICATS_TRAVAILLEURS_SALAIRE_SYNDICAT_DOC_NAME = "listeTravailleursSalaireParSyndicat";
    public static final String LISTES_SYNDICATS_TRAVAILLEURS_SALAIRE_SYNDICAT = "0016PPT";

    public static final String LISTES_SYNDICATS_TRAVAILLEURS_PAIEMENT_SYNDICAT_NAME = "Liste des travailleurs paiement par syndicat";
    public static final String LISTES_SYNDICATS_TRAVAILLEURS_PAIEMENT_SYNDICAT_DOC_NAME = "listeTravailleursPaiementParSyndicat";
    public static final String LISTES_SYNDICATS_TRAVAILLEURS_PAIEMENT_SYNDICAT = "0017PPT";

    public static final String LISTES_BOUCLEMENT_CP_NAME = "Bouclement CP";
    public static final String LISTES_BOUCLEMENT_CP_DOC_NAME = "bouclementCP";
    public static final String LISTES_BOUCLEMENT = "0018PPT";

    public static final String LISTES_AF_RETENUES_COMMISSION_NAME = "LR retenues et commissions par CAF";
    public static final String LISTES_AF_RETENUES_DOC_NAME = "LR_retenues_et_commissions_par_CAF";
    public static final String LISTES_AF_RETENUES = "0019PPT";

    public static final String ATTESTATION_AF_TEMPLATE = "PT_ATTESTATION_AF";
    public static final String ATTESTATION_AF_CT_NAME = "Attestation AF IS";
    public static final String ATTESTATION_AF_TYPE_NUMBER = "0021PPT";
    public static final String ATTESTATION_AF_SUBJECT = "Attestation AF";

    public static final String ATTESTATION_AF_FISC_TEMPLATE = "PT_ATTESTATION_AF_FISC";
    public static final String ATTESTATION_AF_FISC_CT_NAME = "Attestation AF Fisc";
    public static final String ATTESTATION_AF_FISC_TYPE_NUMBER = "0022PPT";
    public static final String ATTESTATION_AF_FISC_SUBJECT = "Attestation AF pour le Fisc";

    public static final String SUIVI_CAISSE_STANDARD_NAME = "Suivi caisse maladie standard";
    public static final String SUIVI_CAISSE_STANDARD_DOC_NAME = "Suivi_caisse_maladie_standard";
    public static final String SUIVI_CAISSE_STANDARD_TYPE_NUMBER = "0023PPT";

    public static final String SUIVI_CAISSE_FA_NAME = "Suivi caisse maladie FA";
    public static final String SUIVI_CAISSE_FA_DOC_NAME = "Suivi_caisse_maladie_FA";
    public static final String SUIVI_CAISSE_FA_TYPE_NUMBER = "0024PPT";

    public static final String LISTES_TRAVAILLEURS_SANS_SYNDICAT_NAME = "Liste des travailleurs sans syndicat";
    public static final String LISTES_TRAVAILLEURS_SANS_SYNDICAT_DOC_NAME = "listeTravailleursSansSyndicat";
    public static final String LISTES_TRAVAILLEURS_SANS_SYNDICAT = "0025PPT";

    public static final String SALAIRE_SOCIO_ECONOMIQUE_NAME = "Salaire socio-économique";
    public static final String SALAIRE_SOCIO_ECONOMIQUE_DOC_NAME = "salaire socio-économique";
    public static final String SALAIRE_SOCIO_ECONOMIQUE_TYPE_NUMBER = "0026PPT";

    public static final String PRIME_NAISSANCE_AF_TEMPLATE = "PT_PRIME_NAISSANCE_AF";
    public static final String PRIME_NAISSANCE_AF_DOC_NAME = "Prime_naissance_AF";
    public static final String PRIME_NAISSANCE_AF_CT_NAME = "Prime de naissance";
    public static final String PRIME_NAISSANCE_AF_TYPE_NUMBER = "0027PPT";

    public static final String ABSENCES_JUSTIFIEES_TEMPLATE = "PT_PRESTATIONS_AJ";
    public static final String ABSENCES_JUSTIFIEES_NAME = "Absences justifiees";
    public static final String ABSENCES_JUSTIFIEES_TYPE_NUMBER = "0028PPT";
    public static final String ABSENCES_JUSTIFIEES_DOC_NAME = "Absences justifiées";

    public static final String CONGES_PAYES_TEMPLATE = "PT_PRESTATIONS_CP";
    public static final String CONGES_PAYES_ELEC_TEMPLATE = "PT_PRESTATIONS_CP_ELEC";
    public static final String CONGES_PAYES_NAME = "Conges payes";
    public static final String CONGES_PAYES_TYPE_NUMBER = "0029PPT";
    public static final String CONGES_PAYES_DOC_NAME = "Congés payées";

    public static final String SERVICE_MILITAIRE_TEMPLATE = "PT_PRESTATIONS_SM";
    public static final String SERVICE_MILITAIRE_NAME = "Service militaire";
    public static final String SERVICE_MILITAIRE_TYPE_NUMBER = "0030PPT";
    public static final String SERVICE_MILITAIRE_DOC_NAME = "Service militaire";

    public static final String PERTE_GAIN_FRAIS_MEDICAUX_NAME = "Perte de gain et frais médicaux";
    public static final String PERTE_GAIN_FRAIS_MEDICAUX_DOC_NAME = "Perte_gain_frais_médicaux";
    public static final String PERTE_GAIN_FRAIS_MEDICAUX_TYPE_NUMBER = "0031PPT";

    public static final String SALAIRE_QUALIFICATION_NAME = "Salaire qualification";
    public static final String SALAIRE_QUALIFICATION_DOC_NAME = "salaire qualification";
    public static final String SALAIRE_QUALIFICATION_TYPE_NUMBER = "0032PPT";

    public static final String LISTES_REVISION_NAME = "Liste des travailleurs pour révision";
    public static final String LISTES_REVISION_DOC_NAME = "listeTravailleursPourRevision";
    public static final String LISTES_REVISION_TYPE_NUMBER = "0033PPT";

    public static final String LISTES_REVISION_RECAP_NAME = "Récap. employeur";
    public static final String LISTES_REVISION_RECAP_DOC_NAME = "RecapEmployeur";
    public static final String LISTES_REVISION_RECAP_TYPE_NUMBER = "0034PPT";

    public static final String LISTES_SALAIRES_AVS_NAME = "Liste de decompte des salaires AVS";
    public static final String LISTES_SALAIRES_AVS_DOC_NAME = "DecompteSalairesAVS";
    public static final String LISTES_SALAIRES_AVS_TYPE_NUMBER = "0035PPT";

    public static final String LISTES_NOUVELLES_AFFILIATIONS_NAME = "Nouvelles affiliations";
    public static final String LISTES_NOUVELLES_AFFILIATIONS_DOC_NAME = "NouvellesAffiliations";
    public static final String LISTES_NOUVELLES_AFFILIATIONS_NUMBER = "0036PPT";

    public static final String LISTES_NON_CONTROLE_NAME = "Liste des entreprises non contrôlées depuis 4 ans";
    public static final String LISTES_NON_CONTROLE_DOC_NAME = "listeEntrepriseNonControle4Ans";
    public static final String LISTES_NON_CONTROLE_TYPE_NUMBER = "0037PPT";

    public static final String LETTRE_CONTROLE_EMPLOYEUR_TEMPLATE = "PT_LETTRE_CONTROLE_EMPLOYEUR";
    public static final String LETTRE_CONTROLE_EMPLOYEUR_CT_NAME = "Lettre contrôle employeur";
    public static final String LETTRE_CONTROLE_EMPLOYEUR_TYPE_NUMBER = "0038PPT";
    public static final String LETTRE_CONTROLE_EMPLOYEUR_SUBJECT = "Lettre contrôle employeur";

    public static final String LETTRE_CONTROLE_EMPLOYEUR_AVS_TEMPLATE = "PT_LETTRE_CONTROLE_EMPLOYEUR";
    public static final String LETTRE_CONTROLE_EMPLOYEUR_AVS_CT_NAME = "Lettre contrôle employeur AVS";
    public static final String LETTRE_CONTROLE_EMPLOYEUR_AVS_TYPE_NUMBER = "0039PPT";
    public static final String LETTRE_CONTROLE_EMPLOYEUR_AVS_SUBJECT = "Lettre contrôle employeur AVS";

    public static final String RECAP_EMPLOYEUR_EMPLOYEUR_NAME = "Récapitulatif employeur";
    public static final String RECAP_EMPLOYEUR_EMPLOYEUR_TYPE_NUMBER = "0042PPT";
    public static final String RECAP_EMPLOYEUR_EMPLOYEUR_SUBJECT = "Récapitulatif employeur";

    public static final String FACTURE_AP_TEMPLATE = "ap/PT_FACTURE_AP";
    public static final String FACTURE_AP_NAME = "Facture AP";
    public static final String FACTURE_AP_TYPE_NUMBER = "0040PPT";
    public static final String FACTURE_AP_SUBJECT = "Facture(s) associations professionnelles";

    public static final String LISTES_FACTURATION_AP = "Protocole facturation AP";
    public static final String LISTES_FACTURATION_AP_DOC_NAME = "listeFaturationAP";
    public static final String LISTES_FACTURATION_AP_TYPE_NUMBER = "0041PPT";

    // TODO mettre les bonnes valeurs ici
    public static final String LISTES_INTERNES_RECAP_CONVENTION = "Recapitulation par convention";
    public static final String LISTES_INTERNES_RECAP_CAISSE = "Recapitulation par caisse";
    public static final String LISTES_INTERNES_RECAP_GENRE_CAISSE = "Recapitulation par genre de caisse";
    public static final String LISTES_INTERNES_RECAP_CAISSE_CONVENTION = "Recapitulation par caisse et convention";
    // TODO mettre les bonnes valeurs ici
    public static final String LISTES_INTERNES_RECAP_CONVENTION_DOC_NAME = "recapConvention";
    public static final String LISTES_INTERNES_RECAP_CAISSE_DOC_NAME = "recapCaisse";
    public static final String LISTES_INTERNES_RECAP_GENRE_CAISSE_DOC_NAME = "recapGenreCaisse";
    public static final String LISTES_INTERNES_RECAP_CAISSE_CONVENTION_DOC_NAME = "recapCaisseConvention";

    public static final String LISTES_INTERNES_RECAP_CONVENTION_TYPE_NUMBER = "0042PPT";
    // TODO mettre les bonnes valeurs ici
    public static final String LISTES_INTERNES_RECAP_CAISSE_TYPE_NUMBER = "0042PPT";
    public static final String LISTES_INTERNES_RECAP_GENRE_CAISSE_TYPE_NUMBER = "0042PPT";
    public static final String LISTES_INTERNES_RECAP_CAISSE_CONVENTION_TYPE_NUMBER = "0042PPT";

    public static final String LISTES_CP_SOUMIS_LPP = "Liste des congés payés soumis à la LPP";
    public static final String LISTES_CP_SOUMIS_LPP_DOC_NAME = "listeCPsoumisLPP";
    public static final String LISTES_CP_SOUMIS_LPP_TYPE_NUMBER = "0043PPT";

    public static final String LISTES_RECAP_PAR_RUBRIQUE = "Listes récapitulative par rubrique";
    public static final String LISTES_RECAP_PAR_RUBRIQUE_DOC_NAME = "ListeRecapParRubrique";
    public static final String LISTES_RECAP_PAR_RUBRIQUE_TYPE_NUMBER = "0044PPT";

    public static final String LISTES_TO_PREVISIONNELLE = "Liste prévisionnelle des taxations d'office";
    public static final String LISTES_TO_PREVISIONNELLE_DOC_NAME = "listePrevisionnelleTOs";
    public static final String LISTES_TO_PREVISIONNELLE_TYPE_NUMBER = "0045PPT";

    public static final String LISTES_TO_ANNULE = "Liste des taxations d'office annulées";
    public static final String LISTES_TO_ANNULE_DOC_NAME = "listeTosAnnulees";
    public static final String LISTES_TO_ANNULE_TYPE_NUMBER = "0046PPT";

    public static final String LISTES_DECOMPTES_SANS_AF = "Liste des décomptes sans AF générées";
    public static final String LISTES_DECOMPTES_SANS_AF_DOC_NAME = "listeDecomptesSansAF";
    public static final String LISTES_DECOMPTES_SANS_AF_TYPE_NUMBER = "0047PPT";

    public static final String LISTES_SOLDE_CPP_ASSOCIATION_NAME = "Soldes CPP Association";
    public static final String LISTES_SOLDE_CPP_ASSOCIATION_DOC_NAME = "SoldeCPPAssociation";

    public static final String LISTES_CONTROLES_EMPLOYEURS_PAR_ANNEE_NAME = "Controles employeur par année";
    public static final String LISTES_CONTROLES_EMPLOYEURS_PAR_ANNEE_DOC_NAME = "ControleEmployeurAnnee";

    public static final String LISTES_DIVERSES_NAME = "Listes fin d'année DS";
    public static final String LISTES_DIVERSES_DOC_NAME = "Listes fin d'année DS";
    public static final String LISTES_DIVERSES_FILE_NAME = "listesFinAnneeDSFile";
    public static final String LISTES_DIVERSES_TYPE_NUMBER = "0048PPT";

    public static final String LISTES_PERSONNES_AVEC_AGE_NAME = "Listes des personnes avec leur âge";
    public static final String LISTES_PERSONNES_AVEC_AGE_DOC_NAME = "Listes personnes avec âge";
    public static final String LISTES_PERSONNES_AVEC_AGE_FILE_NAME = "listesPersonnesAvecAge";
    public static final String LISTES_PERSONNES_AVEC_AGE_TYPE_NUMBER = "0049PPT";

    public static final String LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL_TEMPLATE = "PT_LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL";
    public static final String LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL_CT_NAME = "Employeurs actifs sans pers";
    public static final String LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL_TYPE_NUMBER = "0051PPT";
    public static final String LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL_SUBJECT = "Lettre employeurs actifs sans personnel";

    public static final String LISTES_AF_VERSEES_NAME = "Liste des AF versées";
    public static final String LISTES_AF_VERSEES_NAME_DOC_NAME = "List_AF_Versees";
    public static final String LISTES_AF_VERSEES_TYPE_NUMBER = "0052PPT";

    public static final String LISTES_AMCAB_DOC_NAME = "ListeCaisseMaladieParTravailleur";
    public static final String LISTES_AMCAB_NAME = "Liste des caisses maladies par travailleur";
    public static final String LISTES_AMCAB = "0050PPT";

    public static final String LISTES_COMPARAISON_MYPRODIS_DOC_NAME = "ListeDifferencesDecomptes";
    public static final String LISTES_COMPARAISON_MYPRODIS_NAME = "Liste des différences entre les décomptes WebMetier et MyProdis";
    public static final String LISTES_COMPARAISON_MYPRODIS = "0053PPT";
    
    public static final String LISTE_QUORUMS_DOC_NAME = "ListeQuorums";
    public static final String LISTE_QUORUMS_NAME = "Liste des détails quorums";
    public static final String LISTE_QUORUMS = "0054PPT";

    /* Paramètres d'un BVR */
    public static final String P_ADRESSE = "P_ADRESSE";
    public static final String P_ADRESSECOPY = "P_ADRESSECOPY";
    public static final String P_COMPTE = "P_COMPTE";
    public static final String P_FRANC = "P_FRANC";
    public static final String P_CENTIME = "P_CENTIME";
    public static final String P_VERSE = "P_VERSE";
    public static final String P_PAR = "P_PAR";
    public static final String P_OCR = "P_OCR";

    public static final String P_TOTAL_SALAIRE = "P_TOTAL_SALAIRE";
    public static final String P_DECOMPTE_ID = "P_DECOMPTE_ID";
    public static final String P_DECOMPTE_DESCRIPTION = "P_DECOMPTE_DESCRIPTION";

    public static final String DOCUMENT_TOTAL_DES_SALAIRES = "DOCUMENT_TOTAL_DES_SALAIRES";
    public static final String DOCUMENT_ID_DECOMPTE = "DOCUMENT_ID_DECOMPTE";

    public static final String COL_3 = "COL_3";
    public static final String COL_2 = "COL_2";
    public static final String COL_1 = "COL_1";
    public static final String P_MONTANT_PAYABLE = "P_MONTANT_PAYABLE";
    public static final String P_TOTAL_CONTRI = "P_TOTAL_CONTRI";
    public static final String P_LIEU_DATE = "P_LIEU_DATE";
    public static final String P_TIMBRE_SIGNATURE = "P_TIMBRE_SIGNATURE";
    public static final String P_CERTIFIE_EXACT = "P_CERTIFIE_EXACT";
    public static final String F_GROUPE_LABEL = "F_GROUPE_LABEL";
    public static final String P_COL_HEADER_VAC = "P_COL_HEADER_5";

    public static final String F_MASSE = "F_MASSE";
    public static final String F_MONTANT = "F_MONTANT";
    public static final String F_SUR = "F_SUR";
    public static final String P_MONTANT_TOTAL = "P_MONTANT_TOTAL";

    public static final String LISTES_MONTANT_RUBRIQUE = "0026PPT";
    public static final String LISTES_MONTANT_RUBRIQUE_FILENAME = "listeMasseSalariale";
    public static final String LISTES_MONTANT_RUBRIQUE_TITLE = "Masse salariale et cotisation par rubrique et par caisse";
    public static final String ANNULATION_SOLDE_MINIME_TITLE = "ANNULATION_SOLDE_MINIME_TITLE";
    public static final String ANNULATION_SOLDE_MINIME_NON_MEMBRE_TITLE = "ANNULATION_SOLDE_MINIME_NON_MEMBRE_TITLE";

}
