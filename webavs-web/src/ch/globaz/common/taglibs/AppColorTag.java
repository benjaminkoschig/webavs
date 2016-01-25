package ch.globaz.common.taglibs;

import globaz.jade.common.Jade;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/***
 * Définit la variable "appColor" dans le pageContext afin qu'elle puisse être accessible via une EL
 * 
 */
public class AppColorTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    /** Nom de la variable à stocker dans le pageContext */
    private static final String APP_COLOR = "appColor";

    @Override
    public int doStartTag() throws JspException {
        String appColor = Jade.getInstance().getWebappBackgroundColor();
        pageContext.setAttribute(AppColorTag.APP_COLOR, appColor);
        return super.doStartTag();
    }
}
