package globaz.apg.servlet;

import globaz.apg.helpers.prestation.APPrestationHelper;

/**
 * @author scr Créé le 3 mai 05
 *         <p>
 *         Descpription
 *         </p>
 */
public interface IAPActions {

    public static final String ACTION_ANNONCEAPG = "apg.annonces.annonceAPG";
    public static final String ACTION_ANNONCEREVISION2005 = "apg.annonces.annonceRevision2005";
    public static final String ACTION_ANNONCEREVIVION1999 = "apg.annonces.annonceRevision1999";
    public static final String ACTION_ANNONCESEDEX = "apg.annonces.annonceSedex";
    public static final String ACTION_ATTESTATIONS_FISCALES = "apg.process.attestationsFiscales";
    public static final String ACTION_CALCUL_ACOR = "apg.prestation.calculACOR";
    public static final String ACTION_CATALOGUES = "babel.cat.document";
    public static final String ACTION_COMPENSATIONS_LOT = "apg.lots.factureJointCompensation";
    public static final String ACTION_CONTROLE_PRESTATIONS_APG = "apg.process.controlerPrestations";
    public static final String ACTION_COTISATION_JOINT_REPARTITION = "apg.prestation.cotisationJointRepartition";
    public static final String ACTION_COTISATIONS_ASSURANCES = "apg.prestation.assurance";
    public static final String ACTION_DROIT_LAPG = "apg.droits.droitLAPGJointDemande";
    public static final String ACTION_ENFANT_APG = "apg.droits.enfantAPG";
    public static final String ACTION_ENFANT_MAT = "apg.droits.enfantMat";
    public static final String ACTION_ENVOYER_ANNONCE = "apg.process.envoyerAnnonces";
    public static final String ACTION_ENVOYER_CI = "apg.process.envoyerCI";
    public static final String ACTION_FACTURES_LOT = "apg.lots.factureACompenser";
    public static final String ACTION_GENERER_ATTESTATIONS_APG = "apg.process.genererAttestations";
    public static final String ACTION_GENERER_COMM_DEC_AMAT = "apg.process.genererDecisionAMAT";
    public static final String ACTION_GENERER_COMPENSATIONS = "apg.process.genererCompensations";
    public static final String ACTION_GENERER_DECISION_REFUS = "apg.process.genererDecisionRefus";
    public static final String ACTION_GENERER_DECOMPTES = "apg.process.genererDecomptes";
    public static final String ACTION_GENERER_LOT = "apg.process.genererLot";
    public static final String ACTION_GENERER_STATS_OFAS = "apg.process.genererStatsOFAS";
    public static final String ACTION_INFO_COMPL = "apg.droits.infoCompl";
    public static final String ACTION_INSCRIRE_CI = "apg.process.inscrireCI";
    public static final String ACTION_LISTE_CONTROLE = "apg.process.listeControle";
    public static final String ACTION_LISTE_PRESTATION_VERSEE = "apg.process.listePrestationVersee";
    public static final String ACTION_LISTE_RECAPITULATION_ANNONCE = "apg.process.recapitulationAnnonce";
    public static final String ACTION_LOTS = "apg.lots.lot";
    public static final String ACTION_PERE_MAT = "apg.droits.pereMat";
    public static final String ACTION_PERIODE = "apg.droits.periodeAPG";
    public static final String ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT = "apg.prestation.prestationJointLotTiersDroit";
    public static final String ACTION_PRESTATIONS = "apg.prestation.prestation";
    public static final String ACTION_RECAPITUALATIF_DROIT_APG = "apg.droits.recapitulatifDroitAPG";
    public static final String ACTION_RECAPITUALATIF_DROIT_MAT = "apg.droits.recapitulatifDroitMat";
    public static final String ACTION_REPARTITION_PAIEMENTS = "apg.prestation.repartitionPaiements";
    public static final String ACTION_SAISIE_CARTE_AMAT = "apg.droits.droitMatP";
    public static final String ACTION_SAISIE_CARTE_APG = "apg.droits.droitAPGP";
    public static final String ACTION_SITUATION_PROFESSIONNELLE = "apg.droits.situationProfessionnelle";
    public static final String ACTION_TEXTE_JOINT_CATALOGUE = "apg.cattxt.texteJointCatalogue";
    public static final String ACTION_TEXTE_JOINT_CATALOGUE_SAISIE = "apg.cattxt.texteJointCatalogueSaisie";
    public static final String ACTION_CONTROLE_PRESTATION_CALCULEES = APPrestationHelper.ACTION_CONTROLE_PRESTATION_CALCULEES;
    public static final String ACTION_AFFICHER_ERREURS_VALIDATION_PRESTATION = "apg.prestation.actionAfficherErreurValidationPrestation";

}
