package globaz.cygnus.db.paiement;

import globaz.cygnus.api.ordresversements.IRFOrdresVersements;
import globaz.cygnus.db.ordresversements.RFOrdresVersements;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

public class RFPrestationJointOrdreVersementJointAssDecOvManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTiersAyantDroit = "";
    private boolean forIsRestitution = Boolean.FALSE;
    private boolean forNumRestitutionNull = Boolean.FALSE;
    private transient String fromClause = null;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    RFPrestationJointOrdreVersementJointAssDecOv.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        // Clause recherchant par idTiers
        if (!JadeStringUtil.isBlankOrZero(forIdTiersAyantDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestation.FIELDNAME_ID_TIERS_BENEFICIAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(forIdTiersAyantDroit);
        }

        // Clause recherchant par Ordre de versement de type restitution
        if (forIsRestitution) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFOrdresVersements.FIELDNAME_TYPE_VERSEMENT);
            sqlWhere.append(" = ");
            sqlWhere.append(IRFOrdresVersements.CS_TYPE_RESTITUTION);
        } else {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFOrdresVersements.FIELDNAME_TYPE_VERSEMENT);
            sqlWhere.append(" != ");
            sqlWhere.append(IRFOrdresVersements.CS_TYPE_RESTITUTION);
        }

        // Clause recherchant les numéros de restitution à NULL
        if (forNumRestitutionNull) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("(");
            sqlWhere.append(RFAssDecOv.FIELDNAME_NO_RESTITUTION);
            sqlWhere.append(" = ");
            sqlWhere.append("0");
            sqlWhere.append(" OR ");
            sqlWhere.append(RFAssDecOv.FIELDNAME_NO_RESTITUTION);
            sqlWhere.append(" IS NULL");
            sqlWhere.append(")");
        }

        return sqlWhere.toString();

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new RFPrestationJointOrdreVersementJointAssDecOv();
    }

    public String getForIdTiersAyantDroit() {
        return forIdTiersAyantDroit;
    }

    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isForIsRestitution() {
        return forIsRestitution;
    }

    public boolean isForNumRestitutionNull() {
        return forNumRestitutionNull;
    }

    public void setForIdTiersAyantDroit(String forIdTiersAyantDroit) {
        this.forIdTiersAyantDroit = forIdTiersAyantDroit;
    }

    public void setForIsRestitution(boolean forIsRestitution) {
        this.forIsRestitution = forIsRestitution;
    }

    public void setForNumRestitutionNull(boolean forNumRestitutionNull) {
        this.forNumRestitutionNull = forNumRestitutionNull;
    }

}
