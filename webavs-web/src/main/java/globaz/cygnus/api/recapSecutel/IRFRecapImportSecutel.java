/*
 * Créé le 04 juillet 2013
 */
package globaz.cygnus.api.recapSecutel;

/**
 * @author mbo
 */
public interface IRFRecapImportSecutel {

    public static final String ADRESSE_SECUTEL = "ADRESSE_SECUTEL";
    public static final String ADRESSE_CMS = "ADRESSE_CMS";
    public static final String ANNEE_QD_DEMANDE_REJETEE = "anneeTraiteQdRecap";
    public static final String ANNEE_QD_DEMANDE_TRAITEE = "anneeTraiteQdRecap";
    public static final String CMS_REFERENCE = "cmsReference";
    public static final String CODE_PAR_TYPE_ERREUR_RECAPITULATIF = "codeRecap";
    public static final String CODE_RECAP_REJETE = "codeRecapRejete";
    public static final String CS_DOCUMENT = "";
    public static final String DATE_DOCUMENT = "dateDocument";
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_CMS = "descriptionCms";
    public static final String DESCRIPTION_DEMANDE_ERREUR = "descriptionDemE";
    public static final String FICHIER_MODELE_DOCUMENT_DECISION_RFM = "RF_DOCUMENT_DECISION";
    public static final String LIBELLE_MOTIF_REFUS = "libelleMotifRefus";
    public static final String LIBELLE_RECAPITULATIF = "libelleTraitementRecap";
    public static final String LIBELLE_TABLEAU_FACTURES_TRAITEES = "libelleTableauRecap";
    public static final String LIGNE_DEMANDE_ACCEPTEE = "ligneDemA";
    public static final String LIGNE_DEMANDE_ERREUR = "ligneDemE";
    public static final String LIGNE_DEMANDE_REFUS = "ligneDemR";
    public static final String LIGNE_DESCRIPTION_CMS = "ligneDescriptionCms";
    public static final String LIGNE_DETAIL_CAS_REJETES_RECAPITULATIF = "ligneDetailRejeteRecapSecutel";
    public static final String LIGNE_DETAIL_CAS_TRAITES_RECAPITULATIF = "ligneDetailTraiteRecapSecutel";
    public static final String LIGNE_DETAIL_RECAPITULATIF = "ligneDetailRecapSecutel";
    public static final String LIGNE_DETAIL_TOTAL_CAS_REJETES_RECAPITULATIF = "ligneTotalRejeteRecapSecutel";
    public static final String LIGNE_DETAIL_TOTAL_CAS_TRAITES_RECAPITULATIF = "ligneTotalTraiteRecapSecutel";
    public static final String LIGNE_LIBELLE_MOTIF_REFUS = "ligneLibelleDemR";
    public static final String LIGNE_TOTALE_RECAPITULATIF = "ligneTotalRecapSecutel";
    public static final String MESSAGE_ERREUR_DEMANDE_REJETE_RECAPITULATIF = "messageErreur";
    public static final String MONTANT__REFUSE_PAR_TYPE_ERREUR_RECAPITULATIF = "montantRefuseRecap";
    public static final String MONTANT_DEMANDE_ACCEPTEE_RECAPITULATIF = "montantAccepteRecap";
    public static final String MONTANT_DEMANDE_REFUSEE = "mntDemR";
    public static final String MONTANT_DEMANDE_REJETEE = "montantRejeteRecap";
    public static final String MONTANT_DEMANDE_TRAITE = "montantTraiteRecap";
    public static final String MONTANT_TOTAL_ACCEPTE_RECAPITULATIF = "montantTotalAccepteRecap";
    public static final String MONTANT_TOTAL_DEMANDES_ERREURS = "mntTotalDemE";
    public static final String MONTANT_TOTAL_DEMANDES_REFUSEES = "mntTotalDemR";
    public static final String MONTANT_TOTAL_DEMANDES_REFUSEES_PAR_MOTIF = "mntTotalDemRParMotif";
    public static final String MONTANT_TOTAL_DEMANDES_REJETEES = "montantTotalRejeteRecap";
    public static final String MONTANT_TOTAL_DEMANDES_TRAITEES = "montantTotalTraiteRecap";
    public static final String MONTANT_TOTAL_REFUSE_RECAPITULATIF = "montantTotalRefusRecap";
    public static final String NB_CAS_TOTAL_RECAPITULATIF = "nbCasTotalRecap";
    public static final String NOM_PRENOM_DEMANDE_ERREUR = "tiersRejeteRecap";
    public static final String NOM_PRENOM_DEMANDE_REFUSEE = "nomPrenomDemR";
    public static final String NOM_PRENOM_DEMANDE_TRAITEE = "tiersTraiteRecap";
    public static final String NOMBRE_CAS_PAR_TYPE_RECAPITULATIF = "nbCasRecap";
    public static final String NSS_DEMANDE_ERREUR = "nssDemE";
    public static final String NSS_DEMANDE_REFUSEE = "nssDemR";
    public static final String NSS_DEMANDE_REJETEE = "nssRejeteRecap";
    public static final String NSS_DEMANDE_TRAITEE = "nssTraiteRecap";
    public static final String NUMERO_DECISION_DEMANDE_ACCEPTEE = "numeroDecisionDemA";
    public static final String NUMERO_DECISION_DEMANDE_REFUSEE = "numeroDecisionDemR";
    public static final String NUMERO_DEMANDE_ACCEPTEE = "numDemA";
    public static final String NUMERO_DEMANDE_REFUSEE = "numDemR";
    public static final String NUMERO_LIGNE_CAS_REJETES = "numLigne";
    public static final String NUMERO_LIGNE_EN_ERREUR = "numLigneDemE";
    public static final String TABLE_NAME_CMS = "tabCmsSecutel";
    public static final String TABLE_NAME_DEMANDE_ACCEPTEE = "tabDemandeAcceptee";
    public static final String TABLE_NAME_DEMANDE_ERREUR = "tabDemandeErreur";
    public static final String TABLE_NAME_DEMANDE_REFUSEE = "tabDemandeRefusee";
    public static final String TABLEAU_CAS_REJETES_RECAPITULATIF = "tabCasRejeteRecapSecutel";
    public static final String TABLEAU_CAS_TRAITES_RECAPITULATIF = "tabCasTraiteRecapSecutel";
    public static final String TABLEAU_RECAPITULATIF = "tabRecapSecutel";
    public static final String TEXTE_ANNEXE_CAS_PAYES_RECAPITULATIF = "texteAnnexeCasPaye";
    public static final String TEXTE_ANNEXE_CAS_REJETES_RECAPITULATIF = "texteAnnexeCasRejete";
    public static final String TEXTE_INFO_CORRECTION_RECAPITULATIF = "texteInfoCorrection";
    public static final String TEXTE_INFO_DEPASSEMENT_QD_RECAPITULATIF = "texteInfoDepassementQd";
    public static final String TEXTE_TOTAL_DEMANDES_REJETEES_RECAPITULATIF = "texteTotalRejeteRecap";
    public static final String TEXTE_TOTAL_DEMANDES_TRAITEES_RECAPITULATIF = "texteTotalTraiteRecap";
    public static final String TEXTE_TOTAL_RECAPITULATIF = "titreMontantTotalRecap";
    public static final String TITRE_ANNEE_QD = "titreAnneeQd";
    public static final String TITRE_ANNEXE_RECAPITULATIF = "titreAnnexe";
    public static final String TITRE_CONCERNE_CMS_REFERENCE = "titreConcerneCmsReference";
    public static final String TITRE_DOCUMENT_DEMANDES_REJETEES_RECAP_SECUTEL = "titreDocumentRecapSecutelListeRejete";
    public static final String TITRE_DOCUMENT_DEMANDES_TRAITEES_RECAP_SECUTEL = "titreDocumentRecapSecutelListeTraite";
    public static final String TITRE_DOCUMENT_RECAP_SECUTEL = "titreDocumentRecapSecutel";
    public static final String TITRE_MESSAGE_ERREUR = "titreMessageErreur";
    public static final String TITRE_MONTANT = "titreMontant";
    public static final String TITRE_MONTANT_TOTAL = "montantTotal";
    public static final String TITRE_NOM_PRENOM = "titreNomPrenomTiers";
    public static final String TITRE_NSS = "titreNss";
    public static final String TITRE_NUMERO_DECISON = "titreNumeroDecision";
    public static final String TITRE_NUMERO_DEMANDE = "titreNumeroDemande";
    public static final String TITRE_NUMERO_LIGNE = "titreNumeroLigne";
    public static final String TITRE_PERIODE_TRAITEMENT_REJETE_RECAPITULATION = "titrePeriodeRejeteRecap";
    public static final String TITRE_TABLEAU_CODE = "titreCode";
    public static final String TITRE_TABLEAU_DEMANDES_ACCEPT = "titreDemandeAccept";
    public static final String TITRE_TABLEAU_DEMANDES_EN_ERREUR = "titreDemandeErreur";
    public static final String TITRE_TABLEAU_DEMANDES_REFUSEES = "titreDemandeRefus";
    public static final String TITRE_TABLEAU_LIBELLE_TRAITEMENT = "titreLibelleTraitement";
    public static final String TITRE_TABLEAU_MONTANT_PAYE = "titreMontantPaye";
    public static final String TITRE_TABLEAU_MONTANT_REFUSE = "titreMontantRefuse";
    public static final String TITRE_TABLEAU_NOMBRE_DE_CAS = "titreNbCas";

}
