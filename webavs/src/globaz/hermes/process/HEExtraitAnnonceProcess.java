package globaz.hermes.process;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.print.itext.HEExtraitAnnonceAssureBean;
import globaz.hermes.print.itext.HEExtraitAnnonce_Doc;
import globaz.hermes.print.itext.HELettreAccompagneCI_Doc;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import java.util.ArrayList;
import java.util.List;

public class HEExtraitAnnonceProcess extends HEExtraitAnnonce_Doc {

    private static final long serialVersionUID = 1L;
    private String service;
    private List<?> attachedDocumentsTemps;

    /**
     * Constructor for HEExtraitAnnonceProcess.
     * 
     * @param session
     * @throws FWIException
     */
    public HEExtraitAnnonceProcess() throws FWIException {
        super();
    }

    /**
     * Constructor for HEExtraitAnnonceProcess.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public HEExtraitAnnonceProcess(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    /**
     * Constructor for HEExtraitAnnonceProcess.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public HEExtraitAnnonceProcess(FWProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Rajoute un document d'accompagnement pour chaque extrait de l'annonce
     * 
     * @throws Exception
     *             En cas de problème lors de l'envoi
     */
    @Override
    public void afterBuildReport() {
        setLastRefUnique(m_container.getReferenceUnique());
    }

    @Override
    public void afterExecuteReport() {
        if (hasAttachedDocuments()) {
            try {
                // Merge du dernier document
                getDocumentInfo().setPublishDocument(false);
                getDocumentInfo().setArchiveDocument(true);
                this.mergePDF(getDocumentInfo(), true, 0, false, null);

                // On reconstruit la liste des fichiers
                if (attachedDocumentsTemps == null) {
                    attachedDocumentsTemps = new ArrayList();
                }
                attachedDocumentsTemps.addAll(getAttachedDocuments());
                getAttachedDocuments().clear();
                getAttachedDocuments().addAll(attachedDocumentsTemps);
                attachedDocumentsTemps.clear();

                // On merge tous les pdf générés.
                getDocumentInfo().setPublishDocument(true);
                getDocumentInfo().setArchiveDocument(false);
                this.mergePDF(getDocumentInfo(), false, 0, false, null);

            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL,
                        "globaz.hermes.process.HEExtraitAnnonceProcess");
                JadeLogger.error(this, e);
                // e.printStackTrace();
            }
        }
    }

    @Override
    public void beforeBuildReport() {

        if (!getLastRefUnique().equals("") && !getLastRefUnique().equals(m_container.getReferenceUnique())) {

            try {
                getDocumentInfo().setPublishDocument(false);
                getDocumentInfo().setArchiveDocument(true);
                this.mergePDF(getDocumentInfo(), true, 0, false, null);
                if (attachedDocumentsTemps == null) {
                    attachedDocumentsTemps = new ArrayList();
                }
                attachedDocumentsTemps.addAll(getAttachedDocuments());
                getAttachedDocuments().clear();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }

        try {
            HEExtraitAnnonceAssureBean annonce = null;

            // Récupère l'annonce pour le document courrant
            annonce = m_container;

            // Crée le fichier d'accompagnement au CI
            if ((annonce != null) && "97".equals(annonce.getMotif())
                    && "true".equals(getSession().getApplication().getProperty("adresse.input"))) {
                if (!annonce.getReferenceUnique().equals(getLastRefUnique())) {
                    HELettreAccompagneCI_Doc document = new HELettreAccompagneCI_Doc(getSession());
                    document.setEMailAddress(getEMailAddress());
                    if (!JadeStringUtil.isBlank(getService())) {
                        document.setService(getService());
                    }
                    // document.setSexe(new String(getSexe()));
                    // document.setNumeroAVS(new String(getNumeroAVS20()));
                    // Recherche et ajout de l'adresse de l'assuré dans le
                    // document (HEInfos)
                    if (!JadeStringUtil.isEmpty(annonce.getIdAnnonce11())) {
                        document.addEntity(document.getInfoAssure(annonce.getIdAnnonce11(), HEInfos.CS_ADRESSE_ASSURE));
                    } else {
                        // idAnnonce est vide, donc on prend la référence unique
                        // passé
                        document.addEntity(document.getInfoAssure(annonce.getReferenceUnique(),
                                HEInfos.CS_ADRESSE_ASSURE));
                    }
                    // document.setIsArchivage(super.isArchivage());
                    // document.setUserId(super.getUserId());
                    document.setAnnonceAssure(annonce);
                    // document.setFirstDocument(this);
                    document.setLangueFromEcran(getLangueFromEcran());

                    document.setSendMailOnError(true);
                    // document.setSendCompletionMail(false);
                    document.setPrintCompletionDoc(false);
                    // document.setControleTransaction(true);
                    document.setDeleteOnExit(false);
                    // document.setParent(this);
                    document.executeProcess();
                    if (document.getDocumentList().size() > 0) {
                        // Rajoute le document d'accompagnement à la liste
                        JadePublishDocument published = (JadePublishDocument) document.getAttachedDocuments().get(0);
                        this.registerAttachedDocument(published.getPublishJobDefinition().getDocumentInfo(),
                                published.getDocumentLocation());
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    @Override
    public boolean beforePrintDocument() {
        return ((super.getDocumentList().size() > 0) && !isAborted());
    }

    @Override
    public String getService() {
        return service;
    }

    @Override
    public void setService(String service) {
        this.service = service;
    }
}