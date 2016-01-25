package globaz.aquila.jsp.taglib;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COTrueFalseImgTag extends TagSupport {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String PATH_TO_FALSE_IMG = "/images/erreur.gif";
    private static final String PATH_TO_TRUE_IMG = "/images/ok.gif";

    private static final long serialVersionUID = -7651924617434480949L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean flag = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return
     * @throws JspException
     */
    @Override
    public int doEndTag() throws JspException {
        try {
            pageContext.getOut().write("<IMG src=\"");
            pageContext.getOut().write(((HttpServletRequest) pageContext.getRequest()).getContextPath());

            if (Boolean.TRUE.equals(flag)) {
                pageContext.getOut().write(COTrueFalseImgTag.PATH_TO_TRUE_IMG);
            } else {
                pageContext.getOut().write(COTrueFalseImgTag.PATH_TO_FALSE_IMG);
            }

            pageContext.getOut().write("\"/>");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Tag.EVAL_PAGE;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public Boolean getFlag() {
        return flag;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param flag
     *            DOCUMENT ME!
     */
    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

}
