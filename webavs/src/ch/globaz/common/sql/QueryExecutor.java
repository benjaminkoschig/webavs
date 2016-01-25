package ch.globaz.common.sql;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.mapping.JadeModelMappingProvider;
import globaz.jade.persistence.sql.JadeSqlConstantes;
import globaz.jade.persistence.util.JadePersistenceUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.common.business.exceptions.CommonTechnicalException;

public class QueryExecutor {

    public static <T> List<T> execute(String query, Class<T> clazz) {
        checkThreadContext(query, clazz);
        return executeQueryList(query, clazz, JadeThread.currentJdbcConnection(), JadePersistenceUtil.getDbSchema());
    }

    public static <T> List<T> execute(String query, Class<T> clazz, BSession session) {
        Transaction transaction = new Transaction(session);
        try {
            return executeQueryList(query, clazz, transaction.getTransaction().getConnection(),
                    JadePersistenceUtil.getDbSchema());
        } finally {
            // dans tous les cas, si on a ouvert une nouvelle transaction, on la clôture.
            if (transaction != null) {
                transaction.closeOpenedTransaction();
            }
        }
    }

    public static BigDecimal executeAggregate(String query) {
        checkThreadContext(query, QueryExecutor.class);
        return executeAggregate(query, BSessionUtil.getSessionFromThreadContext());
    }

    public static BigDecimal executeAggregate(String query, BSession session) {
        Transaction transaction = new Transaction(session);
        try {
            return executeAggregate(query, QueryExecutor.class, transaction.getTransaction().getConnection());
        } finally {
            // dans tous les cas, si on a ouvert une nouvelle transaction, on la clôture.
            if (transaction != null) {
                transaction.closeOpenedTransaction();
            }
        }
    }

    private static String underscoreify(String in) {
        StringBuilder sb = new StringBuilder();
        for (char c : in.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void checkThreadContext(String query, Class<?> clazz) {
        if (JadeThread.currentContext() == null) {
            throw new CommonTechnicalException(clazz.getName() + " - " + "Unable to execute query (" + query
                    + ") without a thread context!");
        } else if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)) {
            throw new CommonTechnicalException(clazz.getName() + " - " + "Unable to execute query (" + query
                    + ") without an opened connection in the current thread context!");
        }
    }

