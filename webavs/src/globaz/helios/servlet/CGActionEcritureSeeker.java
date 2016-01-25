package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la recherche des écritures.
 * 
 * @author DDA
 */
public class CGActionEcritureSeeker extends CGActionNeedExerciceComptable {

    public CGActionEcritureSeeker(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((BManager) viewBean).changeManagerSize(1000);
        return viewBean;
    }

}
