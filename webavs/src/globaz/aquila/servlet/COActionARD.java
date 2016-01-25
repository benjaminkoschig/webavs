/*
 * Créé le 1 mars 06
 */
package globaz.aquila.servlet;

import globaz.aquila.db.ard.COARDViewBean;
import globaz.aquila.db.ard.CORechercheARD;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author sch
 */
public class COActionARD extends CODefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public COActionARD(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super._getDestAjouterSucces(session, request, response, viewBean) + "&selectedId="
                + ((COARDViewBean) viewBean).getIdCompteAnnexe();
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super._getDestModifierSucces(session, request, response, viewBean) + "&selectedId="
                + ((COARDViewBean) viewBean).getIdCompteAnnexe();
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return super._getDestSupprimerSucces(session, request, response, viewBean) + "&selectedId="
                + ((COARDViewBean) viewBean).getIdCompteAnnexe();
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // on va rechercher le numero de l'administrateur à partir de
        // l'idCompteAnnexe et le mettre dans un ViewBean
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();

        try {
            BSession sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                    .newSession();
            mainDispatcher.getSession().connectSession(sessionOsiris);
            compteAnnexe.setISession(sessionOsiris);
            compteAnnexe.setIdCompteAnnexe(request.getParameter("selectedId"));
            compteAnnexe.retrieve();
        } catch (Exception e) {
            // l'écran n'affichera rien.
        }

        CORechercheARD rechercheARD = new CORechercheARD();
        rechercheARD.setNumeroAdministrateur(compteAnnexe.getIdExterneRole());
        rechercheARD.setIdCompteAuxiliaire(compteAnnexe.getIdCompteAnnexe());
        rechercheARD.setNomAdministrateur(compteAnnexe.getTiers().getNom());
        rechercheARD.setISession(mainDispatcher.getSession());
        try {
            rechercheARD.setSocietes(compteAnnexe.getIdCompteAnnexe(), mainDispatcher.getSession());
        } catch (Exception e) {
            rechercheARD.setMessage(e.toString());
            session.setAttribute("viewBean", rechercheARD);
            servlet.getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }

        session.setAttribute("viewBean", rechercheARD);

        super.actionChercher(session, request, response, mainDispatcher);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((COARDViewBean) viewBean).setIdCompteAnnexe(request.getParameter("idCompteAnnexe"));

        return viewBean;
    }

}
