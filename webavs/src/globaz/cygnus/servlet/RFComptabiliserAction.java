/*
 * Créé le 30 novembre 2009
 */
package globaz.cygnus.servlet;

import globaz.cygnus.vb.process.RFComptabiliserViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jje
 */
public class RFComptabiliserAction extends RFDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RFComptabiliserAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return getActionFullURL() + ".reAfficher" + "&dateEcheancePaiement="
                + ((RFComptabiliserViewBean) viewBean).getDateEcheancePaiement() + "&idOrganeExecution="
                + ((RFComptabiliserViewBean) viewBean).getIdOrganeExecution() + "&lancerValidation="
                + ((RFComptabiliserViewBean) viewBean).getTypeValidation() + "&numeroOG="
                + ((RFComptabiliserViewBean) viewBean).getNumeroOG();
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

}
