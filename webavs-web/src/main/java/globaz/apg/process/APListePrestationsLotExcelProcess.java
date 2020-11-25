package globaz.apg.process;

import globaz.apg.ApgServiceLocator;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroit;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroitManager;
import globaz.apg.excel.APListePandemieControleExcel;
import globaz.apg.excel.APListePrestationsLotExcel;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWMessage;
import globaz.globall.db.*;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.List;

public class APListePrestationsLotExcelProcess extends BProcess {

    private String idLot;
    List<APPrestationJointLotTiersDroit> list;
    public APListePrestationsLotExcelProcess(){
        super();
    }
    public APListePrestationsLotExcelProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
            try {
                setSendMailOnError(true);
                list = createSource();
                if (!list.isEmpty()){
                    JadePublishDocumentInfo docInfoExcel = JadePublishDocumentInfoProvider.newInstance(this);
                    APListePrestationsLotExcel listeExcel = new APListePrestationsLotExcel(getSession());
                    listeExcel.setSumOPAE(getTotalOPAE(getIdLot()));
                    listeExcel.setList(list);
                    listeExcel.creerDocument();
                    docInfoExcel.setDocumentTypeNumber(IPRConstantesExternes.CONTROLE_PRESTATIONS_LOT_APG);
                    docInfoExcel.setPublishDocument(true);
                    docInfoExcel.setArchiveDocument(false);
                    JadePublishDocument documentAPublier = new JadePublishDocument(listeExcel.getOutputFile(),docInfoExcel);
                    this.registerAttachedDocument(documentAPublier);

                }
            } catch (Exception e) {
                _addError(getSession().getCurrentThreadTransaction(), e.getMessage());
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("LISTE_CONTROLE_TITLE_EXCEL"));
                return false;
            }
        return true;
    }

    private List<APPrestationJointLotTiersDroit> createSource() throws Exception {
        APPrestationJointLotTiersDroitManager manager = new APPrestationJointLotTiersDroitManager();
        manager.setSession(getSession());
        manager.setForIdLot(getIdLot());
        manager.setOrderBy(APPrestationJointLotTiersDroit.FIELDNAME_NOM + ","
                + APPrestationJointLotTiersDroit.FIELDNAME_PRENOM + "," + APPrestation.FIELDNAME_IDDROIT + ","
                + APPrestation.FIELDNAME_IDPRESTATIONAPG);
        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        return manager.getContainerAsList();
    }

    private String getTotalOPAE(String idLot) throws Exception {
        return ApgServiceLocator.getLotService().getTotauxOPAE(getSession(), idLot);
    }

    @Override
    protected String getEMailObject() {
        String  objectMail = getSession().getLabel("LISTE_CONTROLE_TITLE_EXCEL");
        objectMail = objectMail.replace("{0}", idLot);
        return objectMail;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public String getIdLot() {
        return idLot;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public List<APPrestationJointLotTiersDroit> getList() {
        return list;
    }

    public void setList(List<APPrestationJointLotTiersDroit> list) {
        this.list = list;
    }

}
