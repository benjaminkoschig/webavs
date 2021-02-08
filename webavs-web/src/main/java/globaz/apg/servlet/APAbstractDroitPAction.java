/*
 * Créé le 11 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.db.droits.APAbstractRecapitulatifDroit;
import globaz.apg.vb.droits.*;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public abstract class APAbstractDroitPAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     */
    protected static final String VERS_ECRAN_DE = "_de.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APAbstractDroitPAction.
     * 
     * @param servlet
     */
    public APAbstractDroitPAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerEchec(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return _getDestSupprimerSucces(session, request, response, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getUserActionURL(request, IAPActions.ACTION_DROIT_LAPG, FWAction.ACTION_CHERCHER);
    }

    protected void actionAfficherApresBack(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = "";
        try {

            String selectedId = ((APDroitDTO) session.getAttribute(PRSessionDataContainerHelper.KEY_DROIT_DTO))
                    .getIdDroit();

            /*
             * Creation dynamique de notre viewBean
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());

            /*
             * pour pouvoir faire un setId remarque : si il y a d'autre set a faire (si plusieurs id par ex) il faut le
             * faire dans le beforeAfficher(...)
             */
            Class b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    /**
     * Action intermédiaire exécutée lors de la sortie du champs AVS, recherche un tiers à partir du numéro et recharge
     * la page
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return retourne vers l'écran de saisie numéro 1 @throws ServletException si la personne au numéro AVS ne peut
     *         être trouvée ou la demande associée cause une erreur
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String actionFindIdTiersByNoAvs(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        // getAction().setRight(FWSecureConstants.ADD);
        mainDispatcher.dispatch(viewBean, getAction());

        String destination = getRelativeURL(request, session) + VERS_ECRAN_DE + "?";

        if (JadeStringUtil.isIntegerEmpty(((APAbstractDroitProxyViewBean) viewBean).getIdDroit())) {
            destination = destination + METHOD_ADD;
        } else {
            destination = destination + METHOD_UPD;
        }

        return destination;
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        APAbstractDroitProxyViewBean droitVB = (APAbstractDroitProxyViewBean) loadViewBean(session);

        if (droitVB.isModifiable()
                && mainDispatcher.getSession().hasRight(IAPActions.ACTION_DROIT_LAPG, FWSecureConstants.UPDATE)) {
            super.actionModifier(session, request, response, mainDispatcher);
        } else {
            goSendRedirect(_getDestModifierSucces(session, request, response, droitVB), request, response);
        }
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
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String arreterEtape1(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        // getAction().setRight(FWSecureConstants.ADD);
        mainDispatcher.dispatch(viewBean, getAction());

        return getUserActionURL(request, IAPActions.ACTION_DROIT_LAPG, FWAction.ACTION_CHERCHER);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // on retourne un viewBean correct pour les cas ou cette action a ete
        // appellee depuis la page de recapitulation
        FWViewBeanInterface retValue = viewBean;
        APAbstractDroitProxyViewBean droitVB;

        if (viewBean instanceof APRecapitulatifDroitMatViewBean) {
            droitVB = new APDroitMatPViewBean();
        } else if (viewBean instanceof APRecapitulatifDroitAPGViewBean) {
            droitVB = new APDroitAPGPViewBean();
        }  else if (viewBean instanceof APRecapitulatifDroitPanViewBean) {
            droitVB = new APDroitPanViewBean();
        }  else if (viewBean instanceof APRecapitulatifDroitPatViewBean) {
            droitVB = new APDroitPatPViewBean();
        } else {
            return viewBean;
        }

        droitVB.setISession(viewBean.getISession());
        droitVB.setIdDroit(((APAbstractRecapitulatifDroit) viewBean).getIdDroit());

        try {
            // HACK: desactive car lors du processus d'effacement, on va de
            // toutes facons recharger le droit.
            // droit.retrieve();
            retValue = droitVB;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retValue;
    }
}
