package ch.globaz.common.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.jadedb.TableDefinition;
import com.google.common.base.Joiner;

/**
 * Le but de cette class est de permettre la cr�ation de requ�te ou une partie de requ�te.
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
     * Cr�er l'instance.
     * 
     * @return SQLWriter la nouvelle instance cr�er
     */
    public static SQLWriter write() {
        return new SQLWriter(null);
    }

    /**
     * Cr�er l'instance en d�finissant le schema qui sera utilis� pour le SQL.
     * Le "." dans le sch�ma doit �tre fourni
     * 
     * @return SQLWriter la nouvelle instance cr�er
     */
    public static SQLWriter write(String schema) {
        return new SQLWriter(schema);
    }

    /**
     * Ajout le mot select � la requ�te.
     * 
     * @return SQLWriter utilis�
     */
    public SQLWriter select() {
        query.append("select ");
        return this;
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
     * Ajout le mot max � la requ�te et ajoute la colonne dans le max.
     * 
     * @param column sur laquelle utilis� le mot cl� max
     * @return SQLWriter utilis�
     */
    public SQLWriter max(String column) {
        query.append("max(" + column + ") ");
        return this;
    }

    public SQLWriter max(TableDefinition tableDefinition) {
        return max(SCHEMA + tableDefinition.getTableName() + "." + tableDefinition.getColumn());
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
        countAddOperators++;
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
        countAddOperators++;
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
        countAddOperators++;
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
     * Ajoute le mot 'and' � la requ�te si besoin et le sqlFramgment.
     * 
     * @param sqlFramgment
     * @return SQLWriter utilis�
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
     * Ajoute le mot 'and' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null).
     * 
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: nom = '?' , => nom = 'Test').
     * @return SQLWriter utilis�
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
     * TODO que faire si certaine param�trer sont vide ???
     * 
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
            for (String p : params) {
                paramsToUse.add(p);
            }
            this.and(sql);
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
            paramsToUse.addAll(params);
            this.and(sql);
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
     * Ajoute le mot 'or' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilis�
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
     * Ajoute le mot 'or' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null ou chain
     * vide).
     * 
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilis�
     */
    public SQLWriter or(String sqlFragement, Collection<String> params) {
        if (params != null && !params.isEmpty()) {
            paramsToUse.addAll(params);
            this.or(sqlFragement);
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
     * Ajoute le fragment SQL � la requ�te si les params donn�e en param�tres ne sont pas vide.
     * 
     * @param sqlFragment Le fragment SQL � ajouter.
     * @param params Les param�tres � utiliser pour la requ�te.
     * @return SQLWriter utilis�
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
     * Ajoute le fragment SQL � la requ�te si les params donn�e en param�tres ne sont pas vide.
     * 
     * @param sqlFragment Le fragment SQL � ajouter.
     * @param params Les param�tres � utiliser pour la requ�te.
     * @return SQLWriter utilis�
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
