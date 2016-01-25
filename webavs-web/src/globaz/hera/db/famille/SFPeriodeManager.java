package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author JPA
 */
public class SFPeriodeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** order by par clé primaire WHIPER */
    public static final String ORDER_DEFAULT = "WHIPER";
    /** order by par type et date début (WHTTYP, WHDDEB ASC, WHIPER) */
    public static final String ORDER_TYPE_DATE = "WHTTYP, WHDDEB ASC, WHIPER";

    private Collection<String> forCsTypePeriodeIn;
    private String forIdMembreFamille;
    private String forOrderBy;
    private String forType;

    public SFPeriodeManager() {
        super();

        forIdMembreFamille = "";
        forOrderBy = "";
        forType = "";
        forCsTypePeriodeIn = null;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        if (JadeStringUtil.isEmpty(getForOrderBy())) {
            return getForOrderBy();
        } else {
            return SFPeriodeManager.ORDER_DEFAULT;
        }
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePeriodes = _getCollection() + SFPeriode.TABLE_NAME;

        if (!JadeStringUtil.isEmpty(forIdMembreFamille)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tablePeriodes).append(".").append(SFPeriode.FIELD_IDMEMBREFAMILLE).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), forIdMembreFamille));
        }

        if (!JadeStringUtil.isEmpty(forType)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tablePeriodes).append(".").append(SFPeriode.FIELD_TYPE).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), forType));
        }

        if ((forCsTypePeriodeIn != null) && (forCsTypePeriodeIn.size() > 0)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tablePeriodes).append(".").append(SFPeriode.FIELD_TYPE).append(" IN(");

            for (Iterator<String> iterator = forCsTypePeriodeIn.iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }

            sql.append(")");
        }
        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new SFPeriode();
    }

    public Collection<String> getForCsTypePeriodeIn() {
        return forCsTypePeriodeIn;
    }

    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getForType() {
        return forType;
    }

    public void setForCsTypePeriodeIn(Collection<String> forCsTypePeriodeIn) {
        this.forCsTypePeriodeIn = forCsTypePeriodeIn;
    }

    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setForType(String forType) {
        this.forType = forType;
    }
}
