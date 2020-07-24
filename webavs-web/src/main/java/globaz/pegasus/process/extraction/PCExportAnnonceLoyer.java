package globaz.pegasus.process;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import com.google.common.base.Throwables;
import globaz.aquila.process.elp.COProtocoleELP;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pegasus.process.extraction.PCExportAnnonceLoyerDonne;
import globaz.pegasus.process.extraction.PCExportAnnoneLoyerExcel;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PCExportAnnonceLoyer extends BProcess {

    private static final Logger LOG = LoggerFactory.getLogger(PCExportAnnonceLoyer.class);

    private static final String ELP_SCHEMA = "aquila.ch.eschkg";
    private static final String MAIL_SUBJECT = "Extraction des loyers";
    private static final String BACKUP_FOLDER = "/backup/";
    private static final String FOLDER = "C:\\temp2\\ExportLoyer\\CCVS";
    private static final String XML_EXTENSION = ".xml";
    private static final String DESTINATAIRE_PROTOCOLE = "ebko@globaz.ch";
    public static final String FILE_101 = "-101-";

    private COProtocoleELP protocole;
    private List<PCExportAnnonceLoyerDonne> donnees;
    private String error = "";
    private String backupFolder;

    public static void main(String[] args) throws Exception {
        BSession session = (BSession) GlobazSystem.getApplication("PEGASUS").newSession("ccjuglo","glob4az");
        PCExportAnnonceLoyer process = new PCExportAnnonceLoyer();
        process.setSession(session);
        process.executeProcess();
    }

    private void initBsession() throws Exception {
        BSessionUtil.initContext(getSession(), this);
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

            String urlFichiersELP = FOLDER;
            List<String> repositoryELP = JadeFsFacade.getFolderChildren(urlFichiersELP);
            backupFolder = new File(urlFichiersELP).getAbsolutePath() + BACKUP_FOLDER;
            donnees = new ArrayList<>();
            for (String nomFichierDistant : repositoryELP) {
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
     *
     * @param nomFichierDistant
     * @throws JAXBException
     */
    private void importFile(String nomFichierDistant) throws Exception {
        if (nomFichierDistant.contains(FILE_101) && nomFichierDistant.endsWith(XML_EXTENSION)) {
            String tmpLocalWorkFile;
            String nameOriginalFile = FilenameUtils.getName(nomFichierDistant);
            String nameOriginalFileWithoutExt = FilenameUtils.removeExtension(nameOriginalFile);
            try {
                tmpLocalWorkFile = JadeFsFacade.readFile(nomFichierDistant);
                File eLPFile = new File(tmpLocalWorkFile);
                if (eLPFile.isFile()) {
                    Message message = getDocument(eLPFile);
                    if(message != null) {
                        LOG.info("Traitement du fichier " + nameOriginalFile);
                        boolean traitementInSucces = traitementFichier(message);
                        if (traitementInSucces) {
                            //movingFile(nomFichierDistant, tmpLocalWorkFile, nameOriginalFile);
                        }
                    }
                }
            } catch (JadeServiceLocatorException | JadeClassCastException | JadeServiceActivatorException e) {
                LOG.error("COImportMessageELP#importFile : erreur lors de l'importation du fichier " + nameOriginalFile, e);
                //protocole.addMsgIncoherentInattendue(e.getMessage());
            }
        }
    }

    /**
     * Return le document XML : unmarshall depuis le fichier
     *
     * @param eLPFile
     * @return
     * @throws JAXBException
     */
    private Message getDocument(File xmlFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Message.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Object message = unmarshaller.unmarshal(xmlFile);
        if(message instanceof  Message) {
            return (Message) message;
        }
         return null;
    }

    /**
     * Traitement fichier selon le type
     *
     * @param doc
     * @param infos
     */
    private boolean traitementFichier(Message xml) {
        CaseType typeCase = xml.getContent().getCase();
        for(DecisionType decision : typeCase.getDecisions().getDecision()){
            if(decision.getCalculationElements().getRents() != null
                && decision.getCalculationElements().getRents().getGrossRental() > 0) {
                PCExportAnnonceLoyerDonne donne = new PCExportAnnonceLoyerDonne();

                PersonType personne = null;
                int nbEnfant = 0;
                for(PersonType pt : decision.getPersons().getPerson()){
                    if(pt.isRepresentative()) {
                        personne = pt;
                    }
                }
                if(personne == null) {
                    continue;
                }

                donne.setNap1(Long.toString(personne.getVn()));
                donne.setCswo(personne.getHousingMode());
                donne.setCsbe(personne.getVitalNeedsCategory());
                donne.setCsak_t(decision.getDeliveryOffice().getElOffice().toString());

                RentsType rents = decision.getCalculationElements().getRents();
                Long maxRente = rents.getMaxRent();
                Long montant = rents.getGrossRental();
                Long rentGrossTotal = rents.getRentGrossTotal();
                Long rentGrossTotalPart = rents.getRentGrossTotalPart();
                donne.setMamim(Long.toString(maxRente));
                donne.setMami(Long.toString(montant));
                donne.setCski_t(Long.toString(decision.getCalculationElements().getChildren()));
                donne.setMami_x(Long.toString(rentGrossTotal));
                donne.setMamip_x(Long.toString(rentGrossTotalPart));
                donnees.add(donne);
            }
        }
        return true;
    }

    /**
     * Permet de déplacer le fichier suite au traitement.
     *
     * @param nomFichierDistant
     * @param tmpLocalWorkFile
     * @param nameOriginalFile
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws JadeClassCastException
     */
    private void movingFile(String nomFichierDistant, String tmpLocalWorkFile, String nameOriginalFile) throws JadeServiceLocatorException, JadeServiceActivatorException, JadeClassCastException {
        if (!JadeFsFacade.exists(backupFolder)) {
            JadeFsFacade.createFolder(backupFolder);
        }
        JadeFsFacade.copyFile(tmpLocalWorkFile, backupFolder + nameOriginalFile);
        JadeFsFacade.delete(nomFichierDistant);
        JadeFsFacade.delete(tmpLocalWorkFile);
    }



    /**
     * Génération du protocol retour
     *
     * @throws Exception
     */
    private void generationProtocol() throws Exception {
        PCExportAnnoneLoyerExcel result = new PCExportAnnoneLoyerExcel(getSession(), donnees);
        sendResultMail(result.getOutputFile());
    }

    private void sendResultMail(String filesPath) throws Exception {
        JadeSmtpClient.getInstance().sendMail(getMailAddress(), getEMailObject(), error, new String[]{filesPath});
    }

    @Override
    protected String getEMailObject() {
        return MAIL_SUBJECT + JACalendar.todayJJsMMsAAAA();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    @Override
    protected void _executeCleanUp() {
        //Nothing to do
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            //Pas d'envoi de mail automatique du process, tout est géré manuellement
            this.setSendCompletionMail(false);
            this.setSendMailOnError(false);
            importFiles();
            generationProtocol();

        } catch (Exception e) {
            error = "erreur fatale : " + Throwables.getStackTraceAsString(e);
            try {
                sendResultMail(null);
            } catch (Exception e1) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e1);
            }
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }

        return true;
    }

    private String getMailAddress() {
        String eMailAddress = DESTINATAIRE_PROTOCOLE;

        if (((eMailAddress == null) || (eMailAddress.length() == 0)) && getSession() != null) {
            return getSession().getUserEMail();
        }
        return eMailAddress;
    }

}
