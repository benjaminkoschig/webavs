package globaz.osiris.servlet.action.yellowreportfile;

import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la lecture des BVRs.
 * 
 * @author DDA
 */
public class CAYellowReportFileAction extends CADefaultServletAction {

    public CAYellowReportFileAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        System.out.println("passage");
    }
}
