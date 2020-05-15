package globaz.apg.helpers.droits;

import globaz.apg.ApgServiceLocator;
import globaz.apg.db.droits.APDroitPanSituation;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.exceptions.APWrongViewBeanTypeException;
import globaz.apg.properties.APProperties;
import globaz.apg.vb.droits.APDroitPanSituationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.common.Jade;
import globaz.pyxis.db.tiers.TIMoyenCommunication;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;

public class APDroitPanSituationHelper extends APAbstractDroitPHelper {

    @Override
    protected void _init(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitPanSituationViewBean viewBean = null;
        if (!(vb instanceof APDroitPanSituationViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitPanHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitPanViewBean");
        }
        BSession session = (BSession) iSession;
        viewBean = (APDroitPanSituationViewBean) vb;
        viewBean.setISession(session);
        viewBean.setModeEditionDroit(APModeEditionDroit.CREATION);

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            // On récupère le mail du tiers.
            String moyen  = getMailTiers(viewBean.getDroitPanDTO().getIdTiers(),transaction);
            viewBean.setEmail(moyen);

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

    @Override
    protected void _add(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitPanSituationViewBean viewBean = null;
        if (!(vb instanceof APDroitPanSituationViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitAPGPHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitAPGPViewBean");
        }
        viewBean = (APDroitPanSituationViewBean) vb;
        BSession session = (BSession) iSession;
        BTransaction transaction = null;

        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            APDroitPanSituation droit = ApgServiceLocator.getEntityService()
                    .creerDroitPanSituation(session, transaction, viewBean);
            if (!hasError(session, transaction)) {
                viewBean.setDroitPanSituation(droit);
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
        APDroitPanSituationViewBean viewBean = null;
        if (!(vb instanceof APDroitPanSituationViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitPanHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitPanViewBean");
        }
        BSession session = (BSession) iSession;
        viewBean = (APDroitPanSituationViewBean) vb;
        viewBean.setISession(session);

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            APDroitPanSituation droit = ApgServiceLocator.getEntityService().getDroitPanSituation(session, transaction,
                    viewBean.getIdDroit());
            viewBean.setDroitPanSituation(droit);
            if (StringUtils.isNotEmpty(droit.getIdApgPandemie())){
                viewBean.setModeEditionDroit(APModeEditionDroit.LECTURE);
            } else {
                viewBean.setModeEditionDroit(APModeEditionDroit.CREATION);
            }

            // On récupère le mail du tiers.
            String moyen  = getMailTiers(viewBean.getDroitPanDTO().getIdTiers(),transaction);
            viewBean.setEmail(moyen);

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
     * Récupère le mail du domaine pandémie pour le tiers concerné.
     *
     * @param idTiers : id tiers
     * @param transaction : la transaction
     * @return l'adresse mail si elle exite.
     * @throws Exception
     */
    private String getMailTiers(String idTiers, BTransaction transaction) throws Exception {
        String output = "";
        String schema = Jade.getInstance().getDefaultJdbcSchema();
        BStatement statement = new BStatement(transaction);
        statement.createStatement();

        StringBuilder requete = new StringBuilder("select HLCONT as EMAIL from ");
        requete.append(schema).append(".TIMCOMP as moy INNER JOIN ").append(schema).append(".TIACONP as cont on moy.HLICON=cont.HLICON ");
        requete.append("where cont.HTITIE =").append(idTiers).append(" and moy.HLAPPL=").append(APProperties.DOMAINE_ADRESSE_APG_PANDEMIE.getValue());
        requete.append(" and moy.HLTTCO=\'").append(TIMoyenCommunication.EMAIL).append("\'");

        ResultSet resultSet = statement.executeQuery(requete.toString());
        if (resultSet.next()) {
            output = resultSet.getObject(1).toString();
        }

        statement.closeStatement();

        return output;
    }

    /**
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface vb, FWAction action, BISession iSession) throws Exception {
        APDroitPanSituationViewBean viewBean = null;
        if (!(vb instanceof APDroitPanSituationViewBean)) {
            throw new APWrongViewBeanTypeException("Wrong viewBean type for action APDroitAPGPHelper._delete ["
                    + vb.getClass().getName() + "]. it must be from type APDroitAPGPViewBean");
        }
        viewBean = (APDroitPanSituationViewBean) vb;
        BSession session = (BSession) iSession;
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) session.newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            APDroitPanSituation droit = ApgServiceLocator.getEntityService().miseAjourDroitPanSituation(session, transaction, viewBean);
            viewBean.setDroitPanSituation(droit);

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

