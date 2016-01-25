package globaz.corvus.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.vb.avances.REAvanceViewBean;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import globaz.pyxis.application.TIApplication;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author SCR
 */
public class REAvanceAction extends PRDefaultAction {

    public REAvanceAction(FWServlet servlet) {
        super(servlet);
    }

    // Surcharge de l'action afficher pour setter la session dans le vieanBean
    // lors de l'action nouveau.
    // Permet d'initialiser certaines info dans le retrieve du helper.
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = "";
        try {

            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            boolean isRetourDepuisPyxis = false;
            FWViewBeanInterface vvv = this.loadViewBean(session);
            if ((vvv != null) && (vvv instanceof REAvanceViewBean) && ((REAvanceViewBean) vvv).isRetourDepuisPyxis()) {
                isRetourDepuisPyxis = true;
            }

            if ((method != null) && (method.equalsIgnoreCase("ADD")) && !isRetourDepuisPyxis) {

                REAvanceViewBean vb = new REAvanceViewBean();
                vb.setSession((BSession) mainDispatcher.getSession());
                RENSSDTO dto = (RENSSDTO) PRSessionDataContainerHelper.getData(session,
                        PRSessionDataContainerHelper.KEY_NSS_DTO);
                String nssF = dto.getNSS();
                nssF = "756" + nssF;

                nssF = TIApplication.pyxisApp().getAvsFormater().format(nssF);
                vb.setNss(nssF);
                // BZ 5544
                PRTiersWrapper tiersWrapper = PRTiersHelper.getTiers(vb.getSession(), nssF);
                vb.setIdTiersBeneficiaire(tiersWrapper.getIdTiers());
                //
                String action = request.getParameter("userAction");
                FWAction _action = FWAction.newInstance(action);

                /*
                 * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
                 */
                FWViewBeanInterface viewBean = mainDispatcher.dispatch(vb, _action);
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

                /*
                 * redirection vers la destination
                 */
                servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

            } else {
                FWViewBeanInterface viewBean = this.loadViewBean(session);
                if ((viewBean != null) && (viewBean instanceof REAvanceViewBean)
                        && ((REAvanceViewBean) viewBean).isRetourDepuisPyxis()) {
                    ((REAvanceViewBean) viewBean).setRetourDepuisPyxis(false);
                    // On réinitialise l'adresse de pmt. Ainsi elle sera
                    // rechargée avec la nouvelle récupérée des tiers.
                    ((REAvanceViewBean) viewBean).setAdpmt(null);
                    /*
                     * redirection vers la destination
                     */
                    session.setAttribute("viewBean", viewBean);
                    destination = getRelativeURL(request, session) + "_de.jsp";
                    servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

                } else {
                    super.actionAfficher(session, request, response, mainDispatcher);
                }
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

        }
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RENSSDTO dto = new RENSSDTO();
        dto.setNSS(getNumeroAvsFormate(request.getParameter("idTierRequerant"), (BSession) mainDispatcher.getSession()));
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
        PRSessionDataContainerHelper
                .setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO, null);

        super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionModifier(session, request, response, mainDispatcher);
    }

    private String getNumeroAvsFormate(String idTierBeneficiaire, BSession session) {

        String result = "";

        if (!JadeStringUtil.isIntegerEmpty(idTierBeneficiaire)) {

            PRTiersWrapper tiers;
            try {
                tiers = PRTiersHelper.getTiersParId(session, idTierBeneficiaire);
                String nnss = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                result = NSUtil.formatWithoutPrefixe(nnss, nnss.length() > 14 ? true : false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
