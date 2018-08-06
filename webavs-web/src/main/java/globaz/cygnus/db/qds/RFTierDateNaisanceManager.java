package globaz.cygnus.db.qds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRStringUtils;

public class RFTierDateNaisanceManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String F_IDTIER = "HTITIE";

    // champs
    public static final String F_TIER_NAISSANCE = "HPDNAI";
    // tables
    public static final String T_PERSONNE = "TIPERSP";

    private transient String fromClause = null;

    private String forIdTiers;

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {

        String schema = _getCollection();

        StringBuffer fields = new StringBuffer();

        fields.append(" distinct ");

        fields.append(schema + RFTierDateNaisanceManager.T_PERSONNE + "." + RFTierDateNaisanceManager.F_TIER_NAISSANCE);

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFTierDateNaissance.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forIdTiers)) {
            sqlWhere.append(schema + T_PERSONNE + "." + F_IDTIER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(forIdTiers)));
        }

        return sqlWhere.toString();
    }

    // Méthodes
    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFTierDateNaissance();
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

}
