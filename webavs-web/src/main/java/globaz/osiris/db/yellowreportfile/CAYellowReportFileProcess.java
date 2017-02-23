package globaz.osiris.db.yellowreportfile;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.ordres.sepa.AbstractSepa;
import globaz.osiris.db.ordres.sepa.CACamt054DefinitionType;
import globaz.osiris.db.ordres.sepa.CACamt054Processor;
import globaz.osiris.db.ordres.sepa.exceptions.CAYellowReportFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import ch.globaz.common.properties.PropertiesException;

/**
 * Processus permettant d'être d'aller récupérer des fichiers dans un serveur FTP de PostFinance et créer en DB les
 * documents identifiés (selon critères)
 * 
 * @author dcl
 * 
 */
public class CAYellowReportFileProcess extends BProcess {

    private static final long serialVersionUID = 8356579821532864914L;
    private CAYellowReportFileService service;
    private CACamt054Processor serviceFtp;

    @Override
    protected void _executeCleanUp() {
        // Nothing to do
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        initParameters();

        // Nous recherchons tous les fichiers CashManagment déposés sur le serveur FTP PostFinance (dossier physique)
        final List<String> files = serviceFtp.getListFiles();

        // Nous recherchons tous les anciens fichiers du FTP PostFinance déjà traités (enregistrement DB)
        final Set<String> identifiedFiles = getDBIdentifiedFile();

        for (final String file : files) {
            // Nous passons au suivant s'il a déjà été identifié une fois
            if (identifiedFiles.contains(file)) {
                continue;
            }

            ByteArrayOutputStream baos = null;
            ByteArrayInputStream bais = null;
            try {
                baos = new ByteArrayOutputStream();

                // Nous recherchons le fichier XML physique
                serviceFtp.retrieveFile(file, baos);

                bais = new ByteArrayInputStream(baos.toByteArray());
                Document document = AbstractSepa.parseDocument(bais);

                // Nous allons persisté en DB les XML qui sont accepté par l'application
                // Actuellement, nous acceptons que le type CAMT054 BVR
                manageAcceptedCamt054Type(CACamt054DefinitionType.CAMT054_BVR, document, file, baos.toByteArray());

                // Nous créons dans tous les cas en DB dans une table des fichiers identifié par le processus afin de
                // ne plus les retraiter plus tard (gain de performance)
                createIdentifiedFile(file);

                // Pour chaque fichier traité, si tout s'est bien passé nous commitons ce qui a été fait pour lui.
                getSession().getCurrentThreadTransaction().commit();
            } catch (Exception exception) {
                final String informative = "Error for file : " + file + " " + exception.getMessage();

                JadeLogger.error(exception, informative);
                Logger.getLogger(CAYellowReportFileProcess.class.getName()).log(Level.SEVERE, informative,
                        exception.getCause());

                // Pour chaque fichier traité, si une erreur et apparue nous rollbackons ce qui a été fait pour lui.
                getSession().getCurrentThreadTransaction().rollback();
            } finally {
                IOUtils.closeQuietly(baos);
                IOUtils.closeQuietly(bais);
            }
        }

        return true;
    }

    private void createIdentifiedFile(final String fileName) throws Exception {
        CAYellowReportIdentifiedFile identifiedFile = new CAYellowReportIdentifiedFile();
        identifiedFile.setSession(getSession());
        identifiedFile.setFileName(fileName);
        identifiedFile.setDateCreated(new Date());
        identifiedFile.add(getSession().getCurrentThreadTransaction());
    }

    private Set<String> getDBIdentifiedFile() throws CAYellowReportFileException {
        Set<String> fileNames = new HashSet<String>();

        try {
            CAYellowReportIdentifiedFileManager manager = new CAYellowReportIdentifiedFileManager();
            manager.setSession(getSession());
            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.getSize(); i++) {
                fileNames.add(((CAYellowReportIdentifiedFile) manager.get(i)).getFileName());
            }

            return fileNames;
        } catch (Exception e) {
            throw new CAYellowReportFileException("Problem during the research of yellow report files : "
                    + e.getMessage());
        }
    }

    private void manageAcceptedCamt054Type(final CACamt054DefinitionType type, final Document document,
            final String fileName, final byte[] content) throws Exception {

        if (serviceFtp.isCamt054AndWantedType(type, document)) {
            // Nous créons une identification de fichier Yellow Report
            service.create(fileName, CAYellowReportFileType.getYellowReportTypeFromLinkedType(type), content);
        }
    }

    private void initParameters() throws PropertiesException {
        setSendCompletionMail(false);
        service = new CAYellowReportFileService(getSession());
        serviceFtp = new CACamt054Processor();
    }

    @Override
    protected String getEMailObject() {
        return "";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

}
