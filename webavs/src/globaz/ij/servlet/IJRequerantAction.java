package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.ISFUrlEncode;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.ij.vb.prononces.IJRequerantViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author DVH
 */
public class IJRequerantAction extends PRDefaultAction {

    public IJRequerantAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        IJRequerantViewBean requerantViewBean = (IJRequerantViewBean) viewBean;
        String csTypeIJ = request.getParameter("csTypeIJ");
        try {
            String idTiers = requerantViewBean.loadDemande(null).loadTiers()
                    .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

            // on donne la bonne action en fonction du type d'IJ
            String action = "";
            if (IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ) || IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)) {
                action = IIJActions.ACTION_SAISIE_PRONONCE;
            } else if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)) {
                action = IIJActions.ACTION_SAISIE_PRONONCE_AIT;
            } else if (IIJPrononce.CS_ALLOC_ASSIST.equals(csTypeIJ)) {
                action = IIJActions.ACTION_SAISIE_PRONONCE_ALLOC_ASSIST;
            } else {
                throw new Exception("Type d'IJ inconnu");
            }

            String urlRetour = ISFUrlEncode.encodeUrl("/ij?userAction=" + action + "." + FWAction.ACTION_AFFICHER
                    + "&selectedId=" + requerantViewBean.getIdPrononce() + "&noAVS=" + requerantViewBean.getNss()
                    + "&prenomNom=" + requerantViewBean.getNom() + " " + requerantViewBean.getPrenom() + "&csTypeIJ="
                    + csTypeIJ + "&detailRequerant=" + requerantViewBean.getDetailRequerantDetail());

            return "/hera?userAction=hera.famille.apercuRelationFamilialeRequerant.entrerApplication&csDomaine="
                    + ISFSituationFamiliale.CS_DOMAINE_INDEMNITEE_JOURNALIERE + "&idTiers=" + idTiers + "&urlFrom="
                    + urlRetour;
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        IJRequerantViewBean requerantViewBean = (IJRequerantViewBean) viewBean;
        String csTypeIJ = request.getParameter("csTypeIJ");
        try {
            String idTiers = requerantViewBean.loadDemande(null).loadTiers()
                    .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

            // on donne la bonne action en fonction du type d'IJ
            String action = "";
            if (IIJPrononce.CS_PETITE_IJ.equals(csTypeIJ) || IIJPrononce.CS_GRANDE_IJ.equals(csTypeIJ)) {
                action = IIJActions.ACTION_SAISIE_PRONONCE;
            } else if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(csTypeIJ)) {
                action = IIJActions.ACTION_SAISIE_PRONONCE_AIT;
            } else if (IIJPrononce.CS_ALLOC_ASSIST.equals(csTypeIJ)) {
                action = IIJActions.ACTION_SAISIE_PRONONCE_ALLOC_ASSIST;
            } else {
                throw new Exception("Type d'IJ inconnu");
            }

            String urlRetour = ISFUrlEncode.encodeUrl("/ij?userAction=" + action + "." + FWAction.ACTION_AFFICHER
                    + "&selectedId=" + requerantViewBean.getIdPrononce() + "&noAVS=" + requerantViewBean.getNss()
                    + "&prenomNom=" + requerantViewBean.getNom() + " " + requerantViewBean.getPrenom() + "&csTypeIJ="
                    + csTypeIJ + "&detailRequerant=" + requerantViewBean.getDetailRequerantDetail());

            return "/hera?userAction=hera.famille.apercuRelationFamilialeRequerant.entrerApplication&csDomaine="
                    + ISFSituationFamiliale.CS_DOMAINE_INDEMNITEE_JOURNALIERE + "&idTiers=" + idTiers + "&urlFrom="
                    + urlRetour;
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

    public void actionEcranSuivant(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws ServletException, IOException {
        FWViewBeanInterface viewBean = this.loadViewBean(session);
        String destination = _getDestModifierSucces(session, request, response, viewBean);
        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAfficher(session, request, response, mainDispatcher);
        FWViewBeanInterface viewBean = this.loadViewBean(session);

        IJNSSDTO dto = new IJNSSDTO();
        dto.setNSS(((IJRequerantViewBean) viewBean).getNss());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

    }

    /**
     * Action intermédiaire exécutée lors de la sortie du champs AVS, recherche un tiers à partir du numéro et recharge
     * la page.
     * 
     * @return retourne vers l'écran de saisie numéro 1
     * @throws ServletException
     *             si la personne au numéro AVS ne peut être trouvée ou la demande associée cause une erreur
     */
    public String actionFindIdTiersByNoAvs(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        mainDispatcher.dispatch(viewBean, getAction());

        String destination = getRelativeURL(request, session) + "_de.jsp?";

        if (JadeStringUtil.isIntegerEmpty(((IJRequerantViewBean) viewBean).getIdPrononce())) {
            destination = destination + PRDefaultAction.METHOD_ADD;
        } else {
            destination = destination + PRDefaultAction.METHOD_UPD;
        }

        return destination;
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        FWViewBeanInterface viewBean = this.loadViewBean(session);

        if (mainDispatcher.getSession().hasRight(IIJActions.ACTION_PRONONCE, FWSecureConstants.UPDATE)) {

            super.actionModifier(session, request, response, mainDispatcher);

        } else {
            goSendRedirect(_getDestModifierSucces(session, request, response, viewBean), request, response);
        }
    }

    public String arreterEtape1(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        IJRequerantViewBean requerantViewBean = (IJRequerantViewBean) viewBean;
        if (!IIJPrononce.CS_COMMUNIQUE.equals(requerantViewBean.getCsEtat())) {
            mainDispatcher.dispatch(viewBean, getAction());
        }

        return this.getUserActionURL(request, IIJActions.ACTION_PRONONCE_JOINT_DEMANDE, FWAction.ACTION_CHERCHER);
    }

    /**
     * en affichant le requerant, il a forcément été créé. Il se peut que l'Id n'ait pas été retrouvé dans le
     * comportement par défaut (il peut se trouver dans idPrononce ou dans selectedId)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        IJRequerantViewBean requerantViewBean = (IJRequerantViewBean) viewBean;

        if (JadeStringUtil.isIntegerEmpty(requerantViewBean.getIdPrononce())) {
            requerantViewBean.setIdPrononce(request.getParameter("idPrononce"));
        }

        return requerantViewBean;
    }
}
