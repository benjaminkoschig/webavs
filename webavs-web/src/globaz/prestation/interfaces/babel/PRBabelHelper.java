package globaz.prestation.interfaces.babel;

import globaz.babel.api.ICTDocument;
import globaz.babel.application.CTApplication;
import globaz.globall.api.BISession;
import globaz.prestation.tools.PRSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe avec des méthodes statiques pour acceder facilement a babel avec une session babel.
 * </p>
 * 
 * @author vre
 */
public class PRBabelHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static final ICTDocument getDocumentHelper(BISession session) throws Exception {
        ICTDocument document = (ICTDocument) session.getAPIFor(ICTDocument.class);

        document.setISession(PRSession.connectSession(session, CTApplication.DEFAULT_APPLICATION_BABEL));

        return document;
    }
}
