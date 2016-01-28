package globaz.corvus.servlet;

import globaz.corvus.vb.adaptation.REAdaptationDTO;
import globaz.corvus.vb.adaptation.RERentesAdapteesJointRATiersListViewBean;
import globaz.corvus.vb.adaptation.RERentesAdapteesJointRATiersViewBean;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.controller.FWViewBeanActionFactory;
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
 * 
 * @author HPE
 * 
 */
public class REAdaptationAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public REAdaptationAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return "/corvus?userAction=corvus.adaptation.rentesAdaptees.chercher";
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            FWViewBeanInterface viewBean;

            if (getAction().getClassPart().equals("rentesAdaptees")) {
                viewBean = new RERentesAdapteesJointRATiersViewBean();
            } else {
                viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
            }

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            if (getAction().getClassPart().equals("rentesAdaptees")
                    || getAction().getClassPart().equals("adaptationManuelle")) {
                String selectedId = request.getParameter("selectedId");

                if (JadeStringUtil.isEmpty(selectedId)) {
                    selectedId = request.getParameter("id");
                }

                Class b = Class.forName("globaz.globall.db.BIPersistentObject");
                Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
                mSetId.invoke(viewBean, new Object[] { selectedId });

                if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                    viewBean = beforeNouveau(session, request, response, viewBean);
                }

                viewBean = beforeAfficher(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, getAction());

            }

            viewBean.setISession(mainDispatcher.getSession());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RERentesAdapteesJointRATiersViewBean viewBean = new RERentesAdapteesJointRATiersViewBean();

        viewBean.setISession(mainDispatcher.getSession());

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);

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
            RERentesAdapteesJointRATiersListViewBean viewBean = new RERentesAdapteesJointRATiersListViewBean();

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = (RERentesAdapteesJointRATiersListViewBean) beforeLister(session, request, response, viewBean);
            viewBean = (RERentesAdapteesJointRATiersListViewBean) mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);

            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_rcListe.jsp";

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RERentesAdapteesJointRATiersListViewBean listViewBean = (RERentesAdapteesJointRATiersListViewBean) viewBean;

        REAdaptationDTO dto = (REAdaptationDTO) PRSessionDataContainerHelper.getData(session,
                PRSessionDataContainerHelper.KEY_ADAPTATION_DTO);

        if (null == dto) {
            dto = new REAdaptationDTO();
        }

        dto.setAnneeAug(listViewBean.getForAnneeAdaptation());
        dto.setCodePrestation(listViewBean.getForCodePrestation());
        dto.setMontantAdapte(listViewBean.getForNouveauMontant());
        dto.setTypeAdaptation(listViewBean.getForCsTypeAdaptation());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_ADAPTATION_DTO, dto);

        RENSSDTO nssDto = (RENSSDTO) PRSessionDataContainerHelper.getData(session,
                PRSessionDataContainerHelper.KEY_NSS_DTO);

        if (null == nssDto) {
            nssDto = new RENSSDTO();
        }

        nssDto.setNSS(listViewBean.getLikeNumeroAVS());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, nssDto);

        return super.beforeLister(session, request, response, viewBean);
    }

}
