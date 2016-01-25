package globaz.lynx.servlet.fournisseur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.fournisseur.LXFournisseurViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXFournisseurAction extends FWDefaultServletAction {

    public LXFournisseurAction(FWServlet servlet) {
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

        if (request.getAttribute(FWServlet.VIEWBEAN) instanceof LXFournisseurViewBean) {
            request.getSession().setAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX,
                    ((LXFournisseurViewBean) request.getAttribute(FWServlet.VIEWBEAN)).getIdTiers());
        }

    }

    /**
     * L'action est-elle appelée en retour du module tiers ?
     * 
     * @param viewBean
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof LXFournisseurViewBean) && ((LXFournisseurViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

}
