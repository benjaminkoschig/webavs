package globaz.cygnus.utils;

import globaz.cygnus.application.RFApplication;
import globaz.globall.api.BIApplication;
import globaz.globall.api.GlobazSystem;
import globaz.jade.properties.JadePropertiesService;
import java.util.HashMap;
import java.util.Map;

public class RFPropertiesUtils {

    public static Map<String, String> intervalDeLettreParGestMap = new HashMap<String, String>();

    /**
     * Methode permettant d'afficher dans les décisions, les textes liés au remboursement par le DSAS
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean affichageTextesLiesAuxRemboursmentsParDsas() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_AJOUTER_TEXTES_LIES_AU_DSAS);

    }

    /**
     * Methode qui permet de voir si il faut affihcer la case forcer le paiement dans l'ecran des demandes
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean afficherCaseForcerPaiementDemande() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_AFFIHCER_CASE_FORCER_PAIEMENT);
    }

    /**
     * Methode permettant de voir si il faut afficher la case du RI
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean afficherCaseRi() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_AFFIHCER_CASE_RI);
    }

    /**
     * Methode permettant de voir si il faut afficher dans le libelle, la personne de référence
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean afficherLibellePersonneReference() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_AFFICHER_PERSONNE_REFERENCE);
    }

    /**
     * Methode qui vérifie si il faut afficher le champ remarque fournisseur dans l'ecran des demandes
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean afficherRemarqueFournisseur() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_AFFICHER_REMARQUE_FOURNISSEUR);
    }

    /**
     * Methode qui vérifie si il faut afficher dans le libelle, le numéro de téléphone du gestionnaire
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean afficherTelephoneGestionnaire() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_AFFICHER_TELEPHONE_GESTIONNAIRE);
    }

    /**
     * Methode pour savoir si il faut afficher le type de remboursement
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean afficherTypeRemboursement() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_AFFIHCER_TYPE_REMBOURSEMENT);
    }

    /**
     * Methode pour ajouter les demande en comptabilité sans tenir compte du type de home (RFModCpt_Normal)
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean ajoutDemandesEnComptabiliteSansTenirCompteTypeDeHome() throws Exception {
        return RFPropertiesUtils
                .checkProperty(RFApplication.PROPERTY_AJOUTER_DEMANDE_EN_COMPTA_SANS_TENIR_COMPTE_TYPE_HOME);
    }

    /**
     * Methode privé permettant de setter la propriété reçue et retourner la valeur souhaitée
     * 
     * @param session
     * @param property
     * @throws Exception
     */
    private static boolean checkProperty(String property) throws Exception {
        try {
            BIApplication application = RFPropertiesUtils.getApplication();
            return "true".equals(application.getProperty(property));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage() + " : error property undefined");
        }
    }

    /**
     * Methode permettant de retrouvé le service GED utilisé par le client (airs/JadeGedFacade/GEDOS)
     * 
     * @param session
     * @return
     * @throws Exception
     */
    public static String gedTargetName() throws Exception {
        return RFPropertiesUtils.getProperty(RFApplication.PROPERTY_GED_TARGET_NAME);
    }

    private static BIApplication getApplication() throws Exception {
        BIApplication application = GlobazSystem.getApplication(RFApplication.DEFAULT_APPLICATION_CYGNUS);
        return application;
    }

    /**
     * Methode permettant de retrouver le cs du genre d'administation CMS
     * 
     * @param session
     * @throws Exception
     */
    public static String getCsGenreAdminstrationCMS() throws Exception {
        return RFPropertiesUtils.getProperty(RFApplication.PROPERTY_CS_GENRE_ADMINISTRATION_CMS);
    }

    /**
     * Methode permettant de retrouver les sous types de soins à contrôler
     * 
     * @param session
     * @throws Exception
     */
    public static String getDecompteTypeDeSoinsAControler() throws Exception {
        return RFPropertiesUtils.getProperty(RFApplication.PROPERTY_DECOMPTE_SOUS_TYPES_DE_SOINS_A_CONTROLER);
    }

    /**
     * Methode permettant de retrouver l'id tiers des avances SAS
     * 
     * @param session
     * @throws Exception
     */
    public static String getIdTiersAvanceSas() throws Exception {
        return RFPropertiesUtils.getProperty(RFApplication.PROPERTY_AVANCES_SAS_ID_TIERS);

    }

    /**
     * 
     * Retourne un intervalle de lettre (ex: C-F) par utilisateur selon les properties commençant par
     * "cygnus.groupe.gestionnaire"
     * 
     * @return Map<String,String>
     */
    public static Map<String, String> getIntervalDeLettreParGestionnaire() throws Exception {

        final String prefixKey = RFApplication.PROPERTY_GROUPES_GESTIONNAIRES;
        final String applicationName = RFApplication.DEFAULT_APPLICATION_CYGNUS.toLowerCase();

        if ((RFPropertiesUtils.intervalDeLettreParGestMap == null)
                || (RFPropertiesUtils.intervalDeLettreParGestMap.size() == 0)) {

            RFPropertiesUtils.intervalDeLettreParGestMap = new HashMap<String, String>();

            for (String key : JadePropertiesService.getInstance().getPropertiesList().keySet()) {

                String[] keyGroupesGestionnaire = key.split("\\.");

                if ((keyGroupesGestionnaire != null) && (keyGroupesGestionnaire.length == 3)) {

                    if ((keyGroupesGestionnaire[0] != null) && keyGroupesGestionnaire[0].equals(applicationName)
                            && (keyGroupesGestionnaire[1] != null) && keyGroupesGestionnaire[1].equals(prefixKey)) {

                        RFPropertiesUtils.intervalDeLettreParGestMap.put(
                                keyGroupesGestionnaire[2],
                                RFPropertiesUtils.getProperty(keyGroupesGestionnaire[1] + "."
                                        + keyGroupesGestionnaire[2]));

                    }
                }
            }
        }

        return RFPropertiesUtils.intervalDeLettreParGestMap;
    }

    /**
     * Methode pour ajouter les QDs quandidate
     * 
     * @param session
     * @throws Exception
     */
    /*
     * public static Boolean ajoutQdsCandidates(BSession session) throws Exception { return
     * RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_AJOUTER_QD_CANDIDATE); }
     */

    /**
     * Methode retournant le nombre de mois à prendre en compte lors de la recherche des devis
     * 
     * @param session
     * @throws Exception
     */
    public static String getNbMoisRechercheDevis() throws Exception {
        return RFPropertiesUtils.getProperty(RFApplication.PROPERTY_NB_MOIS_RECHERCHE_DEVIS);
    }

    /**
     * Methode privé permettant de retourner la valeur d'une propriétés
     * 
     * @param session
     * @param property
     * @throws Exception
     */
    private static String getProperty(String property) throws Exception {
        try {
            return RFPropertiesUtils.getApplication().getProperty(property);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage() + " : error property undefined");
        }
    }

    /**
     * Propriété pour définir si les décisions doivent être imprimées avec la fonction recto/verso
     * 
     * @return
     * @throws Exception
     */
    public static Boolean imprimerDecisionsRectoVerso() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_DECISION_RECTO_VESRO);

    }

    /**
     * Methode pour vérifier si l'imputation doit s'effectuer par période de traitement
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean imputationParPeriodeDeTraitement() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_IMPUTATION_PAR_PERIODE_DE_TRAITEMENT);
    }

    public static Boolean insererLigneTechniqueDeRegroupement() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_INSERER_LIGNE_TECHNIQUE_DE_REGROUPEMENT);

    }

    /**
     * Methode permettant d'afficher dans les décisions, les textes liés au remboursement par le DSAS
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean isPeriodeDeTraitementRegCotParCumulative() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_IS_PERIODE_DE_TRAITEMENT_REG_COTPAR_CUMULATIVE);

    }

    /**
     * Methode pour retourner la valeur cherché sur le gestionnaire à sélectionner
     * 
     * @param session
     * @throws Exception
     */
    private static boolean isUtilisateurSelectionneEcran() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_UTILISER_GESTIONNAIRE_ECRAN);
    }

    /**
     * Methode pour utiliser le gestionnaire du viewBean dans la mise a jour des Qds principales dans les demandes à
     * imputer
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean misesAJourQdsPrincipalesDemandesAImputer() throws Exception {
        return RFPropertiesUtils.isUtilisateurSelectionneEcran();
    }

    /**
     * Methode permettant de remonter l'adresse du fournisseur SAS lors de la saise d'une demande 13.06
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean remonterAdressePaiementFournisseurSAS() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_GERER_AVANCES_SAS);

    }

    /**
     * Methode qui permet de générer les décisions de type régime.
     * 
     * @return
     * @throws Exception
     */
    public static Boolean retirerDecisionsRegime() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_RETIRER_DECISIONS_REGIME_DANS_PDF);
    }

    public static Boolean utiliserDecisionAvecExcedantRevenu() throws Exception {
        return RFPropertiesUtils
                .checkProperty(RFApplication.PROPERTY_UTILISER_DOCUMENT_DECISION_REGIME_AVEC_EXCEDANT_REVENU);
    }

    /**
     * Methode pour utiliser le gestionnaire du viewBean dans la recherche des demandes a imputer
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean utiliserGestionnaireRechercheDemandesAImputer() throws Exception {
        return RFPropertiesUtils.isUtilisateurSelectionneEcran();
    }

    /**
     * Methode permettant d'utiliser le gestionnaire de la session dans le lancement de la simulation de génération Le
     * résultat ressortis est inversé
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean utiliserGestionnaireSessionSimulationDecisions() throws Exception {
        return RFPropertiesUtils.isUtilisateurSelectionneEcran();
    }

    /**
     * Methode qui permet de voir s'il faut utiliser le gestionnaire de la session ou celui du viewBean.
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean utiliserGestionnaireViewBean() throws Exception {
        return RFPropertiesUtils.isUtilisateurSelectionneEcran();
    }

    /**
     * Methode pour utiliser le gestionnaire du viewBean pour l'annulation des demandes
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean utiliserGestionnaireViewBeanAnnulationDemandes() throws Exception {
        return RFPropertiesUtils.isUtilisateurSelectionneEcran();
    }

    /**
     * Methode pour utiliser le gestionnaire sélectionné dans la mise à jour des prestations accordées
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean utiliserGestionnaireViewBeanMajPrestationsAccordees() throws Exception {
        return RFPropertiesUtils.isUtilisateurSelectionneEcran();
    }

    /**
     * Methode qui détermine si il faut utiliser ou non les groupes de gestionnaires
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean utiliserGroupesGestionnaires() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_UTILISERS_GROUPE_GESTIONNAIRES);
    }

    /**
     * Methode permettant d'utiliser la liste de récapitulation complète
     * 
     * @param session
     * @return
     * @throws Exception
     */
    public static Boolean utiliserListeRecapComplete() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_UTILISER_LISTE_RECAPITULATION_COMPLETE);
    }

    /**
     * Methode pour savoir si il faut utiliser le type de bénéficiaire de type PC
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean utiliserTypeBeneficiaire() throws Exception {
        return RFPropertiesUtils.checkProperty("utiliser.type.beneficiaire.type.pc");
    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des attestation, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationAttestationAjoutAideMenageParOsad() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_ATTESTATION_AIDE_MENAGE_ORG_OSAD);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des attestation, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationAttestationAjoutchaisePerce() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_ATTESTATION_CHAISES_PERCEES);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des attestation, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationAttestationAjoutCorsetOrthopedique() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_ATTESTATION_CORSET_ORTHOPEDIQUE);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des attestation, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationAttestationAjoutFraisEndoprotheses() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_ATTESTATION_FRAIS_ENDOPROTHESES);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des attestation, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationAttestationAjoutLombostatOrthopedique() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_ATTESTATION_LOMBOSTAT_ORTHOPEDIQUE);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des attestation, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationAttestationAjoutLunetteVerresContact() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_ATTESTATION_LUNETTES_VERRES_CONTACT);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des attestation, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationAttestationAjoutMinerve() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_ATTESTATION_MINERVE);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des attestation, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationAttestationMoyensAuxRemisEnPretAvecBon() throws Exception {
        return RFPropertiesUtils
                .checkProperty(RFApplication.PROPERTY_VERIFIER_ATTESTATION_MOYENS_AUX_REMIS_EN_PRET_AVEC_BON);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des attestation, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationAttestationAjoutTaxi() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_ATTESTATION_TAXI);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des attestation, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static boolean verificationAttestationAjoutLit() throws Exception {

        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_ATTESTATION_LIT);
    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des conventions, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationConventionAjoutAccompagnementSocial() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_CONVENTION_ACCOMPAGNEMENT_SOCIAL);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des conventions, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationConventionAjoutAnimation() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_CONVENTION_ANIMATION);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des conventions, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationConventionAjoutBarriere() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_CONVENTION_BARRIERE);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des conventions, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationConventionAjoutencadrementSecuritaire() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_CONVENTION_ENCADREMENT_SECURITAIRE);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des conventions, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationConventionAjoutEncadrementSocioEducatif() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_CONVENTION_ENCADREMENT_SOCIO_EDUCATIF);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des conventions, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationConventionAjoutLitElectrique() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_CONVENTION_LIT_ELECTRIQUE);

    }

    /**
     * Methode pour verifier s'il faut ajouter dans la verification des conventions, ce sous-type de soins
     * 
     * @param session
     * @return true / false
     * @throws Exception
     */
    public static Boolean verificationConventionAjoutPotence() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_CONVENTION_POTENCE);

    }

    /**
     * Methode pour vérifier si le devis est rattaché à d'autre demandes
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean verificationDevisRattacheAutreDemande() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_SI_DEVIS_RATTACHE_AUTRES_DEMANDES);
    }

    /**
     * Methode permettant de controler si le calcul de préparation des décisions concernent d'autre gestionnaire
     * 
     * @param session
     * @throws Exception
     */
    public static Boolean verifierSiCalculConcerneAutreGestionnaire() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_VERIFIER_SI_CALCUL_CONCERNE_AUTRE_GESTIONNAIRE);
    }

    /**
     * Méthode permettant de savoir si on doit annuler uniquement les décisions liées à la même QD (Et donc aux petites
     * QDs liées) au lieu d'annuler toutes les décisions à l'état NON_VALIDER
     * 
     * @return True si on doit annuler uniquement les décisions non validées du bénéficiaire, False si on annule toutes
     *         les décisions non validées
     * @throws Exception
     */
    public static Boolean annulerUniquementDecisionsLieesAuxQd() throws Exception {
        return RFPropertiesUtils.checkProperty(RFApplication.PROPERTY_ANNULER_UNIQUEMENT_DECISIONS_LIEES_AU_QDS);
    }

}
