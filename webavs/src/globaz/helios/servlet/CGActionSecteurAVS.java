package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.avs.CGSecteurAVS;
import globaz.helios.db.avs.CGSecteurAVSListViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des secteurs AVS.
 * 
 * @author DDA
 * 
 */
public class CGActionSecteurAVS extends CGDefaultServletAction {

    public CGActionSecteurAVS(FWServlet servlet) {
        super(servlet);
    }

    protected void actionAjouterSecteur(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, boolean isExtourner) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = "";
        try {

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);
            JSPUtils.setBeanProperties(request, viewBean);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            setSessionAttribute(session, VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = _getDestEchec(session, request, response, viewBean);
            } else {
                destination = getActionFullURL() + ".chercher";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_CHERCHER);

            CGSecteurAVSListViewBean vBean = new CGSecteurAVSListViewBean();
            JSPUtils.setBeanProperties(request, vBean);

            vBean = (CGSecteurAVSListViewBean) mainDispatcher.dispatch(vBean, action);

            setSessionAttribute(session, VIEWBEAN, vBean);
            destination = getRelativeURL(request, session) + "_rc.jsp";

        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("ajouterSecteur".equals(getAction().getActionPart())) {
            actionAjouterSecteur(session, request, response, mainDispatcher, false);
        }
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWViewBeanInterface) La table des secteurs AVS est composée de deux clé primaires
     *      (idMandat + idSecteurAvs) --> mise à jours de l'id Mandat
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CGSecteurAVS secteurAvs = (CGSecteurAVS) viewBean;
        secteurAvs.setIdMandat(request.getParameter("idMandat"));
        secteurAvs.setIdSecteurAVS(request.getParameter("selectedId"));
        return super.beforeAfficher(session, request, response, secteurAvs);
    }

}
