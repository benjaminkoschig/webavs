package ch.globaz.common.sql;

import java.util.Collection;
import org.apache.commons.lang.StringUtils;

/**
 * Le but de cette class est de permettre la cr�ation de requ�te ou une partie de requ�te.
 * 
 * @author dma
 * 
 */
public class SQLWriter {

    private final StringBuffer query = new StringBuffer();
    private final String schema;
    private boolean mustAddOperator = false;

    private SQLWriter(String schema) {
        this.schema = schema;
    }

    /**
     * Cr�er l'instance.
     * 
     * @return SQLWriter la nouvelle instance cr�er
     */
    public static SQLWriter write() {
        return new SQLWriter(null);
    }

    /**
     * Cr�er l'instance en d�finissant le schema qui sera utilis� pour le SQL.
     * 
     * @return SQLWriter la nouvelle instance cr�er
     */
    public static SQLWriter write(String schema) {
        return new SQLWriter(schema);
    }

    /**
     * Ajoute le mot select � la requ�te et ajoute le fragment SQL pass� en param�tres � la requ�te.
     * 
     * @param sqlFragement SQl � ajout� � la requ�te. Le fragment devrait contenir que les fields.
     * @return SQLWriter utilis�
     */

    public SQLWriter select(String sqlFragement) {
        query.append("select ").append(sqlFragement);
        return this;
    }

    /**
     * Ajoute le mot from � la requ�te et ajoute le fragment SQL pass� en param�tres � la requ�te.
     * 
     * @param sqlFragement SQl � ajout� � la requ�te. Le fragment devrait contenir que la table pour la clause from.
     * 
     * @return SQLWriter utilis�
     */
    public SQLWriter from(String sqlFragement) {
        query.append(" from ").append(sqlFragement);
        return this;
    }

    /**
     * Ajoute le mot where � la requ�te.
     * 
     * @return SQLWriter utilis�.
     */
    public SQLWriter where() {
        query.append(" where");
        return this;
    }

    /**
     * Ajoute le mot where � la requ�te et ajoute le fragment SQL pass� en param�tres � la requ�te.
     * 
     * @param sqlFragement SQl � ajout� � la requ�te.
     * 
     * @return SQLWriter utilis�
     */
    public SQLWriter where(String sqlFragement) {
        this.where().query.append(" ").append(sqlFragement);
        mustAddOperator = true;
        return this;
    }

    /**
     * Ajoute le mot 'where' � la requ�te et ajoute le fragment SQL pass� en param�tre � la requ�te. Si params n'est pas
     * vide le fragment SQL est ajoute � la requ�te.
     * 
     * @param sqlFragement SQl � ajout� � la requ�te. les caract�res [?] sont remplac�s par ordre des params donn�e en
     *            param�tre(Ex: nom = '?' and age = '?', => nom = 'Test' and age=12).
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * 
     * @return SQLWriter utilis�
     */
    public SQLWriter where(String sqlFragement, String... params) {
        this.where();
        this.and(sqlFragement, params);
        mustAddOperator = true;
        return this;
    }

    /**
     * Ajoute le mot 'where' � la requ�te et ajoute le fragment SQL pass� en param�tre � la requ�te. Si le param donn�
     * n'est pas vide le fragment SQL est ajoute � la requ�te.
     * 
     * @param sqlFragement SQl � ajout� � la requ�te. le caract�re [?] est remplacer par le param donn�e en
     *            param�tre(Ex: nom = '?' , => nom = 'Test').
     * @param params Param�tr�es � utilis� dans le fragment SQL.
     * 
     * @return SQLWriter utilis�
     */
    public SQLWriter where(String sqlFragement, Collection<String> params) {
        this.where();
        this.and(sqlFragement, params);
        mustAddOperator = true;
        return this;
    }

    /**
     * Ajoute le mot 'and' � la requ�te si besoin.
     * 
     * @param fragmentSql
     * @return SQLWriter utilis�
     */
    public SQLWriter and() {
        addOpertor("and");
        return this;
    }

    /**
     * Ajoute le mot 'and' � la requ�te si besoin et le sqlFramgment.
     * 
     * @param sqlFramgment
     * @return SQLWriter utilis�
     */
    public SQLWriter and(String sqlFramgment) {
        this.and();
        query.append(" ").append(sqlFramgment);
        return this;
    }

