package ch.globaz.common.sql;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.jadedb.TableDefinition;

/**
 * Le but de cette class est de permettre la création de requête ou une partie de requête.
 * Le comportement est le suivant lors que l'on donne un paramétrer et que celui-ci est vide on ajoute pas le fragment
 * SQL à l requête.
 * 
 * @author dma
 * 
 */
public class SQLWriter {

    private final StringBuilder query = new StringBuilder();
    private final String schema;
    private int countAddOperators = 0;
    private String charToReplace = "?";
    private List<String> paramsToUse = new ArrayList<String>();
    private Integer currentIndice;
    private final boolean addSchema;
    private static final String CONST_SCHEMA = "schema.";
    private boolean addComma = false;

    private SQLWriter(String schema) {
        this.schema = schema;
        addSchema = false;
    }

    private SQLWriter(String schema, boolean addSchema) {
        this.schema = schema;
        this.addSchema = addSchema;
    }

    /**
     * Créer l'instance.
     * 
     * @return SQLWriter la nouvelle instance créer
     */
    public static SQLWriter write() {
        return new SQLWriter(null);
    }

    /**
     * Créer l'instance et définit que lorsque l'on ajoute pour chaque table(sqlFragment) le mot schema devant.
     * 
     * @return SQLWriter la nouvelle instance créer
     */
    public static SQLWriter writeWithSchema() {
        return new SQLWriter(null, true);
    }

    /**
     * Créer l'instance en définissant le schema qui sera utilisé pour le SQL.
     * Le "." dans le schéma doit être fourni
     * 
     * @return SQLWriter la nouvelle instance créer
     */
    public static SQLWriter write(String schema) {
        return new SQLWriter(schema);
    }

    /**
     * Ajout le mot select à la requête.
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter select() {
        query.append("select ");
        return this;
    }

    /**
     * Ajout les fields à la requête.
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter fields(String... fields) {
        StringWriter writer = new StringWriter();
        for (String field : fields) {
            if (addComma) {
                writer.append(", ");
            } else {
                addComma = true;
            }
            writer.append(field);
        }
        query.append(" ").append(writer.toString().toUpperCase());
        return this;
    }

    /**
     * Ajout les fields à la requête.
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter fields(String aliasTable, Class<? extends TableDefinition> clazz) {
        TableDefinition[] values = clazz.getEnumConstants();
        StringWriter writer = new StringWriter();
        for (TableDefinition def : values) {
            if (addComma) {
                writer.append(", ");
            } else {
                addComma = true;
            }
            writer.append(aliasTable);
            writer.append(".");
            writer.append(def.getColumnName());
            writer.append(" as ");
            writer.append(aliasTable);
            writer.append("_");
            writer.append(def.getColumnName());
        }
        query.append(" ").append(writer.toString().toUpperCase());
        return this;
    }

    /**
     * Ajoute le mot select à la requête et ajoute le fragment SQL passé en paramètres à la requête.
     * 
     * @param sqlFragement SQl à ajouté à la requête. Le fragment devrait contenir que les fields.
     * @return SQLWriter utilisé
     */

    public SQLWriter select(String sqlFragement) {
        query.append("select ").append(sqlFragement);
        return this;
    }

    /**
     * Ajout le mot max à la requête et ajoute la colonne dans le max.
     * 
     * @param column sur laquelle utilisé le mot clé max
     * @return SQLWriter utilisé
     */
    public SQLWriter max(String column) {
        query.append("max(" + addSchemaToSql(column) + ") ");
        return this;
    }

    /**
     * Ajout le mot max à la requête et ajoute la colonne dans le max.
     * 
     * @param column(TableDefinition) sur laquelle utilisé le mot clé max
     * @return SQLWriter utilisé
     */
    public SQLWriter max(TableDefinition tableDefinition) {
        query.append("max(" + CONST_SCHEMA + tableDefinition.getTableName() + "." + tableDefinition.getColumnName()
                + ") ");
        return this;
    }

