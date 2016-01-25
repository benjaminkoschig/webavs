package globaz.musca.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.musca.db.facturation.FAOrdreAttribuerViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionOrdreAttribuer extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionOrdreAttribuer(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        FAOrdreAttribuerViewBean vBean = (FAOrdreAttribuerViewBean) viewBean;
        // Sauvegarde du module de facturation pour test dans _validate du
        // viewBean
        vBean.setIdOrdreRegroupement(request.getParameter("selectedId"));
        vBean.setIdRubrique(request.getParameter("selectedId2"));
        vBean.setIdOrdreAttribuer(request.getParameter("selectedId3"));

        return vBean;
    }
}
