package ch.globaz.pegasus.tests.util;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.util.JadePersistenceUtil;
import java.sql.SQLException;
import java.sql.Statement;

public class PegasusPersistanceUtils {

    public static void deletwByCsPY(String tableName, Class<?> class1) throws JadePersistenceException {
        StringBuffer sql;
        String toDay = "";// JadeDateUtil.getYMDDate(new Date());
        String userSpy = "'%" + toDay + JadeThread.currentUserId() + "'";
        sql = new StringBuffer("DELETE FROM ").append(JadePersistenceUtil.getDbSchema()).append(".").append(tableName)
                .append(" WHERE CSPY like ").append(userSpy);
        PegasusPersistanceUtils.executeUpdate(sql.toString(), class1);

    }

    public static void deletwByPsPY(String tableName, Class<?> class1) throws JadePersistenceException {
        StringBuffer sql;
        String toDay = "";// JadeDateUtil.getYMDDate(new Date());
        String userSpy = "'%" + toDay + JadeThread.currentUserId() + "'";
        sql = new StringBuffer("DELETE FROM ").append(JadePersistenceUtil.getDbSchema()).append(".").append(tableName)
                .append(" WHERE PSPY like ").append(userSpy);
        PegasusPersistanceUtils.executeUpdate(sql.toString(), class1);

    }

    public static int executeUpdate(String sql, Class<?> class1) throws JadePersistenceException {
        long currentTime = System.currentTimeMillis();
        long dbExecTime = 0;
        if (JadeStringUtil.isEmpty(sql)) {
            return -1;
        }
        if (JadeThread.currentContext() == null) {
            throw new JadePersistenceException(class1.getName() + " - " + "Unable to execute query (" + sql
                    + ") without a thread context!");
        } else if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)) {
            throw new JadePersistenceException(class1.getName() + " - " + "Unable to execute query (" + sql
                    + ") without an opened connection in the current thread context!");
        }
        Statement stmt = null;
        int nbModifiedEntity = -1;
        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
            long time = System.currentTimeMillis();
            nbModifiedEntity = stmt.executeUpdate(sql);
            dbExecTime = System.currentTimeMillis() - time;
        } catch (SQLException e) {
            throw new JadePersistenceException(class1.getName() + " - " + "Unable to execute query (" + sql
                    + "), a SQLException happend!", e);

        } finally {
            if (stmt != null) {
                if (stmt != null) {
                    try {

                        stmt.close();
                    } catch (SQLException e) {
                        JadeLogger.warn(JadePersistenceManager.class,
                                "Problem to close statement in persistence manager, reason : " + e.toString());
                    }
                }
            }
            JadeLogger.info(JadePersistenceManager.class, "Exec. time (Total :"
                    + (System.currentTimeMillis() - currentTime) + "ms - DB :" + dbExecTime + "ms) -  Query : " + sql);
        }
        return nbModifiedEntity;
    }
}
