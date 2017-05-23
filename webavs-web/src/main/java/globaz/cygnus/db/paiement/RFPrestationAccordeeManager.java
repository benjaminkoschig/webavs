/*
 * Créé le 10 décembre 2010
 */
package globaz.cygnus.db.paiement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import java.util.Set;

/**
 * <H1>Description</H1>
 * 
 * @author fha
 */
public class RFPrestationAccordeeManager extends PRAbstractManager {

    private static final long serialVersionUID = 1L;
    private String forCsTypesPrestationsAccordees = "";
    private Boolean forDateFinDroitInitialeNotNull = Boolean.FALSE;
    private String forIdDecision = "";
    private String forIdGestionnairePreparerDecision = "";
    private Boolean forIdGestionnairePreparerDecisionNotNull = Boolean.FALSE;
    private String forIdRFMAccordee = "";
    private Set<String> forNotIdsRfPrestationAccordee = null;
    private String forOrderBy = "";

    public RFPrestationAccordeeManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuilder sqlOrder = new StringBuilder();
        if (!JadeStringUtil.isEmpty(forOrderBy)) {
            sqlOrder.append(forOrderBy);
        }
        return sqlOrder.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sqlWhere = new StringBuilder();

        if (!JadeStringUtil.isEmpty(forIdRFMAccordee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdRFMAccordee));
        }

        if (!JadeStringUtil.isEmpty(forIdDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if (!JadeStringUtil.isEmpty(forCsTypesPrestationsAccordees)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypesPrestationsAccordees));
        }

        if (!JadeStringUtil.isEmpty(forIdGestionnairePreparerDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationAccordee.FIELDNAME_ID_GESTIONNAIRE_PREPARER_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGestionnairePreparerDecision));
        }

        if (forDateFinDroitInitialeNotNull) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_FIN_DROIT_INITIALE);
            sqlWhere.append(" <> ");
            sqlWhere.append("0");
            sqlWhere.append(" AND ");
            sqlWhere.append(RFPrestationAccordee.FIELDNAME_DATE_FIN_DROIT_INITIALE);
            sqlWhere.append(" IS NOT NULL ");
        }

        if (forIdGestionnairePreparerDecisionNotNull) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationAccordee.FIELDNAME_ID_GESTIONNAIRE_PREPARER_DECISION);
            sqlWhere.append(" IS NOT NULL ");
        }

        if ((null != forNotIdsRfPrestationAccordee) && (forNotIdsRfPrestationAccordee.size() > 0)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationAccordee.FIELDNAME_ID_RFM_ACCORDEE);
            sqlWhere.append(" NOT IN (");
            int j = 0;
            for (String id : forNotIdsRfPrestationAccordee) {
                if (!JadeStringUtil.isEmpty(id)) {
                    j++;
                    if (forNotIdsRfPrestationAccordee.size() != j) {
                        sqlWhere.append(id + ",");
                    } else {
                        sqlWhere.append(id + ") ");
                    }
                }
            }
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFPrestationAccordee();
    }

    public String getForCsTypesPrestationsAccordees() {
        return forCsTypesPrestationsAccordees;
    }

    public Boolean getForDateFinDroitInitialeNotNull() {
        return forDateFinDroitInitialeNotNull;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdGestionnairePreparerDecision() {
        return forIdGestionnairePreparerDecision;
    }

    public Boolean getForIdGestionnairePreparerDecisionNotNull() {
        return forIdGestionnairePreparerDecisionNotNull;
    }

    public String getForIdRFMAccordee() {
        return forIdRFMAccordee;
    }

    public Set<String> getForNotIdsRfPrestationAccordee() {
        return forNotIdsRfPrestationAccordee;
    }

    @Override
    public String getOrderByDefaut() {
        return null;
    }

    public void setForCsTypesPrestationsAccordees(String forCsTypesPrestationsAccordees) {
        this.forCsTypesPrestationsAccordees = forCsTypesPrestationsAccordees;
    }

    public void setForDateFinDroitInitialeNotNull(Boolean forDateFinDroitInitialeNotNull) {
        this.forDateFinDroitInitialeNotNull = forDateFinDroitInitialeNotNull;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdGestionnairePreparerDecision(String forIdGestionnairePreparerDecision) {
        this.forIdGestionnairePreparerDecision = forIdGestionnairePreparerDecision;
    }

    public void setForIdGestionnairePreparerDecisionNotNull(Boolean forIdGestionnairePreparerDecisionNotNull) {
        this.forIdGestionnairePreparerDecisionNotNull = forIdGestionnairePreparerDecisionNotNull;
    }

    public void setForIdRFMAccordee(String forIdRFMAccordee) {
        this.forIdRFMAccordee = forIdRFMAccordee;
    }

    public void setForNotIdsRfPrestationAccordee(Set<String> forNotIdsRfPrestationAccordee) {
        this.forNotIdsRfPrestationAccordee = forNotIdsRfPrestationAccordee;
    }

}
