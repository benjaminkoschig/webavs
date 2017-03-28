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
 * Le but de cette class est de permettre la cr�ation de requ�te ou une partie de requ�te.
 * Le comportement est le suivant lors que l'on donne un param�trer et que celui-ci est vide on ajoute pas le fragment
 * SQL � l requ�te.
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
     * Cr�er l'instance.
     * 
     * @return SQLWriter la nouvelle instance cr�er
     */
    public static SQLWriter write() {
        return new SQLWriter(null);
    }

    /**
     * Cr�er l'instance et d�finit que lorsque l'on ajoute pour chaque table(sqlFragment) le mot schema devant.
     * 
     * @return SQLWriter la nouvelle instance cr�er
     */
    public static SQLWriter writeWithSchema() {
        return new SQLWriter(null, true);
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
     * Ajout les fields � la requ�te.
     * 
     * @return SQLWriter utilis�
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
     * Ajout les fields � la requ�te.
     * 
     * @return SQLWriter utilis�
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
        query.append("max(" + addSchemaToSql(column) + ") ");
        return this;
    }

    /**
     * Ajout le mot max � la requ�te et ajoute la colonne dans le max.
     * 
     * @param column(TableDefinition) sur laquelle utilis� le mot cl� max
     * @return SQLWriter utilis�
     */
    public SQLWriter max(TableDefinition tableDefinition) {
        query.append("max(" + CONST_SCHEMA + tableDefinition.getTableName() + "." + tableDefinition.getColumnName()
                + ") ");
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
        query.append(" from ").append(addSchemaToSql(sqlFragement));
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
    public SQLWriter and(boolean condition, String sqlFramgment) {
        if (condition) {
            and(sqlFramgment);
        }
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
        query.append(" ").append(addSchemaToSql(sqlFramgment));
        return this;
    }

    /**
     * Ajoute le = avec le param�tre d�finit � la requ�te SQL
     * 
     * @param param String sql
     * @return SQLWriter utilis�
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
     * Ajoute le = avec le param�tre d�finit � la requ�te SQL
     * 
     * @param param Le code system
     * @return SQLWriter utilis�
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
     * Ajoute le = avec le param�tre d�finit � la requ�te SQL
     * et transforme la chaine de caract�re en date globaz.
     * EX: 10.2015 = > 201510, 01.10.2015 =>20151001
     * 
     * @param param String sql
     * @return SQLWriter utilis�
     */
    public SQLWriter equalForDate(String param) {
        Integer date = null;
        if (param != null && !param.isEmpty()) {
            date = datToGlobazFormat(param);
        }
        return equal(date);
    }

    /**
     * Ajoute le = avec le param�tre d�finit � la requ�te SQL
     * 
     * @param param Integer sql
     * @return SQLWriter utilis�
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
     * Ajoute le "in(values)" avec le param�tre d�finit � la requ�te SQL
     * Attention : Les guillemets et les doubles guillemets ne sont pas remplac�s par les caract�res suivants : '�' et
     * '�'
     * 
     * @param param String sql
     * @return SQLWriter utilis�
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
     * Ajoute le "in(x,y,z)" avec le param�tre donn�e ajoute les ","
     * Attention : Les guillemets et les doubles guillemets ne sont pas remplac�s par les caract�res suivants : '�' et
     * '�'
     * 
     * @param param String sql
     * @return SQLWriter utilis�
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
     * Ajoute le "in('x','y','z')" avec le param�tre donn�e ajoute les "','"
     * Attention : Les guillemets et les doubles guillemets ne sont pas remplac�s par les caract�res suivants : '�' et
     * '�'
     * 
     * @param param String sql
     * @return SQLWriter utilis�
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
     * Ajoute le mot 'and' � la requ�te si besoin
     * 
     * @param sqlFramgment
     * @return SQLWriter utilis�
     */
    public SQLWriter and(TableDefinition tableDefinition) {
        String column = tableDefinition.getTableName() + "." + tableDefinition.getColumnName();

        this.and();
        query.append(" ").append(addSchemaToSql(CONST_SCHEMA + column));

        return this;
    }

    /**
     * Ajoute le mot 'and' � la requ�te si besoin
     * 
     * @param sqlFramgment
     * @return SQLWriter utilis�
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
     * Ajoute le mot 'like %param%' � la requ�te si besoin
     * 
     * @param sqlFramgment
     * @return SQLWriter utilis�
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
     * Ajoute le mot 'like param' � la requ�te si besoin
     * 
     * @param sqlFramgment
     * @return SQLWriter utilis�
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
     * Ajoute le mot 'and' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null).
     * 
     * @param condition Condition pour ajouter le fragment.
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: nom = '?' , => nom = 'Test').
     * @return SQLWriter utilis�
     */
    public SQLWriter and(boolean condition, String sqlFragement, Integer... params) {
        if (condition) {
            and(sqlFragement, params);
        }
        return this;
    }

    /**
     * Ajoute le mot 'and' � la requ�te(si besoin) et le fragment SQL si les param�tres ne sont pas vide(null).
     * 
     * @param params Param�tres � utilis� dans le fragment SQL
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
     * @param condition Condition pour ajouter le fragment.
     * @param params Param�tr�es � utilis� dans le fragment SQL
     * @param sqlFragement SQl � ajout� � la requ�te. Les caract�res [?] sont remplac�s par les params donn�s en
     *            param�tre(Ex: and('Test', '12') nom = '?' and age = ? , => nom = 'Test' and age = 12).
     * @return SQLWriter utilis�
     */
    public SQLWriter and(boolean condition, String sql, String... params) {
        if (condition) {
            and(sql, params);
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
    public SQLWriter or(String sqlFragment) {
        addOpertor("or");
        query.append(" ").append(addSchemaToSql(sqlFragment));
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

    /**
     * Serialize la requ�te SQL.
     * 
     * @return le SQL si un sch�ma a �t� d�finit celui-ci sera automatiquement remplac�.
     */
    public String toSql() {
        String sql = query.toString();
        if (hasSchema()) {
            sql = sql.replaceAll(CONST_SCHEMA, schema);
        }
        return this.replace(sql, paramsToUse);
    }

    private String replaceQuotes(String param) {
        return param.replace('\'', '�').replace('"', '�');
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
