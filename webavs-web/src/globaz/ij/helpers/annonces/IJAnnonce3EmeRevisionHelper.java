/*
 * Créé le 7 nov. 05
 */
package globaz.ij.helpers.annonces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.ij.vb.annonces.IJAnnonce3EmeRevisionViewBean;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJAnnonce3EmeRevisionHelper extends IJAnnonceHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_add(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJAnnonce3EmeRevisionViewBean annonce3EmeRevisionViewBean = (IJAnnonce3EmeRevisionViewBean) viewBean;

        if (!JadeStringUtil.isIntegerEmpty(annonce3EmeRevisionViewBean.getNombreJoursPeriode1())
                && !JadeStringUtil.isDecimalEmpty(annonce3EmeRevisionViewBean.getTauxJournalierPeriode1())) {
            BigDecimal totalAIPeriode1 = new BigDecimal(annonce3EmeRevisionViewBean.getNombreJoursPeriode1())
                    .multiply(new BigDecimal(annonce3EmeRevisionViewBean.getTauxJournalierPeriode1()));
            annonce3EmeRevisionViewBean.getPeriodeAnnonce1().setTotalIJ(totalAIPeriode1.toString());
        }

        if (annonce3EmeRevisionViewBean.isDeuxiemePeriode()) {
            if (!JadeStringUtil.isIntegerEmpty(annonce3EmeRevisionViewBean.getNombreJoursPeriode2())
                    && !JadeStringUtil.isDecimalEmpty(annonce3EmeRevisionViewBean.getTauxJournalierPeriode2())) {
                BigDecimal totalAIPeriode2 = new BigDecimal(annonce3EmeRevisionViewBean.getNombreJoursPeriode2())
                        .multiply(new BigDecimal(annonce3EmeRevisionViewBean.getTauxJournalierPeriode2()));
                annonce3EmeRevisionViewBean.getPeriodeAnnonce2().setTotalIJ(totalAIPeriode2.toString());
            }
        }

        super._add(annonce3EmeRevisionViewBean, action, session);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJAnnonce3EmeRevisionViewBean annonce3EmeRevisionViewBean = (IJAnnonce3EmeRevisionViewBean) viewBean;

        if (!JadeStringUtil.isIntegerEmpty(annonce3EmeRevisionViewBean.getNombreJoursPeriode1())
                && !JadeStringUtil.isDecimalEmpty(annonce3EmeRevisionViewBean.getTauxJournalierPeriode1())) {
            BigDecimal totalAIPeriode1 = new BigDecimal(annonce3EmeRevisionViewBean.getNombreJoursPeriode1())
                    .multiply(new BigDecimal(annonce3EmeRevisionViewBean.getTauxJournalierPeriode1()));
            annonce3EmeRevisionViewBean.getPeriodeAnnonce1().setTotalIJ(totalAIPeriode1.toString());
        }

        if (annonce3EmeRevisionViewBean.isDeuxiemePeriode()) {
            if (!JadeStringUtil.isIntegerEmpty(annonce3EmeRevisionViewBean.getNombreJoursPeriode2())
                    && !JadeStringUtil.isDecimalEmpty(annonce3EmeRevisionViewBean.getTauxJournalierPeriode2())) {
                BigDecimal totalAIPeriode2 = new BigDecimal(annonce3EmeRevisionViewBean.getNombreJoursPeriode2())
                        .multiply(new BigDecimal(annonce3EmeRevisionViewBean.getTauxJournalierPeriode2()));
                annonce3EmeRevisionViewBean.getPeriodeAnnonce2().setTotalIJ(totalAIPeriode2.toString());
            }
        }

        super._update(annonce3EmeRevisionViewBean, action, session);
    }
}
