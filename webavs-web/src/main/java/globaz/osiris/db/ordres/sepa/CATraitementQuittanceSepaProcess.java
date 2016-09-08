package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import java.io.FileInputStream;

public class CATraitementQuittanceSepaProcess extends BProcess {

    /**
     * globaz.osiris.db.ordres.sepa.CATraitementQuittanceSepaProcess
     */
    private static final long serialVersionUID = -7053310867121692418L;

    private String filePath;
    private String body = "ended";
    private String title = "CATraitementQuittanceSepaProcess";

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        SepaAcknowledgementProcessor processor = new SepaAcknowledgementProcessor();
        // processor.findAndProcessAllAcknowledgements(getSession());
        processor.setSession(getSession());
        processor.processAcknowledgement(new FileInputStream(filePath));
        title = processor.getTitle();
        body = processor.getBody();
        return true;
    }

    @Override
    protected String getEMailObject() {
        return title;
    }

    @Override
    public String getSubjectDetail() {
        return body;
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

}
