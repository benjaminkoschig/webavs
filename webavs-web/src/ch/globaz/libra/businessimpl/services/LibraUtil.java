package ch.globaz.libra.businessimpl.services;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThread;
import java.sql.SQLException;
import ch.globaz.libra.business.exceptions.LibraException;

public class LibraUtil {
    /**
     * @return Une transaction bas�e sur la connection jdbc du thread context
     * @throws LibraException
     *             Lev�e dans le cas o� le thread context n'est pas attach� au thread courant, si il ne contient pas de
     *             connection jdbc ou si la session n'est pas contenue dans le thread context
     */
    protected static BTransaction getCurrentTransaction() throws LibraException {
        // Contr�le qu'il y ait un thread context (�l�ment de session du
        // framework de persistance Jade)
        try {
            if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)
                    || JadeThread.currentJdbcConnection().isClosed()) {
                throw new LibraException("Unable to create transaction, thread context not started!");
            }
        } catch (SQLException e) {
            throw new LibraException("Unable to create transaction, problem to access jdbc connection!", e);
        }
        // R�cup�re l'�l�ment session du thread context - N�cessaire pour
        // l'interfa�age entre nouveau et ancien framework
        BSession session = (BSession) JadeThread.getTemporaryObject("bsession");
        if (session == null) {
            throw new LibraException("Unable to create transaction, session not defined in thread context!");
        }
        // R�cup�re la transaction courante
        return session.getCurrentThreadTransaction();
    }
}
