package ch.globaz.common.taglibs;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/***
 * Définit la variable "creationSpy" dans le pageContext afin qu'elle puisse être accessible via une EL
 */
public class CreationSpyTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    /** Nom de la variable à stocker dans le pageContext */
    private static final String CREATION_SPY = "creationSpy";

    @Override
    public int doStartTag() throws JspException {
        FWViewBeanInterface viewBean = (FWViewBeanInterface) pageContext.getSession().getAttribute(FWServlet.VIEWBEAN);
        String creationSpy = "";
        java.lang.reflect.Method creationSpyMethod = null;
        try {
            creationSpyMethod = viewBean.getClass().getMethod("getCreationSpy", new Class[0]);
            globaz.globall.db.BSpy creationSpyObject = (globaz.globall.db.BSpy) creationSpyMethod.invoke(this,
                    new Object[0]);
            if (creationSpyObject != null) {
                creationSpy = "Creation: " + creationSpyObject.getDate() + ", " + creationSpyObject.getTime() + " - "
                        + creationSpyObject.getUser() + " / ";
            }
        } catch (Exception e) {
        }
        pageContext.setAttribute(CreationSpyTag.CREATION_SPY, creationSpy);
        return super.doStartTag();
    }
}
