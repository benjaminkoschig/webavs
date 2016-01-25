/*
 * Created on 21-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import globaz.naos.db.affiliation.AFAutreDossierViewBean;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.summary.TIActionSummary;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour la selection d'une autre entité Affiliation.
 * 
 * @author sau
 */
public class AFActionAutreDossier extends FWDefaultServletAction {

    public static final String CODE_ECRAN_DETAIL_AFFILIATION = "2";
    public static final String CODE_ECRAN_LIST_AFFILIATION = "1";

    /**
     * Constructeur d'AFActionAutreDossier.
     * 
     * @param servlet
     */
    public AFActionAutreDossier(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Effectue des traitements avant recupération de l'entité dans la DB, pour l'afficher.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    public void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainController) throws ServletException, IOException {

        String _destination = "";

        AFAutreDossierViewBean viewBean = new AFAutreDossierViewBean();
        try {
            BISession bSession = CodeSystem.getSession(session);
            viewBean.setSession((BSession) bSession);

        } catch (Exception e) {
            e.printStackTrace();
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        _destination = getRelativeURLwithoutClassPart(request, session) + "autreDossier_de.jsp";

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    // protected void actionAfficher(
    // HttpSession session,
    // HttpServletRequest request,
    // HttpServletResponse response,
    // FWDispatcher mainDispatcher)
    // throws ServletException, IOException {
    //
    // String _destination;
    //
    // AFAutreDossierViewBean viewBean = new AFAutreDossierViewBean();
    //
    // /*try {
    // viewBean = (AFAutreDossierViewBean) mainDispatcher.manager(viewBean,
    // request.getParameter("userAction"));
    // } catch (Exception e) {
    // viewBean.setMessage(e.getMessage());
    // viewBean.setMsgType(FWViewBeanInterface.ERROR);
    // }*/
    //
    // if (!
    // JAUtil.isStringEmpty((String)session.getAttribute("affiliationPrincipale")))
    // {
    // session.removeAttribute("affiliationPrincipale");
    // }
    // if (!
    // JAUtil.isStringEmpty((String)session.getAttribute("tiersPrincipale"))) {
    // session.removeAttribute("tiersPrincipale");
    // }
    //
    // session.setAttribute("viewBean", viewBean);
    // if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
    // _destination = ERROR_PAGE;
    // } else {
    // _destination = getRelativeURLwithoutClassPart(request, session) +
    // "autreDossier_de.jsp";
    // }
    // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
    // response);
    // }

    /*
     * Conservation Context vue globale (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if ("diriger".equals(getAction().getActionPart())) {
            String _destination = "/naos?userAction=naos.affiliation.autreDossier.afficher&_method=upd";
            try {
                String numeroSession = (String) session
                        .getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX);
                if (!JadeStringUtil.isBlank(numeroSession)) {
                    AFAffiliationManager mgr = new AFAffiliationManager();
                    BSession bSession = (BSession) CodeSystem.getSession(session);
                    mgr.setSession(bSession);
                    mgr.setForAffilieNumero(numeroSession);
                    mgr.find();
                    AFAffiliation aff = (AFAffiliation) mgr.getFirstEntity();
                    request.getSession().setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX, aff.getIdTiers());
                    _destination = "/naos?userAction=naos.affiliation.affiliation.afficher&selectedId="
                            + aff.getAffiliationId() + "&idTiers=" + aff.getIdTiers();

                }
            } catch (Exception e) {
            }

            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        }
    }

    /**
     * Modification des paramètres de Saisie pour le séléction d'une autre Affiliation.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    public void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainController) throws ServletException, IOException {

        String _destination = "";

        AFAutreDossierViewBean viewBean = new AFAutreDossierViewBean();

        Object vBean = session.getAttribute("viewBean");
        String forAction = request.getParameter("forAction");

        if ((vBean != null) && (vBean instanceof AFAutreDossierViewBean)) {
            viewBean = (AFAutreDossierViewBean) vBean;
        } else if ((vBean != null) && (vBean instanceof AFAffiliationViewBean)) {
            viewBean.setNumAffilie(((AFAffiliationViewBean) vBean).getAffilieNumero());
        }

        try {
            viewBean.setMsgType("");
            BISession bSession = CodeSystem.getSession(session);
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setSession((BSession) bSession);

            viewBean._controle((BSession) bSession);
        } catch (Exception e) {
            e.printStackTrace();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        // --- Check view bean
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            _destination = getRelativeURLwithoutClassPart(request, session)
                    + "autreDossier_de.jsp?_valid=fail&_method=upd";
        } else {

            session.removeAttribute("tiersPrincipale");
            session.setAttribute("tiersPrincipale", viewBean.getIdTiers());

            if (AFActionAutreDossier.CODE_ECRAN_LIST_AFFILIATION.equalsIgnoreCase(viewBean.getCodeDirection())) {
                _destination = "/naos?userAction=naos.affiliation.affiliation.chercher&idTiers="
                        + viewBean.getIdTiers();

            } else if (AFActionAutreDossier.CODE_ECRAN_DETAIL_AFFILIATION.equalsIgnoreCase(viewBean.getCodeDirection())) {
                if ("updateMasse".equals(forAction)) {
                    _destination = "/naos?userAction=naos.masse.masseModifier.afficher&affiliationId="
                            + viewBean.getAffiliationId() + "&_valid=fail&_method=upd";
                } else {
                    _destination = "/naos?userAction=naos.affiliation.affiliation.afficher&selectedId="
                            + viewBean.getAffiliationId();
                }

                session.removeAttribute("affiliationPrincipale");
                session.setAttribute("affiliationPrincipale", viewBean.getAffiliationId());
            }
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}
