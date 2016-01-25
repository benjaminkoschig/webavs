package globaz.osiris.helpers.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAPaiementEtrangerViewBean;
import globaz.osiris.helpers.paiement.CAProcessPaiementHelper;

/**
 * Classe : type_conteneur Description : Date de création: 7 juin 04
 * 
 * @author scr
 */
public class CAJournalOperationPaiementEtrangerHelper extends FWHelper {

    /**
     * Constructor for CAJournalOperationPaiementEtrangerHelper.
     */
    public CAJournalOperationPaiementEtrangerHelper() {
        super();
    }

    private CAPaiementEtrangerViewBean doAjouterPaiementEtranger(CAPaiementEtrangerViewBean viewBean, BSession session)
            throws Exception {
        String date = viewBean.getDate();

        String noAvs = null;

        BTransaction transaction = (BTransaction) session.newTransaction();

        try {
            noAvs = viewBean.getNoAvs1();
            noAvs += ".";
            noAvs += viewBean.getNoAvs2();
            noAvs += ".";
            noAvs += viewBean.getNoAvs3();
            noAvs += ".";
            noAvs += viewBean.getNoAvs4();

            CAProcessPaiementHelper helper = new CAProcessPaiementHelper(session, transaction);

            CACompteAnnexe compteAnnexe = null;
            try {
                helper.validateDate(date);
            } catch (globaz.osiris.file.paiement.exception.LabelNameException lne) {
                throw new Exception(session.getLabel(lne.getLabel()) + " " + lne.getExtraMessage());
            }

            try {
                compteAnnexe = helper.validateNoAvs(noAvs, false, viewBean.getQuittanceLogEcran().booleanValue());
            } catch (globaz.osiris.file.paiement.exception.LabelNameException lne) {
                throw new Exception(session.getLabel(lne.getLabel()) + " " + lne.getExtraMessage());
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }

            try {
                helper.addPaiementEtranger(compteAnnexe, noAvs, viewBean);
            } catch (globaz.osiris.file.paiement.exception.LabelNameException lne) {
                throw new Exception(session.getLabel(lne.getLabel()) + " " + lne.getExtraMessage());
            } catch (java.lang.Exception e) {
                throw new Exception(e.getMessage());
            }
        } catch (Exception e) {
            JadeLogger.trace(this, "Error(" + transaction + "," + e.getMessage() + ")");
            transaction.addErrors(e.getMessage());
            throw e;
        }

        return viewBean;
    }

    private CAPaiementEtrangerViewBean doModifierPaiementEtranger(CAPaiementEtrangerViewBean viewBean, BSession session)
            throws Exception {
        String date = viewBean.getDate();

        String noAvs = null;
        BTransaction transaction = (BTransaction) session.newTransaction();

        try {
            noAvs = viewBean.getNoAvs1();
            noAvs += ".";
            noAvs += viewBean.getNoAvs2();
            noAvs += ".";
            noAvs += viewBean.getNoAvs3();
            noAvs += ".";
            noAvs += viewBean.getNoAvs4();

            CAProcessPaiementHelper helper = new CAProcessPaiementHelper(session, transaction);

            CACompteAnnexe compteAnnexe = null;
            try {
                helper.validateDate(date);
            } catch (globaz.osiris.file.paiement.exception.LabelNameException lne) {
                throw new Exception(session.getLabel(lne.getLabel()) + " " + lne.getExtraMessage());
            }

            try {
                compteAnnexe = helper.validateNoAvs(noAvs, false, false);
            } catch (globaz.osiris.file.paiement.exception.LabelNameException lne) {
                throw new Exception(session.getLabel(lne.getLabel()) + " " + lne.getExtraMessage());
            } catch (Exception e) {
                throw e;
            }

            try {
                helper.updatePaiementEtranger(viewBean, compteAnnexe, null);

            } catch (globaz.osiris.file.paiement.exception.LabelNameException lne) {
                throw new Exception(session.getLabel(lne.getLabel()) + " " + lne.getExtraMessage());
            } catch (java.lang.Exception e) {
                throw new Exception(e.getMessage());
            }
        } catch (Exception e) {
            JadeLogger.trace(this, "Error(" + transaction + "," + e.getMessage() + ")");
            transaction.addErrors(e.getMessage());
            throw e;
        }

        return viewBean;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(FWViewBeanInterface, FWAction, BISession)
     */
    @Override
    public FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        CAPaiementEtrangerViewBean vBean = (CAPaiementEtrangerViewBean) viewBean;

        try {
            if ("ajouterAvantCompensation".equals(action.getActionPart())) {
                return doAjouterPaiementEtranger(vBean, (BSession) session);
            } else if ("modifierAvantCompensation".equals(action.getActionPart())) {
                return doModifierPaiementEtranger(vBean, (BSession) session);
            } else if ("supprimerPaiement".equals(action.getActionPart())) {
                vBean.delete(((BSession) session).newTransaction());
                return vBean;
            } else {
                throw new Exception(this.getClass().getName() + "Action not supported !!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage(e.getMessage());
            return vBean;
        }
    }

}
