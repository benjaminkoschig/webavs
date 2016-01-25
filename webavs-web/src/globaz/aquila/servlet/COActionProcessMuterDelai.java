package globaz.aquila.servlet;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.helpers.process.COProcessMuterDelaiHelper;
import globaz.aquila.vb.process.COProcessMuterDelaiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COActionProcessMuterDelai extends CODefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COActionProcessMuterDelai.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public COActionProcessMuterDelai(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Renseigne le contentieux en cours dans le viewBean de manière à pouvoir le retrouver dans le
     * {@link COProcessMuterDelaiHelper helper}.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((COProcessMuterDelaiViewBean) viewBean).setContentieux((COContentieux) session
                .getAttribute("contentieuxViewBean"));

        return super.beforeAfficher(session, request, response, viewBean);
    }
}
