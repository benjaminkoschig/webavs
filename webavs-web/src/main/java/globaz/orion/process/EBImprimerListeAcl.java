package globaz.orion.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.orion.mappingXmlml.EBXmlmlMappingListeAcl;
import globaz.orion.utils.EBExcelmlUtils;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.Locale;
import ch.globaz.orion.EBApplication;
import ch.globaz.orion.business.models.acl.Acl;

public class EBImprimerListeAcl extends BProcess {

    private static final long serialVersionUID = 1L;

    public static final String MODEL_NAME = "listeAcl.xml";
    private static final String NUMERO_REFERENCE_INFOROM = "0286CEB";

    // Attributs
    private Acl[] acl;

    public EBImprimerListeAcl() {
        super();
    }

    // Constructeur
    public EBImprimerListeAcl(Acl[] acl) {
        super();
        this.acl = acl;
    }

    @Override
    protected void _executeCleanUp() {
        // Do nothing
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        if (acl.length >= 1) {
            return createDocument();
        }

        return false;
    }

    private boolean createDocument() throws Exception {
        setProgressScaleValue(acl.length);

        if (isAborted()) {
            return false;
        }

        CommonExcelmlContainer container = EBXmlmlMappingListeAcl.loadResults(acl, this);

        String nomDoc = getSession().getLabel("LISTE_ACL_NOM_DOCUMENT");
        String docPath = EBExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase(Locale.ENGLISH)
                + "/" + EBImprimerListeAcl.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(EBApplication.APPLICATION_ID);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setDocumentTypeNumber(EBImprimerListeAcl.NUMERO_REFERENCE_INFOROM);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    public Acl[] getAcl() {
        return acl;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_LISTE_ACL_ERROR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_LISTE_ACL");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAcl(Acl[] newAcl) {
        acl = newAcl;
    }
}
