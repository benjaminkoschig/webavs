package ch.globaz.common.sql;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.mapping.JadeModelMappingProvider;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * <p>
 * Permet d'executer une requête SQL et de charger un objet automatiquement.
 * </p>
 * <p>
 * Pour charger l'objet on se base sur le nom des champs définit dans l'objet.
 * </p>
 * <p>
 * Il faut que cette objet soit un bean, car on se base sur les champs de class pour la concordance l'alias SQL, pour
 * ensuite passer par les setters pour peupler l'objet.
 * </p>
 * <strong>ATTENTION</strong> si aucune concordance n'est trouvé depuis un champ du bean, par défaut ceci ne lève pas
 * une exception mais
 * génère un log de type warn</p>
 * <p>
 * Pour réaliser le mapping entre le bean et la requête on se base sur le nom des champs de la requête il est conseillé
 * d'utiliser des alias dans ces champs. Pour réaliser l'introspection des champ n'pas sensible à la casse.
 * <p>
 * Mais si le setter et du style monChampDate l'alias de la db doit être nommé de la manière suivante: mon_champ_date ou
 * MON_CHAMP_DATE
 * </p>
 * 
 * <p>
 * Pour l'instant les primitives ne sont pas géré !!
 * </p>
 * 
 * @author dma
 * 
 */
public class QueryExecutor {

    /**
     * Execute une requête SQL et charge une liste d'objet et convertit des valeur en fonction.
     * Cette fonction est à utilisé avec la nouvelle persistence on se base sur le threadContext pour avoir la
     * connection.
     * 
     * Si aucun thread context n'existe une exception est lancé
     * 
     * @param query La requête qui sera exécuté.
     * @param clazz La represent la class des objet qui seront charger en mémoire. L'introspection pour créer les objets
     *            sont fait sur cette class.
     * @param converters Liste des converter à utiliser pour charger l'objet.
     * @return La liste des objets chargé en mémoire.
     */
    public static <T> List<T> execute(String query, Class<T> clazz) {
        checkThreadContext(query, clazz);
        return executeQueryList(query, clazz, JadeThread.currentJdbcConnection(), JadePersistenceUtil.getDbSchema(),
                null);
    }

    /**
     * Execute une requête SQL et charge une liste d'objet et convertit des valeur en fonction des converters.
     * Cette fonction est à utilisé avec la nouvelle persistence on se base sur le threadContext pour avoir la
     * connection.
     * 
     * @param query La requête qui sera exécuté.
     * @param clazz La represent la class des objet qui seront charger en mémoire. L'introspection pour créer les objets
     *            sont fait sur cette class.
     * @param converters Liste des converter à utiliser pour charger l'objet.
     * @return La liste des objets chargé en mémoire.
     */
    public static <T> List<T> execute(String query, Class<T> clazz, Set<ConverterDb<?>> converters) {
        checkThreadContext(query, clazz);
        return executeQueryList(query, clazz, JadeThread.currentJdbcConnection(), JadePersistenceUtil.getDbSchema(),
                converters);
    }

    /**
     * Execute une requête SQL et charge une liste d'objet et convertit des valeur en fonction des converters.
     * Cette fonction est à utilisé avec la nouvelle persistence on se base sur le threadContext pour avoir la
     * connection.
     * 
     * @param query La requête qui sera exécuté.
     * @param clazz La represent la class des objet qui seront charger en mémoire. L'introspection pour créer les objets
     *            sont fait sur cette class.
     * @param converters Liste des converter à utiliser pour charger l'objet.
     * @return La liste des objets chargé en mémoire.
     */
    public static <T> List<T> execute(String query, Class<T> clazz, ConverterDb<?>... converters) {
        checkThreadContext(query, clazz);
        return executeQueryList(query, clazz, JadeThread.currentJdbcConnection(), JadePersistenceUtil.getDbSchema(),
                newSetConverter(converters));
    }

