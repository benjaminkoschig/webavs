package globaz.apg.servlet;

import globaz.apg.vb.droits.APDroitDTO;
import globaz.apg.vb.droits.APDroitPanDTO;
import globaz.apg.vb.droits.APDroitPanSituationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.prestation.tools.PRSessionDataContainerHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;

public class APDroitPanSituationAction extends APAbstractDroitDTOAction {

    /**
     * Crée une nouvelle instance de la classe APAbstractDroitPAction.
     *
     * @param servlet
     */
    public APDroitPanSituationAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected Class getViewBeanClass() {
        return APDroitPanSituationViewBean.class;
    }

    /**
     * DOCUMENT ME!
     *
     * @param viewBean
     *            DOCUMENT ME!
     * @param droitDTO
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface initViewBean(FWViewBeanInterface viewBean, APDroitDTO droitDTO, HttpSession session) {
        if (droitDTO instanceof APDroitPanDTO) {
            ((APDroitPanSituationViewBean) viewBean).setDroitPanDTO((APDroitPanDTO) droitDTO);
        }
        return viewBean;
    }


    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {
        APDroitPanDTO dto = ((APDroitPanSituationViewBean) viewBean).getDroitPanDTO();
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        return this.getUserActionURL(request, IAPActions.ACTION_SITUATION_PROFESSIONNELLE, FWAction.ACTION_CHERCHER
                + "&" + APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + dto.getIdDroit());
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWViewBeanInterface viewBean) {
        APDroitPanDTO dto = ((APDroitPanSituationViewBean) viewBean).getDroitPanDTO();
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        return this.getUserActionURL(request, IAPActions.ACTION_SITUATION_PROFESSIONNELLE, FWAction.ACTION_CHERCHER
                + "&" + APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + dto.getIdDroit());
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                 FWDispatcher mainDispatcher) throws ServletException, IOException {

        APDroitPanSituationViewBean viewBean = new APDroitPanSituationViewBean();
        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);

        String destination = null;

        try {

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = (APDroitPanSituationViewBean) beforeAjouter(session, request, response, viewBean);
            viewBean = (APDroitPanSituationViewBean) mainDispatcher.dispatch(viewBean, newAction);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                session.setAttribute("viewBean", viewBean);
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            this.saveViewBean(viewBean, session);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);

    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {
        APDroitPanSituationViewBean viewBean = new APDroitPanSituationViewBean();
        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);

        String destination = null;
        try {

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = (APDroitPanSituationViewBean) beforeAjouter(session, request, response, viewBean);
            viewBean = (APDroitPanSituationViewBean) mainDispatcher.dispatch(viewBean, newAction);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                session.setAttribute("viewBean", viewBean);
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            this.saveViewBean(viewBean, session);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {

        String[] userAction = request.getParameterValues("userAction");
        if ((userAction != null) && (userAction.length > 1) && userAction[1].toLowerCase().equals("back")) {
            actionAfficherApresBack(session, request, response, mainDispatcher);
        } else {
            super.actionAfficher(session, request, response, mainDispatcher);
        }

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

}
