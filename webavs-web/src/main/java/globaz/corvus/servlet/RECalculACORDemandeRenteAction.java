package globaz.corvus.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.vb.acor.RECalculACORDemandeRenteViewBean;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author hpe
 */

public class RECalculACORDemandeRenteAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE = "_de.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJCalculACORIJAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public RECalculACORDemandeRenteAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = null;

        try {

            String selectedId = request.getParameter("selectedId");

            String csTypeDemande = request.getParameter("csTypeDemandeRente");
            if (null == csTypeDemande) {
                csTypeDemande = request.getParameter("csTypeDemande");
            }

            String idTiers = request.getParameter("idTiers");
            if (null == idTiers) {
                idTiers = request.getParameter("idTierRequerant");
            }

            if (JadeStringUtil.isIntegerEmpty(selectedId)) {
                selectedId = request.getParameter("idPrononce");
            }

            FWViewBeanInterface viewBean = new RECalculACORDemandeRenteViewBean();

            ((RECalculACORDemandeRenteViewBean) viewBean).setIdDemandeRente(selectedId);
            ((RECalculACORDemandeRenteViewBean) viewBean).setCsTypeDemandeRente(csTypeDemande);
            ((RECalculACORDemandeRenteViewBean) viewBean).setIdTiers(idTiers);
            ((RECalculACORDemandeRenteViewBean) viewBean).setISession(mainDispatcher.getSession());

            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(getNumeroAvsFormate(idTiers, (BSession) mainDispatcher.getSession()));

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

            if (csTypeDemande.equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {

                RECalculACORDemandeRenteViewBean caViewBean = (RECalculACORDemandeRenteViewBean) viewBean;

                getAction().changeActionPart("actionCalculerAPI");
                mainDispatcher.dispatch(viewBean, getAction());

                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                    _destination = _getDestEchec(session, request, response, caViewBean);
                } else {
                    _destination = this.getUserActionURL(request, IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE,
                            "chercher&noDemandeRente=" + caViewBean.loadDemandeRente(null).getIdDemandeRente()
                                    + "&idTierRequerant=" + caViewBean.getIdTiers() + "&idRenteCalculee="
                                    + caViewBean.loadDemandeRente(null).getIdRenteCalculee());
                }

            } else {

                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    _destination = _getDestEchec(session, request, response, viewBean);
                } else {
                    _destination = getRelativeURL(request, session) + "_de.jsp";
                }

            }

            session.removeAttribute(FWServlet.VIEWBEAN);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Calcul de la demande API
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
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionCalculerAPI(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        RECalculACORDemandeRenteViewBean caViewBean = (RECalculACORDemandeRenteViewBean) viewBean;

        mainDispatcher.dispatch(viewBean, getAction());

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            return getRelativeURL(request, session) + RECalculACORDemandeRenteAction.VERS_ECRAN_DE;
        } else {
            return this.getUserActionURL(request, IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE,
                    "chercher&noDemandeRente=" + caViewBean.loadDemandeRente(null).getIdDemandeRente()
                            + "&idTierRequerant=" + caViewBean.getIdTiers() + "&idRenteCalculee="
                            + caViewBean.loadDemandeRente(null).getIdRenteCalculee());
        }
    }

    public String actionExporterScriptACOR(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        try {

            // l'ecriture se fait depuis le helper
            mainDispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        this.saveViewBean(viewBean, session);

        FWAction action = FWAction.newInstance("corvus.acor.calculACORDemandeRente.reAfficher");
        return this.getUserActionURL(request, action.toString());

    }

    /**
     * Sauve la feuille de calcul ACOR si besoin et importe les données contenues dans les fichiers retournes par ACOR
     * dans le cadre du calcul.
     * 
     * @param session
     *            La session HTTP
     * @param request
     *            La requête HTTP
     * @param response
     *            La réponse HTTP
     * @param mainDispatcher
     *            Le dispatcher
     * @param viewBean
     * @throws Exception
     */
    public void actionImporterScriptACOR(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        RECalculACORDemandeRenteViewBean caViewBean = (RECalculACORDemandeRenteViewBean) viewBean;

        sauvegarderFeuilleCalculAcor(caViewBean);

        mainDispatcher.dispatch(viewBean, getAction());

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) || viewBean.getMsgType().equals(FWViewBeanInterface.WARNING)) {
            forward(getRelativeURL(request, session) + RECalculACORDemandeRenteAction.VERS_ECRAN_DE, request, response);
        } else {
            goSendRedirectWithoutParameters(this.getUserActionURL(request,
                    IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE,
                    "chercher&noDemandeRente=" + caViewBean.loadDemandeRente(null).getIdDemandeRente()
                            + "&idTierRequerant=" + caViewBean.getIdTiers()), request, response);
        }
    }

    /**
     * Méthode qui retourne le NNSS formaté
     * 
     * @param session
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

    /**
     * InfoRom 198 : Stockage de la feuille de calcul ACOR, si le paramètre est actif
     * 
     * @param caViewBean
     * @throws RemoteException
     * @throws Exception
     */
    private void sauvegarderFeuilleCalculAcor(RECalculACORDemandeRenteViewBean caViewBean) throws Exception {
        String isActive = GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS).getProperty(
                "calculAcor.save");

        if ("true".equals(isActive)) {

            try {
                String path = GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS).getProperty(
                        "calculAcor.path");

                if (JadeStringUtil.isEmpty(path)) {
                    throw new Exception(
                            "Can not save the 'feuille de calculc ACOR' because property [calculAcor.path] is empty");
                }

                if (!path.endsWith("\\") && !path.endsWith("/")) {
                    path += File.separatorChar;
                }

                if (caViewBean.getContenuFeuilleCalculXML() != null) {
                    Date d = new Date();
                    String filePath = path + JadeDateUtil.getYMDDate(d) + "-" + d.getTime() + ".xml";
                    BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                    writer.write(caViewBean.getContenuFeuilleCalculXML());
                    writer.newLine();
                    writer.close();
                }
            } catch (Exception exception) {
                // BZ 8253 - la description de l'exception est transmise au
                // view bean afin que cette information soit reprise sous la
                // section "Message" par l'écran d'erreur
                caViewBean.setMsgType(FWViewBeanInterface.ERROR);
                caViewBean.setMessage(exception.toString());

                JadeLogger.error(this, exception);
                throw exception;
            }
        }
    }
}
