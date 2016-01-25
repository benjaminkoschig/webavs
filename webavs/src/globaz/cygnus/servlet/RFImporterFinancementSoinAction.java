/*
 * Créé le 30 novembre 2009
 */
package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.process.RFValiderDecisionsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author LFO
 * 
 * @revision FHA idDecision
 */
public class RFImporterFinancementSoinAction extends RFDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RFImporterFinancementSoinAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".reAfficher" + "&dateSurDocument="
                + ((RFValiderDecisionsViewBean) viewBean).getDateSurDocument();
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            ((RFValiderDecisionsViewBean) viewBean).setIdDecision(request.getParameter("idDecision"));
            return viewBean;

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }
}
