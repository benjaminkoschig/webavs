package globaz.aquila.servlet;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.application.COApplication;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxFactory;
import globaz.aquila.db.access.poursuite.COContentieuxManager;
import globaz.aquila.service.COServiceLocator;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * <p>
 * Action de gestion des historiques.
 * </p>
 * 
 * @author vre
 */
public class COActionHistorique extends CODefaultServletAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Creates a new COActionHistorique object.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public COActionHistorique(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String libSequence = request.getParameter("libSequence");
        BSession appSession = (BSession) mainDispatcher.getSession();

        if (JadeStringUtil.isEmpty(libSequence)) {
            /*
             * on a pas de param�tre libSequence quand on vient depuis Osiris, par contre on a un idSection qu'on va
             * utiliser pour cr�er un contentieux bidon en session pour pouvoir afficher les �crans
             */
            actionChercherLienDirectOsiris(session, appSession, request.getParameter("idSection"));
        } else {
            // sinon, on pr�pare l'affichage de l'�cran
            preparerActionChercher(session, request, appSession);
        }

        super.actionChercher(session, request, response, mainDispatcher);
    }

    /**
     * Recherche le contentieux pour la section depuis la page de laquelle on est arriv� ici.
     * <p>
     * Si le contentieux n'existe pas encore, sauve dans la session, un contentieux ayant un identifiant
     * {@link COContentieux#ID_CONTENTIEUX_BIDON d�termin�}. De cette fa�on, l'utilisateur va pouvoir naviguer dans
     * Aquila pour cr�er un nouveau contentieux. Celui-ci pourra �tre sauvegard� de deux mani�res, soit par le bouton
     * valider ({@link COActionPoursuite}), soit il sera sauvegard� dans l'{@link COActionBatch action batch}, juste
     * avant d'ex�cuter la transition.
     * </p>
     * 
     * @param session
     * @param appSession
     * @param idSection
     * @throws ServletException
     */
    private void actionChercherLienDirectOsiris(HttpSession session, BSession appSession, String idSection)
            throws ServletException {
        try {
            // Recherche le contentieux
            COContentieux contentieux = null;
            COContentieuxManager manager = new COContentieuxManager();
            manager.setSession(appSession);
            manager.setForIdSection(idSection);
            manager.find();
            if (manager.size() == 1) {
                contentieux = (COContentieux) manager.getEntity(0);
            } else if (manager.size() < 1) {
                // On met � jour la section et le compte annexe
                BSession remoteSession = (BSession) ((COApplication) appSession.getApplication())
                        .getSessionOsiris(appSession);

                // charger la section
                CASection section = new CASection();

                section.setSession(remoteSession);
                section.setIdSection(idSection);
                section.retrieve();

                // charger le compte annexe
                CACompteAnnexe compteAnnexe = new CACompteAnnexe();

                compteAnnexe.setSession(remoteSession);
                compteAnnexe.setIdCompteAnnexe(section.getIdCompteAnnexe());
                compteAnnexe.retrieve();

                // on cr�e un contentieux bidon
                contentieux = COContentieuxFactory.creerNouveauContentieux(appSession, section, compteAnnexe);

                // On assigne des id externes inexistantes
                contentieux.setIdContentieux(COContentieux.ID_CONTENTIEUX_BIDON);

                // Charge une transition pour calculer la prochaine date de
                // d�clenchement.
                COTransition transition = loadTransition(appSession, contentieux.getIdSequence());
                contentieux.setProchaineDateDeclenchement(transition.calculerDateProchainDeclenchement(contentieux));
                COServiceLocator.getTaxeService().initMontantsTaxes(appSession, contentieux);
            } else if (manager.size() > 1) {
                throw new Exception("Plus d'un contentieux trouv� pour idSection=" + idSection);
            }

            // On met le contentieux trouv� en session
            session.setAttribute("contentieuxViewBean", contentieux.loadContentieuxViewBean());
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * Charge une transition pour calculer la prochaine date de d�clenchement.
     * 
     * @param idSequence
     * @return
     * @throws Exception
     */
    private COTransition loadTransition(BSession appSession, String idSequence) throws Exception {

        COTransition transition = null;
        // rechercher l'�tape de cr�ation pour cette s�quence
        COEtapeManager etapeManager = new COEtapeManager();

        etapeManager.setForLibEtape(ICOEtape.CS_CONTENTIEUX_CREE);
        etapeManager.setForIdSequence(idSequence);
        etapeManager.setSession(appSession);
        etapeManager.find();

        if (etapeManager.size() != 1) {
            throw new Exception("L'�tape de cr�ation du contentieux pour la s�quence " + idSequence
                    + " n'est pas configur�e correctement");
        }

        // rechercher la transition pour cette �tape
        COTransitionManager transitionManager = new COTransitionManager();

        transitionManager.setSession(appSession);
        transitionManager.setForIdEtape(((COEtape) etapeManager.get(0)).getIdEtape());
        transitionManager.find();

        if (transitionManager.size() == 1) {
            transition = (COTransition) transitionManager.get(0);
        } else {
            throw new Exception("L'�tape de cr�ation du contentieux pour la s�quence " + idSequence
                    + " n'est pas configur�e correctement");
        }

        return transition;
    }

    /**
     * Pr�pare la session pour pouvoir afficher l'�cran de recherche des historiques d'�tapes correctement.
     * <p>
     * Un cartouche d'information s'affiche sur l'�cran de recherche des historiques d'�tapes, cette m�thode charge les
     * infos n�cessaires � son affichage.
     * </p>
     * 
     * @param session
     * @param request
     * @param appSession
     */
    private void preparerActionChercher(HttpSession session, HttpServletRequest request, BSession appSession) {
        chargerContentieux(request, session, appSession);
    }
}
