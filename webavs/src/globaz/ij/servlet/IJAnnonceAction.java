package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.ij.db.annonces.IJAnnonce;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.itext.IJRecapitulationAnnonceAdapter;
import globaz.ij.vb.annonces.IJAnnonceListViewBean;
import globaz.ij.vb.annonces.IJAnnonceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.utils.VueGlobaleTiersUtils;

/**
 * @author DVH
 */
public class IJAnnonceAction extends PRDefaultAction {

    public IJAnnonceAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        String destination = null;
        String method = request.getParameter("_method");
        String selectedId = request.getParameter("selectedId");

        try {
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                action.changeActionPart(FWAction.ACTION_NOUVEAU);
            } else {
                // suivant le type d'annonce, on redirigera vers telle ou telle
                // page
                String codeApplication = request.getParameter("codeApplication");

                if (JadeStringUtil.isEmpty(codeApplication)) {
                    // deux possibilité :

                    // 1) On vient de la pages des prestations
                    if (!JadeStringUtil.isEmpty(request.getParameter("forIdPrestation"))) {
                        // on vient de la page des prestations, il faut aller
                        // récupérer le code application
                        String forIdPrestation = request.getParameter("forIdPrestation");
                        IJPrestation prestation = new IJPrestation();
                        prestation.setSession((BSession) mainDispatcher.getSession());
                        prestation.setIdPrestation(forIdPrestation);
                        prestation.retrieve();
                        selectedId = prestation.getIdAnnonce();
                    }
                    // 2) on a cliquer sur l'icone menu option détail de
                    // annonce_rcListe
                    // (dans ce cas, selectedId est renseigné, mais pas le
                    // codeApplication, car on ne peut passer qu'un seul
                    // paramètre au menu)
                    else {
                        selectedId = request.getParameter("selectedId");
                    }
                    IJAnnonce annonce = new IJAnnonce();
                    annonce.setSession((BSession) mainDispatcher.getSession());
                    annonce.setIdAnnonce(selectedId);
                    annonce.retrieve();

                    codeApplication = annonce.getCodeApplication();

                    // if
                    // (BSessionUtil.compareDateFirstGreaterOrEqual(prestation.getSession(),
                    // prestation.getDateDebut(),
                    // prestation.getSession().getApplication()
                    // .getProperty(IJApplication.PROPERTY_DATE_DEBUT_4EME_REVISION)))
                    // {
                    // codeApplication = "8G";
                    // } else {
                    // codeApplication = "85";
                    // }
                }

                // on fait l'action en fonction du type d'annonce qu'on veut
                // afficher
                if (codeApplication.equals("85")) {
                    action = FWAction.newInstance("ij.annonces.annonce3EmeRevision.afficher");
                } else if (codeApplication.equals("8G")) {
                    action = FWAction.newInstance("ij.annonces.annonce4EmeRevision.afficher");
                }
            }

            IJAnnonceViewBean viewBean = (IJAnnonceViewBean) FWViewBeanActionFactory.newInstance(action,
                    mainDispatcher.getPrefix());

            viewBean.setIdAnnonce(selectedId);

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = (IJAnnonceViewBean) beforeAfficher(session, request, response, viewBean);
            viewBean = (IJAnnonceViewBean) mainDispatcher.dispatch(viewBean, action);

            if (!JadeStringUtil.isBlank(viewBean.getIdTiers())) {
                VueGlobaleTiersUtils.stockerIdTiersPourVueGlobale(session, viewBean.getIdTiers());
            }

            session.removeAttribute(FWServlet.VIEWBEAN);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                if (action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                    destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
                } else {
                    if (JadeStringUtil.isIntegerEmpty(viewBean.getIdAnnonce())) {
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        viewBean.setMessage(((BSession) viewBean.getISession()).getLabel("ANNONCE_INEXISTANTE"));
                    }

                    destination = getRelativeURLwithoutClassPart(request, session) + action.getClassPart() + "_de.jsp";
                }
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        // on doit calculer le montant des annonces non-erronées pour les mois et année donnés
        if ("true".equals(request.getParameter("isActionCalculer"))) {
            String moisAnnee = request.getParameter("forMoisAnneeComptable");
            String forType = request.getParameter("forType");

            if (!JadeStringUtil.isEmpty("moisAnnee")) {
                try {
                    IJRecapitulationAnnonceAdapter recapAnnonce = new IJRecapitulationAnnonceAdapter(
                            (BSession) mainDispatcher.getSession(), moisAnnee);
                    recapAnnonce.chargerParServices();

                    FWCurrency totalRestitutuion = null;
                    if (recapAnnonce.getTotalGeneralRestitutions() != null) {
                        totalRestitutuion = new FWCurrency(recapAnnonce.getTotalGeneralRestitutions().toString());
                    } else {
                        totalRestitutuion = new FWCurrency("0");
                    }

                    FWCurrency totalSansRestitutuion = null;
                    if (recapAnnonce.getTotalGeneralAC() != null) {
                        totalSansRestitutuion = new FWCurrency(recapAnnonce.getTotalGeneralAC().toString());
                    } else {
                        totalSansRestitutuion = new FWCurrency("0");
                    }

                    request.setAttribute("totalSansRestitutuion", totalSansRestitutuion.toStringFormat());
                    request.setAttribute("totalRestitutuion", totalRestitutuion.toStringFormat());

                    request.setAttribute("forMoisAnneeComptable", moisAnnee);
                    request.setAttribute("forType", forType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String destination = getRelativeURLwithoutClassPart(request, session) + "annonce_rc.jsp";
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((IJAnnonceListViewBean) viewBean).setHierarchicalOrder(true);
        return super.beforeLister(session, request, response, viewBean);
    }
}
