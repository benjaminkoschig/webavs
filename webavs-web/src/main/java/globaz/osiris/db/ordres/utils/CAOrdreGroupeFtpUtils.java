package globaz.osiris.db.ordres.utils;

import globaz.ftp.FtpEnterprisedtWrapper;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.bvrftp.CABvrFtpListViewBean;
import globaz.osiris.db.bvrftp.log.CABvrFtpLog;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import java.io.IOException;
import org.slf4j.LoggerFactory;

public class CAOrdreGroupeFtpUtils {

    public static final String POSTFINANCE_FTP_OPAE_LSV_PREFIX = "dfsp";

    /**
     * La fonction ftp est-elle activée ?
     * 
     * @param organeExecution
     * @return
     * @throws Exception
     */
    public static boolean ftpFunctionActive(APIOrganeExecution organeExecution) throws Exception {
        if (organeExecution.getModeTransfert().equals(APIOrganeExecution.CS_BY_FTPPOST)) {
            CAApplication currentApplication = CAApplication.getApplicationOsiris();
            return (!JadeStringUtil.isBlank(currentApplication.getProperty(FtpEnterprisedtWrapper.FTP_HOST))
                    && !JadeStringUtil.isBlank(currentApplication.getProperty(FtpEnterprisedtWrapper.FTP_PORT)) && !JadeStringUtil
                        .isBlank(currentApplication.getProperty(FtpEnterprisedtWrapper.FTP_LOGIN)));
        } else {
            return false;
        }
    }

    /**
     * Return le nom de fichier a déposé sur le ftp de la poste.
     * 
     * @param dateEcheanche
     * @param numeroOG
     * @return
     * @throws JAException
     */
    public static String getFtpDestinationFileName(String dateEcheanche, String numeroOG) throws JAException {
        JADate tmpDate = new JADate(dateEcheanche);
        String destinationFileName = CAOrdreGroupeFtpUtils.POSTFINANCE_FTP_OPAE_LSV_PREFIX
                + JACalendar.format(tmpDate, JACalendar.FORMAT_YYYYMMDD) + numeroOG;
        return destinationFileName;
    }

    /**
     * Log l'activité du serveur Ftp respectivement le transfert du fichier OPAE sur serveur Ftp Postfinance.
     * 
     * @param session
     * @param destinationFileName
     * @param motif
     * @throws Exception
     */
    public static void logFtpActivity(BSession session, String destinationFileName, String motif) throws Exception {
        CABvrFtpLog log = new CABvrFtpLog();
        log.setSession(session);
        log.setFileName(destinationFileName);
        log.setCodeJournal(CABvrFtpLog.FILE_SEND);

        log.setMoreInformations(motif);

        log.add();

        if (log.hasErrors()) {
            throw new Exception(log.getErrors().toString());
        }
    }

    /**
     * Post le fichier OPAE sur le serveur FTP Postfinance.
     * 
     * @param session
     * @param typeOrdreGroupe
     * @param sourceFileLocation
     * @param destinationFileName
     * @return
     */
    public static boolean postFtp(BSession session, String typeOrdreGroupe, String sourceFileLocation,
            String destinationFileName) {
        try {
            CABvrFtpListViewBean ftp = new CABvrFtpListViewBean();
            ftp.setSession(session);

            if (typeOrdreGroupe.equals(CAOrdreGroupe.VERSEMENT)) {
                ftp.setDistantDirectoryName(CABvrFtpListViewBean.FTP_SEND_OPAE_SUB_DIRECTORY);
            } else if (typeOrdreGroupe.equals(CAOrdreGroupe.RECOUVREMENT)) {
                ftp.setDistantDirectoryName(CABvrFtpListViewBean.FTP_SEND_LSV_SUB_DIRECTORY);
            } else {
                return false;
            }

            if (!ftp.isFtpConnectionAvailable()) {
                session.addWarning(session.getLabel("SERVER_FTP_UNVAILABLE"));
                return false;
            } else {
                ftp.putFileToFtp(sourceFileLocation, destinationFileName);
                return true;
            }
        } catch (Exception e) {
            session.addError(e.getMessage());
            LoggerFactory.getLogger(CAOrdreGroupeFtpUtils.class.getName()).error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Si fichier d'échange, le transmettre en pièce jointe ou le déposer sur le ftp de la poste.
     * 
     * @param context
     * @param ordreGroupe
     * @param organeExecution
     * @param sourceFileName
     * @throws Exception
     * @throws JAException
     * @throws IOException
     */
    public static void sendOrRegisterFile(BProcess context, APIOrdreGroupe ordreGroupe,
            APIOrganeExecution organeExecution, String sourceFileName) throws Exception, JAException, IOException {
        if ((organeExecution.getGenre().equals(APIOrganeExecution.POSTE))
                && (CAOrdreGroupeFtpUtils.ftpFunctionActive(organeExecution))) {
            String destinationFileName = CAOrdreGroupeFtpUtils.getFtpDestinationFileName(ordreGroupe.getDateEcheance(),
                    ordreGroupe.getNumeroOG());

            if (CAOrdreGroupeFtpUtils.postFtp(context.getSession(), ordreGroupe.getTypeOrdreGroupe(), sourceFileName,
                    destinationFileName)) {
                CAOrdreGroupeFtpUtils.logFtpActivity(context.getSession(), destinationFileName, ordreGroupe.getMotif());
            } else {
                context.registerAttachedDocument(sourceFileName);
            }
        } else {
            context.registerAttachedDocument(sourceFileName);
        }
    }

}
