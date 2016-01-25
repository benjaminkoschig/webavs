package globaz.corvus.api.arc.downloader;

import globaz.corvus.api.external.arc.REDownloaderException;
import globaz.globall.api.BISession;

public abstract class REAbstractDownloader {

    private BISession session = null;

    void assertBeforeExecute() throws REDownloaderException {
        if (session == null) {
            throw new REDownloaderException("Session is not set");
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
