package globaz.itucana.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;

/**
 * Process permettant le lancement des process CG, CA, ACM et AF
 * 
 * @author fgo date de création : 13 juin 06
 * @version : version 1.0
 * 
 */
public class TUProcessHandler extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur process
     */
    public TUProcessHandler() {
        super();
    }

    /**
     * Constructeur process
     * 
     * @param parent
     */
    public TUProcessHandler(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur
     * 
     * @param session
     */
    public TUProcessHandler(BSession session) {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    /**
     * Exécute le process
     * 
     * @param monProcess
     */
    public void handle(TUProcessusBouclement monProcess) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

}