    /**
     * Ajoute le mot 'and' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null).
     * 
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: nom = '?' , => nom = 'Test').
     * @return SQLWriter utilis�
     */
    public SQLWriter and(String sqlFragement, Integer... params) {
        if (isNotEmpty(params)) {
            this.and(replace(sqlFragement, params));
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilis�
     */
    public SQLWriter and(String sql, String... params) {
        if (isNotEmpty(params)) {
            this.and(replace(sql, params));
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilis�
     */
    public SQLWriter and(String sql, Collection<String> params) {
        if (params != null && !params.isEmpty()) {
            this.and(replace(sql, params));
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' � la requ�te.
     * 
     * @param sqlFragement
     * @return SQLWriter utilis�
     */
    public SQLWriter or(String sqlFramgment) {
        addOpertor("or");
        query.append(" ").append(sqlFramgment);
        return this;
    }

    /**
     * Ajoute le mot 'or' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null).
     * 
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: nom = '?' , => nom = 'Test').
     * @return SQLWriter utilis�
     */
    public SQLWriter or(String sql, Integer... params) {
        if (isNotEmpty(params)) {
            this.or(replace(sql, params));
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilis�
     */
    public SQLWriter or(String sql, String... params) {
        if (isNotEmpty(params)) {
            this.or(replace(sql, params));
        }
        return this;
    }

    /**
     * Ajoute le mot 'or' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilis�
     */
    public SQLWriter or(String sql, Collection<String> params) {
        if (params != null && !params.isEmpty()) {
            this.or(replace(sql, params));
        }
        return this;
    }

    /**
     * Ajoute le fragment SQL � la requ�te et met un espace devant le fragment SQL.
     * 
     * @param sqlFragment Le fragment SQL � ajouter � la requ�te.
     * @return SQLWriter utilis�
     */
    public SQLWriter append(String sqlFragment) {
        if (!isQueryEmpty()) {
            query.append(" ");
        }
        query.append(sqlFragment);
        return this;
    }

    /**
     * Ajoute le fragment SQL � la requ�te et met un espace devant le fragment SQL.
     * 
     * @param sqlFragment Le fragment SQL � ajouter � la requ�te.
     * @return SQLWriter utilis�
     */
    public SQLWriter append(String sqlFragment, String... params) {
        query.append(replace(sqlFragment, params));
        return this;
    }

    /**
     * Ajoute le fragment SQL � la requ�te si la condition est vrais.
     * 
     * 
     * @param sqlFragment Le fragment SQL � ajouter � la requ�te.
     * @param condition Condition pour ajouter le fragment.
     * @return SQLWriter utilis�
     */
    public SQLWriter append(boolean condition, String sqlFragment) {
        if (condition) {
            this.append(sqlFragment);
        }
        return this;
    }

    /**
     * Ajoute le mot 'inner join' � la requ�te et ajoute le fragment SQL � la requ�te.
     * 
     * @param sqlFragment Le fragment SQL � ajouter � la requ�te.
     * @return SQLWriter utilis�
     */
    public SQLWriter join(String sql) {
        query.append(" inner join ").append(sql);
        return this;
    }

    /**
     * Ajoute le mot 'inner join' � la requ�te et ajoute le fragment SQL � la requ�te si la condition est vrais.
     * 
     * @param sqlFragment Le fragment SQL � ajouter � la requ�te.
     * @param condition Condition pour ajouter le fragment.
     * @return SQLWriter utilis�
     */
    public SQLWriter join(boolean condition, String sql) {
        if (condition) {
            query.append(" inner join ").append(sql);
        }
        return this;
    }

    /**
     * Ajoute le mot 'left join' � la requ�te et ajoute le fragment SQL � la requ�te.
     * 
     * @param sqlFragment Le fragment SQL � ajouter � la requ�te.
     * @return SQLWriter utilis�
     */
    public SQLWriter leftJoin(String sql) {
        query.append(" left join ").append(sql);
        return this;
    }

    /**
     * Ajoute le mot 'left join' � la requ�te et ajoute le fragment SQL � la requ�te si la condition est vrais.
     * 
     * @param sqlFragment Le fragment SQL � ajouter � la requ�te.
     * @param condition Condition pour ajouter le fragment.
     * @return SQLWriter utilis�
     */
    public SQLWriter leftJoin(boolean condition, String sql) {
        if (condition) {
            query.append(" left join ").append(sql);
        }
        return this;
    }

    /**
     * Ajoute le mot 'right join' � la requ�te et ajoute le fragment SQL � la requ�te.
     * 
     * @param sqlFragment Le fragment SQL � ajouter � la requ�te.
     * @return SQLWriter utilis�
     */
    public SQLWriter rightJoin(String sql) {
        query.append(" right join ").append(sql);
        return this;
    }

    /**
     * Ajoute le mot 'right join' � la requ�te et ajoute le fragment SQL � la requ�te si la condition est vrais.
     * 
     * @param sqlFragment Le fragment SQL � ajouter � la requ�te.
     * @param condition Condition pour ajouter le fragment.
     * @return SQLWriter utilis�
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
     * Serialize la requ�te SQL.
     * 
     * @return le SQL si un sch�ma a �t� d�finit celui-ci sera automatiquement remplac�.
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

    static String toStringForIn(Integer... params) {
        return StringUtils.join(params, ",");
    }

    static String toStringForIn(String... params) {
        return "\'" + StringUtils.join(params, "','") + "\'";
    }

    static String toStringForIn(Collection<String> params) {
        return "\'" + StringUtils.join(params, "','") + "\'";
    }

    String replace(String sql, String... params) {
        for (String p : params) {
            sql = replace(sql, p);
        }
        return sql;
    }

    String replace(String sql, Integer... params) {
        for (Integer p : params) {
            sql = replace(sql, String.valueOf(p));
        }
        return sql;
    }

    String replace(String sql, Collection<String> params) {
        for (String p : params) {
            sql = replace(sql, p);
        }
        return sql;
    }

    private void addOpertor(String operator) {
        if (mustAddOperator) {
            query.append(" ").append(operator);
        }
        mustAddOperator = true;
    }

    private String replace(String sql, String p) {
        return sql.replaceFirst("\\?", p);
    }

}
