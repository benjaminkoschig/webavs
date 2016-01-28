package ch.globaz.pegasus.businessimpl.utils;

import globaz.globall.context.BJadeThreadActivator;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.mapping.JadeModelMappingProvider;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.sql.JadeSqlConstantes;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public final class PersistenceUtil {

    private static final String SERIAL_VERSION_UID_TO_EXCLUDE = "serialVersionUID";

    public interface SearchLotExecutor<T> {
        public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                JadePersistenceException;
    }

    public static final String PERSISTENCE_NO_VALUE = "0";

    private static String addFilter(String sql, JadeAbstractSearchModel search) throws JadePersistenceException {
        Field[] fields = search.getClass().getDeclaredFields();

        for (Field field : fields) {
            // on ignore le champ serialversionuid si il est présent --> bonnes pratiques de l'inclure
            if (!SERIAL_VERSION_UID_TO_EXCLUDE.equalsIgnoreCase(field.getName())) {
                String methodName = "get" + JadeStringUtil.firstLetterToUpperCase(field.getName());
                Object value = null;
                try {
                    Method method = search.getClass().getMethod(methodName);
                    value = method.invoke(search);
                    sql = sql.replaceAll(":" + field.getName(), value.toString());
                } catch (NoSuchMethodException nsme) {
                    String msgError = "Error durring introspection: " + nsme.getMessage();
                    JadeLogger.info(PersistenceUtil.class, msgError);
                    throw new JadePersistenceException(msgError, nsme);
                } catch (Exception e) {
                    String msgError = "Error durring retrieving field for filtering : ";
                    throw new JadePersistenceException(msgError, e);
                }
            } else {
                JadeLogger.info(PersistenceUtil.class, "SerialVersionUID detetected and ignored");
            }
        }
        return sql;
    }

    /*
     * http://stackoverflow.com/questions/9578211/replacing-underscore-with-upper-case-letter-in-java-string
     */
    public static String camelcasify(String in) {
        StringBuilder sb = new StringBuilder();
        boolean capitalizeNext = false;
        in = in.toLowerCase();
        for (char c : in.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    sb.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Used to read date formatted as month.year field content
     * 
     * @param rs
     *            resultSet that contains the result of the query
     * @param fieldName
     *            The field name that we want to extract the value from resultSet
     * @return The string that match the date field's value in "MM.yyyy" format
     * @throws JadePersistenceException
     *             Thrown if problem during value extraction, either because the resultset or the fieldname are not
     *             defined, or because the object type is not supported
     */
    public static String dbReadDateAM(Object value, String fieldName) throws JadePersistenceException {
        try {
            if ((value == null) || JadeNumericUtil.isEmptyOrZero(value.toString())) {
                return "";
            }
            if (value instanceof BigDecimal) {
                Date date = PersistenceUtil.newFormatter("yyyyMM").parse(((BigDecimal) value).toString());
                return PersistenceUtil.newFormatter("MM.yyyy").format(date);
            }
            if (value instanceof String) {
                Date date = PersistenceUtil.newFormatter("yyyyMM").parse((String) value);
                return PersistenceUtil.newFormatter("MM.yyyy").format(date);
            }
        } catch (ParseException e) {
            throw new JadePersistenceException("Problem to parse date as (mm.yyyy) from field'" + fieldName + "'", e);
        }
        throw new JadePersistenceException("The field's type from field '" + fieldName
                + "' is currently not supported (value=" + value + ")");
    }

    /**
     * Used to read date field content
     * 
     * @param rs
     *            resultSet that contains the result of the query
     * @param fieldName
     *            The field name that we want to extract the value from resultSet
     * @return The string that match the date field's value in "dd.MM.yyyy" format
     * @throws JadePersistenceException
     *             Thrown if problem during value extraction, either because the resultset or the fieldname are not
     *             defined, or because the object type is not supported
     */
    public static String dbReadDateAMJ(Object value, String fieldName) throws JadePersistenceException {
        try {
            if ((value == null) || JadeNumericUtil.isEmptyOrZero(value.toString())) {
                return "";
            }
            if (value instanceof BigDecimal) {
                Date date = PersistenceUtil.newFormatter("yyyyMMdd").parse(((BigDecimal) value).toString());
                return PersistenceUtil.newFormatter("dd.MM.yyyy").format(date);
            }
            if (value instanceof String) {
                Date date = PersistenceUtil.newFormatter("yyyyMMdd").parse((String) value);
                return PersistenceUtil.newFormatter("dd.MM.yyyy").format(date);
            }
        } catch (ParseException e) {
            throw new JadePersistenceException("Problem to parse date from field'" + fieldName + "' (value=" + value
                    + ")", e);
        }
        throw new JadePersistenceException("The field's type from field '" + fieldName
                + "' is currently not supported (value=" + value + ")");
    }

    public static BManager executeOldFind(final BManager manager) throws Exception {

        OldPersistence<BManager> per = new OldPersistence<BManager>() {
            @Override
            public BManager action() throws Exception {
                manager.find();
                return manager;
            }
        };

        return per.execute();
    }

    public static ArrayList<HashMap<String, Object>> executeQuery(String sql, Class<?> class1)
            throws JadePersistenceException {
        if (JadeStringUtil.isEmpty(sql)) {
            return null;
        }
        if (JadeThread.currentContext() == null) {
            throw new JadePersistenceException(class1.getName() + " - " + "Unable to execute query (" + sql
                    + ") without a thread context!");
        } else if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)) {
            throw new JadePersistenceException(class1.getName() + " - " + "Unable to execute query (" + sql
                    + ") without an opened connection in the current thread context!");
        }
        long currentTime = System.currentTimeMillis();
        Statement stmt = null;
        ResultSet resultSet = null;
        ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();

        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
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
            throw new JadePersistenceException(class1.getName() + " - " + "Unable to execute query (" + sql
                    + "), a SQLException happend!", e);
        } finally {
            if (JadeModelMappingProvider.getInstance().isVerbose()) {
                JadeLogger.info(JadePersistenceManager.class, "Exec. time ("
                        + (System.currentTimeMillis() - currentTime) + "ms) - Perform search (" + class1.getName()
                        + ") - Query : " + sql);
            }
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
        }
        return results;
    }

    public static <T> List<T> executeQuery(String sql, JadeAbstractSearchModel search, Class<T> class1)
            throws JadePersistenceException {
        sql = PersistenceUtil.addFilter(sql, search);

        ArrayList<HashMap<String, Object>> listSql = PersistenceUtil.executeQuery(
                sql.replaceAll("\n", "").replaceAll("\t", " "), class1);

        Field[] fields = class1.getDeclaredFields();
        List<T> list = new ArrayList<T>();
        for (HashMap<String, Object> row : listSql) {
            T newObjet = null;
            try {
                newObjet = class1.newInstance();
            } catch (Exception e) {
                throw new JadePersistenceException("Unable to create an instance for this class " + class1.getName(), e);
            }
            for (Field field : fields) {
                String nameBd = PersistenceUtil.undersoreify(field.getName()).toUpperCase();
                Object value = row.get(nameBd.toUpperCase());

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
                                    value = PersistenceUtil.dbReadDateAMJ(value, nameBd);
                                } else {
                                    value = PersistenceUtil.dbReadDateAM(value, nameBd);
                                }
                                method.invoke(newObjet, value);
                            } catch (Exception e) {
                                throw new JadePersistenceException("Error durring introspection", e);
                            }
                        } else {
                            try {
                                Method method = class1.getMethod(methodName, String.class);
                                method.invoke(newObjet, String.valueOf(value));
                            } catch (Exception e) {
                                throw new JadePersistenceException("Error durring introspection", e);
                            }
                        }
                    } else if (Boolean.class.isAssignableFrom(field.getType())) {
                        try {
                            Method method = class1.getMethod(methodName, Boolean.class);
                            Boolean val = false;
                            if (value != null) {
                                val = JadeSqlConstantes.DB_BOOLEAN_TRUE.equals(String.valueOf(value));
                            }
                            method.invoke(newObjet, val);
                        } catch (Exception e) {
                            throw new JadePersistenceException("Error durring introspection", e);
                        }
                    }
                }
            }
            list.add(newObjet);
        }
        return list;
    }

    public static Integer executeUpdate(String sql, Class<?> class1) throws JadePersistenceException {
        if (JadeStringUtil.isEmpty(sql)) {
            return null;
        }
        if (JadeThread.currentContext() == null) {
            throw new JadePersistenceException(class1.getName() + " - " + "Unable to execute query (" + sql
                    + ") without a thread context!");
        } else if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)) {
            throw new JadePersistenceException(class1.getName() + " - " + "Unable to execute query (" + sql
                    + ") without an opened connection in the current thread context!");
        }
        long currentTime = System.currentTimeMillis();
        Statement stmt = null;

        Integer nb = 0;

        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
            nb = stmt.executeUpdate(sql);

        } catch (SQLException e) {
            throw new JadePersistenceException(class1.getName() + " - " + "Unable to execute query (" + sql
                    + "), a SQLException happend!", e);
        } finally {
            if (JadeModelMappingProvider.getInstance().isVerbose()) {
                JadeLogger.info(JadePersistenceManager.class, "Exec. time ("
                        + (System.currentTimeMillis() - currentTime) + "ms) - Perform Update (" + class1.getName()
                        + ") - Query : " + sql);
            }
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
        }
        return nb;
    }

    /**
     * Initialise un threadContexte pour pouvoir utiliser la nouvelle persistence depuis la vue globale
     * 
     * @param session
     * @return
     * @throws Exception
     */
    public static JadeThreadContext initContext(BSession session) throws Exception {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);
        return context;
    }

    /**
     * Used to instanciate the simple date formatter
     * 
     * @param pattern
     *            The date pattern to match
     * @return The simple date formatter
     */
    private static SimpleDateFormat newFormatter(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setLenient(false);
        return formatter;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> search(JadeAbstractSearchModel search, Class<T> t) throws JadePersistenceException {
        JadePersistenceManager.search(search);
        List<T> list = new ArrayList<T>(search.getSize());
        for (JadeAbstractModel modelAbstractModel : search.getSearchResults()) {
            list.add((T) modelAbstractModel);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> search(JadeAbstractSearchModel search) throws JadePersistenceException {
        JadePersistenceManager.search(search);
        List<T> list = new ArrayList<T>(search.getSize());
        for (JadeAbstractModel modelAbstractModel : search.getSearchResults()) {
            list.add((T) modelAbstractModel);
        }
        return list;
    }

    /**
     * Permet d'executer une recherche par lot, utiles quand on fait une recherche avec un "in" qui a une grande
     * collection
     * 
     * @param ids
     * @param executor
     * @param limitSize
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public static <T> List<T> searchByLot(Collection<String> ids, PersistenceUtil.SearchLotExecutor<T> executor,
            int limitSize) throws JadePersistenceException, JadeApplicationException {
        List<String> idsTemp = new ArrayList<String>();
        List<T> listReturn = new ArrayList<T>();
        List<T> temp = new ArrayList<T>();
        int i = 1;
        for (String id : ids) {
            idsTemp.add(id);
            if ((((i % limitSize) == 0) || (i == ids.size())) && (idsTemp.size() > 0)) {
                temp = (List<T>) Arrays.asList(executor.execute(idsTemp).getSearchResults());
                listReturn.addAll(temp);
                idsTemp = new ArrayList<String>();
            }
            i++;
        }
        return listReturn;
    }

    public static Object startUsingContex(BSession aUserSession) throws Exception {
        Object token = null;
        if (aUserSession.getCurrentThreadTransaction() != null) {
            BJadeThreadActivator.startUsingContext(aUserSession.getCurrentThreadTransaction());
            token = aUserSession.getCurrentThreadTransaction();
        } else {
            token = new Object();
            JadeThreadActivator.startUsingJdbcContext(token, PersistenceUtil.initContext(aUserSession).getContext());
        }
        return token;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> typeSearch(JadeAbstractSearchModel search, Class<T> t) {
        List<T> list = new ArrayList<T>(search.getSize());
        for (JadeAbstractModel modelAbstractModel : search.getSearchResults()) {
            list.add((T) modelAbstractModel);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> typeSearch(JadeAbstractSearchModel search) {
        List<T> list = new ArrayList<T>(search.getSize());
        for (JadeAbstractModel modelAbstractModel : search.getSearchResults()) {
            list.add((T) modelAbstractModel);
        }
        return list;
    }

    public static String undersoreify(String in) {
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

}