package globaz.orion.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.orion.mappingXmlml.EBXmlmlMappingListeAcl;
import globaz.orion.utils.EBExcelmlUtils;
import globaz.orion.utils.OrionContainer;
import ch.globaz.orion.EBApplication;
import ch.globaz.orion.business.models.acl.Acl;

public class EBImprimerListeAcl extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Constantes
    public final static String MODEL_NAME = "listeAcl.xml";
    private final static String NUMERO_REFERENCE_INFOROM = "0286CEB";

    // Attributs
    private Acl[] acl = null;

    public EBImprimerListeAcl() {
        super();
        /*
         * Acl[] newAcl = new Acl[3]; Acl acl1 = new Acl(); Acl acl2 = new Acl(); Acl acl3 = new Acl();
         * 
         * acl1.setNumeroAssure("1"); acl1.setDateEngagement("01.01.2010");
         * 
         * acl2.setNumeroAssure("7564333932276"); acl2.setDateEngagement("02.02.2010");
         * 
         * acl3.setNumeroAssure("7561335886668"); acl3.setDateEngagement("03.03.2010");
         * 
         * newAcl[0] = acl1; newAcl[1] = acl2; newAcl[2] = acl3;
         * 
         * this.acl = newAcl;
         */
    }

    // Constructeur
    public EBImprimerListeAcl(Acl[] acl) {
        super();
        this.acl = acl;
    }

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

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
        OrionContainer container = EBXmlmlMappingListeAcl.loadResults(acl, this);

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("LISTE_ACL_NOM_DOCUMENT");
        String docPath = EBExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + EBImprimerListeAcl.MODEL_NAME, nomDoc, container);

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

    /**
     * getter
     */

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

    /**
     * setter
     */

    public void setAcl(Acl[] newAcl) {
        acl = newAcl;
    }
}
