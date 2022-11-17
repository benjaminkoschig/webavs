package globaz.orion.process;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.orion.business.models.pucs.PucsFileMerge;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;
import com.google.common.base.Throwables;
import com.google.gson.Gson;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.smtp.JadeSmtpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EBTreatPucsFilesProtocole {

    private List<EBTreatPucsFileProtocoleContainer> protocole = new ArrayList<>();
    private boolean hasError = false;
    private BSession session;
    private BProcess process;

    public EBTreatPucsFilesProtocole(BProcess process) {
        this.process = process;
        this.session = process.getSession();
    }

    public void addOkToProtocol(PucsFileMerge fileMerge) {
        String numAffile = "";
        fileMerge.clearDomParser();
        if (fileMerge.getPucsFile() != null) {
            numAffile = fileMerge.getPucsFile().getNumeroAffilie();
        }
        protocole.add(EBTreatPucsFileProtocoleContainer.of(fileMerge, "", numAffile, "OK"));
    }

    public void addWarnToProtocol(PucsFileMerge fileMerge, String message) {
        String numAffile = "";
        fileMerge.clearDomParser();
        if (fileMerge.getPucsFile() != null) {
            numAffile = fileMerge.getPucsFile().getNumeroAffilie();
        }
        protocole.add(EBTreatPucsFileProtocoleContainer.of(fileMerge, message, numAffile, "NON TRAITE"));
    }

    public void addErrorToProtocol(Throwable e, String messageInfo, PucsFileMerge fileMerge) {
        hasError = true;
        String message = "";
        String numAffile = "";
        fileMerge.clearDomParser();
        if (fileMerge.getPucsFile() != null) {
            numAffile += fileMerge.getPucsFile().getNumeroAffilie() + "\n";
        }
        message += messageInfo + "\n";
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
            String msg = "";
            for (JadeBusinessMessage jadeBusinessMessage : messages) {
                msg = msg
                        + JadeI18n.getInstance().getMessage(session.getIdLangueISO(),
                        jadeBusinessMessage.getMessageId()) + "\n";
            }
            message += session.getLabel("PROCESS_ERROR") + ": " + msg;
        } else {
            message += session.getErrors().toString() + process.getTransaction().getErrors().toString();
            if (e != null) {
                message += session.getLabel("PROCESS_ERROR") + ": " + e.getMessage();
            }
        }
        if (e != null) {
            message += "Stack: \t " + Throwables.getStackTraceAsString(e) + "\n";
        }
        if (JadeThread.logHasMessagesToLevel(JadeBusinessMessageLevels.ERROR)) {
            message += "Thread messages: "
                    + new Gson().toJson(JadeThread.logMessagesToLevel(JadeBusinessMessageLevels.ERROR)) + "\n\n";
        }

        protocole.add(EBTreatPucsFileProtocoleContainer.of(fileMerge, message, numAffile, "KO"));

    }

    public void sendProtocole(String emailAdress, BProcess process) throws Exception {
        String file = buildXlsProtocole().getAbsolutePath();
        String titre = session.getLabel(hasError ? "PROCESS_ERROR" : "PROCESS_SUCCES");
        String body = titre + " : ";
        body += session.getLabel("BATCH_PROTOCOLE_BODY");

        JadeSmtpClient.getInstance().sendMail(emailAdress, process.getName() + " - " + titre, body,  new String[] { file });
    }

    private File buildXlsProtocole() {
        Details detail = new Details();
        detail.add(session.getLabel("BATCH_PROTOCOLE_NB_FICHIER"), Integer.toString(protocole.size()));
        detail.newLigne();
        detail.add(session.getLabel("BATCH_PROTOCOLE_TRAITEMENT_DU"), Date.now().toString());

        SimpleOutputListBuilderJade simpleOutputListBuilderJade = SimpleOutputListBuilderJade.newInstance();

        File file = simpleOutputListBuilderJade.session(session)
                .outputNameAndAddPath(session.getLabel("BATCH_PROTOCOLE_NAME"))
                .globazTheme()
                .addTranslater()
                .addList(protocole)
                .classElementList(EBTreatPucsFileProtocoleContainer.class)
                .addTitle(session.getLabel("BATCH_PROTOCOLE_TITRE"), Align.CENTER)
                .addHeaderDetails(detail)
                .asXls()
                .build();

        simpleOutputListBuilderJade.close();
        return file;
    }
}
