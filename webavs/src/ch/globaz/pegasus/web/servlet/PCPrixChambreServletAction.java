package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.vb.home.PCPrixChambreViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;

public class PCPrixChambreServletAction extends PCAbstractServletAction {

    public PCPrixChambreServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // on set l'idHome pour pouvoir chercher les types de chambre du home
        ((PCPrixChambreViewBean) viewBean).setIdHome(JadeStringUtil.toNotNullString(request.getParameter("idHome")));

        // on set idTypeChambre pour pouvoir chercher les types de chambre du
        // home
        ((PCPrixChambreViewBean) viewBean).getPrixChambre().getSimplePrixChambre()
                .setIdTypeChambre(JadeStringUtil.toNotNullString(request.getParameter("idTypeChambre")));

        // initialisation de la liste des types de chambres pour les idHome et
        // idTypeChambre
        try {
            ((PCPrixChambreViewBean) viewBean).init();
        } catch (TypeChambreException e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        } catch (JadeApplicationServiceNotAvailableException e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        } catch (JadePersistenceException e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        } catch (HomeException e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
