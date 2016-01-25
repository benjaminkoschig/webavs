package globaz.naos.db.avisMutation;

import globaz.jade.client.util.JadeStringUtil;

public class AFAvisMutationManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAffiliationId = new String();
    private java.lang.String forAvisMutationId;
    private java.lang.String forTiersId;
    private java.lang.String fromDateAnnonce;
    private java.lang.String fromTiersId;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "AFAVISP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (!JadeStringUtil.isIntegerEmpty(forAvisMutationId)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MQIAVI=" + this._dbWriteNumeric(statement.getTransaction(), getForAvisMutationId());
        }
        if (!JadeStringUtil.isIntegerEmpty(fromDateAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MQDANN>=" + this._dbWriteNumeric(statement.getTransaction(), getFromDateAnnonce());
        } else {

            if (!JadeStringUtil.isIntegerEmpty(forTiersId)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForTiersId());
            }
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new AFAvisMutation();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:03:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:03:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForAvisMutationId() {
        return forAvisMutationId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:03:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForTiersId() {
        return forTiersId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:03:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDateAnnonce() {
        return fromDateAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:03:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromTiersId() {
        return fromTiersId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:03:21)
     * 
     * @param newForIdAssurance
     *            java.lang.String
     */
    public void setForAffiliationId(java.lang.String newForAffiliationId) {
        forAffiliationId = newForAffiliationId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:03:21)
     * 
     * @param newForIdAssurance
     *            java.lang.String
     */
    public void setForAvisMutation(java.lang.String newForAvisMutationId) {
        forTiersId = newForAvisMutationId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:03:21)
     * 
     * @param newForIdAssurance
     *            java.lang.String
     */
    public void setForTiersId(java.lang.String newForTiersId) {
        forTiersId = newForTiersId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:03:21)
     * 
     * @param newForIdAssurance
     *            java.lang.String
     */
    public void setFromDateAnnonce(java.lang.String newFromDateAnnonce) {
        fromDateAnnonce = newFromDateAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:03:21)
     * 
     * @param newForIdAssurance
     *            java.lang.String
     */
    public void setFromTiersId(java.lang.String newFromTiersId) {
        fromTiersId = newFromTiersId;
    }
}
