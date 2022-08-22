package globaz.corvus.process.cron;

import acor.ch.admin.zas.rc.annonces.rente.pool.PoolAntwortVonZAS;
import ch.globaz.common.properties.PropertiesException;
import com.google.common.annotations.VisibleForTesting;
import globaz.corvus.properties.REProperties;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.smtp.JadeSmtpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * CRON permettant d'importer les annonces 51 et 53
 */
@Slf4j
public class REImportAnnoncesAdaptationsRentes extends BProcess {

    private static final String XML_EXTENSION = ".xml";
    private static final String XSD_FOLDER = "/xsd/acor/xsd/annoncesRC/";
    private static final String XSD_FILE_NAME = "AntwortVonZas.xsd";
    private static final String ANNONCES_SCHEMA = "acor.ch.admin.zas.rc.annonces.rente.pool";
    private Schema xsdSchema;

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            LOG.info("Lancement du process d'importation des Annonces 51.");
            //Pas d'envoi de mail automatique du process, tout est géré manuellement
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);

            initBsession();

            importFiles();

        } catch (Exception e) {
            // TODO : gestion des erreurs : voir si le mail on error ne suffit pas en surchargeant le getAdresseEmail()
            sendErrorMail("Les erreurs");
        } finally {
            closeBsession();
        }

        return true;
    }

    private void importFiles() throws PropertiesException, JadeServiceActivatorException, JadeClassCastException, JadeServiceLocatorException, JAXBException {
        LOG.info("Récupération des fichiers xml de la CdC");
        // récupération des fichiers distants
        // TODO : créer une nouvelle propriété de connexion au sftp dans le dossier fromZas et non toZas
        String urlFtpCdC = REProperties.URL_CENTRALE_ADAPTATIONS_RENTES.getValue();
        List<String> repositoryFtpCdc = JadeFsFacade.getFolderChildren(urlFtpCdC);
        // création du répertoire de travail local
        String localFolder = Jade.getInstance().getPersistenceDir() + "annonces51_53/";
        if (!JadeFsFacade.exists(localFolder)) {
            JadeFsFacade.createFolder(localFolder);
        }
        // copie des fichiers
        for (String nomFichierDistant : repositoryFtpCdc) {
            String nameOriginalFile = FilenameUtils.getName(nomFichierDistant);
            // TODO : comment filtrer tous les fichiers ?
            if (nameOriginalFile.endsWith(XML_EXTENSION) && nameOriginalFile.startsWith("R")) {
                String localFile = localFolder + nameOriginalFile;
                JadeFsFacade.copyFile(nomFichierDistant, localFile);
                // TODO : traitement du fichier local
                traitementFichier(localFile);
                JadeFsFacade.delete(localFolder + nameOriginalFile);
            }
        }
    }

    private void traitementFichier(String localFile) throws JadeServiceActivatorException, JadeClassCastException, JadeServiceLocatorException, JAXBException {
        String tmpLocalWorkFile = JadeFsFacade.readFile(localFile);
        File annonce51ou53File = new File(tmpLocalWorkFile);
        if (annonce51ou53File.isFile()) {
//            if (validateXml(annonce51ou53File)) {
            PoolAntwortVonZAS annonces = getAnnonces(annonce51ou53File);
//            }
        }
    }

    private PoolAntwortVonZAS getAnnonces(File annonce51ou53File) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ANNONCES_SCHEMA);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (PoolAntwortVonZAS) unmarshaller.unmarshal(annonce51ou53File);
    }

    private boolean validateXml(File annonce51ou53File) {
        try {
            Optional<Schema> theSchema = loadXsdSchema();
            if (theSchema.isPresent()) {
                xsdSchema = theSchema.get();
                Validator validator = xsdSchema.newValidator();
                validator.validate(new StreamSource(annonce51ou53File));
                return true;
            } else {
                LOG.error("Impossible de lire l'url du xsd: {}", XSD_FOLDER + XSD_FILE_NAME);
                return false;
            }
        } catch (SAXException | NullPointerException | IOException e) {
            LOG.error("e fichier xml fourni n'est pas valide.", e);
            return false;
        }
    }

    @VisibleForTesting
    Optional<Schema> loadXsdSchema() {
        if (Objects.isNull(xsdSchema)) {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL url = getClass().getResource(XSD_FOLDER + XSD_FILE_NAME);
            if (Objects.nonNull(url)) {
                try {
                    return Optional.of(factory.newSchema(url));
                } catch (SAXException e) {
                    LOG.error("Impossible de charger le schema xsd: {}", url, e);
                }
            }
        }
        return Optional.ofNullable(xsdSchema);
    }


    private void sendErrorMail(String errors) throws Exception {
        JadeSmtpClient.getInstance().sendMail(getEMailAddressAdaptationRentes(), getEMailObject(), errors, null);
    }

    private String getEMailAddressAdaptationRentes() throws PropertiesException {
        String eMailAddress = REProperties.DESTINATAIRE_MAIL_ERREURS_ADAPTATIONS_RENTES.getValue();

        if (((eMailAddress == null) || (eMailAddress.length() == 0)) && getSession() != null) {
            return getSession().getUserEMail();
        }
        return eMailAddress;
    }

    private void initBsession() throws Exception {
        LOG.info("Initialisation de la session");
        BSessionUtil.initContext(getSession(), this);
    }

    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    @Override
    protected void _executeCleanUp() {
        //Nothing to do
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }
}
