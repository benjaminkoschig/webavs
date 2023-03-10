package globaz.cygnus.process.adaptationAnnuelle.octroi;

import globaz.cygnus.mappingXmlml.IRFAdaptationsJournalieresListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogAdaptationJournaliere;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;

public class RFProcessAdaptationAnnuelleOctroiStep implements JadeProcessStepInterface, JadeProcessStepAfterable {

    private List<String[]> logsList = new ArrayList<String[]>();

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        try {
            RFXmlmlContainer container = RFXmlmlMappingLogAdaptationJournaliere.loadResults(logsList,
                    new ArrayList<String>(), null);

            String nomDoc = BSessionUtil.getSessionFromThreadContext().getLabel("PROCESS_ADAPTATION_ANNUELLE_OCTROI");
            String docPath = RFExcelmlUtils.createDocumentExcel(BSessionUtil.getSessionFromThreadContext()
                    .getIdLangueISO().toUpperCase()
                    + "/" + IRFAdaptationsJournalieresListeColumns.MODEL_NAME, nomDoc, container);

            JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(),
                    "Process adaptation annuelle RFM: Octroi", "R?sultat du process des adaptations annuelles: Octroi",
                    new String[] { docPath });

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
        }

    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new RFProcessAdaptationAnnuelleOctroiHandlerEntity(logsList);
    }

}
