package globaz.corvus.helpers.decisions;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.helpers.process.REValiderDecisionsHelper;
import globaz.corvus.vb.decisions.REDecisionsListViewBean;
import globaz.corvus.vb.demandes.REDemandeRenteJointDemandeViewBean;
import globaz.corvus.vb.process.REValiderDecisionsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.helpers.PRAbstractHelper;

public class REValiderGroupeDecisionsHelper extends PRAbstractHelper {

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        REDemandeRenteJointDemandeViewBean vb = (REDemandeRenteJointDemandeViewBean) viewBean;

        BITransaction transaction = null;
        StringBuilder errorMessageBuilder = new StringBuilder();

        try {
            // Pour chaque demande sélectionnée
            for (String idDemandeRenteToSplit : vb.getListeIdDemande()) {

                // WEBAVS - 3497 -> on a besoin de l'idDemandePrestation pour faire le lien avec PRDEMAP || split de
                // idDemandeRente_idDemandePrestation
                String[] tableIDsSplitted = idDemandeRenteToSplit.split("_");

                String idDemandeRente = tableIDsSplitted[0];
                String idDemandePrestation = tableIDsSplitted[1];

                /*
                 * LGA : BZ 6744 -6984 -4570
                 * L'appel à la méthode REValiderDecisionsHelper.tttValider(...) va clore la transaction à la fin de son
                 * job. Il est donc opportun de recréer une nouvelle transaction pour chaque décision que l'on va
                 * traiter. Sans cela, on est sur d'avoir des problèmes très bizarre ;)
                 */
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                // Retrieve de la décision
                REDecisionsListViewBean mgr = new REDecisionsListViewBean();
                mgr.setSession((BSession) session);
                mgr.setForIdDemandeRente(idDemandeRente);
                mgr.find(transaction, BManager.SIZE_NOLIMIT);

                if (mgr.isEmpty()) {
                    errorMessageBuilder.append("Pas de décision pour la demande : ").append(vb.getIdDemandeRente());
                } else {
                    REDecisionEntity decision = (REDecisionEntity) mgr.getFirstEntity();

                    PRDemande demande = new PRDemande();
                    demande.setSession((BSession) session);
                    demande.setIdDemande(idDemandePrestation);
                    demande.retrieve();

                    if (!demande.isNew()) {
                        vb.setIdTiersRequerant(demande.getIdTiers());
                    }

                    REValiderDecisionsViewBean validerVb = new REValiderDecisionsViewBean();
                    validerVb.setIdDemandeRente(decision.getIdDemandeRente());
                    validerVb.setIdDecision(decision.getIdDecision());
                    validerVb.setIdTiersRequerant(vb.getIdTiersRequerant());

                    // Traitement de validation des décisions en bloc
                    REValiderDecisionsHelper helper = new REValiderDecisionsHelper();
                    helper.tttValider((BSession) session, validerVb, (BTransaction) transaction);

                    if (FWViewBeanInterface.ERROR.equals(validerVb.getMsgType())) {
                        if (errorMessageBuilder.length() > 0) {
                            errorMessageBuilder.append("\n");
                        }
                        errorMessageBuilder.append(validerVb.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            if (errorMessageBuilder.length() > 0) {
                errorMessageBuilder.append("\n");
            }
            errorMessageBuilder.append(e.getMessage());
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    if (errorMessageBuilder.length() > 0) {
                        errorMessageBuilder.append("\n");
                    }
                    errorMessageBuilder.append(e.getMessage());
                } finally {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                        if (errorMessageBuilder.length() > 0) {
                            errorMessageBuilder.append("\n");
                        }
                        errorMessageBuilder.append(e.getMessage());
                    }
                }
            }
        }

        if (errorMessageBuilder.length() > 0) {
            vb.setMessage(errorMessageBuilder.toString());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        }

    }

}
