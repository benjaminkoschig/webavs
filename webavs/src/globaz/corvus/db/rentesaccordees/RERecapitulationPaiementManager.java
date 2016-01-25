package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.tools.PRDateFormater;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BSC
 */
public class RERecapitulationPaiementManager extends PRAbstractManager implements
        BIGenericManager<REPrestationsAccordeesListeRecap> {

    private static final long serialVersionUID = 1L;

    private String forCsEtatIn;
    private String forMoisFinRANotEmptyAndHigherOrEgal;

    public RERecapitulationPaiementManager() {
        super();

        forCsEtatIn = "";
        forMoisFinRANotEmptyAndHigherOrEgal = "";
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;

        if (!JadeStringUtil.isEmpty(forMoisFinRANotEmptyAndHigherOrEgal)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append("((");

            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append(">=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(),
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forMoisFinRANotEmptyAndHigherOrEgal)));

            sql.append(") OR (");

            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append("=0");

            sql.append(") OR (");

            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sql.append(" IS NULL");

            sql.append("))");

            sql.append(" AND (");

            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
            sql.append("<=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(),
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(forMoisFinRANotEmptyAndHigherOrEgal)));

            sql.append(")");
        }

        if (!JadeStringUtil.isEmpty(forCsEtatIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
            sql.append(" IN (").append(forCsEtatIn).append(")");
        }

        return sql.toString();
    }

    @Override
    protected REPrestationsAccordeesListeRecap _newEntity() throws Exception {
        return new REPrestationsAccordeesListeRecap();
    }

    public String getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForMoisFinRANotEmptyAndHigherOrEgal() {
        return forMoisFinRANotEmptyAndHigherOrEgal;
    }

    @Override
    public String getOrderByDefaut() {
        return REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE;
    }

    public void setForCsEtatIn(String forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForMoisFinRANotEmptyAndHigherOrEgal(String forMoisFinRANotEmptyAndHigherOrEgal) {
        this.forMoisFinRANotEmptyAndHigherOrEgal = forMoisFinRANotEmptyAndHigherOrEgal;
    }

    @Override
    public List<REPrestationsAccordeesListeRecap> getContainerAsList() {
        List<REPrestationsAccordeesListeRecap> list = new ArrayList<REPrestationsAccordeesListeRecap>();

        for (int i = 0; i < size(); i++) {
            list.add((REPrestationsAccordeesListeRecap) get(i));
        }

        return list;
    }
}
