package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;

public class FAModuleImpressionManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdCritereDecompte = new String();
    private java.lang.String forIdModeRecouvrement = new String();
    private java.lang.String forIdModuleImpression = new String();
    private java.lang.String fromLibelleDe = new String();
    private java.lang.String fromLibelleFr = new String();
    private java.lang.String fromLibelleIt = new String();
    protected java.lang.String order = "IDMODULEIMPRESSION";

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAModuleImpression.TABLE_FIELDS;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "FAMOIMP AS FAMOIMP ";
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

        if (getForIdModuleImpression().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOIMP.IDMODULEIMPRESSION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModuleImpression());
        }

        if (getForIdCritereDecompte().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOIMP.IDCRITEREDECOMPTE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCritereDecompte());
        }

        if (getForIdModeRecouvrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOIMP.IDMODERECOUVREMENT="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModeRecouvrement());
        }

        if (getFromLibelleFr().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOIMP.LIBELLEFR>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleFr());
        }

        if (getFromLibelleDe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOIMP.LIBELLEDE>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleDe());
        }

        if (getFromLibelleIt().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAMOIMP.LIBELLEIT>=" + this._dbWriteString(statement.getTransaction(), getFromLibelleIt());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAModuleImpression();
    }

    public java.lang.String getForIdCritereDecompte() {
        return forIdCritereDecompte;
    }

    public java.lang.String getForIdModeRecouvrement() {
        return forIdModeRecouvrement;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdModuleImpression() {
        return forIdModuleImpression;
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
        order = "LIBELLEDE";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByLibelleFr() {
        order = "LIBELLEFR";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByLibelleIt() {
        order = "LIBELLEIT";
    }

    public void setForIdCritereDecompte(java.lang.String newForIdCritereDecompte) {
        forIdCritereDecompte = newForIdCritereDecompte;
    }

    public void setForIdModeRecouvrement(java.lang.String newForIdModeRecouvrement) {
        forIdModeRecouvrement = newForIdModeRecouvrement;
    }

    /**
     * Setter
     */
    public void setForIdModuleImpression(java.lang.String newForIdModuleImpression) {
        forIdModuleImpression = newForIdModuleImpression;
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
