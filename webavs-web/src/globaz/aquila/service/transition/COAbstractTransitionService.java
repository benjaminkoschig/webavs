package globaz.aquila.service.transition;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.aquila.util.COJournalAdapter;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;

/**
 * <H1>Description</H1>
 * <p>
 * Classe de base pour les services li�s aux transitions.
 * </p>
 * 
 * @author vre
 */
public abstract class COAbstractTransitionService {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajoute une ligne d'historique.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @param motif
     * @param idEtape
     * @param idHistoriquePrecedente
     * @param journal
     * @return
     * @throws Exception
     */
    protected COHistorique addHistorique(BSession session, BTransaction transaction, COContentieux contentieux,
            String motif, String idEtape, String idHistoriquePrecedente, COJournalAdapter journal) throws Exception {
        COHistoriqueService service = COServiceLocator.getHistoriqueService();
        COHistorique historique = service.ajoutHistorique(session, transaction, contentieux, motif, idEtape,
                idHistoriquePrecedente, journal);

        return historique;
    }

    /**
     * Cr�e un {@link COJournalAdapter} standard de type journalier (� la date du jour), non pr�visionnel.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @return
     * @throws Exception
     */
    protected COJournalAdapter createJournal(BSession session, BTransaction transaction, COContentieux contentieux)
            throws Exception {
        // cr�er un journal journalier, le libell� importe peu
        COJournalAdapter journal = new COJournalAdapter(session, contentieux);

        journal.creerJournal(transaction, false, JACalendar.todayJJsMMsAAAA(), "aquila - journalier");

        return journal;
    }

    /**
     * Met � jour la section dans Osiris avec la derni�re �tape effectu�e.
     * 
     * @param transaction
     * @param contentieux
     * @param libEtape
     * @throws Exception
     */
    protected void updateSection(BTransaction transaction, COContentieux contentieux, String libEtape) throws Exception {
        // met � jour la section
        contentieux.getSection().setIdLastEtatAquila(libEtape);
        contentieux.getSection().update(transaction);
    }
}