    /**
     * Ajoute le mot from à la requête et ajoute le fragment SQL passé en paramètres à la requête.
     * 
     * @param sqlFragement SQl à ajouté à la requête. Le fragment devrait contenir que la table pour la clause from.
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter from(String sqlFragement) {
        query.append(" from ").append(addSchemaToSql(sqlFragement));
        return this;
    }

    /**
     * Ajoute le mot where à la requête.
     * 
     * @return SQLWriter utilisé.
     */
    public SQLWriter where() {
        query.append(" where");
        return this;
    }

    /**
     * Ajoute le mot where à la requête et ajoute le fragment SQL passé en paramètres à la requête.
     * 
     * @param sqlFragement SQl à ajouté à la requête.
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter where(String sqlFragement) {
        this.where().query.append(" ").append(sqlFragement);
        countAddOperators++;
        return this;
    }

    /**
     * Ajoute le mot 'where' à la requête et ajoute le fragment SQL passé en paramètre à la requête. Si params n'est pas
     * vide le fragment SQL est ajoute à la requête.
     * 
     * @param sqlFragement SQl à ajouté à la requête. les caractères [?] sont remplacés par ordre des params donnée en
     *            paramètre(Ex: nom = '?' and age = '?', => nom = 'Test' and age=12).
     * @param params Paramétrées à utilisé dans le fragment SQL
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter where(String sqlFragement, String... params) {
        this.where();
        this.and(sqlFragement, params);
        countAddOperators++;
        return this;
    }

    /**
     * Ajoute le mot 'where' à la requête et ajoute le fragment SQL passé en paramètre à la requête. Si le param donné
     * n'est pas vide le fragment SQL est ajoute à la requête.
     * 
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = '?' , => nom = 'Test').
     * @param params Paramétrées à utilisé dans le fragment SQL.
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter where(String sqlFragement, Collection<String> params) {
        this.where();
        this.and(sqlFragement, params);
        countAddOperators++;
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête si besoin.
     * 
     * @param fragmentSql
     * @return SQLWriter utilisé
     */
    public SQLWriter and() {
        addOpertor("and");
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête si besoin et le sqlFramgment.
     * 
     * @param sqlFramgment
     * @return SQLWriter utilisé
     */
    public SQLWriter and(boolean condition, String sqlFramgment) {
        if (condition) {
            and(sqlFramgment);
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête si besoin et le sqlFramgment.
     * 
     * @param sqlFramgment
     * @return SQLWriter utilisé
     */
    public SQLWriter and(String sqlFramgment) {
        this.and();
        query.append(" ").append(addSchemaToSql(sqlFramgment));
        return this;
    }

    /**
     * Ajoute le = avec le paramètre définit à la requête SQL
     * 
     * @param param String sql
     * @return SQLWriter utilisé
     */
    public SQLWriter equal(String param) {
        if (param != null && !param.isEmpty()) {
            String sanitized = replaceQuotes(param);
            paramsToUse.add(sanitized);
            query.append("='?'");
        } else {
            rollback();
        }
        return this;
    }

    /**
     * Ajoute le = avec le paramètre définit à la requête SQL
     * 
     * @param param Le code system
     * @return SQLWriter utilisé
     */
    public SQLWriter equal(CodeSystemEnum<?> param) {
        if (param != null) {
            paramsToUse.add(param.getValue());
            query.append("=?");
        } else {
            rollback();
        }
        return this;
    }

    /**
     * Ajoute le = avec le paramètre définit à la requête SQL
     * et transforme la chaine de caractère en date globaz.
     * EX: 10.2015 = > 201510, 01.10.2015 =>20151001
     * 
     * @param param String sql
     * @return SQLWriter utilisé
     */
    public SQLWriter equalForDate(String param) {
        Integer date = null;
        if (param != null && !param.isEmpty()) {
            date = datToGlobazFormat(param);
        }
        return equal(date);
    }

    /**
     * Ajoute le = avec le paramètre définit à la requête SQL
     * 
     * @param param Integer sql
     * @return SQLWriter utilisé
     */
    public SQLWriter equal(Number param) {
        if (param != null) {
            paramsToUse.add(String.valueOf(param));
            query.append("=?");
        } else {
            rollback();
        }
        return this;
    }

    /**
     * Ajoute le "in(values)" avec le paramètre définit à la requête SQL
     * Attention : Les guillemets et les doubles guillemets ne sont pas remplacés par les caractères suivants : '¬' et
     * '¢'
     * 
     * @param param String sql
     * @return SQLWriter utilisé
     */
    public SQLWriter in(String values) {
        if (values != null && !values.isEmpty()) {
            paramsToUse.add(values);
            query.append(" in (?)");
        } else {
            rollback();
        }
        return this;
    }

    /**
     * Ajoute le "in(x,y,z)" avec le paramètre donnée ajoute les ","
     * Attention : Les guillemets et les doubles guillemets ne sont pas remplacés par les caractères suivants : '¬' et
     * '¢'
     * 
     * @param param String sql
     * @return SQLWriter utilisé
     */
    public SQLWriter in(Collection<?> list) {
        if (list != null && !list.isEmpty()) {
            String params = toStrForIn(list);
            paramsToUse.add(params);
            query.append(" in (?)");
        } else {
            rollback();
        }
        return this;
    }

    /**
     * Ajoute le "in('x','y','z')" avec le paramètre donnée ajoute les "','"
     * Attention : Les guillemets et les doubles guillemets ne sont pas remplacés par les caractères suivants : '¬' et
     * '¢'
     * 
     * @param param String sql
     * @return SQLWriter utilisé
     */
    public SQLWriter inForString(Collection<String> list) {
        if (list != null && !list.isEmpty()) {
            String params = toStringForIn(list);
            paramsToUse.add(params);
            query.append(" in (?)");
        } else {
            rollback();
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête si besoin
     * 
     * @param sqlFramgment
     * @return SQLWriter utilisé
     */
    public SQLWriter and(TableDefinition tableDefinition) {
        String column = tableDefinition.getTableName() + "." + tableDefinition.getColumnName();

        this.and();
        query.append(" ").append(addSchemaToSql(CONST_SCHEMA + column));

        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête si besoin
     * 
     * @param sqlFramgment
     * @return SQLWriter utilisé
     */
    public SQLWriter and(String alias, TableDefinition tableDefinition) {
        String column = alias + "." + tableDefinition.getColumnName();

        this.and();
        query.append(" ").append(column);

        return this;
    }

    public SQLWriter isNullOrZero(TableDefinition tableDefinition) {
        String column = tableDefinition.getTableName() + "." + tableDefinition.getColumnName();
        return isNullOrZero(column);
    }

    public SQLWriter isNullOrZero(String column) {
        query.append(" (").append(addSchemaToSql(column)).append(" IS NULL").append(" OR ")
                .append(addSchemaToSql(column)).append("=0)");
        return this;
    }

    public SQLWriter isNotNullOrZero(TableDefinition tableDefinition) {
        String column = tableDefinition.getTableName() + "." + tableDefinition.getColumnName();
        return isNotNullOrZero(column);
    }

    public SQLWriter isNotNullOrZero(String column) {
        query.append(" (").append(addSchemaToSql(column)).append(" IS NOT NULL").append(" AND ")
                .append(addSchemaToSql(column)).append("<>0)");
        return this;
    }

    /**
     * Ajoute le mot 'like %param%' à la requête si besoin
     * 
     * @param sqlFramgment
     * @return SQLWriter utilisé
     */
    public SQLWriter fullLike(String param) {
        if (param != null && !param.isEmpty()) {
            String sanitized = replaceQuotes(param);
            paramsToUse.add(sanitized);
            query.append(" like '%?%'");
        } else {
            rollback();
        }
        return this;
    }

    /**
     * Ajoute le mot 'like param' à la requête si besoin
     * 
     * @param sqlFramgment
     * @return SQLWriter utilisé
     */
    public SQLWriter like(String param) {
        if (param != null && !param.isEmpty()) {
            String sanitized = replaceQuotes(param);
            paramsToUse.add(sanitized);
            query.append(" like '?'");
        } else {
            rollback();
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête(si besoin) et le fragment SQL si les paramètres ne sont pas vide(null).
     * 
     * @param condition Condition pour ajouter le fragment.
     * @param params Paramétrées à utilisé dans le fragment SQL
     * @param sqlFragement SQl à ajouté à la requête. Les caractères [?] sont remplacés par les params donnés en
     *            paramètre(Ex: nom = '?' , => nom = 'Test').
     * @return SQLWriter utilisé
     */
    public SQLWriter and(boolean condition, String sqlFragement, Integer... params) {
        if (condition) {
            and(sqlFragement, params);
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête(si besoin) et le fragment SQL si les paramètres ne sont pas vide(null).
     * 
     * @param params Paramètres à utilisé dans le fragment SQL
     * @param sqlFragement SQl à ajouté à la requête. Les caractères [?] sont remplacés par les params donnés en
     *            paramètre(Ex: nom = '?' , => nom = 'Test').
     * @return SQLWriter utilisé
     */
    public SQLWriter and(String sqlFragement, Integer... params) {
        if (isNotEmpty(params)) {
            for (Integer p : params) {
                paramsToUse.add(String.valueOf(p));
            }
            this.and(sqlFragement);
        }
        return this;
    }

    /**
     * TODO que faire si certaine paramétrer sont vide ???
     * 
     * Ajoute le mot 'and' à la requête(si besoin) et le fragment SQL si les paramètres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param condition Condition pour ajouter le fragment.
     * @param params Paramétrées à utilisé dans le fragment SQL
     * @param sqlFragement SQl à ajouté à la requête. Les caractères [?] sont remplacés par les params donnés en
     *            paramètre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilisé
     */
    public SQLWriter and(boolean condition, String sql, String... params) {
        if (condition) {
            and(sql, params);
        }
        return this;
    }

    /**
     * TODO que faire si certaine paramétrer sont vide ???
     * 
     * Ajoute le mot 'and' à la requête(si besoin) et le fragment SQL si les paramètres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL
     * @param sqlFragement SQl à ajouté à la requête. Les caractères [?] sont remplacés par les params donnés en
     *            paramètre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilisé
     */
    public SQLWriter and(String sql, String... params) {
        if (isNotEmpty(params)) {
            for (String p : params) {
                paramsToUse.add(p);
            }
            this.and(sql);
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête(si besoin) et le fragment SQL si les paramètres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL
     * @param sqlFragement SQl à ajouté à la requête. Les caractères [?] sont remplacés par les params donnés en
     *            paramètre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilisé
     */
    public SQLWriter and(String sql, Collection<String> params) {
        if (params != null && !params.isEmpty()) {
            paramsToUse.addAll(params);
            this.and(sql);
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' à la requête.
     * 
     * @param sqlFragement
     * @return SQLWriter utilisé
     */
    public SQLWriter or(String sqlFragment) {
        addOpertor("or");
        query.append(" ").append(addSchemaToSql(sqlFragment));
        return this;
    }

    /**
     * Ajoute le mot 'or' à la requête(si besoin) et le fragment SQL si les paramètres ne sont pas vide(null).
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL
     * @param sqlFragement SQl à ajouté à la requête. Les caractères [?] sont remplacés par les params donnés en
     *            paramètre(Ex: nom = '?' , => nom = 'Test').
     * @return SQLWriter utilisé
     */
    public SQLWriter or(String sqlFragement, Integer... params) {
        if (isNotEmpty(params)) {
            for (Integer p : params) {
                paramsToUse.add(String.valueOf(p));
            }
            this.or(sqlFragement);
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' à la requête(si besoin) et le fragment SQL si les paramètres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL
     * @param sqlFragement SQl à ajouté à la requête. Les caractères [?] sont remplacés par les params donnés en
     *            paramètre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilisé
     */
    public SQLWriter or(String sqlFragement, String... params) {
        if (isNotEmpty(params)) {
            for (String p : params) {
                paramsToUse.add(String.valueOf(p));
            }
            this.or(sqlFragement);
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' à la requête(si besoin) et le fragment SQL si les paramètres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL
     * @param sqlFragement SQl à ajouté à la requête. Les caractères [?] sont remplacés par les params donnés en
     *            paramètre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilisé
     */
    public SQLWriter or(String sqlFragement, Collection<String> params) {
        if (params != null && !params.isEmpty()) {
            paramsToUse.addAll(params);
            this.or(sqlFragement);
        }
        return this;
    }

    /**
     * Ajoute le fragment SQL à la requête et met un espace devant le fragment SQL.
     * 
     * @param sqlFragment Le fragment SQL à ajouter à la requête.
     * @return SQLWriter utilisé
     */
    public SQLWriter append(String sqlFragment) {
        if (!isQueryEmpty()) {
            query.append(" ");
        }
        query.append(sqlFragment);
        return this;
    }

    /**
     * Ajoute le fragment SQL à la requête si les params donnée en paramètres ne sont pas vide.
     * 
     * @param sqlFragment Le fragment SQL à ajouter.
     * @param params Les paramètres à utiliser pour la requête.
     * @return SQLWriter utilisé
     */
    public SQLWriter append(String sqlFragment, String... params) {
        if (isNotEmpty(params)) {
            for (String p : params) {
                paramsToUse.add(p);
            }
            query.append(sqlFragment);
        }
        return this;
    }

    /**
     * Ajoute le fragment SQL à la requête si les params donnée en paramètres ne sont pas vide.
     * 
     * @param sqlFragment Le fragment SQL à ajouter.
     * @param params Les paramètres à utiliser pour la requête.
     * @return SQLWriter utilisé
     */
    public SQLWriter append(String sqlFragment, Integer... params) {
        if (isNotEmpty(params)) {
            for (Integer p : params) {
                paramsToUse.add(String.valueOf(p));
            }
            query.append(sqlFragment);
        }
        return this;
    }

    /**
     * Ajoute le fragment SQL à la requête si la condition est vrais.
     * 
     * 
     * @param sqlFragment Le fragment SQL à ajouter à la requête.
     * @param condition Condition pour ajouter le fragment.
     * @return SQLWriter utilisé
     */
    public SQLWriter append(boolean condition, String sqlFragment) {
        if (condition) {
            this.append(sqlFragment);
        }
        return this;
    }

    /**
     * Ajoute le mot 'inner join' à la requête et ajoute le fragment SQL à la requête.
     * 
     * @param sqlFragment Le fragment SQL à ajouter à la requête.
     * @return SQLWriter utilisé
     */
    public SQLWriter join(String sql) {
        query.append(" inner join ").append(sql);
        return this;
    }

    /**
     * Ajoute le mot 'inner join' à la requête et ajoute le fragment SQL à la requête si la condition est vrais.
     * 
     * @param sqlFragment Le fragment SQL à ajouter à la requête.
     * @param condition Condition pour ajouter le fragment.
     * @return SQLWriter utilisé
     */
    public SQLWriter join(boolean condition, String sql) {
        if (condition) {
            query.append(" inner join ").append(sql);
        }
        return this;
    }

    /**
     * Ajoute le mot 'left join' à la requête et ajoute le fragment SQL à la requête.
     * 
     * @param sqlFragment Le fragment SQL à ajouter à la requête.
     * @return SQLWriter utilisé
     */
    public SQLWriter leftJoin(String sql) {
        query.append(" left join ").append(sql);
        return this;
    }

    /**
     * Ajoute le mot 'left join' à la requête et ajoute le fragment SQL à la requête si la condition est vrais.
     * 
     * @param sqlFragment Le fragment SQL à ajouter à la requête.
     * @param condition Condition pour ajouter le fragment.
     * @return SQLWriter utilisé
     */
    public SQLWriter leftJoin(boolean condition, String sql) {
        if (condition) {
            query.append(" left join ").append(sql);
        }
        return this;
    }

    /**
     * Ajoute le mot 'right join' à la requête et ajoute le fragment SQL à la requête.
     * 
     * @param sqlFragment Le fragment SQL à ajouter à la requête.
     * @return SQLWriter utilisé
     */
    public SQLWriter rightJoin(String sql) {
        query.append(" right join ").append(sql);
        return this;
    }

    /**
     * Ajoute le mot 'right join' à la requête et ajoute le fragment SQL à la requête si la condition est vrais.
     * 
     * @param sqlFragment Le fragment SQL à ajouter à la requête.
     * @param condition Condition pour ajouter le fragment.
     * @return SQLWriter utilisé
     */
    public SQLWriter rightJoin(boolean condition, String sql) {
        if (condition) {
            query.append(" right join ").append(sql);
        }
        return this;
    }

    /**
     * Serialize la requête SQL.
     * 
     * @return le SQL si un schéma a été définit celui-ci sera automatiquement remplacé.
     */
    public String toSql() {
        String sql = query.toString();
        if (hasSchema()) {
            sql = sql.replaceAll(CONST_SCHEMA, schema);
        }
        return this.replace(sql, paramsToUse);
    }

    private String replaceQuotes(String param) {
        return param.replace('\'', '¬').replace('"', '¢');
    }

    boolean isQueryEmpty() {
        return query.length() == 0;
    }

    boolean isNotEmpty(String... param) {
        if (param == null || param.length == 0) {
            return false;
        }

        if (!(param[0] == null || param[0].length() == 0)) {
            return true;
        }

        return false;
    }

    boolean isNotEmpty(Integer... param) {
        if (param.length == 1 && param[0] == null) {
            return false;
        }
        return true;
    }

    static String toStrForIn(Collection<?> params) {
        return StringUtils.join(params, ",");
    }

    static String toStringForIn(Integer... params) {
        return StringUtils.join(params, ",");
    }

    static String toStringForIn(String... params) {
        return "\'" + StringUtils.join(params, "','") + "\'";
    }

    static String toStringForIn(Collection<String> params) {
        return "\'" + StringUtils.join(params, "','") + "\'";
    }

    String replace(String sqlFragment, Collection<String> params) {
        checkMatchParams(sqlFragment, params.size());
        for (String p : params) {
            sqlFragment = replace(sqlFragment, p);
        }
        return sqlFragment;
    }

    void checkMatchParams(String sqlFragment, int nbParams) {
        int nbMatch = countCharToReplace(sqlFragment);
        if (nbMatch != nbParams) {
            throw new RuntimeException("Unabeld to replace the " + charToReplace + " with parmas. The number ("
                    + nbMatch + ") of the " + charToReplace + " not match with the number of parmas (" + nbParams + ")");
        }
    }

    int currentIndex() {
        currentIndice = query.length();
        return currentIndice;
    }

    SQLWriter rollback() {
        if (currentIndice != null) {
            query.delete(currentIndice, query.length());
            currentIndice = null;
            countAddOperators--;
        }
        return this;
    }

    int countCharToReplace(String sqlFragment) {
        return StringUtils.countMatches(sqlFragment, charToReplace);
    }

    Integer datToGlobazFormat(String param) {
        Date date = new Date(param);
        String value = date.getValue();
        if (param.length() == 6 || param.length() == 7) {
            value = date.getValueMonth();
        }
        return Integer.valueOf(value);
    }

    private void addOpertor(String operator) {
        currentIndex();
        if (countAddOperators > 0) {
            query.append(" ").append(operator);
        }
        countAddOperators++;
    }

    private String replace(String sql, String p) {
        return StringUtils.replaceOnce(sql, charToReplace, p);
    }

    private String addSchemaToSql(String sqlFragement) {
        String sql = sqlFragement;
        if (addSchema) {
            sql = CONST_SCHEMA + sqlFragement;
        }
        return sql;
    }

    private boolean hasSchema() {
        return schema != null;
    }
}
