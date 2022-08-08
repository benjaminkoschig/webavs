package globaz.osiris.process.ebill;

import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.common.Jade;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.musca.FAImpressionFactureEBillXml;
import globaz.musca.api.musca.PaireIdEcheanceParDateExigibiliteEBill;
import globaz.musca.api.musca.PaireIdExterneEBill;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.ebill.enums.CATraitementEtatEBillEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osiris.ch.ebill.send.invoice.InvoiceEnvelope;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EBillHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EBillHelper.class);

    public void creerFichierEBill(CACompteAnnexe compteAnnexe, FAEnteteFacture entete, FAEnteteFacture enteteReference, String montantFacture, List<Map> lignes, Map<PaireIdEcheanceParDateExigibiliteEBill, List<Map>> lignesSursis, String reference, List<JadePublishDocument> attachedDocuments, String dateImprOuFactu, String dateEcheance, String dateOctroiSursis, BSession session, String titreSursis, EBillTypeDocument typeDocument) throws Exception {

        String billerId = CAApplication.getApplicationOsiris().getCAParametres().getEBillBillerId();

        // Initialisation de l'objet � marshaller dans la facture eBill
        FAImpressionFactureEBillXml factureEBill = new FAImpressionFactureEBillXml();
        factureEBill.setEBillAccountID(compteAnnexe.getEBillAccountID());
        factureEBill.setEntete(entete);
        factureEBill.setEnteteReference(enteteReference);
        factureEBill.setMontantFacture(montantFacture);
        factureEBill.setLignes(lignes);
        factureEBill.setLignesSursis(lignesSursis);
        factureEBill.setReference(reference);
        factureEBill.setAttachedDocuments(attachedDocuments);
        factureEBill.setDateImprOuFactu(dateImprOuFactu);
        factureEBill.setDateEcheance(dateEcheance);
        factureEBill.setDateOctroiSursis(dateOctroiSursis);
        factureEBill.setSession(session);
        factureEBill.setTitreSursis(titreSursis);
        factureEBill.setBillerId(billerId);
        factureEBill.setTypeDocument(typeDocument);

        // Init de la facture eBill
        factureEBill.initFactureEBill();

        // Rempli l'objet qui sera marshall� dans la facture eBill
        InvoiceEnvelope content = factureEBill.createFileContent();

        // Creation du fichier XML
        String filename = billerId + "_" + entete.getEBillTransactionID() + ".xml";
        String localPath = Jade.getInstance().getPersistenceDir() + EBillSftpProcessor.getFolderOutName() + filename;
        File localFile = new File(localPath);
        LOGGER.info("Cr�ation du fichier xml eBill : " + localFile.getAbsoluteFile() + "...");

        // Marshall de l'objet dans le fichier XML
        factureEBill.marshallDansFichier(content, localFile);

        // Upload du fichier XML sur le sftp
        try (FileInputStream retrievedFile = new FileInputStream(localFile)) {
            EBillSftpProcessor.getInstance().sendFile(retrievedFile, filename);
            LOGGER.info("Fichier eBill envoy� sur le ftp : " + localFile.getAbsoluteFile() + "...");
        }
    }

    /**
     * M�thode permetant de construire une ent�te de facture fictive
     * partiellement initialis� avec les informations trouv�es
     * dans la section et dans le compte annexe
     *
     * @param section : une section
     * @param session : une session
     * @return une ent�te de facture partiellement initialis�e
     */
    public FAEnteteFacture generateEnteteFacture(CASection section, BSession session) {
        FAEnteteFacture entete = new FAEnteteFacture();
        entete.setSession(session);
        entete.setIdModeRecouvrement(CodeSystem.MODE_RECOUV_AUTOMATIQUE);
        entete.setIdTiers(section.getCompteAnnexe().getIdTiers());
        entete.setIdTypeCourrier(section.getTypeAdresse());
        entete.setIdDomaineCourrier(section.getDomaine());
        entete.setIdExterneRole(section.getCompteAnnexe().getIdExterneRole());
        entete.setIdExterneFacture(section.getIdExterne());
        return entete;
    }

    /**
     * M�thode permetant de rechercher une ent�te eBill par le biais de l'idExterneFactureCompensation
     * cette m�thode est utilis� dans le processus d'impression des bulletins de soldes eBill.
     *
     * @param paireIdExterneEBill : une combinaison d'idExterneRole et d'idExterneFactureCompensation
     * @param idPassage           : l'id de passage
     * @return l'ent�te
     */
    public FAEnteteFacture getEnteteFacture(PaireIdExterneEBill paireIdExterneEBill, String idPassage, BSession session) throws Exception {
        FAEnteteFactureManager manager = new FAEnteteFactureManager();
        manager.setSession(session);
        manager.setForIdExterneRole(paireIdExterneEBill.getIdExterneRole());
        manager.setForIdPassage(idPassage);
        manager.find(BManager.SIZE_NOLIMIT);
        return (FAEnteteFacture) manager.getFirstEntity();
    }

    /**
     * M�thode permetant de rechercher une ent�te eBill par le biais d'une paire d'idExterneRole et d'idExterneFactureCompensation
     * cette m�thode est utilis� dans le processus d'impression des bulletins de soldes eBill.
     *
     * @param paireIdExterneEBill : pair d'idExterneRole et d'idExterneFactureCompensation
     * @param session : une session
     * @return l'ent�te ou null
     */
    public FAEnteteFacture getEnteteFactureReference(PaireIdExterneEBill paireIdExterneEBill, BSession session) throws Exception {
        FAEnteteFactureManager manager = new FAEnteteFactureManager();
        manager.setSession(session);
        manager.setForIdExterneRole(paireIdExterneEBill.getIdExterneRole());
        manager.setForIdExterneFacture(paireIdExterneEBill.getIdExterneFactureCompensation());
        manager.find(BManager.SIZE_NOLIMIT);
        return (FAEnteteFacture) manager.getFirstEntity();
    }

    /**
     * M�thode permettant de rechercher le fichier g�n�r� durant l'impression
     * de le retourner pour �tre ajouter � la facture eBill
     *
     * @param attachedDocuments : les fichiers g�n�r� durant l'impression
     * @return le fichier g�n�r� durant l'impression
     */
    public List<JadePublishDocument> findAndReturnAttachedDocuments(List<JadePublishDocument> attachedDocuments, String documentType) {
        List<JadePublishDocument> filteredAttachedDocuments = new ArrayList<>();
        Iterator<JadePublishDocument> it = attachedDocuments.iterator();
        while (it.hasNext()) {
            final JadePublishDocument jadePublishDocument = it.next();
            if (jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentType().equals(documentType)) {
                filteredAttachedDocuments.add(jadePublishDocument);
                break;
            }
        }
        return filteredAttachedDocuments;
    }

    /**
     * M�thode permettant de rechercher le fichier g�n�r� durant l'impression
     * de le retourner pour �tre ajouter � la facture eBill et de le supprimer
     * si n�cessaire de la listes de fichiers � merger dans l'impression actuelle
     *
     * @param entete : l'entete qui permet d'identifier le fichier � retourner
     * @param attachedDocuments : les fichiers g�n�r� durant l'impression
     * @return le fichier g�n�r� durant l'impression
     */
    public List<JadePublishDocument> findRemoveAndReturnAttachedDocuments(FAEnteteFacture entete, List<JadePublishDocument> attachedDocuments, String documentType, boolean removeAttachment) {
        List<JadePublishDocument> filteredAttachedDocuments = new ArrayList<>();
        Iterator<JadePublishDocument> it = attachedDocuments.iterator();
        while (it.hasNext()) {
            final JadePublishDocument jadePublishDocument = it.next();
            if (entete.getIdExterneFacture().equals(jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentProperties().get(CADocumentInfoHelper.SECTION_ID_EXTERNE))
                    && entete.getIdExterneRole().equals(jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentProperties().get("numero.role.formatte"))) {
                if (documentType != null) {
                    if(jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentType().equals(documentType)) {
                        filteredAttachedDocuments.add(jadePublishDocument);
                    }
                } else {
                    filteredAttachedDocuments.add(jadePublishDocument);
                }
                if (removeAttachment) {
                    it.remove();
                }
            }
        }
        return filteredAttachedDocuments;
    }

     /**
     * Mise � jour du statut eBill de la section et de son transactionID eBill
     * passe l'�tat � pending.
     *
     * @param section  la section � mettre � jour.
     * @param transactionId l'id de transaction li� au traitement.
     */
    public void updateSectionEtatEtTransactionID(final CASection section, final String transactionId, FWMemoryLog memoryLog) {
        try {
            section.setEBillEtat(CATraitementEtatEBillEnum.NUMERO_ETAT_REJECTED_OR_PENDING);
            section.setEBillErreur("");
            section.setEBillTransactionID(transactionId);
            section.update();
        } catch (Exception e) {
            memoryLog.logMessage("Impossible de mettre � jour la section avec l'id : " + section.getIdSection() + " : " + e.getMessage(), FWViewBeanInterface.WARNING, this.getClass().getName());
        }
    }

    /**
     * Mise � jour l'historique du contentieux avec
     * le eBillPrinted et le transactionID
     *
     * @param transactionID l'id de transaction li� au traitement.
     */
    public void updateHistoriqueEBillPrintedEtTransactionID(COContentieux curContentieux, String transactionID, FWMemoryLog memoryLog) {
        try {
            COHistorique dernierHistorique = curContentieux.loadHistorique();
            if (dernierHistorique.getIdEtape().equals(curContentieux.getIdEtape())
                    && dernierHistorique.getIdContentieux().equals(curContentieux.getIdContentieux())
                    && dernierHistorique.getIdSequence().equals(curContentieux.getIdSequence())) {
                dernierHistorique.setEBillTransactionID(transactionID);
                dernierHistorique.setEBillPrinted(true);
                dernierHistorique.update();
            }
        } catch (Exception e) {
            memoryLog.logMessage("Impossible de mettre � jour l'historique du contentieux avec le transactionID : " + transactionID + " : " + e.getMessage(), FWViewBeanInterface.WARNING, this.getClass().getName());
        }
    }
}
