package globaz.naos.process.ide;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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

public abstract class AFIdeAbstractReceptionSedex<T> implements AFIdeReceptionSedexInterface<T> {

    protected BSession session = null;
    protected JAXBContext jaxbContext = null;
    protected Unmarshaller unmarshaller = null;
    private String passSedex = "";
    private String userSedex = "";

    public AFIdeAbstractReceptionSedex() {
        super();
    }

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

    @Setup
    public void setUp(Properties properties)
            throws JadeDecryptionNotSupportedException, JadeEncrypterNotFoundException, Exception {

        String encryptedUser = properties.getProperty("userSedex");
        if (encryptedUser == null) {
            JadeLogger.error(this, this.getClass().getName() + " : user sedex null");
            throw new IllegalStateException(this.getClass().getName() + " : user sedex null");
        }
        String decryptedUser = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, this.getClass().getName() + " : password sedex null");
            throw new IllegalStateException(this.getClass().getName() + " : password sedex null");
        }
        String decryptedPass = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        setUserSedex(decryptedUser);
        setPassSedex(decryptedPass);

        session = (BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS).newSession(userSedex,
                passSedex);

        jaxbContext = JAXBContext.newInstance(getPackageClass());
        unmarshaller = jaxbContext.createUnmarshaller();
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

    void receiveAsSimpleMessage(SedexMessage messageSedex) throws Exception {
        List<IDEDataBean> ideDataBeans = readSedex50Message(messageSedex);

        generateAnnoncesEntrante(ideDataBeans);
    }

    public List<IDEDataBean> readSedex50Message(SedexMessage messageSedex) throws FileNotFoundException, JAXBException {

        InputStream fileInputStream = new FileInputStream(messageSedex.getFileLocation());

        T message = (T) unmarshaller.unmarshal(fileInputStream);

        return convertMessageToIdeDataBean(message);
    }

    public void generateAnnoncesEntrante(List<IDEDataBean> ideDataBeans) throws Exception {
        for (IDEDataBean ideDataBean : ideDataBeans) {

            BTransaction transaction = null;

            try {

                transaction = new BTransaction(session);
                transaction.openTransaction();

                // Création annonce
                AFIdeAnnonce annonce = new AFIdeAnnonce();
                annonce.setSession(session);
                annonce.setIdeAnnonceCategorie(CodeSystem.CATEGORIE_ANNONCE_IDE_RECEPTION);
                annonce.setIdeAnnonceType(ideDataBean.getTypeAnnonceIde());
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
                annonce.setHistNaissance(ideDataBean.getNaissance());
                annonce.setMessageSedex50(ideDataBean.getMessageSedex50());
                annonce.add(transaction);

            } finally {

                if (transaction.hasErrors() || transaction.hasWarnings()) {
                    transaction.rollback();
                    transaction.closeTransaction();
                    throw new Exception(this.getClass().getName()
                            + ".generateAnnoncesEntrante : unable to add annonce due to error/warning in transaction : "
                            + transaction.getErrors().toString() + " / " + transaction.getWarnings().toString()
                            + " you must check sedexMessage in error and put them one more in Inbox");
                } else {
                    transaction.commit();
                    transaction.closeTransaction();
                }

            }
        }
    }

}