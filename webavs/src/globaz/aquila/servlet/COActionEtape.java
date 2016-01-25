package globaz.aquila.servlet;

import globaz.aquila.db.batch.COSequenceListViewBean;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * <p>
 * Action de gestion des �crans li�s � la configuration des �tapes.
 * </p>
 * 
 * @author vre
 */
public class COActionEtape extends CODefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Creates a new COEtapeAction object.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public COActionEtape(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Charge la liste des s�quences pour le filtre de l'�cran de recherche des �tapes.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // charger la liste des s�quences
        COSequenceListViewBean sequenceListViewBean = new COSequenceListViewBean();

        mainDispatcher.dispatch(sequenceListViewBean, getAction());
        request.setAttribute("viewBean", sequenceListViewBean);

        // reprendre le comportement standard
        super.actionChercher(session, request, response, mainDispatcher);
    }
}
