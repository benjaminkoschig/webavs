package globaz.osiris.servlet.action.contentieux;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASectionViewBean;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAEvenementContentieuxViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour annuler les étapes de "l'ancien" contentieux.
 * 
 * @author DDA
 */
public class CAAnnulerEtapeContentieuxAction extends CADefaultServletAction {

    /**
     * Constructor for CAAnnulerEtapeContentieux.
     * 
     * @param servlet
     */
    public CAAnnulerEtapeContentieuxAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Affiche le detail d'un apercu d'échéance de police de recouvrement
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        try {
            CAEvenementContentieuxViewBean viewBean = new CAEvenementContentieuxViewBean();

            // si les paramètres sectionId et parametreEtapeId ne sont pas
            // définis (on vient de la page de détail)
            // on utilise le paramètre id (idEvenementContentieux)
            if (JadeStringUtil.isNull(request.getParameter("idSection"))
                    || JadeStringUtil.isNull(request.getParameter("idParametreEtape"))) {
                viewBean.setIdEvenementContentieux(request.getParameter("id"));
            } else {
                viewBean.setIdSection(request.getParameter("idSection"));
                viewBean.setIdParametreEtape(request.getParameter("idParametreEtape"));
                viewBean.setAlternateKey(CAEvenementContentieux.AK_IDSECPARAM);
            }

            viewBean.setSession((BSession) mainDispatcher.getSession());
            viewBean = (CAEvenementContentieuxViewBean) mainDispatcher.dispatch(viewBean, super.getAction());

            setSessionAttribute(session, VB_ELEMENT, viewBean);

        } catch (Exception e) {
            // Do nothing
        }

        String _myDestination = getRelativeURLwithoutClassPart(request, session) + "annulerEtapeContentieux_de.jsp";
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        CAEvenementContentieuxViewBean viewBean = (CAEvenementContentieuxViewBean) session.getAttribute(VB_ELEMENT);
        if (request.getParameter("motif") != null) {
            viewBean.setMotif(String.valueOf(request.getParameter("motif")));
        }

        viewBean = (CAEvenementContentieuxViewBean) mainDispatcher.dispatch(viewBean,
                FWAction.newInstance(request.getParameter("userAction")));

        String _myDestination = "";
        try {
            if (viewBean.hasErrors()) {
                setSessionAttribute(session, VB_ELEMENT, viewBean);

                _myDestination = getRelativeURLwithoutClassPart(request, session) + "annulerEtapeContentieux_de.jsp";
            } else {
                CASectionViewBean vbSection = new CASectionViewBean();
                vbSection.setSession(viewBean.getSession());
                vbSection.setIdSection(viewBean.getSection().getIdSection());
                vbSection.retrieve();

                setSessionAttribute(session, VB_ELEMENT, vbSection);

                _myDestination = getRelativeURLwithoutClassPart(request, session) + "operationContentieux_rc.jsp";
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);

            setSessionAttribute(session, VB_ELEMENT, viewBean);

            _myDestination = getRelativeURLwithoutClassPart(request, session) + "annulerEtapeContentieux_de.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }
}
