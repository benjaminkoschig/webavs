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
 * Classe de base pour les services liés aux transitions.
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
     * Crée un {@link COJournalAdapter} standard de type journalier (à la date du jour), non prévisionnel.
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @return
     * @throws Exception
     */
    protected COJournalAdapter createJournal(BSession session, BTransaction transaction, COContentieux contentieux)
            throws Exception {
        // créer un journal journalier, le libellé importe peu
        COJournalAdapter journal = new COJournalAdapter(session, contentieux);

        journal.creerJournal(transaction, false, JACalendar.todayJJsMMsAAAA(), "aquila - journalier");

        return journal;
    }

    /**
     * Met à jour la section dans Osiris avec la dernière étape effectuée.
     * 
     * @param transaction
     * @param contentieux
     * @param libEtape
     * @throws Exception
     */
    protected void updateSection(BTransaction transaction, COContentieux contentieux, String libEtape) throws Exception {
        // met à jour la section
        contentieux.getSection().setIdLastEtatAquila(libEtape);
        contentieux.getSection().update(transaction);
    }
}
