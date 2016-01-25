package globaz.pavo.process;

import globaz.common.util.CommonBlobUtils;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.pavo.db.compte.CIAnnonceCentrale;
import globaz.pavo.db.compte.CIAnnonceCentraleBlob;
import globaz.pavo.db.compte.CIAnnonceCentraleBlobManager;
import globaz.pavo.print.list.CIAnnoncesCentrale_Doc;
import java.io.File;
import java.util.List;

public class CIAnnonceCentraleImpressionRapportProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String MODE_FONCTIONNEMENT_IMPRESSION = "impression";
    public static final String MODE_FONCTIONNEMENT_REIMPRESSION = "reimpression";
    public static final String MODE_IMPRESSION_INFOROM_D0064 = "modeImpressionInforomD0064";

    private CIAnnonceCentrale annonceCentrale;
    private String modeFonctionnement;
    private boolean processOnError;

    public CIAnnonceCentraleImpressionRapportProcess() {
        super();
        modeFonctionnement = CIAnnonceCentraleImpressionRapportProcess.MODE_FONCTIONNEMENT_IMPRESSION;
        processOnError = false;
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        String mailDetailMessage = getSession().getLabel("CI_ANNONCE_CENTRALE_IMPRESSION_PROTOCOLE_MAIL_DETAIL_SUCCES");

        try {
            if (CIAnnonceCentraleImpressionRapportProcess.MODE_FONCTIONNEMENT_REIMPRESSION
                    .equalsIgnoreCase(modeFonctionnement)) {
                reImprimerProtocole();
            } else {
                imprimerProtocole();
                saveProtocoleInDb();
            }

            if (isAborted() || isOnError() || getSession().hasErrors()) {
                processOnError = true;
                mailDetailMessage = getSession().getLabel(
                        "CI_ANNONCE_CENTRALE_IMPRESSION_PROTOCOLE_MAIL_DETAIL_ERREURS");
            }

        } catch (Exception e) {
            processOnError = true;
            mailDetailMessage = getSession().getLabel("CI_ANNONCE_CENTRALE_IMPRESSION_PROTOCOLE_MAIL_DETAIL_ERREURS")
                    + " : " + e.toString();
        }

        getMemoryLog().logMessage(mailDetailMessage, FWMessage.INFORMATION, this.getClass().getName());

        return processOnError;
    }

    private void createBlob(String idAnnonce, String idBlob, String docLocation) throws Exception,
            JadePersistenceException {

        byte[] bytes = CommonBlobUtils.fileToByteArray(docLocation);

        CommonBlobUtils.addBlob(idBlob, bytes, getTransaction());

        CIAnnonceCentraleBlob annonceCentraleBlob = new CIAnnonceCentraleBlob();
        annonceCentraleBlob.setSession(getSession());
        annonceCentraleBlob.setIdAnnonce(idAnnonce);
        annonceCentraleBlob.setIdBlob(idBlob);

        annonceCentraleBlob.add(getTransaction());

    }

    private String genererIdBlob() {
        return this.getClass().getName() + "_" + JadeUUIDGenerator.createStringUUID() + "_"
                + getAnnonceCentrale().getAnnonceCentraleId();
    }

    public CIAnnonceCentrale getAnnonceCentrale() {
        return annonceCentrale;
    }

    @Override
    protected String getEMailObject() {

        return getSession().getLabel("CI_ANNONCE_CENTRALE_IMPRESSION_PROTOCOLE_MAIL_SUBJECT");
    }

    @Override
    public boolean getForceCompletionMail() {

        if (CIAnnonceCentraleImpressionRapportProcess.MODE_FONCTIONNEMENT_REIMPRESSION
                .equalsIgnoreCase(modeFonctionnement)) {
            return true;
        }

        return false;
    }

    public String getModeFonctionnement() {
        return modeFonctionnement;
    }

    private void imprimerProtocole() throws Exception {

        CIAnnoncesCentrale_Doc annonceCentraleRapport = new CIAnnoncesCentrale_Doc(getSession());
        annonceCentraleRapport.setParentWithCopy(this);
        annonceCentraleRapport
                .setModeImpression(CIAnnonceCentraleImpressionRapportProcess.MODE_IMPRESSION_INFOROM_D0064);
        annonceCentraleRapport.setSpecifiqueAnnonceCentraleIdPrint(getAnnonceCentrale().getAnnonceCentraleId());
        annonceCentraleRapport.executeProcess();

    }

    public boolean isProcessOnError() {
        return processOnError;
    }

    @Override
    public GlobazJobQueue jobQueue() {

        return GlobazJobQueue.READ_LONG;
    }

    private void reImprimerProtocole() throws Exception {

        CIAnnonceCentraleBlobManager annonceCentraleBlobManager = new CIAnnonceCentraleBlobManager();
        annonceCentraleBlobManager.setSession(getSession());
        annonceCentraleBlobManager.setForIdAnnonce(getAnnonceCentrale().getAnnonceCentraleId());
        annonceCentraleBlobManager.find();

        if (annonceCentraleBlobManager.size() == 1) {
            CIAnnonceCentraleBlob annonceCentraleBlob = (CIAnnonceCentraleBlob) annonceCentraleBlobManager
                    .getFirstEntity();
            byte[] bytes = (byte[]) CommonBlobUtils.readBlob(annonceCentraleBlob.getIdBlob(), getTransaction());
            File file = CommonBlobUtils.byteArrayToFile(
                    Jade.getInstance().getSharedDir() + "/" + annonceCentraleBlob.getIdBlob() + ".pdf", bytes);

            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentTypeNumber(CIAnnoncesCentrale_Doc.NUMERO_REFERENCE_INFOROM);

            this.registerAttachedDocument(docInfo, file.getAbsolutePath());
        } else {
            getMemoryLog().logMessage(
                    getSession().getLabel(
                            "CI_ANNONCE_CENTRALE_IMPRESSION_PROTOCOLE_MAIL_DETAIL_ANNONCE_PAS_REIMPRIMABLE"),
                    FWMessage.INFORMATION, this.getClass().getName());
        }

    }

    private void saveProtocoleInDb() throws Exception {

        List<JadePublishDocument> listAttachedDocuments = getAttachedDocuments();
        for (JadePublishDocument aDocument : listAttachedDocuments) {
            createBlob(getAnnonceCentrale().getAnnonceCentraleId(), genererIdBlob(), aDocument.getDocumentLocation());
        }
    }

    public void setAnnonceCentrale(CIAnnonceCentrale annonceCentrale) {
        this.annonceCentrale = annonceCentrale;
    }

    public void setModeFonctionnement(String modeFonctionnement) {
        this.modeFonctionnement = modeFonctionnement;
    }

}
