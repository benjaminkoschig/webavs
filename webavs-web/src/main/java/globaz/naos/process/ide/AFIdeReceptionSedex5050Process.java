package globaz.naos.process.ide;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import ch.admin.bfs.xmlns.bfs_5050_000101._1.Message;
import ch.admin.bfs.xmlns.bfs_5050_000101._1.SHABMsgType;
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
import globaz.naos.util.IDEDataBean;
import globaz.naos.util.ide.IDEFoscConverter;

public class AFIdeReceptionSedex5050Process {

    private static final String PACKAGE_CLASS_SEDEX_FOSC_MESSAGE = "ch.admin.bfs.xmlns.bfs_5050_000101._1";

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

    private List<IDEDataBean> readFoscMessage(SedexMessage messageSedex) throws FileNotFoundException, JAXBException {

        InputStream fileInputStream = new FileInputStream(messageSedex.getFileLocation());

        Message message = (Message) unmarshaller.unmarshal(fileInputStream);

        List<IDEDataBean> ideDataBeanList = new ArrayList<IDEDataBean>();

        for (SHABMsgType msgFosc : message.getContent().getSHABMeldung()) {
            ideDataBeanList.add(IDEFoscConverter.formatdata(msgFosc));
        }

        return ideDataBeanList;
    }

    @Setup
    public void setUp(Properties properties)
            throws JadeDecryptionNotSupportedException, JadeEncrypterNotFoundException, Exception {

        String encryptedUser = properties.getProperty("userSedex");
        if (encryptedUser == null) {
            JadeLogger.error(this, "AFIdeReceptionSedex5050Process : user sedex null");
            throw new IllegalStateException("AFIdeReceptionSedex5050Process : user sedex null");
        }
        String decryptedUser = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "AFIdeReceptionSedex5050Process : password sedex null");
            throw new IllegalStateException("AFIdeReceptionSedex5050Process : password sedex null");
        }
        String decryptedPass = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        setUserSedex(decryptedUser);
        setPassSedex(decryptedPass);

        session = (BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).newSession(userSedex,
                passSedex);

        jaxbContext = JAXBContext.newInstance(PACKAGE_CLASS_SEDEX_FOSC_MESSAGE);
        unmarshaller = jaxbContext.createUnmarshaller();
    }

    private void generateAnnoncesEntrante(List<IDEDataBean> ideDataBeans) throws Exception {
        for (IDEDataBean ideDataBean : ideDataBeans) {

            BTransaction transaction = null;

            try {

                transaction = new BTransaction(session);
                transaction.openTransaction();

                // Création annonce
                AFIdeAnnonce annonce = new AFIdeAnnonce();
                annonce.setSession(session);
                annonce.setIdeAnnonceCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_RECEPTION);
                annonce.setIdeAnnonceType(CodeSystem.TYPE_ANNONCE_IDE_FOSC);
                annonce.setIdeAnnonceEtat(CodeSystem.ETAT_ANNONCE_IDE_ENREGISTRE);
                annonce.setIdeAnnonceDateCreation(JACalendar.todayJJsMMsAAAA());
                annonce.setNumeroIde(ideDataBean.getNumeroIDE());
                annonce.setHistNumeroIde(ideDataBean.getNumeroIDE());
                annonce.setStatutIde(ideDataBean.getStatut());
                annonce.setHistRaisonSociale(ideDataBean.getRaisonSociale());
                annonce.setHistRue(ideDataBean.getRue());
                annonce.setHistNPA(ideDataBean.getNpa());
                annonce.setHistCanton(ideDataBean.getCanton());
                annonce.setHistLocalite(ideDataBean.getLocalite());
                annonce.setHistNoga(ideDataBean.getNogaCode());
                annonce.setMessageSedex50(ideDataBean.getMessageSedex50());
                annonce.add(transaction);

            } finally {

                if (transaction.hasErrors() || transaction.hasWarnings()) {
                    transaction.rollback();
                    transaction.closeTransaction();
                    throw new Exception(
                            "AFIdeReceptionMessageSedex5050Process.generateAnnoncesEntrante : unable to add annonce due to error/warning in transaction : "
                                    + transaction.getErrors().toString() + " / " + transaction.getWarnings().toString()
                                    + " you must check sedexMessage in error and put them one more in Inbox");
                } else {
                    transaction.commit();
                    transaction.closeTransaction();
                }

            }
        }
    }

    @OnReceive
    public void onReceive(SedexMessage messageSedexInfoAbo) throws Exception {

        // permet de gérer les messages groupés de Sedex
        if (messageSedexInfoAbo instanceof GroupedSedexMessage) {
            for (SimpleSedexMessage message : ((GroupedSedexMessage) messageSedexInfoAbo).simpleMessages) {
                receiveAsSimpleMessage(message);
            }
        } else if (messageSedexInfoAbo instanceof SimpleSedexMessage) {
            receiveAsSimpleMessage(messageSedexInfoAbo);
        }

    }

    private void receiveAsSimpleMessage(SedexMessage messageSedex) throws Exception {
        List<IDEDataBean> ideDataBeans = readFoscMessage(messageSedex);

        generateAnnoncesEntrante(ideDataBeans);
    }

}
