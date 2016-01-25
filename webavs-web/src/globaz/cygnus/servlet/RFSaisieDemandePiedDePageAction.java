package globaz.cygnus.servlet;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.demandes.RFSaisieDemandeAbstractViewBean;
import globaz.cygnus.vb.demandes.RFSaisieDemandeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author jje
 */
public class RFSaisieDemandePiedDePageAction extends RFDefaultAction {

    public RFSaisieDemandePiedDePageAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String _destination = "";

        if (viewBean.getMsgType().equals(FWViewBeanInterface.WARNING) == false) {

            if (RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_FIN_DE_SAISIE
                    .equals(((RFSaisieDemandeAbstractViewBean) viewBean).getTypeValidation())) {

                _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS
                        + ".chercher";

            } else if (RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_ASSURE
                    .equals(((RFSaisieDemandeAbstractViewBean) viewBean).getTypeValidation())) {

                _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_SAISIE_DEMANDE
                        + ".afficher&_method=add&isretro=" + ((RFSaisieDemandeAbstractViewBean) viewBean).getIsRetro();

            } else if (RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_FACTURE
                    .equals(((RFSaisieDemandeAbstractViewBean) viewBean).getTypeValidation())) {

                _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE
                        + ".afficher&_method=add&codeTypeDeSoinList="
                        + ((RFSaisieDemandeAbstractViewBean) viewBean).getCodeTypeDeSoinList()
                        + "&codeSousTypeDeSoinList="
                        + ((RFSaisieDemandeAbstractViewBean) viewBean).getCodeSousTypeDeSoinList();

            } else if (RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_TYPE
                    .equals(((RFSaisieDemandeAbstractViewBean) viewBean).getTypeValidation())) {

                _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE
                        + ".afficher&_method=add&codeTypeDeSoinList="
                        + ((RFSaisieDemandeAbstractViewBean) viewBean).getCodeTypeDeSoinList()
                        + "&codeSousTypeDeSoinList="
                        + ((RFSaisieDemandeAbstractViewBean) viewBean).getCodeSousTypeDeSoinList();

            } else if (RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_NOUVELLE_SAISIE
                    .equals(((RFSaisieDemandeAbstractViewBean) viewBean).getTypeValidation())) {

                _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_SAISIE_DEMANDE
                        + ".afficher&_method=add";

            }
        } else {
            _destination = _getDestAjouterEchec(session, request, response, viewBean);

        }

