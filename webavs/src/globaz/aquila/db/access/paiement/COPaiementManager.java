/**
 *
 */
package globaz.aquila.db.access.paiement;

import globaz.aquila.api.helper.ICOEtapeHelper;
import globaz.aquila.common.COBManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

/**
 * @author sch
 */
public class COPaiementManager extends COBManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FOR_ALL_IDTYPEOPERATION = "all";
    public static final String FOR_SELECTION_ALL_ETAPES = "1000";
    public static final String FOR_SELECTION_JUSQU_A_RP = "1";
    public static final String FOR_SELECTION_RP_ET_SUIVANTES = "2";

    private String forIdTypeOperation = null;
    private String forSelectionEtapes = null;
    private String fromDate = null;
    private String untilDate = null;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " op " + COBManager.INNER_JOIN + _getCollection()
                + CASection.TABLE_CASECTP + " se " + COBManager.ON + " op." + CAOperation.FIELD_IDSECTION
                + COBManager.EGAL + " se." + CASection.FIELD_IDSECTION + COBManager.INNER_JOIN + _getCollection()
                + CACompteAnnexe.TABLE_CACPTAP + " ca " + COBManager.ON + " op." + CAOperation.FIELD_IDCOMPTEANNEXE
                + COBManager.EGAL + " ca." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // Tenir compte de la date de début et de fin
        if (!JadeStringUtil.isBlank(getFromDate()) && !JadeStringUtil.isBlank(getUntilDate())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += COBManager.AND;
            }
            sqlWhere += "op." + CAOperation.FIELD_DATE + COBManager.PLUS_GRAND_EGAL
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate());
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += COBManager.AND;
            }
            sqlWhere += "op." + CAOperation.FIELD_DATE + COBManager.PLUS_PETIT_EGAL
                    + this._dbWriteDateAMJ(statement.getTransaction(), getUntilDate());
        } else if (!JadeStringUtil.isBlank(getFromDate()) && JadeStringUtil.isBlank(getUntilDate())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += COBManager.AND;
            }
            sqlWhere += "op." + CAOperation.FIELD_DATE + COBManager.PLUS_GRAND_EGAL
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate());
        } else if (JadeStringUtil.isBlank(getFromDate()) && !JadeStringUtil.isBlank(getUntilDate())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += COBManager.AND;
            }
            sqlWhere += "op." + CAOperation.FIELD_DATE + COBManager.PLUS_PETIT_EGAL
                    + this._dbWriteDateAMJ(statement.getTransaction(), getUntilDate());
        }

        if (!JadeStringUtil.isBlank(getForIdTypeOperation())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += COBManager.AND;
            }
            if (getForIdTypeOperation().equalsIgnoreCase(COPaiementManager.FOR_ALL_IDTYPEOPERATION)) {
                sqlWhere += "((op." + CAOperation.FIELD_IDTYPEOPERATION + COBManager.IN + "('A', 'E')" + COBManager.AND
                        + "op." + CAOperation.FIELD_MONTANT + COBManager.SMALLER_DB_OPERAND + COBManager.ZERO;
                sqlWhere += ")" + COBManager.OR + "(";
                sqlWhere += "op." + CAOperation.FIELD_IDTYPEOPERATION + COBManager.IN
                        + "('AP', 'EP', 'EPB', 'EPR', 'EC')))";
            } else {
                sqlWhere += "op." + CAOperation.FIELD_IDTYPEOPERATION + COBManager.EGAL
                        + this._dbWriteString(statement.getTransaction(), getForIdTypeOperation());
                if (getForIdTypeOperation().equalsIgnoreCase("A") || getForIdTypeOperation().equalsIgnoreCase("E")) {
                    sqlWhere += COBManager.AND;
                    sqlWhere += " op." + CAOperation.FIELD_MONTANT + COBManager.SMALLER_DB_OPERAND + COBManager.ZERO;
                }
            }
        }
        if (!JadeStringUtil.isBlank(getForSelectionEtapes())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += COBManager.AND;
            }
            if (getForSelectionEtapes().equalsIgnoreCase(COPaiementManager.FOR_SELECTION_ALL_ETAPES)) {
                sqlWhere += "se." + CASection.FIELD_IDLASTETATAQUILA + COBManager.IS_NOT_NULL;
                if (!"A".equalsIgnoreCase(getForIdTypeOperation()) && !"AP".equalsIgnoreCase(getForIdTypeOperation())) {
                    sqlWhere += COBManager.AND + "se." + CASection.FIELD_IDLASTETATAQUILA + COBManager.DIFFERENT + "0";
                }
            } else if (getForSelectionEtapes().equalsIgnoreCase(COPaiementManager.FOR_SELECTION_JUSQU_A_RP)) {
                sqlWhere += "se." + CASection.FIELD_IDLASTETATAQUILA + COBManager.IS_NOT_NULL + COBManager.AND + "se."
                        + CASection.FIELD_IDLASTETATAQUILA + COBManager.IN + "("
                        + ICOEtapeHelper.ETAPE_CONTENTIEUX_JUSQUA_RP_SQL_FORMAT + ")";
            } else if (getForSelectionEtapes().equalsIgnoreCase(COPaiementManager.FOR_SELECTION_RP_ET_SUIVANTES)) {
                sqlWhere += "se." + CASection.FIELD_IDLASTETATAQUILA + COBManager.IS_NOT_NULL + COBManager.AND + "se."
                        + CASection.FIELD_IDLASTETATAQUILA + COBManager.NOT_IN + "("
                        + ICOEtapeHelper.ETAPE_POURSUITE_SQL_NOT_IN_FORMAT + ")";
            }
        }

        // Reprend que les états (codemaster = 1 et etat = 205002) et
        // (codemaster = 2 et etat = 205005)
        sqlWhere += COBManager.AND;
        sqlWhere += "((op." + CAOperation.FIELD_CODEMASTER + COBManager.EGAL + "1" + COBManager.AND + "op."
                + CAOperation.FIELD_ETAT + COBManager.EGAL + APIOperation.ETAT_COMPTABILISE + ")";
        sqlWhere += COBManager.OR;
        sqlWhere += "(op." + CAOperation.FIELD_CODEMASTER + COBManager.EGAL + "2" + COBManager.AND + "op."
                + CAOperation.FIELD_ETAT + COBManager.EGAL + APIOperation.ETAT_INACTIF + "))";

        sqlWhere += getOrdreBy();

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COPaiement();
    }

    public String getForIdTypeOperation() {
        return forIdTypeOperation;
    }

    public String getForSelectionEtapes() {
        return forSelectionEtapes;
    }

    public String getFromDate() {
        return fromDate;
    }

    protected String getOrdreBy() {
        String order = COBManager.ORDER_BY;
        order += "ca." + CACompteAnnexe.FIELD_IDEXTERNEROLE;
        return order;
    }

    public String getUntilDate() {
        return untilDate;
    }

    public void setForIdTypeOperation(String forIdTypeOperation) {
        this.forIdTypeOperation = forIdTypeOperation;
    }

    public void setForSelectionEtapes(String forSelectionEtapes) {
        this.forSelectionEtapes = forSelectionEtapes;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }
}
