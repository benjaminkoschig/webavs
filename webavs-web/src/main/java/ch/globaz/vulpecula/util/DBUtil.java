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
     * donn�es (code syst�me notamment).
     * 
     * @return Retourne le langage courant ("F" pour fran�ais, "D" pour allemand
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
     * G�n�ration d'une liste de param�tres pour les requ�tes SQL � partir d'une liste de String.
     * 
     * @param liste Liste de param�tres
     * @return Un string dont chaque param�tre est s�par� par une virgule
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
     * Ex�cution d'une requ�te SQL par lot afin d'�viter des probl�mes de longueur de requ�tes. Les tuples retourn�es
     * sont retourn�es sous forme de liste de String. Cette m�thode doit �tre principalement utilis� lorsque l'on
     * souhaite retourner des identifiants � partir d'une requ�te SQL.
     * 
     * @param ids
     * @param sql Requ�te SQL � ex�cuter qui doit contenir la cha�ne ":inClause" et ":schema", remplac� par les ids.
     *            DOIT IDEALEMENT retourner une seule colonnes. Dans le cas contraire, les
     *            colonnes seront agr�g�es dans une seule liste.
     * @param clazz Class utilis� pour le logger
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
     * @return la value avec les caract�res de stockages remplac�s par ceux d'affichage
     */
    private static String convertChar(String value) {
        if (value != null) {
            value = (value).trim().replace('�', '\'').replace('�', '"');
        }
        return value;
    }

    /**
     * Ex�cute la requ�te SQL pass�e en param�tres.
     * 
     * @param sql String repr�sentant une requ�te SQL
     * @param clazz Classe utilis�e pour le logging
     * @return Structure de donn�es repr�sentant les enregistrements
     * @throws JadePersistenceException Si un probl�me de persistence est apparu
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
     * Ex�cute une requ�te SQL et retourne les enregistrements � partir de l'indice (offset) de taille (size) en
     * fonction des param�tres.
     * La requ�te SQL doit contenir "SCHEMA" en tant que nom de sch�ma et peut contenir des param�tres pr�sent dans la
     * requ�te sous le format ":nomParametre".
     * La map de param�trage ne DOIT pas contenir le s�parateur ":" mais uniquement le nom du param�tre.
     * 
     * @param sql String repr�sentant la requ�te SQL � lancer
     * @param namedParameters Les param�tres de la requ�te (Pour un param�tre :suivant, l'entr�e dans la map
     *            correspondra � {suivant : value}
     * @param clazz Classe utilis� pour effectuer du logging
     * @return Nombre d'�l�ments retourn� par la requ�te SQL
     * @throws JadePersistenceException Exception lanc� si il y a des probl�mes avec la persistence.
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
     * Ex�cute une requ�te SQL et retourne le r�sultat
     * La requ�te SQL doit contenir "SCHEMA" en tant que nom de sch�ma et peut contenir une liste de param�tres qui
     * seront remplac�s dans le prepareStatement.
     * 
     * @param sql String repr�sentant la requ�te SQL � lancer
     * @param params Les param�tres de la requ�te
     * @param clazz Classe utilis� pour effectuer du logging
     * @return Structure de donn�es repr�sentant le r�sultat de la requ�te
     * @throws JadePersistenceException Exception lanc� si il y a des probl�mes avec la persistence.
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
     * Compte le nombre d'enregistrement qu'une requ�te SQL retourne.
     * La requ�te SQL doit contenir "SCHEMA" en tant que nom de sch�ma et peut contenir des param�tres pr�sent dans la
     * requ�te sous le format ":suivant".
     * La map de param�trage ne DOIT pas contenir le s�parateur ":" mais uniquement le nom du param�tre.
     * 
     * @param sql String repr�sentant la requ�te SQL � lancer
     * @param namedParameters Les param�tres de la requ�te (Pour un param�tre :suivant, l'entr�e dans la map
     *            correspondra � {suivant : value}
     * @param clazz Classe utilis� pour effectuer du logging
     * @return Nombre d'�l�ments retourn� par la requ�te SQL
     * @throws JadePersistenceException Exception lanc� si il y a des probl�mes avec la persistence.
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
     * Remplace la SCHEMA dans la requ�te SQL par le sch�ma en cours d'utilisation et assigne les param�tres � partir de
     * la map pass� en param�tres.
     * 
     * @param sql Requ�te SQL � transformer
     * @param params Map de param�tres nomm�s
     * @return Nouvelle requ�te SQL � ex�cuter
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
     * Ferme les resources utilis�es et log les informations si n�cessaire
     * 
     * @param sql Requ�te SQL utilis� pour le Log
     * @param stmt Statement � fermer
     * @param resultSet ResultSet � fermer
     * @param currentTime Temps pass� � l'ex�cution de la requ�te
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
     * Contr�le que la requ�te SQL est bien saisie, dans le cas ou elle est null ou vide, retourne une
     * {@link IllegalArgumentException}
     * 
     * @param sql String repr�sentant potentiellement une requ�te SQL
     * @throws IllegalArgumentException Retourne une IllegalArgumentException
     */
    private static void checkSqlQuery(String sql) {
        if (JadeStringUtil.isEmpty(sql)) {
            throw new IllegalArgumentException("La requ�te SQL est null ou une cha�ne vide");
        }
    }

    /**
     * Contr�le la pr�sence du context et la pr�sence d'une connection JDBC dans le thread
     * 
     * @param sql Requ�te SQL servant au log
     * @param clazz Class � logger
     * @throws JadePersistenceException Exception retourn�e
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
