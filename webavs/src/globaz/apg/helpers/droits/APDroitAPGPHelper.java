/*
 * Créé le 7 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.helpers.droits;

import globaz.apg.ApgServiceLocator;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.exceptions.APWrongViewBeanTypeException;
import globaz.apg.vb.droits.APDroitAPGPViewBean;
import globaz.apg.vb.prestation.APValidationPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.beans.PRPeriode;
import java.util.Collections;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APDroitAPGPHelper extends APAbstractDroitPHelper {

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

        APDroitAPGPViewBean actionAfficherViewBean = new APDroitAPGPViewBean();
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            ApgServiceLocator.getEntityService().remplacerBreakRulesDuDroit(session, transaction,
                    viewBean.getIdDroit(), viewBean.getBreakRules());
            // Si le traitement s'est bien passé, on retourne le bon viewBean pour aller sur l'affichage du droit
            APDroitAPG droit = ApgServiceLocator.getEntityService().getDroitAPG(session, transaction,
                    viewBean.getIdDroit());
            actionAfficherViewBean.setDroit(droit);

            if (!hasError(session, transaction)) {
                transaction.commit();
            }

            return actionAfficherViewBean;
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            String message = "Exception lors de la suppression du droit  :" + exception.toString();
            actionAfficherViewBean.setMsgType(FWViewBeanInterface.ERROR);
            actionAfficherViewBean.setMessage(message);
            throw new Exception(message);
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        viewBean.setISession(session);
        ((APDroitAPGPViewBean) viewBean).setModeEditionDroit(APModeEditionDroit.CREATION);
    }

    @Override
    protected void _delete(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitAPGPViewBean viewBean = null;
        if (!(vb instanceof APDroitAPGPViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitAPGPHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitAPGPViewBean");
        }
        viewBean = (APDroitAPGPViewBean) vb;
        BSession session = (BSession) iSession;
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            ApgServiceLocator.getEntityService().supprimerDroitCompletAPG(session, transaction, viewBean.getIdDroit());

            if (!hasError(session, transaction)) {
                transaction.commit();
            }

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

    @Override
    protected void _retrieve(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitAPGPViewBean viewBean = null;
        if (!(vb instanceof APDroitAPGPViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitAPGPHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitAPGPViewBean");
        }
        BSession session = (BSession) iSession;
        viewBean = (APDroitAPGPViewBean) vb;
        viewBean.setISession(session);
        viewBean.getPeriodes().clear();
        viewBean.setModeEditionDroit(APModeEditionDroit.LECTURE);

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            APDroitLAPG droit = ApgServiceLocator.getEntityService().getDroitLAPG(session, transaction,
                    viewBean.getIdDroit());
            viewBean.setDroit(droit);
            // Si le droit est un droit APG, on charge les prériodes d'APG
            if (droit instanceof APDroitAPG) {
                List<APPeriodeAPG> periodes = ApgServiceLocator.getEntityService().getPeriodesDuDroitAPG(session,
                        transaction, droit.getIdDroit());
                PRPeriode periode = null;
                for (APPeriodeAPG p : periodes) {
                    periode = new PRPeriode();
                    periode.setDateDeDebut(p.getDateDebutPeriode());
                    periode.setDateDeFin(p.getDateFinPeriode());
                    viewBean.getPeriodes().add(periode);
                }
                // Tri des périodes pour l'affichage
                Collections.sort(viewBean.getPeriodes());
            }
            if (!hasError(session, transaction)) {
                transaction.commit();
            }

        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            String message = "Exception lors de la lecture du droit  :" + exception.toString();
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
     * @see globaz.framework.controller.FWHelper#_add(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitAPGPViewBean viewBean = null;
        if (!(vb instanceof APDroitAPGPViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitAPGPHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitAPGPViewBean");
        }
        viewBean = (APDroitAPGPViewBean) vb;
        BSession session = (BSession) iSession;
        BTransaction transaction = null;

        // Création du tiers si besoin
        super._add(viewBean, action, session);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            throw new Exception(viewBean.getMessage());
        }
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            APDroitAPG droit = ApgServiceLocator.getEntityService()
                    .creerDroitAPGComplet(session, transaction, viewBean);
            if (!hasError(session, transaction)) {
                viewBean.setDroit(droit);
            }

            if (!hasError(session, transaction)) {
                transaction.commit();
            }
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            String message = "Exception lors de la création du droit  :" + exception.toString();
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
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitAPGPViewBean viewBean = null;
        if (!(vb instanceof APDroitAPGPViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitAPGPHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitAPGPViewBean");
        }
        viewBean = (APDroitAPGPViewBean) vb;
        BSession session = (BSession) iSession;
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            APDroitAPG droit = ApgServiceLocator.getEntityService().miseAjourDroit(session, transaction, viewBean);
            viewBean.setDroit(droit);

            if (!hasError(session, transaction)) {
                transaction.commit();
            }
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            String message = "Une exception est survenue lors de la création du droit : " + exception.toString();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(message);
            transaction.addErrors(message);
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
    private boolean hasError(BSession session, BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly();
    }

}
