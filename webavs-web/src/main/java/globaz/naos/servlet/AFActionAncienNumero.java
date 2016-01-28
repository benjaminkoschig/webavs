/*
 * Created on 21-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import globaz.naos.db.affiliation.AFAncienNumViewBean;
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
public class AFActionAncienNumero extends FWDefaultServletAction {

    public static final String CODE_ECRAN_DETAIL_AFFILIATION = "2";
    public static final String CODE_ECRAN_LIST_AFFILIATION = "1";

    /**
     * Constructeur d'AFActionAutreDossier.
     * 
     * @param servlet
     */
    public AFActionAncienNumero(FWServlet servlet) {
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

        getAction().setRight(FWSecureConstants.READ);

        AFAncienNumViewBean viewBean = new AFAncienNumViewBean();
        try {
            BISession bSession = CodeSystem.getSession(session);
            viewBean.setSession((BSession) bSession);

        } catch (Exception e) {
            e.printStackTrace();
        }
        viewBean = (AFAncienNumViewBean) mainController.dispatch(viewBean, getAction());
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } else {
            _destination = getRelativeURLwithoutClassPart(request, session) + "ancienNumero_de.jsp";

        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
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

        AFAncienNumViewBean viewBean = new AFAncienNumViewBean();

        Object vBean = session.getAttribute("viewBean");

        if ((vBean != null) && (vBean instanceof AFAncienNumViewBean)) {
            viewBean = (AFAncienNumViewBean) vBean;
        } else if ((vBean != null) && (vBean instanceof AFAffiliationViewBean)) {

            viewBean.setAncienNumero(((AFAffiliationViewBean) vBean).getAncienAffilieNumero());
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
                    + "ancienNumero_de.jsp?_valid=fail&_method=upd";
        } else {

            session.removeAttribute("tiersPrincipale");
            session.setAttribute("tiersPrincipale", viewBean.getIdTiers());

            if (AFActionAncienNumero.CODE_ECRAN_LIST_AFFILIATION.equalsIgnoreCase(viewBean.getCodeDirection())) {
                _destination = "/naos?userAction=naos.affiliation.affiliation.chercher&idTiers="
                        + viewBean.getIdTiers();
                request.getSession().setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX, viewBean.getIdTiers());

            } else if (AFActionAncienNumero.CODE_ECRAN_DETAIL_AFFILIATION.equalsIgnoreCase(viewBean.getCodeDirection())) {
                _destination = "/naos?userAction=naos.affiliation.affiliation.afficher&selectedId="
                        + viewBean.getAffiliationId();

                session.removeAttribute("affiliationPrincipale");
                session.setAttribute("affiliationPrincipale", viewBean.getAffiliationId());
                request.getSession().setAttribute(TIActionSummary.PYXIS_VG_IDTIERS_CTX, viewBean.getIdTiers());
            }
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}
