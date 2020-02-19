package globaz.aquila.process;

import aquila.ch.eschkg.Document;
import aquila.ch.eschkg.RcType;
import aquila.ch.eschkg.ScType;
import aquila.ch.eschkg.SpType;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.daemon.suividecompte.AbstractDaemon;
import com.google.common.base.Throwables;
import globaz.aquila.api.ICOApplication;
import globaz.aquila.application.COProperties;
import globaz.aquila.print.list.COResultELPExcel;
import globaz.aquila.print.list.elp.COMotifMessageELP;
import globaz.aquila.print.list.elp.COProtocoleELP;
import globaz.aquila.print.list.elp.COTypeELPFactory;
import globaz.aquila.process.elp.COInfoFileELP;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.smtp.JadeSmtpClient;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class COImportMessageELP extends AbstractDaemon {

    private static final Logger LOG = LoggerFactory.getLogger(COImportMessageELP.class);

    private static final String ELP_SCHEMA = "aquila.ch.eschkg";
    private static final String MAIL_SUBJECT = "ELP_MAIL_SUBJECT";
    private static final String BACKUP_FOLDER =  "/backup/";
    public static final String XML_EXTENSION = ".xml";

    private BSession bsession;
    private COProtocoleELP protocole;
    private String error = "";
    private String backupFolder;

    @Override
    public void run() {

        try {
            initBsession();
            String isActive = JadePropertiesService.getInstance().getProperty(COProperties.RECEPTION_MESSAGES_ELP.getProperty());
            if("true".equals(isActive)) {
                importFiles();
                generationProtocol();
            }

        } catch (Exception e) {
            error = "erreur fatale : " + Throwables.getStackTraceAsString(e);
            try {
                sendResultMail(null);
            } catch (Exception e1) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e1);
            }
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        } finally {
            closeBsession();
        }
    }

    private void initBsession() throws Exception {
        bsession = (BSession) GlobazServer.getCurrentSystem()
                .getApplication(ICOApplication.DEFAULT_APPLICATION_AQUILA)
                .newSession(getUsername(), getPassword());
        BSessionUtil.initContext(bsession, this);
        protocole = new COProtocoleELP();
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    /**
     * Traitement des fichiers eLP
     */
    private void importFiles() {
        try {

            String urlFichiersELP = JadePropertiesService.getInstance().getProperty(COProperties.REPERTOIRE_GESTION_MESSAGES_ELP.getProperty());
            List<String> repositoryELP = JadeFsFacade.getFolderChildren(urlFichiersELP);
            backupFolder = new File(urlFichiersELP).getAbsolutePath() + BACKUP_FOLDER;
            for(String nomFichierDistant : repositoryELP) {
                importFile(nomFichierDistant);
            }
        } catch (Exception e) {
            error = "erreur fatale : " + Throwables.getStackTraceAsString(e);
            LOG.error("COImportMessageELP#import : erreur lors de l'importation des fichiers", e);
        }
    }

    /**
     * Copie le fichier eLP en local, effectue le traitement d'importion, sauvegarde le fichier
     * dans le dossier backup, supprime le fichier de base
     * @param nomFichierDistant
     * @throws JAXBException
     */
    private void importFile(String nomFichierDistant) throws JAXBException {
        if (nomFichierDistant.endsWith(XML_EXTENSION)) {
            String tmpLocalWorkFile;
            String nameOriginalFile = FilenameUtils.getName(nomFichierDistant);
            String nameOriginalFileWithoutExt = FilenameUtils.removeExtension(nameOriginalFile);
            COInfoFileELP infos = COInfoFileELP.getInfosFromFile(nameOriginalFileWithoutExt);
            if(infos != null) {
                try {
                    tmpLocalWorkFile = JadeFsFacade.readFile(nomFichierDistant);
                    File eLPFile = new File(tmpLocalWorkFile);
                    if (eLPFile.isFile()) {
                        traitementFichier(getDocument(eLPFile), infos);
                    }

                    if(!JadeFsFacade.exists(backupFolder)){
                        JadeFsFacade.createFolder(backupFolder);
                    }
                    JadeFsFacade.copyFile(tmpLocalWorkFile, backupFolder+nameOriginalFile);
                    JadeFsFacade.delete(nomFichierDistant);
                    JadeFsFacade.delete(tmpLocalWorkFile);

                } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException e) {
                    LOG.error("COImportMessageELP#importFile : erreur lors de l'importation du fichier "+nameOriginalFile, e);
                    protocole.addMsgIncoherentInattendue(infos, e.getMessage());
                }
            } else {
                protocole.addMsgIncoherentNomFichier(nameOriginalFile);
            }
        }
    }

    /**
     * Return le document XML : unmarshall depuis le fichier
     * @param eLPFile
     * @return
     * @throws JAXBException
     */
    private Document getDocument(File eLPFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ELP_SCHEMA);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Document) unmarshaller.unmarshal(eLPFile);
    }

    /**
     * Traitement fichier selon le type
     * @param doc
     * @param infos
     */
    private void traitementFichier(Document doc, COInfoFileELP infos) {
        if(doc.getSC() != null) {
            traitement(doc.getSC(), infos);
        } else if (doc.getSP() != null) {
            traitement(doc.getSP(), infos);
        } else if (doc.getRC() != null) {
            traitement(doc.getRC(), infos);
        } else {
            protocole.addMsgIncoherentOther(infos, COTypeELPFactory.getCOTypeMessageELPFromXml(doc));
        }

    }

    /**
     * Traitement du fichier CDP
     * @param scType
     * @param infos
     */
    private void traitement(ScType scType, COInfoFileELP infos) {
        // TODO Non traité : indiquer le motif
        protocole.addnonTraite(scType, infos, COMotifMessageELP.POURSUITE_RADIE);
        // TODO Pas de dossier trouvé
        protocole.addMsgIncoherent(scType, infos, COMotifMessageELP.REF_INCOMPATIBLE);
        // TODO Traitement OK
        protocole.addMsgTraite(scType, infos);
    }

    /**
     * Traitement du fichier PV
     * @param spType
     * @param infos
     */
    private void traitement(SpType spType, COInfoFileELP infos) {
        // TODO Non traité : indiquer le motif
        protocole.addnonTraite(spType, infos, COMotifMessageELP.PV_NON_TRAITE);
        // TODO Pas de dossier trouvé
        protocole.addMsgIncoherent(spType, infos, COMotifMessageELP.REF_INCOMPATIBLE);
        // TODO Traitement OK
        protocole.addMsgTraite(spType, infos);
    }

    /**
     * Traitement du fichier ADB
     * @param rcType
     * @param infos
     */
    private void traitement(RcType rcType, COInfoFileELP infos) {
        // TODO Non traité : indiquer le motif
        protocole.addnonTraite(rcType, infos, COMotifMessageELP.RUBRIQUE_FRAIS_INDEF);
        // TODO Pas de dossier trouvé
        protocole.addMsgIncoherent(rcType, infos, COMotifMessageELP.REF_INCOMPATIBLE);
        // TODO Traitement OK
        protocole.addMsgTraite(rcType, infos);
    }

    /**
     * Génération du protocol retour
     * @throws Exception
     */
    private void generationProtocol() throws Exception {
        COResultELPExcel listResultELP =  new COResultELPExcel(bsession, protocole);
        sendResultMail(listResultELP.getOutputFile());
    }

    private void sendResultMail(String filesPath) throws Exception {
        JadeSmtpClient.getInstance().sendMail(getEMailAddress(), getEMailObject(), error, new String[] {filesPath});
    }

    protected String getEMailObject() {
        return bsession.getLabel(MAIL_SUBJECT) + JACalendar.todayJJsMMsAAAA();
    }

    public final java.lang.String getEMailAddress() {
        String eMailAddress = JadePropertiesService.getInstance().getProperty(COProperties.DESTINATAIRE_PROTOCOLE_MESSAGES_ELP.getProperty());

        if (((eMailAddress == null) || (eMailAddress.length() == 0)) && bsession != null) {
                return bsession.getUserEMail();
        }
        return eMailAddress;
    }

}
