package ch.globaz.al.utils;

import globaz.fweb.taglib.html.GlobazBaseHtmlTag;
import globaz.fweb.taglib.html.GlobazSelectTag;
import globaz.jade.client.util.JadeStringUtil;
import java.lang.reflect.Method;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * @author jts
 * 
 */
public class ALOptionEnumTag extends GlobazBaseHtmlTag {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Nom de l'enum (y compris package) */
    private String enumName = null;
    /** Nom de la méthode permettant de récupérer le libellé */
    private String mLibelle = null;
    /** Nom de la méthode permettant de récupérer la valeur */
    private String mValue = null;

    public String getEnumName() {
        return enumName;
    }

    public String getmLibelle() {
        return mLibelle;
    }

    public String getmValue() {
        return mValue;
    }

    private boolean isSelected(String idCode) {
        GlobazSelectTag parent = (GlobazSelectTag) TagSupport.findAncestorWithClass(this, GlobazSelectTag.class);

        if (parent != null) {
            return idCode.equals(JadeStringUtil.isEmpty(parent.getValue()) ? parent.getDefaultValue() : parent
                    .getValue());
        }

        return false;
    }

    @Override
    protected String renderHtml() throws JspException {
        StringBuffer html = new StringBuffer();
        Class<?> en;
        try {
            en = Class.forName(getEnumName());
            Method mLibelle = en.getMethod(getmLibelle(), (Class<?>[]) null);
            Method mValue = en.getMethod(getmValue(), (Class<?>[]) null);
            Object[] consts = en.getEnumConstants();

            for (int i = 0; i < consts.length; i++) {
                String label = (String) mLibelle.invoke(consts[i], (Object[]) null);
                String value = (String) mValue.invoke(consts[i], (Object[]) null);

                html.append("<option value=\"").append(value).append("\"");
                html.append(" label=\"").append(label).append("\"");
                html.append(isSelected(value) ? " selected=\"selected\">" : ">");
                html.append(label).append("</option>");
            }
        } catch (Exception e) {
            throw new JspException("Error : " + e.getMessage());
        }

        return html.toString();
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public void setmLibelle(String mLibelle) {
        this.mLibelle = mLibelle;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }

    public String test() throws JspException {
        return renderHtml();
    }
}
