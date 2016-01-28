package globaz.phenix.db.divers;

import globaz.jade.client.util.JadeStringUtil;

public class CPPeriodeFiscaleManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAnneeDecisionDebut = "";
    private java.lang.String forAnneeRevenuDebut = "";
    private java.lang.String forIdIfd = "";
    private java.lang.String forNumIfd = "";
    private java.lang.String fromNumIfd = "";
    private java.lang.String order = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPPEFIP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return getOrder();
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdIfd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICIIFD=" + _dbWriteNumeric(statement.getTransaction(), getForIdIfd());
        }

        // traitement du positionnement
        if (getForNumIfd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICNIFD=" + _dbWriteNumeric(statement.getTransaction(), getForNumIfd());
        }

        // traitement du positionnement
        if (getForAnneeRevenuDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICANRD=" + _dbWriteNumeric(statement.getTransaction(), getForAnneeRevenuDebut());
        }

        // traitement du positionnement
        if (getForAnneeDecisionDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICANDD=" + _dbWriteNumeric(statement.getTransaction(), getForAnneeDecisionDebut());
        }

        // traitement du positionnement
        if (getFromNumIfd().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ICNIFD>=" + _dbWriteNumeric(statement.getTransaction(), getFromNumIfd());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPPeriodeFiscale();
    }

    public java.lang.String getForAnneeDecisionDebut() {
        return forAnneeDecisionDebut;
    }

    public java.lang.String getForAnneeRevenuDebut() {
        return forAnneeRevenuDebut;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdIfd() {
        return forIdIfd;
    }

    public java.lang.String getForNumIfd() {
        return forNumIfd;
    }

    public java.lang.String getFromNumIfd() {
        return fromNumIfd;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 08:09:00)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOrder() {
        return order;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnneeDecisionDebut() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("ICANDD");
        } else {
            setOrder(getOrder() + ", ICANDD");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByAnneeRevenuDebut() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("ICANRD");
        } else {
            setOrder(getOrder() + ", ICANRD");
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIfd() {
        if (JadeStringUtil.isEmpty(getOrder())) {
            setOrder("ICNIFD");
        } else {
            setOrder(getOrder() + ", ICNIFD");
        }
    }

    public void setForAnneeDecisionDebut(java.lang.String newForAnneeDecisionDebut) {
        forAnneeDecisionDebut = newForAnneeDecisionDebut;
    }

    public void setForAnneeRevenuDebut(java.lang.String newForAnneeRevenuDebut) {
        forAnneeRevenuDebut = newForAnneeRevenuDebut;
    }

    /**
     * Setter
     */
    public void setForIdIfd(java.lang.String newForIdIfd) {
        forIdIfd = newForIdIfd;
    }

    public void setForNumIfd(java.lang.String newForNumIfd) {
        forNumIfd = newForNumIfd;
    }

    public void setFromNumIfd(java.lang.String newFromNumIfd) {
        fromNumIfd = newFromNumIfd;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.07.2003 08:09:00)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void setOrder(java.lang.String newOrder) {
        order = newOrder;
    }
}
