package globaz.aquila.process.batch.utils;

import globaz.aquila.util.COJournalAdapter;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

public class COJournalAdapterBatch {

    private String dateValeur;
    private COJournalAdapter journal;
    private String libelle;

    private boolean previsionnel = false;

    public COJournalAdapterBatch(String libelle, String dateValeur, boolean previsionnel) {
        this.libelle = libelle;
        this.dateValeur = dateValeur;
        this.previsionnel = previsionnel;
    }

    /**
     * 
     * @param session
     * @param transaction
     * @return le journal
     * @throws Exception
     */
    public COJournalAdapter getJournal(BSession session, BTransaction transaction) throws Exception {
        if (journal == null) {
            journal = new COJournalAdapter(session);
            journal.creerJournal(transaction, previsionnel, dateValeur, libelle);
        }

        return journal;
    }

    /**
     * Return le journal adapter créé par le batch. <br/>
     * Utilisé par process principal, si != null alors le journal sera comptabilisé.
     * 
     * @return le journal adapter créé par le batch.
     */
    public COJournalAdapter getJournalAdapter() {
        return journal;
    }

    /**
     * Return le libellé du journal
     * 
     * @return le libellé du journal
     */
    public String getLibelle() {
        return libelle;
    }
}
