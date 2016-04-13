package ch.globaz.common.sql;

import java.util.Collection;
import org.apache.commons.lang.StringUtils;

/**
 * Le but de cette class est de permettre de créer une requête ou une partie de requête.
 * 
 * @author dma
 * 
 */
public class SQLWriter {

    private StringBuffer query = new StringBuffer();
    private String schema;
    private boolean mustAddOperator = false;

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
     * 
     * @return SQLWriter la nouvelle instance créer
     */
    public static SQLWriter write(String schema) {
        return new SQLWriter(schema);
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
     * @param sqlFragement SQl à ajouté à la requête. Le fragment devrait contenir que la condition.
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter where(String sqlFragement) {
        this.where().query.append(" ").append(sqlFragement);
        mustAddOperator = true;
        return this;
    }

    /**
     * Ajoute le mot 'where' à la requête et ajoute le fragment SQL passé en paramètre à la requête. Si le param donné
     * n'est pas vide le fragment SQL est ajoute à la requête.
     * 
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = '?' , => nom = 'Test').
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : p1,p2,p3.
     * 
     * @return SQLWriter utilisé
     */
    public SQLWriter where(String sqlFragement, String... params) {
        this.where();
        this.and(sqlFragement, params);
        mustAddOperator = true;
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête.
     * 
     * @param sqlFragement
     * @return SQLWriter utilisé
     */
    public SQLWriter and(String sqlFragement) {
        addOpertor(sqlFragement, "and");
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête et le fragment SQL si les paramètres ne sont pas vide.
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : p1,p2,p3
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = '?' , => nom = 'Test'). * @param sqlFragement
     * @return SQLWriter utilisé
     */
    public SQLWriter and(String sqlFragement, Integer... params) {
        if (isNotEmpty(params)) {
            this.and(replace(sqlFragement, StringUtils.join(params, ',')));
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête et le fragment SQL si les paramètres ne sont pas vide.
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : p1,p2,p3
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = '?' , => nom = 'Test'). * @param sqlFragement
     * @return SQLWriter utilisé
     */
    public SQLWriter and(String sql, String... params) {
        if (isNotEmpty(params)) {
            this.and(replace(sql, StringUtils.join(params, ',')));
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête et le fragment SQL si les paramètres ne sont pas vide.
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : p1,p2,p3
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = '?' , => nom = 'Test'). * @param sqlFragement
     * @return SQLWriter utilisé
     */
    public SQLWriter and(String sql, Collection<String> params) {
        if (params != null && !params.isEmpty()) {
            this.and(replace(sql, StringUtils.join(params, ',')));
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête et le fragment SQL si les paramètres ne sont pas vide.
     * Ajoute automatiquement des quotes au paramètres données
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : 'p1','p2','p3'
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = ? , => nom = 'Test').
     * @return SQLWriter utilisé
     */
    public SQLWriter andAddQuote(String sql, String... params) {
        if (isNotEmpty(params)) {
            this.and(replace(sql, toStringForIn(params)));
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' à la requête et le fragment SQL si les paramètres ne sont pas vide.
     * Ajoute automatiquement des quotes au paramètres données
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : 'p1','p2','p3'
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = ? , => nom = 'Test').
     * @return SQLWriter utilisé
     */
    public SQLWriter andAddQuote(String sql, Collection<String> params) {
        if (params != null && !params.isEmpty()) {
            this.and(replace(sql, toStringForIn(params)));
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' à la requête.
     * 
     * @param sqlFragement
     * @return SQLWriter utilisé
     */
    public SQLWriter or(String sqlFragement) {
        addOpertor(sqlFragement, "or");
        return this;
    }

    /**
     * Ajoute le mot 'or' à la requête et le fragment SQL si les paramètres ne sont pas vide.
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : p1,p2,p3
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = '?' , => nom = 'Test'). * @param sqlFragement
     * @return SQLWriter utilisé
     */
    public SQLWriter or(String sql, Integer... params) {
        if (isNotEmpty(params)) {
            this.or(replace(sql, StringUtils.join(params, ',')));
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' à la requête et le fragment SQL si les paramètres ne sont pas vide.
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : p1,p2,p3
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = '?' , => nom = 'Test'). * @param sqlFragement
     * @return SQLWriter utilisé
     */
    public SQLWriter or(String sql, String... params) {
        if (isNotEmpty(params)) {
            this.or(replace(sql, StringUtils.join(params, ',')));
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' à la requête et le fragment SQL si les paramètres ne sont pas vide.
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : p1,p2,p3
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = '?' , => nom = 'Test'). * @param sqlFragement
     * @return SQLWriter utilisé
     */
    public SQLWriter or(String sql, Collection<String> params) {
        if (params != null && !params.isEmpty()) {
            this.or(replace(sql, StringUtils.join(params, ',')));
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' à la requête et le fragment SQL si les paramètres ne sont pas vide.
     * Ajoute automatiquement des quotes au paramètres données
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : 'p1','p2','p3'
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = ? , => nom = 'Test').
     * @return SQLWriter utilisé
     */
    public SQLWriter orAddQuote(String sql, String... params) {
        if (isNotEmpty(params)) {
            this.or(replace(sql, toStringForIn(params)));
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' à la requête et le fragment SQL si les paramètres ne sont pas vide.
     * Ajoute automatiquement des quotes au paramètres données
     * 
     * @param params Paramétrées à utilisé dans le fragment SQL, si il y a plusieurs paramètres ceux-ci seront sérialisé
     *            de la manière suivante : 'p1','p2','p3'
     * @param sqlFragement SQl à ajouté à la requête. le caractère [?] est remplacer par le param donnée en
     *            paramètre(Ex: nom = ? , => nom = 'Test').
     * @return SQLWriter utilisé
     */
    public SQLWriter orAddQuote(String sql, Collection<String> params) {
        if (params != null && !params.isEmpty()) {
            this.or(replace(sql, toStringForIn(params)));
        }
        return this;
    }

    /**
     * Ajoute le fragment SQL à la requête.
     * 
     * @param sqlFragment Le fragment SQL à ajouter à la requête.
     * @return SQLWriter utilisé
     */
    public SQLWriter append(String sqlFragment) {
        query.append(sqlFragment);
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
    public void append(String sqlFragment, boolean condition) {
        if (condition) {
            query.append(sqlFragment);
        }
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
    public SQLWriter join(String sql, boolean condition) {
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
    public SQLWriter leftJoin(String sql, boolean condition) {
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
    public SQLWriter rightJoin(String sql, boolean condition) {
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
        if (schema != null) {
            return query.toString().replaceAll("schema.", schema);
        }
        return query.toString();
    }

    boolean isNotEmpty(String... param) {
        if (param.length == 1) {
            if (param[0] == null || param[0].length() == 0) {
                return false;
            }
        }
        return true;
    }

    boolean isNotEmpty(Integer... param) {
        if (param.length == 1) {
            if (param[0] == null) {
                return false;
            }
        }
        return true;
    }

    static String toStringForIn(String... params) {
        return "\'" + StringUtils.join(params, "\','") + "\'";
    }

    static String toStringForIn(Collection<String> params) {
        return "\'" + StringUtils.join(params, "\','") + "\'";
    }

    String replaceParam(String sql, String param) {
        return replace(sql, param);
    }

    private void addOpertor(String sql, String operator) {
        if (mustAddOperator) {
            query.append(" ").append(operator);
        }
        query.append(" ").append(sql);
        mustAddOperator = true;
    }

    private String replace(String sql, String param) {
        return sql.replaceAll("\\?", param);
    }
}
