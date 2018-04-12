package globaz.orion.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.orion.mappingXmlml.EBXmlmlMappingListAdi;
import globaz.orion.utils.EBExcelmlUtils;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.List;
import ch.globaz.orion.business.models.adi.RecapDemandesTransmises;

public class EBImprimerListeDemandesTransmisesProcess extends BProcess {
    private static final long serialVersionUID = 1L;
    public static final String MODEL_NAME = "listeDemandeADI.xml";

    private List<RecapDemandesTransmises> listeRecapDemandesTransmises;

    @Override
    protected void _executeCleanUp() {
        // DO nothing
    }

    public void setListeRecapDemandesTransmises(List<RecapDemandesTransmises> listeRecapDemandesTransmises) {
        this.listeRecapDemandesTransmises = listeRecapDemandesTransmises;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        if (listeRecapDemandesTransmises == null || listeRecapDemandesTransmises.isEmpty()) {
            return true;
        }

        return createDocument(listeRecapDemandesTransmises, listeRecapDemandesTransmises.size());
    }

    private boolean createDocument(List<RecapDemandesTransmises> listeRecapDemandesTransmises, int nbCasTraites)
            throws Exception {

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("LISTE_DEMANDES_TRANSMISES");
        CommonExcelmlContainer container = EBXmlmlMappingListAdi.loadResults(listeRecapDemandesTransmises, this,
                nbCasTraites);
        String docPath = EBExcelmlUtils.createDocumentExcel(MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("PROCESS_ADI_KO");
        } else {
            return getSession().getLabel("PROCESS_ADI_OK");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

}
