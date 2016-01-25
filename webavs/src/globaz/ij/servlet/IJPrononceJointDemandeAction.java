/*
 * Créé le 9 sept. 05
 */
package globaz.ij.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.ij.vb.prononces.IJPrononceJointDemandeListViewBean;
import globaz.ij.vb.prononces.IJPrononceJointDemandeViewBean;
import globaz.ij.vb.prononces.IJPrononceParametresRCDTO;
import globaz.ij.vb.prononces.IJPrononceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
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
public class IJPrononceJointDemandeAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrononceJointDemandeAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJPrononceJointDemandeAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String actionAnnuler(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        IJPrononceJointDemandeViewBean prononce = new IJPrononceJointDemandeViewBean();

        try {
            JSPUtils.setBeanProperties(request, prononce);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(prononce.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            prononce.setMsgType(FWViewBeanInterface.ERROR);
            prononce.setMessage(e.getMessage());
        }

        mainDispatcher.dispatch(prononce, getAction());
        this.saveViewBean(prononce, request);

        if (FWViewBeanInterface.ERROR.equals(prononce.getMsgType())) {
            this.saveViewBean(prononce, session);
            return FWDefaultServletAction.ERROR_PAGE;
        }
        return getRelativeURL(request, session) + "_rc.jsp";
    }

    /**
     * Redéfinition d'actionChercher permettant de créer un viewBean qui sera utilisé pour l'affichage de données dans
     * la page rc.
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
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        IJPrononceJointDemandeViewBean viewBean = new IJPrononceJointDemandeViewBean();

        try {
            JSPUtils.setBeanProperties(request, viewBean);
            try {
                RENSSDTO nssDtoRente = null;
                IJNSSDTO nssDto = null;
                if (session.getAttribute(PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RENSSDTO) {
                    nssDtoRente = (RENSSDTO) session.getAttribute(PRSessionDataContainerHelper.KEY_NSS_DTO);
                    nssDto = new IJNSSDTO();
                    nssDto.setNSS(NSUtil.formatAVSUnknown(nssDtoRente.getNSS()));
                    PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, nssDto);
                } else if (session.getAttribute(PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof IJNSSDTO) {
                    nssDto = (IJNSSDTO) session.getAttribute(PRSessionDataContainerHelper.KEY_NSS_DTO);
                } else {
                    // On n'avait rien en session
                    nssDto = new IJNSSDTO();
                }

                if (!JadeStringUtil.isBlankOrZero(request.getParameter("noAVS"))
                        && !request.getParameter("noAVS").equals(nssDto.getNSS())) {
                    nssDto.setNSS(request.getParameter("noAVS"));
                    PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, nssDto);
                }
                viewBean.setNoAVS(nssDto.getNSS());
            } catch (Exception e) {
                // Erreur vue globale, on ne va rien faire d'autre pour ne pas empecher le chargement de la page !
            }

            mainDispatcher.dispatch(viewBean, getAction());
            this.saveViewBean(viewBean, request);

            if ((viewBean.getSession() == null) || viewBean.hasErrors()) {
                // si on rentre dans ij et qu'on a pas les droits, la session vaut
                // null
                forward(FWDefaultServletAction.ERROR_PAGE, request, response);
            } else {
                forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    public String actionCreerCopie(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        IJPrononceJointDemandeViewBean prononce = new IJPrononceJointDemandeViewBean();

        try {
            JSPUtils.setBeanProperties(request, prononce);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(prononce.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            prononce.setMsgType(FWViewBeanInterface.ERROR);
            prononce.setMessage(e.getMessage());
        }

        mainDispatcher.dispatch(prononce, getAction());

        boolean goesToSuccessDest = !prononce.getMsgType().equals(FWViewBeanInterface.ERROR);

        if (!goesToSuccessDest) {
            if (null == viewBean) {
                viewBean = new IJPrononceViewBean();
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(prononce.getMessage());
        }

        this.saveViewBean(prononce, request);

        if (goesToSuccessDest) {
            return getRelativeURL(request, session) + "_rc.jsp";
        } else {

            return FWDefaultServletAction.ERROR_PAGE;
        }

    }

    /**
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
     */
    public String actionCreerCorrection(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        IJPrononceJointDemandeViewBean prononce = new IJPrononceJointDemandeViewBean();

        try {
            JSPUtils.setBeanProperties(request, prononce);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(prononce.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            prononce.setMsgType(FWViewBeanInterface.ERROR);
            prononce.setMessage(e.getMessage());
        }

        mainDispatcher.dispatch(prononce, getAction());

        boolean goesToSuccessDest = !prononce.getMsgType().equals(FWViewBeanInterface.ERROR);

        if (!goesToSuccessDest) {
            if (null == viewBean) {
                viewBean = new IJPrononceViewBean();
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(prononce.getMessage());
        }

        this.saveViewBean(prononce, request);

        if (goesToSuccessDest) {
            return getRelativeURL(request, session) + "_rc.jsp";
        } else {
            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        IJPrononceJointDemandeListViewBean listViewBean = (IJPrononceJointDemandeListViewBean) viewBean;

        IJPrononceParametresRCDTO dto = new IJPrononceParametresRCDTO();
        dto.setResponsable(listViewBean.getForIdGestionnaire());
        dto.setEtatDemande(listViewBean.getForCsEtatDemande());
        dto.setEtatPrononce(listViewBean.getForCsEtatPrononce());
        dto.setOrderBy(listViewBean.getOrderBy());

        IJNSSDTO dtoNSS = new IJNSSDTO();
        dtoNSS.setNSS(listViewBean.getLikeNumeroAVS());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO, dto);
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dtoNSS);

        return super.beforeLister(session, request, response, viewBean);
    }

    /**
     * Action custom permettant de simuler le paiement d'un prononce.
     * 
     * Est utilisé pour récupéré des anciens cas de l'AS400 qui doivent être restitué.
     * 
     * Pré requis : Saisir l'ancien cas dans le nouveau système.
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
    public String simulerPaiementDroit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        String destination = null;

        try {
            viewBean = new IJPrononceJointDemandeViewBean();
            viewBean.setISession(mainDispatcher.getSession());
            ((IJPrononceJointDemandeViewBean) viewBean).setIdPrononce(request.getParameter("selectedId"));

            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        return destination;
    }
}
