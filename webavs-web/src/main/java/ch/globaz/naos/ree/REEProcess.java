package ch.globaz.naos.ree;

import globaz.jade.common.Jade;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.message.JadeSedexMessageNotSentException;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.utils.PRStringFormatter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.naos.ree.domain.pojo.ProcessProperties;
import ch.globaz.naos.ree.process.REEProcess5053_101;
import ch.globaz.naos.ree.process.REEProcess5053_102;
import ch.globaz.naos.ree.process.REEProcess5054_101;
import ch.globaz.naos.ree.process.REEProcess5054_102;
import ch.globaz.naos.ree.protocol.Protocol5053;
import ch.globaz.naos.ree.protocol.Protocol5054;
import ch.globaz.naos.ree.protocol.ProtocolAndMessages;
import ch.globaz.naos.ree.protocol.SedexTechnicalProtocol;
import ch.globaz.naos.ree.protocol.SedexTechnicalProtocol5054;
import ch.globaz.naos.ree.sedex.Sedex5053_101;
import ch.globaz.naos.ree.sedex.Sedex5053_102;
import ch.globaz.naos.ree.sedex.Sedex5054_101;
import ch.globaz.naos.ree.sedex.Sedex5054_102;
import ch.globaz.naos.ree.sedex.SedexMessageSender;
import ch.globaz.naos.ree.tools.ExecutionMode;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.ProtocolUtils;
import ch.globaz.naos.ree.tools.SedexInfo;

public class REEProcess extends AbstractJadeJob {

    private static final String BR = "</br>";
    private static final String SUBJECT_MAIL = "Protocole de l'envoi des données REE via Sedex ";
    private static final String SUBJECT_5053 = "Protocole de livraison du registre des affiliés à l'OFS (5053)";
    private static final String SUBJECT_5054 = "Revenu salariés et indépendants (5054)";

    private static final Logger LOG = LoggerFactory.getLogger(REEProcess.class);

    private static final long serialVersionUID = -8719662775143831441L;
    private static final String PROTOCOL_FILE_NAME = "ProtocoleLivraisonRegistreAffilies_";
    private static final String PROTOCOL_FILE_EXTENSION = "protocole";
    private static final String PROTOCOL_FILE_DATE_FORMAT = "yyyy-MM-dd-hh-mm";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private ProcessProperties properties;

    private Date processStartDate;
    private Date sendingStartDate;

    /**
     * Information liées à la caisse
     */
    private InfoCaisse infoCaisse;

    /**
     * Argument d'entrée du processus
     */
    private String modeExecution;

    @Override
    public void run() throws RuntimeException {
        processStartDate = new Date();
        LOG.info("{} run()", getClass().getSimpleName());

        validate();

        ExecutionMode executionMode = getExecutionMode();

        LOG.info("{} run() : executionMode = [{}]", getClass().getSimpleName(), executionMode.name());

        try {
            SedexInfo sedexInfo = buildSedexInfos();

            if (ExecutionMode._5053.equals(executionMode)) {
                execute5053(sedexInfo);
            } else if (ExecutionMode._5053_AND_5054.equals(executionMode)) {
                execute5053And5054(sedexInfo);
            } else {
                throw new IllegalArgumentException("Invalid ExecutionMode value [" + executionMode + "].");
            }
        } catch (Exception exception) {
            LOG.error("Exception thrown during execution : {}", exception.toString());
            try {
                sendErrorMail(exception);
            } catch (Exception e) {
                LOG.error("Exception thrown when triing to send error mail : {}", exception.toString());
            }
            throw new RuntimeException(getClass().getName() + " Exception thrown in run() method : "
                    + exception.toString(), exception);
        }
    }

    private void sendErrorMail(Exception exception) throws Exception {
        String subject = SUBJECT_MAIL + "(" + getExecutionMode().getUserArg() + ") exécution du processus en erreur";
        StringBuilder body = new StringBuilder();
        String[] stack = ExceptionUtils.getStackFrames(exception);

        body.append("<strong>Le process a été interrompu à cause d'une erreur fatale</strong>" + BR);
        body.append(BR);
        for (String string : stack) {
            body.append(string + BR);
        }

        JadeSmtpClient.getInstance().sendMail(properties.getEmail(), subject, body.toString(), new String[] {});
    }

