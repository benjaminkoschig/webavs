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
     * Cet �cran ne va pas sur l'�cran de recherche apr�s la cr�ation d'un cas (car il n'y a pas de rc / rcListe) Il
     * reste donc sur l'�cran de d�tail
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
