/**
 * 
 */
package globaz.orion.process;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.orion.mappingXmlml.EBXmlmlMappingListeInscription;
import globaz.orion.utils.EBExcelmlUtils;
import globaz.orion.utils.OrionContainer;
import java.util.ArrayList;
import ch.globaz.orion.EBApplication;
import ch.globaz.orion.business.models.inscription.InscriptionEbusiness;

/**
 * @author sel
 * 
 */
public class EBImprimerListeInscription extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String MODEL_NAME = "listeInscription.xml";
    private final static String NUMERO_REFERENCE_INFOROM = "0287CEB";

    /**
     * @param args
     */
    public static void main(String[] args) {
        JadeLogger.info(EBImprimerListeInscription.class, "START");

        BSession session = null;
        try {
            session = (BSession) GlobazSystem.getApplication("ORION").newSession("ccjuglo", "glob4az");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        ArrayList<InscriptionEbusiness> list = new ArrayList<InscriptionEbusiness>();
        for (int i = 0; i < 15; i++) {
            InscriptionEbusiness in1 = new InscriptionEbusiness();
            in1.setNumAffilie("000.0" + i + "0");
            in1.setRaisonSociale("Raison " + i);
            in1.setNom("Nom" + i);
            in1.setPrenom("Prenom" + i);
            in1.setMail("mail" + i + "@mail.com");
            in1.setTel("032.731.00.0" + i);
            in1.setModeDeclSalaire((i % 2) == 0 ? "1" : "2");
            in1.setStatut((i % 2) == 0 ? (i % 3) == 0 ? "3" : "1" : "2");
            in1.setRemarque("?");

            list.add(in1);
        }

        InscriptionEbusiness[] tap = new InscriptionEbusiness[list.size()];
        tap = list.toArray(tap);

        EBImprimerListeInscription process = new EBImprimerListeInscription(tap);
        process.setSession(session);
        process.setEMailAddress("sel@globaz.ch");

        try {
            process.executeProcess();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JadeLogger.info(EBImprimerListeInscription.class, "STOP");
        System.exit(1);
    }

    /**
     * Tableaux des inscriptions saisies.
     */
    private InscriptionEbusiness[] inscriptions = null;

    /**
	 * 
	 */
    public EBImprimerListeInscription() {
        super();
    }

    /**
	 * 
	 */
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
        OrionContainer container = EBXmlmlMappingListeInscription.loadResults(inscriptions, this);

        if (isAborted()) {
            return false;
        }

        String nomDoc = getSession().getLabel("LISTE_INSCRIPTION_NOM_DOCUMENT");
        String docPath = EBExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                + EBImprimerListeInscription.MODEL_NAME, nomDoc, container);

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
