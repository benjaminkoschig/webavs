package ch.globaz.common.taglibs;

import globaz.framework.controller.FWController;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/***
 * Définit la variable {@link #var} après avoir contrôlé que l'utilisateur actuellement connecté dispose du/des droit(s)
 * {@link #crud} sur {@link #element}
 * 
 */
public class CheckRightTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    private String crud = "";
    private String element = "";
    private String var = "";

    private boolean checkRight(BSession userSession) {
        boolean result = true;
        for (int i = 0; i < crud.length(); i++) {
            switch (crud.charAt(i)) {
                case 'c':
                    result &= userSession.hasRight(element, FWSecureConstants.ADD);
                    break;
                case 'r':
                    result &= userSession.hasRight(element, FWSecureConstants.READ);
                    break;
                case 'u':
                    result &= userSession.hasRight(element, FWSecureConstants.UPDATE);
                    break;
                case 'd':
                    result &= userSession.hasRight(element, FWSecureConstants.REMOVE);
                    break;
                default:
            }
        }
        return result;
    }

    @Override
    public int doStartTag() throws JspException {
        // Récupérer la BISession dans le pageContext
        FWController controller = (FWController) pageContext.getSession().getAttribute(FWServlet.OBJ_CONTROLLER);
        BSession session = (BSession) controller.getSession();

        boolean hasRight = checkRight(session);

        pageContext.setAttribute(var, hasRight);

        return super.doStartTag();
    }

    public String getVar() {
        return var;
    }

    public void setCrud(String newCrud) {
        crud = newCrud;
    }

    public void setElement(String newElement) {
        element = newElement;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
