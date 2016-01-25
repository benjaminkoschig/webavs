package globaz.corvus.helpers.recap;

import globaz.corvus.api.recap.IRERecapMensuelle;
import globaz.corvus.db.recap.access.RERecapElement;
import globaz.corvus.db.recap.access.RERecapElementManager;
import globaz.corvus.db.recap.access.RERecapMensuelle;
import globaz.corvus.db.recap.access.RERecapMensuelleManager;
import globaz.corvus.process.RELoadRecapMensuelleProcess;
import globaz.corvus.vb.recap.REChargerRecapMensuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Iterator;

public class REChargerRecapMensuelleHelper extends PRAbstractHelper {

    public static final String RECAP_ENVOYE = "Envoye";

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REChargerRecapMensuelleViewBean vb = (REChargerRecapMensuelleViewBean) viewBean;
        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            if (action.getActionPart().equals("charger")) {

                String vdate = vb.getDateRapport(); // format MMxAAAA
                String existRecapMensuelleEtatsNonEnvoye = idRecapMensuelle(vdate, vb.getSession());

                if (JadeStringUtil.isBlankOrZero(existRecapMensuelleEtatsNonEnvoye)) {

                    RELoadRecapMensuelleProcess process = new RELoadRecapMensuelleProcess();
                    process.setSession(vb.getSession());
                    process.setTransaction(transaction);
                    process.setDateRapportMensuel(vdate);

                    process.executeProcess();
                    if (process.isOnError() || (process.getMemoryLog().size() > 0)) {
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(process.getMemoryLog().getMessagesInString());
                    } else {
                        vb.setIdRecapMensuelle(process.getIdRecapMensuelle());
                    }

                } else {

                    if (REChargerRecapMensuelleHelper.RECAP_ENVOYE.equals(existRecapMensuelleEtatsNonEnvoye)) {
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(((BSession) session).getLabel("RECAP_ENVOYE"));
                    } else {
                        // Suppression des anciennes Recap.
                        RERecapMensuelleManager recapMensMgr = new RERecapMensuelleManager();
                        recapMensMgr.setSession((BSession) session);
                        recapMensMgr.setForIdRecapMensuelle(existRecapMensuelleEtatsNonEnvoye);
                        recapMensMgr.find(transaction);

                        Iterator iterRecapMens = recapMensMgr.iterator();

                        while (iterRecapMens.hasNext()) {
                            RERecapMensuelle recapMens = (RERecapMensuelle) iterRecapMens.next();
                            recapMens.delete(transaction);
                        }

                        RERecapElementManager recapElementMgr = new RERecapElementManager();
                        recapElementMgr.setSession((BSession) session);
                        recapElementMgr.setForIdRecapMensuelle(existRecapMensuelleEtatsNonEnvoye);
                        recapElementMgr.find(transaction);

                        Iterator iterRecapElem = recapElementMgr.iterator();

                        while (iterRecapElem.hasNext()) {
                            RERecapElement recapElem = (RERecapElement) iterRecapElem.next();
                            recapElem.delete(transaction);
                        }

                        // Chargement des nouvelles recap. depuis REINFREC
                        RELoadRecapMensuelleProcess process = new RELoadRecapMensuelleProcess();
                        process.setSession(vb.getSession());
                        process.setDateRapportMensuel(vdate);
                        process.setTransaction(transaction);
                        process.executeProcess();

                        if (process.isOnError() || (process.getMemoryLog().size() > 0)) {
                            viewBean.setMsgType(FWViewBeanInterface.ERROR);
                            viewBean.setMessage(process.getMemoryLog().getMessagesInString());
                        } else {
                            vb.setIdRecapMensuelle(process.getIdRecapMensuelle());
                        }
                    }

                }

            } else {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("ERROR ! NO ACTION");
            }
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        } finally {
            if (transaction != null) {
                try {
                    try {
                        transaction.commit();
                    } finally {
                        transaction.closeTransaction();
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
        return vb;
    }

    /**
     * Recherche l'existance d'un enregistrement de récap mensuel pour le mois donne //dans l'état ENVOYE//
     * 
     * @param vdate
     * @param session
     * @return IdRecap si en attente, RECAP_ENVOYE si envoyé, "" si n'a jamais été chargé
     */
    private String idRecapMensuelle(String vdate, BSession session) {

        RERecapMensuelle recap = new RERecapMensuelle();
        recap.setSession(session);
        recap.setAlternateKey(RERecapMensuelle.DATE_RAPPORT_KEY);
        recap.setDateRapportMensuel(vdate);
        try {
            recap.retrieve();
            if (recap.isNew()) {
                return "";
            } else {
                if (IRERecapMensuelle.CS_ETAT_ATTENTE.equals(recap.getCsEtat())) {
                    return recap.getIdRecapMensuelle();
                } else {
                    return REChargerRecapMensuelleHelper.RECAP_ENVOYE;
                }
            }
        } catch (Exception e) {
            return "";
        }
    }
}
