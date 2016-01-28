/*
 * Créé le 20 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications.envoiWritterImpl;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPSedexWriterBCK {

    private final static String BASE_FILENAME = "ENVOI_FISC.xml";
    private static final String NAMESPACE101 = "eahv-iv-2011-000101";
    private static final String NAMESPACE102 = "eahv-iv-2011-000102";
    private static final String SOUS_TYPE_MESSAGE_101 = "000101";
    private static final String SOUS_TYPE_MESSAGE_102 = "000102";
    private static final String TAG_2011101 = "xmlns:eahv-iv-2011-000101=\"http://www.eahv-iv.ch/xmlns/eahv-iv-2011-000101/0\"";

    private static final String TAG_2011102 = "xmlns:eahv-iv-2011-000102=\"http://www.eahv-iv.ch/xmlns/eahv-iv-2011-000102/0\"";
    private static final String TAG_COMMON = "xmlns:eahv-iv-common=\"http://www.eahv-iv.ch/xmlns/eahv-iv-common/0\"";

    private static final String TAG_CONTENT = "content";
    private static final String TAG_DECLARATION_LOCAL_REFERENCE = "declarationLocalReference";
    private static final String TAG_DEPARTEMENT = "department";
    private static final String TAG_eCH0007 = "xmlns:eCH-0007=\"http://www.ech.ch/xmlns/eCH-0007/3\"";
    private static final String TAG_eCH0010 = "xmlns:eCH-0010=\"http://www.ech.ch/xmlns/eCH-0010/3\"";
    private static final String TAG_eCH0044 = "xmlns:eCH-0044=\"http://www.ech.ch/xmlns/eCH-0044/1\"";
    private static final String TAG_eCH0058 = "xmlns:eCH-0058=\"http://www.ech.ch/xmlns/eCH-0058/2\"";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ENTETE = "<eahv-iv-2011-000102:header>";
    private static final String TAG_ENTETE101 = "<eahv-iv-2011-000101:header>";

    private static final String TAG_HEADER = "header";
    private static final String TAG_MESSAGE = "<eahv-iv-2011-000101:message ";

    private static final String TAG_MESSAGE_NOM = "message";
    private static final String TAG_MESSAGE102 = "<eahv-iv-2011-000102:message ";
    private static final String TAG_MESSAGEID = "messageId";

    private static final String TAG_MESSAGETYPE = "messageType";
    private static final String TAG_NOM = "name";
    private static final String TAG_NSS = "vn";
    private static final String TAG_OBJECT = "object";
    private static final String TAG_OFFICIAL_NAME = "officialName";

    private static final String TAG_ORDERSCOPE = "orderScope";
    private static final String TAG_PHONE = "phone";

    private static final String TAG_RECIPIENTID = "recipientId";
    private static final String TAG_SENDERID = "senderId";
    private static final String TAG_SUBMESSAGETYPE = "subMessageType";

    private static final String TAG_TAXPAYER = "taxpayer";
    private static final String TAG_VERSION = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";

    private static final String TAG_W3 = "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" minorVersion=\"4\">";
    private static final String TYPE_MESSAGE_2011 = "2011";

    private String date_impression = "";
    public String filename = "";
    private BProcess processAppelant = null;

    private BSession session;
    private String user = "";

    public CPSedexWriterBCK(BSession session) {
        this.session = session;
        setUser(getSession().getUserFullName());
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures...
     */
    public String create(CPCommunicationFiscaleAffichageManager manager, BTransaction transaction) {
        CPCommunicationFiscaleAffichage entity;
        BStatement statement = null;
        java.io.FileWriter fileOut = null;
        try {
            statement = manager.cursorOpen(transaction);
            try {
                filename = getNomFichier(manager);
            } catch (Exception e) {
                filename = BASE_FILENAME;
            }
            // Création du fichier
            File file = new File(getFilename());
            if (file.exists()) {
                file.delete();
            }
            fileOut = new FileWriter(file);
            int nbCas = manager.getCount();
            if (nbCas > 1) {
                fileOut.write(TAG_VERSION + "\n");
                fileOut.write(TAG_MESSAGE102);
                // Création de l'entête pour plusieurs demandes
                creationEntete102(fileOut);
            } else {
                // Création de l'entête pour une demande seule
                // creationEntete101(fileOut);
            }
            fileOut.write(createBaliseTag102(TAG_CONTENT) + "\n");
            while ((entity = (CPCommunicationFiscaleAffichage) manager.cursorReadNext(statement)) != null
                    && (!entity.isNew())) {
                {

                    creationDemande(fileOut, entity);
                    // Mise à jour de la date d'envoi
                    CPCommunicationFiscale comFis = new CPCommunicationFiscale();
                    comFis.setSession(getSession());
                    comFis.setIdCommunication(entity.getIdCommunication());
                    comFis.retrieve(transaction);
                    if (!comFis.isNew()) {
                        if (manager.getDemandeAnnulee().equals(Boolean.FALSE)) {
                            comFis.setDateEnvoi(JACalendar.todayJJsMMsAAAA());
                        } else {
                            comFis.setDateEnvoiAnnulation(JACalendar.todayJJsMMsAAAA());
                        }
                        comFis.update(transaction);
                    }
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                }

            }
            fileOut.write(createBaliseUnTag102(TAG_CONTENT));
            fileOut.write(createBaliseUnTag102(TAG_MESSAGE_NOM));
            // creationFooter(fileOut, nbCas);
            fileOut.close();
        } catch (IOException e) {
            JadeLogger.error(this, e);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return getFilename();
    }

    private String createBaliseTag101(String nomBalise) {
        return "<" + NAMESPACE101 + ":" + nomBalise + ">";
    }

    private String createBaliseTag102(String nomBalise) {
        return "<" + NAMESPACE102 + ":" + nomBalise + ">";
    }

    private String createBaliseUnTag101(String nomBalise) {
        return "</" + NAMESPACE101 + ":" + nomBalise + ">" + "\n";
    }

    private String createBaliseUnTag102(String nomBalise) {
        return "</" + NAMESPACE102 + ":" + nomBalise + ">" + "\n";
    }

    private void createDeclarationLocalReference(FileWriter fileOut) throws IOException {
        fileOut.write(createBaliseTag102(TAG_DECLARATION_LOCAL_REFERENCE) + "\n");
        fileOut.write(createBaliseTag102(TAG_NOM));
        fileOut.write(getSession().getUserFullName());
        fileOut.write(createBaliseUnTag102(TAG_NOM));
        fileOut.write(createBaliseTag102(TAG_DEPARTEMENT));
        fileOut.write(" ");
        fileOut.write(createBaliseUnTag102(TAG_DEPARTEMENT));
        fileOut.write(createBaliseTag102(TAG_PHONE));
        fileOut.write(" ");
        fileOut.write(createBaliseUnTag102(TAG_PHONE));
        fileOut.write(createBaliseTag102(TAG_EMAIL));
        fileOut.write(getSession().getUserEMail());
        fileOut.write(createBaliseUnTag102(TAG_EMAIL));
        fileOut.write(createBaliseUnTag102(TAG_DECLARATION_LOCAL_REFERENCE));
    }

    private void createDeclarationLocalReference101(FileWriter fileOut) throws IOException {
        fileOut.write(createBaliseTag101(TAG_DECLARATION_LOCAL_REFERENCE) + "\n");
        fileOut.write(createBaliseTag101(TAG_NOM));
        fileOut.write(getSession().getUserFullName());
        fileOut.write(createBaliseUnTag101(TAG_NOM));
        fileOut.write(createBaliseTag101(TAG_DEPARTEMENT));
        fileOut.write(" ");
        fileOut.write(createBaliseUnTag101(TAG_DEPARTEMENT));
        fileOut.write(createBaliseTag101(TAG_PHONE));
        fileOut.write(" ");
        fileOut.write(createBaliseUnTag101(TAG_PHONE));
        fileOut.write(createBaliseTag101(TAG_EMAIL));
        fileOut.write(getSession().getUserEMail());
        fileOut.write(createBaliseUnTag101(TAG_EMAIL));
        fileOut.write(createBaliseUnTag101(TAG_DECLARATION_LOCAL_REFERENCE));
    }

    private void createHeader(FileWriter fileOut) throws IOException {
        fileOut.write(TAG_eCH0007 + "\n");
        fileOut.write(TAG_eCH0010 + "\n");
        fileOut.write(TAG_eCH0044 + "\n");
        fileOut.write(TAG_eCH0058 + "\n");
        fileOut.write(TAG_COMMON + "\n");
        fileOut.write(TAG_2011102 + "\n");
        fileOut.write(TAG_W3 + "\n");
        fileOut.write(TAG_ENTETE + "\n");
    }

    private void createHeader101(FileWriter fileOut) throws IOException {
        fileOut.write(TAG_MESSAGE + "\n");
        fileOut.write(TAG_eCH0007 + "\n");
        fileOut.write(TAG_eCH0010 + "\n");
        fileOut.write(TAG_eCH0044 + "\n");
        fileOut.write(TAG_eCH0058 + "\n");
        fileOut.write(TAG_COMMON + "\n");
        fileOut.write(TAG_2011101 + "\n");
        fileOut.write(TAG_W3 + "\n");
        fileOut.write(TAG_ENTETE101 + "\n");
    }

    private void createInfosDemandePur(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws IOException {
        fileOut.write(createBaliseTag101(TAG_NSS));
        fileOut.write(entity.getNumAvs());
        fileOut.write(createBaliseUnTag101(TAG_NSS));

        fileOut.write(createBaliseTag101(TAG_OFFICIAL_NAME));
        entity.getNom();
        fileOut.write(createBaliseUnTag101(TAG_OFFICIAL_NAME));
    }

    private void createMessageInfos(FileWriter fileOut) throws IOException {
        fileOut.write(createBaliseTag102(TAG_MESSAGEID));
        // TODO rechercher message id
        fileOut.write("MESSAGE_ID");
        fileOut.write(createBaliseUnTag102(TAG_MESSAGEID));
        fileOut.write(createBaliseTag102(TAG_MESSAGETYPE));
        fileOut.write(TYPE_MESSAGE_2011);
        fileOut.write(createBaliseUnTag102(TAG_MESSAGETYPE));
        fileOut.write(createBaliseTag102(TAG_SUBMESSAGETYPE));
        fileOut.write(SOUS_TYPE_MESSAGE_102);
        fileOut.write(createBaliseUnTag102(TAG_SUBMESSAGETYPE));
    }

    private void createMessageInfos101(FileWriter fileOut) throws IOException {
        fileOut.write(createBaliseTag101(TAG_MESSAGEID));
        // TODO rechercher message id
        fileOut.write("MESSAGE_ID");
        fileOut.write(createBaliseUnTag101(TAG_MESSAGEID));
        fileOut.write(createBaliseTag101(TAG_MESSAGETYPE));
        fileOut.write(TYPE_MESSAGE_2011);
        fileOut.write(createBaliseUnTag101(TAG_MESSAGETYPE));
        fileOut.write(createBaliseTag101(TAG_SUBMESSAGETYPE));
        fileOut.write(SOUS_TYPE_MESSAGE_101);
        fileOut.write(createBaliseUnTag101(TAG_SUBMESSAGETYPE));
    }

    private void createObject(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws IOException {
        fileOut.write(createBaliseTag101(TAG_OBJECT) + "\n");
        createInfosDemandePur(fileOut, entity);
        fileOut.write(createBaliseUnTag101(TAG_OBJECT));
    }

    private void createRecipientId(FileWriter fileOut) throws IOException {
        fileOut.write(createBaliseTag102(TAG_RECIPIENTID));
        // TODO rechercher recipient_id
        fileOut.write("RECIPIENT_ID");
        fileOut.write(createBaliseUnTag102(TAG_RECIPIENTID));
    }

    private void createRecipientId101(FileWriter fileOut) throws IOException {
        fileOut.write(createBaliseTag101(TAG_RECIPIENTID));
        // TODO rechercher recipient_id
        fileOut.write("RECIPIENT_ID");
        fileOut.write(createBaliseUnTag101(TAG_RECIPIENTID));
    }

    private void createSenderId(FileWriter fileOut) throws IOException {
        fileOut.write(createBaliseTag102(TAG_SENDERID));
        // TODO rechercher senderId
        fileOut.write("SENDER_ID");
        fileOut.write(createBaliseUnTag102(TAG_SENDERID));
    }

    private void createSenderId101(FileWriter fileOut) throws IOException {
        fileOut.write(createBaliseTag101(TAG_SENDERID));
        // TODO rechercher senderId
        fileOut.write("SENDER_ID");
        fileOut.write(createBaliseUnTag101(TAG_SENDERID));
    }

    private void creationContent101(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws Exception {
        fileOut.write(createBaliseTag101(TAG_CONTENT));
        fileOut.write(createBaliseTag101(TAG_ORDERSCOPE));
        fileOut.write("");
        fileOut.write(createBaliseUnTag101(TAG_ORDERSCOPE));
        fileOut.write(createBaliseTag101(TAG_TAXPAYER));
        createInfosDemandePur(fileOut, entity);
        fileOut.write(createBaliseUnTag101(TAG_TAXPAYER));
        fileOut.write(createBaliseUnTag101(TAG_CONTENT));
        fileOut.write(createBaliseUnTag101(TAG_MESSAGE_NOM));
    }

    private void creationDemande(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws Exception {

        creationEntete101(fileOut, entity);
        creationContent101(fileOut, entity);
    }

    private void creationEntete101(FileWriter fileOut, CPCommunicationFiscaleAffichage entity) throws Exception {
        createHeader101(fileOut);
        createSenderId101(fileOut);
        createDeclarationLocalReference101(fileOut);
        createRecipientId101(fileOut);

        createObject(fileOut, entity);
        createMessageInfos101(fileOut);
        fileOut.write(createBaliseUnTag101(TAG_HEADER));
    }

    private void creationEntete102(FileWriter fileOut) throws Exception {
        createHeader(fileOut);
        createSenderId(fileOut);
        createDeclarationLocalReference(fileOut);
        createRecipientId(fileOut);
        createMessageInfos(fileOut);
        fileOut.write(createBaliseUnTag102(TAG_HEADER));
    }

    public String getDate_impression() {
        return date_impression;
    }

    public String getFilename() {
        return filename;
    }

    private String getNomFichier(CPCommunicationFiscaleAffichageManager manager) throws Exception {
        return "demandes_" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD) + ".xml";
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public BSession getSession() {
        return session;
    }

    public String getUser() {
        return user;
    }

    public void setDate_impression(String string) {
        date_impression = string;
    }

    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setUser(String string) {
        user = string;
    }
}
