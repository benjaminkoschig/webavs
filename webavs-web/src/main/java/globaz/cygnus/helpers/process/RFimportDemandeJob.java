package globaz.cygnus.helpers.process;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.models.jobProcess.JadeProcessAbstractJob;

public class RFimportDemandeJob extends JadeProcessAbstractJob {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void afterJob() throws JadeApplicationException, JadePersistenceException {

    }

    @Override
    public void runProcess() throws JadeApplicationException, JadePersistenceException {

        JadeProcessServiceLocator.getJadeProcessCommonService().createEntitysAndExecuterFirstStep(getKeyProcess(),
                getIdExecutionProcess(), getProperties());
    }

}
