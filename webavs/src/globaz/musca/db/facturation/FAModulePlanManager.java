package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class FAModulePlanManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdModuleFacturation = new String();
    private java.lang.String forIdPlanFacturation = new String();
    private java.lang.String fromIdModuleFacturation = new String();
    private java.lang.String fromIdPlanFacturation = new String();
    private java.lang.String fromLibelleDe = new String();
    private java.lang.String fromLibelleFr = new String();
    private java.lang.String fromLibelleIt = new String();
    protected java.lang.String order = "";

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAModulePlan.TABLE_FIELDS + ",FAMODUP.LIBELLEFR, FAMODUP.LIBELLEDE, FAMODUP.LIBELLEIT, NIVEAUAPPEL";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "FAMOPLP";
        String table2 = "FAMODUP";
        return _getCollection() + table1 + " AS " + table1 + " LEFT JOIN " + _getCollection() + table2 + " AS "
                + table2 + " ON (" + table1 + ".IDMODFAC=" + table2 + ".IDMODFAC)";

    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForIdModuleFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOPLP.IDMODFAC="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModuleFacturation());
        }

        if (getForIdPlanFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOPLP.IDPLANFACTURATION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdPlanFacturation());
        }

        if (getFromIdModuleFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOPLP.IDMODFAC>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdModuleFacturation());
        }

        if (getFromIdPlanFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOPLP.IDPLANFACTURATION>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdPlanFacturation());
        }

        if (getFromLibelleFr().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.LIBELLEFR>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleFr());
        }

        if (getFromLibelleDe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.LIBELLEDE>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleDe());
        }

        if (getFromLibelleIt().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMODUP.LIBELLEIT>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleIt());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAModulePlan();
    }

    /**
     * Getter
     */
    public java.lang.String getForIdModuleFacturation() {
        return forIdModuleFacturation;
    }

    public java.lang.String getForIdPlanFacturation() {
        return forIdPlanFacturation;
    }

    public java.lang.String getFromIdModuleFacturation() {
        return fromIdModuleFacturation;
    }

    public java.lang.String getFromIdPlanFacturation() {
        return fromIdPlanFacturation;
    }

    public java.lang.String getFromLibelleDe() {
        return fromLibelleDe;
    }

    public java.lang.String getFromLibelleFr() {
        return fromLibelleFr;
    }

    public java.lang.String getFromLibelleIt() {
        return fromLibelleIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByLibelleDe() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "LIBELLEDE";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByLibelleFr() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "LIBELLEFR";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            String
     */
    public void orderByLibelleIt() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "LIBELLEIT";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            String
     */
    public void orderByLibelleLangueSession() {
        if (getSession().getIdLangueISO().equalsIgnoreCase("FR")) {
            orderByLibelleFr();
        } else if (getSession().getIdLangueISO().equalsIgnoreCase("DE")) {
            orderByLibelleDe();
        } else if (getSession().getIdLangueISO().equalsIgnoreCase("IT")) {
            orderByLibelleIt();
        } else { // par défaut
            orderByLibelleFr();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            String
     */
    public void orderByNiveauAppel() {
        if (!JadeStringUtil.isEmpty(order)) {
            order += ", ";
        }
        order += "NIVEAUAPPEL";
    }

    /**
     * Setter
     */
    public void setForIdModuleFacturation(java.lang.String newForIdModuleFacturation) {
        forIdModuleFacturation = newForIdModuleFacturation;
    }

    public void setForIdPlanFacturation(java.lang.String newForIdPlanFacturation) {
        forIdPlanFacturation = newForIdPlanFacturation;
    }

    public void setFromIdModuleFacturation(java.lang.String newFromIdModuleFacturation) {
        fromIdModuleFacturation = newFromIdModuleFacturation;
    }

    public void setFromIdPlanFacturation(java.lang.String newFromIdPlanFacturation) {
        fromIdPlanFacturation = newFromIdPlanFacturation;
    }

    public void setFromLibelleDe(java.lang.String newFromLibelleDe) {
        fromLibelleDe = newFromLibelleDe;
    }

    public void setFromLibelleFr(java.lang.String newFromLibelleFr) {
        fromLibelleFr = newFromLibelleFr;
    }

    public void setFromLibelleIt(java.lang.String newFromLibelleIt) {
        fromLibelleIt = newFromLibelleIt;
    }
}
