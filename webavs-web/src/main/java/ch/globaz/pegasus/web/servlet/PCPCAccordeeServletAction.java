package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BIPersistentObject;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.pegasus.vb.pcaccordee.PCPcAccordeeDetailViewBean;
import globaz.pegasus.vb.pcaccordee.PCPlanCalculViewBean;
import globaz.prestation.ged.PRGedAffichageDossier;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.pegasus.business.constantes.IPCActions;

public class PCPCAccordeeServletAction extends PCAbstractServletAction {

    private static final String EGAL = "=";
    private static final String ESPERLUETTE = "&";
    private static final String ID_DEMANDE_PARAM = "idDemande";
    private static final String ID_DEMANDE_PC_PARAM = "idDemandePc";
    private static final String ID_DOSSIER_PARAM = "idDossier";
    private static final String ID_DROIT_PARAM = "idDroit";
    private static final String ID_VERSION_DROIT_PARAM = "idVersionDroit";

    private static final String IS_READY_FOR_DAC_PARAM = "isReadyForDac";
    private static final String NO_VERSION_DROIT_PARAM = "noVersion";

    private static final String PCACCORDEE_DETAIL_DEBLOCAGE_MONTANT_URL = "/pegasus?userAction=pegasus.pcaccordee.pcAccordeeDeblocageMontant.afficher";
    private static final String PCACCORDEE_LIST_URL = "/pegasus?userAction=pegasus.pcaccordee.pcAccordee.rechercher";
    private static final String WHERE_KEY_PARAM = "whereKey";
    private String idDemande = null;
    private String idDossier = null;
    private String idDroit = null;
    private String idVersionDroit = null;
    private String isReadyForDac = null;
    private String noVersion = null;
    private String whereKey = null;

    public PCPCAccordeeServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        // on n'utilise pas la session pour afficher le plan de calcule, car il se trouvera dans un nouvelle onglet
        if (getAction().getElement().contains(IPCActions.ACTION_PCACCORDEE_PLANCLACULE)) {
            String destination = "";

            try {
                String selectedId = request.getParameter("selectedId");
                if (JadeStringUtil.isEmpty(selectedId)) {
                    selectedId = request.getParameter("id");
                }

                /*
                 * Creation dynamique de notre viewBean
                 */
                FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(),
                        mainDispatcher.getPrefix());

                ((BIPersistentObject) viewBean).setId(selectedId);
                /*
                 * initialisation du viewBean
                 */
                if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                    viewBean = beforeNouveau(session, request, response, viewBean);
                }

