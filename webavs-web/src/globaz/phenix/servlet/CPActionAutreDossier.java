package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.db.principale.CPAutreDossierViewBean;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionAutreDossier extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionAutreDossier(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof CPAutreDossierViewBean) {
            String destination = "";
            CPAutreDossierViewBean vb = (CPAutreDossierViewBean) viewBean;
            if ("1".equalsIgnoreCase(vb.getCodeDirection())) {
                destination = getActionFullURL().substring(0, getActionFullURL().lastIndexOf('.'))
                        + ".decision.chercher&selectedId=" + vb.getIdTiers() + "&selectedId2=" + vb.getIdAffiliation();
            } else if ("2".equalsIgnoreCase(vb.getCodeDirection())) {
                destination = "/naos?userAction=naos.affiliation.chercher&idTiers=" + vb.getIdTiers();
            } else {
                destination = super._getDestModifierEchec(session, request, response, viewBean);
            }
            return destination;
        } else {
            return super._getDestModifierEchec(session, request, response, viewBean);
        }
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if ("diriger".equals(getAction().getActionPart())) {

            String _destination = "/phenix?userAction=phenix.principale.autreDossier.afficher&_method=upd";

            try {
                String numeroSession = (String) session
                        .getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_IDTIERS_CTX);
                if (!JadeStringUtil.isBlank(numeroSession)) {

                    AFAffiliationManager mgr = new AFAffiliationManager();
                    BSession bSession = (BSession) CodeSystem.getSession(session);
                    mgr.setSession(bSession);
                    mgr.setForIdTiers(numeroSession);
                    mgr.setForDateFin("0");
                    mgr.find();

                    if (mgr.size() == 0) {
                        mgr = new AFAffiliationManager();
                        mgr.setSession(bSession);
                        mgr.setForIdTiers(numeroSession);
                        mgr.find();
                    }

                    for (Iterator<?> it = mgr.iterator(); it.hasNext();) {
                        AFAffiliation aff = (AFAffiliation) it.next();
                        // cotisations personnelles
                        if (CodeSystem.TYPE_AFFILI_INDEP.equals(aff.getTypeAffiliation())
                                || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(aff.getTypeAffiliation())
                                || CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(aff.getTypeAffiliation())
                                || CodeSystem.TYPE_AFFILI_TSE.equals(aff.getTypeAffiliation())
                                || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equals(aff.getTypeAffiliation())
                                || CodeSystem.TYPE_AFFILI_FICHIER_CENT.equals(aff.getTypeAffiliation())) {

                            _destination = "/phenix?userAction=phenix.principale.decision.chercher&idTiers= "
                                    + aff.getIdTiers() + "&selectedId2=" + aff.getAffiliationId();

                            break;
                        }
                    }
                }
            } catch (Exception e) {
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        }
    }

    // protected void actionCustom(
    // javax.servlet.http.HttpSession session,
    // javax.servlet.http.HttpServletRequest request,
    // javax.servlet.http.HttpServletResponse response,
    // FWDispatcher mainDispatcher)
    // throws javax.servlet.ServletException, java.io.IOException {
    // if("afficherDecisions".equals(getAction().getActionPart())){
    // actionAfficherDecisions(session, request, response, mainDispatcher);
    // }
    // }

    // private void actionAfficherDecisions(HttpSession session,
    // HttpServletRequest request, HttpServletResponse response,
    // FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
    // java.io.IOException {
    //
    // CPAutreDossierViewBean viewBean = null;
    // try {
    // // --- Variables
    // viewBean = (CPAutreDossierViewBean) session.getAttribute("viewBean");
    // if (viewBean == null) {
    // viewBean = new CPAutreDossierViewBean();
    // }
    // viewBean.setMsgType("");
    // globaz.globall.api.BISession bSession =
    // globaz.phenix.translation.CodeSystem.getSession(session);
    // globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
    // viewBean.setSession((globaz.globall.db.BSession) bSession);
    // //viewBean._controle();
    // //FWHelper.afterExecute(viewBean);
    // mainDispatcher.dispatch(viewBean, getAction());
    // //--- Check view bean
    // session.removeAttribute("viewBean");
    // session.setAttribute("viewBean", viewBean);
    //
    // if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true){
    // servlet.getServletContext().getRequestDispatcher(getRelativeURLwithoutClassPart(request,
    // session)+ "autreDossier_de.jsp").forward(request, response);
    // } else if ("1".equalsIgnoreCase(viewBean.getCodeDirection())){
    // servlet.getServletContext().getRequestDispatcher(getActionFullURL().substring(0,getActionFullURL().lastIndexOf('.'))+
    // ".decision.chercher&selectedId="
    // + viewBean.getIdTiers()
    // + "&selectedId2="
    // + viewBean.getIdAffiliation())
    // .forward(request, response);
    // }else if ("2".equalsIgnoreCase(viewBean.getCodeDirection())){
    // servlet
    // .getServletContext()
    // .getRequestDispatcher(
    // "/naos?userAction=naos.affiliation.chercher&idTiers="
    // + viewBean.getIdTiers())
    // .forward(request, response);
    // }
    // }
    // catch (Exception e) {
    // if (viewBean!=null){
    // viewBean.setMessage(e.getMessage());
    // viewBean.setMsgType(FWViewBeanInterface.ERROR);
    // }else{
    // servlet.getServletContext().getRequestDispatcher
    // (ERROR_PAGE).forward(request, response);
    // }
    // }
    // }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    // public void actionAfficher(HttpSession session, HttpServletRequest
    // request, HttpServletResponse response, FWDispatcher mainController)
    // throws ServletException, IOException {
    // // --- Get value from request
    // CPAutreDossierViewBean viewBean = new CPAutreDossierViewBean();
    // try {
    // BISession bSession =
    // globaz.phenix.translation.CodeSystem.getSession(session);
    // viewBean.setSession((BSession) bSession);
    // // --- Check view bean
    // session.setAttribute("viewBean", viewBean);
    // //HACK on change l'action pour éviter ACTION_NOUVEAU
    // getAction().changeActionPart(FWAction.ACTION_AFFICHER);
    // super.actionAfficher(session, request, response, mainController);
    //
    // } catch (Exception e) {
    // servlet.getServletContext().getRequestDispatcher (ERROR_PAGE).forward
    // (request, response);
    // }
    // }
    /**
     * Réimplémentation de la méthode update à cause du problème setProperties de l'action par défaut qui remet les
     * booléens à false Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void actionModifier(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // CPAutreDossierViewBean viewBean = null;
        // try {
        // // --- Variables
        // viewBean = (CPAutreDossierViewBean) session.getAttribute("viewBean");
        // if (viewBean == null) {
        // viewBean = new CPAutreDossierViewBean();
        // }
        // viewBean.setMsgType("");
        // globaz.globall.api.BISession bSession =
        // globaz.phenix.translation.CodeSystem.getSession(session);
        // globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        // viewBean.setSession((globaz.globall.db.BSession) bSession);
        // viewBean._controle();
        // FWHelper.afterExecute(viewBean);
        // //--- Check view bean
        // session.removeAttribute("viewBean");
        // session.setAttribute("viewBean", viewBean);
        //
        // if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true)
        // {
        // servlet
        // .getServletContext()
        // .getRequestDispatcher(
        // getRelativeURLwithoutClassPart(request, session)
        // + "autreDossier_de.jsp")
        // .forward(request, response);
        // } else if ("1".equalsIgnoreCase(viewBean.getCodeDirection())){
        // servlet
        // .getServletContext()
        // .getRequestDispatcher(
        // getActionFullURL().substring(
        // 0,
        // getActionFullURL().lastIndexOf('.'))
        // + ".decision.chercher&selectedId="
        // + viewBean.getIdTiers()
        // + "&selectedId2="
        // + viewBean.getIdAffiliation())
        // .forward(request, response);
        // }else if ("2".equalsIgnoreCase(viewBean.getCodeDirection())){
        // servlet
        // .getServletContext()
        // .getRequestDispatcher(
        // "/naos?userAction=naos.affiliation.chercher&idTiers="
        // + viewBean.getIdTiers())
        // .forward(request, response);
        // }
        // } catch (Exception e) {
        // if (viewBean!=null){
        // viewBean.setMessage(e.getMessage());
        // viewBean.setMsgType(FWViewBeanInterface.ERROR);
        // }else{
        // servlet.getServletContext().getRequestDispatcher (ERROR_PAGE).forward
        // (request, response);
        // }
        // }

        super.actionModifier(session, request, response, mainController);
    }
}
