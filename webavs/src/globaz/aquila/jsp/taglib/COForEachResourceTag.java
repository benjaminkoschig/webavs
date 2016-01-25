package globaz.aquila.jsp.taglib;

import java.io.File;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 * <H1>Description</H1>
 * <p>
 * Tag d'itération sur les ressources d'un serveur.
 * </p>
 * <p>
 * Obtient une liste de type dossier de tous les chemins vers les ressources de l'application Web dont le sous-chemin
 * commence par l'uri transmis en argument, itère sur cette liste et stocke le sous-chemin courant comme attribut du
 * pageContext en utilisant le nom var. Strip le chemin et ne conserve que le nom de fichier si stripPath est vrai.
 * </p>
 * 
 * @author vre
 */
public class COForEachResourceTag extends BodyTagSupport {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -650280774353129645L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private File[] files;
    private int id;
    private boolean stripPath;
    private String uri;
    private String var;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return
     * @throws JspException
     */
    @Override
    public int doAfterBody() throws JspException {
        if (id < files.length) {
            doStartBody();

            return BodyTag.EVAL_BODY_BUFFERED;
        }

        return Tag.SKIP_BODY;
    }

    private void doStartBody() throws JspException {
        File file = files[id++];
        String path = file.getName();

        if (!stripPath) {
            // faire précéder de l'uri si nécessaire
            if (!uri.endsWith("/")) {
                path = uri + "/" + path;
            } else {
                path = uri + path;
            }
        }

        pageContext.setAttribute(var, path);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     * @throws JspException
     */
    @Override
    public int doStartTag() throws JspException {
        // obtenir la liste des ressources de l'uri donné
        String realPath = pageContext.getServletContext().getRealPath(uri);
        File file = new File(realPath);

        if (file.isDirectory()) {
            files = file.listFiles();

            if ((files == null) || (files.length == 0)) {
                return Tag.SKIP_BODY;
            }

            // initier l'itération
            id = 0;
            doStartBody();
        }

        return Tag.EVAL_BODY_INCLUDE;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getUri() {
        return uri;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public String getVar() {
        return var;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean isStripPath() {
        return stripPath;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param stripPath
     *            DOCUMENT ME!
     */
    public void setStripPath(boolean stripPath) {
        this.stripPath = stripPath;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param dir
     *            DOCUMENT ME!
     */
    public void setUri(String dir) {
        uri = dir;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param var
     *            DOCUMENT ME!
     */
    public void setVar(String var) {
        this.var = var;
    }
}
