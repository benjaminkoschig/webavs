/*
 * Créé le 31 mai 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.corvus.servlet;

import globaz.corvus.vb.documents.RECopiesViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author SCR
 * 
 */
public class RECopiesDefautAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RECopiesDefautAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = "";
        try {

            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String selectedId = request.getParameter("selectedId");

            String idTiersRequerant = "";
            FWViewBeanInterface vb = this.loadViewBean(session);

            String method = request.getParameter("_method");
            FWViewBeanInterface viewBean = new RECopiesViewBean();

            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);

                if (vb instanceof RECopiesViewBean) {
                    idTiersRequerant = ((RECopiesViewBean) vb).getIdTiersRequerant();

                    ((RECopiesViewBean) viewBean).setIdTiersRequerant(idTiersRequerant);

                    if (((RECopiesViewBean) vb).isRetourDepuisPyxis()) {
                        ((RECopiesViewBean) viewBean).setDateDebutCopie(((RECopiesViewBean) vb).getDateDebutCopie());
                        ((RECopiesViewBean) viewBean).setDateFinCopie(((RECopiesViewBean) vb).getDateFinCopie());
                        ((RECopiesViewBean) viewBean).setIdTiersCopieA(((RECopiesViewBean) vb).getIdTiersCopieA());
                        ((RECopiesViewBean) viewBean).setReference(((RECopiesViewBean) vb).getReference());
                    }
                }

                viewBean = beforeNouveau(session, request, response, viewBean);

                /*
                 * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
                 */
                viewBean = beforeAfficher(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, getAction());

            } else {

                if (JadeStringUtil.isEmpty(selectedId)) {
                    selectedId = request.getParameter("id");
                }

                Class b = Class.forName("globaz.globall.db.BIPersistentObject");
                Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
                mSetId.invoke(viewBean, new Object[] { selectedId });

                // Retrieve sur le viewBean
                viewBean = beforeAfficher(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, getAction());

                // On fait la maj des champs
                if ((vb instanceof RECopiesViewBean) && ((RECopiesViewBean) vb).isRetourDepuisPyxis()) {

                    // Maj des nouveaux champs avant de réafficher
                    ((RECopiesViewBean) viewBean).setDateDebutCopie(((RECopiesViewBean) vb).getDateDebutCopie());
                    ((RECopiesViewBean) viewBean).setDateFinCopie(((RECopiesViewBean) vb).getDateFinCopie());
                    ((RECopiesViewBean) viewBean).setIdTiersCopieA(((RECopiesViewBean) vb).getIdTiersCopieA());
                    ((RECopiesViewBean) viewBean).setReference(((RECopiesViewBean) vb).getReference());
                }
            }

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";

        FWViewBeanInterface viewBean = null;
        String idTiersRequerant = request.getParameter("idTierRequerant");

        // APrés l'ajout d'une nouvelle copie, on prest idTierRequerant ->
        // rechargement du viewBean depuis
        // la request.
        if (JadeStringUtil.isBlankOrZero(idTiersRequerant)) {
            viewBean = this.loadViewBean(session);

        }

        if (viewBean instanceof RECopiesViewBean) {
            ;
        } else {
            viewBean = new RECopiesViewBean();
            ((RECopiesViewBean) viewBean).setIdTiersRequerant(idTiersRequerant);
        }
        /*
         * redirection vers destination
         */

        destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);
        if (JadeStringUtil.isBlank(destination)) {
            destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        this.saveViewBean(viewBean, request);
        this.saveViewBean(viewBean, session);

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }
}