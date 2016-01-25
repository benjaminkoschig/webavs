package globaz.pegasus.process.lot;

import globaz.hercule.utils.HerculeContainer;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.op.excelml.model.document.ExcelmlBuilder;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.pegasus.process.PCAbstractJob;
import java.io.FileInputStream;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.web.application.PCApplication;

public class PCGenererListeOrdresVersementProcess extends PCAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String MODEL_NAME = "listeOrdresVersement.xml";
    private String idLot = null;
    private String mail = null;

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdLot() {
        return idLot;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public ExcelmlWorkbook load(String model) throws Exception {
        String sourceFile = Jade.getInstance().getHomeDir() + PCApplication.APPLICATION_PEGASUS_REP + "/model/excelml/"
                + model;
        ExcelmlWorkbook wk = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(sourceFile);
            wk = ExcelmlBuilder.getDocument(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return wk;
    }

    public ExcelmlWorkbook mergeDocumentExcel(String modelName, String nomDoc, HerculeContainer container)
            throws Exception {
        ExcelmlWorkbook wk = null;
        nomDoc += "-" + JadeUUIDGenerator.createStringUUID() + ".xls";
        // On va charger le classeur
        wk = load(modelName);
        wk.mergeDocument(container);
        return wk;
        // On sauvegarde le document sur persistance avec un nom unique
        // return CEExcelmlUtils.save(wk, nomDoc);
    }

    @Override
    protected void process() throws Exception {
        try {

            // if (ordreVersementSearch.getSize() > 0) {

            // HerculeContainer container = PCXmlmlMappingOrdresVersment.loadResults(ordreVersementSearch);

            Object doc = PegasusServiceLocator.getListeDeControleService().createListeOrdreDeVersement(idLot);

            // On génère le doc
            String nomDoc = getSession().getLabel("LISTE_CONTROLE_ATTRIBUES_NOM_DOCUMENT");

            // ExcelmlWorkbook wk = this.mergeDocumentExcel(PCGenererListeOrdresVersementProcess.MODEL_NAME, "test",
            // container);

            // wk. /* String docPath =
            // CEExcelmlUtils.createDocumentExcel(this.getSession().getIdLangueISO().toUpperCase() + "/"
            // + PCGenererListeOrdresVersementProcess.MODEL_NAME, nomDoc, container);

            // Publication du document
            JadePublishDocumentInfo docInfo = JadePublishDocumentInfoProvider.newInstance(this);
            // JadePublishDocumentInfo.

            JadePrintDocumentContainer jadePrintDocumentContainer = new JadePrintDocumentContainer();

            // jadePrintDocumentContainer.addDocument(aDoc, aDest);

            docInfo.setOwnerEmail("dma@globaz.ch");

            docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, "dma@globaz.ch");
            docInfo.setApplicationDomain(PCApplication.DEFAULT_APPLICATION_PEGASUS);
            docInfo.setDocumentTitle(nomDoc);
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);

            // jadePrintDocumentContainer.addDocument(wk., docInfo);

            // jadePrintDocumentContainer.setMergedDocDestination(pubInfosDestination);

            this.createDocuments(jadePrintDocumentContainer);

            // this.registerAttachedDocument(docInfo, docPath);
            // }

        } catch (Exception e) {

            // this._addError(this.getTransaction(),
            // this.getSession().getLabel("EXECUTION_CONTROLES_ATTRIBUES_ERREUR"));
            // this._addError(this.getTransaction(), CEUtils.stack2string(e));
            /*
             * String messageInformation = "Annee de rattrapage : " + getAnnee() + "\n"; messageInformation +=
             * "GenreControle : " + getGenreControle() + "\n"; messageInformation += "VisaReviseur : " +
             * getVisaReviseur() + "\n"; messageInformation += "AEffectuer : " + getAEffectuer() + "\n";
             * messageInformation += "DejaEffectuer : " + getDejaEffectuer() + "\n"; messageInformation +=
             * CEUtils.stack2string(e);
             */

            // CEUtils.addMailInformationsError(this.getMemoryLog(), messageInformation, this.getClass().getName());

        }

    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

}
