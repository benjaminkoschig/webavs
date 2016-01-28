package globaz.lyra.process;

import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.List;

public abstract class LYAbstractListEcheanceProcess<T extends LYAbstractListGenerator> extends
        LYAbstractEcheanceProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private T listGenerator;

    public LYAbstractListEcheanceProcess() {
        super();
        this.listGenerator = null;
    }

    @Override
    protected void afterExecute() throws Exception {
        List<String> documentAttaches = new ArrayList<String>();
        for (JadePublishDocument document : (List<JadePublishDocument>) this.listGenerator.getAttachedDocuments()) {
            documentAttaches.add(document.getDocumentLocation());
        }
        String body = "";
        if (this.getListGenerator().getMemoryLog().hasMessages()) {
            body = this.getListGenerator().getMemoryLog().getMessagesInString();
        }
        if (documentAttaches.size() > 0) {
            JadeSmtpClient.getInstance().sendMail(getEmailAddress(), this.listGenerator.getEmailObject(), body,
                    documentAttaches.toArray(new String[0]));
        }
    }

    @Override
    protected final void beforeExecute() throws Exception {
        this.listGenerator = this.buildListGenerator();

        this.listGenerator.setSession(getSession());
        this.listGenerator.setEMailAddress(getEmailAddress());
        this.listGenerator.setIdEcheance(getIdEcheance());
        this.listGenerator.setIdLog(getIdLog());
        this.listGenerator.setMoisTraitement(getMoisTraitement());

        this.preparerListGenerator(this.listGenerator);
    }

    protected abstract T buildListGenerator() throws Exception;

    public T getListGenerator() {
        return this.listGenerator;
    }

    protected abstract void preparerListGenerator(T listGenerator) throws Exception;

    @Override
    protected void runProcess() throws Exception {
        this.listGenerator.executeProcess();
    }
}
