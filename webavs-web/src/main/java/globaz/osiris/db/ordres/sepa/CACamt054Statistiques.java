package globaz.osiris.db.ordres.sepa;

public class CACamt054Statistiques {
    private int totTransactionErrors;
    private int nbTxErrors;
    private int nbTxValidated;
    private int nbTxTreated;

    public CACamt054Statistiques() {
        totTransactionErrors = 0;
        nbTxErrors = 0;
        nbTxValidated = 0;
        nbTxTreated = 0;
    }

    public void initStatGroupTransaction() {
        nbTxErrors = 0;
        nbTxValidated = 0;
        nbTxTreated = 0;
    }

    public void incTotTransactionErrors() {
        totTransactionErrors++;
    }

    public void incNbTxErrors() {
        nbTxErrors++;
    }

    public void incNbTxValidated() {
        nbTxValidated++;
    }

    public void incNbTxTreated() {
        nbTxTreated++;
    }

    public int getNbTxErrors() {
        return nbTxErrors;
    }

    public int getNbTxTreated() {
        return nbTxTreated;
    }

    public int getNbTxValidated() {
        return nbTxValidated;
    }

    public int getTotTransactionErrors() {
        return totTransactionErrors;
    }
}
