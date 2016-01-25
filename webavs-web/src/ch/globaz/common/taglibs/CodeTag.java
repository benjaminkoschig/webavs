package ch.globaz.common.taglibs;

import globaz.framework.controller.FWController;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import java.io.IOException;
import java.rmi.RemoteException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Tag permettant de rechercher un code système court
 */
public class CodeTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    private String csCode;

    @Override
    public int doStartTag() throws JspException {
        // Récupérer la BISession dans le pageContext
        BISession session = ((FWController) pageContext.getSession().getAttribute(FWServlet.OBJ_CONTROLLER))
                .getSession();
        try {
            pageContext.getOut().print(session.getCode(csCode));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doStartTag();
    }

    public String getCsCode() {
        return csCode;
    }

    public void setCsCode(String csCode) {
        this.csCode = csCode;
    }
}
