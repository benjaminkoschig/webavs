package globaz.naos.process.ide;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.naos.application.AFApplication;
import globaz.naos.db.ide.AFIdeAnnonce;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.IDEServiceCallUtil;
import idech.admin.bfs.xmlns.bfs_5102_000001._2.Message;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class AFIdeReceptionMessageInfoAboSedexProcess {

    private static final String PACKAGE_CLASS_FOR_READ_SEDEX_INFO_ABO_MESSAGE = "idech.admin.bfs.xmlns.bfs_5102_000001._2";

    private BSession session = null;
    private JAXBContext jaxbContext = null;
    private Unmarshaller unmarshaller = null;
    private String passSedex = "";
    private String userSedex = "";

    public String getPassSedex() {
        return passSedex;
    }

    public BSession getSession() {
        return session;
    }

    public String getUserSedex() {
        return userSedex;
    }

    public void setPassSedex(String passSedex) {
        this.passSedex = passSedex;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setUserSedex(String userSedex) {
        this.userSedex = userSedex;
    }

    private String getInfoAboMessageType(Message message) {
        return String.valueOf(message.getRegisterModification().getUidInfoAboMessageType());
    }

    private IDEDataBean formatdata(Message message) {
        IDEDataBean ideDataBean = IDEServiceCallUtil.formatdata(message.getRegisterModification()
                .getRegisterOrganisationData());

        ideDataBean.setTypeAnnonceIde(AFIDEUtil
                .translateInfoAboMessageTypeInTypeAnnonceIde(getInfoAboMessageType(message)));

        return ideDataBean;
    }

    private IDEDataBean readInfoAboMessage(SedexMessage messageSedexInfoAbo) throws FileNotFoundException,
            JAXBException {

        InputStream fileInputStream = new FileInputStream(messageSedexInfoAbo.getFileLocation());

        Message message = (Message) unmarshaller.unmarshal(fileInputStream);
        IDEDataBean ideDataBean = formatdata(message);

        return ideDataBean;

    }

    @Setup
    public void setUp(Properties properties) throws JadeDecryptionNotSupportedException,
            JadeEncrypterNotFoundException, Exception {

        String encryptedUser = properties.getProperty("userSedex");
        if (encryptedUser == null) {
            JadeLogger.error(this, "AFIdeReceptionMessageInfoAboSedexProcess : user sedex null");
            throw new IllegalStateException("AFIdeReceptionMessageInfoAboSedexProcess : user sedex null");
        }
        String decryptedUser = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "AFIdeReceptionMessageInfoAboSedexProcess : password sedex null");
            throw new IllegalStateException("AFIdeReceptionMessageInfoAboSedexProcess : password sedex null");
        }
        String decryptedPass = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        setUserSedex(decryptedUser);
        setPassSedex(decryptedPass);

        session = (BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).newSession(userSedex,
                passSedex);

        jaxbContext = JAXBContext.newInstance(PACKAGE_CLASS_FOR_READ_SEDEX_INFO_ABO_MESSAGE);
        unmarshaller = jaxbContext.createUnmarshaller();
    }

    private void generateAnnonceIdeEntrante(IDEDataBean ideDataBean) throws Exception {

        BTransaction transaction = null;

        try {

            transaction = new BTransaction(session);
            transaction.openTransaction();

            // Création annonce
            AFIdeAnnonce annonce = new AFIdeAnnonce();
            annonce.setSession(session);
            annonce.setNumeroIdeRemplacement(ideDataBean.getNumeroIDERemplacement());
            annonce.setIdeAnnonceCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_RECEPTION);
            annonce.setIdeAnnonceDateCreation(JACalendar.todayJJsMMsAAAA());
            annonce.setIdeAnnonceEtat(CodeSystem.ETAT_ANNONCE_IDE_ENREGISTRE);
            annonce.setIdeAnnonceType(ideDataBean.getTypeAnnonceIde());
            annonce.setHistAdresse(ideDataBean.getAdresse());
            annonce.setHistLocalite(ideDataBean.getLocalite());
            annonce.setHistNPA(ideDataBean.getNpa());
            annonce.setHistRue(ideDataBean.getRue() + " " + ideDataBean.getNumeroRue());
            annonce.setHistCanton(ideDataBean.getCanton());
            annonce.setHistLangueTiers(ideDataBean.getLangue());
            annonce.setHistBrancheEconomique(ideDataBean.getBrancheEconomique());
            annonce.setHistFormeJuridique(ideDataBean.getPersonnaliteJuridique());
            annonce.setHistNumeroIde(ideDataBean.getNumeroIDE());
            annonce.setHistRaisonSociale(ideDataBean.getRaisonSociale());
            annonce.setHistStatutIde(ideDataBean.getStatut());
            annonce.setHistNoga(ideDataBean.getNogaCode());
            annonce.setHistNaissance(ideDataBean.getNaissance());
            annonce.add(transaction);

        } finally {

            if (transaction.hasErrors() || transaction.hasWarnings()) {
                transaction.rollback();
                transaction.closeTransaction();
                throw new Exception(
                        "AFIdeReceptionMessageInfoAboSedexProcess.generateAnnonceIdeEntrante : unable to add annonce due to error/warning in transaction : "
                                + transaction.getErrors().toString()
                                + " / "
                                + transaction.getWarnings().toString()
                                + " you must check sedexMessage in error and put them one more in Inbox");
            } else {
                transaction.commit();
                transaction.closeTransaction();
            }

        }

    }

    @OnReceive
    public void onReceive(SedexMessage messageSedexInfoAbo) throws Exception {

        // permet de gérer les messages groupés de Sedex
        if (messageSedexInfoAbo instanceof GroupedSedexMessage) {
            for (SimpleSedexMessage message : ((GroupedSedexMessage) messageSedexInfoAbo).simpleMessages) {
                receiveSimpleMessage(message);
            }
        } else if (messageSedexInfoAbo instanceof SimpleSedexMessage) {
            receiveSimpleMessage(messageSedexInfoAbo);
        }

    }

    private void receiveSimpleMessage(SedexMessage messageSedexInfoAbo) throws Exception {
        IDEDataBean ideDataBean = readInfoAboMessage(messageSedexInfoAbo);
        generateAnnonceIdeEntrante(ideDataBean);
    }

}
