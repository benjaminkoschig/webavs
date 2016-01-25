package globaz.lynx.servlet.societedebitrice;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitriceViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXSocieteDebitriceAction extends FWDefaultServletAction {

    public LXSocieteDebitriceAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see FWDefaultServletAction#actionAfficher(HttpSession, HttpServletRequest, HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        if (!JadeStringUtil.isBlank(request.getParameter("_method")) && isRetourDepuisPyxis(viewBean)) {
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                    .forward(request, response);
        } else {
            super.actionAfficher(session, request, response, mainDispatcher);
        }
    }

    /**
     * L'action est-elle appelée en retour du module tiers ?
     * 
     * @param viewBean
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return (viewBean != null && viewBean instanceof LXSocieteDebitriceViewBean && ((LXSocieteDebitriceViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

}
