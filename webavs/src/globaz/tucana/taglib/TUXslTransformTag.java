package globaz.tucana.taglib;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jsp.util.GlobazJSPBeanUtil;
import globaz.tucana.exception.transform.TUXmlTransformException;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;

/**
 * Classe permettant la transformation XML
 * 
 * @author fgo date de création : 5 juil. 06
 * @version : version 1.0
 * 
 */
public class TUXslTransformTag extends TagSupport {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String beanName = null;

    private String beanProperty = null;

    private String scope = "request";

    private String xslFile = null;

    /**
	 *
	 */
    public TUXslTransformTag() {
        super();
    }

    /**
     * Vérifie les propriété du tag et retourne une erreur si un propriété obligatoire n'est pas saisie
     */
    private void checkProperties() throws JspException {
        if (JadeStringUtil.isEmpty(getBeanName())) {
            throw new JspException("xslTransformTag : Bean name value is empty!");
        } else if (JadeStringUtil.isEmpty(getBeanProperty())) {
            throw new JspException("xslTransformTag : Bean property value is empty!");
        } else if (JadeStringUtil.isEmpty(getXslFile())) {
            throw new JspException("xslTransformTag : Xsl file value is empty!");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        try {

            transform(pageContext.getOut());
        } catch (TransformerConfigurationException e) {
            throw new JspException(e.getMessage());
        } catch (TransformerFactoryConfigurationError e) {
            throw new JspException(e.getMessage());
        } catch (TransformerException e) {
            printError(e);
        } catch (IllegalArgumentException e) {
            throw new JspException(e.getMessage());
        } catch (SecurityException e) {
            throw new JspException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new JspException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new JspException(e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new JspException(e.getMessage());
        } catch (TUXmlTransformException e) {
            printError(e);
        }
        return super.doEndTag();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        checkProperties();
        return super.doStartTag();
    }

    /**
     * Récupère le nom du bean
     * 
     * @return
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * Instancie un objet bean
     * 
     * @return
     */
    private Object getBeanObject() throws IllegalArgumentException, SecurityException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, TUXmlTransformException {
        Object scopeObject = getScopeObject();
        if (scopeObject instanceof HttpServletRequest) {
            return ((HttpServletRequest) scopeObject).getAttribute(getBeanName());
        } else if (scopeObject instanceof HttpSession) {
            return ((HttpSession) scopeObject).getAttribute(getBeanName());
        } else if (scopeObject instanceof PageContext) {
            return ((PageContext) scopeObject).getAttribute(getBeanName());
        } else {
            throw new TUXmlTransformException("Bean object null!");
        }
    }

    /**
     * Récupère les propriétés du bean
     * 
     * @return
     */
    public String getBeanProperty() {
        return beanProperty;
    }

    /**
     * Récupère le scope
     * 
     * @return
     */
    public String getScope() {
        return scope;
    }

    /**
     * Récupère l'objet du scope
     * 
     * @return
     */
    private Object getScopeObject() {
        if ("request".equals(getScope())) {
            return pageContext.getRequest();
        } else if ("session".equals(getScope())) {
            return pageContext.getSession();
        } else if ("pageContext".equals(getScope())) {
            return pageContext;
        } else {
            return null;
        }
    }

    /**
     * retourne l'emplacement du fichier xsl
     * 
     * @return
     */
    public String getXslFile() {
        return xslFile;
    }

    /**
     * Retourne le document "DOM" en fonction du bean
     * 
     * @return
     */
    private Document loadDocument() throws IllegalArgumentException, SecurityException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, TUXmlTransformException {
        Object beanObject = getBeanObject();
        if (beanObject != null) {
            return (Document) GlobazJSPBeanUtil.getProperty(getBeanProperty(), beanObject);
        } else {
            throw new TUXmlTransformException("Bean object null!");
        }
    }

    /**
     * Affichage d'une erreur sur la page en cas de problème de transformation
     * 
     * @param e
     *            l'exception
     */
    private void printError(Exception e) throws JspException {
        try {
            if (e instanceof TransformerException) {
                pageContext.getOut().write("<div>Problème de transformation</div>");
            } else if (e instanceof TUXmlTransformException) {
                pageContext.getOut().write("<div>Problème d'accès aux données xml</div>");
            }
        } catch (IOException ioe) {
            throw new JspException(ioe.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    @Override
    public void release() {
        super.release();
        beanName = null;
        beanProperty = null;
        scope = "request";
        xslFile = null;
    }

    /**
     * Modification du nom du bean
     * 
     * @param string
     */
    public void setBeanName(String string) {
        beanName = string;
    }

    /**
     * Modification de la propriété du bean
     * 
     * @param string
     */
    public void setBeanProperty(String string) {
        beanProperty = string;
    }

    /**
     * Modification du scope
     * 
     * @param string
     */
    public void setScope(String string) {
        scope = string;
    }

    /**
     * Modification de l'emplacement du fichier xsl
     * 
     * @param string
     */
    public void setXslFile(String string) {
        xslFile = string;
    }

    /**
     * Appel de la transformation
     * 
     * 
     */
    private void transform(Writer writer) throws IllegalArgumentException, SecurityException,
            TransformerFactoryConfigurationError, TransformerException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, TUXmlTransformException {
        transform(writer, loadDocument());
    }

    /**
     * Appel de la transformation
     * 
     * @param document
     * @return
     */
    private void transform(Writer writer, Document document) throws TransformerFactoryConfigurationError,
            TransformerException {
        TransformerFactory.newInstance()
                .newTransformer(new StreamSource(pageContext.getServletContext().getRealPath(xslFile)))
                .transform(new DOMSource(document), new StreamResult(writer));
    }

}
