package globaz.lyra.process;

import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.List;

public abstract class LYAbstractDocumentEcheanceProcess<T extends LYAbstractDocumentGenerator> extends
        LYAbstractEcheanceProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private T documentGenerator;

    public LYAbstractDocumentEcheanceProcess() {
        super();
        this.documentGenerator = null;
    }

    @Override
    protected void afterExecute() throws Exception {
        List<String> documentAttaches = new ArrayList<String>();
        for (JadePublishDocument document : (List<JadePublishDocument>) this.documentGenerator.getAttachedDocuments()) {
            documentAttaches.add(document.getDocumentLocation());
        }
        if (documentAttaches.size() > 0) {
            JadeSmtpClient.getInstance().sendMail(getEmailAddress(), this.documentGenerator.getEmailObject(), "",
                    documentAttaches.toArray(new String[0]));
        }
    }

    @Override
    protected final void beforeExecute() throws Exception {
        this.documentGenerator = this.buildDocumentGenerator();

        this.documentGenerator.setSession(getSession());
        this.documentGenerator.setEMailAddress(getEmailAddress());
        this.documentGenerator.setMoisTraitement(getMoisTraitement());
        this.documentGenerator.setIdEcheance(getIdEcheance());
        this.documentGenerator.setIdLog(getIdLog());

        this.preparerDocumentGenerator(this.documentGenerator);
    }

    protected abstract T buildDocumentGenerator() throws Exception;

    public T getDocumentGenerator() {
        return this.documentGenerator;
    }

    protected abstract void preparerDocumentGenerator(T documentGenerator) throws Exception;

    @Override
    protected void runProcess() throws Exception {
        this.documentGenerator.executeProcess();
    }
}
