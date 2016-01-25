/*
 * Créé le 8 nov. 05
 */
package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceManager;
import globaz.ij.utils.IJUtils;
import globaz.ij.vb.process.IJRecapitulationAnnonceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJGlobazAdminAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJAnnonceAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJGlobazAdminAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return getRelativeURLwithoutClassPart(request, session) +
        // "annonce_rc.jsp";
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return getRelativeURLwithoutClassPart(request, session) +
        // "annonce_rc.jsp";
        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return getRelativeURLwithoutClassPart(request, session) +
        // "annonce_rc.jsp";
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void migrerNoDecision(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        viewBean = new IJRecapitulationAnnonceViewBean();

        String destination = "";
        BTransaction transaction = null;

        StringBuffer sb = new StringBuffer();
        try {
            BSession bSession = (BSession) mainDispatcher.getSession();
            IJPrononceManager mgr = new IJPrononceManager();
            mgr.setSession(bSession);
            transaction = (BTransaction) bSession.newTransaction();
            transaction.openTransaction();
            BStatement statement = null;
            statement = mgr.cursorOpen(transaction);
            IJPrononce prononce = null;

            String nnssRequerant = "";
            while ((prononce = (IJPrononce) mgr.cursorReadNext(statement)) != null) {

                try {
                    String oldNoDecisionAI = prononce.getNoDecisionAI();

                    if (!JadeStringUtil.isBlankOrZero(oldNoDecisionAI)) {
                        PRTiersWrapper tw = prononce.loadDemande(transaction).loadTiers();
                        nnssRequerant = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                        // On supprime le no de controle
                        String newNoDecisionAI = oldNoDecisionAI;
                        newNoDecisionAI = newNoDecisionAI.substring(0, 10);

                        String chiffreCle = IJUtils.getChiffreCleDecision(bSession, nnssRequerant,
                                prononce.getOfficeAI(), newNoDecisionAI);
                        newNoDecisionAI = newNoDecisionAI + chiffreCle;

                        sb.append("-").append(prononce.getIdPrononce());
                        if (!JadeStringUtil.isBlankOrZero(newNoDecisionAI) && !oldNoDecisionAI.equals(newNoDecisionAI)) {

                            prononce.setNoDecisionAI(newNoDecisionAI);
                            prononce.update(transaction);
                            sb.append("*");
                            if (transaction.hasErrors()) {
                                sb.append("ERR:" + nnssRequerant + " - " + prononce.getIdPrononce());
                                throw new Exception(transaction.getErrors().toString());
                            }
                            transaction.commit();
                        }
                    }
                } catch (Exception e) {
                    transaction.rollback();
                    transaction.clearErrorBuffer();
                    sb.append("ERR:" + nnssRequerant + " - " + prononce.getIdPrononce());
                    e.printStackTrace();
                }
            }
            viewBean.setMessage(sb.toString());
            saveViewBean(viewBean, request);
            saveViewBean(viewBean, session);
            destination = ERROR_PAGE;

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
