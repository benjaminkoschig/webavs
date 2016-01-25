package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;

public class FAPassageModuleManager extends FAPassageManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String forIdModuleFacturation = "";
    public String forIdTypeModule = "";
    public String inTypeModule = "";

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement)
                + ", FAMOPAP.IDMODFAC, FAMOPAP.ESTGENERE, FAMOPAP.IDACTION, FAMODUP.IDTYPEMODULE";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = super._getFrom(statement);
        String table1 = "FAMOPAP";
        String table2 = "FAMODUP";
        from = from + " INNER JOIN " + _getCollection() + table1 + " " + table1 + " ON (" + table1 + ".IDPASSAGE="
                + "FAPASSP.IDPASSAGE)";
        from = from + " INNER JOIN " + _getCollection() + table2 + " " + table2 + " ON (" + table2 + ".IDMODFAC="
                + table1 + ".IDMODFAC)";
        return from;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // ACU 19.06.2003 Optimization
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = super._getWhere(statement);
        // traitement du positionnement
        if (getForIdModuleFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOPAP.IDMODFAC="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModuleFacturation());
        }
        if (getForIdTypeModule().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.IDTYPEMODULE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeModule());
        }

        if (getInTypeModule().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.IDTYPEMODULE in (" + getInTypeModule() + ")";
            ;
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAPassageModule();
    }

    public String getForIdModuleFacturation() {
        return forIdModuleFacturation;
    }

    public String getForIdTypeModule() {
        return forIdTypeModule;
    }

    public String getInTypeModule() {
        return inTypeModule;
    }

    public void setForIdModuleFacturation(String idModuleFacturation) {
        forIdModuleFacturation = idModuleFacturation;
    }

    public void setForIdTypeModule(String forIdTypeModule) {
        this.forIdTypeModule = forIdTypeModule;
    }

    public void setInTypeModule(String inTypeModule) {
        this.inTypeModule = inTypeModule;
    }

}
