package globaz.aquila.db.access.batch;

import globaz.globall.db.BConstants;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 11:25:33)
 * 
 * @author: Administrator
 */
public class COParametreTaxe extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FNAME_EST_REQUIS = "OJBREQ";
    public static final String FNAME_ID_CALCUL_TAXE = "OIICTX";
    public static final String FNAME_ID_RUBRIQUE = "OJIRUB";

    public static final String TABLE_NAME = "COTXPTP";

    private java.lang.Boolean estRequis = new Boolean(false);
    private java.lang.Boolean estTrouve = new Boolean(false);
    private java.lang.String idCalculTaxe = new String();
    private java.lang.String idRubrique = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CAParametreTaxe
     */
    public COParametreTaxe() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return COParametreTaxe.TABLE_NAME;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRubrique = statement.dbReadNumeric(COParametreTaxe.FNAME_ID_RUBRIQUE);
        idCalculTaxe = statement.dbReadNumeric(COParametreTaxe.FNAME_ID_CALCUL_TAXE);
        estRequis = statement.dbReadBoolean(COParametreTaxe.FNAME_EST_REQUIS);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(COParametreTaxe.FNAME_ID_RUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), ""));
        statement.writeKey(COParametreTaxe.FNAME_ID_CALCUL_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(COParametreTaxe.FNAME_ID_RUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(COParametreTaxe.FNAME_ID_CALCUL_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), "idCalculTaxe"));
        statement.writeField(COParametreTaxe.FNAME_EST_REQUIS, this._dbWriteBoolean(statement.getTransaction(),
                getEstRequis(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estRequis"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2002 08:58:02)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getEstRequis() {
        return estRequis;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2002 08:58:28)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getEstTrouve() {
        return estTrouve;
    }

    public java.lang.String getIdCalculTaxe() {
        return idCalculTaxe;
    }

    /**
     * Getter
     */
    public java.lang.String getIdRubrique() {
        return idRubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2002 08:58:02)
     * 
     * @param newEstRequis
     *            java.lang.Boolean
     */
    public void setEstRequis(java.lang.Boolean newEstRequis) {
        estRequis = newEstRequis;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2002 08:58:28)
     * 
     * @param newEstTrouve
     *            java.lang.Boolean
     */
    public void setEstTrouve(java.lang.Boolean newEstTrouve) {
        estTrouve = newEstTrouve;
    }

    public void setIdCalculTaxe(java.lang.String newIdCalculTaxe) {
        idCalculTaxe = newIdCalculTaxe;
    }

    /**
     * Setter
     */
    public void setIdRubrique(java.lang.String newIdRubrique) {
        idRubrique = newIdRubrique;
    }
}
