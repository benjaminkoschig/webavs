package ch.globaz.amal.web.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AMProcessServletAction extends FWDefaultServletAction {
    public AMProcessServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionExporter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        // AMUploadFichierRepriseViewBean uploadFichierRepriseViewBean = new AMUploadFichierRepriseViewBean();
        //
        // try {
        // uploadFichierRepriseViewBean.serializeXmlFile(request);
        // } catch (Exception e) {
        // JadeThread.logError(this.getClass().getName(), e.getMessage());
        // }

        // super.actionExporter(session, request, response, mainDispatcher);
    }

}
