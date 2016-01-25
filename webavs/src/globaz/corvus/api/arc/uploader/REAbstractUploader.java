package globaz.corvus.api.arc.uploader;

import globaz.corvus.api.external.arc.REUploaderException;
import globaz.globall.api.BISession;

public abstract class REAbstractUploader {

    private BISession session = null;

    void assertBeforeExecute() throws REUploaderException {
        if (session == null) {
            throw new REUploaderException("Session is not set");
        }
    }

    /**
     * @return the session
     */
    public BISession getSession() {
        return session;
    }

    /**
     * @param session
     *            the session to set
     */
    public void setSession(BISession session) {
        this.session = session;
    }
}
