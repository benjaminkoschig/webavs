package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.sepa.AbstractSepa.SepaException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.six_interbank_clearing.de.pain_002_001_03_ch_02.Document;

public class CATraitementQuittanceSepaProcess extends BProcess {
    private static final Logger LOG = LoggerFactory.getLogger(CATraitementQuittanceSepaProcess.class);

    /**
     * globaz.osiris.db.ordres.sepa.CATraitementQuittanceSepaProcess
     */
    private static final long serialVersionUID = -7053310867121692418L;

    private String filePath;
    private String folderPath;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    private static final class DocAndFile {
        Document doc;
        File file;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        setSendCompletionMail(false);
        SepaAcknowledgementProcessor processor = new SepaAcknowledgementProcessor();
        if (filePath == null && folderPath == null) {
            processor.findAndProcessAllAcknowledgements(getSession());
        } else {

            processor.setSession(getSession());
            if (StringUtils.isNotBlank(filePath)) {
                processor.processAcknowledgement(new FileInputStream(filePath));
            } else if (StringUtils.isNotBlank(folderPath)) {
                // process the full folder;
                File folder = new File(folderPath);

                List<DocAndFile> allPains = new ArrayList<DocAndFile>();
                Set<CAOrdreGroupe> ogProcessed = new HashSet<CAOrdreGroupe>();
                for (File f : folder.listFiles((FilenameFilter) new SuffixFileFilter(".xml", IOCase.INSENSITIVE))) {
                    Document pain002 = parsePain002(f);

                    if (pain002 != null) {
                        DocAndFile doc = new DocAndFile();
                        doc.doc = pain002;
                        doc.file = f;
                        allPains.add(doc);
                    }
                }

                // trier les pains par date de traitement, pour les traiter séquentiellement
                Collections.sort(allPains, new Comparator<DocAndFile>() {
                    @Override
                    public int compare(DocAndFile o1, DocAndFile o2) {
                        if (o1 == o2) {
                            return 0;
                        }

                        return o1.doc.getCstmrPmtStsRpt().getGrpHdr().getCreDtTm()
                                .compare(o2.doc.getCstmrPmtStsRpt().getGrpHdr().getCreDtTm());
                    }
                });

                for (DocAndFile daf : allPains) {
                    try {
                        ogProcessed.add(processor.processAcknowledgement(daf.doc));
                        FileUtils.deleteQuietly(daf.file);
                    } catch (SepaException e) {
                        LOG.warn("unable to process acknowledgement {}: {}", daf.file, e, e);
                    }
                }
                CAListOrdreRejeteProcess listORProcess = new CAListOrdreRejeteProcess();
                listORProcess.addMail(getEMailAddress());
                for (CAOrdreGroupe ogTraite : ogProcessed) {
                    listORProcess.process(getSession(), ogTraite);
                }

            }
        }

        return true;
    }

    private Document parsePain002(File f) {
        FileInputStream stream = null;

        try {
            stream = new FileInputStream(f);
            return AbstractSepa.unmarshall(AbstractSepa.parseDocument(stream), Document.class);
        } catch (Exception e) {
            JadeLogger.warn(this, "unable to process file " + f);
        } finally {
            IOUtils.closeQuietly(stream);
        }

        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

}
