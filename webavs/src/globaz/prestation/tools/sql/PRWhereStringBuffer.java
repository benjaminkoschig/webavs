/**
 * 
 */
package globaz.prestation.tools.sql;

/**
 * @author ECO
 * 
 *         <P>
 *         WhereStringBuffer est une extension par délegation de StringBuffer pour faciliter construction d'une clause
 *         WHERE.
 *         </P>
 */
public class PRWhereStringBuffer {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    protected String _defaultSchema = "";
    protected String _egal = "=";
    protected String _et = " AND ";
    protected String _like = " LIKE ";

    protected String _ou = " OR ";
    private String _point = ".";

    private StringBuffer sb = new StringBuffer();

    // ~ Constructors
    // --------------------------------------------------------------------------------------------------------

    public PRWhereStringBuffer() {
        super();
    }

    public PRWhereStringBuffer(String defaultSchema) {
        super();
        _defaultSchema = defaultSchema;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param str
     * @return le PRWhereStringBuffer modifié
     * @see java.lang.StringBuffer#append(java.lang.String)
     */
    public PRWhereStringBuffer append(String str) {
        sb.append(str);
        return this;
    }

    /**
     * @param sb
     * @return le PRWhereStringBuffer modifié
     * @see java.lang.StringBuffer#append(java.lang.StringBuffer)
     */
    public PRWhereStringBuffer append(StringBuffer sb) {
        this.sb.append(sb);
        return this;
    }

    public PRWhereStringBuffer appendDateGreaterOrNull(String table, String field, String value) {
        sb.append("(" + _defaultSchema + table + _point + field + " >= " + value + _ou + _defaultSchema + table
                + _point + field + " IS NULL" + _ou + _defaultSchema + table + _point + field + " = 0)");

        return this;
    }

    /**
     * ajoute le signe de comparaison numérique ("=" par défaut)
     * 
     * @return le PRWhereStringBuffer modifié
     */
    public PRWhereStringBuffer appendEgal() {
        sb.append(_egal);
        return this;
    }

    /**
     * ajoute un champ construit automatiquement sur le template "defaultSchema + table + point + field". defaultSchema
     * et point sur des champs parametrables de l'objet.
     * 
     * @param table
     *            nom de la table du champ
     * @param field
     *            nom du champ
     * @return le PRWhereStringBuffer modifié
     */
    public PRWhereStringBuffer appendField(String table, String field) {
        sb.append(_defaultSchema + table + _point + field);
        return this;
    }

    /**
     * ajoute le signe de comparaison text (" LIKE " par défaut)
     * 
     * @return le PRWhereStringBuffer modifié
     */
    public PRWhereStringBuffer appendLike() {
        sb.append(_like);
        return this;
    }

    /**
     * @return the _ou
     */
    public String getOu() {
        return _ou;
    }

    /**
     * @return
     * @see java.lang.StringBuffer#length(java.lang.StringBuffer)
     */
    public int length() {
        return sb.length();
    }

    /**
     * newClause ajoute une clause à la query where, et ajoute un AND si nécessaire (si la chaine n'est pas vide)
     * 
     * @return le PRWhereStringBuffer modifié
     */
    public PRWhereStringBuffer newClause() {
        if (sb.length() > 0) {
            sb.append(_et);
        }
        return this;
    }

    /**
     * définit le schema utilisé par défaut pour ajouter des tables et champs dans la requête.
     * 
     * @param defaultSchema
     *            nom du schema
     */
    public void setDefaultSchema(String defaultSchema) {
        _defaultSchema = defaultSchema;
    }

    /**
     * @param egal
     *            the _egal to set
     */
    public void setEgal(String egal) {
        _egal = egal;
    }

    /**
     * @param et
     *            the _et to set
     */
    public void setEt(String et) {
        _et = " " + et.trim() + " ";
    }

    /**
     * @param like
     *            the _like to set
     */
    public void setLike(String like) {
        _like = like;
    }

    /**
     * @param ou
     *            the _ou to set
     */
    public void setOu(String ou) {
        _ou = ou;
    }

    public void setPoint(String point) {
        _point = point;
    }

    /**
     * @param start
     * @return
     * @see java.lang.StringBuffer#substring(int)
     */
    public String substring(int start) {
        return sb.substring(start);
    }

    /**
     * @param start
     * @param end
     * @return
     * @see java.lang.StringBuffer#substring(int, int)
     */
    public String substring(int start, int end) {
        return sb.substring(start, end);
    }

    /**
     * @return
     * @see java.lang.StringBuffer#toString()
     */
    @Override
    public String toString() {
        return sb.toString();
    }

    /**
     * Retourne l'objet sous forme de StringBuffer
     * 
     * @return l'objet sous sa forme StringBuffer
     */
    public StringBuffer toStringBuffer() {
        return sb;
    }

}
