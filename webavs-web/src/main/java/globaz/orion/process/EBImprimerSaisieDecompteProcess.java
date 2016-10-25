package globaz.orion.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.orion.mappingXmlml.EBXmlmlRecapSaisieDecompte;
import globaz.orion.utils.EBExcelmlUtils;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.List;
import ch.globaz.orion.business.models.sdd.RecapSaisieDecompte;

public class EBImprimerSaisieDecompteProcess extends BProcess {

    private static final long serialVersionUID = 1L;
    public static final String MODEL_NAME = "listeRecapSaisieDecompte.xml";
    public static final String NUMERO_INFOROM = "0302CAF";

    public static final String NUM_AFFILIE = "NUM_AFFILIE";
    public static final String NOM = "NOM";
    public static final String LOCALITE = "LOCALITE";
    public static final String PERIODE = "PERIODE";
    public static final String TYPE = "TYPE";
    public static final String SAISIE_LE = "SAISIE_LE";
    public static final String ERREUR = "ERREUR";

    private List<RecapSaisieDecompte> listeRecapSaisieDecompte;

    @Override
    protected void _executeCleanUp() {
        // DO nothing
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        if (listeRecapSaisieDecompte == null || listeRecapSaisieDecompte.isEmpty()) {
            return true;
        }

        return createDocument(listeRecapSaisieDecompte, listeRecapSaisieDecompte.size());
    }

    private boolean createDocument(List<RecapSaisieDecompte> listeRecapSaisieDecompte, int nbCasTraites)
            throws Exception {

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("LISTE_SAISIE_DECOMPTE");
        CommonExcelmlContainer container = EBXmlmlRecapSaisieDecompte.loadResults(listeRecapSaisieDecompte, this,
                nbCasTraites);
        String docPath = EBExcelmlUtils.createDocumentExcel(EBImprimerSaisieDecompteProcess.MODEL_NAME, nomDoc,
                container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(EBImprimerSaisieDecompteProcess.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("SAISIE_DECOMPTE_KO");
        } else {
            return getSession().getLabel("SAISIE_DECOMPTE_OK");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setListeRecapSaisieDecompte(List<RecapSaisieDecompte> listeRecapSaisieDecompte) {
        this.listeRecapSaisieDecompte = listeRecapSaisieDecompte;
    }

}
