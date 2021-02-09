/*
 * Créé le 5 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.helpers.droits;

import globaz.apg.ApgServiceLocator;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.exceptions.APWrongViewBeanTypeException;
import globaz.apg.vb.droits.APDroitAPGPViewBean;
import globaz.apg.vb.droits.APDroitMatPViewBean;
import globaz.apg.vb.prestation.APValidationPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APDroitMatPHelper extends APAbstractDroitPHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._add(viewBean, action, session);

        APDroitMatPViewBean droitVB = (APDroitMatPViewBean) viewBean;
        // Traitement des breakRules
        traiterBreakRules(droitVB.getRulesToBreak(), droitVB.getIdDroit(), (BSession) session);
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._update(viewBean, action, session);

        APDroitMatPViewBean droitVB = (APDroitMatPViewBean) viewBean;
        // Traitement des breakRules
        traiterBreakRules(droitVB.getRulesToBreak(), droitVB.getIdDroit(), (BSession) session);
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        if (action.getActionPart().equals("finaliserCreationDroit")) {
            try {
                return finaliserCreationDroit(viewBean, action, (BSession) session);
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.toString());
                return viewBean;
            }
        } else {
            return super.execute(viewBean, action, session);
        }
    }

    /**
     * Finalisation du droit. En gros ça se résume à mettre à jour les breakRules si besoin
     * 
     * @param vb
     * @param action
     * @param iSession
     * @throws Exception
     */
    public FWViewBeanInterface finaliserCreationDroit(FWViewBeanInterface vb, FWAction action, BSession iSession)
            throws Exception {
        if (!(vb instanceof APValidationPrestationViewBean)) {
            throw new APWrongViewBeanTypeException(
                    "Wrong viewBean type received for doing action finaliserCreationDroit. it must be from type APValidationPrestationViewBean");
        }
        APValidationPrestationViewBean viewBean = (APValidationPrestationViewBean) vb;
        BSession session = iSession;
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            ApgServiceLocator.getEntityService().remplacerBreakRulesDuDroit(session, transaction,
                    viewBean.getIdDroit(), viewBean.getBreakRules());
            // Si le traitement s'est bien passé, on retourne le bon viewBean pour aller sur l'affichage du droit
            APDroitMaternite droit = ApgServiceLocator.getEntityService().getDroitMaternite(session, transaction,
                    viewBean.getIdDroit());
            APDroitAPGPViewBean actionAfficherViewBean = new APDroitAPGPViewBean();
            droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_VALIDE);
            droit.update();
            actionAfficherViewBean.setDroit(droit);

            if (!hasErrors(session, transaction)) {
                transaction.commit();
            }
            return actionAfficherViewBean;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            String message = "Exception lors de la suppression du droit  :" + exception.toString();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(message);
            throw new Exception(message);
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @param session
     * @param transaction
     * @return
     */
    private boolean hasErrors(BSession session, BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly();
    }
}
