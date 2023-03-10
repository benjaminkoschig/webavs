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
 * Process pour l'edition des journaux de r�capitulation
 * 
 * @author fgo date de cr�ation : 3 juil. 06
 * @version : version 1.0
 */
public class TUJournalRecapF3ProcessOld extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String csAgence = "";
    private TUJournal journal = null;

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
     * R�cup�ration de l'ann�e
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * R�cup�ration du csAgence
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
        str.append(getSession().getLabel("PRO_TIT_JOURNAL_RECAP_F3"));
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
     * D�marrage du process
     * 
     * @param transaction
     * @return
     */
    public boolean process(BTransaction transaction) {
        boolean processValid = true;
        try {
            // le traitement � r�aliser
            journal = TUJournalGenerator.generate(transaction, getAnnee(), "", getCsAgence(),
                    ITUCSConstantes.CS_TY_CATEGORIE_RUBRIQUE_F001_2, TUJournal.TYPE_CHARGEMENT_AGENCE);
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
     * Modification de l'ann�e
     * 
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * Modification du csAgence
     * 
     * @param string
     */
    public void setCsAgence(String string) {
        csAgence = string;
    }

}
