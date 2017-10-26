package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUserGroup;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pegasus.vb.rpc.PCAnnoncesViewBean;
import globaz.pegasus.vb.rpc.PCDetailAnnonceAjaxViewBean;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;

public class PCRpcServletAction extends PCAbstractServletAction {

    private String idAnnonce = null;

    /**
     * Constructeur
     * 
     * @param aServlet
     */
    public PCRpcServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.actionAjouter(session, request, response, mainDispatcher);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.actionModifier(session, request, response, mainDispatcher);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if ((viewBean instanceof PCDetailAnnonceAjaxViewBean)) {
            PCDetailAnnonceAjaxViewBean vb = (PCDetailAnnonceAjaxViewBean) viewBean;
            idAnnonce = request.getParameter("annonceId");

            try {
                vb.setAnnonceId(idAnnonce);
                viewBean = vb;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (viewBean instanceof PCAnnoncesViewBean) {
            boolean hasRightsForGroupResponsableRPC = hasRightsForGroupResponsableRPC((BSession) viewBean.getISession());
            boolean canGenerateAnnonces = canGenerateAnnonces();

            request.setAttribute("hasRightsForGroupResponsableRPC", hasRightsForGroupResponsableRPC);
            request.setAttribute("canGenerateAnnonces", canGenerateAnnonces);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    private boolean hasRightsForGroupResponsableRPC(final BSession session) {
        boolean hasRights = false;

        try {
            // Récupération de la valeur de la propriété (peut avoir plusieurs groupes séparé par des virgules)
            final String groupResponsableValue = PCproperties.getProperties(EPCProperties.RPC_GROUPE_RESPONSABLE);

            if (!JadeStringUtil.isBlankOrZero(groupResponsableValue)) {

                // Nous supprimons les espaces vides et découpons les virgules pour en faire un tableau
                final String[] groupsResponsable = StringUtils.deleteWhitespace(groupResponsableValue).split(",");

                final HashSet<String> hashSet = new HashSet<String>();
                hashSet.addAll(Arrays.asList(groupsResponsable));

                // Récupération des tous les groupes de l'utilisateur de la session
                final JadeUserGroup[] groupsUser = JadeAdminServiceLocatorProvider.getLocator().getUserGroupService()
                        .findForIdUser(session.getUserId());

                // Vérification si l'utilisateur a bien un des groupes responsable pour les RPC
                for (int i = 0; i < groupsUser.length; i++) {
                    final JadeUserGroup groupUser = groupsUser[i];

                    if (hashSet.contains(groupUser.getIdGroup())) {
                        hasRights = true;
                    }
                }
            }
        } catch (PropertiesException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return hasRights;
    }

    // Only is possible to generate annonces between 1st and 10th of each month (take day from RPC properties on DB)
    private boolean canGenerateAnnonces() {
        boolean canGenerate = false;
        try {
            // Récupération de la valeur de la propriété (peut avoir plusieurs groupes séparé par des virgules)
            final String limitDayString = PCproperties.getProperties(EPCProperties.RPC_LIMIT_DAY_GENERATION);

            if (!JadeStringUtil.isBlankOrZero(limitDayString)) {
                int todayDay = Integer.parseInt(Date.now().getJour());
                int limitDay = Integer.parseInt(limitDayString);
                canGenerate = todayDay <= limitDay;
            }
        } catch (PropertiesException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return canGenerate;
    }

}
