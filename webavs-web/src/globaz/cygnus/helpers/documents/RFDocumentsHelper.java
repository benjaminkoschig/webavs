package globaz.cygnus.helpers.documents;

import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.process.RFDocumentsProcess;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.documents.RFDocumentsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * author fha
 */
public class RFDocumentsHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (validate((RFDocumentsViewBean) viewBean)) {
            // on lance le process
            RFDocumentsViewBean docViewBean = (RFDocumentsViewBean) viewBean;
            RFDocumentsProcess process = new RFDocumentsProcess();
            process.setSession((BSession) session);
            process.setEmail(docViewBean.getEmail());
            process.setDateDocument(docViewBean.getDateDocument());
            process.setIdDocument(docViewBean.getIdDocument());
            process.setIdTiers(docViewBean.getIdTiers());
            // TODO en fonction du doc on en charge plus ou moins?
            process.setRegimeLibelleRegime(docViewBean.getRegimeLibelleRegime());
            process.setRegimeMontantOctroi(docViewBean.getRegimeMontantOctroi());
            process.setRegimeDatePremierVersement(docViewBean.getRegimeDatePremierVersement());
            process.setRegimeDateEvaluationOMSV(docViewBean.getRegimeDateEvaluationOMSV());
            process.setRegimeMontantAllocationMensuelle(docViewBean.getRegimeMontantAllocationMensuelle());
            process.setRegimeDateCourrierPrecedent(docViewBean.getRegimeDateCourrierPrecedent());
            process.setRegimeMontantAllocationMensuelleRappel(docViewBean.getRegimeMontantAllocationMensuelleRappel());
            process.setRegimeMontantAllocationMensuelleApresRevision(docViewBean
                    .getRegimeMontantAllocationMensuelleApresRevision());
            process.setRegimeDateAllocationMensuelleApresRevision(docViewBean
                    .getRegimeDateAllocationMensuelleApresRevision());
            process.setIsRegimeNouveauMontantMensuel(docViewBean.getIsRegimeNouveauMontantMensuel());
            process.setRegimeDateEnvoiQuestionnaire(docViewBean.getRegimeDateEnvoiQuestionnaire());
            process.setRegimeDateDemandeIndemnisation(docViewBean.getRegimeDateDemandeIndemnisation());
            process.setRegimeDateDemandeIndemnisationRefus2(docViewBean.getRegimeDateDemandeIndemnisationRefus2());
            process.setRegimeDateLettre1_11(docViewBean.getRegimeDateLettre1_11());
            process.setRegimeDateLettre1_3(docViewBean.getRegimeDateLettre1_3());
            process.setRegimeMontantAllocationMensuelleSuppression(docViewBean
                    .getRegimeMontantAllocationMensuelleSuppression());
            process.setRegimeDateAllocationMensuelleSuppression(docViewBean
                    .getRegimeDateAllocationMensuelleSuppression());
            process.setRegimeDateEnvoiLettre1_7(docViewBean.getRegimeDateEnvoiLettre1_7());
            process.setRegimeDateEnvoiLettre1_8(docViewBean.getRegimeDateEnvoiLettre1_8());
            process.setIsRegimeRecenteRevision(docViewBean.getIsRegimeRecenteRevision());
            process.setRegimeDateCourrierPrecedent11(docViewBean.getRegimeDateCourrierPrecedent11());
            process.setRegimeDateCourrierPrecedent12(docViewBean.getRegimeDateCourrierPrecedent12());
            process.setRegimeDateCourrierPrecedent13(docViewBean.getRegimeDateCourrierPrecedent13());
            process.setRegimeDateCourrierPrecedent15(docViewBean.getRegimeDateCourrierPrecedent15());
            process.setMiseEnGed(docViewBean.getMiseEnGed());

