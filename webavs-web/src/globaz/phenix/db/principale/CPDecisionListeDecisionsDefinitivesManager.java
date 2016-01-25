package globaz.phenix.db.principale;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;

/**
*
*/
public class CPDecisionListeDecisionsDefinitivesManager extends globaz.globall.db.BManager implements
        java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateFin = "";
    private String fields = _getCollection() + "AFAFFIP.MAIAFF, " + _getCollection() + "AFAFFIP.MALNAF, "
            + _getCollection() + "AFAFFIP.MADDEB, " + _getCollection() + "AFAFFIP.MADFIN, " + _getCollection()
            + "AFAFFIP.MATMOT, " + _getCollection() + "AFAFFIP.MATTAF, " + _getCollection() + "AFAFFIP.HTITIE";
    private String fromAnneeDecision = "";
    private String orderBy = "MALNAF";
    private String toAnneeDecision = "";

    /**
     * @see globaz.globall.db.BManager#_getFields(BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return fields;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = "";
        if (!JadeStringUtil.isIntegerEmpty(fromAnneeDecision) && !JadeStringUtil.isIntegerEmpty(toAnneeDecision)) {
            dateFin = "01.01." + fromAnneeDecision;
            int difference = Integer.parseInt(toAnneeDecision) - Integer.parseInt(fromAnneeDecision) + 1;
            String tableAffiliation = _getCollection() + "AFAFFIP";
            String table1 = "(SELECT a.MAIAFF, b.MADDEB, b.MADFIN FROM " + _getCollection() + "CPDECIP a INNER JOIN "
                    + _getCollection() + "AFAFFIP b ON a.MAIAFF=b.MAIAFF WHERE a.IAANNE BETWEEN "
                    + _dbWriteNumeric(statement.getTransaction(), getFromAnneeDecision()) + " AND "
                    + _dbWriteNumeric(statement.getTransaction(), getToAnneeDecision())
                    + " GROUP BY a.IAANNE, a.MAIAFF, b.MADFIN, b.MADDEB) AS T1";
            String table2 = "(SELECT MAIAFF FROM " + table1 + " GROUP BY MAIAFF, MADDEB, MADFIN HAVING COUNT(*) >="
                    + difference + ") AS T2";
            from = tableAffiliation + " LEFT OUTER JOIN " + table2 + " ON " + tableAffiliation + ".MAIAFF= T2.MAIAFF";
        } else {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0023"));
        }

        return from;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return orderBy;
    }

    /**
     * retourne la clause WHERE de la requete SQL version 1
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isIntegerEmpty(fromAnneeDecision) && !JadeStringUtil.isIntegerEmpty(toAnneeDecision)) {
            String toDateDebut = "31.12." + toAnneeDecision;
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MADFIN >= " + _dbWriteDateAMJ(statement.getTransaction(), dateFin)
                    + " OR MADFIN = 0) AND MADFIN <> MADDEB AND MADDEB<="
                    + _dbWriteDateAMJ(statement.getTransaction(), toDateDebut) + " AND (MATTAF = "
                    + CodeSystem.TYPE_AFFILI_INDEP + " OR MATTAF = " + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY
                    + " OR MATTAF = " + CodeSystem.TYPE_AFFILI_NON_ACTIF + " OR MATTAF = "
                    + CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE + " OR MATTAF = " + CodeSystem.TYPE_AFFILI_TSE
                    + ") AND T2.MAIAFF IS NULL";
        } else {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0023"));
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new AFAffiliation();
    }

    /**
     * Returns the fields.
     * 
     * @return String
     */
    public String getFields() {
        return fields;
    }

    /**
     * Returns the fromAnneeDecision.
     * 
     * @return String
     */
    public String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    /**
     * Returns the orderBy.
     * 
     * @return String
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Returns the toAnneeDecision.
     * 
     * @return String
     */
    public String getToAnneeDecision() {
        return toAnneeDecision;
    }

    /**
     * Sets the fields.
     * 
     * @param fields
     *            The fields to set
     */
    public void setFields(String fields) {
        this.fields = fields;
    }

    /**
     * Sets the fromAnneeDecision.
     * 
     * @param fromAnneeDecision
     *            The fromAnneeDecision to set
     */
    public void setFromAnneeDecision(String fromAnneeDecision) {
        this.fromAnneeDecision = fromAnneeDecision;
    }

    /**
     * Sets the orderBy.
     * 
     * @param orderBy
     *            The orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * Sets the toAnneeDecision.
     * 
     * @param toAnneeDecision
     *            The toAnneeDecision to set
     */
    public void setToAnneeDecision(String toAnneeDecision) {
        this.toAnneeDecision = toAnneeDecision;
    }

}
