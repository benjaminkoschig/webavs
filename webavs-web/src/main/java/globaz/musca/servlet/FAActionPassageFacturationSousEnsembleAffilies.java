package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.musca.db.facturation.FAPassageFacturationSousEnsembleAffiliesViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FAActionPassageFacturationSousEnsembleAffilies extends FWDefaultServletAction {
    public FAActionPassageFacturationSousEnsembleAffilies(FWServlet servlet) {
        super(servlet);
    }

    /*
     * Cet écran ne va pas sur l'écran de recherche après la création d'un cas (car il n'y a pas de rc / rcListe) Il
     * reste donc sur l'écran de détail
     * 
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_valid=&_method=&selectedId="
                + ((FAPassageFacturationSousEnsembleAffiliesViewBean) viewBean).getIdPassage();
    }
}
