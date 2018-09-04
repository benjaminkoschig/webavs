package globaz.corvus.helpers.recap;

import java.lang.reflect.Method;
import globaz.corvus.api.recap.IRERecapMensuelle;
import globaz.corvus.db.recap.access.RERecapElement;
import globaz.corvus.db.recap.access.RERecapElementManager;
import globaz.corvus.db.recap.access.RERecapMensuelle;
import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

public class REDetailRecapMensuelleHelper extends PRAbstractHelper {
    public static REDetailRecapMensuelleViewBean vbOld;

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REDetailRecapMensuelleViewBean vb = (REDetailRecapMensuelleViewBean) viewBean;
        BITransaction transaction = null;

        try {

            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            if (action.getActionPart().equals("chargerAvs")) {
                // String vdate =
                // PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(vb.getDateRapport());
                vb.retrieve();
                Method methode[] = vb.getClass().getDeclaredMethods();
                vbOld = new REDetailRecapMensuelleViewBean();

                /*
                 * Copier les données avants les modifs
                 */
                for (int i = 0; i < methode.length; i++) {
                    if ((methode[i]).getName().startsWith("getElem")) {
                        RERecapElement recapElement = ((RERecapElement) methode[i].invoke(vb, new Object[0]));
                        for (int j = 0; j < methode.length; j++) {
                            if ((methode[j]).getName().startsWith("setElem" + recapElement.getCodeRecap())) {
                                methode[j].invoke(vbOld, recapElement);
                            }
                        }
                    }
                }

            } else {
                if (action.getActionPart().equals("modifierRecapAvs")
                        || action.getActionPart().equals("modifierRecapAi")) {
                    // Récupération de la recap mensuelle pour le mois en cours

                    RERecapMensuelle rerecapMens = new RERecapMensuelle();
                    rerecapMens.setSession((BSession) session);
                    rerecapMens.setIdRecapMensuelle(vb.getIdRecapMensuelle());
                    rerecapMens.retrieve();
                    // Si la recap se trouve dans l'état "attente" on enregistre
                    // les modifications
                    if (rerecapMens.getCsEtat().equals(IRERecapMensuelle.CS_ETAT_ATTENTE)) {

                        // Suppression des éléments de la recap
                        RERecapElementManager recaElemMgr = new RERecapElementManager();
                        recaElemMgr.setSession((BSession) session);
                        recaElemMgr.setForIdRecapMensuelle(rerecapMens.getIdRecapMensuelle());
                        recaElemMgr.delete(transaction);

                        if (transaction.hasErrors()) {
                            transaction.rollback();
                            vb.setMessage(transaction.getErrors().toString());
                            vb.setMsgType(FWViewBeanInterface.ERROR);
                        } else {

                            transaction.commit();

                            Method methode[] = vb.getClass().getDeclaredMethods();

                            // Sauvegarde des montants des éléments de la recap
                            for (int i = 0; i < methode.length; i++) {

                                if ((methode[i]).getName().startsWith("getElem")) {

                                    RERecapElement oldRecapBeforeModif = ((RERecapElement) methode[i]
                                            .invoke(REDetailRecapMensuelleHelper.vbOld, new Object[0]));
                                    RERecapElement oldReRecapElement = ((RERecapElement) methode[i].invoke(vb,
                                            new Object[0]));

                                    if (!JadeStringUtil.isBlankOrZero(oldReRecapElement.getMontant())) {
                                        RERecapElement newReRecapElement = new RERecapElement();

                                        newReRecapElement.setSession((BSession) session);
                                        newReRecapElement.setIdRecapMensuelle(rerecapMens.getIdRecapMensuelle());
                                        newReRecapElement.setMontant(oldReRecapElement.getMontant());
                                        newReRecapElement.setCodeRecap((methode[i]).getName().substring(7));
                                        newReRecapElement.setCas(oldReRecapElement.getCas());
                                        newReRecapElement.add(transaction);

                                        if (transaction.hasErrors()) {
                                            transaction.setRollbackOnly();
                                            vb.setMessage(transaction.getErrors().toString());
                                            vb.setMsgType(FWViewBeanInterface.ERROR);
                                            vbOld.setIsError(true);

                                        }
                                    }
                                }
                            }
                        }
                    } else {// Si la recap se trouve dans l'état "envoyé" on ne
                        // prend pas en compte les modifications
                        vb.setMessage(((BSession) session).getLabel("ERREUR_RECAP_DEJA_ENVOYE"));
                        vb.setMsgType(FWViewBeanInterface.ERROR);
                    }
                }
            }
        } catch (Exception e) {
            return vb;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                        vbOld.setIsError(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return vb;

    }

    private boolean fieldisModifiable(String codeRecap) {
        if (codeRecap.equals("500001") || codeRecap.equals("500002") || codeRecap.equals("500003")
                || codeRecap.equals("500004") || codeRecap.equals("500005") || codeRecap.equals("500007")
                || codeRecap.equals("501001") || codeRecap.equals("501002") || codeRecap.equals("501003")
                || codeRecap.equals("501004") || codeRecap.equals("501005") || codeRecap.equals("501007")
                || codeRecap.equals("503001") || codeRecap.equals("503002") || codeRecap.equals("503003")
                || codeRecap.equals("503004") || codeRecap.equals("503005") || codeRecap.equals("503007")) {
            return true;
        }
        return false;
    }
}
