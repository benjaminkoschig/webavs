package globaz.osiris.servlet.action.interets;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.osiris.db.interets.CAGenreInteretViewBean;
import globaz.osiris.db.interets.CAPlanCalculInteretViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage du genre des intérêts.
 * 
 * @author sch
 */
public class CAGenreInteretAction extends CADefaultServletAction {

    public CAGenreInteretAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(HttpSession , HttpServletRequest,
     * HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        CAGenreInteretViewBean viewBean = new CAGenreInteretViewBean();
        // enregister les paramètres de la requête dans le bean
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

        } catch (Exception e) {
            // bean reste vide
            e.printStackTrace();
        }
        FWAction action = getAction();
        // action.setRight(FWSecureConstants.READ);
        // appel du controlleur
        viewBean = (CAGenreInteretViewBean) mainDispatcher.dispatch(viewBean, action);

        // sauve le bean dans la session en tant que bean foreign key (utilisé
        // plus tard également)
        session.setAttribute("viewBean", viewBean);

        super.actionAfficher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        CAPlanCalculInteretViewBean viewBean = new CAPlanCalculInteretViewBean();
        // enregister les paramètres de la requête dans le bean
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

        } catch (Exception e) {
            // bean reste vide
            e.printStackTrace();
        }
        FWAction action = getAction();
        // action.setRight(FWSecureConstants.READ);
        // appel du controlleur
        viewBean = (CAPlanCalculInteretViewBean) mainDispatcher.dispatch(viewBean, action);

        // sauve le bean dans la session en tant que bean foreign key (utilisé
        // plus tard également)
        session.setAttribute("viewBean", viewBean);

        // corrections pour chercher écritures depuis le journal

        // TODO Auto-generated method stub

        super.actionChercher(session, request, response, mainDispatcher);
    }

    // protected CAGenreInteretViewBean getGenreInteret(HttpServletRequest request) throws ServletException {
    // CAGenreInteretViewBean element = (CAGenreInteretViewBean) JSPUtils.useBean(request, "element",
    // "globaz.osiris.db.interets.CAGenreInteretViewBean", "session");
    // if ((!JadeStringUtil.isBlank(this.getId(request, "idGenreInteret")))
    // && (!JadeStringUtil.isNull(this.getId(request, "idGenreInteret")))) {
    // element.setIdGenreInteret(this.getId(request, "idGenreInteret"));
    // } else {
    // element = (CAGenreInteretViewBean) JSPUtils.useBean(request, "element",
    // "globaz.osiris.db.interets.CAGenreInteretViewBean", "session", true);
    // }
    //
    // return element;
    // }

}
