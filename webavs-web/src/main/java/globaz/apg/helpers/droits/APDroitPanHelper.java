package globaz.apg.helpers.droits;

import globaz.apg.ApgServiceLocator;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitPandemie;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.exceptions.APWrongViewBeanTypeException;
import globaz.apg.vb.droits.APDroitAPGPViewBean;
import globaz.apg.vb.droits.APDroitPanViewBean;
import globaz.apg.vb.prestation.APValidationPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.prestation.beans.PRPeriode;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;

public class APDroitPanHelper extends APAbstractDroitPHelper {


    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        viewBean.setISession(session);
        ((APDroitPanViewBean) viewBean).setModeEditionDroit(APModeEditionDroit.CREATION);
    }

    @Override
    protected void _add(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitPanViewBean viewBean = null;
        if (!(vb instanceof APDroitPanViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitAPGPHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitAPGPViewBean");
        }
        viewBean = (APDroitPanViewBean) vb;
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
            APDroitPandemie droit = ApgServiceLocator.getEntityService()
                    .creerDroitPanComplet(session, transaction, viewBean);
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

    @Override
    protected void _retrieve(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitPanViewBean viewBean = null;
        if (!(vb instanceof APDroitPanViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitPanHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitPanViewBean");
        }
        BSession session = (BSession) iSession;
        viewBean = (APDroitPanViewBean) vb;
        viewBean.setISession(session);
        viewBean.getPeriodes().clear();
        viewBean.setModeEditionDroit(APModeEditionDroit.LECTURE);

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            APDroitPandemie droit = ApgServiceLocator.getEntityService().getDroitPandemie(session, transaction,
                    viewBean.getIdDroit());
            viewBean.setDroit(droit);
            // Si le droit est un droit APG, on charge les prériodes d'APG
            List<APPeriodeAPG> periodes = ApgServiceLocator.getEntityService().getPeriodesDuDroitAPG(session,
                    transaction, droit.getIdDroit());
            PRPeriode periode = null;
            for (APPeriodeAPG p : periodes) {
                periode = new PRPeriode();
                periode.setDateDeDebut(p.getDateDebutPeriode());
                periode.setDateDeFin(p.getDateFinPeriode());
                if (!StringUtils.equals(p.getNbrJours(), "0")) {
                    periode.setNbJour(p.getNbrJours());
                }
                viewBean.getPeriodes().add(periode);
            }
            // Tri des périodes pour l'affichage
            Collections.sort(viewBean.getPeriodes());
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
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitPanViewBean viewBean = null;
        if (!(vb instanceof APDroitPanViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitAPGPHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitAPGPViewBean");
        }
        viewBean = (APDroitPanViewBean) vb;
        BSession session = (BSession) iSession;
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            APDroitPandemie droit = ApgServiceLocator.getEntityService().miseAjourDroitPan(session, transaction, viewBean);
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
            APDroitPandemie droit = ApgServiceLocator.getEntityService().getDroitPandemie(session, transaction,
                    viewBean.getIdDroit());
            // On met à jour l'état du droit
            droit.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_VALIDE);
            droit.update();

            APDroitAPGPViewBean actionAfficherViewBean = new APDroitAPGPViewBean();
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
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(message);
            throw new Exception(message);
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    public FWViewBeanInterface passerDroitErreur(FWViewBeanInterface vb, FWAction action, BSession session)
            throws Exception {
        updateDroitEtat(((APValidationPrestationViewBean) vb).getIdDroit(), session, IAPDroitLAPG.CS_ETAT_DROIT_ERREUR);
        return vb;
    }

    private void updateDroitEtat(String idDroit, BSession session, String etat) throws Exception {
        BTransaction transaction = (BTransaction) session.newTransaction();
        APDroitPandemie droitPandemie = ApgServiceLocator.getEntityService().getDroitPandemie(session, transaction, idDroit);
        droitPandemie.setEtat(etat);
        droitPandemie.update();
    }

    /**
     * @param session
     * @param transaction
     * @return
     */
    private boolean hasError(BSession session, BTransaction transaction) {
        return session.hasErrors() || (transaction == null) || transaction.hasErrors() || transaction.isRollbackOnly();
    }

    @Override
    protected void _delete(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitPanViewBean viewBean = null;
        if (!(vb instanceof APDroitPanViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitPanHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitPanViewBean");
        }
        viewBean = (APDroitPanViewBean) vb;
        BSession session = (BSession) iSession;
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            ApgServiceLocator.getEntityService().supprimerDroitCompletPan(session, transaction, viewBean.getIdDroit());

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

}
