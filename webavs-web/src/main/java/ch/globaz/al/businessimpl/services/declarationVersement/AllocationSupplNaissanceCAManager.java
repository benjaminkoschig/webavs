package ch.globaz.al.businessimpl.services.declarationVersement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.util.List;

public class AllocationSupplNaissanceCAManager extends BManager {
    private static final long serialVersionUID = -595568500979753629L;

    private List<String> inIdExterne;
    private String forIdExterneRole = "";
    private String dateValeurFrom = "";
    private String dateValeurTo = "";

    public List<String> getInIdExterne() {
        return inIdExterne;
    }

    public void setInIdExterne(List<String> compteANS) {
        inIdExterne = compteANS;
    }

    public String getForIdExterneRole() {
        return forIdExterneRole;
    }

    public void setForIdExterneRole(String forIdExterneRole) {
        this.forIdExterneRole = forIdExterneRole;
    }

    public String getDateValeurFrom() {
        return dateValeurFrom;
    }

    public void setDateValeurFrom(String dateValeurFrom) {
        this.dateValeurFrom = dateValeurFrom;
    }

    public String getDateValeurTo() {
        return dateValeurTo;
    }

    public void setDateValeurTo(String dateValeurTo) {
        this.dateValeurTo = dateValeurTo;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CAOPERP op JOIN " + _getCollection()
                + "CARUBRP  rub on op.IDCOMPTE = rub.IDRUBRIQUE join " + _getCollection()
                + "CAJOURP jour on op.IDJOURNAL = jour.IDJOURNAL join " + _getCollection()
                + "CACPTAP ca on ca.IDCOMPTEANNEXE = op.IDCOMPTEANNEXE join " + _getCollection()
                + "CASECTP sec on sec.IDSECTION = op.IDSECTION";
    }

    private String getIdClause() {
        String s_inValues = "";
        int cpt = 0;
        for (String inValue : getInIdExterne()) {
            cpt++;
            s_inValues += "'" + inValue + "'";

            if (cpt < getInIdExterne().size()) {
                s_inValues += ",";
            }

        }
        return s_inValues;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer("");
        // traitement du positionnement
        if (getInIdExterne().size() > 0) {
            addCondition(sqlWhere, "rub.IDEXTERNE IN (" + getIdClause() + ")");
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getForIdExterneRole())) {
            addCondition(sqlWhere,
                    "ca.IDEXTERNEROLE = " + this._dbWriteString(statement.getTransaction(), getForIdExterneRole()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getDateValeurFrom())) {
            addCondition(sqlWhere,
                    "jour.DATEVALEURCG >=" + this._dbWriteDateAMJ(statement.getTransaction(), getDateValeurFrom()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getDateValeurTo())) {
            addCondition(sqlWhere,
                    "jour.DATEVALEURCG <=" + this._dbWriteDateAMJ(statement.getTransaction(), getDateValeurTo()));
        }

        return sqlWhere.toString();
    }

    @Override
    protected String _getFields(BStatement statement) {
        return "SUM(op.MONTANT) AS Montant, sec.IDEXTERNE AS IDEXTERNE, jour.DATE";
    }

    @Override
    protected String _getGroupBy(BStatement statement) {
        return "sec.IDEXTERNE, jour.DATE";
    }

    @Override
    protected String _getSql(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer("SELECT ");
            String sqlFields = _getFields(statement);
            if ((sqlFields != null) && (sqlFields.trim().length() != 0)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }
            sqlBuffer.append(" FROM ");
            sqlBuffer.append(_getFrom(statement));
            //
            String sqlWhere = _getWhere(statement);
            if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
                sqlBuffer.append(" WHERE ");
                sqlBuffer.append(sqlWhere);
            }
            String sqlOrder = _getOrder(statement);
            if ((sqlOrder != null) && (sqlOrder.trim().length() != 0)) {
                sqlBuffer.append(" ORDER BY ");
                sqlBuffer.append(sqlOrder);
            }

            String sqlGroup = _getGroupBy(statement);
            if ((sqlGroup != null) && (sqlGroup.trim().length() != 0)) {
                sqlBuffer.append(" GROUP BY ");
                sqlBuffer.append(sqlGroup);
            }
            return sqlBuffer.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "ERROR IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }

    /**
     * Permet l'ajout d'une condition dans la clause WHERE. <br>
     * 
     * @param sqlWhere
     * @param condition
     *            à ajouter au where
     */
    protected void addCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
        sqlWhere.append(condition);
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AllocationSupplNaissanceCA();
    }

}
