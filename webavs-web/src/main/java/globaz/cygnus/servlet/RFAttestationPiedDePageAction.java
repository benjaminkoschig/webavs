package globaz.cygnus.servlet;

import globaz.cygnus.api.attestations.IRFAttestations;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.attestations.RFAttestationPiedDePageViewBean;
import globaz.cygnus.vb.attestations.RFAttestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFAttestationPiedDePageAction extends RFDefaultAction {

    public RFAttestationPiedDePageAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_ATTESTATION_PIED_DE_PAGE
                + ".reAfficher";
        return _destination;
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // on vide le viewBean
        String _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_ATTESTATION_JOINT_TIERS
                + ".chercher";

        return _destination;

    }

    @Override
    protected String _getDestEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        String _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_ATTESTATION_PIED_DE_PAGE
                + ".reAfficher";
        return _destination;
    }

    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_ATTESTATION_PIED_DE_PAGE
                + ".reAfficher";
        return _destination;
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_ATTESTATION_JOINT_TIERS
                + ".chercher";
        return _destination;
    }

    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_ATTESTATION_PIED_DE_PAGE
                + ".reAfficher";
        return _destination;
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_ATTESTATION_JOINT_TIERS
                + ".chercher";
        return _destination;
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        try {

            RFAttestationPiedDePageViewBean outputViewBean = new RFAttestationPiedDePageViewBean();
            outputViewBean.setIdDossier(((RFAttestationViewBean) this.loadViewBean(session)).getIdDossier());
            globaz.globall.http.JSPUtils.setBeanProperties(request, outputViewBean);
            return outputViewBean;

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }

        return super.beforeNouveau(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }

        return super.beforeSupprimer(session, request, response, viewBean);
    }

    @Override
    protected String getRelativeURL(HttpServletRequest request, HttpSession session) {

        String cygnusRelativeUrl = "/cygnusRoot/attestations/attestation";
        String _destination = null;
        String codeAttestation = null;
        FWViewBeanInterface sessionVb = this.loadViewBean(session);
        // récupére le code système correspondant au type d'attestation
        // source d'exception
        codeAttestation = ((RFAttestationViewBean) sessionVb).getCsTypeAttestation();

        if (!FWViewBeanInterface.ERROR.equals(sessionVb.getMsgType())) {
            if (codeAttestation.equals(IRFAttestations.REGIME_ALIMENTAIRE)) {
                _destination = cygnusRelativeUrl + "RegimeAli2";

            } else if (codeAttestation.equals(IRFAttestations.FRAIS_LIVRAISON)) {
                _destination = cygnusRelativeUrl + "FraisLivraison9RepriseLit10";

            } else if (codeAttestation.equals(IRFAttestations.BON_MOYENS_AUXILIAIRES)) {
                _destination = cygnusRelativeUrl + "BonMoyensAux11";
            } else if (codeAttestation.equals(IRFAttestations.CERTIFICAT_MOYENS_AUXILIAIRES)) {
                _destination = cygnusRelativeUrl + "CertificatMoyensAux3";
            } else if (codeAttestation.equals(IRFAttestations.DECISION_MOYENS_AUXILIAIRES)) {
                _destination = cygnusRelativeUrl + "DecisionMoyensAux5";
            } else if (codeAttestation.equals(IRFAttestations.MAINTIEN_DOMICILE)) {
                _destination = cygnusRelativeUrl + "MaintienDomicile13";
            } else if (codeAttestation.equals(IRFAttestations.ATTESTATION_AVS)) {
                _destination = cygnusRelativeUrl + "AVS";
            } else {
                _destination = cygnusRelativeUrl + "Defaut";
            }
        } else {
            // si on vient de attestation
            if ("1".equals(request.getParameter("fromAttestation"))) {
                _destination = cygnusRelativeUrl;
            } else {
                if (codeAttestation.equals(IRFAttestations.REGIME_ALIMENTAIRE)) {
                    _destination = cygnusRelativeUrl + "RegimeAli2";

                } else if (codeAttestation.equals(IRFAttestations.FRAIS_LIVRAISON)) {
                    _destination = cygnusRelativeUrl + "FraisLivraison9RepriseLit10";

                } else if (codeAttestation.equals(IRFAttestations.BON_MOYENS_AUXILIAIRES)) {
                    _destination = cygnusRelativeUrl + "BonMoyensAux11";
                } else if (codeAttestation.equals(IRFAttestations.CERTIFICAT_MOYENS_AUXILIAIRES)) {
                    _destination = cygnusRelativeUrl + "CertificatMoyensAux3";
                } else if (codeAttestation.equals(IRFAttestations.DECISION_MOYENS_AUXILIAIRES)) {
                    _destination = cygnusRelativeUrl + "DecisionMoyensAux5";
                } else if (codeAttestation.equals(IRFAttestations.MAINTIEN_DOMICILE)) {
                    _destination = cygnusRelativeUrl + "MaintienDomicile13";
                } else if (codeAttestation.equals(IRFAttestations.ATTESTATION_AVS)) {
                    _destination = cygnusRelativeUrl + "AVS";
                } else {
                    _destination = cygnusRelativeUrl + "Defaut";
                }
            }

        }

        return _destination;
    }
}
