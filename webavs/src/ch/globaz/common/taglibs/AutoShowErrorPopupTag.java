package ch.globaz.common.taglibs;

import globaz.framework.controller.FWController;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import java.rmi.RemoteException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/***
 * Définit la variable "autoShowErrorPopup" dans le pageContext afin qu'elle puisse être accessible via une EL
 */
public class AutoShowErrorPopupTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    /** Nom de la variable à stocker dans le pageContext */
    private static final String AUTO_SHOW_ERROR_POPUP = "autoShowErrorPopup";

    @Override
    public int doStartTag() throws JspException {
        // Récupére la BISession dans le pageContext
        BISession session = ((FWController) pageContext.getSession().getAttribute(FWServlet.OBJ_CONTROLLER))
                .getSession();
        boolean autoShowErrorPopup = false;
        try {
            autoShowErrorPopup = session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_NO_JSP_POPUP) == null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        pageContext.setAttribute(AutoShowErrorPopupTag.AUTO_SHOW_ERROR_POPUP, autoShowErrorPopup);
        return super.doStartTag();
    }
}
