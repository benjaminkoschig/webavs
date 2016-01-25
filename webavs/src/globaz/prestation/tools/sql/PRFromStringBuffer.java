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
public class PRFromStringBuffer extends PRCustomSQLStringBuffer {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    protected String _defaultSchema = "";

    protected String _innerJoin = " INNER JOIN ";
    protected String _leftJoin = " LEFT JOIN ";

    protected String _on = " ON ";

    private String _point = ".";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public PRFromStringBuffer() {
        super();
    }

    public PRFromStringBuffer(String defaultSchema) {
        super();
        _defaultSchema = defaultSchema;
    }

    /**
     * @param str
     * @return
     * @see java.lang.StringBuffer#append(java.lang.String)
     */
    public PRFromStringBuffer append(String str) {
        sb.append(str);
        return this;
    }

    /**
     * @param sb
     * @return
     * @see java.lang.StringBuffer#append(java.lang.StringBuffer)
     */
    public PRFromStringBuffer append(StringBuffer sb) {
        this.sb.append(sb);
        return this;
    }

    /**
     * ajoute le signe de comparaison numérique ("=" par défaut)
     * 
     * @return String d'égalité
     */
    public PRFromStringBuffer appendEgal() {
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
     * @return le PRFromStringBuffer modifié
     */
    public PRFromStringBuffer appendField(String table, String field) {
        sb.append(_defaultSchema + table + _point + field);
        return this;
    }

    /**
     * ajoute le signe de comparaison text (" LIKE " par défaut)
     * 
     * @return String d'égalité
     */
    public PRFromStringBuffer appendLike() {
        sb.append(_like);
        return this;
    }

    public PRFromStringBuffer appendOn() {
        sb.append(_on);
        return this;
    }

    /**
     * innerJoin ajoute une clause INNER JOIN.
     * 
     * @param table
     *            nom de la table jointe
     * @return le PRFromStringBuffer modifié
     */
    public PRFromStringBuffer innerJoin(String table) {
        sb.append(_innerJoin);
        sb.append(_defaultSchema);
        sb.append(table);
        return this;
    }

    /**
     * leftJoin ajoute une clause LEFT JOIN.
     * 
     * @param table
     *            nom de la table jointe
     * @return le PRFromStringBuffer modifié
     */
    public PRFromStringBuffer leftJoin(String table) {
        sb.append(_leftJoin);
        sb.append(_defaultSchema);
        sb.append(table);
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

    public void setInnerJoin(String innerJoin) {
        _innerJoin = innerJoin;
    }

    public void setLeftJoin(String leftJoin) {
        _leftJoin = leftJoin;
    }

    public void setOn(String on) {
        _on = on;
    }

    public void setPoint(String point) {
        _point = point;
    }

}
