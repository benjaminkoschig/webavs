package ch.globaz.vulpecula.util;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.mapping.JadeModelMappingProvider;
import globaz.jade.persistence.util.JadePersistenceUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade.SearchLotExecutor;

public class DBUtil {
    private static final String PARAMETER_ESCAPE_CHAR = ":";
    private static final String SCHEMA = "SCHEMA";

    /**
     * Retourne le langage courant de l'application selon leur format en base de
     * données (code système notamment).
     * 
     * @return Retourne le langage courant ("F" pour français, "D" pour allemand
     *         et "I" pour italien)
     */
    public static String getCurrentLanguage() {
        if (JadeThread.currentLanguage().equals("fr")) {
            return "F";
        } else if (JadeThread.currentLanguage().equals("de")) {
            return "D";
        } else if (JadeThread.currentLanguage().equals("it")) {
            return "I";
        }
        return "F";
    }

    /**
     * Génération d'une liste de paramètres pour les requêtes SQL à partir d'une liste de String.
     * 
     * @param liste Liste de paramètres
     * @return Un string dont chaque paramètre est séparé par une virgule
     */
    public static String generateParameters(final List<String> liste) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < liste.size(); i++) {
            if (i == 0) {
                sb.append(liste.get(i));
            } else {
                sb.append(",").append(liste.get(i));
            }
        }
        return sb.toString();
    }

    /**
     * Exécution d'une requête SQL par lot afin d'éviter des problèmes de longueur de requêtes. Les tuples retournées
     * sont retournées sous forme de liste de String. Cette méthode doit être principalement utilisé lorsque l'on
     * souhaite retourner des identifiants à partir d'une requête SQL.
     * 
     * @param ids
     * @param sql Requête SQL à exécuter qui doit contenir la chaîne ":inClause" et ":schema", remplacé par les ids.
     *            DOIT IDEALEMENT retourner une seule colonnes. Dans le cas contraire, les
     *            colonnes seront agrégées dans une seule liste.
     * @param clazz Class utilisé pour le logger
     * @return
     */
    public static List<String> executeQuery(final List<String> ids, final String sql, final Class<?> clazz) {
        List<String> validIds = RepositoryJade.searchByLot(ids, new SearchLotExecutor<String>() {
            @Override
            public List<String> execute(final List<String> ids) {
                List<String> validIds = new ArrayList<String>();
                String sqlToExecute = sql;
                sqlToExecute = replaceSchema(sqlToExecute);

                String whereClause = generateParameters(ids);
                sqlToExecute = sqlToExecute.replace(":inClause", whereClause);

                try {
                    List<HashMap<String, Object>> values = DBUtil.executeQuery(sqlToExecute, getClass());
                    for (Map<String, Object> value : values) {
                        for (Entry<String, Object> entry : value.entrySet()) {
                            validIds.add(entry.getValue().toString());
                        }
                    }
                } catch (JadePersistenceException e) {
                    e.printStackTrace();
                }

                return validIds;
            }
        });
        return validIds;
    }

    /**
     * @param value provenant de la DB
     * @return la value avec les caractères de stockages remplacés par ceux d'affichage
     */
    private static String convertChar(String value) {
        if (value != null) {
            value = (value).trim().replace('¬', '\'').replace('¢', '"');
        }
        return value;
    }

    /**
     * Exécute la requête SQL passée en paramètres.
     * 
     * @param sql String représentant une requête SQL
     * @param clazz Classe utilisée pour le logging
     * @return Structure de données représentant les enregistrements
     * @throws JadePersistenceException Si un problème de persistence est apparu
     */
    public static ArrayList<HashMap<String, Object>> executeQuery(final String sql, final Class<?> clazz)
            throws JadePersistenceException {
        checkSqlQuery(sql);
        checkContext(sql, clazz);

        String query = replaceSchema(sql);

        long currentTime = System.currentTimeMillis();

        Statement stmt = null;
        ResultSet resultSet = null;

        ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();

        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
            resultSet = stmt.executeQuery(query);

            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            while (resultSet.next()) {
                HashMap<String, Object> row = new HashMap<String, Object>();
                results.add(row);

                for (int i = 1; i <= columns; i++) {
                    if (resultSet.getObject(i) instanceof String) {
                        row.put(md.getColumnName(i), convertChar(String.valueOf(resultSet.getObject(i))));
                    } else {
                        row.put(md.getColumnName(i), resultSet.getObject(i));
                    }
                }
            }

        } catch (SQLException e) {
            logSQLException(query, e, clazz);
        } finally {
            closeResources(query, stmt, resultSet, currentTime, clazz);
        }
        return results;
    }

    /**
     * Exécute une requête SQL et retourne les enregistrements à partir de l'indice (offset) de taille (size) en
     * fonction des paramètres.
     * La requête SQL doit contenir "SCHEMA" en tant que nom de schéma et peut contenir des paramètres présent dans la
     * requête sous le format ":nomParametre".
     * La map de paramétrage ne DOIT pas contenir le séparateur ":" mais uniquement le nom du paramètre.
     * 
     * @param sql String représentant la requête SQL à lancer
     * @param namedParameters Les paramètres de la requête (Pour un paramètre :suivant, l'entrée dans la map
     *            correspondra à {suivant : value}
     * @param clazz Classe utilisé pour effectuer du logging
     * @return Nombre d'éléments retourné par la requête SQL
     * @throws JadePersistenceException Exception lancé si il y a des problèmes avec la persistence.
     */
    public static ArrayList<HashMap<String, Object>> executeQuery(final String sql, Map<String, String> params,
            int offset, int size, Class<?> clazz) throws JadePersistenceException {
        checkSqlQuery(sql);
        checkContext(sql, clazz);

        long currentTime = System.currentTimeMillis();

        String query = setParameters(sql, params);

        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();

        try {
            stmt = JadeThread.currentJdbcConnection().prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            resultSet = stmt.executeQuery();
            resultSet.absolute(offset);

            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            int currentSize = 0;
            while (resultSet.next() && currentSize < size) {
                HashMap<String, Object> row = new HashMap<String, Object>();
                results.add(row);

                for (int i = 1; i <= columns; i++) {
                    if (resultSet.getObject(i) instanceof String) {
                        row.put(md.getColumnName(i), convertChar(String.valueOf(resultSet.getObject(i))));
                    } else {
                        row.put(md.getColumnName(i), resultSet.getObject(i));
                    }
                }
                currentSize++;
            }

        } catch (SQLException e) {
            logSQLException(query, e, clazz);
        } finally {
            closeResources(query, stmt, resultSet, currentTime, clazz);
        }
        return results;
    }

    /**
     * Exécute une requête SQL et retourne le résultat
     * La requête SQL doit contenir "SCHEMA" en tant que nom de schéma et peut contenir une liste de paramètres qui
     * seront remplacés dans le prepareStatement.
     * 
     * @param sql String représentant la requête SQL à lancer
     * @param params Les paramètres de la requête
     * @param clazz Classe utilisé pour effectuer du logging
     * @return Structure de données représentant le résultat de la requête
     * @throws JadePersistenceException Exception lancé si il y a des problèmes avec la persistence.
     */
    public static ArrayList<HashMap<String, Object>> executeQuery(final String sql, List<String> params,
            final Class<?> clazz) throws JadePersistenceException {
        checkSqlQuery(sql);
        checkContext(sql, clazz);

        String query = replaceSchema(sql);

        long currentTime = System.currentTimeMillis();

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();

        try {
            stmt = JadeThread.currentJdbcConnection().prepareStatement(query);
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
            resultSet = stmt.executeQuery();

            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            while (resultSet.next()) {
                HashMap<String, Object> row = new HashMap<String, Object>();
                results.add(row);

                for (int i = 1; i <= columns; i++) {
                    if (resultSet.getObject(i) instanceof String) {
                        row.put(md.getColumnName(i), convertChar(String.valueOf(resultSet.getObject(i))));
                    } else {
                        row.put(md.getColumnName(i), resultSet.getObject(i));
                    }
                }
            }

        } catch (SQLException e) {
            logSQLException(query, e, clazz);
        } finally {
            closeResources(query, stmt, resultSet, currentTime, clazz);
        }
        return results;
    }

    /**
     * Compte le nombre d'enregistrement qu'une requête SQL retourne.
     * La requête SQL doit contenir "SCHEMA" en tant que nom de schéma et peut contenir des paramètres présent dans la
     * requête sous le format ":suivant".
     * La map de paramétrage ne DOIT pas contenir le séparateur ":" mais uniquement le nom du paramètre.
     * 
     * @param sql String représentant la requête SQL à lancer
     * @param namedParameters Les paramètres de la requête (Pour un paramètre :suivant, l'entrée dans la map
     *            correspondra à {suivant : value}
     * @param clazz Classe utilisé pour effectuer du logging
     * @return Nombre d'éléments retourné par la requête SQL
     * @throws JadePersistenceException Exception lancé si il y a des problèmes avec la persistence.
     */
    public static int count(final String sql, Map<String, String> namedParameters, Class<?> clazz)
            throws JadePersistenceException {
        checkSqlQuery(sql);
        checkContext(sql, clazz);

        long currentTime = System.currentTimeMillis();

        String query = setParameters(sql, namedParameters);

        query = "SELECT COUNT(*) FROM (" + query + ")";

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            stmt = JadeThread.currentJdbcConnection().prepareStatement(query);
            resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            logSQLException(query, e, clazz);
        } finally {
            closeResources(query, stmt, resultSet, currentTime, clazz);
        }
        return 0;
    }

    /**
     * Remplace la SCHEMA dans la requête SQL par le schéma en cours d'utilisation et assigne les paramètres à partir de
     * la map passé en paramètres.
     * 
     * @param sql Requête SQL à transformer
     * @param params Map de paramètres nommés
     * @return Nouvelle requête SQL à exécuter
     */
    private static String setParameters(final String sql, Map<String, String> params) {
        String query = sql;
        query = replaceSchema(sql);

        for (Map.Entry<String, String> parameter : params.entrySet()) {
            query = query.replace(PARAMETER_ESCAPE_CHAR + parameter.getKey(), parameter.getValue());
        }
        return query;
    }

    private static String replaceSchema(final String sql) {
        String query;
        query = sql.replace(SCHEMA, JadePersistenceUtil.getDbSchema());
        return query;
    }

    private static void logSQLException(final String sql, SQLException e, Class<?> clazz)
            throws JadePersistenceException {
        throw new JadePersistenceException(clazz.getName() + " - " + "Unable to execute query (" + sql
                + "), a SQLException happend!", e);
    }

    /**
     * Ferme les resources utilisées et log les informations si nécessaire
     * 
     * @param sql Requête SQL utilisé pour le Log
     * @param stmt Statement à fermer
     * @param resultSet ResultSet à fermer
     * @param currentTime Temps passé à l'exécution de la requête
     * @param clazz
     */
    private static void closeResources(String sql, Statement stmt, ResultSet resultSet, long currentTime, Class<?> clazz) {
        if (JadeModelMappingProvider.getInstance().isVerbose()) {
            JadeLogger.info(JadePersistenceManager.class, "Exec. time (" + (System.currentTimeMillis() - currentTime)
                    + "ms) - Perform search (" + clazz.getName() + ") - Query : " + sql);
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                JadeLogger.warn(JadePersistenceManager.class,
                        "Problem to close statement in persistence manager, reason : " + e.toString());
            }
        }

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                JadeLogger.warn(JadePersistenceManager.class,
                        "Problem to close resultSet in persistence manager, reason : " + e.toString());
            }
        }
    }

    /**
     * Contrôle que la requête SQL est bien saisie, dans le cas ou elle est null ou vide, retourne une
     * {@link IllegalArgumentException}
     * 
     * @param sql String représentant potentiellement une requête SQL
     * @throws IllegalArgumentException Retourne une IllegalArgumentException
     */
    private static void checkSqlQuery(String sql) {
        if (JadeStringUtil.isEmpty(sql)) {
            throw new IllegalArgumentException("La requête SQL est null ou une chaîne vide");
        }
    }

    /**
     * Contrôle la présence du context et la présence d'une connection JDBC dans le thread
     * 
     * @param sql Requête SQL servant au log
     * @param clazz Class à logger
     * @throws JadePersistenceException Exception retournée
     */
    private static void checkContext(String sql, Class<?> clazz) throws JadePersistenceException {
        if (JadeThread.currentContext() == null) {
            throw new JadePersistenceException(clazz.getName() + " - " + "Unable to execute query (" + sql
                    + ") without a thread context!");
        } else if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)) {
            throw new JadePersistenceException(clazz.getName() + " - " + "Unable to execute query (" + sql
                    + ") without an opened connection in the current thread context!");
        }
    }
}
