package globaz.aquila.service.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.util.COJournalAdapter;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COCancelTransitionService extends COAbstractTransitionService {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Annule l'étape donnée du contentieux donné.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     *            le contentieux pour lequel annuler une étape
     * @param historiqueAnnule
     *            l'historique à annuler
     * @param annulerEcritures
     *            vrai pour annuler les écritures dans la comptabilité
     * @throws Exception
     */
    public void annulerDerniereTransition(BSession session, BTransaction transaction, COContentieux contentieux,
            COHistorique historiqueAnnule, boolean annulerEcritures, boolean idExtourne) throws Exception {
        // String idEtapeSuivante = historiqueAnnule.getIdEtape();
        String idHistoriquePrecedant = historiqueAnnule.getIdHistoriquePrecedant();

        // charger l'avant-derniere ligne de l'historique
        COHistorique historiqueARetablir = new COHistorique();

        historiqueARetablir.setIdHistorique(idHistoriquePrecedant);
        historiqueARetablir.setSession(session);
        historiqueARetablir.retrieve();

        if (historiqueARetablir.isNew()) {
            // si l'historique n'existe pas, on a rien à annuler
            return;
        }

        // annuler la derniere transition
        // COTransitionManager transitions = new COTransitionManager();

        // transitions.setForIdEtape(historiqueARetablir.getIdEtape());

        // if
        // (ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION.equals(historiqueAnnule.getEtape().getLibEtape()))
        // {
        // COEtapeManager etapes = new COEtapeManager();
        //
        // etapes.setSession(session);
        // etapes.setForLibEtape(ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_SANS_OPPOSITION);
        // etapes.find();
        //
        // if (etapes.isEmpty()) {
        // transaction.addErrors("??");
        // } else {
        // transitions.setForIdEtapeSuivante(((COEtape)
        // etapes.get(0)).getIdEtape());
        // }
        // } else if
        // (ICOEtape.CS_TYPE_SPECIFIQUE.equals(historiqueARetablir.getEtape().getTypeEtape()))
        // {
        // // On ne fait rien
        // } else {
        // transitions.setForIdEtapeSuivante(idEtapeSuivante);
        // }

        // transitions.setSession(session);
        // transitions.find();
        //
        // if (transitions.isEmpty()) {
        // // il ne peut y avoir aucune transition
        // transaction.addErrors(transaction.getSession().getLabel("AQUILA_ERR_TRANSITION_EMPTY"));
        // } else if (transitions.size() > 1) {
        // // ni plus d'une transition
        // transaction.addErrors(transaction.getSession().getLabel("AQUILA_ERR_TRANSITION_SIZE"));
        // }

        if (!transaction.hasErrors()) {
            // sinon on lance l'annulation
            // COTransition transition = (COTransition) transitions.get(0);
            // COTransitionAction action =
            // COServiceLocator.getActionService().getTransitionAction(transition);

            // action.setAnnulerEcritures(annulerEcritures);
            this.annulerDerniereTransition(session, transaction, contentieux, historiqueAnnule, historiqueARetablir,
                    annulerEcritures, idExtourne);
        }
    }

    /**
     * Annule la dernière transition.
     * <p>
     * Lance l'annulation sur la transition puis met à jour l'historique et les infos par étape.
     * </p>
     * 
     * @param session
     * @param transaction
     * @param contentieux
     *            le contentieux pour lequel annuler l'historique
     * @param historiqueAnnule
     *            l'historique annulé
     * @param historiqueRetabli
     *            l'historique rétabli (normalement, celui lié dans l'historique annulé)
     * @param action
     *            l'action de transition qui lie les deux historiques
     * @throws Exception
     */
    public void annulerDerniereTransition(BSession session, BTransaction transaction, COContentieux contentieux,
            COHistorique historiqueAnnule, COHistorique historiqueRetabli, boolean annulerEcritures, boolean extourner)
            throws Exception {
        // annuler les modifications specifiques qui ont ete apportees par cette
        // action
        // remet le contentieux dans son état antérieur
        contentieux.setIdEtape(historiqueRetabli.getIdEtape());
        contentieux.setProchaineDateDeclenchement(contentieux.getDateDeclenchement());
        contentieux.setDateDeclenchement(historiqueRetabli.getDateDeclenchement());
        contentieux.setDateExecution(historiqueRetabli.getDateExecution());

        /*
         * annuler les opérations dans Osiris. Attention ! Le processus de comptabilisation de Osiris va mettre à jour
         * la section et le compte annexe dans un contexte transactionnel mal défini ! Tenter de modifier la section ou
         * le compte annexe après l'appel de cette méthode entraînera presque certainement un dead-lock !
         */
        if (annulerEcritures) {
            COJournalAdapter.annulerEcritures(contentieux.getSession(), transaction, contentieux, historiqueAnnule,
                    extourner);

            if (transaction.hasErrors()) {
                // l'annulation des écritures est en erreur, on interromp
                // l'annulation
                return;
            }

            // la désactivation des opérations met à jour la section donc il
            // faut recharger.
            contentieux.getSection().retrieve();
        }

        // mettre à jour le contentieux
        contentieux.update(transaction);

        // met à jour la section avec la dernière étape effectuée
        updateSection(transaction, contentieux, historiqueRetabli.getEtape().getLibEtape());

        // met à jour les éléments liés dans le contentieux
        contentieux.refreshLinks(transaction);
    }
}
