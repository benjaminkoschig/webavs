package globaz.campus.servlet;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.vb.annonces.GEImputationsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GEActionImputations extends FWDefaultServletAction {

    public GEActionImputations(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        GEImputationsViewBean imputation = (GEImputationsViewBean) viewBean;
        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), viewBean);
        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getActionFullURL() + ".afficher&selectedId=" + imputation.getIdAnnonce() + "&_method=upd";
        }
        return _destination;
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        GEImputationsViewBean imputation = (GEImputationsViewBean) viewBean;
        return getActionFullURL() + ".afficher&selectedId=" + imputation.getIdAnnonce();
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                _action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");

            /*
             * Creation dynamique de notre viewBean
             */
            GEImputationsViewBean viewBean = new GEImputationsViewBean();

            /*
             * pour pouvoir faire un setId remarque : si il y a d'autre set a faire (si plusieurs id par ex) il faut le
             * faire dans le beforeAfficher(...)
             */
            Class b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            /*
             * initialisation du viewBean
             */
            if (_action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = (GEImputationsViewBean) beforeNouveau(session, request, response, viewBean);
                GEAnnonces annonceParent = new GEAnnonces();
                annonceParent
                        .setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
                annonceParent.setIdAnnonce(request.getParameter("idAnnonceParent"));
                annonceParent.retrieve();
                if (annonceParent != null && !annonceParent.isNew()) {
                    viewBean.setIdEtudiant(annonceParent.getIdEtudiant());
                    viewBean.setNom(annonceParent.getNom());
                    viewBean.setPrenom(annonceParent.getPrenom());
                    viewBean.setNumAvs(annonceParent.getNumAvs());
                    viewBean.setNumImmatriculationTransmis(annonceParent.getNumImmatriculationTransmis());
                    viewBean.setCsSexe(annonceParent.getCsSexe());
                    viewBean.setCsEtatCivil(annonceParent.getCsEtatCivil());
                    viewBean.setCsCodeDoctorant(annonceParent.getCsCodeDoctorant());
                    viewBean.setDateNaissance(annonceParent.getDateNaissance());
                }
            }

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = (GEImputationsViewBean) beforeAfficher(session, request, response, viewBean);
            viewBean = (GEImputationsViewBean) mainDispatcher.dispatch(viewBean, _action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

}
