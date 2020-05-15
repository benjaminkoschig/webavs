package globaz.apg.db.droits;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PBA
 */
public class APDroitLAPGJointTiersManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDroitContenuDansDateDebut;
    private String forDroitContenuDansDateFin;
    private List<String> forIdDroitNotIn;
    private String forIdTiers;
    private String likeNumeroAvs;
    private List<String> forEtatDroitNotIn;

    public APDroitLAPGJointTiersManager() {
        super();

        forDroitContenuDansDateDebut = "";
        forDroitContenuDansDateFin = "";
        forIdTiers = "";
        forIdDroitNotIn = new ArrayList<String>();
        likeNumeroAvs = "";

        wantCallMethodAfterFind(true);
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableDroitLAPG = _getCollection() + APDroitLAPG.TABLE_NAME_LAPG;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;

        sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        sql.append(" NOT IN (SELECT DISTINCT ").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT).append(" FROM ")
                .append(tableDroitLAPG).append(")");

        if (!JadeStringUtil.isBlank(forDroitContenuDansDateDebut)
                && !JadeStringUtil.isBlank(forDroitContenuDansDateFin)) {

            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append("(");

            sql.append("(");
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateDebut));
            sql.append(" AND ");
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
            sql.append("<=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateFin));

            sql.append(") OR (");

            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEFINDROIT);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateDebut));
            sql.append(" AND ");
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEFINDROIT);
            sql.append("<=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateFin));

            sql.append(") OR (");

            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
            sql.append("<=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateFin));
            sql.append(" AND ");
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEFINDROIT);
            sql.append("= '0'");

            sql.append(") OR (");

            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
            sql.append("<=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateDebut));
            sql.append(" AND ");
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_DATEFINDROIT);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDroitContenuDansDateFin));

            sql.append("))");
        }

        if (!JadeStringUtil.isBlank(forIdTiers)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
            sql.append("=");
            sql.append(forIdTiers);
        }

        if (!JadeStringUtil.isBlank(likeNumeroAvs)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(), likeNumeroAvs + "%"));
        }

        if ((forEtatDroitNotIn != null) && (forEtatDroitNotIn.size() > 0)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_ETAT);
            sql.append(" NOT IN (");
            for (int ctr = 0; ctr < forEtatDroitNotIn.size(); ctr++) {
                sql.append(forEtatDroitNotIn.get(ctr));
                if ((ctr + 1) < forEtatDroitNotIn.size()) {
                    sql.append(", ");
                }
            }
            sql.append(")");
        }

        if (!forIdDroitNotIn.isEmpty()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDroitLAPG).append(".").append(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
            sql.append(" NOT IN (");
            int i = 0;
            for (String s : forIdDroitNotIn) {
                sql.append(s);
                i++;
                if (i != forIdDroitNotIn.size()) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        return sql.toString();
    }

    @Override
    protected APDroitLAPGJointTiers _newEntity() throws Exception {
        return new APDroitLAPGJointTiers();
    }

    public String getForDroitContenuDansDateDebut() {
        return forDroitContenuDansDateDebut;
    }

    public String getForDroitContenuDansDateFin() {
        return forDroitContenuDansDateFin;
    }

    /**
     * @return the forIdDroitNotIn
     */
    public List<String> getForIdDroitNotIn() {
        return forIdDroitNotIn;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getLikeNumeroAvs() {
        return likeNumeroAvs;
    }

    @Override
    public String getOrderByDefaut() {
        return APDroitLAPG.FIELDNAME_IDDROIT_LAPG;
    }

    public void setForDroitContenuDansDateDebut(String forDroitContenuDansDateDebut) {
        this.forDroitContenuDansDateDebut = forDroitContenuDansDateDebut;
    }

    public void setForDroitContenuDansDateFin(String forDroitContenuDansDateFin) {
        this.forDroitContenuDansDateFin = forDroitContenuDansDateFin;
    }

    /**
     * @param forIdDroitNotIn
     *            the forIdDroitNotIn to set
     */
    public void setForIdDroitNotIn(List<String> forIdDroitNotIn) {
        this.forIdDroitNotIn = forIdDroitNotIn;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setLikeNumeroAvs(String likeNumeroAvs) {
        this.likeNumeroAvs = likeNumeroAvs;
    }

    public final List<String> getForEtatDroitNotIn() {
        return forEtatDroitNotIn;
    }

    public final void setForEtatDroitNotIn(List<String> forEtatDroitNotIn) {
        this.forEtatDroitNotIn = forEtatDroitNotIn;
    }

}
