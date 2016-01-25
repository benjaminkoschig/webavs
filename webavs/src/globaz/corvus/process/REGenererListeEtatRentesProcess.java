package globaz.corvus.process;

import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.itext.REEtatRentesAdapter;
import globaz.corvus.itext.REListeEtatRentesPage1;
import globaz.corvus.itext.REListeEtatRentesPage2;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;

public class REGenererListeEtatRentesProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private REEtatRentesAdapter adapter;
    private String forMoisAnnee = "";

    public REGenererListeEtatRentesProcess() throws Exception {
        this(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS));
    }

    public REGenererListeEtatRentesProcess(BProcess parent) throws Exception {
        super(parent);
    }

    public REGenererListeEtatRentesProcess(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {

            // remplissage de l'adapter
            adapter = new REEtatRentesAdapter(getSession(), getForMoisAnnee());
            adapter.chargerParGenrePrestation();

            // Création des pages
            createPage1(adapter);
            createPage2(adapter);

            // Fusionne les documents ci-dessus
            fusionneDocuments();

            // Tester si abort
            if (isAborted()) {
                return false;
            }

        } catch (Exception e) {
            StringBuilder message = new StringBuilder();
            message.append(getSession().getLabel("LISTE_ETR_ERREUR_MAIL"));

            if (!JadeStringUtil.isBlank(e.getMessage())) {
                message.append(" : ").append(e.getMessage());
            }

            JadeLogger.error(this, message.toString());
            this._addError(message.toString());
            getMemoryLog().logMessage(message.toString(), FWViewBeanInterface.ERROR, null);
            return false;
        }

        return true;
    }

    private REListeEtatRentesPage1 createPage1(REEtatRentesAdapter adapter) throws FWIException, Exception {

        REListeEtatRentesPage1 page1 = new REListeEtatRentesPage1(this);
        page1.setSession(getSession());
        page1.setForMoisAnnee(getForMoisAnnee());
        page1.setAdapter(adapter);
        page1.executeProcess();

        return page1;
    }

    private REListeEtatRentesPage2 createPage2(REEtatRentesAdapter adapter) throws FWIException, Exception {

        REListeEtatRentesPage2 page2 = new REListeEtatRentesPage2(this);
        page2.setSession(getSession());
        page2.setForMoisAnnee(getForMoisAnnee());
        page2.setAdapter(adapter);
        page2.executeProcess();

        return page2;
    }

    private void fusionneDocuments() throws Exception {
        JadePublishDocumentInfo info = createDocumentInfo();
        info.setPublishDocument(true);
        info.setArchiveDocument(false);

        info.setDocumentType(IRENoDocumentInfoRom.LISTE_ETAT_RENTES);
        info.setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_ETAT_RENTES);

        this.mergePDF(info, true, 500, false, null);
    }

    @Override
    protected String getEMailObject() {
        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("LISTE_ETR_MAIL_OBJECT_KO");
        } else {
            return getSession().getLabel("LISTE_ETR_MAIL_OBJECT_OK");
        }
    }

    public String getForMoisAnnee() {
        return forMoisAnnee;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForMoisAnnee(String forMoisAnnee) {
        this.forMoisAnnee = forMoisAnnee;
    }
}