    /**
     * Execute une requête SQL et charge une liste d'objet et convertit des valeur en fonction.
     * Cette fonction est à utilisé avec l'ancienne persistence pour avoir la connection.
     * <p>
     * Si aucun transaction n'est présente dans la session en créer une nouvelle pour la query et on la ferme.
     * </p>
     * 
     * @param query La requête qui sera exécuté.
     * @param clazz La represent la class des objet qui seront charger en mémoire. L'introspection pour créer les objets
     *            sont fait sur cette class.
     * @param session La session à utiliser pour trouver la transaction
     * @return La liste des objets chargé en mémoire.
     */
    public static <T> List<T> execute(String query, Class<T> clazz, BSession session) {
        return execute(query, clazz, session, null);
    }

    /**
     * Execute une requête SQL et charge une liste d'objet et convertit des valeur en fonction des converters.
     * Cette fonction est à utilisé avec l'ancienne persistence pour avoir la connection.
     * <p>
     * Si aucun transaction n'est présente dans la session en créer une nouvelle pour la query et on la ferme.
     * </p>
     * 
     * @param query La requête qui sera exécuté.
     * @param clazz La represent la class des objet qui seront charger en mémoire. L'introspection pour créer les objets
     *            sont fait sur cette class.
     * @param converters Liste des converter à utiliser pour charger l'objet.
     * @param session La session à utiliser pour trouver la transaction
     * 
     * @return La liste des objets chargé en mémoire.
     */
    public static <T> List<T> execute(String query, Class<T> clazz, BSession session, Set<ConverterDb<?>> converters) {
        Transaction transaction = new Transaction(session);
        try {
            return executeQueryList(query, clazz, transaction.getTransaction().getConnection(),
                    JadePersistenceUtil.getDbSchema(), converters);
        } finally {
            // dans tous les cas, si on a ouvert une nouvelle transaction, on la clôture.
            if (transaction != null) {
                transaction.closeOpenedTransaction();
            }
        }
    }

    /**
     * Execute une requête SQL et charge une liste d'objet et convertit des valeur en fonction.
     * Cette fonction est à utilisé avec l'ancienne persistence pour avoir la connection.
     * 
     * @param query La requête qui sera exécuté.
     * @param clazz La represent la class des objet qui seront charger en mémoire. L'introspection pour créer les objets
     *            sont fait sur cette class.
     * @param transaction La transaction à utiliser pour réaliser la requête.
     * @return La liste des objets chargé en mémoire.
     */
    public static <T> List<T> execute(String query, Class<T> clazz, BITransaction transaction) {
        return executeQueryList(query, clazz, ((BTransaction) transaction).getConnection(),
                JadePersistenceUtil.getDbSchema(), null);
    }

    /**
     * Execute une requête SQL et charge une liste d'objet et convertit des valeur en fonction.
     * Cette fonction est à utilisé avec l'ancienne persistence pour avoir la connection.
     * 
     * @param query La requête qui sera exécuté.
     * @param clazz La represent la class des objet qui seront charger en mémoire. L'introspection pour créer les objets
     *            sont fait sur cette class.
     * @param converters Liste des converter à utiliser pour charger l'objet.
     * @return La liste des objets chargé en mémoire.
     */
    public static <T> List<T> execute(String query, Class<T> clazz, BITransaction transaction,
            Set<ConverterDb<?>> converters) {
        return executeQueryList(query, clazz, ((BTransaction) transaction).getConnection(),
                JadePersistenceUtil.getDbSchema(), converters);
    }

    /**
     * Permet d'executer une requête qui à un fonction d'aggregate(count, min, max, sum) dont on veut un seul résultat
     * et champs.
     * Cette fonction est à utilisé avec la nouvelle persistence on se base sur le threadContext pour avoir la
     * connection.
     * 
     * @param query La requête qui sera exécuté.
     * @return Le résultat de la requête
     */
    public static BigDecimal executeAggregate(String query) {
        checkThreadContext(query, QueryExecutor.class);
        return executeAggregate(query, BSessionUtil.getSessionFromThreadContext());
    }

