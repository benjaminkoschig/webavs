package globaz.corvus.helpers.process;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.decisions.IREPreparationDecision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.process.REImprimerDecisionProcess;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.process.REImprimerDecisionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;

public class REImprimerDecisionHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non javadoc)
     *
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REImprimerDecisionViewBean vb = (REImprimerDecisionViewBean) viewBean;

        try {

            // Vérification des dates de traitements, début du droit Pmt.
            // Mensuel

            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession((BSession) session);
            decision.setIdDecision(vb.getIdDecision());
            decision.retrieve();

            REDemandeRente dem = new REDemandeRente();
            dem.setSession((BSession) session);
            dem.setIdDemandeRente(vb.getIdDemandeRente());
            dem.retrieve();

            JACalendar cal = new JACalendarGregorian();
            JADate datePmtMensuel = null;

            if (!JadeStringUtil.isBlankOrZero(REPmtMensuel.getDateDernierPmt((BSession) session))) {
                datePmtMensuel = new JADate(PRDateFormater
                        .convertDate_JJxMMxAAAA_to_MMxAAAA(REPmtMensuel.getDateDernierPmt((BSession) session)));
            }

            JADate dateDebutDroit = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dem.getDateDebut()));
            JADate dateDecision = new JADate(vb.getDateDocument());
            dateDecision.setDay(1);

            if (!IREDecision.CS_ETAT_VALIDE.equals(decision.getCsEtat())) {
                if (datePmtMensuel != null) {

                    if (decision.getCsTypeDecision().equals(IREPreparationDecision.CS_TYP_PREP_DECISION_STANDARD)) {

                        if (cal.compare(dateDecision, datePmtMensuel) == JACalendar.COMPARE_FIRSTLOWER) {
                            vb._addError("JSP_PRP_D_DDECMOICON");
                        }

                        if ((cal.compare(dateDecision, datePmtMensuel) == JACalendar.COMPARE_FIRSTUPPER &&

                                (cal.compare(dateDecision, dateDebutDroit) == JACalendar.COMPARE_FIRSTUPPER
                                        || cal.compare(dateDecision, dateDebutDroit) == JACalendar.COMPARE_EQUALS))) {
                            vb._addError("JSP_PRP_D_RECDEM");
                        }
                    } else {

                        if (cal.compare(dateDecision, datePmtMensuel) != JACalendar.COMPARE_EQUALS) {
                            vb._addError("JSP_PRP_D_DDECMOICON");
                        }
                    }
                } else {
                    throw new Exception("JSP_PRP_D_RECDEM");
                }
            }
            if (!vb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                REImprimerDecisionProcess imprimerDecision = new REImprimerDecisionProcess();
                imprimerDecision.setSession((BSession) session);
                imprimerDecision.setIdDecision(vb.getIdDecision());
                imprimerDecision.setIdDemandeRente(vb.getIdDemandeRente());
                imprimerDecision.setDateDocument(vb.getDateDocument());
                imprimerDecision.setEMailAddress(vb.getEMailAddress());
                imprimerDecision.setIsSendToGed(vb.getIsSendToGed());

                BProcessLauncher.start(imprimerDecision, false);
            }
            /*
             * Si c'est une décision sur opposition, on vérifie si la propriété est configuré correctement sinon il
             * devront définir le choix du tribunal
             * Si ce n'est pas une décision sur opposition, alors on ne fait pas de vérification.
             */
            String typeCaisse = session.getRemoteApplication().getProperty(PRTiersHelper.TYPE_DE_CAISSE);
            if (decision.getCsGenreDecision().equals(IREDecision.CS_GENRE_DECISION_DECISION_SUR_OPPOSITION)) {
                if (typeCaisse.contains(PRTiersHelper.CAISSE_CANT) || typeCaisse.contains(PRTiersHelper.CAISSE_PROF)) {
                    vb.setIsTypeCorrect(true);
                } else {
                    vb.setIsTypeCorrect(false);
                }
            } else {
                vb.setIsTypeCorrect(false);
            }
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWMessage.ERREUR);
        }

    }

}
