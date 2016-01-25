package globaz.tucana.process.transfert;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.fs.JadeFsFacade;
import globaz.tucana.exception.process.TUProcessException;
import globaz.tucana.transfert.TUImportHandler;
import java.io.FileInputStream;

/**
 * Process pour l'importation du bordereau
 * 
 * @author fgo date de création : 25.06.2006
 * @version : version 1.0
 * 
 */
public class TUImportationProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int nbrErreur = 0;
    private String source = "";

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
        // boolean succeed = true;
        // TUTransactionHandler transactionHandler = new
        // TUTransactionHandler(getSession()) {
        // protected void handleBean(BTransaction transaction) throws Exception
        // {
        // if (process(transaction))
        // ;
        // transaction.commit();
        // }
        // };
        //
        // try {
        // transactionHandler.execute();
        // } catch (Exception e) {
        // JadeCodingUtil.catchException(this, "process", e);
        // succeed = false;
        // }
        return process();
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
     * 
     * @param sourceFileName
     * @return
     * @throws Exception
     */
    private FileInputStream getDocument(String sourceFileName) throws Exception {
        JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + sourceFileName, Jade
                .getInstance().getHomeDir() + "persistence/" + sourceFileName);

        // return JadeXmlReader.parseFile(new
        // FileInputStream(Jade.getInstance().getHomeDir() + "work/" +
        // sourceFileName));
        return new FileInputStream(Jade.getInstance().getHomeDir() + "persistence/" + sourceFileName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel(nbrErreur > 0 ? "PRO_TIT_IMPORTATION_ERROR" : "PRO_TIT_IMPORTATION");
    }

    /**
     * Récupération de la source
     * 
     * @return
     */
    public String getSource() {
        return source;
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
    public boolean process() {
        // public boolean process(BTransaction transaction) {
        boolean processValid = true;
        // BTransaction transaction = getTransaction();
        try {
            // vérifie que le numéro de passage a ete saisi
            if (JadeStringUtil.isEmpty(getSource())) {
                getMemoryLog().logMessage(getTransaction().getSession().getLabel("ERR_PATH_VIDE"), FWMessage.ERREUR,
                        "--> ");
                JadeCodingUtil.catchException(this, "process", new TUProcessException(getTransaction().getSession()
                        .getLabel("ERR_PATH_VIDE")));
                processValid = false;
                getTransaction().addErrors(getTransaction().getSession().getLabel("ERR_PATH_VIDE"));
                nbrErreur++;
                throw new TUProcessException(this + " : "
                        + new TUProcessException(getTransaction().getSession().getLabel("ERR_PATH_VIDE")));
            } else {
                // le traitement à réaliser

                TUImportHandler.importation(new BTransaction(getSession()),
                        getDocument(JadeFilenameUtil.extractFilename(getSource())));

                getMemoryLog().logMessage(getSession().getLabel("PROCESS_SUCCES"), FWMessage.INFORMATION,
                        getSession().getLabel("PRO_TIT_IMPORTATION"));
            }
        } catch (Exception e) {
            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                JadeCodingUtil.catchException(this, "process", e1);
                nbrErreur++;
            }
            nbrErreur++;
            getMemoryLog().logMessage(getSession().getLabel("PROCESS_ERROR"), FWMessage.ERREUR,
                    getSession().getLabel("PRO_TIT_IMPORTATION_ERROR"));
            JadeCodingUtil.catchException(this, "process", e);
            processValid = false;
        }
        return processValid;
    }

    /**
     * Modification de la source
     * 
     * @param newSource
     */
    public void setSource(String newSource) {
        source = newSource;
    }
}
