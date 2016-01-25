package globaz.phenix.helpers.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationImprimerViewBean;
import globaz.phenix.process.documentsItext.CPProcessImprimerCommunicationEnvoi;
import globaz.phenix.process.listes.CPProcessListeCommunicationsFiscales;

public class CPCommunicationFiscaleAffichageHelper extends FWHelper {

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("imprimer".equals(action.getActionPart()) && viewBean instanceof CPCommunicationImprimerViewBean) {
            CPCommunicationImprimerViewBean vb = (CPCommunicationImprimerViewBean) viewBean;

            if (Boolean.TRUE.equals(vb.getImpressionListe())) {

                CPProcessListeCommunicationsFiscales process = new CPProcessListeCommunicationsFiscales();
                process.setEMailAddress(vb.getEMailAddress());
                process.setForCanton(vb.getCanton());
                if (Boolean.TRUE.equals(vb.getDateEnvoiVide())) {
                    process.setDateEnvoiVide(Boolean.TRUE);
                } else {
                    process.setDateEnvoiVide(Boolean.FALSE);
                }
                process.setDateEdition(vb.getDateEdition());
                process.setDateEnvoi(vb.getDateEnvoi());
                process.setForGenreAffilie(vb.getGenreAffilie());
                process.setAnneeDecision(vb.getAnneeDecision());
                // Avec année encours
                if (JadeStringUtil.isEmpty(vb.getAnneeDecision())) {
                    if (Boolean.TRUE.equals(vb.getWithAnneeEnCours())) {
                        process.setWithAnneeEnCours(Boolean.TRUE);
                    } else {
                        process.setWithAnneeEnCours(Boolean.FALSE);
                    }
                } else {
                    process.setWithAnneeEnCours(Boolean.FALSE);
                }
                process.setSession(vb.getSession());
                process.start();
                if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    viewBean.setMsgType(process.getMsgType());
                    viewBean.setMessage(process.getMessage());
                }
            } else {
                try {
                    CPProcessImprimerCommunicationEnvoi process = new CPProcessImprimerCommunicationEnvoi();
                    process.setEMailAddress(vb.getEMailAddress());
                    process.setForCanton(vb.getCanton());
                    if (Boolean.TRUE.equals(vb.getDateEnvoiVide())) {
                        process.setDateEnvoiVide(Boolean.TRUE);
                    } else {
                        process.setDateEnvoiVide(Boolean.FALSE);
                    }
                    process.setForGenreAffilie(vb.getGenreAffilie());
                    process.setAnneeDecision(vb.getAnneeDecision());
                    process.setDateEdition(vb.getDateEdition());
                    process.setDateEnvoi(vb.getDateEnvoi());
                    // Avec année encours
                    if (JadeStringUtil.isEmpty(vb.getAnneeDecision())) {
                        if (Boolean.TRUE.equals(vb.getWithAnneeEnCours())) {
                            process.setWithAnneeEnCours(Boolean.TRUE);
                        } else {
                            process.setWithAnneeEnCours(Boolean.FALSE);
                        }
                    } else {
                        process.setWithAnneeEnCours(Boolean.FALSE);
                    }
                    process.setSession(vb.getSession());
                    process.start();
                    if (process.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                        viewBean.setMsgType(process.getMsgType());
                        viewBean.setMessage(process.getMessage());
                    }
                } catch (Exception e) {
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(e.toString());
                }
            }
        }
        return super.execute(viewBean, action, session);
    }
}
