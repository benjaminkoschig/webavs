package globaz.batch.osiris.bvrftp;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.bvrftp.CABvrFtpListViewBean;
import globaz.osiris.db.bvrftp.CABvrFtpViewBean;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.process.CAProcessBVR;
import globaz.osiris.process.journal.CAProcessComptabiliserJournal;
import globaz.osiris.utils.CADateUtil;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class CABvrFtpAutomaticDownload extends BProcess {

    private static final long serialVersionUID = 1L;
    private static String emailContent = "";
    private String idOrganeExecution = "";
    private String isTestEnvironnement = "";

    /**
     * Si le journal ne contient pas d'erreur, comptabiliser le.
     * 
     * @param session
     * @param idJournal
     * @throws Exception
     */
    private static void comptabiliser(BSession session, String idJournal) throws Exception {
        CAProcessComptabiliserJournal comptabiliser = new CAProcessComptabiliserJournal();
        comptabiliser.setSession(session);

        comptabiliser.setSendCompletionMail(false);
        comptabiliser.setSendMailOnError(false);

        comptabiliser.setImprimerJournal(new Boolean(false));

        comptabiliser.setIdJournal(idJournal);

        comptabiliser.executeProcess();
    }

    /**
     * Exécute la lecture et l'ajout des paiements BVR.
     * 
     * @param session
     * @param transaction
     * @param bvrFile
     * @param dateValeur
     * @param idOrganeExecution
     * @return
     * @throws Exception
     */
    private static List<String> executeBvr(BSession session, BTransaction transaction, File bvrFile, JADate dateValeur,
            String idOrganeExecution) throws Exception {
        CAProcessBVR processBvr = new CAProcessBVR();
        processBvr.setSession(session);

        processBvr.setSendCompletionMail(false);
        processBvr.setSendMailOnError(false);

        processBvr.setIdOrganeExecution(idOrganeExecution);
        processBvr.setSimulation(new Boolean(false));
        processBvr.setDateValeur(dateValeur.toStr("."));
        processBvr.setLibelle(session.getLabel("BVRFTP_AUTO_DOWNLOAD_LIBELLE") + " " + dateValeur.toStr("."));

        processBvr.setFileName(bvrFile.getName());
        processBvr.setRetrieveBvrFromDataBase(false);

        processBvr.executeProcess();

        if (session.hasErrors() || transaction.hasErrors()) {
            transaction.rollback();
            throw new Exception(session.getErrors().toString());
        } else {
            transaction.commit();
        }

        return processBvr.getIdJournauxBvr();
    }

    /**
     * Return le journal qui contient les BVRs.
     * 
     * @param session
     * @param transaction
     * @param idJournal
     * @return
     * @throws Exception
     */
    private static CAJournal getJournal(BSession session, BTransaction transaction, String idJournal) throws Exception {
        CAJournal journal = new CAJournal();
        journal.setSession(session);

        journal.setIdJournal(idJournal);

        journal.retrieve(transaction);

        return journal;
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        if (JadeStringUtil.isBlank(idOrganeExecution)) {
            System.err.println("L'organe d'exécution doit être renseigné.");
            return false;
        }

        CABvrFtpListViewBean ftpConnection = new CABvrFtpListViewBean();
        ftpConnection.setSession(getSession());

        if ((!JadeStringUtil.isBlank(isTestEnvironnement)) && ("true".equalsIgnoreCase(isTestEnvironnement))) {
            ftpConnection.setDistantDirectoryName("esr-bvr-pvr-t");
        } else {
            ftpConnection.setDistantDirectoryName("esr-bvr-pvr");
        }

        ftpConnection.listDirectoryFiles();

        for (int i = 0; i < ftpConnection.getPvrFiles().size(); i++) {
            CABvrFtpViewBean bvr = ftpConnection.getPvrFile(i);

            if (!bvr.isAlreadyDownloaded()) {
                File bvrFile = new File(ftpConnection.getFileFromFtp(bvr.getFileName()));
                JADate dateValeur = null;
                try {
                    dateValeur = new JADate(new BigDecimal(bvrFile.getName().substring(0, 8)));
                    if (JadeDateUtil.isGlobazDate(dateValeur.toStr("."))) {
                        JACalendar cal = getSession().getApplication().getCalendar();
                        dateValeur = CADateUtil.getDateOuvrable(cal.addDays(dateValeur, 1));

                        List<String> idJournaux = executeBvr(getSession(), getTransaction(), bvrFile, dateValeur,
                                idOrganeExecution);

                        emailContent = emailContent + getSession().getLabel("BVRFTP_AUTO_DOWNLOAD_LIBELLE") + " "
                                + dateValeur.toStr(".") + " "
                                + getSession().getLabel("BVRFTP_AUTO_DOWNLOAD_LIBELLE_TRAITES") + ".\n";
                        for (String idJournal : idJournaux) {
                            CAJournal journal = getJournal(getSession(), getTransaction(), idJournal);

                            if ((!journal.isNew()) && (journal.isOuvert())) {
                                comptabiliser(getSession(), idJournal);

                                emailContent = emailContent + getSession().getLabel("BVRFTP_AUTO_DOWNLOAD_JOURNAL")
                                        + " " + idJournal + " "
                                        + getSession().getLabel("BVRFTP_AUTO_DOWNLOAD_COMPTABILISE") + ".\n";
                            } else {
                                emailContent = emailContent + getSession().getLabel("BVRFTP_AUTO_DOWNLOAD_JOURNAL")
                                        + " " + idJournal + " " + getSession().getLabel("BVRFTP_AUTO_DOWNLOAD_MANUEL")
                                        + ".\n";
                            }
                        }
                    } else {
                        throw new Exception("Filename date is not valid" + bvrFile.getName().substring(0, 8));
                    }

                } catch (NumberFormatException e) {
                    emailContent = emailContent + "Fichier non traité : " + bvrFile.getName()
                            + " Format de la date incorrect.\n";
                    e.printStackTrace();
                }
            }
        }

        return (!isAborted()) && (!isOnError());
    }

    @Override
    protected String getEMailObject() {
        if (!isOnError()) {
            return "L'importation automatique des BVR s'est effectuée avec succès\n" + emailContent;
        }
        return "L'importation automatique des BVR a échoué\n" + emailContent;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public String getIsTestEnvironnement() {
        return isTestEnvironnement;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setIsTestEnvironnement(String isTestEnvironnement) {
        this.isTestEnvironnement = isTestEnvironnement;
    }
}