    static BigDecimal executeAggregate(String query, Class<?> clazz, Connection connextion) {
        query = createQuery(query, clazz, JadePersistenceUtil.getDbSchema());
        long currentTime = System.currentTimeMillis();
        ArrayList<HashMap<String, Object>> result = executeQuery(query, clazz, connextion);
        long timeQuery = System.currentTimeMillis() - currentTime;
        currentTime = System.currentTimeMillis();
        long timeMapping = System.currentTimeMillis() - currentTime;

        if (JadeModelMappingProvider.getInstance().isVerbose()) {
            JadeLogger.info(JadePersistenceManager.class, "Exec. time (" + (timeQuery + timeMapping) + "ms, query:"
                    + timeQuery + "ms, mapping:" + timeMapping + "ms) - Perform count (" + clazz.getName()
                    + ") - Query : " + query);
        }
        BigDecimal nb = BigDecimal.ZERO;

        if (!result.isEmpty()) {
            HashMap<String, Object> map = result.get(0);
            Object value = map.get("1");
            if (value != null) {
                if (value instanceof Integer) {
                    nb = BigDecimal.valueOf((Integer) value);
                } else if (value instanceof BigDecimal) {
                    nb = (BigDecimal) value;
                } else if (value instanceof Float) {
                    nb = BigDecimal.valueOf((Float) value);
                }
            }
        }
        return nb;
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

    static <T> List<T> executeQueryList(String query, Class<T> clazz, Connection connection, String schema) {
        query = createQuery(query, clazz, schema);
        long currentTime = System.currentTimeMillis();
        ArrayList<HashMap<String, Object>> listSql = executeQuery(query, clazz, connection);
        long timeQuery = System.currentTimeMillis() - currentTime;
        currentTime = System.currentTimeMillis();

        List<T> list = createList(clazz, listSql);

        long timeMapping = System.currentTimeMillis() - currentTime;

        if (JadeModelMappingProvider.getInstance().isVerbose()) {
            JadeLogger.info(JadePersistenceManager.class, "Exec. time (" + (timeQuery + timeMapping) + "ms, query:"
                    + timeQuery + "ms, mapping:" + timeMapping + "ms) - Perform search (" + clazz.getName()
                    + ") - Query : " + query);
        }
        return list;
    }

    private static ArrayList<HashMap<String, Object>> executeQuery(String sql, Class<?> clazz, Connection connection) {
        ResultSet resultSet = null;
        ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(sql);

            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            while (resultSet.next()) {
                HashMap<String, Object> row = new HashMap<String, Object>();
                results.add(row);

                for (int i = 1; i <= columns; i++) {
                    row.put(md.getColumnName(i), resultSet.getObject(i));
                }
            }

        } catch (SQLException e) {
            throw new CommonTechnicalException(clazz.getName() + " - " + "Unable to execute query (" + sql
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
        return results;
    }

    /**
     * Cette méthode permet de retourner une liste d'objets en fonction de la classe paramétrée.
     * Si la classe est d'un type primitif comme du String on itère et on retourne une liste de Strings.
     * 
     * @param class1
     * @param listSql
     * @return
     */
    private static <T> List<T> createList(Class<T> class1, ArrayList<HashMap<String, Object>> listSql) {
        Field[] fields = class1.getDeclaredFields();
        List<T> list = new ArrayList<T>();
        for (HashMap<String, Object> row : listSql) {
            T newObjet = null;
            try {
                newObjet = class1.newInstance();
            } catch (Exception e) {
                throw new CommonTechnicalException("Unable to create an instance for this class " + class1.getName(), e);
            }
            if (newObjet instanceof String) {
                // on retourne une liste de String
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    list.add((T) String.valueOf(entry.getValue()));
                }
            } else {
                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        String nameBd = underscoreify(field.getName()).toUpperCase();
                        Object value = row.get(nameBd);

                        if (value == null) {
                            value = row.get(field.getName().toUpperCase());
                        }

                        if (value != null) {
                            Object[] args = new Object[1];
                            args[0] = value;
                            String methodName = "set" + JadeStringUtil.firstLetterToUpperCase(field.getName());
                            if (String.class.isAssignableFrom(field.getType())) {
                                if (nameBd.startsWith("DATE")) {
                                    try {
                                        Method method = class1.getMethod(methodName, String.class);
                                        String str = String.valueOf(value);
                                        if (str.length() == 8) {
                                            value = Converters.dbReadDateAMJ(value, nameBd);
                                        } else {
                                            value = Converters.dbReadDateAM(value, nameBd);
                                        }
                                        method.invoke(newObjet, value);
                                    } catch (Exception e) {
                                        throw new CommonTechnicalException("Error durring introspection", e);
                                    }
                                } else {
                                    try {
                                        Method method = class1.getMethod(methodName, String.class);
                                        String val = String.valueOf(value).trim();
                                        if ("0".equals(val)) {
                                            val = null;
                                        }
                                        method.invoke(newObjet, val);
                                    } catch (Exception e) {
                                        throw new CommonTechnicalException("Error durring introspection", e);
                                    }
                                }
                            } else if (Boolean.class.isAssignableFrom(field.getType())) {
                                try {
                                    Method method = class1.getMethod(methodName, Boolean.class);
                                    Boolean val = JadeSqlConstantes.DB_BOOLEAN_TRUE.equals(String.valueOf(value));
                                    method.invoke(newObjet, val);
                                } catch (Exception e) {
                                    throw new CommonTechnicalException("Error durring introspection", e);
                                }
                            } else if (Integer.class.isAssignableFrom(field.getType())) {
                                try {
                                    Method method = class1.getMethod(methodName, Integer.class);
                                    Integer val = (Integer) value;

                                    method.invoke(newObjet, val);
                                } catch (Exception e) {
                                    throw new CommonTechnicalException("Error durring introspection", e);
                                }
                            }
                        }
                    }
                }
                list.add(newObjet);
            }

        }
        return list;
    }
}