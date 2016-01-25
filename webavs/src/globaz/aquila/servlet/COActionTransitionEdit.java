package globaz.aquila.servlet;

import globaz.aquila.db.batch.COTransitionEditViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COActionTransitionEdit extends CODefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Creates a new COActionTransitionEdit object.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public COActionTransitionEdit(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * redirige vers la page d'�dition de l'�tape depuis laquelle l'action ajout a �t� appell�e.
     * 
     * @param session
     * @param request
     * @param response
     * @param viewBean
     * @return
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getDestinationRetour(request, (COTransitionEditViewBean) viewBean);
    }

    /**
     * redirige vers la page d'�dition de l'�tape depuis laquelle l'action ajout a �t� appell�e.
     * 
     * @param session
     * @param request
     * @param response
     * @param viewBean
     * @return
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getDestinationRetour(request, (COTransitionEditViewBean) viewBean);
    }

    /**
     * redirige vers la page d'�dition de l'�tape depuis laquelle l'action ajout a �t� appell�e.
     * 
     * @param session
     * @param request
     * @param response
     * @param viewBean
     * @return
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getDestinationRetour(request, (COTransitionEditViewBean) viewBean);
    }

    /**
     * D�termine quel id a �t� transmis en requ�te pour savoir sur quelle page revenir apr�s l'action.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        setIdEtapeRetour(request, (COTransitionEditViewBean) viewBean);

        return super.beforeAfficher(session, request, response, viewBean);
    }

    /**
     * D�termine quel id a �t� transmis en requ�te pour savoir sur quelle page revenir apr�s l'action.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        setIdEtapeRetour(request, (COTransitionEditViewBean) viewBean);

        return super.beforeNouveau(session, request, response, viewBean);
    }

    /**
     * D�termine quel id a �t� transmis en requ�te pour savoir sur quelle page revenir apr�s l'action.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        /*
         * si l'action supprimer a �t� appell�e depuis l'�cran d'�tape (GCO0021) on a un mauvais viewBean, on le
         * remplace
         */
        if (!(viewBean instanceof COTransitionEditViewBean)) {
            viewBean = new COTransitionEditViewBean();
        }

        setIdEtapeRetour(request, (COTransitionEditViewBean) viewBean);

        return super.beforeSupprimer(session, request, response, viewBean);
    }

    private String getDestinationRetour(HttpServletRequest request, COTransitionEditViewBean transitionEditViewBean) {
        return "/aquila?userAction=aquila.batch.etape.afficher&_method=upd&selectedId="
                + transitionEditViewBean.getIdEtapeRetour();
    }

    private void setIdEtapeRetour(HttpServletRequest request, COTransitionEditViewBean transitionEditViewBean) {
        try {
            JSPUtils.setBeanProperties(request, transitionEditViewBean);
        } catch (Exception e) {
            transitionEditViewBean.setMessage(e.getMessage());
            transitionEditViewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        if (!StringUtils.isBlank(transitionEditViewBean.getIdEtape())) {
            transitionEditViewBean.setIdEtapeRetour(transitionEditViewBean.getIdEtape());
            transitionEditViewBean.setVersEtapeRetour(false);
        } else if (!StringUtils.isBlank(transitionEditViewBean.getIdEtapeSuivante())) {
            transitionEditViewBean.setIdEtapeRetour(transitionEditViewBean.getIdEtapeSuivante());
            transitionEditViewBean.setVersEtapeRetour(true);
        }
    }
}
