/**
 *
 */
package ch.globaz.vulpecula.web.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWActionGedResult;
import globaz.framework.servlets.FWServlet;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.ged.message.JadeGedCallDefinition;
import globaz.jade.ged.target.JadeGedTargetProperties;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.vulpecula.web.application.PTApplication;

/**
 * G�re les actions pour un d�compte
 * 
 * @since Web@BMS 0.01.01
 */
public class PTAfficherGedAction extends FWDefaultServletAction {

    public static final String GED_DECOMPTE_INDEX = "N_DECOMPTE_VALUE";
    public static final String GED_FIELD1 = "FIELD1";

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String idDecompte = request.getParameter("selectedId");
        // cr�ation des props � passer � la GED
        Properties gedCallProps = new Properties();
        // on veut montrer un dossier: ACTION pour les deux GED
        gedCallProps.setProperty(JadeGedTargetProperties.ACTION, JadeGedTargetProperties.ACTION_OPEN_FOLDER);
        // propri�t� TI:groupdoc: nom correspondant � un folder
        // gedCallProps.setProperty(JadeGedTargetProperties.FOLDER_TYPE, "affilie");
        // ajout du nom de service, pour au cas o�
        gedCallProps.setProperty(JadeGedTargetProperties.SERVICE, PTApplication.DEFAULT_APPLICATION_VULPECULA);
        // propri�t�s business, correspondant � des indexes
        gedCallProps.setProperty(GED_DECOMPTE_INDEX, idDecompte);

        try {
            JadeGedCallDefinition result = JadeGedFacade.call(gedCallProps);
            request.setAttribute(FWActionGedResult.OBJ_JADE_CALL_RESULT, result);
            String servletCall = request.getServletPath() + "?userAction=" + FWActionGedResult.FRAMEWORK_GED;
            RequestDispatcher rd = request.getRequestDispatcher(servletCall);
            rd.forward(request, response);
        } catch (Exception e) {
            new ServletException("GED call failed. ", e);
        }

    }

    public PTAfficherGedAction(final FWServlet aServlet) {
        super(aServlet);
    }

}
