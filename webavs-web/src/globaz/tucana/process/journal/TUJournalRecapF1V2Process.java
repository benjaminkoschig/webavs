package globaz.tucana.process.journal;

import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.common.JadeCodingUtil;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.statistiques.TUJournal;
import globaz.tucana.statistiques.TUJournalGenerator;
import globaz.tucana.transaction.TUTransactionHandler;

/**
 * Process pour l'edition des journaux de récapitulation
 * 
 * @author fgo date de création : 3 juil. 06
 * @version : version 1.0
 */
public class TUJournalRecapF1V2Process extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String csAgence = new String();
    private TUJournal journal = null;
    private String mois = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        boolean succeed = true;
        TUTransactionHandler transactionHandler = new TUTransactionHandler(getSession()) {
            @Override
            protected void handleBean(BTransaction transaction) throws Exception {
                if (process(transaction)) {
                    ;
                }
                transaction.commit();
            }
        };

        try {
            transactionHandler.execute();
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "process", e);
            succeed = false;
        }
        return succeed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);

    }

    /**
     * Récupération de l'année
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Récupération de l'id code système agence
     * 
     * @return
     */
    public String getCsAgence() {
        return csAgence;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        StringBuffer str = new StringBuffer();
        str.append(getSession().getLabel("PRO_TIT_JOURNAL_RECAP_F1V2"));
        return str.toString();
    }

    /**
     * Modification du journal
     * 
     * @return
     */
    public TUJournal getJournal() {
        return journal;
    }

    /**
     * Récupération du mois
     * 
     * @return
     */
    public String getMois() {
        return mois;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param transaction
     * @return
     */
    public boolean process(BTransaction transaction) {
        boolean processValid = true;
        try {
            // le traitement à réaliser
            journal = TUJournalGenerator.generate(transaction, getAnnee(), getMois(), getCsAgence(),
                    ITUCSConstantes.CS_TY_CATEGORIE_RUBRIQUE_F001_2, TUJournal.TYPE_CHARGEMENT_CANTON);

        } catch (Exception e) {
            try {
                e.printStackTrace();
                transaction.rollback();
            } catch (Exception e1) {
                JadeCodingUtil.catchException(this, "process", e1);
            }
            JadeCodingUtil.catchException(this, "process", e);
            processValid = false;
        }
        return processValid;
    }

    /**
     * Modification de l'année
     * 
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * Modification de l'id code système agence
     * 
     * @param newCsAgence
     */
    public void setCsAgence(String newCsAgence) {
        csAgence = newCsAgence;
    }

    /**
     * Modification du mois
     * 
     * @param string
     */
    public void setMois(String string) {
        mois = string;
    }
}
