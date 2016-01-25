package globaz.prestation.servlet;

import globaz.framework.controller.FWController;
import globaz.framework.controller.IFWActionHandler;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRHybridHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Base pour créer une action ayant les fonctionnements des deux framework (FW et Jade) embarqués.<br/>
 * Fonctionne comme une action de l'ancien framework, mais charge la session dans un thread context avant d'executer
 * toute action.
 * </p>
 * 
 * @author PBA
 */
public class PRHybridAction extends PRDefaultAction implements IFWActionHandler {

    public PRHybridAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {

        if (mainController.getSession() != null) {
            PRHybridHelper.initContext((BSession) mainController.getSession(), this);
        }
        try {
            super.doAction(session, request, response, mainController);
        } finally {
            PRHybridHelper.stopUsingContext(this);
        }
    }

}
