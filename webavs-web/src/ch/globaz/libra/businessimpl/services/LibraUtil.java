package ch.globaz.libra.businessimpl.services;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThread;
import java.sql.SQLException;
import ch.globaz.libra.business.exceptions.LibraException;

public class LibraUtil {
    /**
     * @return Une transaction basée sur la connection jdbc du thread context
     * @throws LibraException
     *             Levée dans le cas où le thread context n'est pas attaché au thread courant, si il ne contient pas de
     *             connection jdbc ou si la session n'est pas contenue dans le thread context
     */
    protected static BTransaction getCurrentTransaction() throws LibraException {
        // Contrôle qu'il y ait un thread context (élément de session du
        // framework de persistance Jade)
        try {
            if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)
                    || JadeThread.currentJdbcConnection().isClosed()) {
                throw new LibraException("Unable to create transaction, thread context not started!");
            }
        } catch (SQLException e) {
            throw new LibraException("Unable to create transaction, problem to access jdbc connection!", e);
        }
        // Récupère l'élément session du thread context - Nécessaire pour
        // l'interfaçage entre nouveau et ancien framework
        BSession session = (BSession) JadeThread.getTemporaryObject("bsession");
        if (session == null) {
            throw new LibraException("Unable to create transaction, session not defined in thread context!");
        }
        // Récupère la transaction courante
        return session.getCurrentThreadTransaction();
    }
}
