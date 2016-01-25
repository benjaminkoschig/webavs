package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe pour les actions specifiques aux objet qui heritent de CGNeedExerciceComptable<br/>
 * Particularitée : transferer l'objet de la session http 'exerciceComtable' dans le bean pour traitement ultérieur dans
 * le controller ou dans le _validate() de l'objet. <br/>
 * Les classes qui auront besoin de cette objet doivent heriter de CGNeedExerciceComptable. exemple : CGPeriodeComptable
 * ou CGModeleEcriture <br/>
 * <br/>
 * Date de création : (10.10.2002 16:08:43)
 * 
 * @author: oca
 */

public class CGActionNeedExerciceComptable extends CGDefaultServletAction {

    public CGActionNeedExerciceComptable(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return setExerciceComptable(session, request, response, (CGNeedExerciceComptable) viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return setExerciceComptable(session, request, response, (CGNeedExerciceComptable) viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return setExerciceComptable(session, request, response, (CGNeedExerciceComptable) viewBean);
    }

    private FWViewBeanInterface setExerciceComptable(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, CGNeedExerciceComptable ex) {
        CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
        ex.setIdExerciceComptable(exerciceComptable.getIdExerciceComptable());

        return ex;
    }

}
