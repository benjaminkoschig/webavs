package globaz.corvus.db.ordresversements;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtre de recherche, utilisant l'ancien framework Jade, pour rechercher des soldes pour restitution dans les rentes
 */
public class RESoldePourRestitutionManager extends PRAbstractManager implements
        BIGenericManager<RESoldePourRestitution> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdFactureARestituer;
    private String forIdOV;
    private String forIdPrestation;
    private String forIdRetenue;
    private boolean sansRestitutionPourDettEnCompta;

    public RESoldePourRestitutionManager() {
        super();

        forIdFactureARestituer = "";
        forIdOV = "";
        forIdPrestation = "";
        forIdRetenue = "";
        sansRestitutionPourDettEnCompta = false;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isIntegerEmpty(forIdPrestation)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append(RESoldePourRestitution.FIELDNAME_ID_PRESTATION).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdPrestation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdOV)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append(RESoldePourRestitution.FIELDNAME_ID_ORDRE_VERSEMENT).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOV()));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdFactureARestituer)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append(RESoldePourRestitution.FIELDNAME_ID_FACTURE_A_RESTITUER).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdFactureARestituer()));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdRetenue)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append(RESoldePourRestitution.FIELDNAME_ID_RETENUE).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdRetenue()));
        }

        if (sansRestitutionPourDettEnCompta) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append("(").append(RESoldePourRestitution.FIELDNAME_ID_FACTURE_A_RESTITUER).append("=0 OR ")
                    .append(RESoldePourRestitution.FIELDNAME_ID_FACTURE_A_RESTITUER).append(" IS NULL)");
        }

        return sql.toString();
    }

    @Override
    protected RESoldePourRestitution _newEntity() throws Exception {
        return new RESoldePourRestitution();
    }

    @Override
    public List<RESoldePourRestitution> getContainerAsList() {
        List<RESoldePourRestitution> list = new ArrayList<RESoldePourRestitution>();

        for (int i = 0; i < size(); i++) {
            list.add((RESoldePourRestitution) get(i));
        }

        return list;
    }

    public String getForIdFactureARestituer() {
        return forIdFactureARestituer;
    }

    public String getForIdOV() {
        return forIdOV;
    }

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public String getForIdRetenue() {
        return forIdRetenue;
    }

    @Override
    public String getOrderByDefaut() {
        return RESoldePourRestitution.FIELDNAME_ID_SOLDE_RESTIT;
    }

    public boolean isSansRestitutionPourDettEnCompta() {
        return sansRestitutionPourDettEnCompta;
    }

    public void setForIdFactureARestituer(String forIdFactureARestituer) {
        this.forIdFactureARestituer = forIdFactureARestituer;
    }

    public void setForIdOV(String forIdOV) {
        this.forIdOV = forIdOV;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

    public void setForIdRetenue(String forIdRetenue) {
        this.forIdRetenue = forIdRetenue;
    }

    public void setSansRestitutionPourDettEnCompta(boolean sansRestitutionPourDettEnCompta) {
        this.sansRestitutionPourDettEnCompta = sansRestitutionPourDettEnCompta;
    }
}
