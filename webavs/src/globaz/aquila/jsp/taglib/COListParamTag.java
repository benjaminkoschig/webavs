package globaz.aquila.jsp.taglib;

import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Permet d'ajouter des param�tres au manager du COListTag parent.
 * 
 * @author Pascal Lovy, 27-oct-2004
 */
public class COListParamTag extends TagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Le nom du param�tre */
    private String name = null;
    /** La valeur du param�tre */
    private Object value = null;

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        // R�cup�ration du parent
        COListTag listTag = (COListTag) TagSupport.findAncestorWithClass(this, COListTag.class);
        if (listTag == null) {
            throw new JspTagException("Le tag \"COListParam\" doit �tre � l'int�rieur d'un tag \"COList\".");
        }
        // Ajout du param�tre
        if (!JadeStringUtil.isEmpty(name)) {
            listTag.getManagerParameters().put(name, value);
        }
        // Continue � �valuer la page
        return Tag.EVAL_PAGE;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getName() {
        return name;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param object
     *            La nouvelle valeur de la propri�t�
     */
    public void setValue(Object object) {
        value = object;
    }

}