    private void execute5053(SedexInfo sedexInfo) throws Exception {
        List<String> idAffilies = new LinkedList<String>();

        REEProcess5053_101 process5053_101 = new REEProcess5053_101(getSession(), sedexInfo, infoCaisse);
        REEProcess5053_102 process5053_102 = new REEProcess5053_102(getSession(), sedexInfo, infoCaisse);

        SedexMessageSender sedex5053_101 = new Sedex5053_101(properties, infoCaisse, sedexInfo);
        SedexMessageSender sedex5053_102 = new Sedex5053_102(properties, infoCaisse, sedexInfo);

        ProtocolAndMessages pam_5053_101 = process5053_101.execute(idAffilies, sedex5053_101);
        ProtocolAndMessages pam_5053_102 = process5053_102.execute(idAffilies, sedex5053_102);

        sendingStartDate = new Date();

        SedexTechnicalProtocol sendProtocol53_101 = sedex5053_101.marshallAndValidate(pam_5053_101
                .getProcessProtocolAndMessages());

        SedexTechnicalProtocol sendProtocol53_102 = sedex5053_102.marshallAndValidate(pam_5053_102
                .getProcessProtocolAndMessages());

        Protocol5053 protocol5053 = buildProtocol5053(sedexInfo, pam_5053_101, pam_5053_102, sendProtocol53_101,
                sendProtocol53_102);

        sendMessages(sendProtocol53_101.getFiles());
        sendMessages(sendProtocol53_102.getFiles());

        List<String> content = new LinkedList<String>();
        content.add(ProtocolUtils.indent("Process") + " : " + getClass());
        content.add(ProtocolUtils.indent("Mode d'exécution") + " : " + modeExecution);
        content.add("");

        List<String> protocol53 = protocol5053
                .generateProtocolContent(SUBJECT_5053, processStartDate, sendingStartDate);
        content.addAll(protocol53);

        addTechnicalInfoToProtocol(sedexInfo, content);
        File file = createProtocolFile();

        FileUtils.writeLines(file, content);

        String subject = SUBJECT_MAIL + "(" + getExecutionMode().getUserArg() + ")";

        JadeSmtpClient.getInstance().sendMail(properties.getEmail(), subject, "Execution success",
                new String[] { file.getAbsolutePath() });
    }

    private void addTechnicalInfoToProtocol(SedexInfo sedexInfo, List<String> content) {
        content.add("");

        content.add("Informations techniques");
        content.add(ProtocolUtils.indent("Taille des lots") + " : " + properties.getTailleLot());
        content.add(ProtocolUtils.indent("Identifiant Sedex de l'expéditeur") + " : " + sedexInfo.getSedexSenderId());
        content.add(ProtocolUtils.indent("Identifiant Sedex du destinataire") + " : " + properties.getRecipientId());
        content.add("");
        content.add(ProtocolUtils.indent("Paramétrage du processus (fichier TransmissionREE.properties)"));
        content.add(ProtocolUtils.indent("Nom utilisateur") + " : " + properties.getName());
        content.add(ProtocolUtils.indent("E-mail ") + " : " + properties.getEmail());
        content.add(ProtocolUtils.indent("Télephone") + " : " + properties.getPhone());
        content.add(ProtocolUtils.indent("Département") + " : " + properties.getDepartment());
        content.add(ProtocolUtils.indent("Autre") + " : " + properties.getOther());

        content.add(ProtocolUtils.indent("Temps total") + " : "
                + ProtocolUtils.formatDuration(new Date().getTime() - processStartDate.getTime()));
    }

    private File createProtocolFile() throws IOException {
        String fileName = PROTOCOL_FILE_NAME + new SimpleDateFormat(PROTOCOL_FILE_DATE_FORMAT).format(processStartDate)
                + "." + PROTOCOL_FILE_EXTENSION;

        String persistenceFolderPath = Jade.getInstance().getPersistenceDir();
        String filePath = persistenceFolderPath + File.separatorChar + fileName;
        File file = new File(filePath);
        file.createNewFile();
        return file;
    }

