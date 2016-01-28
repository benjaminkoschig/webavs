package globaz.cygnus.helpers.process;

// import globaz.cygnus.process.financementSoin.step1.RFProcessImportFinancementSoinEnum;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.models.jobProcess.JadeProcessAbstractJob;

public class RFimportFinancementSoinJob extends JadeProcessAbstractJob {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String eMail;
    private String pathFile;

    @Override
    public void afterJob() throws JadeApplicationException, JadePersistenceException {

    }

    public String getPathFile() {
        return pathFile;
    }

    @Override
    public void runProcess() throws JadeApplicationException, JadePersistenceException {

        Map<Enum<?>, String> properties = new HashMap<Enum<?>, String>();
        properties.put(RFProcessImportFinancementSoinEnum.FILE_PATH_FOR_POPULATION, pathFile);
        properties.put(RFProcessImportFinancementSoinEnum.EMAIL, eMail);

        // JadeProcessExecut process = JadeProcessServiceLocator.getJadeProcesService().createProcess(
        // "Cygnus.import.financement.soin", properties);

        JadeProcessServiceLocator.getJadeProcessCommonService().createEntitysAndExecuterFirstStep(
                "Cygnus.import.financement.soin", getIdExecutionProcess(), properties);

    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

}