    /**
     * Permet d'executer une requête qui à un fonction d'aggregate(count, min, max, sum) dont on veut un seul résultat
     * et champs.
     * Cette fonction est à utilisé avec l'ancienne persistence pour avoir la connection.
     * 
     * @param query La requête qui sera exécuté.
     * @return Le résultat de la requête
     */
    public static BigDecimal executeAggregate(String query, BSession session) {
        Transaction transaction = new Transaction(session);
        try {
            return executeAggregate(query, transaction.getTransaction().getConnection());
        } finally {
            // dans tous les cas, si on a ouvert une nouvelle transaction, on la clôture.
            if (transaction != null) {
                transaction.closeOpenedTransaction();
            }
        }
    }

    /**
     * Permet d'executer une requête qui à un fonction d'aggregate(count, min, max, sum) dont on veut un seul résultat
     * et champs.
     * Cette fonction est à utilisé avec l'ancienne persistence pour avoir la connection.
     * 
     * @param sql La requête qui sera exécuté.
     * @param transaction La transaction à utiliser pour réaliser la query
     * @return Le résultat de la requête
     */
    public static BigDecimal executeAggregate(String sql, BITransaction transaction) {
        return executeAggregate(sql, ((BTransaction) transaction).getConnection());
    }

    /**
     * Permet de créer un set depuis une ellipse.
     * 
     * @param converters
     * @return La liste des converters
     */
    public static Set<ConverterDb<?>> newSetConverter(ConverterDb<?>... converters) {
        Set<ConverterDb<?>> set = new HashSet<ConverterDb<?>>();
        set.addAll(Arrays.asList(converters));
        return set;
    }

    /**
     * @param list
     * @return
     */
    public static String forInString(Collection<String> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("'").append(Joiner.on("','").join(list)).append("'");
        return builder.toString();
    }

    /**
     * This split a list by 1000 elements.
     * 
     * @param list The list to split
     * @return the list splited
     */
    public static <T> List<List<T>> splitBy1000(Collection<T> list) {
        return Lists.partition(new ArrayList<T>(list), 1000);
    }

    /**
     * This split a list by the size defined.
     * 
     * @param list The list to split
     * @param size The number of elements in the list
     * @return the list splited
     */
    public static <T> List<List<T>> split(Collection<T> list, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("The size is less than or equal to zero, size passed: " + size);
        }
        return Lists.partition(new ArrayList<T>(list), size);
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

    static BigDecimal executeAggregate(String query, String schema, Connection connextion) {
        Class<?> clazz = QueryExecutor.class;
        query = createQuery(query, clazz, schema);
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
            if (!map.values().isEmpty()) {
                Object value = map.values().iterator().next();
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
        }
        return nb;
    }

