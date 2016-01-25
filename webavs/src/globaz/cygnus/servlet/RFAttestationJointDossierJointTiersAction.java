package globaz.cygnus.servlet;

import globaz.cygnus.api.attestations.IRFAttestations;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.RFNSSDTO;
import globaz.cygnus.vb.attestations.RFAttestationJointDossierJointTiersListViewBean;
import globaz.cygnus.vb.attestations.RFAttestationJointDossierJointTiersViewBean;
import globaz.cygnus.vb.attestations.RFAttestationPiedDePageViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFAttestationJointDossierJointTiersAction extends RFDefaultAction {

    public RFAttestationJointDossierJointTiersAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub

    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_DOSSIER_JOINT_TIERS + ".chercher";
        return _destination;

    }

    /*
     * peut-on éviter de tout redéfinir?
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFAttestationJointDossierJointTiersViewBean viewBean = new RFAttestationJointDossierJointTiersViewBean();

        if (!JadeStringUtil.isBlank(request.getParameter("likeNumeroAVS"))) {
            viewBean.setLikeNumeroAVS(request.getParameter("likeNumeroAVS"));
        }

        viewBean.setSession((BSession) mainDispatcher.getSession());
        mainDispatcher.dispatch(viewBean, getAction());
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);

        this.saveViewBean(viewBean, request);

        /*
         * choix destination
         */
        String _destination = "";

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } else {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {

            RFAttestationPiedDePageViewBean outputViewBean = new RFAttestationPiedDePageViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, outputViewBean);
            outputViewBean.setCsTypeAttestation(request.getParameter("csTypeAttestation"));
            outputViewBean.setIsUpdate(Boolean.TRUE);
            outputViewBean.setCodeSousTypeDeSoinList(outputViewBean.getCodeSousTypeDeSoin());
            outputViewBean.setCodeTypeDeSoinList(outputViewBean.getCodeTypeDeSoin());
            outputViewBean.setIdGestionnaire(request.getParameter("idGestionnaire"));
            return outputViewBean;

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RFNSSDTO dto = new RFNSSDTO();

        dto.setNSS(((RFAttestationJointDossierJointTiersListViewBean) viewBean).getLikeNumeroAVS());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);
    }

    // on veut le redefinir
    @Override
    protected String getRelativeURL(HttpServletRequest request, HttpSession session) {

        if ("afficher".equals(getAction().getActionPart())) {

            String cygnusRelativeUrl = "/cygnusRoot/attestations/attestation";
            String _destination = null;
            String codeAttestation = null;
            // récupére le code système correspondant au type d'attestation
            codeAttestation = request.getParameter("csTypeAttestation");

            if (codeAttestation.equals(IRFAttestations.REGIME_ALIMENTAIRE)) {
                _destination = cygnusRelativeUrl + "RegimeAli2";

            } else if (codeAttestation.equals(IRFAttestations.FRAIS_LIVRAISON)) {
                _destination = cygnusRelativeUrl + "FraisLivraison9RepriseLit10";

            } else if (codeAttestation.equals(IRFAttestations.BON_MOYENS_AUXILIAIRES)) {
                _destination = cygnusRelativeUrl + "BonMoyensAux11";

            } else if (codeAttestation.equals(IRFAttestations.MAINTIEN_DOMICILE)) {
                _destination = cygnusRelativeUrl + "MaintienDomicile13";
            } else if (codeAttestation.equals(IRFAttestations.CERTIFICAT_MOYENS_AUXILIAIRES)) {
                _destination = cygnusRelativeUrl + "CertificatMoyensAux3";
            } else if (codeAttestation.equals(IRFAttestations.DECISION_MOYENS_AUXILIAIRES)) {
                _destination = cygnusRelativeUrl + "DecisionMoyensAux5";
            } else if (codeAttestation.equals(IRFAttestations.ATTESTATION_AVS)) {
                _destination = cygnusRelativeUrl + "AVS";
            } else {
                _destination = cygnusRelativeUrl + "Defaut";
            }

            return _destination;
        } else {
            return super.getRelativeURL(request, session);
        }
    }

}
