package globaz.orion.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.orion.mappingXmlml.EBXmlmlMappingListeInscription;
import globaz.orion.utils.EBExcelmlUtils;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.Locale;
import ch.globaz.orion.EBApplication;
import ch.globaz.orion.business.models.inscription.InscriptionEbusiness;

public class EBImprimerListeInscription extends BProcess {

    private static final long serialVersionUID = 1L;
    private static final String MODEL_NAME = "listeInscription.xml";
    private static final String NUMERO_REFERENCE_INFOROM = "0287CEB";

    /**
     * Tableaux des inscriptions saisies.
     */
    private InscriptionEbusiness[] inscriptions;

    public EBImprimerListeInscription() {
        super();
    }

    public EBImprimerListeInscription(InscriptionEbusiness[] inscriptions) {
        super();
        this.inscriptions = inscriptions;
    }

    @Override
    protected void _executeCleanUp() {
        // not used !
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        if (inscriptions.length >= 1) {
            return createDocument();
        }
        return false;
    }

    /**
     * @return
     * @throws Exception
     */
    private boolean createDocument() throws Exception {
        setProgressScaleValue(inscriptions.length);

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("LISTE_INSCRIPTION_NOM_DOCUMENT");
        CommonExcelmlContainer container = EBXmlmlMappingListeInscription.loadResults(inscriptions, this);

        String docPath = EBExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase(Locale.ENGLISH)
                + "/" + EBImprimerListeInscription.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(EBApplication.APPLICATION_ID);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setDocumentTypeNumber(EBImprimerListeInscription.NUMERO_REFERENCE_INFOROM);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("EMAIL_OBJECT_LISTE_INSCRIPTION_ERROR");
        } else {
            return getSession().getLabel("EMAIL_OBJECT_LISTE_INSCRIPTION");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

}
