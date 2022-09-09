package globaz.corvus.process.cron;

import acor.ch.admin.zas.rc.annonces.rente.pool.PoolAntwortVonZAS;
import acor.ch.admin.zas.rc.annonces.rente.rc.RRBestandesmeldung10Type;
import acor.ch.admin.zas.rc.annonces.rente.rc.RRBestandesmeldung9Type;
import ch.globaz.common.properties.PropertiesException;
import globaz.corvus.db.annonces.REAnnonce51;
import globaz.corvus.db.annonces.REAnnonce53;
import globaz.corvus.db.annonces.REFicheAugmentation;
import globaz.corvus.properties.REProperties;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.adaptation.rentes.REAnnonces51Mapper;
import globaz.corvus.utils.adaptation.rentes.REAnnonces53Mapper;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.smtp.JadeSmtpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * CRON permettant d'importer les annonces 51 et 53
 */
@Slf4j
public class REImportAnnoncesAdaptationsRentes extends BProcess {

    private static final String XML_EXTENSION = ".xml";
    private static final String ANNONCES_SCHEMA = "acor.ch.admin.zas.rc.annonces.rente.pool";
    public static final String DOSSIER_ANNONCES_51_53 = "annonces51_53/";
    private static final String MAIL_ERROR_CONTENT = "ADAPTATIONS_RENTES_MAIL_ERROR_CONTENT";
    private static final String MAIL_ERROR_CONTENT_51 = "ADAPTATIONS_RENTES_MAIL_ERROR_CONTENT_51";
    private static final String MAIL_ERROR_CONTENT_53 = "ADAPTATIONS_RENTES_MAIL_ERROR_CONTENT_53";
    private static final String MAIL_ERROR_SUBJECT = "ADAPTATIONS_RENTES_MAIL_ERROR_SUBJECT";
    private JADate datePmtMensuel;
    private LocalDateTime dateDuTraitement;
    private List<REProtocoleErreurAdaptationsRentes> protocoles = new ArrayList<>();

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            LOG.info("Lancement du process d'importation des Annonces 51.");
            //Pas d'envoi de mail automatique du process, tout est géré manuellement
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);

            initBsession();
            importFiles();

            if (!protocoles.isEmpty()) {
                StringBuilder errors = generateErrorMailContent();
                sendErrorMail(errors.toString());
            }

        } catch (Exception e) {
            sendErrorMail(e.getMessage());
        } finally {
            closeBsession();
        }

        return true;
    }

    private StringBuilder generateErrorMailContent() {
        StringBuilder errors = new StringBuilder();
        String content = getSession().getLabel(MAIL_ERROR_CONTENT);
        String content51 = getSession().getLabel(MAIL_ERROR_CONTENT_51);
        String content53 = getSession().getLabel(MAIL_ERROR_CONTENT_53);
        for (REProtocoleErreurAdaptationsRentes eachProtocole : protocoles) {
            errors.append(FWMessageFormat.format(content, eachProtocole.getFichier()));
            if (StringUtils.isNotEmpty(eachProtocole.getAnnonces51enErreur())) {
                errors.append(FWMessageFormat.format(content51, eachProtocole.getAnnonces51enErreur()));
            }
            if (StringUtils.isNotEmpty(eachProtocole.getAnnonces53enErreur())) {
                errors.append(FWMessageFormat.format(content53, eachProtocole.getAnnonces53enErreur()));
            }
        }
        return errors;
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
            PoolAntwortVonZAS annonces = getAnnonces(annonce51ou53File);
            datePmtMensuel = new JADate(REPmtMensuel.getDateProchainPmt(getSession()));
            dateDuTraitement = LocalDateTime.now();
            REProtocoleErreurAdaptationsRentes protocole = new REProtocoleErreurAdaptationsRentes(annonce51ou53File.getName());
            // Traitement des annonces 53
            traitementAnnonces53(annonces, protocole);
            // Traitement des annonces 51
            traitementAnnonces51(annonces, protocole);

            if (protocole.hasErrors()) {
                protocoles.add(protocole);
            } else {
                return true;
            }
        }
        return false;
    }

    private PoolAntwortVonZAS getAnnonces(File annonce51ou53File) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ANNONCES_SCHEMA);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (PoolAntwortVonZAS) unmarshaller.unmarshal(annonce51ou53File);
    }

    private void traitementAnnonces53(PoolAntwortVonZAS annonces, REProtocoleErreurAdaptationsRentes protocole) {
        annonces.getLot().stream().map(PoolAntwortVonZAS.Lot::getVAIKEmpfangsbestaetigungOrIKEroeffnungsermaechtigungOrIKUebermittlungsauftrag)
                .flatMap(Collection::stream)
                .filter(o -> o instanceof RRBestandesmeldung10Type)
                .map(o -> (RRBestandesmeldung10Type) o)
                .forEach((each) -> createAnnonce53(each, protocole));
    }

    private void traitementAnnonces51(PoolAntwortVonZAS annonces, REProtocoleErreurAdaptationsRentes protocole) {
        annonces.getLot().stream().map(PoolAntwortVonZAS.Lot::getVAIKEmpfangsbestaetigungOrIKEroeffnungsermaechtigungOrIKUebermittlungsauftrag)
                .flatMap(Collection::stream)
                .filter(o -> o instanceof RRBestandesmeldung9Type)
                .map(o -> (RRBestandesmeldung9Type) o)
                .forEach((each) -> createAnnonce51(each, protocole));
    }

    /**
     * @param bestandesmeldung10Type
     * @param protocole
     * @return
     */
    private void createAnnonce53(RRBestandesmeldung10Type bestandesmeldung10Type, REProtocoleErreurAdaptationsRentes protocole) {
        StringBuilder errorMessage = new StringBuilder();
        try {
            REAnnonces53Mapper annonces53Mapper = new REAnnonces53Mapper(getSession().getCurrentThreadTransaction());
            REAnnonce53 ann53 = null;
            if (Objects.nonNull(bestandesmeldung10Type.getOrdentlicheRente())) {
                errorMessage.append(bestandesmeldung10Type.getOrdentlicheRente().getLeistungsberechtigtePerson().getVersichertennummer()).append("-").append(bestandesmeldung10Type.getOrdentlicheRente().getLeistungsbeschreibung().getLeistungsart());
                ann53 = annonces53Mapper.createAnnonce53Ordinaire(bestandesmeldung10Type);
            } else if (Objects.nonNull(bestandesmeldung10Type.getAusserordentlicheRente())) {
                errorMessage.append(bestandesmeldung10Type.getAusserordentlicheRente().getLeistungsberechtigtePerson().getVersichertennummer()).append("-").append(bestandesmeldung10Type.getAusserordentlicheRente().getLeistungsbeschreibung().getLeistungsart());
                ann53 = annonces53Mapper.createAnnonce53Extraordinaire(bestandesmeldung10Type);
            } else if (Objects.nonNull(bestandesmeldung10Type.getHilflosenentschaedigung())) {
                errorMessage.append(bestandesmeldung10Type.getHilflosenentschaedigung().getLeistungsberechtigtePerson().getVersichertennummer()).append("-").append(bestandesmeldung10Type.getHilflosenentschaedigung().getLeistungsbeschreibung().getLeistungsart());
                ann53 = annonces53Mapper.createAnnonce53Indemnisation(bestandesmeldung10Type);
            }

            REFicheAugmentation ficheAugmentation = new REFicheAugmentation();
            ficheAugmentation.setSession(getSession());
            ficheAugmentation.setIdAnnonceHeader(ann53.getIdAnnonce());
            ficheAugmentation.setDateAugmentation(datePmtMensuel.toStrAMJ());
            ficheAugmentation.setDateTraitement(JadeDateUtil.getFormattedDateTime(Timestamp.valueOf(dateDuTraitement)));
            ficheAugmentation.add(getSession().getCurrentThreadTransaction());

        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la création des annonces 53. ", e);
            protocole.addAnnonces53enErreur(errorMessage.toString());
            clearErrorsWarning();
        }
    }

    private void clearErrorsWarning() {
        getSession().getErrors();
        getTransaction().clearErrorBuffer();
        getTransaction().clearWarningBuffer();
        getSession().getCurrentThreadTransaction().clearErrorBuffer();
        getSession().getCurrentThreadTransaction().clearWarningBuffer();
        getMemoryLog().clear();
        JadeThread.logClear();
    }


    /**
     * @param rrBestandesmeldung9Type
     */
    private void createAnnonce51(RRBestandesmeldung9Type rrBestandesmeldung9Type, REProtocoleErreurAdaptationsRentes protocole) {
        StringBuilder errorMessage = new StringBuilder();
        try {
            REAnnonces51Mapper annonces51Mapper = new REAnnonces51Mapper(getSession().getCurrentThreadTransaction());
            REAnnonce51 ann51 = null;
            if (Objects.nonNull(rrBestandesmeldung9Type.getOrdentlicheRente())) {
                errorMessage.append(rrBestandesmeldung9Type.getOrdentlicheRente().getLeistungsberechtigtePerson().getVersichertennummer()).append("-").append(rrBestandesmeldung9Type.getOrdentlicheRente().getLeistungsbeschreibung().getLeistungsart());
                ann51 = annonces51Mapper.createAnnonce51Ordinaire(rrBestandesmeldung9Type);
            } else if (Objects.nonNull(rrBestandesmeldung9Type.getAusserordentlicheRente())) {
                errorMessage.append(rrBestandesmeldung9Type.getAusserordentlicheRente().getLeistungsberechtigtePerson().getVersichertennummer()).append("-").append(rrBestandesmeldung9Type.getAusserordentlicheRente().getLeistungsbeschreibung().getLeistungsart());
                ann51 = annonces51Mapper.createAnnonce51Extraordinaire(rrBestandesmeldung9Type);
            } else if (Objects.nonNull(rrBestandesmeldung9Type.getHilflosenentschaedigung())) {
                errorMessage.append(rrBestandesmeldung9Type.getHilflosenentschaedigung().getLeistungsberechtigtePerson().getVersichertennummer()).append("-").append(rrBestandesmeldung9Type.getHilflosenentschaedigung().getLeistungsbeschreibung().getLeistungsart());
                ann51 = annonces51Mapper.createAnnonce51Indemnisation(rrBestandesmeldung9Type);
            }

            REFicheAugmentation ficheAugmentation = new REFicheAugmentation();
            ficheAugmentation.setSession(getSession());
            ficheAugmentation.setIdAnnonceHeader(ann51.getIdAnnonce());

            ficheAugmentation.setDateAugmentation(datePmtMensuel.toStrAMJ());
            ficheAugmentation.setDateTraitement(JadeDateUtil.getFormattedDateTime(Timestamp.valueOf(dateDuTraitement)));
            ficheAugmentation.add(getSession().getCurrentThreadTransaction());

        } catch (Exception e) {
            LOG.error("Une erreur s'est produite lors de la création des annonces 53. ", e);
            protocole.addAnnonces51enErreur(errorMessage.toString());
            clearErrorsWarning();
        }
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
        return getSession().getLabel(MAIL_ERROR_SUBJECT);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }
}
