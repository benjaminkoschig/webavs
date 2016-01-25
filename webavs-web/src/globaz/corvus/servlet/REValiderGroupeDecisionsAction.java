package globaz.corvus.servlet;

import globaz.corvus.vb.decisions.REValiderGroupeDecisionsListViewBean;
import globaz.corvus.vb.demandes.REDemandeRenteJointDemandeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet prennant en charge les actions liées à la validation de groupe de décision de rente (écran PRE0005)
 */
public class REValiderGroupeDecisionsAction extends PRDefaultAction {

    public REValiderGroupeDecisionsAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return _getDestExecuterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        StringBuilder destination = new StringBuilder();
        destination.append("/corvus?userAction=").append(IREActions.ACTION_VALIDER_GROUPE_DECISIONS)
                .append(".chercher");
        return destination.toString();
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        REDemandeRenteJointDemandeViewBean viewBean = new REDemandeRenteJointDemandeViewBean();

        viewBean.setISession(mainDispatcher.getSession());

        if ((session.getAttribute(FWServlet.VIEWBEAN) != null)
                && (session.getAttribute(FWServlet.VIEWBEAN) instanceof REDemandeRenteJointDemandeViewBean)) {
            viewBean = (REDemandeRenteJointDemandeViewBean) session.getAttribute(FWServlet.VIEWBEAN);
        }

        saveViewBean(viewBean, session);
        saveViewBean(viewBean, request);

        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);

        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            REValiderGroupeDecisionsListViewBean viewBean = new REValiderGroupeDecisionsListViewBean();

            JSPUtils.setBeanProperties(request, viewBean);

            String likeNumeroAVS = viewBean.getLikeNumeroAVS();
            String likeNumeroAVSNNSS = viewBean.getLikeNumeroAVSNNSS();
            String likeNom = viewBean.getLikeNom();
            String likePrenom = viewBean.getLikePrenom();
            String forDateNaissance = viewBean.getForDateNaissance();
            String forCsSexe = viewBean.getForCsSexe();
            String likePreparePar = viewBean.getLikePreparerPar();

            viewBean.changeManagerSize(BManager.SIZE_NOLIMIT);

            viewBean = (REValiderGroupeDecisionsListViewBean) beforeLister(session, request, response, viewBean);
            viewBean = (REValiderGroupeDecisionsListViewBean) mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);

            HashMap<String, REDemandeRenteJointDemandeViewBean> mapDemandes = new HashMap<String, REDemandeRenteJointDemandeViewBean>();
            String listIdsDemandeRente = "";

            // Trouver toutes les demandes de rentes
            for (Iterator<?> iterator = viewBean.iterator(); iterator.hasNext();) {
                REDemandeRenteJointDemandeViewBean demande = (REDemandeRenteJointDemandeViewBean) iterator.next();

                mapDemandes.put(demande.getIdDemandeRente(), demande);

                listIdsDemandeRente += demande.getIdDemandeRente();

                if (iterator.hasNext()) {
                    listIdsDemandeRente += ",";
                }

            }

            // Charger la map de toutes les décisions pour toutes les demandes
            REValiderGroupeDecisionsListViewBean.getMapDecisionsParDemandeOneTime(listIdsDemandeRente,
                    viewBean.getSession(), likeNumeroAVS, likeNumeroAVSNNSS, likeNom, likePrenom, forDateNaissance,
                    forCsSexe, likePreparePar);

            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}
