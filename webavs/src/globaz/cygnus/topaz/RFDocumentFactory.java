/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.topaz;

import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.process.RFDocumentsProcess;
import globaz.cygnus.topaz.dentiste.RFDentistesDemandeDocumentsManquantsOO;
import globaz.cygnus.topaz.dentiste.RFDentistesDemandeDocumentsManquantsQMDOO;
import globaz.cygnus.topaz.dentiste.RFDentistesDemandeTraitementOrthodontiqueQMDOO;
import globaz.cygnus.topaz.dentiste.RFDentistesEnvoiDevisMedecinOO;
import globaz.cygnus.topaz.dentiste.RFDentistesEnvoiDevisTraitementOrthodontiqueMedecinOO;
import globaz.cygnus.topaz.dentiste.RFDentistesEnvoiDossierMedecinOO;
import globaz.cygnus.topaz.dentiste.RFDentistesEnvoiFactureEtrangerOO;
import globaz.cygnus.topaz.dentiste.RFDentistesEnvoiFactureMedecinOO;
import globaz.cygnus.topaz.dentiste.RFDentistesEnvoiRappelMedecinOO;
import globaz.cygnus.topaz.dentiste.RFDentistesLettreAssureReponseMedecinDevisOO;
import globaz.cygnus.topaz.dentiste.RFDentistesLettreDevisEtrangerOO;
import globaz.cygnus.topaz.dentiste.RFDentistesRappelDemandeDocumentsManquantsQMDOO;
import globaz.cygnus.topaz.litElectrique.RFBonLivraisonOO;
import globaz.cygnus.topaz.litElectrique.RFQuestionnaireCCJUOO;
import globaz.cygnus.topaz.menage.RFMenageDemandeEvaluationCMSOO;
import globaz.cygnus.topaz.menage.RFMenageDemandeReevaluationCMSOO;
import globaz.cygnus.topaz.menage.RFMenageRappelDemandeReevaluationCMSOO;
import globaz.cygnus.topaz.moyensAuxiliaires.RFAnnexeMoyensAuxiliaireBonAchatAccuseReceptionBeneficiairePcOO;
import globaz.cygnus.topaz.moyensAuxiliaires.RFBonAchatMoyensAuxiliairesOO;
import globaz.cygnus.topaz.moyensAuxiliaires.RFBonMoyensAuxiliairesOO;
import globaz.cygnus.topaz.regime.RFDecisionOctroiOO;
import globaz.cygnus.topaz.regime.RFDecisionRefus1OO;
import globaz.cygnus.topaz.regime.RFDecisionRefus2OO;
import globaz.cygnus.topaz.regime.RFDecisionRefus3OO;
import globaz.cygnus.topaz.regime.RFDecisionSuppressionOO;
import globaz.cygnus.topaz.regime.RFDemandeEvaluationCMSOO;
import globaz.cygnus.topaz.regime.RFDroitMaintenuRevisionsOO;
import globaz.cygnus.topaz.regime.RFFormulaireCompleterCMSOO;
import globaz.cygnus.topaz.regime.RFPrescriptionDietetiqueFormulaireOO;
import globaz.cygnus.topaz.regime.RFPrescriptionDietetiqueMedecinOO;
import globaz.cygnus.topaz.regime.RFQuestionnaireRevisionsOO;
import globaz.cygnus.topaz.regime.RFRappelQuestionnaireRemplirOO;
import globaz.cygnus.topaz.regime.RFRappelQuestionnaireRevisionsOO;
import globaz.cygnus.topaz.regime.RFRegimeQuestionnaireOO;
import globaz.cygnus.topaz.restitution.RFRestitutionSuiteAuDecesBeneficiaireOO;
import globaz.cygnus.topaz.restitution.RFRestitutionSuiteNouveauCalculRetroactifOO;
import globaz.jade.print.server.JadePrintDocumentContainer;

/**
 * author fha
 */
public class RFDocumentFactory {

    protected JadePrintDocumentContainer documentContainer;