                /*
                 * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
                 */
                viewBean = beforeAfficher(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, getAction());
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                /*
                 * choix destination
                 */

                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    destination = FWDefaultServletAction.ERROR_PAGE;
                } else {
                    destination = getRelativeURL(request, session) + "_de.jsp";
                }
                viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            } catch (Exception e) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

            /*
             * redirection vers la destination
             */
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        } else {
            super.actionAfficher(session, request, response, mainDispatcher);
        }
    }

    /**
     * Affichage du dossier en GED
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws ServletException
     * @throws IOException
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws NullPointerException
     * @throws ClassCastException
     * @throws JadeClassCastException
     */
    public void actionAfficherDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException,
            JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException, ClassCastException,
            JadeClassCastException {
        PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);
    }

    public String actionBloquerMontantPC(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        idDemande = null;
        idDossier = null;
        idDroit = null;
        noVersion = null;
        whereKey = null;

        if (viewBean instanceof PCPcAccordeeDetailViewBean) {
            PCPcAccordeeDetailViewBean vb = (PCPcAccordeeDetailViewBean) viewBean;
            // vb.bloquerPC();
            session.setAttribute("viewBean", vb);
            viewBean = vb;

        }

        return PCPCAccordeeServletAction.PCACCORDEE_DETAIL_DEBLOCAGE_MONTANT_URL;
    }

    public String actionBloquerPC(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        idDemande = null;
        idDossier = null;
        idDroit = null;
        noVersion = null;
        whereKey = null;

        if (viewBean instanceof PCPcAccordeeDetailViewBean) {
            PCPcAccordeeDetailViewBean vb = (PCPcAccordeeDetailViewBean) viewBean;
            vb.bloquerPC();
            session.setAttribute("viewBean", vb);
            // request.setAttribute("viewBean", vb);
        }

        // récupération des valeurs pour la génération de l'url de retour
        String idDroit = ((PCPcAccordeeDetailViewBean) viewBean).getPcAccordee().getSimpleDroit().getId();
        String noVersion = ((PCPcAccordeeDetailViewBean) viewBean).getPcAccordee().getSimpleVersionDroit()
                .getNoVersion();

        return getListPcAccordeeUrl(idDroit, noVersion);
    }

    public String actionDebloquerMontantPC(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        idDemande = null;
        idDossier = null;
        idDroit = null;
        noVersion = null;
        whereKey = null;

        if (viewBean instanceof PCPcAccordeeDetailViewBean) {
            PCPcAccordeeDetailViewBean vb = (PCPcAccordeeDetailViewBean) viewBean;
            // vb.bloquerPC();
            session.setAttribute("viewBean", vb);
            session.setAttribute("selectedId", request.getParameter("selectedId"));
        }

        return PCPCAccordeeServletAction.PCACCORDEE_DETAIL_DEBLOCAGE_MONTANT_URL;
    }

    public String actionDebloquerPC(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        idDemande = null;
        idDossier = null;
        idDroit = null;
        noVersion = null;
        whereKey = null;

        if (viewBean instanceof PCPcAccordeeDetailViewBean) {
            PCPcAccordeeDetailViewBean vb = (PCPcAccordeeDetailViewBean) viewBean;
            vb.deBloquerPC();
            session.setAttribute("viewBean", vb);
        }

        // récupération des valeurs pour la génération de l'url de retour
        String idDroit = ((PCPcAccordeeDetailViewBean) viewBean).getPcAccordee().getSimpleDroit().getId();
        String noVersion = ((PCPcAccordeeDetailViewBean) viewBean).getPcAccordee().getSimpleVersionDroit()
                .getNoVersion();

        return getListPcAccordeeUrl(idDroit, noVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCPlanCalculViewBean) {
            // parametres pour plan de calcul, id plan de calcul, et id tiers beneficiare
            if (request.getParameter("idPca") != null) {
                ((PCPlanCalculViewBean) viewBean).setIdPca(request.getParameter("idPca"));
            } else if (request.getParameter("idPcal") != null) {
                ((PCPlanCalculViewBean) viewBean).setIdPcal(request.getParameter("idPcal"));
            }

            if (request.getParameter("idBenef") != null) {
                ((PCPlanCalculViewBean) viewBean).setIdBeneficiaire(request.getParameter("idBenef"));
            }
        }
        if (viewBean instanceof PCPcAccordeeDetailViewBean) {
            if (request.getParameter("idPca") != null) {
                ((PCPcAccordeeDetailViewBean) viewBean).setId(request.getParameter("idPca"));
            }
        }

        return super.beforeAfficher(session, request, response, viewBean);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#doAction(javax.servlet .http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWController)
     */
    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {

        putParameters(request);
        super.doAction(session, request, response, mainController);
    }

    private String getListPcAccordeeUrl(String idDroit, String noVersion) {
        StringBuffer url = new StringBuffer(PCPCAccordeeServletAction.PCACCORDEE_LIST_URL);
        url.append(getParameterWithValueAsString(PCPCAccordeeServletAction.ID_DROIT_PARAM, idDroit));
        url.append(getParameterWithValueAsString(PCPCAccordeeServletAction.NO_VERSION_DROIT_PARAM, noVersion));
        return url.toString();
    }

    private String getParameterWithValueAsString(String parameter, String value) {
        StringBuilder urlReturning = new StringBuilder("");
        urlReturning.append(PCPCAccordeeServletAction.ESPERLUETTE);
        urlReturning.append(parameter);
        urlReturning.append(PCPCAccordeeServletAction.EGAL);
        urlReturning.append(value);
        return urlReturning.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWDefaultServletAction# goSendRedirectWithoutParameters(java.lang.String,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goSendRedirect(String url, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuffer urlReturning = new StringBuffer(url);

        if (!JadeStringUtil.isEmpty(idDroit)) {
            urlReturning.append(getParameterWithValueAsString(PCPCAccordeeServletAction.ID_DROIT_PARAM, idDroit));
        }
        if (!JadeStringUtil.isEmpty(noVersion)) {
            urlReturning.append(getParameterWithValueAsString(PCPCAccordeeServletAction.NO_VERSION_DROIT_PARAM,
                    noVersion));
        }
        if (!JadeStringUtil.isEmpty(whereKey)) {
            urlReturning.append(getParameterWithValueAsString(PCPCAccordeeServletAction.WHERE_KEY_PARAM, whereKey));
        }
        if (!JadeStringUtil.isEmpty(idDemande)) {
            urlReturning.append(getParameterWithValueAsString(PCPCAccordeeServletAction.ID_DEMANDE_PARAM, idDemande));
            urlReturning
                    .append(getParameterWithValueAsString(PCPCAccordeeServletAction.ID_DEMANDE_PC_PARAM, idDemande));
        }
        if (!JadeStringUtil.isEmpty(idDossier)) {
            urlReturning.append(getParameterWithValueAsString(PCPCAccordeeServletAction.ID_DOSSIER_PARAM, idDossier));
        }
        if (!JadeStringUtil.isEmpty(isReadyForDac)) {
            urlReturning.append(getParameterWithValueAsString(PCPCAccordeeServletAction.IS_READY_FOR_DAC_PARAM,
                    isReadyForDac));
        }
        if (!JadeStringUtil.isEmpty(idVersionDroit)) {
            urlReturning.append(getParameterWithValueAsString(PCPCAccordeeServletAction.ID_VERSION_DROIT_PARAM,
                    idVersionDroit));
        }
        super.goSendRedirect(url, request, response);
    }

    private void putParameters(HttpServletRequest request) {
        idDemande = request.getParameter("idDemandePc");
        idDroit = request.getParameter("idDroit");
        noVersion = request.getParameter("noVersion");
        whereKey = request.getParameter("whereKey");
        idDossier = request.getParameter("idDossier");
        isReadyForDac = request.getParameter("isReadyForDac");
        idVersionDroit = request.getParameter("idVersionDroit");
    }
}
