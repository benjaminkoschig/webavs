package globaz.aquila.externe;

import globaz.aquila.api.ICOGestionContentieuxExterne;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.transition.COTransitionAction;
import globaz.aquila.db.access.batch.transition.COTransitionException;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COContentieuxManager;
import globaz.aquila.process.batch.utils.COProcessContentieuxUtils;
import globaz.aquila.process.batch.utils.COTransitionUtil;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.util.COJournalAdapter;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

public class COGestionContentieuxExterne implements ICOGestionContentieuxExterne {

    /**
     * Créer le contentieux à partir de la section.
     * 
     * @param session
     * @param transaction
     * @param section
     * @param transition
     * @return
     * @throws COTransitionException
     * @throws Exception
     */
    private COContentieux createContentieux(BSession session, BTransaction transaction, APISection section,
            COTransition transition) throws COTransitionException, Exception {
        COContentieux contentieux = new COContentieux();
        contentieux.setSession(session);

        // TODO sel : 31 oct. 2008 getSoldeInitial
        contentieux.setMontantInitial(((CASection) section).getSolde());

        contentieux.setIdCompteAnnexe(((CASection) section).getIdCompteAnnexe());
        contentieux.setIdSection(((CASection) section).getIdSection());
        contentieux.setIdSequence(((CASection) section).getIdSequenceContentieux());

        contentieux.setDateExecution(JACalendar.todayJJsMMsAAAA());
        contentieux.setDateOuverture(JACalendar.todayJJsMMsAAAA());
        contentieux.setDateDeclenchement(((CASection) section).getDateEcheance());

        // Charge le compte annexe pour pouvoir calculer la date de prochain
        // declenchement
        contentieux.setCompteAnnexe((CACompteAnnexe) ((CASection) section).getCompteAnnexe());

        // Charge la section pour pouvoir calculer la date de prochain
        // declenchement
        contentieux.setSection(((CASection) section));

        contentieux.setProchaineDateDeclenchement(transition.calculerDateProchainDeclenchement(contentieux));
        contentieux.setIdEtape(transition.getIdEtapeSuivante());
        contentieux.setUser(session.getUserName());

        contentieux.add(transaction);

        if (contentieux.hasErrors()) {
            throw new Exception(contentieux.getErrors().toString());
        }

        if (contentieux.isNew()) {
            throw new Exception(session.getLabel("IMPOSSIBLE_CREER_CONTENTIEUX"));
        }

        return contentieux;
    }

    /**
     * Ajoute l'opération contentieuse dans un journal journalier de comptabilité auxiliaire.
     * 
     * @param session
     * @param transaction
     * @param transition
     * @param contentieux
     * @return le journal
     * @throws Exception
     */
    private COJournalAdapter createJournal(BSession session, BTransaction transaction, COTransition transition,
            COContentieux contentieux) throws Exception {
        COJournalAdapter journal = new COJournalAdapter(session, contentieux);
        journal.creerJournal(transaction, false, JACalendar.todayJJsMMsAAAA(), "aquila - journalier");

        return journal;
    }

    /**
     * @see ICOGestionContentieuxExterne#creerContentieux(BSession, BTransaction, APISection)
     */
    @Override
    public void creerContentieux(BSession session, BTransaction transaction, APISection section) throws Exception {
        if (estDejaAuContentieux(session, transaction, section)) {
            return;
        }

        COTransitionUtil transitionUtil = new COTransitionUtil(session, transaction,
                ((CASection) section).getIdSequenceContentieux());

        COTransition transition = transitionUtil.getTransitionForCreerCtx();

        COContentieux contentieux = createContentieux(session, transaction, section, transition);

        COJournalAdapter journal = createJournal(session, transaction, transition, contentieux);

        COTransitionAction action = COServiceLocator.getActionService().getTransitionAction(transition);
        action.setDateExecution(JACalendar.todayJJsMMsAAAA());

        if (action != null) {
            action.canExecute(contentieux, transaction);
            action.setTaxes(COProcessContentieuxUtils.getTaxes(session, contentieux, transition, action));

            COServiceLocator.getTransitionService().executerTransition(session, transaction, contentieux, action,
                    journal);
        }

    }

    /**
     * @see ICOGestionContentieuxExterne#decalerEcheance(BSession, BTransaction, APISection, int)
     */
    @Override
    public void decalerEcheance(BSession session, BTransaction transaction, APISection section, int nombreJours)
            throws Exception {
        COContentieuxManager manager = findContentieux(session, transaction, section);

        if (manager.isEmpty() || (manager.size() > 1)) {
            throw new Exception(session.getLabel("ERR_RECHERCHE_CONTENTIEUX"));
        }

        COContentieux contentieux = (COContentieux) manager.getFirstEntity();

        JACalendarGregorian calendar = new JACalendarGregorian();
        String dateRep = calendar.addDays(section.getDateEcheance(), nombreJours);
        if (calendar.compare(dateRep, contentieux.getProchaineDateDeclenchement()) == JACalendar.COMPARE_FIRSTUPPER) {
            contentieux.setProchaineDateDeclenchement(dateRep);
            contentieux.update(transaction);
        }
    }

    /**
     * Return true si la section est déjà au contentieux ?
     * 
     * @param session
     * @param transaction
     * @param section
     * @throws Exception
     */
    private boolean estDejaAuContentieux(BSession session, BTransaction transaction, APISection section)
            throws Exception {
        COContentieuxManager manager = findContentieux(session, transaction, section);

        return !manager.isEmpty();
    }

    /**
     * Recherche les contentieux pour la section.
     * 
     * @param session
     * @param transaction
     * @param section
     * @return
     * @throws Exception
     */
    private COContentieuxManager findContentieux(BSession session, BTransaction transaction, APISection section)
            throws Exception {
        COContentieuxManager manager = new COContentieuxManager();
        manager.setSession(session);

        manager.setForIdSection(section.getIdSection());

        manager.find(transaction);

        return manager;
    }
}
