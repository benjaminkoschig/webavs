/*
 * Créé le 20 févr. 07
 */
package globaz.corvus.servlet;

import globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.prestation.servlet.PRDefaultAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author bsc
 * 
 */
public class REPrestationsDuesJointDemandeRenteAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REPrestationsDuesJointDemandeRenteAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        REPrestationsDuesJointDemandeRenteViewBean pdViewBean = (REPrestationsDuesJointDemandeRenteViewBean) viewBean;
        pdViewBean.setIdTiersBeneficiaire(request.getParameter("idTierRequerant"));
        pdViewBean.setNoDemandeRente(request.getParameter("noDemandeRente"));
        pdViewBean.setIdRenteAccordee(request.getParameter("idRenteAccordee"));

        return pdViewBean;
    }

    // ~ Methods
    // ----------------------------------------------------------------------

}