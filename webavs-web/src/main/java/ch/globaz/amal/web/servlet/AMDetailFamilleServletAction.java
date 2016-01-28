/**
 * 
 */
package ch.globaz.amal.web.servlet;

import globaz.amal.vb.contribuable.AMContribuableViewBean;
import globaz.amal.vb.detailfamille.AMDetailfamilleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.op.common.model.document.Document;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;

/**
 * @author DHI
 * 
 */
public class AMDetailFamilleServletAction extends AMAbstractServletAction {

    /**
     * Default constructor
     * 
     * @param aServlet
     */
    public AMDetailFamilleServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterEchec(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AMDetailfamilleViewBean detailFamilleViewBean = (AMDetailfamilleViewBean) viewBean;
        String destination = super._getDestAjouterEchec(session, request, response, viewBean);
        destination += "&_method=add";
        destination += "&contribuableId=" + detailFamilleViewBean.getContribuable().getId();
        destination += "&membreFamilleId=" + detailFamilleViewBean.getFamilleContribuable().getSimpleFamille().getId();
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AMDetailfamilleViewBean detailFamilleViewBean = (AMDetailfamilleViewBean) viewBean;
        // String destination = "/amal?userAction=amal.famille.famille.afficher&selectedId=";
        // destination += detailFamilleViewBean.getFamilleContribuable().getId();
        String destination = "/amal?userAction=amal.detailfamille.detailfamille.afficher&selectedId=";
        destination += detailFamilleViewBean.getId();
        destination += "&contribuableId=" + detailFamilleViewBean.getContribuable().getId();
        destination += "&membreFamilleId=" + detailFamilleViewBean.getFamilleContribuable().getSimpleFamille().getId();
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierEchec(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String destination = super._getDestModifierEchec(session, request, response, viewBean);
        AMDetailfamilleViewBean detailFamilleViewBean = (AMDetailfamilleViewBean) viewBean;
        destination += "&selectedId=" + detailFamilleViewBean.getDetailFamille().getId();
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces (HttpSession
     * session,HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AMDetailfamilleViewBean detailFamilleViewBean = (AMDetailfamilleViewBean) viewBean;
        String destination = "/amal?userAction=amal.famille.famille.afficher&selectedId=";
        destination += detailFamilleViewBean.getFamilleContribuable().getId();
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerEchec(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        session.setAttribute("msgType", viewBean.getMsgType());
        session.setAttribute("message", viewBean.getMessage());
        String destination = "";
        AMDetailfamilleViewBean detailViewBean = (AMDetailfamilleViewBean) viewBean;
        destination = "/amal?userAction=amal.famille.famille.reAfficher";
        // destination = "/amal?userAction=amal.famille.famille.reAfficher&selectedId=";
        // destination += detailViewBean.getDetailFamille().getIdFamille();
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = "";
        AMDetailfamilleViewBean detailViewBean = (AMDetailfamilleViewBean) viewBean;
        destination = "/amal?userAction=amal.famille.famille.afficher&selectedId=";
        destination += detailViewBean.getDetailFamille().getIdFamille();
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String selectedId = request.getParameter("selectedId");
        if (!JadeStringUtil.isEmpty(selectedId)) {
            AMDetailfamilleViewBean detailFamilleViewBean = new AMDetailfamilleViewBean();
            detailFamilleViewBean.setId(selectedId);
            return detailFamilleViewBean;
        } else {
            return null;
        }
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @return
     */
    public String createInteractivDocument(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        // --------------------------------------------------------------------
        // Récupération des paramètres intéressants : modele Id et modele type
        // --------------------------------------------------------------------
        String modeleId = request.getParameter("modeleId");
        String modeleType = request.getParameter("modeleType");
        AMDetailfamilleViewBean detailFamilleViewBean = (AMDetailfamilleViewBean) viewBean;

        // --------------------------------------------------------------------
        // Récupération de la date du jour
        // --------------------------------------------------------------------
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss");
        String csDateComplete = sdf.format(date);
        String destination = "";

        // --------------------------------------------------------------------
        // Traitement de la demande
        // --------------------------------------------------------------------
        try {
            // --------------------------------------------------------------------
            // Cas d'un modèle interactif >> création word ML
            // --------------------------------------------------------------------
            if (modeleType.equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())) {

                // --------------------------------------------------------------------
                // Code pour une réponse directe dans le flux
                // --------------------------------------------------------------------
                // response.setContentType("application/word");
                // response.setHeader("Content-Disposition", "attachment; filename=\"test.doc");
                // doc.write(response.getOutputStream());

                String filePath = "";
                Document doc = (Document) detailFamilleViewBean.merge(modeleId);
                if (doc != null) {
                    // -----------------------------------------------------------
                    // Sauvegarde du document (persistence - ged dir)
                    // -----------------------------------------------------------
                    String fileName = detailFamilleViewBean.getInteractivDocumentFileName(csDateComplete, modeleId);
                    filePath = detailFamilleViewBean.writeInteractivDocument(fileName, doc);
                    // ------------------------------------------------------------
                    // Si ok, enregistrement de l'opération dans les tables de jobs
                    // ------------------------------------------------------------
                    if (!"".equals(filePath) && (filePath.indexOf("persistence") < 0)) {
                        detailFamilleViewBean.writeInJobTable(csDateComplete, modeleId, modeleType);
                    }
                }
                // ------------------------------------------------------------------------
                // Enregistrement du path dans la réponse >> traitement Ajax JQuery de base
                // ------------------------------------------------------------------------
                response.getWriter().write(filePath);
                return "";
            } else {
                // --------------------------------------------------------------------
                // Cas d'un modèle batch >> création Topaz
                // --------------------------------------------------------------------
                destination = "/amal?userAction=amal.detailfamille.detailfamille.afficher&selectedId=";
                destination += detailFamilleViewBean.getDetailFamille().getIdDetailFamille();
                detailFamilleViewBean.writeInJobTable(csDateComplete, modeleId, modeleType);
                return destination;
            }
        } catch (Exception e) {
            // --------------------------------------------------------------------
            // Erreur
            // --------------------------------------------------------------------
            JadeLogger.error("Failed to export file", e);
            JadeThread.logError("Error", "amal.revenu.simpleRevenu.integrity.SameRevenuExistAndIsActif");
            destination = "/amal?userAction=amal.detailfamille.detailfamille.reafficher&selectedId=";
            destination += detailFamilleViewBean.getDetailFamille().getIdDetailFamille();
            return destination;
        }
    }

    /**
     * action de génération du(des) subside(s) depuis la fenêtre de détails du calcul
     * 
     * a) Depuis détail famille b) Depuis contribuable
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String generateSubside(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        String destination = "";

        try {
            if (viewBean instanceof AMDetailfamilleViewBean) {
                AMDetailfamilleViewBean detailFamilleViewBean = (AMDetailfamilleViewBean) viewBean;
                detailFamilleViewBean.getCalculs().parseAllSubsidesAsStringAndGenerateSubsides();
                detailFamilleViewBean.generateSubside();
                destination = "/amal?userAction=amal.detailfamille.detailfamille.afficher&selectedId=";
                destination += detailFamilleViewBean.getDetailFamille().getIdDetailFamille();
            } else if (viewBean instanceof AMContribuableViewBean) {
                AMContribuableViewBean contribuableViewBean = (AMContribuableViewBean) viewBean;
                contribuableViewBean.getCalculs().parseAllSubsidesAsStringAndGenerateSubsides();
                contribuableViewBean.generateSubside();
                destination = "/amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
                destination += contribuableViewBean.getContribuable().getContribuable().getIdContribuable();
            } else {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        return destination;
    }
}