    public JadePrintDocumentContainer remplir(RFDocumentsProcess process, boolean miseEnGed) throws Exception {

        documentContainer = new JadePrintDocumentContainer();

        // selon le document dans lequel on arrive
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_QUESTIONNAIRE)) {
            return new RFRegimeQuestionnaireOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DECISION_REFUS)) {
            return new RFDecisionRefus1OO().remplir(documentContainer, process, miseEnGed);
        }

        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_ANNEXE_PRESCRIPTION_DIETETIQUE_FORMULAIRE)) {
            documentContainer = new RFPrescriptionDietetiqueMedecinOO().remplir(documentContainer, process, miseEnGed);
            documentContainer = new RFPrescriptionDietetiqueFormulaireOO().remplir(documentContainer, process,
                    miseEnGed);

            return documentContainer;
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DEMANDE_EVALUTION_CMS)) {
            documentContainer = new RFDemandeEvaluationCMSOO().remplir(documentContainer, process, miseEnGed);
            documentContainer = new RFFormulaireCompleterCMSOO().remplir(documentContainer, process, miseEnGed);

            return documentContainer;
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DECISION_OCTROI)) {
            return new RFDecisionOctroiOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DECISION_REFUS2)) {
            return new RFDecisionRefus2OO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_QUESTIONNAIRE_REVISION)) {
            return new RFQuestionnaireRevisionsOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_RAPPEL_QUESTIONNAIRE_REVISION)) {
            return new RFRappelQuestionnaireRevisionsOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DROIT_MAINTENU_REVISION)) {
            return new RFDroitMaintenuRevisionsOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DECISION_SUPPRESSION)) {
            return new RFDecisionSuppressionOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_RAPPEL_QUESTIONNAIRE)) {
            return new RFRappelQuestionnaireRemplirOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DECISION_REFUS3)) {
            return new RFDecisionRefus3OO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_BON_LIVRAISON)) {
            return new RFBonLivraisonOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_BON_REMISE_PRET)) {
            return new RFBonMoyensAuxiliairesOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_BON_ACHAT_REMISE_PRET)) {
            return new RFBonAchatMoyensAuxiliairesOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_FACTURE_MEDECIN)) {
            return new RFDentistesEnvoiFactureMedecinOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DEVIS_MEDECIN)) {
            return new RFDentistesEnvoiDevisMedecinOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DOSSIER_MEDECIN)) {
            return new RFDentistesEnvoiDossierMedecinOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_RAPPEL_MEDECIN)) {
            return new RFDentistesEnvoiRappelMedecinOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_PRECISIONS_DENTISTE)) {
            return new RFDentistesDemandeDocumentsManquantsQMDOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_RAPPEL_PRECISIONS_DENTISTE)) {
            return new RFDentistesRappelDemandeDocumentsManquantsQMDOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_MEDECIN_FACTURE_ETRANGER)) {
            return new RFDentistesEnvoiFactureEtrangerOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_REFUS_DEVIS_ETRANGER)) {
            return new RFDentistesLettreDevisEtrangerOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_PRECISIONS_TRAITEMENT_ORTHODONTIQUE_DENTISTE)) {
            return new RFDentistesDemandeTraitementOrthodontiqueQMDOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DEVIS_TRAITEMENT_ORTHODONTIQUE_MEDECIN)) {
            return new RFDentistesEnvoiDevisTraitementOrthodontiqueMedecinOO().remplir(documentContainer, process,
                    miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DEMANDE_EVALUATION_BESOINS_CMS)) {
            return new RFMenageDemandeEvaluationCMSOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DEMANDE_REEVALUATION_BESOINS_CMS)) {
            return new RFMenageDemandeReevaluationCMSOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_RAPPEL_DEMANDE_EVALUATION_BESOINS_CMS)) {
            return new RFMenageRappelDemandeReevaluationCMSOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_PRECISIONS_DENTISTE_BIS)) {
            return new RFDentistesDemandeDocumentsManquantsOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_DENTISTE_LETTRE_ASSURE_REPONSE_MEDECIN_CONSEIL_DEVIS)) {
            return new RFDentistesLettreAssureReponseMedecinDevisOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(
                IRFCatalogueTexte.CS_MOYENS_AUX_ANNEXE_BON_ACHAT_ACCUSE_RECPETION_BENEFICIARE_PC)) {
            return new RFAnnexeMoyensAuxiliaireBonAchatAccuseReceptionBeneficiairePcOO().remplir(documentContainer,
                    process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_LIT_ELECTRIQUE_QUESTIONNAIRE_CCJU)) {
            return new RFQuestionnaireCCJUOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_RESTITUTION_SUITE_NOUVEAU_CALCUL_RETROACTIF)) {
            return new RFRestitutionSuiteNouveauCalculRetroactifOO().remplir(documentContainer, process, miseEnGed);
        }
        if (process.getIdDocument().equals(IRFCatalogueTexte.CS_RESTITUTION_SUITE_AU_DECES_DU_BENEFICIAIRE)) {
            return new RFRestitutionSuiteAuDecesBeneficiaireOO().remplir(documentContainer, process, miseEnGed);
        }

        return null;
    }
}
