package globaz.tucana.process.journal;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.common.JadeCodingUtil;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.process.TUMessageAttach;
import globaz.tucana.process.message.TUMessagesContainer;
import globaz.tucana.statistiques.TUJournal;
import globaz.tucana.statistiques.TUJournalGenerator;
import globaz.tucana.transaction.TUTransactionHandler;
import java.io.IOException;
import java.util.Calendar;

/**
 * Classe process lancant la récap F1-002 en annexant un fichier csv au courriel
 * 
 * @author fgo date de création : 13.09.2006
 * @version : version 1.0
 */
public class TUJournalRecapF1V2CSVProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = null;
    private String csAgence = new String();
    private TUJournal journal = null;
    private String mois = null;

    /**
	 * 
	 */
    public TUJournalRecapF1V2CSVProcess() {
        super();
    }

    /**
     * @param parent
     */
    public TUJournalRecapF1V2CSVProcess(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public TUJournalRecapF1V2CSVProcess(BSession session) {
        super(session);
    }

    /**
	 * 
	 */
    public TUJournalRecapF1V2CSVProcess(String _annee, String _mois) {
        super();
        annee = _annee;
        mois = _mois;
    }

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
                getMemoryLog().logMessage(getSession().getLabel("PROCESS_SUCCES"), FWMessage.INFORMATION, "-->");
                transaction.commit();
            }
        };

        try {
            transactionHandler.execute();
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "process", e);
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_ERROR"), FWViewBeanInterface.ERROR, "-->");

            getMemoryLog().logMessage(e.getMessage(), FWViewBeanInterface.ERROR, "-->");
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
        if (getMemoryLog().isOnErrorLevel()) {
            str = new StringBuffer(getSession().getLabel("PRO_TIT_EXPORTATION_LISTE_ERROR"));
        } else {
            str = new StringBuffer(getSession().getLabel("PRO_TIT_EXPORTATION_LISTE"));
        }

        str.append(" : ").append(getMois()).append(".").append(getAnnee()).append(" - (");
        str.append(JACalendar.todayJJsMMsAAAA()).append(")");
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
     * Lancement du process
     * 
     * @param transaction
     * @return
     */
    public boolean process(BTransaction transaction) {
        boolean processValid = true;

        getMemoryLog().logMessage(Calendar.getInstance().getTime().toString(), FWMessage.INFORMATION,
                transaction.getSession().getLabel("INFO_DEBUT"));

        TUMessagesContainer messages = new TUMessagesContainer();

        // le traitement à réaliser
        try {
            journal = TUJournalGenerator.generate(transaction, getAnnee(), getMois(), getCsAgence(),
                    ITUCSConstantes.CS_TY_CATEGORIE_RUBRIQUE_F001_2, TUJournal.TYPE_CHARGEMENT_CANTON);
            // préparation du fichier csv
            StringBuffer csvString = new StringBuffer();
            journal.toCsv(csvString);
            registerAttachedDocument(TUMessageAttach.build(csvString, TUMessageAttach.EXTENTION_CSV));

        } catch (Exception e) {
            messages.addMessage("Classe : " + e.getClass().getName() + " erreur : " + e.toString(), FWMessage.ERREUR,
                    "Erreur");
            processValid = false;
            try {
                transaction.rollback();
            } catch (Exception e1) {
                JadeCodingUtil.catchException(this, "process", e1);
            }
            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.WARNING, "! - ");
        } finally {
            try {
                if (!messages.isEmpty()) {
                    registerAttachedDocument(TUMessageAttach.build(messages, TUMessageAttach.EXTENTION_TXT));
                }
            } catch (IOException e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "--> ");
                JadeCodingUtil.catchException(this, "process", e);
                processValid = false;
            }

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
