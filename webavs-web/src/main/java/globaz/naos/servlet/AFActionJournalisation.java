/*
 * Cr�� le 3 f�vr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.naos.db.affiliation.AFAffiliationViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ald
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class AFActionJournalisation extends AFDefaultActionChercher {

    /**
     * @param servlet
     */
    public AFActionJournalisation(FWServlet servlet) {
        super(servlet);
        // TODO Raccord de constructeur auto-g�n�r�
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction userActionDetail = getAction();
        FWViewBeanInterface viewBean = new AFAffiliationViewBean();
        try {
            userActionDetail.changeActionPart(FWAction.ACTION_AFFICHER);
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, userActionDetail);
        } catch (Exception e) {
            viewBean.setMessage("Error to load viewBean, cause :" + e.getMessage());
        }
        session.setAttribute("viewBean", viewBean);
        //
        super.actionChercher(session, request, response, mainDispatcher);
    }

}
