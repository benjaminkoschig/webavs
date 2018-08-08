/*
 * Créé le 8 févr. 07
 */
package globaz.corvus.servlet;

import globaz.corvus.api.recap.IRERecapMensuelle;
import globaz.corvus.vb.recap.REChargerRecapMensuelleViewBean;
import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.corvus.vb.recap.REVisuRecapMensuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.jsp.util.GlobazJSPBeanUtil;
import globaz.prestation.servlet.PRDefaultAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author hpe
 */
public class REDetailRecapMensuelleAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REDetailRecapMensuelleAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if ("detailRecapMensuelle".equals(getAction().getClassPart())) {
            return "/corvus?userAction=" + IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + ".chercher";
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

    public void afficherAi(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        String _destination = "";

        // On mémorise les champs contenant les totaux des prestations
        ((REDetailRecapMensuelleViewBean) viewBean).setTo2_500(request.getParameter("to2_500"));
        ((REDetailRecapMensuelleViewBean) viewBean).setTo2_501(request.getParameter("to2_501"));
        ((REDetailRecapMensuelleViewBean) viewBean).setTo2_503(request.getParameter("to2_503"));

        ((REDetailRecapMensuelleViewBean) viewBean).getElem500099().setMontant(
                request.getParameter("elem500099.montant"));
        ((REDetailRecapMensuelleViewBean) viewBean).getElem501099().setMontant(
                request.getParameter("elem501099.montant"));
        ((REDetailRecapMensuelleViewBean) viewBean).getElem503099().setMontant(
                request.getParameter("elem503099.montant"));

        _destination = "/corvus?userAction=corvus.recap.detailRecapMensuelleAi.reAfficher&process=launched";
        goSendRedirect(_destination, request, response);

    }

    public void afficherAvs(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        String _destination = "";
        _destination = "/corvus?userAction=corvus.recap.detailRecapMensuelleAvs.reAfficher&process=launched";
        goSendRedirect(_destination, request, response);

    }

    public void chargerAvs(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        boolean forward = false;
        String _destination = "";
        try {
            // Défini les droits pour notre action custom
            REDetailRecapMensuelleViewBean vb = new REDetailRecapMensuelleViewBean();

            if (viewBean instanceof REChargerRecapMensuelleViewBean) {
                vb.setIdRecapMensuelle(((REChargerRecapMensuelleViewBean) viewBean).getIdRecapMensuelle());
                vb.setDateRapportMensuel(((REChargerRecapMensuelleViewBean) viewBean).getDateRapport());
            } else {
                vb.setIdRecapMensuelle(((REVisuRecapMensuelleViewBean) viewBean).getIdRecapMensuelle());
                vb.setDateRapportMensuel(((REVisuRecapMensuelleViewBean) viewBean).getDateRapport());
            }

            GlobazJSPBeanUtil.populate(request, vb);

            // Défini les properties contenues dedans
            GlobazJSPBeanUtil.populate(request, viewBean);

            // Effectue le traitement de l'action custom
            // FWAction actionPart = getAction();
            // actionPart.changeActionPart("corvus.recap.detailRecapMensuelleAvs.charger");

            viewBean = mainDispatcher.dispatch(vb, getAction());
            // Mets dans la session le viewbean
            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination, les méthodes appelées ci-dessous peuvent être surchargées selon le besoin...
             */
            if (viewBean instanceof REChargerRecapMensuelleViewBean) {
                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    forward = true;
                    viewBean = new REChargerRecapMensuelleViewBean();
                    viewBean.setISession(viewBean.getISession());
                    ((REChargerRecapMensuelleViewBean) viewBean)
                            .setDateRapport(((REDetailRecapMensuelleViewBean) viewBean).getDateRapport());
                    _destination = "/corvus?userAction=corvus.recap.chargerRecapMensuelle.afficher&process=launched";
                } else {
                    _destination = _getDestExecuterSucces(session, request, response, viewBean);
                }
            } else {
                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    forward = true;
                    viewBean = new REVisuRecapMensuelleViewBean();
                    viewBean.setISession(viewBean.getISession());
                    ((REVisuRecapMensuelleViewBean) viewBean)
                            .setDateRapport(((REDetailRecapMensuelleViewBean) viewBean).getDateRapport());
                    _destination = "/corvus?userAction=corvus.recap.visuRecapMensuelle.afficher&process=launched";
                } else {
                    _destination = "/corvus?userAction=corvus.recap.detailRecapMensuelleAvs.reAfficher&process=launched";
                }
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        if (forward) {
            // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
            // response);
            goSendRedirect(_destination, request, response);
        } else {
            goSendRedirect(_destination, request, response);
        }

    }

    public void modifierRecapAi(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        String _destination = "";

        // Vérification de l'état de la recap. pour le mois selectionné
        try {
            // Récupération du viewBean
            REDetailRecapMensuelleViewBean vb = (REDetailRecapMensuelleViewBean) viewBean;

            if (IRERecapMensuelle.CS_ETAT_ATTENTE.equals(vb.getCsEtat())) {

                // Effectue le traitement de l'action custom
                mainDispatcher.dispatch(vb, getAction());

                // Redirection vers la destination

                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == false) {
                    // _destination =
                    _destination = "/corvus?userAction=corvus.recap.detailRecapMensuelleAi.reAfficher&process=launched";

                } else {
                    _destination = "/corvus?userAction=corvus.recap.detailRecapMensuelleAi.reAfficher";
                }

            } else {

                vb.setMsgType(FWViewBeanInterface.ERROR);
                vb.setMessage(((BSession) mainDispatcher.getSession()).getLabel("ERREUR_RECAP_DEJA_ENVOYE"));
                _destination = "/corvus?userAction=corvus.recap.detailRecapMensuelleAi.reAfficher&process=launched";

            }

            goSendRedirect(_destination, request, response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifierRecapAvs(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        String _destination = "";

        // Vérification de l'état de la recap. pour le mois selectionné
        try {
            // Récupération du viewBean
            REDetailRecapMensuelleViewBean vb = (REDetailRecapMensuelleViewBean) viewBean;

            if (IRERecapMensuelle.CS_ETAT_ATTENTE.equals(vb.getCsEtat())) {

                // Effectue le traitement de l'action custom
                mainDispatcher.dispatch(vb, getAction());

                // Redirection vers la destination

                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == false) {
                    // _destination =
                    _destination = "/corvus?userAction=corvus.recap.detailRecapMensuelleAvs.reAfficher&process=launched";

                } else {
                    _destination = "/corvus?userAction=corvus.recap.detailRecapMensuelleAvs.reAfficher";
                }

            } else {
                vb.setMsgType(FWViewBeanInterface.ERROR);
                vb.setMessage(((BSession) mainDispatcher.getSession()).getLabel("ERREUR_RECAP_DEJA_ENVOYE"));
                _destination = "/corvus?userAction=corvus.recap.detailRecapMensuelleAvs.reAfficher&process=launched";
            }

            goSendRedirect(_destination, request, response);

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
    }

}
