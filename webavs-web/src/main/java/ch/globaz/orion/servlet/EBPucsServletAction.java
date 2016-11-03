package ch.globaz.orion.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.process.importpucs.EBImportPucsDanProcess;
import globaz.orion.process.importpucs.EBImportSwissDecProcess;
import globaz.orion.vb.pucs.EBPucsFileViewBean;
import globaz.orion.vb.pucs.EBPucsImportViewBean;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.common.process.byitem.ProcessItemsFactory;
import ch.globaz.common.process.byitem.ProcessItemsService;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.business.constantes.EBProperties;

public class EBPucsServletAction extends EBAbstractServletAction {

    public EBPucsServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    protected void _actionChangeUser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";
        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = new EBPucsFileViewBean();

            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */

            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);
            EBPucsFileViewBean vb2 = (EBPucsFileViewBean) viewBean;
            vb2.setId(request.getParameter("selectedId"));
            vb2.setProvenance(request.getParameter("provenance"));
            vb2.changeUser();
            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            destination = getActionFullURL() + ".lister";

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("changeUser")) {
            _actionChangeUser(session, request, response, dispatcher);
        } else if (getAction().getActionPart().equals("importInDb")) {
            BSession bsession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
            Boolean isProcessRunnig = ProcessItemsService.isProcessRunnig(EBImportPucsDanProcess.KEY,
                    EBImportSwissDecProcess.KEY);
            if (!isProcessRunnig) {
                try {
                    if (!EBProperties.PUCS_SWISS_DEC_DIRECTORY.isEmpty()) {
                        ProcessItemsFactory.newInstance().session(bsession).start(new EBImportSwissDecProcess())
                                .build();
                    }
                } catch (PropertiesException e) {
                    throw new RuntimeException(e);
                }
                ProcessItemsFactory.newInstance().session(bsession).start(new EBImportPucsDanProcess()).build();
            }
            String destination = "/" + getAction().getApplicationPart() + "?userAction=orion.pucs.pucsFile.chercher";
            goSendRedirect(destination, request, response);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // Set le viewbean en session - vient d'un rc -> pas le bon vb
        // potentiellement
        session.setAttribute("viewBean", FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix()));
        // On prépare la liste pour l'affichage sur le prohain écran
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            ((EBPucsImportViewBean) viewBean).setSession((BSession) ((FWController) session
                    .getAttribute("objController")).getSession());
            // ((EBPucsImportViewBean) viewBean).initMapForDisplay();
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("viewBean", viewBean);

        super.actionExecuter(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination;

        try {
            FWAction privateAction = getAction();
            /*
             * pour compatibilité : si on a le parametre _method=add, c'est que l'on a une action nouveau
             */
            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                privateAction.changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");
            if (JadeStringUtil.isEmpty(selectedId)) {
                selectedId = request.getParameter("id");
            }

            /*
             * Creation dynamique de notre viewBean
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(privateAction,
                    mainDispatcher.getPrefix());

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * pour pouvoir faire un setId remarque : si il y a d'autre set a faire (si plusieurs id par ex) il faut le
             * faire dans le beforeAfficher(...)
             */
            if (viewBean instanceof BIPersistentObject) {
                Class<BIPersistentObject> b = globaz.globall.db.BIPersistentObject.class;
                Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
                mSetId.invoke(viewBean, new Object[] { selectedId });
            }

            /*
             * initialisation du viewBean
             */
            if (privateAction.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, privateAction);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
