/*
 * Créé le 20 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.vb.droits.APDroitDTO;
import globaz.apg.vb.prestation.APRepartitionJointPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.prestation.servlet.PRDefaultAction;
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
public class APCotisationAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APAssuranceAction.
     * 
     * @param servlet
     */
    public APCotisationAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return super._getDestModifierSucces(session, request, response,
        // viewBean)+"&selectedId="+((APCotisationJointRepartitionViewBean)viewBean).getIdRepartitionBeneficiairePaiement();
        saveNewViewBean(session, viewBean);
        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // return getRelativeURL(request, session) + VERS_ECRAN_DE + VALID_NEW;
        saveNewViewBean(session, viewBean);
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        APRepartitionJointPrestationViewBean viewBean = new APRepartitionJointPrestationViewBean();
        FWAction action = FWAction.newInstance(getUserAction(request));
        String destination = ERROR_PAGE;

        try {
            viewBean.setIdRepartitionBeneficiairePaiement(getSelectedId(request));
            viewBean.setGenreService(request.getParameter("genreService"));
            action.changeActionPart("chargerRepartition");
            mainDispatcher.dispatch(viewBean, action);
            viewBean.setDroitDTO(new APDroitDTO(viewBean.loadDroit()));
            saveViewBean(viewBean, request);
            destination = getRelativeURL(request, session) + VERS_ECRAN_RC;
        } catch (Exception e) {
            e.printStackTrace();
        }

        forward(destination, request, response);
    }

    private void saveNewViewBean(HttpSession session, FWViewBeanInterface viewBean) {

        saveViewBean(viewBean, session);

    }

}
