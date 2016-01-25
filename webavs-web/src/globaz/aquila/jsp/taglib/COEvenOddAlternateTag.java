package globaz.aquila.jsp.taglib;

import java.io.IOException;
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
public class COEvenOddAlternateTag extends TagSupport {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String KEY_FLAG = COEvenOddAlternateTag.class.getName() + ".KEY_BOOLEAN";

    private static final long serialVersionUID = 7585037424253533753L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String even;
    private Boolean flag;
    private String odd;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ecrit soit la valeur even soit la valeur odd dans la page.
     * 
     * @return
     * @throws JspException
     */
    @Override
    public int doEndTag() throws JspException {
        try {
            if (flag.booleanValue()) {
                pageContext.getOut().write(odd);
            } else {
                pageContext.getOut().write(even);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Tag.EVAL_PAGE;
    }

    /**
     * Retrouve le booléen dans le contexte de la page et l'inverse si nécessaire.
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        flag = (Boolean) pageContext.getAttribute(COEvenOddAlternateTag.KEY_FLAG);

        if ((flag == null) || Boolean.FALSE.equals(flag)) {
            flag = Boolean.TRUE;
        } else {
            flag = Boolean.FALSE;
        }

        pageContext.setAttribute(COEvenOddAlternateTag.KEY_FLAG, flag);

        return Tag.EVAL_PAGE;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getEven() {
        return even;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getOdd() {
        return odd;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param even
     *            DOCUMENT ME!
     */
    public void setEven(String even) {
        this.even = even;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param odd
     *            DOCUMENT ME!
     */
    public void setOdd(String odd) {
        this.odd = odd;
    }

}
