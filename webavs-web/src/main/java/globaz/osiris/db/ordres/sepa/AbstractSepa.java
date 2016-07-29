package globaz.osiris.db.ordres.sepa;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractSepa {
    public static /* final */ class SepaException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public SepaException() {
            super();
        }

        public SepaException(String message, Throwable cause) {
            super(message, cause);
        }

        public SepaException(String message) {
            super(message);
        }

        public SepaException(Throwable cause) {
            super(cause);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSepa.class);

    /** @throws SepaException en cas d'erreur de connexion au FTP. */
    protected FTPClient connect(String server, String user, String password) {
        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);

        try {
            ftp.connect(server);
            LOG.trace("FTP Answer after connecting: {}", ftp.getReplyString());

            int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                disconnectQuietly(ftp);
                throw new IOException("FTP server replyed with code " + replyCode);
            }

            if (!ftp.login(user, password)) {
                throw new IOException("unable to connect with the provided credentials: " + ftp.getReplyString());
            }
            LOG.trace("FTP Answer after login: {}", ftp.getReplyString());
        } catch (IOException e) {
            throw new SepaException("Unable to connect to FTP", e);
        }

        return ftp;
    }

    protected void disconnectQuietly(FTPClient ftp) {
        if (ftp != null) {
            try {
                ftp.disconnect();
            } catch (IOException e) {
                LOG.trace("exception during disconnect: " + e, e);
            }
        }
    }
}
