package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

public class REAnnonceInscriptionCIManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTiers;
    private boolean onlyCIEnAttenteDeCIAdd;

    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement);
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder whereClause = new StringBuilder();
        if (!JadeStringUtil.isEmpty(forIdTiers)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(REAnnonceInscriptionCI.FIELDNAME_ID_TIERS);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdTiers()));
        }
        if (onlyCIEnAttenteDeCIAdd) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(REAnnonceInscriptionCI.FIELDNAME_ATTENTE_CI_ADDITIONNEL);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(),
                    REAnnonceInscriptionCI.CS_ATTENTE_CI_ADDITIONNEL_PROVISOIRE));
        }
        return whereClause.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAnnonceInscriptionCI();
    }

    public final String getForIdTiers() {
        return forIdTiers;
    }

    public final boolean getOnlyCIEnAttenteDeCIAdd() {
        return onlyCIEnAttenteDeCIAdd;
    }

    public final void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public final void setOnlyCIEnAttenteDeCIAdd(boolean onlyCIEnAttenteDeCIAdd) {
        this.onlyCIEnAttenteDeCIAdd = onlyCIEnAttenteDeCIAdd;
    }

    @Override
    public String getOrderByDefaut() {
        return null;
    }

}
