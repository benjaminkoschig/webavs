package ch.globaz.common.taglibs;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/***
 * D�finit la variable "applicationId" dans le pageContext afin qu'elle puisse �tre accessible via une EL
 */
public class ApplicationIdTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    /** Nom de la variable � stocker dans le pageContext */
    private static final String APPLICATION_ID = "applicationId";

    @Override
    public int doStartTag() throws JspException {
        // R�cup�rer la BISession dans le pageContext
        HttpSession session = pageContext.getSession();
        String applicationId = ((globaz.globall.db.BSession) session
                .getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION)).getApplicationId();
        pageContext.setAttribute(ApplicationIdTag.APPLICATION_ID, applicationId);
        return super.doStartTag();
    }
}
