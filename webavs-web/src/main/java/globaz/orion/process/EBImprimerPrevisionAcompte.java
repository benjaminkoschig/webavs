package globaz.orion.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.orion.mappingXmlml.EBXmlmlRecapPrevisionAcompte;
import globaz.orion.utils.EBExcelmlUtils;
import globaz.webavs.common.CommonExcelmlContainer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EBImprimerPrevisionAcompte extends BProcess {

    private static final long serialVersionUID = 1L;
    public static final String ANCIENNE_MASSE = "ANCIENNE_MASSE";
    public static final String MODEL_NAME = "listeRecapPrevisionAcompte.xml";
    public static final String NOM = "NOM";
    public static final String NOUVELLE_MASSE = "NOUVELLE_MASSE";
    public static final String NUM_AFFILIE = "NUM_AFFILIE";
    public static final String NUMERO_INFOROM = "0302CAF";
    public static final String RELEVE_A_CONTROLER = "RELEVE_A_CONTROLER";
    public static final String STATUT = "STATUT";
    public static final String SUP_MASSE_MAX_AUTORISEE = "SUP_MASSE_MAX_AUTORISEE";

    List<Map<String, String>> listPrevisionAcompte;
    BigDecimal masseAnnuelleMaxPourPeriodiciteAnnuelle;

    @Override
    protected void _executeCleanUp() {
        // Do nothing
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        if (!listPrevisionAcompte.isEmpty()) {
            setProgressScaleValue(listPrevisionAcompte.size());
            return createDocument(listPrevisionAcompte);

        } else {
            setSendCompletionMail(false);
        }

        return false;

    }

    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        /**
         * s�curit� suppl�mentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseign� getEMailAddress() prend l'email du parent ou � d�faut, celui du
         * user connect�
         * 
         */
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_RADIE_ERREUR_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    private boolean createDocument(List<Map<String, String>> listPrevisionAcompte) throws Exception {

        if (isAborted()) {
            return false;
        }

        CommonExcelmlContainer container = EBXmlmlRecapPrevisionAcompte.loadResults(listPrevisionAcompte, this,
                masseAnnuelleMaxPourPeriodiciteAnnuelle);
        String nomDoc = getSession().getLabel("LISTE_PREVISION_ACOMPTE");

        String docPath = EBExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase(Locale.ENGLISH)
                + "/" + EBImprimerPrevisionAcompte.MODEL_NAME, nomDoc, container);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(AFApplication.DEFAULT_APPLICATION_NAOS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentTypeNumber(EBImprimerPrevisionAcompte.NUMERO_INFOROM);
        this.registerAttachedDocument(docInfo, docPath);

        return true;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("PREVISION_ACOMPTE_KO");
        } else {
            return getSession().getLabel("PREVISION_ACOMPTE_OK");
        }
    }

    public List<Map<String, String>> getListPrevisionAcompte() {
        return listPrevisionAcompte;
    }

    public BigDecimal getMasseAnnuelleMaxPourPeriodiciteAnnuelle() {
        return masseAnnuelleMaxPourPeriodiciteAnnuelle;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setListPrevisionAcompte(List<Map<String, String>> listPrevisionAcompte) {
        this.listPrevisionAcompte = listPrevisionAcompte;
    }

    public void setMasseAnnuelleMaxPourPeriodiciteAnnuelle(BigDecimal masseAnnuelleMaxPourPeriodiciteAnnuelle) {
        this.masseAnnuelleMaxPourPeriodiciteAnnuelle = masseAnnuelleMaxPourPeriodiciteAnnuelle;
    }

}
