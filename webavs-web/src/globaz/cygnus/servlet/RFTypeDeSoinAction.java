package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFTypeDeSoinAction extends RFDefaultAction {

    public RFTypeDeSoinAction(FWServlet servlet) {
        super(servlet);
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