        return _destination;

    }

    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            ((RFSaisieDemandeAbstractViewBean) viewBean).setIsAfficherDetail(true);
            if (RFPropertiesUtils.afficherCaseForcerPaiementDemande()) {
                ((RFSaisieDemandeAbstractViewBean) viewBean).setAfficherCaseForcerPaiement(true);
            }
            if (RFPropertiesUtils.afficherRemarqueFournisseur()) {
                ((RFSaisieDemandeAbstractViewBean) viewBean).setAfficherRemFournisseur(true);
            }
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return super._getDestModifierEchec(session, request, response, viewBean);
        }
        return super._getDestModifierEchec(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean.getMsgType().equals(FWViewBeanInterface.WARNING) == false) {
            return RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS
                    + ".chercher";
        } else {
            return _getDestModifierEchec(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        try {
            ((RFSaisieDemandeAbstractViewBean) viewBean).setIsAfficherDetail(true);
            if (RFPropertiesUtils.afficherCaseForcerPaiementDemande()) {
                ((RFSaisieDemandeAbstractViewBean) viewBean).setAfficherCaseForcerPaiement(true);
            }
            if (RFPropertiesUtils.afficherRemarqueFournisseur()) {
                ((RFSaisieDemandeAbstractViewBean) viewBean).setAfficherRemFournisseur(true);
            }
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return super._getDestSupprimerEchec(session, request, response, viewBean);
        }
        return super._getDestSupprimerEchec(session, request, response, viewBean);

    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS + ".chercher";
    }

    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionSelectionner(session, request, response, mainDispatcher);
        FWViewBeanInterface sessionVb = this.loadViewBean(session);
        addMotifsDeRefusVb((RFSaisieDemandeAbstractViewBean) sessionVb, request);
    }

    private void addMotifsDeRefusVb(RFSaisieDemandeAbstractViewBean viewBean, HttpServletRequest request) {

        (viewBean).setMontantsMotifsRefus(new HashMap<String, String[]>());
        String[] motifsRefusTab = viewBean.getListMotifsRefusInput().split(",");

        for (int i = 0; i < motifsRefusTab.length; i++) {
            if (!JadeStringUtil.isBlank(motifsRefusTab[i])) {
                String mntMotifsRefusStr = request.getParameter("champMontantMotifRefus_" + motifsRefusTab[i]);
                // if (!JadeStringUtil.isBlank(mntMotifsRefusStr)){
                (viewBean).getMontantsMotifsRefus().put(motifsRefusTab[i], new String[] { mntMotifsRefusStr, "" });
            }
        }

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        boolean isRetro = ((RFSaisieDemandeAbstractViewBean) viewBean).getIsRetro();

        try {

            FWViewBeanInterface sessionVb = this.loadViewBean(session);

            if (isRetourPyxis(sessionVb)) {

                ((RFSaisieDemandeAbstractViewBean) sessionVb).setIsAfficherDetail(new Boolean(request
                        .getParameter("isAfficherDetail")));
                ((RFSaisieDemandeAbstractViewBean) sessionVb).setAfficherCaseForcerPaiement(new Boolean(request
                        .getParameter("isAfficherCaseForcerPaiement")));
                ((RFSaisieDemandeAbstractViewBean) sessionVb).setAfficherRemFournisseur(new Boolean(request
                        .getParameter("isAfficherRemFournisseur")));
                ((RFSaisieDemandeAbstractViewBean) sessionVb).setIsRetro(isRetro);
                return sessionVb;

            } else {

                if (JadeStringUtil.isEmpty(((RFSaisieDemandeAbstractViewBean) viewBean).getTypeValidation())) {
                    globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                }

                ((RFSaisieDemandeAbstractViewBean) viewBean).setIsRetro(((RFSaisieDemandeAbstractViewBean) sessionVb)
                        .getIsRetro());
                ((RFSaisieDemandeAbstractViewBean) viewBean).setIsAfficherDetail(new Boolean(request
                        .getParameter("isAfficherDetail")));
                ((RFSaisieDemandeAbstractViewBean) viewBean).setAfficherCaseForcerPaiement(new Boolean(request
                        .getParameter("isAfficherCaseForcerPaiement")));
                ((RFSaisieDemandeAbstractViewBean) viewBean).setAfficherRemFournisseur(new Boolean(request
                        .getParameter("isAfficherRemFournisseur")));
                ((RFSaisieDemandeAbstractViewBean) viewBean).setIsRetro(isRetro);
                return viewBean;
            }

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }

    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        addMotifsDeRefusVb((RFSaisieDemandeAbstractViewBean) viewBean, request);

        try {
            if (RFPropertiesUtils.afficherCaseForcerPaiementDemande()) {
                ((RFSaisieDemandeAbstractViewBean) viewBean).setAfficherCaseForcerPaiement(true);
            }
            if (RFPropertiesUtils.afficherRemarqueFournisseur()) {
                ((RFSaisieDemandeAbstractViewBean) viewBean).setAfficherRemFournisseur(true);
            }
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
        }

        return super.beforeAjouter(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        addMotifsDeRefusVb((RFSaisieDemandeAbstractViewBean) viewBean, request);

        return super.beforeModifier(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            // outputViewBean.setAfficherForcerPaiement(viewBean.isAfficherForcerPaiement());
            FWViewBeanInterface sessionVb = this.loadViewBean(session);

            if (!isRetourPyxis(sessionVb)) {
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

                // Si l'on vient de la saisie des demandes par le boutton
                // "Valider + Saisie même assuré", on mémorise l'idTiers de l'assuré
                if (RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_TYPE
                        .equals(((RFSaisieDemandeViewBean) viewBean).getTypeValidation())) {

                    RFSaisieDemandeViewBean outputVb = new RFSaisieDemandeViewBean();

                    setOutputViewBeanMemeType(outputVb, (RFSaisieDemandeViewBean) viewBean);
                    this.saveViewBean(viewBean, session);
                    return outputVb;
                } else if (RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_FACTURE
                        .equals(((RFSaisieDemandeViewBean) viewBean).getTypeValidation())) {
                    RFSaisieDemandeViewBean outputVb = new RFSaisieDemandeViewBean();

                    setOutputViewBeanMemeType(outputVb, (RFSaisieDemandeViewBean) viewBean);
                    setOutputViewBeanMemeFacture(outputVb, (RFSaisieDemandeViewBean) viewBean);
                    this.saveViewBean(viewBean, session);
                    return outputVb;
                } else {

                    if (RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_ASSURE
                            .equals(((RFSaisieDemandeViewBean) sessionVb).getTypeValidation())) {
                        boolean isRetro = ((RFSaisieDemandeAbstractViewBean) sessionVb).getIsRetro();
                        ((RFSaisieDemandeAbstractViewBean) viewBean).setIsRetro(isRetro);
                    }

                    this.saveViewBean(viewBean, session);
                    return viewBean;
                }
            } else {
                return sessionVb;
            }

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
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

        String cygnusRelativeUrl = "/cygnusRoot/demandes/saisieDemande";
        String _destination = null;
        Integer codeTypeDeSoinInt = null;
        FWViewBeanInterface sessionVb = this.loadViewBean(session);
        codeTypeDeSoinInt = new Integer(((RFSaisieDemandeViewBean) sessionVb).getCodeTypeDeSoinList());

        if (!((RFSaisieDemandeViewBean) sessionVb).isErreurSaisieDemande()) {
            if ((codeTypeDeSoinInt.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_5)
                    || (codeTypeDeSoinInt.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_6)
                    || (codeTypeDeSoinInt.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MOYENS_AUXILIAIRES_7)) {
                _destination = cygnusRelativeUrl + "Moy5_6_7";

            } else if (codeTypeDeSoinInt.intValue() == RFUtils.CODE_TYPE_DE_SOIN_MAINTIEN_A_DOMICILE) {
                _destination = cygnusRelativeUrl + "Mai13";

            } else if ((codeTypeDeSoinInt.intValue() == RFUtils.CODE_TYPE_DE_SOIN_FRQP)
                    || (codeTypeDeSoinInt.intValue() == RFUtils.CODE_TYPE_DE_SOIN_FRAIS_REFUSES)) {
                _destination = cygnusRelativeUrl + "Frq17Fra18";

            } else if (codeTypeDeSoinInt.intValue() == RFUtils.CODE_TYPE_DE_SOIN_DEVIS_DENTAIRE) {
                _destination = cygnusRelativeUrl + "Dev19";

            } else if (codeTypeDeSoinInt.intValue() == RFUtils.CODE_TYPE_DE_SOIN_FRAIS_DENTAIRE) {
                _destination = cygnusRelativeUrl + "Ftd15";

            } else if (codeTypeDeSoinInt.intValue() == RFUtils.CODE_TYPE_DE_SOIN_REGIME_2) {
                _destination = cygnusRelativeUrl + "Reg2";

            } else if (codeTypeDeSoinInt.intValue() == RFUtils.CODE_TYPE_DE_SOIN_TRANSPORT_16) {
                _destination = cygnusRelativeUrl + "Fra16";

            } else {
                _destination = cygnusRelativeUrl + "Defaut";
            }
        } else {
            _destination = cygnusRelativeUrl;
            ((RFSaisieDemandeViewBean) sessionVb).setErreurSaisieDemande(false);
        }

        return _destination;
    }

    private boolean isRetourPyxis(FWViewBeanInterface viewBean) {
        return ((null != viewBean) && (viewBean instanceof RFSaisieDemandeAbstractViewBean) && ((RFSaisieDemandeAbstractViewBean) viewBean)
                .isRetourDepuisPyxis());

    }

    /*
     * public void majMembresFamilleDemande(HttpSession session, HttpServletRequest request, HttpServletResponse
     * response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws javax.servlet.ServletException,
     * java.io.IOException {
     * 
     * String _destination = "";
     * 
     * String action = request.getParameter("userAction"); FWAction _action = FWAction.newInstance(action);
     * 
     * try { if ((viewBean instanceof RFSaisieDemandeViewBean) && !((RFSaisieDemandeViewBean)
     * viewBean).isMembreFamilleSet()) { JSPUtils.setBeanProperties(request, viewBean); }
     * this.addMotifsDeRefusVb((RFSaisieDemandeViewBean) viewBean, request);
     * 
     * viewBean = mainDispatcher.dispatch(viewBean, _action); session.removeAttribute("viewBean");
     * session.setAttribute("viewBean", viewBean); request.setAttribute(FWServlet.VIEWBEAN, viewBean);
     * 
     * 
     * // choix destination
     * 
     * 
     * _destination = this.getRelativeURL(request, session) + "_de.jsp";
     * 
     * 
     * //redirection vers la destination
     * 
     * this.servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
     * 
     * } catch (Exception e) { RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage()); }
     * 
     * }
     */

    /**
     * Méthode qui récupère les champs de l'assuré de la dernière demande encodée
     * 
     * @param HttpSession
     *            , HttpServletRequest, HttpServletResponse, FWDispatcher,FWViewBeanInterface
     * @throws ServletException
     *             , IOException
     */
    private void setOutputViewBeanMemeAssure(RFSaisieDemandeViewBean outputViewBean, RFSaisieDemandeViewBean viewBean) {

        outputViewBean.setSession((BSession) viewBean.getISession());

        outputViewBean.setIdTiers(viewBean.getIdTiers());
        outputViewBean.setCsNationalite(viewBean.getCsNationalite());
        outputViewBean.setCsCanton(viewBean.getCsCanton());
        outputViewBean.setCsSexe(viewBean.getCsSexe());
        outputViewBean.setNss(viewBean.getNss());
        outputViewBean.setNom(viewBean.getNom());
        outputViewBean.setPrenom(viewBean.getPrenom());
        outputViewBean.setDateNaissance(viewBean.getDateNaissance());
        outputViewBean.setDateDeces(viewBean.getDateDeces());
        outputViewBean.setTypeValidation(viewBean.getTypeValidation());
        outputViewBean.setIdGestionnaire(viewBean.getIdGestionnaire());
        outputViewBean.setIsRetro(viewBean.getIsRetro());

    }

    /**
     * Méthode qui récupère le numéro et la date de la dernière demande encodée
     * 
     * @param HttpSession
     *            , HttpServletRequest, HttpServletResponse, FWDispatcher,FWViewBeanInterface
     * @throws ServletException
     *             , IOException
     */
    private void setOutputViewBeanMemeFacture(RFSaisieDemandeViewBean outputViewBean, RFSaisieDemandeViewBean viewBean) {

        if (!JadeStringUtil.isBlankOrZero(viewBean.getDateFacture())) {
            outputViewBean.setDateFacture(viewBean.getDateFacture());
        } else {
            if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDecompte())) {
                outputViewBean.setDateDecompte(viewBean.getDateDecompte());
            } else {
                if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDecisionOAI())) {
                    outputViewBean.setDateDecisionOAI(viewBean.getDateDecisionOAI());
                }
            }
        }

        if (!JadeStringUtil.isBlankOrZero(viewBean.getNumeroFacture())) {
            outputViewBean.setNumeroFacture(viewBean.getNumeroFacture());
        } else {
            outputViewBean.setNumeroDecompte(viewBean.getNumeroDecompte());
        }

        if (!JadeStringUtil.isBlankOrZero(viewBean.getRemarqueFournisseur())) {
            outputViewBean.setRemarqueFournisseur(viewBean.getRemarqueFournisseur());
        }

        outputViewBean.setIsRetro(viewBean.getIsRetro());

    }

    /**
     * Méthode qui récupère les champs types, date de réception et fournisseur de la dernière demande encodée
     * 
     * @param HttpSession
     *            , HttpServletRequest, HttpServletResponse, FWDispatcher,FWViewBeanInterface
     * @throws ServletException
     *             , IOException
     */
    private void setOutputViewBeanMemeType(RFSaisieDemandeViewBean outputViewBean, RFSaisieDemandeViewBean viewBean) {

        setOutputViewBeanMemeAssure(outputViewBean, viewBean);
        outputViewBean.setDateReception(viewBean.getDateReception());
        outputViewBean.setCodeTypeDeSoinList(viewBean.getCodeTypeDeSoinList());
        outputViewBean.setCodeSousTypeDeSoinList(viewBean.getCodeSousTypeDeSoinList());
        outputViewBean.setIdFournisseurDemande(viewBean.getIdFournisseurDemande());
        outputViewBean.setIsRetro(viewBean.getIsRetro());

    }

}
