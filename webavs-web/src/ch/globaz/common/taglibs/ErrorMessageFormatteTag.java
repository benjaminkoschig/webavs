package ch.globaz.common.taglibs;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/***
 * Définit la variable "errorMsgFormatte" dans le pageContext afin qu'elle puisse être accessible via une EL
 */
public class ErrorMessageFormatteTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    /** Nom de la variable à stocker dans le pageContext */
    private static final String ERROR_MSG_FORMATTE = "errorMsgFormatte";

    @Override
    public int doStartTag() throws JspException {
        FWViewBeanInterface viewBean = (FWViewBeanInterface) pageContext.getSession().getAttribute(FWServlet.VIEWBEAN);
        String messageFormatte = globaz.framework.util.FWTextFormatter.slash(
                globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"');
        pageContext.setAttribute(ErrorMessageFormatteTag.ERROR_MSG_FORMATTE, messageFormatte);
        return super.doStartTag();
    }
}
