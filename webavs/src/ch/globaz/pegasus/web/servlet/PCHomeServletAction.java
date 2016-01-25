package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.serialization.JavaObjectSerializer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.vb.home.PCHomeViewBean;
import globaz.pegasus.vb.home.PCPeriodeAjaxViewBean;
import globaz.pegasus.vb.home.PCPrixChambreViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.SimpleHome;

public class PCHomeServletAction extends PCAbstractServletAction {

    public PCHomeServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCHomeViewBean) {
            PCHomeViewBean vb = (PCHomeViewBean) viewBean;
            StringBuffer sb = new StringBuffer(getActionFullURL());
            sb.append(".afficher").append("&selectedId=").append(vb.getId());
            return sb.toString();
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
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
        if (viewBean instanceof PCPrixChambreViewBean) {
            // on set l'idHome pour pouvoir chercher les types de chambre du
            // home
            ((PCPrixChambreViewBean) viewBean)
                    .setIdHome(JadeStringUtil.toNotNullString(request.getParameter("idHome")));

            // on set idTypeChambre pour pouvoir chercher les types de chambre
            // du
            // home
            ((PCPrixChambreViewBean) viewBean).getPrixChambre().getSimplePrixChambre()
                    .setIdTypeChambre(JadeStringUtil.toNotNullString(request.getParameter("idTypeChambre")));

            // initialisation de la liste des types de chambres pour les idHome
            // et
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
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        tryGetHome(session, request, viewBean);

        return super.beforeAjouter(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        tryGetHome(session, request, viewBean);
        return super.beforeModifier(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        tryGetHome(session, request, viewBean);
        return super.beforeSupprimer(session, request, response, viewBean);
    }

    private void tryGetHome(HttpSession session, HttpServletRequest request, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCPeriodeAjaxViewBean) {
            PCPeriodeAjaxViewBean vb = (PCPeriodeAjaxViewBean) viewBean;

            String hexaSerializedViewBean = request.getParameter("parentViewBean");

            try {
                SimpleHome simpleHome = (SimpleHome) JavaObjectSerializer.convertHexaToObject(hexaSerializedViewBean);

                vb.getPeriodeServiceEtat().setSimpleHome(simpleHome);
            } catch (Exception e) {
                JadeLogger.warn("no serialized object home was found, or it was corrupted", e);
            }
        }
    }
}
