package ch.globaz.common.taglibs;

import globaz.framework.controller.FWController;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/***
 * D�finit la variable "languePage" dans le pageContext afin qu'elle puisse �tre accessible via une EL
 */
public class LanguePageTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    /** Nom de la variable � stocker dans le pageContext */
    private static final String LANGUE_PAGE = "languePage";

    @Override
    public int doStartTag() throws JspException {
        // R�cup�re la BISession dans le pageContext
        BISession session = ((FWController) pageContext.getSession().getAttribute(FWServlet.OBJ_CONTROLLER))
                .getSession();
        String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
        pageContext.setAttribute(LanguePageTag.LANGUE_PAGE, languePage);
        return super.doStartTag();
    }
}
