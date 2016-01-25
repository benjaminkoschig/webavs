package globaz.cygnus.db.contributions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.Collection;
import java.util.Iterator;

/**
 * <p>
 * Ajouté au mandat InfoRom D0034
 * </p>
 * <p>
 * Permet une recherche des contributions d'assistance AI par ID Tiers
 * </p>
 * 
 * @author PBA
 */
public class RFContributionsJointTiersManager extends RFContributionsAssistanceAIManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTiers;
    private Collection<String> forIdTiersIn;

    public RFContributionsJointTiersManager() {
        super();

        forIdTiers = null;
        forIdTiersIn = null;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        String schema = _getCollection();
        String tableTiers = schema + ITITiersDefTable.TABLE_NAME;
        String tableContribution = schema + RFContributionsAssistanceAI.TABLE_CONTRIBUTION_ASSISTANCE_AI;

        StringBuilder orderBy = new StringBuilder();

        switch (getOrdreDeTri()) {
            case NomPrenomDateDebutCAAI:
                orderBy.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1_MAJ).append(" ASC,");
                orderBy.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2_MAJ).append(" ASC,");
                orderBy.append(tableContribution).append(".").append(RFContributionsAssistanceAI.DATE_DEBUT_PERIODE)
                        .append(" DESC");
                break;
            default:
                orderBy.append(super._getOrder(statement));
                break;
        }
        return orderBy.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        String schema = _getCollection();
        String tableTiers = schema + ITITiersDefTable.TABLE_NAME;

        StringBuilder sql = new StringBuilder();

        sql.append(super._getWhere(statement));

        if (!JadeStringUtil.isBlankOrZero(forIdTiers)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append("=").append(forIdTiers);
        }

        if ((forIdTiersIn != null) && (forIdTiersIn.size() > 0)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append(" IN(");
            for (Iterator<String> iterator = forIdTiersIn.iterator(); iterator.hasNext();) {
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
        return new RFContributionsJointTiers();
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public Collection<String> getForIdTiersIn() {
        return forIdTiersIn;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdTiersIn(Collection<String> forIdTiersIn) {
        this.forIdTiersIn = forIdTiersIn;
    }
}
