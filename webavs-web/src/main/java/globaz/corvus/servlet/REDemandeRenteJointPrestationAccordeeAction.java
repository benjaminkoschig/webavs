package globaz.corvus.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.vb.demandes.REDemandeParametresRCDTO;
import globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeListViewBean;
import globaz.corvus.vb.demandes.REDemandeRenteViewBean;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.ged.PRGedAffichageDossier;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author hpe
 * 
 */
public class REDemandeRenteJointPrestationAccordeeAction extends PRDefaultAction {

    public REDemandeRenteJointPrestationAccordeeAction(FWServlet servlet) {
        super(servlet);
    }

    public void actionAfficherDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException,
            JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException, ClassCastException,
            JadeClassCastException {
        PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);
    }

    /**
     * Redéfinition d'actionChercher permettant de créer un viewBean qui sera utilisé pour l'affichage de données dans
     * la page rc.
     * 
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * 
     * @throws ServletException
     * @throws IOException
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        REDemandeRenteJointPrestationAccordeeListViewBean viewBean = new REDemandeRenteJointPrestationAccordeeListViewBean();

        mainDispatcher.dispatch(viewBean, getAction());
        this.saveViewBean(viewBean, request);

        String idTiersRequerant = (String) session.getAttribute("idTierRequerant");

        // Modification Inforo297 - Affichage vue globale
        // On récupère l'idTiersRequerent présent dans la request si l'on arrive depuis le lien de la vue globale
        try {
            if (request.getParameter("idTierRequerant") != null) {
                idTiersRequerant = request.getParameter("idTierRequerant");
            }
        } catch (Exception e) {
            // nothing
        }

        if ((null != idTiersRequerant) && !JadeStringUtil.isEmpty(idTiersRequerant)) {
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(getNumeroAvsFormate(idTiersRequerant, (BSession) mainDispatcher.getSession()));
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);
            session.setAttribute("idTierRequerant", "");
        }

        if ((viewBean.getISession() == null) || viewBean.hasErrors()) {
            // si on rentre dans corvus et qu'on a pas les droits, la session
            // vaut null
            forward(FWDefaultServletAction.ERROR_PAGE, request, response);
        } else {
            forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
        }
    }

    public String actionCopierDemande(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        String id = request.getParameter("noDemandeRente");
        REDemandeRenteViewBean demande = new REDemandeRenteViewBean();
        demande.setIdDemandeRente(id);

        mainDispatcher.dispatch(demande, getAction());
        this.saveViewBean(demande, request);

        String r = this.getUserActionURL(request, IREActions.ACTION_SAISIE_DEMANDE_RENTE, "afficherCopie");
        return r;
    }

    /**
     * Copie de la demande de rente pour la création d'une prestation transitoire
     * Renvoie vers la jsp : saisiePrestationTransitoire_de.jsp
     * 
     * @param session
     *            La session http
     * @param request
     *            La requête http
     * @param response
     *            La réponse http
     * @param mainDispatcher
     *            Le dispatcher
     * @param viewBean
     *            Le viewBean
     * @return
     */
    public String actionCopierDemandePourPrestTrans(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        String id = request.getParameter("noDemandeRente");
        REDemandeRenteViewBean demandeViewBean = new REDemandeRenteViewBean();
        demandeViewBean.setIdDemandeRente(id);
        mainDispatcher.dispatch(demandeViewBean, getAction());
        this.saveViewBean(demandeViewBean, request);
        this.saveViewBean(demandeViewBean, session);

        String destination = this.getUserActionURL(request, IREActions.ACTION_SAISIE_DEMANDE_RENTE,
                "afficherPrestationTransitoire");
        if (FWViewBeanInterface.ERROR.equals(demandeViewBean.getMsgType())) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        return destination;
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;
        try {
            /*
             * creation automatique du listviewBean
             */
            FWViewBeanInterface viewBean = new REDemandeRenteJointPrestationAccordeeListViewBean();

            /*
             * set automatique des properties du listViewBean depuis la requête
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeLister() , puis appelle du dispatcher, puis le bean est mis en request
             */
            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);

            // pour bt [...] et pagination
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            /*
             * destination : remarque : si erreur, on va quand même sur la liste avec le bean vide en erreur
             */
            destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RENSSDTO dtoNss = new RENSSDTO();

        dtoNss.setNSS(request.getParameter("likeNumeroAVS"));
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dtoNss);

        REDemandeRenteJointPrestationAccordeeListViewBean listViewBean = (REDemandeRenteJointPrestationAccordeeListViewBean) viewBean;

        REDemandeParametresRCDTO dto = new REDemandeParametresRCDTO();

        dto.setForCsEtatDemande(listViewBean.getForCsEtatDemande());
        dto.setForCsSexe(listViewBean.getForCsSexe());
        dto.setForCsType(listViewBean.getForCsType());
        dto.setForDroitDu(listViewBean.getForDroitDu());
        dto.setForDroitAu(listViewBean.getForDroitAu());
        dto.setForDateNaissance(listViewBean.getForDateNaissance());
        dto.setLikeNom(listViewBean.getLikeNom());
        dto.setLikePrenom(listViewBean.getLikePrenom());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);

    }

    private String getNumeroAvsFormate(String idTierRequerant, BSession session) {

        String result = "";

        if (!JadeStringUtil.isIntegerEmpty(idTierRequerant)) {

            PRTiersWrapper tiers;
            try {
                tiers = PRTiersHelper.getTiersParId(session, idTierRequerant);
                String nnss = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                result = NSUtil.formatWithoutPrefixe(nnss, nnss.length() > 14 ? true : false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public String imprimerListeDemandeRente(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        REDemandeRenteJointPrestationAccordeeListViewBean dViewBean = new REDemandeRenteJointPrestationAccordeeListViewBean();
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, dViewBean);
            mainDispatcher.dispatch(dViewBean, getAction());
            this.saveViewBean(dViewBean, request);
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }

        return getRelativeURL(request, session) + "_rc.jsp";
    }

}
