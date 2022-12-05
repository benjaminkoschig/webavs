package globaz.osiris.process.ebill;

import globaz.docinfo.CADocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.api.musca.FAImpressionFactureEBillXml;
import globaz.musca.api.musca.PaireIdEcheanceParDateExigibiliteEBill;
import globaz.musca.api.musca.PaireIdExterneEBill;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.ebill.enums.CATraitementEtatEBillEnum;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osiris.ch.ebill.send.invoice.InvoiceEnvelope;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * partiellement initialis�e avec les informations trouv�es
     * dans la section et dans le compte annexe.
     * Cette ent�te fictive est incompl�te et ne peut pas �tre
     * sauvegard� en DB. Elle est utilis� par eBill comme base pour
     * la cr�ation d'une facture eBill.
     *
     * @param section : une section
     * @param session : une session
     * @return une ent�te de facture partiellement initialis�e
     */
    public FAEnteteFacture generateEnteteFactureFictive(CASection section, BSession session) {
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
     * de le retourner pour �tre ajouter � la facture eBill et de le supprimer
     * si n�cessaire de la listes de fichiers � merger dans l'impression actuelle
     *
     * @param attachedDocuments : les fichiers g�n�r� durant l'impression
     * @param documentType : le type de document recherch�
     * @param removeAttachment : d�finit si le fichier doit �tre supprim� ou non des attachment
     * @return les fichiers qui match les crit�res
     */
    public List<JadePublishDocument> findReturnOrRemoveAttachedDocuments(List<JadePublishDocument> attachedDocuments, String documentType, boolean removeAttachment) {
        List<JadePublishDocument> filteredAttachedDocuments = new ArrayList<>();
        Iterator<JadePublishDocument> it = attachedDocuments.iterator();
        while (it.hasNext()) {
            final JadePublishDocument jadePublishDocument = it.next();
            if (jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentType().equals(documentType)) {
                filteredAttachedDocuments.add(jadePublishDocument);
                if (removeAttachment) {
                    it.remove();
                }
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
     * @param documentType : le type de document recherch�
     * @param removeAttachment : d�finit si le fichier doit �tre supprim� ou non des attachment
     * @return les fichiers qui match les crit�res
     */
    public List<JadePublishDocument> findReturnOrRemoveAttachedDocuments(FAEnteteFacture entete, List<JadePublishDocument> attachedDocuments, String documentType, boolean removeAttachment) {
        List<JadePublishDocument> filteredAttachedDocuments = new ArrayList<>();
        Iterator<JadePublishDocument> it = attachedDocuments.iterator();
        while (it.hasNext()) {
            final JadePublishDocument jadePublishDocument = it.next();
            if (entete.getIdExterneFacture().equals(jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentProperties().get(CADocumentInfoHelper.SECTION_ID_EXTERNE))
                    && entete.getIdExterneRole().equals(jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentProperties().get("numero.role.formatte"))) {
                if (documentType != null) {
                    if (jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentType().equals(documentType)) {
                        filteredAttachedDocuments.add(jadePublishDocument);
                        if (removeAttachment) {
                            it.remove();
                        }
                    }
                } else {
                    filteredAttachedDocuments.add(jadePublishDocument);
                    if (removeAttachment) {
                        it.remove();
                    }
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
     * Recherche la d�cision fusionn�e dans les attachedDocuments
     *
     * @param plan le plan.
     * @param session la session.
     * @param attachedDocuments les attachedDocuments.
     */
    public JadePublishDocument rechercheDecisionFusionneePourEBill(CAPlanRecouvrement plan, BSession session, List<JadePublishDocument> attachedDocuments) {
        boolean eBillOsirisActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillOsirisActifEtDansListeCaisses(session);

        // Recherche la d�cision fusionnee dans les attachedDocuments si :
        //  - eBillOsiris est actif
        //  - le compte annexe poss�de un eBillAccountID
        //  - eBillPrintable est s�lection� sur le plan
        if (eBillOsirisActif && plan.getEBillPrintable()) {
            if (plan.getCompteAnnexe() != null && !JadeStringUtil.isBlankOrZero(plan.getCompteAnnexe().getEBillAccountID())) {

                // Recherche les document de type d�cisions et les supprimes des attachments
                List<JadePublishDocument> decisions = findReturnOrRemoveAttachedDocuments(attachedDocuments, CAILettrePlanRecouvDecision.class.getSimpleName(), true);

                // Trie les d�cisions par nombre de page pour pouvoir trouver la d�cision d�j� merge
                List<JadePublishDocument> decisionsSorted = decisions.stream().sorted(Comparator.comparingInt(EBillHelper::getNumberOfPages).reversed()).collect(Collectors.toList());

                // Supprime tous les autres documents des attachments
                attachedDocuments.clear();

                if (!decisionsSorted.isEmpty()) {
                    return decisionsSorted.get(0);
                }
            }
        }

        return null;
    }

    private static int getNumberOfPages(JadePublishDocument jadePublishDocument) {
        return jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getChildren().size();
    }

    public void ajouteCompteurEBillToMemoryLog(int factureEBill, FWMemoryLog memoryLog, JadePublishDocumentInfo docInfo, BSession session, String className) {
        if (memoryLog != null) {
            memoryLog.logMessage(session.getLabel("OBJEMAIL_EBILL_FAELEC") + factureEBill, FWMessage.INFORMATION, className);
            ajouteMemoryLogEBillToDocumentNotes(memoryLog, docInfo);
        }
    }

    public void ajouteMemoryLogEBillToDocumentNotes(FWMemoryLog memoryLog, JadePublishDocumentInfo docInfo) {
        if (docInfo != null) {
            docInfo.setDocumentNotes((!JadeStringUtil.isBlank(docInfo.getDocumentNotes()) ? docInfo.getDocumentNotes() : "") + memoryLog.getMessagesInString());
        }
    }

}