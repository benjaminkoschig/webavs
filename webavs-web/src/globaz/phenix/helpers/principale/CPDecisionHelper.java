package globaz.phenix.helpers.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.phenix.db.principale.CPDecisionImprimerLotViewBean;
import globaz.phenix.db.principale.CPDecisionViewBean;
import globaz.phenix.db.principale.CPEnteteViewBean;
import globaz.phenix.process.CPProcessCalculCotisation;
import globaz.phenix.process.CPProcessDevalidation;
import globaz.phenix.process.documentsItext.CPProcessImprimerDecisionAgence;
import java.util.Iterator;

/**
 * Insérez la description du type ici. Date de création : (27.11.2002 10:37:05)
 * 
 * @author: Administrator
 */
public class CPDecisionHelper extends FWHelper {
    /**
     * Commentaire relatif au constructeur TITiersHelper.
     */
    public CPDecisionHelper() {
        super();

    }

    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (viewBean instanceof CPEnteteViewBean) {
            ((CPEnteteViewBean) viewBean).retrieve();
        } else {
            super._chercher(viewBean, action, session);
        }
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        if (viewBean instanceof CPDecisionViewBean) {
            if (JadeStringUtil.isBlankOrZero(((CPDecisionViewBean) viewBean).getIdDecision())) { // nous
                // sommes
                // dans
                // le
                // cas
                // d'un
                // ajout
                ((CPDecisionViewBean) viewBean)._initEcran();
            } else {
                ((CPDecisionViewBean) viewBean).retrieve();
                FWHelper.afterExecute(viewBean);
            }
        } else {
            super._retrieve(viewBean, action, session);
        }

    }

    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action,
            globaz.globall.api.BISession session) {

        if ("devalider".equals(action.getActionPart()) && (viewBean instanceof CPDecisionViewBean)) {
            CPDecisionViewBean db = (CPDecisionViewBean) viewBean;

            CPProcessDevalidation devalidation = new CPProcessDevalidation();
            devalidation.setIdDecision(db.getIdDecision());
            devalidation.setISession(db.getSession());
            devalidation.setSendMailOnError(true);
            devalidation.setSendCompletionMail(false);
            try {
                devalidation.executeProcess();
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.toString());
            }

        }

        if ("calculer".equals(action.getActionPart()) && (viewBean instanceof CPDecisionViewBean)) {
            CPDecisionViewBean db = (CPDecisionViewBean) viewBean;

            CPProcessCalculCotisation calcul = new CPProcessCalculCotisation();
            calcul.setIdDecision(db.getIdDecision());
            calcul.setISession(db.getSession());
            calcul.setSendMailOnError(true);
            calcul.setSendCompletionMail(false);
            // // calcul.setEMailAddress("hna@globaz.ch");
            try {
                calcul.executeProcess();
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.toString());
            }
        }

        if ("initCreer".equals(action.getActionPart()) && (viewBean instanceof CPDecisionViewBean)) {
            try {
                ((CPDecisionViewBean) viewBean).retrieve();
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.toString());
            }
        }

        if ("imprimerLot".equals(action.getActionPart()) && (viewBean instanceof CPDecisionImprimerLotViewBean)) {
            try {
                ((CPDecisionImprimerLotViewBean) viewBean)._init();
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        if ("initialiser".equals(action.getActionPart()) && (viewBean instanceof CPDecisionViewBean)) {
            try {
                ((CPDecisionViewBean) viewBean).retrieve();
                ((CPDecisionViewBean) viewBean)._controle(null);
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        if (("imprimer".equals(action.getActionPart()) || "duplicata".equals(action.getActionPart()))
                && (viewBean instanceof CPDecisionViewBean)) {

            CPDecisionViewBean vb = (CPDecisionViewBean) viewBean;
            CPProcessImprimerDecisionAgence process = new CPProcessImprimerDecisionAgence();
            try {

                process.setSession(vb.getSession());
                process.setIdTiers(vb.getIdTiers());
                process.setIdAffiliation(vb.getIdAffiliation());
                process.setIdDecision(vb.getIdDecision());
                process.setSendMailOnError(false);
                process.setSendCompletionMail(false);
                process.setControleTransaction(true);
                process.setDuplicata(vb.getDuplicata());
                process.setEnvoiGed(Boolean.FALSE);
                process.setAffichageEcran(Boolean.TRUE);
                process.executeProcess();
            } catch (Exception e) {
                process.setMessage(e.toString());
                process.setMsgType(FWViewBeanInterface.ERROR);
            }

            String docListe = "";
            if (process.getAttachedDocuments().size() > 0) {
                for (Iterator<?> iter = process.getAttachedDocuments().iterator(); iter.hasNext();) {
                    JadePublishDocument document = (JadePublishDocument) iter.next();
                    docListe += "&doc=" + JadeFilenameUtil.extractFilename(document.getDocumentLocation());
                }
            }
            ((CPDecisionViewBean) viewBean).setDocListe(docListe);
        }
        return super.execute(viewBean, action, session);
    }

}
