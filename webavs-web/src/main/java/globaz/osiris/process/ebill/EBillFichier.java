package globaz.osiris.process.ebill;

import globaz.globall.db.BSession;
import globaz.jade.common.Jade;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.musca.FAImpressionFactureEBillXml;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.db.comptes.CACompteAnnexe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osiris.ch.ebill.send.invoice.InvoiceEnvelope;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

public class EBillFichier {

    private static final Logger LOGGER = LoggerFactory.getLogger(EBillFichier.class);

    public static void creerFichierEBill(CACompteAnnexe compteAnnexe, FAEnteteFacture entete, FAEnteteFacture enteteReference, String montantBulletinSoldes, String montantSursis, List<Map> lignes, String reference, JadePublishDocument attachedDocument, String dateFacturation, String dateEcheance, String billerId, BSession session, EBillSftpProcessor serviceFtp) throws Exception {

        // Initialisation de l'objet à marshaller dans la facture eBill
        FAImpressionFactureEBillXml factureEBill = new FAImpressionFactureEBillXml();
        factureEBill.setEntete(entete);
        factureEBill.setEnteteReference(enteteReference);
        factureEBill.setDateFacturation(dateFacturation);
        factureEBill.setDateEcheance(dateEcheance);
        factureEBill.setReference(reference);
        factureEBill.setLignes(lignes);
        factureEBill.setBillerId(billerId);
        factureEBill.setSession(session);
        factureEBill.setMontantBulletinSoldes(montantBulletinSoldes);
        factureEBill.setMontantSursis(montantSursis);
        factureEBill.setAttachedDocument(attachedDocument);
        factureEBill.seteBillAccountID(compteAnnexe.geteBillAccountID());

        // Init de la facture eBill
        factureEBill.initFactureEBill();

        // Rempli l'objet qui sera marshallé dans la facture eBill
        InvoiceEnvelope content = factureEBill.createFileContent();

        // Creation du fichier XML
        String filename = billerId + "_" + entete.geteBillTransactionID() + ".xml";
        String localPath = Jade.getInstance().getPersistenceDir() + serviceFtp.getFolderOutName() + filename;
        File localFile = new File(localPath);
        LOGGER.info("Création du fichier xml eBill : " + localFile.getAbsoluteFile() + "...");

        // Marshall de l'objet dans le fichier XML
        factureEBill.marshallDansFichier(content, localFile);

        // Upload du fichier XML sur le sftp
        try (FileInputStream retrievedFile = new FileInputStream(localFile)) {
            serviceFtp.sendFile(retrievedFile, filename);
            LOGGER.info("Fichier eBill envoyé sur le ftp : " + localFile.getAbsoluteFile() + "...");
        }
    }
}
