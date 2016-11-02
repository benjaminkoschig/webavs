package ch.globaz.common.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.jadedb.TableDefinition;
import com.google.common.base.Joiner;

/**
 * Le but de cette class est de permettre la création de requête ou une partie de requête.
 * 
 * @author dma
 * 
 */
public class SQLWriter {

    private final StringBuffer query = new StringBuffer();
    private final String schema;
    private int countAddOperators = 0;
    private String charToReplace = "?";
    private List<String> paramsToUse = new ArrayList<String>();
    private Integer currentIndice;
    private static final String SCHEMA = "schema.";

    private SQLWriter(String schema) {
        this.schema = schema;
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
     * Créer l'instance en définissant le schema qui sera utilisé pour le SQL.
     * Le "." dans le schéma doit être fourni
     * 
     * @return SQLWriter la nouvelle instance créer
     */
    public static SQLWriter write(String schema) {
        return new SQLWriter(schema);
    }

    /**
     * Ajout le mot select ä la requête.
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter select() {
        query.append("select ");
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
        query.append("max(" + column + ") ");
        return this;
    }

    public SQLWriter max(TableDefinition tableDefinition) {
        return max(SCHEMA + tableDefinition.getTableName() + "." + tableDefinition.getColumn());
    }

    /**
     * Ajoute le mot from à la requête et ajoute le fragment SQL passé en paramètres à la requête.
     * 
     * @param sqlFragement SQl à ajouté à la requête. Le fragment devrait contenir que la table pour la clause from.
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter from(String sqlFragement) {
        query.append(" from ").append(sqlFragement);
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
    public SQLWriter and(String sqlFramgment) {
        this.and();
        query.append(" ").append(sqlFramgment);
        return this;
    }

    int currentIndex() {
        currentIndice = query.length();
        return currentIndice;
    }

    public SQLWriter equal(String param) {
        if (param != null && !param.isEmpty()) {
            paramsToUse.add(param);
            query.append("='?'");
        } else {
            rollback();
        }
        return this;
    }

    public SQLWriter equal(Integer param) {
        if (param != null) {
            paramsToUse.add(String.valueOf(param));
            query.append("=?");
        } else {
            rollback();
        }
        return this;
    }

    public SQLWriter in(String values) {
        if (values != null && !values.isEmpty()) {
            paramsToUse.add(values);
            query.append(" in (?)");
        } else {
            rollback();
        }
        return this;
    }

    public SQLWriter in(Collection<?> list) {
        if (list != null && !list.isEmpty()) {
            String params = Joiner.on(",").join(list);
            paramsToUse.add(params);
            query.append(" in (?)");
        } else {
            rollback();
        }
        return this;
    }

    SQLWriter rollback() {
        if (currentIndice != null) {
            query.delete(currentIndice, query.length());
            currentIndice = null;
            countAddOperators--;
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête si besoin et le sqlFramgment.
     * 
     * @param sqlFramgment
     * @return SQLWriter utilisé
     */
    public SQLWriter and(TableDefinition tableDefinition) {
        String column = tableDefinition.getTableName() + "." + tableDefinition.getColumn();
        if (hasSchema()) {
            column = SCHEMA + column;
        }
        this.and(column);
        return this;
    }

    public SQLWriter like(String param) {
        if (param != null && !param.isEmpty()) {
            paramsToUse.add(param);
            query.append(" like '%?%'");
        } else {
            rollback();
        }
        return this;
    }

    private boolean hasSchema() {
        return schema != null;
    }

    /**
     * Ajoute le mot 'and' à la requête(si besoin) et le fragment SQL si les paramètres ne sont pas vide(null).
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL
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
    public SQLWriter or(String sqlFramgment) {
        addOpertor("or");
        query.append(" ").append(sqlFramgment);
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

    boolean isQueryEmpty() {
        return query.length() == 0;
    }

    /**
     * Serialize la requête SQL.
     * 
     * @return le SQL si un schéma a été définit celui-ci sera automatiquement remplacé.
     */
    public String toSql() {
        String sql = query.toString();
        if (hasSchema()) {
            sql = sql.replaceAll(SCHEMA, schema);
        }
        return this.replace(sql, paramsToUse);
    }

    boolean isNotEmpty(String... param) {
        if (param == null) {
            return false;
        }

        for (String p : param) {
            if (!(param[0] == null || param[0].length() == 0)) {
                return true;
            }
        }

        return false;
    }

    boolean isNotEmpty(Integer... param) {
        if (param.length == 1) {
            if (param[0] == null) {
                return false;
            }
        }
        return true;
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

    private void addOpertor(String operator) {
        currentIndex();
        if (countAddOperators > 0) {
            query.append(" ").append(operator);
        }
        countAddOperators++;
    }

    int countCharToReplace(String sqlFragment) {
        return StringUtils.countMatches(sqlFragment, charToReplace);
    }

    private String replace(String sql, String p) {
        return StringUtils.replaceOnce(sql, charToReplace, p);
    }

}
