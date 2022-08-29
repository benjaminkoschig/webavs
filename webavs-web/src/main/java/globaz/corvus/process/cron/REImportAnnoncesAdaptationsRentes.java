package globaz.corvus.process.cron;

import acor.ch.admin.zas.rc.annonces.rente.pool.PoolAntwortVonZAS;
import acor.ch.admin.zas.rc.annonces.rente.rc.*;
import ch.globaz.common.properties.PropertiesException;
import com.google.common.annotations.VisibleForTesting;
import globaz.corvus.db.annonces.REAnnonce51;
import globaz.corvus.db.annonces.REAnnonce53;
import globaz.corvus.db.annonces.REFicheAugmentation;
import globaz.corvus.properties.REProperties;
import globaz.corvus.utils.adaptation.rentes.REAnnonces51Mapper;
import globaz.corvus.utils.adaptation.rentes.REAnnonces53Mapper;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
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
import java.util.Collection;
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
    public static final String DOSSIER_ANNONCES_51_53 = "annonces51_53/";
    private Schema xsdSchema;
    private JADate datePmtMensuel;

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

    private void importFiles() throws PropertiesException, JadeServiceActivatorException, JadeClassCastException, JadeServiceLocatorException, JAXBException, JAException {
        LOG.info("Récupération des fichiers xml de la CdC");
        // récupération des fichiers distants
        String urlFtpCdC = REProperties.URL_CENTRALE_ADAPTATIONS_RENTES.getValue();
        List<String> repositoryFtpCdc = JadeFsFacade.getFolderChildren(urlFtpCdC);
        // création du répertoire de travail local
        String localFolder = Jade.getInstance().getPersistenceDir() + DOSSIER_ANNONCES_51_53;
        if (!JadeFsFacade.exists(localFolder)) {
            JadeFsFacade.createFolder(localFolder);
        }
        // copie des fichiers
        for (String nomFichierDistant : repositoryFtpCdc) {
            String nameOriginalFile = FilenameUtils.getName(nomFichierDistant);
            if (nameOriginalFile.endsWith(XML_EXTENSION) && nameOriginalFile.startsWith("R")) {
                String localFile = localFolder + nameOriginalFile;
                JadeFsFacade.copyFile(nomFichierDistant, localFile);
                // TODO : traitement du fichier local
                boolean succes = traitementFichier(localFile);
                if (succes) {
                    JadeFsFacade.delete(localFolder + nameOriginalFile);
                }
            }
        }
    }

    private boolean traitementFichier(String localFile) throws JadeServiceActivatorException, JadeClassCastException, JadeServiceLocatorException, JAXBException, JAException {
        String tmpLocalWorkFile = JadeFsFacade.readFile(localFile);
        File annonce51ou53File = new File(tmpLocalWorkFile);
        if (annonce51ou53File.isFile()) {
            // TODO : actuellement la validation est en erreur
//            if (validateXml(annonce51ou53File)) {
                PoolAntwortVonZAS annonces = getAnnonces(annonce51ou53File);
                datePmtMensuel = new JADate(REPmtMensuel.getDateProchainPmt(getSession()));
                // Traitement des annonces 53
                traitementAnnonces53(annonces);
                // Traitement des annonces 51
                traitementAnnonces51(annonces);
//            }

        }
        return true;
    }

    private PoolAntwortVonZAS getAnnonces(File annonce51ou53File) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ANNONCES_SCHEMA);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (PoolAntwortVonZAS) unmarshaller.unmarshal(annonce51ou53File);
    }

    private void traitementAnnonces53(PoolAntwortVonZAS annonces) {
        annonces.getLot().stream().map(PoolAntwortVonZAS.Lot::getVAIKEmpfangsbestaetigungOrIKEroeffnungsermaechtigungOrIKUebermittlungsauftrag)
                .flatMap(Collection::stream)
                .filter(o -> o instanceof RRBestandesmeldung10Type)
                .map(o -> (RRBestandesmeldung10Type) o)
                .forEach(this::createAnnonce53);
    }

    private void traitementAnnonces51(PoolAntwortVonZAS annonces) {
        annonces.getLot().stream().map(PoolAntwortVonZAS.Lot::getVAIKEmpfangsbestaetigungOrIKEroeffnungsermaechtigungOrIKUebermittlungsauftrag)
                .flatMap(Collection::stream)
                .filter(o -> o instanceof RRBestandesmeldung9Type)
                .map(o -> (RRBestandesmeldung9Type) o)
                .forEach(this::createAnnonce51);
    }

    private void createAnnonce53(RRBestandesmeldung10Type bestandesmeldung10Type) {
        try {
            REAnnonces53Mapper annonces53Mapper = new REAnnonces53Mapper(getSession().getCurrentThreadTransaction());
            REAnnonce53 ann53 = null;
            if (Objects.nonNull(bestandesmeldung10Type.getOrdentlicheRente())) {
                ann53 = annonces53Mapper.createAnnonce53Ordinaire(bestandesmeldung10Type);
            } else if (Objects.nonNull(bestandesmeldung10Type.getAusserordentlicheRente())) {
                ann53 = annonces53Mapper.createAnnonce53Extraordinaire(bestandesmeldung10Type);
            } else if (Objects.nonNull(bestandesmeldung10Type.getHilflosenentschaedigung())) {
                ann53 = annonces53Mapper.createAnnonce53Indemnisation(bestandesmeldung10Type);
            }

            REFicheAugmentation ficheAugmentation = new REFicheAugmentation();
            ficheAugmentation.setSession(getSession());
            ficheAugmentation.setIdAnnonceHeader(ann53.getIdAnnonce());

            ficheAugmentation.setDateAugmentation(datePmtMensuel.toStrAMJ());
            ficheAugmentation.add(getSession().getCurrentThreadTransaction());

        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la création des annonces 53. ", e);
        }
    }


    private void createAnnonce51(RRBestandesmeldung9Type rrBestandesmeldung9Type) {
        try {
            REAnnonces51Mapper annonces51Mapper = new REAnnonces51Mapper(getSession().getCurrentThreadTransaction());
            REAnnonce51 ann51 = null;
            if (Objects.nonNull(rrBestandesmeldung9Type.getOrdentlicheRente())) {
                ann51 = annonces51Mapper.createAnnonce51Ordinaire(rrBestandesmeldung9Type);
            } else if (Objects.nonNull(rrBestandesmeldung9Type.getAusserordentlicheRente())) {
                ann51 = annonces51Mapper.createAnnonce51Extraordinaire(rrBestandesmeldung9Type);
            } else if (Objects.nonNull(rrBestandesmeldung9Type.getHilflosenentschaedigung())) {
                ann51 = annonces51Mapper.createAnnonce51Indemnisation(rrBestandesmeldung9Type);
            }

            REFicheAugmentation ficheAugmentation = new REFicheAugmentation();
            ficheAugmentation.setSession(getSession());
            ficheAugmentation.setIdAnnonceHeader(ann51.getIdAnnonce());

            ficheAugmentation.setDateAugmentation(datePmtMensuel.toStrAMJ());
            ficheAugmentation.add(getSession().getCurrentThreadTransaction());

        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la création des annonces 53. ", e);
        }
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
