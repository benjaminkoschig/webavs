/*
 * Créé le 5 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.exceptions.APWrongViewBeanTypeException;
import globaz.apg.vb.droits.APDroitAPGPViewBean;
import globaz.apg.vb.droits.APDroitDTO;
import globaz.apg.vb.droits.APDroitMatPViewBean;
import globaz.apg.vb.prestation.APValidationPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APDroitMatPAction extends APAbstractDroitPAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APDroitMatPAction.
     * 
     * @param servlet
     */
    public APDroitMatPAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        APDroitDTO dto = new APDroitDTO(((APDroitMatPViewBean) viewBean).getDroit());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        return this.getUserActionURL(request, IAPActions.ACTION_ENFANT_MAT, FWAction.ACTION_CHERCHER + "&"
                + APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + dto.getIdDroit());
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        APDroitDTO dto = new APDroitDTO(((APDroitMatPViewBean) viewBean).getDroit());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        return this.getUserActionURL(request, IAPActions.ACTION_ENFANT_MAT, FWAction.ACTION_CHERCHER + "&"
                + APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + dto.getIdDroit());
    }

    /**
     * On arrive sur cette action depuis l'écran de contrôle des prestation suite à la création d'un droit.
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param vb
     * @return
     * @throws Exception
     */
    public String finaliserCreationDroit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        if (!(vb instanceof APValidationPrestationViewBean)) {
            throw new APWrongViewBeanTypeException(
                    "finaliserCreationDroit wrong viewBean received. It must be an instance of class APValidationPrestationViewBean");
        }
        APValidationPrestationViewBean viewBean = (APValidationPrestationViewBean) vb;
        try {
            APDroitAPGPViewBean actionAfficherViewBean = (APDroitAPGPViewBean) mainDispatcher.dispatch(viewBean,
                    getAction());
            return this.getUserActionURL(request, IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT,
                    "chercher&forIdDroit=" + viewBean.getIdDroit());
        } catch (Exception e) {
            this.saveViewBean(viewBean, session);
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String[] userAction = request.getParameterValues("userAction");
        if ((userAction != null) && (userAction.length > 1) && userAction[1].toLowerCase().equals("back")) {
            super.actionAfficherApresBack(session, request, response, mainDispatcher);
        } else {
            super.actionAfficher(session, request, response, mainDispatcher);
        }

        FWViewBeanInterface viewBean = this.loadViewBean(session);

        APDroitDTO dto = new APDroitDTO();
        dto.setDateDebutDroit(((APDroitMatPViewBean) viewBean).getDateDebutDroit());
        dto.setGenreService(((APDroitMatPViewBean) viewBean).getGenreService());
        dto.setIdDroit(((APDroitMatPViewBean) viewBean).getIdDroit());
        dto.setNoAVS(((APDroitMatPViewBean) viewBean).getNss());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            ((APDroitMaternite) ((APDroitMatPViewBean) viewBean).getDroit()).calculerDateFinDroit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            ((APDroitMaternite) ((APDroitMatPViewBean) viewBean).getDroit()).calculerDateFinDroit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }

}
