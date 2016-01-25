package globaz.cygnus.helpers.process;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.models.jobProcess.JadeProcessAbstractJob;

/**
 * 
 * Remplacé par l'écran des steps
 * 
 * @author jje
 * 
 */
@Deprecated
public class RFImporterAvasadJob extends JadeProcessAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String pathFile = "";

    @Override
    public void afterJob() throws JadeApplicationException, JadePersistenceException {
        // Auto-generated method stub
    }

    public String getPathFile() {
        return pathFile;
    }

    @Override
    public void runProcess() throws JadeApplicationException, JadePersistenceException {

        Map<Enum<?>, String> properties = new HashMap<Enum<?>, String>();
        properties.put(/* RFProcessImportationAvasadEnum.FILE_PATH_FOR_POPULATION */null, pathFile);

        /*
         * JadeProcessExecut process = JadeProcessServiceLocator.getJadeProcesService().createProcess(
         * RFProcessImportationAvasadEnum.PROCESS_KEY_IMPORTATION_AVASAD.toLabel(), properties);
         */

        JadeProcessServiceLocator.getJadeProcessCommonService().createEntitysAndExecuterFirstStep(/*
                                                                                                   * RFProcessImportationAvasadEnum
                                                                                                   * .
                                                                                                   * PROCESS_KEY_IMPORTATION_AVASAD
                                                                                                   * .toLabel()
                                                                                                   */"",
                getIdExecutionProcess(), properties);

    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

}
