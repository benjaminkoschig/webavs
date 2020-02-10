package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUserGroup;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pegasus.vb.rpc.PCAnnoncesViewBean;
import globaz.pegasus.vb.rpc.PCDetailAnnonceAjaxViewBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.process.byitem.ProcessItemsService;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.rpc.process.GenererAnnoncesProcess;

public class PCRpcServletAction extends PCAbstractServletAction {

    private String idAnnonce = null;
    private String idDossier = null;
    private String idVersion = null;

    private String defaultEtat = null;
    private String defaultCode = null;
    private String mode = null;
    private String nss = null;
    private String nom = null;
    private String prenom = null;
    private String periodDebut = null;
    private String periodFin = null;
    private String sortBy = null;
    private boolean rechercheFamille = false;
    private String destination = "";

    /**
     * Constructeur
     *
     * @param aServlet
     */
    public PCRpcServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.framework.controller.FWDefaultServletAction#actionAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                 FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.actionAjouter(session, request, response, mainDispatcher);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.actionModifier(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionListerAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                    FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);

        /*
         * Creation dynamique de notre viewBean
         */
        FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(newAction, mainDispatcher.getPrefix());

        if (viewBean instanceof PCDetailAnnonceAjaxViewBean) {
            try {

                ((PCDetailAnnonceAjaxViewBean) viewBean).setAnnonceId(request.getParameter("annonceId"));


                /*
                 * set des properietes
                 */
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

                if (FWAJAXViewBeanInterface.class.isAssignableFrom(viewBean.getClass())) {
                    ((FWAJAXViewBeanInterface) viewBean).setGetListe(true);

                }

                if (FWAJAXFindInterface.class.isAssignableFrom(viewBean.getClass())) {
                    String orderKey = request.getParameter("orderKey");
                    if (!JadeStringUtil.isEmpty(orderKey) && !"null".equalsIgnoreCase(orderKey)) {
                        ((FWAJAXFindInterface) viewBean).getSearchModel().setOrderKey(request.getParameter("orderKey"));
                    }
                }

                /*
                 * initialisation du viewBean appelle beforeLister, puis le Dispatcher, puis met le bean en session
                 */
                viewBean = beforeLister(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, newAction);

                request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                /*
                 * choix destination
                 */
                // if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                // this.destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
                // } else {
                destination = getAJAXListerSuccessDestination(session, request, viewBean);
                // }

            } catch (Exception e) {
                destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
                request.setAttribute("exception", e);
            }
            /*
             * redirection vers la destination
             */
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        } else {
            super.actionListerAJAX(session, request, response, mainDispatcher);
        }
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
                                                 HttpServletResponse response, FWViewBeanInterface viewBean) {

        if ((viewBean instanceof PCDetailAnnonceAjaxViewBean)) {
            PCDetailAnnonceAjaxViewBean vb = (PCDetailAnnonceAjaxViewBean) viewBean;
            idAnnonce = request.getParameter("annonceId");
            idDossier = request.getParameter("idDossier");
            idVersion = request.getParameter("idVersion");

            try {
                vb.setAnnonceId(idAnnonce);
                vb.setIdDossier(idDossier);
                vb.setIdVersion(idVersion);
                viewBean = vb;
            } catch (Exception e) {
                JadeLogger.error(this, e.getMessage());
            }
        } else if (viewBean instanceof PCAnnoncesViewBean) {

            PCAnnoncesViewBean vb = (PCAnnoncesViewBean) viewBean;
            boolean hasRightsForGroupResponsableRPC = hasRightsForGroupResponsableRPC((BSession) viewBean.getISession());
            boolean canGenerateAnnonces = canGenerateAnnonces();
            Boolean isProcessLaunched = ProcessItemsService.isProcessRunnig(GenererAnnoncesProcess.KEY);

            nss = request.getParameter("nss");
            nom = request.getParameter("nom");
            prenom = request.getParameter("prenom");
            defaultEtat = request.getParameter("etat");
            defaultCode = request.getParameter("codeTraitement");
            periodDebut = request.getParameter("periodeDateDebut");
            periodFin = request.getParameter("periodeDateFin");
            sortBy = request.getParameter("order");
            rechercheFamille = Boolean.parseBoolean(request.getParameter("rechercheFamille"));

            try {
                if (defaultEtat != null) {
                    vb.setDefaultEtat(defaultEtat);
                }
                if (defaultCode != null) {
                    vb.setDefaultCode(defaultCode);
                }

                vb.setNss(nss);
                vb.setNom(nom);
                vb.setPrenom(prenom);
                vb.setPeriodDebut(periodDebut);
                vb.setPeriodFin(periodFin);
                vb.setSortBy(sortBy);
                vb.setRechercheFamille(rechercheFamille);
                viewBean = vb;
            } catch (Exception e) {
                JadeLogger.error(this, e.getMessage());
            }

            request.setAttribute("processLaunched", isProcessLaunched);
            request.setAttribute("hasRightsForGroupResponsableRPC", hasRightsForGroupResponsableRPC);
            request.setAttribute("canGenerateAnnonces", canGenerateAnnonces);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    private boolean hasRightsForGroupResponsableRPC(final BSession session) {
        boolean hasRights = false;

        try {
            // Récupération de la valeur de la propriété (peut avoir plusieurs groupes séparé par des virgules)
            final String groupResponsableValue = PCproperties.getProperties(EPCProperties.RPC_GROUPE_RESPONSABLE);

            if (!JadeStringUtil.isBlankOrZero(groupResponsableValue)) {

                // Nous supprimons les espaces vides et découpons les virgules pour en faire un tableau
                final String[] groupsResponsable = StringUtils.deleteWhitespace(groupResponsableValue).split(",");

                final HashSet<String> hashSet = new HashSet<String>();
                hashSet.addAll(Arrays.asList(groupsResponsable));

                // Récupération des tous les groupes de l'utilisateur de la session
                final JadeUserGroup[] groupsUser = JadeAdminServiceLocatorProvider.getLocator().getUserGroupService()
                        .findForIdUser(session.getUserId());

                // Vérification si l'utilisateur a bien un des groupes responsable pour les RPC
                for (int i = 0; i < groupsUser.length; i++) {
                    final JadeUserGroup groupUser = groupsUser[i];

                    if (hashSet.contains(groupUser.getIdGroup())) {
                        hasRights = true;
                    }
                }
            }
        } catch (PropertiesException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return hasRights;
    }

    // Only is possible to generate annonces between 1st and 10th of each month (take day from RPC properties on DB)
    private boolean canGenerateAnnonces() {
        boolean canGenerate = false;
        try {
            // Récupération de la valeur de la propriété (peut avoir plusieurs groupes séparé par des virgules)
            final String limitDayString = PCproperties.getProperties(EPCProperties.RPC_LIMIT_DAY_GENERATION);

            if (!JadeStringUtil.isBlankOrZero(limitDayString)) {
                int todayDay = Integer.parseInt(Date.now().getJour());
                int limitDay = Integer.parseInt(limitDayString);
                canGenerate = todayDay <= limitDay;
            }
        } catch (PropertiesException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return canGenerate;
    }

}
