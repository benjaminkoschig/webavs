package globaz.cygnus.helpers.prestationsaccordees;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.prestationsaccordees.RFPrestationsAccordeesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

public class RFPrestationsAccordeesHelper extends PRAbstractHelper {

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;
        JACalendar cal = new JACalendarGregorian();
        // DateFormat dateFormat = new SimpleDateFormat("MM.yyyy");

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFPrestationsAccordeesViewBean rfPrestationsAccordeesVB = (RFPrestationsAccordeesViewBean) viewBean;

            REPrestationsAccordees prestationAccordee = new REPrestationsAccordees();
            prestationAccordee.setSession(rfPrestationsAccordeesVB.getSession());
            prestationAccordee.setIdPrestationAccordee(rfPrestationsAccordeesVB.getIdRFMAccordee());
            prestationAccordee.retrieve(transaction);

            if (!prestationAccordee.isNew()) {

                prestationAccordee.setReferencePmt(rfPrestationsAccordeesVB.getReferencePaiement());
                prestationAccordee.setDateFinDroit(rfPrestationsAccordeesVB.getDateFinDroit());

                prestationAccordee.update(transaction);

                RFPrestationAccordee rfPrestationAccordee = new RFPrestationAccordee();
                rfPrestationAccordee.setSession(rfPrestationsAccordeesVB.getSession());
                rfPrestationAccordee.setIdRFMAccordee(prestationAccordee.getIdPrestationAccordee());

                rfPrestationAccordee.retrieve();

                if (!rfPrestationAccordee.isNew()) {

                    if (!JadeStringUtil.isBlankOrZero(rfPrestationsAccordeesVB.getDateFinDroit())) {
                        // Si la date de fin de droit < la date du jour -> date diminution = date du jour
                        // Si la date de fin de droit >= la date du jour -> date diminution = date de fin de droit
                        JADate dateFinDroitJd = new JADate(rfPrestationsAccordeesVB.getDateFinDroit());
                        JADate todayJd = JACalendar.today();
                        if ((cal.compare(dateFinDroitJd, todayJd) == JACalendar.COMPARE_EQUALS)
                                || (cal.compare(dateFinDroitJd, todayJd) == JACalendar.COMPARE_FIRSTUPPER)) {
                            rfPrestationAccordee.setDateDiminution("01." + rfPrestationsAccordeesVB.getDateFinDroit());
                        } else {
                            rfPrestationAccordee.setDateDiminution("01."
                                    + (todayJd.getMonth() < 10 ? "0" + todayJd.getMonth() : todayJd.getMonth()) + "."
                                    + todayJd.getYear());
                        }

                    } else {
                        rfPrestationAccordee.setDateDiminution("");
                    }
                    rfPrestationAccordee.update(transaction);

                } else {
                    RFUtils.setMsgErreurInattendueViewBean(rfPrestationsAccordeesVB, "_update()", "RFPrestationHelper");
                }

            } else {
                RFUtils.setMsgErreurInattendueViewBean(rfPrestationsAccordeesVB, "_update()", "RFPrestationHelper");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()
                            || FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

    }
}