    private void execute5053And5054(SedexInfo sedexInfo) throws Exception {
        List<String> idAffilies = new LinkedList<String>();

        // 5053
        REEProcess5053_101 process5053_101 = new REEProcess5053_101(getSession(), sedexInfo, infoCaisse);
        REEProcess5053_102 process5053_102 = new REEProcess5053_102(getSession(), sedexInfo, infoCaisse);

        SedexMessageSender sedex5053_101 = new Sedex5053_101(properties, infoCaisse, sedexInfo);
        SedexMessageSender sedex5053_102 = new Sedex5053_102(properties, infoCaisse, sedexInfo);

        ProtocolAndMessages pam_5053_101 = process5053_101.execute(idAffilies, sedex5053_101);
        ProtocolAndMessages pam_5053_102 = process5053_102.execute(idAffilies, sedex5053_102);

        // 5054
        REEProcess5054_101 process5054_101 = new REEProcess5054_101(getSession(), sedexInfo, infoCaisse);
        REEProcess5054_102 process5054_102 = new REEProcess5054_102(getSession(), sedexInfo, infoCaisse);

        SedexMessageSender sedex5054_101 = new Sedex5054_101(properties, infoCaisse, sedexInfo);
        SedexMessageSender sedex5054_102 = new Sedex5054_102(properties, infoCaisse, sedexInfo);

        ProtocolAndMessages pam_5054_101 = process5054_101.execute(idAffilies, sedex5054_101);
        ProtocolAndMessages pam_5054_102 = process5054_102.execute(idAffilies, sedex5054_102);

        sendingStartDate = new Date();

        // Envoi et génération du protocol 5053
        SedexTechnicalProtocol sendProtocol53_101 = sedex5053_101.marshallAndValidate(pam_5053_101
                .getProcessProtocolAndMessages());
        SedexTechnicalProtocol sendProtocol53_102 = sedex5053_102.marshallAndValidate(pam_5053_102
                .getProcessProtocolAndMessages());

        Protocol5053 protocol5053 = buildProtocol5053(sedexInfo, pam_5053_101, pam_5053_102, sendProtocol53_101,
                sendProtocol53_102);

        // ----------------------------

        SedexTechnicalProtocol5054 sendProtocol54_101 = (SedexTechnicalProtocol5054) sedex5054_101
                .marshallAndValidate(pam_5054_101.getProcessProtocolAndMessages());
        SedexTechnicalProtocol5054 sendProtocol54_102 = (SedexTechnicalProtocol5054) sedex5054_102
                .marshallAndValidate(pam_5054_102.getProcessProtocolAndMessages());

        Protocol5054 protocol5054 = new Protocol5054(pam_5054_101, sendProtocol54_101, pam_5054_102,
                sendProtocol54_102, infoCaisse, sedexInfo);

        sendMessages(sendProtocol53_101.getFiles());
        sendMessages(sendProtocol53_102.getFiles());
        sendMessages(sendProtocol54_101.getFiles());
        sendMessages(sendProtocol54_102.getFiles());

        // Génération du contenu du fichier de protocol
        List<String> content = new LinkedList<String>();
        content.add(ProtocolUtils.indent("Process") + " : " + getClass());
        content.add(ProtocolUtils.indent("Mode d'exécution") + " : " + modeExecution);
        content.add("");

        List<String> protocol53 = protocol5053
                .generateProtocolContent(SUBJECT_5053, processStartDate, sendingStartDate);
        content.addAll(protocol53);

        content.add("");
        content.add("");

        List<String> protocol54 = protocol5054.generateProtocol5054Content(SUBJECT_5054);
        content.addAll(protocol54);

        addTechnicalInfoToProtocol(sedexInfo, content);

        File file = createProtocolFile();

        FileUtils.writeLines(file, content);

        String subject = SUBJECT_MAIL + "(" + getExecutionMode().getUserArg() + ")";

        JadeSmtpClient.getInstance().sendMail(properties.getEmail(), subject, "Execution success",
                new String[] { file.getAbsolutePath() });

    }

    private Protocol5053 buildProtocol5053(SedexInfo sedexInfo, ProtocolAndMessages pam_5053_101,
            ProtocolAndMessages pam_5053_102, SedexTechnicalProtocol sendProtocol53_101,
            SedexTechnicalProtocol sendProtocol53_102) {

        int nombreAffilie = pam_5053_101.getBusinessMessages().size();
        int nombreDePaquetAffilies = sendProtocol53_101.getNombreLot();
        int nombreLiensAffilies = pam_5053_102.getBusinessMessages().size();
        int nombreDePaquetsLiensAffilies = sendProtocol53_102.getNombreLot();

        Protocol5053 protocol5053 = new Protocol5053(pam_5053_101, sendProtocol53_101, pam_5053_102,
                sendProtocol53_102, infoCaisse, sedexInfo, nombreAffilie, nombreDePaquetAffilies, nombreLiensAffilies,
                nombreDePaquetsLiensAffilies);

        return protocol5053;
    }

