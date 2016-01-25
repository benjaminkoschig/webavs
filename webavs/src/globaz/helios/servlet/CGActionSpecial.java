package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGJournalViewBean;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la sélection d'un journal directement depuis la comptabilité auxiliaire.
 * 
 * @author DDA
 * 
 */
public class CGActionSpecial extends CGDefaultServletAction {

    private static final String USER_ACTION_AFFICHER_JOURNAL = "helios.comptes.journal.afficher";

    /**
     * @param servlet
     */
    public CGActionSpecial(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Action permettant de charger un journal et l'exercice comptable lié. Utilisé depuis Osiris (Lien Journal Osiris
     * -> Journal Helios).
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction action = FWAction.newInstance(USER_ACTION_AFFICHER_JOURNAL);
        String myDestination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, VIEWBEAN, viewBean);

            myDestination = getRelativeURL(request, session) + "_de.jsp";

            myDestination = myDestination.replaceAll("/special/", "/comptes/");

            if (viewBean instanceof CGJournalViewBean) {
                session.removeAttribute(CGNeedExerciceComptable.SESSION_DESTINATION);

                CGExerciceComptableViewBean exercice = new CGExerciceComptableViewBean();
                exercice.setISession(viewBean.getISession());
                exercice.setIdExerciceComptable(((CGJournalViewBean) viewBean).getIdExerciceComptable());

                // User-rights OK car catcher préalablement par dispatch
                exercice.retrieve();

                setSessionAttribute(session, CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE, exercice);
            }
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

}
