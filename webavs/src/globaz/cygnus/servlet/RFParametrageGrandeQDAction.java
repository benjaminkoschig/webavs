/*
 * créé le 24 mars 2010
 */
package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.pots.RFParametrageGrandeQDViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFParametrageGrandeQDAction extends RFDefaultAction {

    public RFParametrageGrandeQDAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        ((RFParametrageGrandeQDViewBean) viewBean).setIsUpdate(false);
        return super._getDestAjouterEchec(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        ((RFParametrageGrandeQDViewBean) viewBean).setIsUpdate(true);
        return super._getDestModifierEchec(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            return viewBean;

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }

}
