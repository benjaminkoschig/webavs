package globaz.aquila.jsp.taglib;

import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Permet d'ajouter des paramètres au manager du COListTag parent.
 * 
 * @author Pascal Lovy, 27-oct-2004
 */
public class COListParamTag extends TagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Le nom du paramètre */
    private String name = null;
    /** La valeur du paramètre */
    private Object value = null;

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        // Récupération du parent
        COListTag listTag = (COListTag) TagSupport.findAncestorWithClass(this, COListTag.class);
        if (listTag == null) {
            throw new JspTagException("Le tag \"COListParam\" doit être à l'intérieur d'un tag \"COList\".");
        }
        // Ajout du paramètre
        if (!JadeStringUtil.isEmpty(name)) {
            listTag.getManagerParameters().put(name, value);
        }
        // Continue à évaluer la page
        return Tag.EVAL_PAGE;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getName() {
        return name;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param object
     *            La nouvelle valeur de la propriété
     */
    public void setValue(Object object) {
        value = object;
    }

}
