/**
 * 
 */
package globaz.fweb.taglib.html;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jsp.util.GlobazJSPBeanUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.jsp.JspException;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;

/**
 * @author ECO
 * 
 */
public class PCDroitDonneeFinanciereButtonsTag extends GlobazBaseHtmlTag {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ModificateurDroitDonneeFinanciere droit;
    private String droitName = null;
    private boolean hasRight = false;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.fweb.taglib.html.GlobazBaseHtmlTag#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        String currentEtatDroit = droit.getSimpleVersionDroit().getCsEtatDroit();
        if ("64003001".equals(currentEtatDroit) || "64003002".equals(currentEtatDroit)
                || "64003003".equals(currentEtatDroit)) {
            hasRight = true;
            StringBuffer html = new StringBuffer("<div");
            html.append(renderAllAttributesHTML()).append(">");

            try {
                pageContext.getOut().write(html.toString());
                if (getBodyContent() != null) {
                    getBodyContent().writeOut(getBodyContent().getEnclosingWriter());
                }
            } catch (IOException e) {
                throw new JspException("Error :" + e.toString());
            }
        }
        return super.doEndTag();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.fweb.taglib.html.GlobazBaseHtmlTag#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        retrieveObject();
        return super.doStartTag();
    }

    /**
     * @return the droit
     */
    public ModificateurDroitDonneeFinanciere getDroit() {
        return droit;
    }

    /**
     * @return the droitName
     */
    public String getDroitName() {
        return droitName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.fweb.taglib.html.GlobazBaseHtmlTag#getStyleClass()
     */

    // @Override
    // public String getStyleClass() {
    // // TODO Auto-generated method stub
    // return super.getStyleClass() + " btnAjax";
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.fweb.taglib.html.GlobazBaseHtmlTag#renderHtml()
     */
    @Override
    protected String renderHtml() throws JspException {
        if (hasRight) {
            return "</div>";
        } else {
            return "";
        }
    }

    /**
     * Retrieves the object located in the page context by its pathname and, if found, stores the object in a temporary
     * private field.
     */
    private void retrieveObject() {
        droit = null;
        setStyleClass(getStyle() + " btnAjax");
        try {
            if (JadeStringUtil.isEmpty(droitName)) {
                droitName = "viewBean";
            }
            // parse the object's pathname
            String[] fields = droitName.split("\\.");
            // seek for the object by traversing the objects of the pathname
            Object currentObj = pageContext.getRequest().getAttribute(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                currentObj = GlobazJSPBeanUtil.getProperty(fields[i], currentObj);
            }
            droit = (ModificateurDroitDonneeFinanciere) currentObj;
        } catch (IllegalAccessException e) {
            JadeLogger.warn(this, e);
        } catch (InvocationTargetException e) {
            JadeLogger.warn(this, e);
        } catch (NoSuchMethodException e) {
            JadeLogger.warn(this, e);
        }
    }

    /**
     * @param droit
     *            the droit to set
     */
    public void setDroit(ModificateurDroitDonneeFinanciere droit) {
        this.droit = droit;
    }

    /**
     * @param droitName
     *            the droitName to set
     */
    public void setDroitName(String droitName) {
        this.droitName = droitName;
    }

}
