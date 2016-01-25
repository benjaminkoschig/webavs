package globaz.lynx.servlet.recherche;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.lynx.db.recherche.LXRechercheGenerale;
import globaz.lynx.db.recherche.LXRechercheGeneraleManager;
import globaz.lynx.db.recherche.LXRechercheGeneraleViewBean;
import globaz.lynx.utils.LXConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXRechercheDetailAction extends FWDefaultServletAction {

    private static final String JSP_DETAIL_RC = "_rc.jsp";

    /*
     * Cosntructeur
     */
    public LXRechercheDetailAction(FWServlet servlet) {
        super(servlet);
    }

    protected void actionChercherDetail(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {
            LXRechercheGeneraleViewBean viewBean = new LXRechercheGeneraleViewBean();

            LXRechercheGeneraleManager manager = new LXRechercheGeneraleManager();
            manager.setSession((BSession) mainDispatcher.getSession());
            manager.setForIdSection(request.getParameter("forIdSection"));
            manager.setForEtat(LXConstants.ETAT_DEFINITIF);
            manager.find();

            if (manager.size() > 0) {
                LXRechercheGenerale rech = (LXRechercheGenerale) manager.getFirstEntity();

                viewBean.setSession(rech.getSession());
                viewBean.setIdFournisseur(rech.getIdFournisseur());
                viewBean.setIdSociete(rech.getIdSociete());
                viewBean.setBase(rech.getBase());
                viewBean.setMouvement(rech.getMouvement());
                viewBean.setSolde(rech.getSolde());
                viewBean.setIdExterne(rech.getIdExterne());
                viewBean.setIdExterneFournisseur(rech.getIdExterneFournisseur());
                viewBean.setCsTypeSection(rech.getCsTypeSection());
                viewBean.setNom(rech.getNom());
                viewBean.setComplement(rech.getComplement());
            }

            manager = new LXRechercheGeneraleManager();
            manager.setSession((BSession) mainDispatcher.getSession());
            manager.setForIdSection(request.getParameter("forIdSection"));
            manager.setForEtat(LXConstants.ETAT_PROVISOIRE);
            manager.find();

            if (manager.size() > 0) {
                LXRechercheGenerale rech = (LXRechercheGenerale) manager.getFirstEntity();

                viewBean.setSession(rech.getSession());
                viewBean.setIdFournisseur(rech.getIdFournisseur());
                viewBean.setIdSociete(rech.getIdSociete());
                viewBean.setBaseProvisoire(rech.getBase());
                viewBean.setMouvementProvisoire(rech.getMouvement());
                viewBean.setSoldeProvisoire(rech.getSolde());
                viewBean.setIdExterne(rech.getIdExterne());
                viewBean.setIdExterneFournisseur(rech.getIdExterneFournisseur());
                viewBean.setCsTypeSection(rech.getCsTypeSection());
                viewBean.setNom(rech.getNom());
                viewBean.setComplement(rech.getComplement());
            }

            setSessionAttribute(session, "viewBean", viewBean);
            destination = getRelativeURL(request, session) + JSP_DETAIL_RC;
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {

        if ("chercherDetail".equals(getAction().getActionPart())) {
            actionChercherDetail(session, request, response, mainDispatcher);
        }
    }

}
