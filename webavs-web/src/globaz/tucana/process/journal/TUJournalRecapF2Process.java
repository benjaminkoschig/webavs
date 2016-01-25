package globaz.tucana.process.journal;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeCodingUtil;
import globaz.tucana.application.TUApplication;
import globaz.tucana.constantes.IPropertiesNames;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.exception.process.TUInitStatistiquesConfigException;
import globaz.tucana.process.TUMessageAttach;
import globaz.tucana.statistiques.TUJournal;
import globaz.tucana.statistiques.TUJournalGenerator;
import globaz.tucana.transaction.TUTransactionHandler;
import globaz.tucana.vb.journal.TUJournalRecapF2ViewBean;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Process pour l'edition des journaux de récapitulation
 * 
 * @author fgo date de création : 13.09.2007
 * @version : version 1.0
 */
public class TUJournalRecapF2Process extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Retourne le nom d'un fichier
     * 
     * @param extension
     * @return
     */
    private static StringBuffer nomFichier(String extension) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss");
        StringBuffer sauvegardeTxt = new StringBuffer(Jade.getInstance().getHomeDir()).append("logs/")
                .append(format.format(Calendar.getInstance().getTime())).append(".").append(extension);
        return sauvegardeTxt;
    }

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
     * Récupération de l'année
     * 
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * Récupère le code système de l'agence
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
        str.append(getSession().getLabel("PRO_TIT_JOURNAL_RECAP_F2"));
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
     * @return
     * @throws TUInitStatistiquesConfigException
     */
    private String nomFichierXsl() throws TUInitStatistiquesConfigException {
        try {
            return Jade
                    .getInstance()
                    .getHomeDir()
                    .concat(TUApplication.DEFAULT_APPLICATION_ROOT)
                    .concat("/xsl/")
                    .concat(GlobazSystem.getApplication(TUApplication.DEFAULT_APPLICATION_TUCANA).getProperty(
                            IPropertiesNames.STATISTIQUES_XSL_F2));
        } catch (RemoteException e) {
            throw new TUInitStatistiquesConfigException(getClass().getName().concat("nomfichierXsl"), e);
        } catch (Exception e) {
            throw new TUInitStatistiquesConfigException(getClass().getName().concat("nomfichierXsl"), e);
        }

    }

    /**
     * @param transaction
     * @return
     */
    public boolean process(BTransaction transaction) {
        boolean processValid = true;
        // FileWriter writer = null;
        try {
            // le traitement à réaliser
            journal = TUJournalGenerator.generate(transaction, getAnnee(), "", getCsAgence(),
                    ITUCSConstantes.CS_TY_CATEGORIE_RUBRIQUE_F002, TUJournal.TYPE_CHARGEMENT_MOIS);
            // writer = transformFile();
            registerAttachedDocument(transformFile());
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
     * Modifie le code système de l'agence
     * 
     * @param csAgence
     */
    public void setCsAgence(String csAgence) {
        this.csAgence = csAgence;
    }

    private String transformFile() throws TUInitStatistiquesConfigException {
        FileWriter writer;
        TUJournalRecapF2ViewBean bean = new TUJournalRecapF2ViewBean();
        bean.setJournal(journal);
        String nomFichier = nomFichier(TUMessageAttach.EXTENTION_XLS).toString();
        try {
            writer = new FileWriter(nomFichier);
            TransformerFactory.newInstance().newTransformer(new StreamSource(nomFichierXsl()))
                    .transform(new DOMSource(bean.getXmlDocument()), new StreamResult(writer));
            writer.close();
        } catch (TransformerConfigurationException e) {
            throw new TUInitStatistiquesConfigException(getClass().getName().concat("transformFile()"), e);
        } catch (IOException e) {
            throw new TUInitStatistiquesConfigException(getClass().getName().concat("transformFile()"), e);
        } catch (TransformerException e) {
            throw new TUInitStatistiquesConfigException(getClass().getName().concat("transformFile()"), e);
        } catch (TransformerFactoryConfigurationError e) {
            throw new TUInitStatistiquesConfigException(getClass().getName().concat("transformFile()"), e);
        }
        return nomFichier;
    }
}