            try {
                BProcessLauncher.start(process, false);
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
        }
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        // TODO Auto-generated method stub
        return super.execute(viewBean, action, session);
    }

    /**
     * on vérifie que tous les champs obligatoire sont effectivement remplis , que l'on a l'idDossier et que le dossier
     * est ouvert
     */
    protected Boolean validate(RFDocumentsViewBean viewBean) {
        // vérification que le dossier est ouvert
        if (viewBean.getCsEtatDossier().equals("")) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_DOSSIER_FERME");
            return false;
        }

        // Adresse email ,
        // Traité par ,
        // Type de document ,
        // Document,
        // Date sur document,
        // Date de début sont obligatoire
        if (JadeStringUtil.isEmpty(viewBean.getEmail()) || JadeStringUtil.isEmpty(viewBean.getIdGestionnaire())
                || JadeStringUtil.isEmpty(viewBean.getIdDocument())
                || JadeStringUtil.isEmpty(viewBean.getDateDocument())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_INCOMPLET");
            return false;
        }

        if (IRFCatalogueTexte.CS_DECISION_OCTROI.equals(viewBean.getIdDocument())) {
            if (!validateDecisionOctroi(viewBean)) {
                return false;
            }
        }

        if (IRFCatalogueTexte.CS_QUESTIONNAIRE.equals(viewBean.getIdDocument())) {
            if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateCourrierPrecedent11())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_LETTRE_1_1_INCORRECT");
                return false;
            }
        }

        if (IRFCatalogueTexte.CS_DECISION_REFUS.equals(viewBean.getIdDocument())) {
            if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateCourrierPrecedent12())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_LETTRE_1_2_INCORRECT");
                return false;
            }
        }

        if (IRFCatalogueTexte.CS_PRESCRIPTION_DIETETIQUE_MEDECIN.equals(viewBean.getIdDocument())) {
            if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateCourrierPrecedent13())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_LETTRE_1_3_INCORRECT");
                return false;
            }
        }

        if (IRFCatalogueTexte.CS_DECISION_REFUS2.equals(viewBean.getIdDocument())) {
            if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateEvaluationOMSV())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_DECISION_REFUS_2_INCORRECT");
                return false;
            }
            if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateDemandeIndemnisationRefus2())) {
                RFUtils.setMsgErreurViewBean(viewBean,
                        "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_DEMANDE_INDEMNISATION_REFUS_2_INCORRECT");
                return false;
            }
        }

        if (IRFCatalogueTexte.CS_QUESTIONNAIRE_REVISION.equals(viewBean.getIdDocument())) {
            if (!JadeNumericUtil.isNumericPositif(viewBean.getRegimeMontantAllocationMensuelle().replace("'", ""))) {
                RFUtils.setMsgErreurViewBean(viewBean,
                        "ERREUR_RF_DOCUMENT_FORMULAIRE_MONTANT_ALLOCATION_MENSUELLE_INCORRECT");
                return false;
            }
        }

        if (IRFCatalogueTexte.CS_RAPPEL_QUESTIONNAIRE_REVISION.equals(viewBean.getIdDocument())) {
            if (!validateRappelQuestionnaireRevision(viewBean)) {
                return false;
            }
        }

        if (IRFCatalogueTexte.CS_DROIT_MAINTENU_REVISION.equals(viewBean.getIdDocument())) {
            if (!validateDroitMaintenuRevision(viewBean)) {
                return false;
            }
        }

        if (IRFCatalogueTexte.CS_DECISION_SUPPRESSION.equals(viewBean.getIdDocument())) {
            if (!validateDecisionSuppression(viewBean)) {
                return false;
            }
        }

        if (IRFCatalogueTexte.CS_RAPPEL_QUESTIONNAIRE.equals(viewBean.getIdDocument())) {
            if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateEnvoiQuestionnaire())) {
                RFUtils.setMsgErreurViewBean(viewBean,
                        "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_ENVOI_QUESTIONNAIRE_INCORRECT");
                return false;
            }
        }

        if (IRFCatalogueTexte.CS_DECISION_REFUS3.equals(viewBean.getIdDocument())) {
            if (!validateDecisionRefus3(viewBean)) {
                return false;
            }
        }

        return true;
    }

    /*
     * vérification des champs propres à la décision d'octroi
     */
    protected Boolean validateDecisionOctroi(RFDocumentsViewBean viewBean) {
        if (!JadeDateUtil.isGlobazDateMonthYear(viewBean.getRegimeDatePremierVersement())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_OCTROI_INCORRECT");
            return false;
        }
        if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateCourrierPrecedent15())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_OCTROI_INCORRECT");
            return false;
        }
        if (!JadeNumericUtil.isNumericPositif(viewBean.getRegimeMontantOctroi().replace("'", ""))) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_MONTANT_OCTROI_INCORRECT");
            return false;
        }
        return true;
    }

    protected Boolean validateDecisionRefus3(RFDocumentsViewBean viewBean) {
        if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateDemandeIndemnisation())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_DEMANDE_INDEMNISATION_INCORRECT");
            return false;
        }
        if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateLettre1_3())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_LETTRE_1_3_INCORRECT");
            return false;
        }
        if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateLettre1_11())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_LETTRE_1_11_INCORRECT");
            return false;
        }

        return true;
    }

    protected Boolean validateDecisionSuppression(RFDocumentsViewBean viewBean) {
        if (!viewBean.getIsRegimeRecenteRevision()) {
            if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateEnvoiLettre1_7())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_ENVOI_LETTRE_1_7_INCORRECT");
                return false;
            }
            if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateEnvoiLettre1_8())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_ENVOI_LETTRE_1_8_INCORRECT");
                return false;
            }
        }
        if (!JadeNumericUtil.isNumericPositif(viewBean.getRegimeMontantAllocationMensuelleSuppression()
                .replace("'", ""))) {
            RFUtils.setMsgErreurViewBean(viewBean,
                    "ERREUR_RF_DOCUMENT_FORMULAIRE_MONTANT_ALLOCATION_SUPPRESSION_INCORRECT");
            return false;
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(viewBean.getRegimeDateAllocationMensuelleSuppression())) {
            RFUtils.setMsgErreurViewBean(viewBean,
                    "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_ALLOCATION_SUPPRESSION_INCORRECT");
            return false;
        }
        return true;
    }

    protected Boolean validateDroitMaintenuRevision(RFDocumentsViewBean viewBean) {
        if (viewBean.getIsRegimeNouveauMontantMensuel()) {
            if (!JadeDateUtil.isGlobazDateMonthYear(viewBean.getRegimeDateAllocationMensuelleApresRevision())) {
                RFUtils.setMsgErreurViewBean(viewBean,
                        "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_ALLOCATION_REVISION_INCORRECT");
                return false;
            }
        }
        if (!JadeNumericUtil.isNumericPositif(viewBean.getRegimeMontantAllocationMensuelleApresRevision().replace("'",
                ""))) {
            RFUtils.setMsgErreurViewBean(viewBean,
                    "ERREUR_RF_DOCUMENT_FORMULAIRE_MONTANT_ALLOCATION_REVISION_INCORRECT");
            return false;
        }
        return true;
    }

    /*
     * vérification des champs propres à la décision d'octroi
     */
    protected Boolean validateRappelQuestionnaireRevision(RFDocumentsViewBean viewBean) {
        if (!JadeDateUtil.isGlobazDate(viewBean.getRegimeDateCourrierPrecedent())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOCUMENT_FORMULAIRE_DATE_COURRIER_PRECEDENT_INCORRECT");
            return false;
        }
        if (!JadeNumericUtil.isNumericPositif(viewBean.getRegimeMontantAllocationMensuelleRappel().replace("'", ""))) {
            RFUtils.setMsgErreurViewBean(viewBean,
                    "ERREUR_RF_DOCUMENT_FORMULAIRE_MONTANT_ALLOCATION_MENSUELLE_RAPPEL_INCORRECT");
            return false;
        }
        return true;
    }

}