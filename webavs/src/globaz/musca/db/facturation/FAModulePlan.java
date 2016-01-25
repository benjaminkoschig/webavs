package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;

public class FAModulePlan extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String TABLE_FIELDS = "FAMOPLP.IDMODFAC, FAMOPLP.IDPLANFACTURATION, FAMOPLP.PSPY";
    private java.lang.String idModuleFacturation = new String();
    private java.lang.String idPlanFacturation = new String();
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();
    private java.lang.String libelleIt = new String();
    private java.lang.String tableName = "FAMOPLP  AS FAMOPLP";

    /**
     * Commentaire relatif au constructeur FAModulePlan
     */
    public FAModulePlan() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        tableName = "FAMOPLP";
        // Test si module déjà présent dans le plan
        FAModulePlan modPlan = new FAModulePlan();
        modPlan.setSession(getSession());
        modPlan.setIdModuleFacturation(getIdModuleFacturation());
        modPlan.setIdPlanFacturation(getIdPlanFacturation());
        modPlan.retrieve();
        if (modPlan.isNew() == false) {
            _addError(transaction, getSession().getLabel("EXISTE_MODULE_PLAN"));
        }
    }

    /*
     * Traitement avant suppression
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAModulePlan.TABLE_FIELDS + ",FAMODUP.LIBELLEFR, FAMODUP.LIBELLEDE, FAMODUP.LIBELLEIT";
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String table1 = "FAMOPLP";
        String table2 = "FAMODUP";
        return _getCollection() + table1 + " AS " + table1 + " LEFT JOIN " + _getCollection() + table2 + " AS "
                + table2 + " ON (" + table1 + ".IDMODFAC=" + table2 + ".IDMODFAC)";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return tableName;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idModuleFacturation = statement.dbReadNumeric("IDMODFAC");
        idPlanFacturation = statement.dbReadNumeric("IDPLANFACTURATION");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("FAMOPLP.IDMODFAC",
                this._dbWriteNumeric(statement.getTransaction(), getIdModuleFacturation(), ""));
        statement.writeKey("FAMOPLP.IDPLANFACTURATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanFacturation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDMODFAC",
                this._dbWriteNumeric(statement.getTransaction(), getIdModuleFacturation(), "idModuleFacturation"));
        statement.writeField("IDPLANFACTURATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanFacturation(), "idPlanFacturation"));
    }

    /**
     * Getter
     */
    public java.lang.String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public java.lang.String getIdPlanFacturation() {
        return idPlanFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.06.2003 06:52:48)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelle() {
        String langue = new String();
        langue = getSession().getIdLangueISO();
        if (langue.equalsIgnoreCase("fr")) {
            return libelleFr;
        }
        if (langue.equalsIgnoreCase("de")) {
            return libelleDe;
        } else {
            return libelleIt;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.06.2003 06:52:48)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.06.2003 06:52:48)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.06.2003 06:52:48)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Setter
     */
    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation) {
        idModuleFacturation = newIdModuleFacturation;
    }

    public void setIdPlanFacturation(java.lang.String newIdPlanFacturation) {
        idPlanFacturation = newIdPlanFacturation;
    }
}