    static BigDecimal executeAggregate(String query, Connection connextion) {
        return executeAggregate(query, JadePersistenceUtil.getDbSchema(), connextion);
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

    static <T> List<T> executeQueryList(String query, Class<T> clazz, Connection connection, String schema,
            Set<ConverterDb<?>> converters) {
        query = createQuery(query, clazz, schema);
        long currentTime = System.currentTimeMillis();
        ArrayList<HashMap<String, Object>> listSql = executeQuery(query, clazz, connection);
        long timeQuery = System.currentTimeMillis() - currentTime;
        currentTime = System.currentTimeMillis();

        List<T> list = createList(clazz, listSql, converters);

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
     * @return the result from the query
     */
    @SuppressWarnings("unchecked")
    static <T> List<T> createList(Class<T> clazz, ArrayList<HashMap<String, Object>> listSql,
            Set<ConverterDb<?>> converters) {
        List<T> list = new ArrayList<T>();

        Map<Class<?>, ConverterDb<?>> mapConverters = new HashMap<Class<?>, ConverterDb<?>>();
        if (converters != null) {
            for (ConverterDb<?> converterDb : converters) {
                if (mapConverters.containsKey(converterDb.forType())) {
                    throw new CommonTechnicalException("Unabaled to use the converters for this class "
                            + clazz.getName() + ". Because more the one converter is found for this type:"
                            + converterDb.forType());
                }
                mapConverters.put(converterDb.forType(), converterDb);
            }
        }

        Map<Class<?>, ConverterDb<?>> internalConverters = new HashMap<Class<?>, ConverterDb<?>>();
        internalConverters.put(String.class, new StringConverter());
        internalConverters.put(Boolean.class, new BooleanConverter());
        internalConverters.put(Integer.class, new IntegerConverter());

        List<Field> fields = resolveFields(clazz);

        for (HashMap<String, Object> row : listSql) {
            if (internalConverters.containsKey(clazz)) {
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    list.add((T) internalConverters.get(clazz).convert(entry.getValue(), null, null));
                }
            } else {
                T newObjet = newBean(clazz);
                for (Field field : fields) {
                    String alias = underscoreify(field.getName()).toUpperCase();
                    Object value = row.get(alias);

                    if (value == null) {
                        value = row.get(field.getName().toUpperCase());
                    }

                    if (value == null) {
                        JadeLogger.warn(QueryExecutor.class,
                                "Unable to map the value in the query result for this field " + field.getName()
                                        + " with this class " + clazz.getName());
                    } else {
                        Method method = resolveMethod(clazz, field);
                        ConverterDb<?> converter = resovlerConverterToUse(mapConverters, internalConverters, field);
                        Object valueForInvocation = null;
                        if (converter != null) {
                            valueForInvocation = converter.convert(value, field.getName(), alias);
                        } else {
                            try {
                                valueForInvocation = value;
                                if (!field.getType().equals(value.getClass())) {
                                    valueForInvocation = field.getType().getConstructor(value.getClass())
                                            .newInstance(value);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                JadeLogger.warn(QueryExecutor.class,
                                        "Error durring introspection for " + field.getType() + " : " + e.toString());
                            }
                        }
                        try {
                            method.invoke(newObjet, valueForInvocation);
                        } catch (Exception e) {
                            throw new CommonTechnicalException("Error durring introspection, value:" + value
                                    + ", field.getName():" + field.getName() + ", alias:" + alias + ", converterDb:"
                                    + converter, e);
                        }
                    }
                }
                list.add(newObjet);
            }
        }
        return list;
    }

    private static <T> T newBean(Class<T> clazz) {
        T newObjet = null;
        try {
            newObjet = clazz.newInstance();
        } catch (Exception e) {
            throw new CommonTechnicalException("Unable to create an instance for this class " + clazz.getName(), e);
        }
        return newObjet;
    }

    static List<Field> resolveFields(Class<?> clazz) {
        Field[] fieldsFound = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList<Field>();
        for (Field field : fieldsFound) {
            if (!Modifier.isStatic(field.getModifiers()) && hasSetter(clazz, field)) {
                fields.add(field);
            }
        }
        return fields;
    }

    static boolean hasSetter(Class<?> clazz, Field field) {
        try {
            resolveMethod(clazz, field);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static <T> Method resolveMethod(Class<T> clazz, Field field) {
        String methodName = "set" + JadeStringUtil.firstLetterToUpperCase(field.getName());
        try {
            return clazz.getMethod(methodName, field.getType());
        } catch (SecurityException e1) {
            throw new CommonTechnicalException("Security error durring introspection for the method:" + methodName
                    + ", for the class " + clazz.getName(), e1);
        } catch (NoSuchMethodException e1) {
            throw new CommonTechnicalException("The method:" + methodName + " for the class " + clazz.getName()
                    + "is not found", e1);
        }
    }

    private static ConverterDb<?> resovlerConverterToUse(Map<Class<?>, ConverterDb<?>> mapConverters,
            Map<Class<?>, ConverterDb<?>> internalConverters, Field field) {
        ConverterDb<?> converter = null;
        if (!mapConverters.isEmpty() && mapConverters.containsKey(field.getType())) {
            converter = mapConverters.get(field.getType());
        } else if (internalConverters.containsKey(field.getType())) {
            converter = internalConverters.get(field.getType());
        }
        return converter;
    }

}