package ch.globaz.common.sql;

import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.util.JadePersistenceUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import ch.globaz.common.exceptions.CommonTechnicalException;

public class QueryUpdateExecutor {

    public static <T> void executeUpdate(String query, Class<T> clazz, BSession session) {
        Transaction transaction = new Transaction(session);
        try {
            executeUpdate(query, clazz, transaction.getTransaction().getConnection());
        } finally {
            // dans tous les cas, si on a ouvert une nouvelle transaction, on la clôture.
            if (transaction != null) {
                transaction.closeOpenedTransaction();
            }
        }
    }

    private static <T> String createQuery(String query, Class<T> clazz, String schema) {
        if (query == null || query.trim().isEmpty()) {
            throw new CommonTechnicalException(clazz.getName() + " - " + "Unable to execute empty query (" + query
                    + ")");
        } else if (!query.toLowerCase().contains("schema")) {
            throw new CommonTechnicalException("schema is not specified in this query: " + query);
        }

        String sql = query.replace("schema", schema);
        sql = sql.replace("SCHEMA", schema);
        sql = sql.replaceAll("\n", " ").replaceAll("\t", " ");
        return sql;
    }

    private static void executeUpdate(String query, Class<?> clazz, Connection connection) {
        query = createQuery(query, clazz, JadePersistenceUtil.getDbSchema());
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            throw new CommonTechnicalException(clazz.getName() + " - " + "Unable to execute query (" + query
                    + "), a SQLException happend!", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    JadeLogger.warn(JadePersistenceManager.class,
                            "Problem to close statement in persistence manager, reason : " + e.toString());
                }
            }
        }
    }

}
