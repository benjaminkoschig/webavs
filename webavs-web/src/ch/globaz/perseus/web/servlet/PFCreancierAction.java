package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.perseus.vb.creancier.PFCreancierViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controller des actions du package "perseus.creancier"
 * 
 * @author mbo
 */
public class PFCreancierAction extends PFAbstractDefaultServletAction {
    private String idDemande = null;

    /**
     * @param aServlet
     */
    public PFCreancierAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFCreancierViewBean) {
            PFCreancierViewBean vb = (PFCreancierViewBean) viewBean;
            vb.setIdDemande(request.getParameter("idDemande"));

            return super.beforeAfficher(session, request, response, vb);
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    /*
     * @Override protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
     * HttpServletResponse response, FWViewBeanInterface viewBean) { if (viewBean instanceof PFCreanceAccordeeViewBean)
     * { return "/" + this.getAction().getApplicationPart() + ".chercher&idDemande=" +
     * "?userAction=perseus.creancier.creanceAccordee.reAfficher&creancierSearchModel.forIdDemande=" +
     * ((PFCreanceAccordeeViewBean) viewBean).getIdDemande(); } else if (viewBean instanceof PFCreancierViewBean) {
     * return this.getActionFullURL() + ".chercher&idDemande=" + ((PFCreancierViewBean) viewBean).getIdDemande(); } else
     * { return super._getDestAjouterEchec(session, request, response, viewBean); }
     * 
     * }
     * 
     * 
     * 
     * @Override protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
     * HttpServletResponse response, FWViewBeanInterface viewBean) { if (viewBean instanceof PFCreanceAccordeeViewBean)
     * { return "/" + this.getAction().getApplicationPart() + ".chercher&idDemande=" +
     * "?userAction=perseus.creancier.creanceAccordee.reAfficher&creancierSearchModel.forIdDemande=" +
     * ((PFCreanceAccordeeViewBean) viewBean).getIdDemande(); } else if (viewBean instanceof PFCreancierViewBean) {
     * return this.getActionFullURL() + ".chercher&idDemande=" + ((PFCreancierViewBean) viewBean).getIdDemande(); } else
     * { return super._getDestModifierEchec(session, request, response, viewBean); } }
     * 
     * @Override protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
     * HttpServletResponse response, FWViewBeanInterface viewBean) { if (viewBean instanceof PFCreanceAccordeeViewBean)
     * { PFCreanceAccordeeViewBean vb = (PFCreanceAccordeeViewBean) viewBean; return this.getActionFullURL() +
     * ".chercher&idDemande=" + request.getParameter("idDemande"); } else if (viewBean instanceof PFCreancierViewBean) {
     * // Ne rafraichit pas la rc_liste dans le navigateur // return this.getActionFullURL() + ".afficher&selectedId=" +
     * ((PFCreancierViewBean) viewBean).getId() // + "&idDemande=" + ((PFCreancierViewBean) viewBean).getIdDemande(); //
     * Rafraichit la rc_liste dans le navigateur return this.getActionFullURL() + ".chercher&idDemande=" +
     * ((PFCreancierViewBean) viewBean).getIdDemande(); } return super._getDestModifierSucces(session, request,
     * response, viewBean); }
     * 
     * @Override protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
     * HttpServletResponse response, FWViewBeanInterface viewBean) { if (viewBean instanceof PFCreancierViewBean) {
     * PFCreancierViewBean vb = (PFCreancierViewBean) viewBean;
     * 
     * return this.getActionFullURL() + ".chercher&idDemande=" + vb.getDemande().getId(); }
     * 
     * return super._getDestSupprimerSucces(session, request, response, viewBean); }
     * 
     * @Override protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
     * HttpServletResponse response, FWViewBeanInterface viewBean) { this.setIdDemandByViewBean(viewBean);
     * this.setListCreanceAccordees(request, viewBean); return super.beforeAjouter(session, request, response,
     * viewBean); }
     * 
     * @Override protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
     * HttpServletResponse response, FWViewBeanInterface viewBean) { this.setIdDemandByViewBean(viewBean);
     * this.setListCreanceAccordees(request, viewBean); return super.beforeModifier(session, request, response,
     * viewBean); }
     * 
     * @Override protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
     * HttpServletResponse response, FWViewBeanInterface viewBean) { this.setIdDemandByViewBean(viewBean); return
     * super.beforeSupprimer(session, request, response, viewBean); }
     * 
     * @Override public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
     * FWController mainController) throws ServletException, IOException { this.idDemande =
     * request.getParameter("idDemande"); super.doAction(session, request, response, mainController); }
     * 
     * 
     * // @Override // protected void goSendRedirect(String url, HttpServletRequest request, HttpServletResponse
     * response) // throws ServletException, IOException { // if (!JadeStringUtil.isEmpty(this.idDemande)) { // url =
     * url + "&idDemande=" + this.idDemande; // } // super.goSendRedirect(url, request, response); // }
     * 
     * private void setIdDemandByViewBean(FWViewBeanInterface viewBean) { if (viewBean instanceof PFCreancierViewBean) {
     * this.idDemande = ((PFCreancierViewBean) viewBean).getCreancier().getSimpleCreancier().getIdDemande(); } else if
     * (viewBean instanceof PFCreanceAccordeeViewBean) { this.idDemande = ((PFCreanceAccordeeViewBean)
     * viewBean).getIdDemande(); } }
     * 
     * private void setListCreanceAccordees(HttpServletRequest request, FWViewBeanInterface viewBean) { if (viewBean
     * instanceof PFCreanceAccordeeViewBean) { List<SimpleCreanceAccordee> list = new
     * ArrayList<SimpleCreanceAccordee>(); SimpleCreanceAccordee sAccordee = null;
     * 
     * Map<String, String[]> map = request.getParameterMap(); Set<Map.Entry<String, String[]>> set = map.entrySet();
     * 
     * Iterator<Entry<String, String[]>> iter = set.iterator(); while (iter.hasNext()) { Map.Entry<String, String[]>
     * entree = iter.next(); String[] ids = entree.getKey().split("_"); if (ids[0].equals("creanceAcc")) { sAccordee =
     * new SimpleCreanceAccordee(); sAccordee.setIdCreancier(ids[1]); sAccordee.setIdPcfAccordee(ids[2]); if (ids.length
     * == 4) { sAccordee.setId(ids[3]); } String t = entree.getValue().toString();
     * sAccordee.setMontantAccorde(entree.getValue()[0]);
     * 
     * list.add(sAccordee); } } ((PFCreanceAccordeeViewBean) viewBean).setListCreanceAccordees(list); } }
     */

}
