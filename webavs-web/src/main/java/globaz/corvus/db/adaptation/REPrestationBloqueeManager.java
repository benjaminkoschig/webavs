package globaz.corvus.db.adaptation;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.tools.PRDateFormater;

/**
 * <p>
 * Manager dédié à la recherche des prestations bloquées pour la liste de vérification du paiement mensuel.
 * </p>
 * <p>
 * Remplace le manager précédant ({@link REPrestAccJointInfoComptaJointTiersManager}) pour des raisons d'adaptations aux
 * PC et RFM. Le manager précédant faisait une jointure sur les tables REREACC et REBACAL qui ne sont pas utilisées par
 * les PC. Aucune PC/RFM n'était donc présente sur la liste.
 * </p>
 * 
 * @author PBA
 */
public class REPrestationBloqueeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forPrestatonEnCoursDansMois;
    private boolean seulementPrestationBloquee;

    public REPrestationBloqueeManager() {
        super();

        forPrestatonEnCoursDansMois = "";
        seulementPrestationBloquee = false;
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;

        StringBuilder sql = new StringBuilder();

        if (isSeulementPrestationBloquee()) {
            sql.append(tablePrestationAccordee).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE);
            sql.append("=");
            sql.append(this._dbWriteBoolean(statement.getTransaction(), true, BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        if (JadeDateUtil.isGlobazDateMonthYear(forPrestatonEnCoursDansMois)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append("(");

            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append(" IS NULL");

            sql.append(" OR ");

            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append("=0");

            sql.append(" OR ");

            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append(">");
            sql.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forPrestatonEnCoursDansMois));

            sql.append(")");
        }

        return sql.toString();
    }

    @Override
    protected REPrestationBloquee _newEntity() throws Exception {
        return new REPrestationBloquee();
    }

    public String getForPrestatonEnCoursDansMois() {
        return forPrestatonEnCoursDansMois;
    }

    public boolean isSeulementPrestationBloquee() {
        return seulementPrestationBloquee;
    }

    public void setForPrestatonEnCoursDansMois(String forPrestatonEnCoursDansMois) {
        this.forPrestatonEnCoursDansMois = forPrestatonEnCoursDansMois;
    }

    public void setSeulementPrestationBloquee(boolean seulementPrestationBloquee) {
        this.seulementPrestationBloquee = seulementPrestationBloquee;
    }
}
