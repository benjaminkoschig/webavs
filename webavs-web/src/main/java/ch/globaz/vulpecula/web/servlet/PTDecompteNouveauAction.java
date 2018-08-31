/**
 *
 */
package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.decomptenouveau.PTDecomptenouveauViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Gère les actions pour un décompte
 * 
 * @since Web@BMS 0.01.01
 */
public class PTDecompteNouveauAction extends FWDefaultServletAction {

    @Override
    protected FWViewBeanInterface beforeAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        // Si on provient de la vue générale des employeurs, il faut renseigné
        // le numéro d'affilié.
        String idEmployeur = request.getParameter("idEmployeur");
        String designationEmployeur = request.getParameter("designationEmployeur");
        PTDecomptenouveauViewBean vb = (PTDecomptenouveauViewBean) viewBean;
        vb.setIdEmployeur(idEmployeur);
        vb.setDesignationEmployeur(designationEmployeur);
        session.setAttribute("viewBean", vb);

        return super.beforeAfficher(session, request, response, viewBean);
    }

    public PTDecompteNouveauAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterSucces(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {

        if (viewBean instanceof PTDecomptenouveauViewBean) {
            PTDecomptenouveauViewBean vb = (PTDecomptenouveauViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.decomptedetail.decomptedetail.afficher&selectedId="
                    + vb.getDecompte().getId();
        }

        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterEchec(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        String destination = super._getDestAjouterEchec(session, request, response, viewBean);
        destination += "&_method=add";
        return destination;
    }

    @Override
    protected void actionAjouter(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        try {
            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            /*
             * recuperation du bean depuis la session
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, newAction);
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix de la destination
             */
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
            e.printStackTrace();
        }

        /*
         * redirection vers la destination
         */
        goSendRedirectWithoutParameters(destination, request, response);
    }
}