    /**
     * Validation des paramètres d'entrées du process
     * 
     * @throws PropertiesException
     */
    public void validate() throws IllegalArgumentException {
        LOG.info("validate()");
        try {
            validateProperties();
            getExecutionMode();

        } catch (RuntimeException e) {
            LOG.error("Exception thrown on validate() : " + e.toString(), e);
            throw e;
        }

        try {
            String numeroCaisseFormate = CommonProperties.KEY_NO_CAISSE.getValue();
            String numeroAgenceFormate = CommonProperties.NUMERO_AGENCE.getValue();

            numeroCaisseFormate = PRStringFormatter.indentLeft(numeroCaisseFormate, 3, "0");
            numeroAgenceFormate = PRStringFormatter.indentLeft(numeroAgenceFormate, 3, "0");

            int numeroCaisse = Integer.valueOf(numeroCaisseFormate);
            int numeroAgence = Integer.valueOf(numeroAgenceFormate);

            infoCaisse = new InfoCaisse(numeroCaisse, numeroAgence, numeroCaisseFormate, numeroAgenceFormate);

        } catch (PropertiesException exception) {
            throw new IllegalArgumentException("Property Exception " + exception.toString(), exception);
        }
    }

    private SedexInfo buildSedexInfos() throws JadeSedexDirectoryInitializationException {
        String sedexSenderId = JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId();
        boolean deliveryFlag = JadeSedexService.getInstance().getTestDeliveryFlag();
        return new SedexInfo(sedexSenderId, deliveryFlag);
    }

    @Override
    public String getDescription() {
        return "Process used to send REE data into Sedex system";
    }

    @Override
    public final String getName() {
        return getClass().getName();
    }

    /**
     * Retourne le mode d'exécution du processus entrée par l'utilisateur
     * 
     * @return le mode d'exécution du processus entrée par l'utilisateur
     */
    public String getModeExecution() {
        return modeExecution;
    }

    /**
     * Définit le mode d'exécution du processus entrée par l'utilisateur
     * 
     * @param modeExecution
     */
    public void setModeExecution(String modeExecution) {
        this.modeExecution = modeExecution;
    }

    private ExecutionMode getExecutionMode() throws IllegalArgumentException {
        return ExecutionMode.parseUserArg(getModeExecution());
    }

    public void setProperties(ProcessProperties properties) {
        this.properties = properties;
    }

    public ProcessProperties getProperties() {
        return properties;
    }

    /**
     * Réalise l'envoi des fichier XML marshallés
     * 
     * @param files
     * @throws JadeSedexMessageNotSentException
     */
    public void sendMessages(List<File> files) throws JadeSedexMessageNotSentException {

        JadeSedexService service = JadeSedexService.getInstance();
        SimpleSedexMessage sms = null;
        for (File file : files) {
            sms = new SimpleSedexMessage();
            sms.fileLocation = file.getAbsolutePath();
            service.sendSimpleMessage(sms);
        }
    }

    protected void validateProperties() throws IllegalArgumentException {
        // name, phone, mail et paquet obligatoire
        if (properties.getName() == null || properties.getName().isEmpty()) {
            throw new IllegalArgumentException("Properties [Name] value cannot be empty");
        }
        if (properties.getPhone() == null || properties.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Properties [Phone] value cannot be empty");
        }
        if (properties.getEmail() == null || properties.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Properties [Email] value cannot be empty");
        }
        if (properties.getTailleLot() < 1) {
            throw new IllegalArgumentException("Properties [TailleLot] value cannot be zero or negative");
        }
        // mail is a valid mail
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(properties.getEmail());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Properties [Email] value not valid");
        }
        if (!properties.getPhone().matches("[0-9]{10,20}")) {
            throw new IllegalArgumentException("Properties [Phone] value must be numeric between 10 and 20 digits");
        }

        if (properties.getRecipientId() == null || properties.getRecipientId().isEmpty()) {
            throw new IllegalArgumentException("Properties [getRecipientId] value cannot be empty");
        }
    }
}
