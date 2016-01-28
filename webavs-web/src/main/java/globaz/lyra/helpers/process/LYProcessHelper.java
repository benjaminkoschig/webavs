package globaz.lyra.helpers.process;

import globaz.corvus.vb.echeances.REAnalyserEcheancesAjaxViewBean;
import globaz.corvus.vb.echeances.REDiminutionRenteEnfantAjaxViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.lyra.process.LYAbstractEcheanceProcess;
import globaz.lyra.vb.process.LYProcessAjaxViewBean;
import globaz.perseus.process.echeance.PFEcheanceProcess;
import globaz.perseus.vb.echeance.PFEcheanceAjaxViewBean;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.corvus.process.echeances.diminution.REDiminutionRentePourEnfantProcess;
import ch.globaz.corvus.process.echeances.travauxaeffectuer.REListeTravauxAEffectuerProcess;

public class LYProcessHelper extends FWHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        LYProcessAjaxViewBean ajaxViewBean = (LYProcessAjaxViewBean) viewBean;

        Class<?> processClass;
        try {
            processClass = Class.forName(ajaxViewBean.getProcessPath());
            LYAbstractEcheanceProcess process = (LYAbstractEcheanceProcess) processClass.newInstance();
            process.setEmailAddress(ajaxViewBean.getAdresseEmail());
            process.setMoisTraitement(ajaxViewBean.getMoisTraiement());

            if (ajaxViewBean.getProcessPath().endsWith(REListeTravauxAEffectuerProcess.class.getName())) {

                REAnalyserEcheancesAjaxViewBean analyserEcheancesViewBean = (REAnalyserEcheancesAjaxViewBean) ajaxViewBean;
                REListeTravauxAEffectuerProcess processTravauxAEffectuerNew = (REListeTravauxAEffectuerProcess) process;

                processTravauxAEffectuerNew.setIsAjournement(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsAjournement()));
                processTravauxAEffectuerNew.setIsAjournementDOC(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsAjournementDOC()));
                processTravauxAEffectuerNew.setIsAjournementGED(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsAjournementGED()));

                processTravauxAEffectuerNew.setIsAutresEcheance(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsAutresEcheances()));

                processTravauxAEffectuerNew.setIsCertificatDeVie(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsCertificatDeVie()));

                processTravauxAEffectuerNew.setIsEcheanceEtude(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsEcheanceEtude()));
                processTravauxAEffectuerNew.setIsEcheanceEtudeDOC(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsEcheanceEtudeDOC()));
                processTravauxAEffectuerNew.setIsEcheanceEtudeGED(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsEcheanceEtudeGED()));

                processTravauxAEffectuerNew.setIsEnfantDe18ans(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsEnfantDe18ans()));
                processTravauxAEffectuerNew.setIsEnfantDe18ansDOC(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsEnfantDe18ansDOC()));
                processTravauxAEffectuerNew.setIsEnfantDe18ansGED(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsEnfantDe18ansGED()));

                processTravauxAEffectuerNew.setIsEnfantDe25ans(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsEnfantDe25ans()));
                processTravauxAEffectuerNew.setIsEnfantDe25ansDOC(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsEnfantDe25ansDOC()));
                processTravauxAEffectuerNew.setIsEnfantDe25ansGED(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsEnfantDe25ansGED()));

                processTravauxAEffectuerNew.setIsFemmeArrivantAgeVieillesse(Boolean
                        .parseBoolean(analyserEcheancesViewBean.getIsFemmeArrivantAgeVieillesse()));
                processTravauxAEffectuerNew.setIsFemmeArrivantAgeVieillesseDOC(Boolean
                        .parseBoolean(analyserEcheancesViewBean.getIsFemmeArrivantAgeVieillesseDOC()));
                processTravauxAEffectuerNew.setIsFemmeArrivantAgeVieillesseGED(Boolean
                        .parseBoolean(analyserEcheancesViewBean.getIsFemmeArrivantAgeVieillesseGED()));

                processTravauxAEffectuerNew.setIsHommeArrivantAgeVieillesse(Boolean
                        .parseBoolean(analyserEcheancesViewBean.getIsHommeArrivantAgeVieillesse()));
                processTravauxAEffectuerNew.setIsHommeArrivantAgeVieillesseDOC(Boolean
                        .parseBoolean(analyserEcheancesViewBean.getIsHommeArrivantAgeVieillesseDOC()));
                processTravauxAEffectuerNew.setIsHommeArrivantAgeVieillesseGED(Boolean
                        .parseBoolean(analyserEcheancesViewBean.getIsHommeArrivantAgeVieillesseGED()));

                processTravauxAEffectuerNew.setIsRenteDeVeuf(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsRenteDeVeuf()));
                processTravauxAEffectuerNew.setIsRenteDeVeufDOC(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsRenteDeVeufDOC()));
                processTravauxAEffectuerNew.setIsRenteDeVeufGED(Boolean.parseBoolean(analyserEcheancesViewBean
                        .getIsRenteDeVeufGED()));

            } else if (ajaxViewBean.getProcessPath().endsWith(REDiminutionRentePourEnfantProcess.class.getName())) {

                REDiminutionRenteEnfantAjaxViewBean diminutionRenteViewBean = (REDiminutionRenteEnfantAjaxViewBean) ajaxViewBean;
                REDiminutionRentePourEnfantProcess processDiminutionRenteEnfant = (REDiminutionRentePourEnfantProcess) process;
                processDiminutionRenteEnfant.setIdRentesADiminuer(diminutionRenteViewBean.getIdRentesADiminuer());
                processDiminutionRenteEnfant.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE
                        .getBooleanValue());

            } else if (ajaxViewBean.getProcessPath().endsWith(PFEcheanceProcess.class.getName())) {

                PFEcheanceAjaxViewBean echeanceAjaxViewBean = (PFEcheanceAjaxViewBean) ajaxViewBean;
                PFEcheanceProcess echeanceProcess = (PFEcheanceProcess) process;

                echeanceProcess.setForCsCaisse(echeanceAjaxViewBean.getForCsCaisse());

                echeanceProcess.setIsAllocations(echeanceAjaxViewBean.getIsAllocations());
                echeanceProcess.setIsAidesEtudes(echeanceAjaxViewBean.getIsAidesEtudes());
                echeanceProcess.setIsDecisionProjetSansChoix(echeanceAjaxViewBean.getIsDecisionProjetSansChoix());
                echeanceProcess.setIsDemandesEnAttente3Mois(echeanceAjaxViewBean.getIsDemandesEnAttente3Mois());
                echeanceProcess.setIsDossierDateRevision(echeanceAjaxViewBean.getIsDossierDateRevision());
                echeanceProcess.setIsEcheanceLibre(echeanceAjaxViewBean.getIsEcheanceLibre());
                echeanceProcess.setIsEnfantDe6ans(echeanceAjaxViewBean.getIsEnfantDe6ans());
                echeanceProcess.setIsEnfantDe16ans(echeanceAjaxViewBean.getIsEnfantDe16ans());
                echeanceProcess.setIsEnfantDe18ans(echeanceAjaxViewBean.getIsEnfantDe18ans());
                echeanceProcess.setIsEnfantDe25ans(echeanceAjaxViewBean.getIsEnfantDe25ans());
                echeanceProcess.setIsEtudiantsEtApprentis(echeanceAjaxViewBean.getIsEtudiantsEtApprentis());
                echeanceProcess.setIsFemmeRetraite(echeanceAjaxViewBean.getIsFemmeRetraite());
                echeanceProcess.setIsHommeRetraite(echeanceAjaxViewBean.getIsHommeRetraite());
                echeanceProcess.setIsIndemnitesJournalieres(echeanceAjaxViewBean.getIsIndemnitesJournalieres());
                echeanceProcess.setIsRentePont(echeanceAjaxViewBean.getIsRentePont());

            }

            BProcessLauncher.start(process, false);

            viewBean.setMsgType(FWViewBeanInterface.OK);
            viewBean.setMessage("");
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("Process introuvable");
        }
    }
}
