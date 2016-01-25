package globaz.corvus.servlet;

import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.corvus.vb.rentesaccordees.RERepriseDuDroitViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RERepriseDuDroitAction extends PRDefaultAction {

    public RERepriseDuDroitAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = "/corvus?userAction=" + IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE + ".chercher";
        return destination;
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";
        String selectedId = request.getParameter("idRenteAccordee");

        FWViewBeanInterface viewBean = new RERepriseDuDroitViewBean();

        try {
            Class<?> b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            PRTiersWrapper tier = PRTiersHelper.getTiersParId(mainDispatcher.getSession(),
                    ((RERepriseDuDroitViewBean) viewBean).getIdTiersBeneficiaire());

            String nss = "";

            if (tier != null) {
                nss = tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            }

            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(nss);

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp?_method=add";
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);

            String message = viewBean.getMessage();
            if (JadeStringUtil.isBlank(message)) {
                message = e.getMessage();
            } else {
                message += "<br />" + e.getMessage();
            }
            viewBean.setMessage(message);

            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        this.saveViewBean(viewBean, request);
        this.saveViewBean(viewBean, session);

        /*
         * redirection vers la destination
         */
        forward(_destination, request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";
        String action = request.getParameter("userAction");

        FWAction _action = FWAction.newInstance(action);
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(FWServlet.VIEWBEAN);

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = "/corvus?userAction=" + IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE
                        + ".chercher";
            } else {
                _destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);

            String message = viewBean.getMessage();
            if (JadeStringUtil.isBlank(message)) {
                message = e.getMessage();
            } else {
                message += "<br />" + e.getMessage();
            }
            viewBean.setMessage(message);

            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        this.saveViewBean(viewBean, session);

        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        try {
            if (viewBean instanceof RERepriseDuDroitViewBean) {
                ((RERepriseDuDroitViewBean) viewBean).retrieve();
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        return viewBean;
    }
}
