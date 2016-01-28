/*
 * Cr�� le 27 sept. 05
 */
package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.vb.prononces.IJPetiteIJJointRevenuViewBean;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJPetiteIJJointRevenuAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe IJPetiteIJJointRevenuAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJPetiteIJJointRevenuAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * redirige vers la page de calcul des ij avec globaz.ij.acor.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        IJPetiteIJJointRevenuViewBean ijviewBean = (IJPetiteIJJointRevenuViewBean) viewBean;

        return getUserActionURL(request, IIJActions.ACTION_CALCUL_IJ,
                ".afficher&idPrononce=" + ijviewBean.getIdPrononce() + "&csTypeIJ=" + IIJPrononce.CS_PETITE_IJ);
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
        // TODO Raccord de m�thode auto-g�n�r�
        super.actionAfficher(session, request, response, mainDispatcher);
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
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String arreterEtape5(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        ((IJPetiteIJJointRevenuViewBean) viewBean).wantCallValidate(false);

        try {
            getAction().changeActionPart(FWAction.ACTION_MODIFIER);
        } catch (Exception e) {
            return ERROR_PAGE;
        }

        mainDispatcher.dispatch(viewBean, getAction());

        return getUserActionURL(request, IIJActions.ACTION_PRONONCE_JOINT_DEMANDE, FWAction.ACTION_CHERCHER);
    }

}
