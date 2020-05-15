package globaz.apg.servlet;

import globaz.apg.helpers.prestation.APPrestationHelper;

/**
 * @author scr Créé le 3 mai 05
 *         <p>
 *         Descpription
 *         </p>
 */
public interface IAPActions {

    String ACTION_ANNONCEAPG = "apg.annonces.annonceAPG";
    String ACTION_ANNONCEREVISION2005 = "apg.annonces.annonceRevision2005";
    String ACTION_ANNONCEREVIVION1999 = "apg.annonces.annonceRevision1999";
    String ACTION_ANNONCESEDEX = "apg.annonces.annonceSedex";
    String ACTION_ATTESTATIONS_FISCALES = "apg.process.attestationsFiscales";
    String ACTION_CALCUL_ACOR = "apg.prestation.calculACOR";
    String ACTION_CATALOGUES = "babel.cat.document";
    String ACTION_COMPENSATIONS_LOT = "apg.lots.factureJointCompensation";
    String ACTION_CONTROLE_PRESTATIONS_APG = "apg.process.controlerPrestations";
    String ACTION_COTISATION_JOINT_REPARTITION = "apg.prestation.cotisationJointRepartition";
    String ACTION_COTISATIONS_ASSURANCES = "apg.prestation.assurance";
    String ACTION_DROIT_LAPG = "apg.droits.droitLAPGJointDemande";
    String ACTION_ENFANT_APG = "apg.droits.enfantAPG";
    String ACTION_ENFANT_MAT = "apg.droits.enfantMat";
    String ACTION_ENFANT_PAN = "apg.droits.enfantPan";
    String ACTION_ENVOYER_ANNONCE = "apg.process.envoyerAnnonces";
    String ACTION_ENVOYER_CI = "apg.process.envoyerCI";
    String ACTION_FACTURES_LOT = "apg.lots.factureACompenser";
    String ACTION_GENERER_ATTESTATIONS_APG = "apg.process.genererAttestations";
    String ACTION_GENERER_COMM_DEC_AMAT = "apg.process.genererDecisionAMAT";
    String ACTION_GENERER_COMPENSATIONS = "apg.process.genererCompensations";
    String ACTION_GENERER_DECISION_REFUS = "apg.process.genererDecisionRefus";
    String ACTION_GENERER_DECOMPTES = "apg.process.genererDecomptes";
    String ACTION_GENERER_LOT = "apg.process.genererLot";
    String ACTION_GENERER_STATS_OFAS = "apg.process.genererStatsOFAS";
    String ACTION_INFO_COMPL = "apg.droits.infoCompl";
    String ACTION_INSCRIRE_CI = "apg.process.inscrireCI";
    String ACTION_LISTE_CONTROLE = "apg.process.listeControle";
    String ACTION_LISTE_PRESTATION_VERSEE = "apg.process.listePrestationVersee";
    String ACTION_LISTE_PRESTATION_CIAB = "apg.process.listePrestationCIAB";
    String ACTION_LISTE_TAXATIONS = "apg.process.listeTaxationsDefinitives";
    String ACTION_LISTE_RECAPITULATION_ANNONCE = "apg.process.recapitulationAnnonce";
    String ACTION_LOTS = "apg.lots.lot";
    String ACTION_PERE_MAT = "apg.droits.pereMat";
    String ACTION_PERIODE = "apg.droits.periodeAPG";
    String ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT = "apg.prestation.prestationJointLotTiersDroit";
    String ACTION_PRESTATIONS = "apg.prestation.prestation";
    String ACTION_RECAPITUALATIF_DROIT_APG = "apg.droits.recapitulatifDroitAPG";
    String ACTION_RECAPITUALATIF_DROIT_MAT = "apg.droits.recapitulatifDroitMat";
    String ACTION_RECAPITUALATIF_DROIT_PAN = "apg.droits.recapitulatifDroitPan";
    String ACTION_REPARTITION_PAIEMENTS = "apg.prestation.repartitionPaiements";
    String ACTION_SAISIE_CARTE_AMAT = "apg.droits.droitMatP";
    String ACTION_SAISIE_CARTE_APG = "apg.droits.droitAPGP";
    String ACTION_SAISIE_CARTE_PAN = "apg.droits.droitPan";
    String ACTION_SAISIE_CARTE_PAN_SITUATION = "apg.droits.droitPanSituation";
    String ACTION_SITUATION_PROFESSIONNELLE = "apg.droits.situationProfessionnelle";
    String ACTION_TEXTE_JOINT_CATALOGUE = "apg.cattxt.texteJointCatalogue";
    String ACTION_TEXTE_JOINT_CATALOGUE_SAISIE = "apg.cattxt.texteJointCatalogueSaisie";
    String ACTION_CONTROLE_PRESTATION_CALCULEES = APPrestationHelper.ACTION_CONTROLE_PRESTATION_CALCULEES;
    String ACTION_AFFICHER_ERREURS_VALIDATION_PRESTATION = "apg.prestation.actionAfficherErreurValidationPrestation";
    String ACTION_GENERER_DROIT_PAN_MENSUEL = "apg.process.genererDroitPandemieMensuel";
    String ACTION_LISTE_PANDEMIE_CONTROLE = "apg.process.listePandemieControle";
    String ACTION_PANDEMIE_FIN_DU_DROIT = "apg.droits.finDroit";

}
