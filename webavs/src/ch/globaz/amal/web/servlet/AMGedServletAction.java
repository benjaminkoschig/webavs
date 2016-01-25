/**
 * 
 */
package ch.globaz.amal.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWActionGedResult;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.message.JadeGedCallDefinition;
import globaz.jade.ged.message.JadeGedCallType;
import globaz.jade.ged.message.JadeGedService;
import globaz.jade.ged.target.JadeGedTargetProperties;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author dhi
 * 
 */
public class AMGedServletAction extends AMAbstractServletAction {

    /**
     * Default constructor
     * 
     * @param aServlet
     */
    public AMGedServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @return
     */
    public String lectureGedContribuable(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        String selectedNSS = request.getParameter("nNSS");
        String selectednoContribuable = request.getParameter("noContribuable");
        try {

            String blankStr = "";
            String nomService = "MAL";

            // vérifier que le service se trouve dans la liste
            boolean isInList = false;

            List services = JadeGedFacade.getServicesList();
            for (Iterator iterator = services.iterator(); iterator.hasNext();) {
                JadeGedService service = (JadeGedService) iterator.next();
                if (service.toString().equals(nomService)) {
                    isInList = true;
                }
            }

            if (!isInList) {
                nomService = blankStr;
            }

            // création des props à passer à la GED
            Properties gedCallProps = new Properties();
            gedCallProps.setProperty(JadeGedTargetProperties.ACTION, JadeGedTargetProperties.ACTION_VIEW_DOCUMENT);
            gedCallProps.setProperty(JadeGedTargetProperties.SERVICE, nomService);
            if (!JadeStringUtil.isEmpty(selectednoContribuable)) {
                gedCallProps.setProperty("numero.contribuable", selectednoContribuable);
            }
            if (!JadeStringUtil.isEmpty(selectedNSS)) {
                gedCallProps.setProperty("pyxis.tiers.numero.avs.non.formatte", selectedNSS);
            }

            BSession currentSession = (BSession) session.getAttribute("objSession");
            gedCallProps.setProperty(JadeGedTargetProperties.USER, currentSession.getUserId());
            // technique: besoin de l'adresse ip du client
            gedCallProps.setProperty(JadeGedTargetProperties.IP_ADDRESS, request.getRemoteAddr()); // adresse ip du
                                                                                                   // client
            System.out
                    .println(" ***** on va appeler la JadeGedFacade avec les properties (service=" + nomService + ")");
            JadeGedCallDefinition result = JadeGedFacade.call(gedCallProps);
            System.out.println(" ***** on va en mettre le résultat en requête");
            request.setAttribute(FWActionGedResult.OBJ_JADE_CALL_RESULT, result);
            JadeGedCallType rType = result.getType();
            System.out.println(" ***** type de résultat GED: " + rType.toString());
            System.out.println(" ***** commande à exécuter: " + result.getCommand());
            System.out.println(" ***** on prépare le servletCall");
            String servletCall = request.getServletPath() + "?userAction=" + FWActionGedResult.FRAMEWORK_GED;
            System.out.println(" ***** on choppe la page " + servletCall + " pour y faire un forward.");
            RequestDispatcher rd = request.getRequestDispatcher(servletCall);
            rd.forward(request, response);
        } catch (Exception e) {
            ServletException error = new ServletException("GED call failed. ", e);
        }
        return "";
    }

}
