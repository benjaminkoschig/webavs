/*
 * Créé le 31 mai 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.corvus.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.vb.ci.REDemandesRassemblementViewBean;
import globaz.corvus.vb.ci.RERassemblementCIViewBean;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
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
 * @author HPE
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class REDemandesRassemblementAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REDemandesRassemblementAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    /**
     * 
     * @author BSC
     * 
     *         Pour changer le modèle de ce commentaire de type généré, allez à :
     *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";
        String errorMessage = null;

        try {
            String selectedId = request.getParameter("selectedId");
            REDemandesRassemblementViewBean viewBean = new REDemandesRassemblementViewBean();
            viewBean.setIdDemandeRente(selectedId);

            FWViewBeanInterface vbs = loadViewBean(session);
            if (vbs != null && vbs instanceof RERassemblementCIViewBean
                    && vbs.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                errorMessage = vbs.getMessage();
            }

            getAction().changeActionPart(FWAction.ACTION_NOUVEAU);

            if (vbs != null && vbs instanceof REDemandesRassemblementViewBean
                    && ((REDemandesRassemblementViewBean) vbs).isRetourDepuisPyxis()) {
                viewBean = (REDemandesRassemblementViewBean) vbs;
            } else {
                mainDispatcher.dispatch(viewBean, getAction());
            }

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = getRelativeURL(request, session) + "_de.jsp?selectedId=" + selectedId + "&" + METHOD_ADD;
            } else {
                destination = ERROR_PAGE;
            }

            if (errorMessage != null) {
                viewBean.setMessage(errorMessage);
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(getNumeroAvsFormate(viewBean.getIdRequerant(), (BSession) mainDispatcher.getSession()));

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

            saveViewBean(viewBean, session);

        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
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
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionEnvoyerDemandeRassemblement(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        // on cherche la demande de rassemblement pour laquelle l'action valider
        // a ete demandee
        String position = request.getParameter("position");
        if (!JadeStringUtil.isEmpty(position)) {

            // on retrouve le rci
            String idRCI = request.getParameter("idRCI_" + position);
            RERassemblementCIViewBean rciViewBean = new RERassemblementCIViewBean();
            rciViewBean.setSession((BSession) mainDispatcher.getSession());
            rciViewBean.setIdRCI(idRCI);
            rciViewBean.retrieve();

            // memorisation du motif et de la date de cloture pour effectuer la
            // demande dans le helper
            rciViewBean.setMotifForDemande(request.getParameter("motif_" + position));
            rciViewBean.setDateClotureForDemande(request.getParameter("dateCloture_" + position));
            // rciViewBean.setIdTiersAyantDroit(request.getParameter("idTiers_"
            // + position));
            rciViewBean.setIdTiersAyantDroit(request.getParameter("idTiersAyantDroit"));
            rciViewBean.setIsTiersAyantDroit(Boolean.valueOf(request.getParameter("isTiersAyantDroit_" + position)));

            // transmet la demande de rassemblement
            // met a jours le rci
            // getAction().setRight(FWSecureConstants.UPDATE);

            rciViewBean = (RERassemblementCIViewBean) mainDispatcher.dispatch(rciViewBean, getAction());

            saveViewBean(rciViewBean, session);
        } else {
            return ERROR_PAGE;
        }

        return getUserActionURL(request, IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT, FWAction.ACTION_AFFICHER);
    }

    /**
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
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionNouvelleDemandeRassemblement(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        // on cherche la demande de rassemblement pour laquelle l'action valider
        // a ete demandee
        String position = request.getParameter("position");
        if (!JadeStringUtil.isEmpty(position)) {

            // on retrouve le rci rassemble
            String idRCI = request.getParameter("idRCI_" + position);
            RERassemblementCIViewBean rciViewBean = new RERassemblementCIViewBean();
            rciViewBean.setSession((BSession) mainDispatcher.getSession());
            rciViewBean.setIdRCI(idRCI);
            rciViewBean.retrieve();

            // getAction().setRight(FWSecureConstants.UPDATE);
            mainDispatcher.dispatch(rciViewBean, getAction());

            saveViewBean(rciViewBean, session);
        } else {
            return ERROR_PAGE;
        }

        return getUserActionURL(request, IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT, FWAction.ACTION_AFFICHER);
    }

    /**
     * Méthode qui retourne le NNSS formaté
     * 
     * @param session
     * 
     * @return NNSS formaté
     */
    private String getNumeroAvsFormate(String idTierBeneficiaire, BSession session) {

        String result = "";

        if (!JadeStringUtil.isIntegerEmpty(idTierBeneficiaire)) {

            PRTiersWrapper tiers;
            try {
                tiers = PRTiersHelper.getTiersParId(session, idTierBeneficiaire);
                String nnss = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                result = NSUtil.formatWithoutPrefixe(nnss, nnss.length() > 14 ? true : false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}