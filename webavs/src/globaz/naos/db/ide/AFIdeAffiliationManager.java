package globaz.naos.db.ide;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.naos.db.affiliation.AFAffiliation;
import java.io.Serializable;

/**
 * L'IdeAffiliation Manager a pour but de fournir au process SyncRegistre (synchronisation des info du registre IDE) la
 * liste des affiliation pour laquelle un numéro IDE est renseigné et le statut ide est inexistant
 * 
 * @author cel
 * 
 */
public class AFIdeAffiliationManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2359708126575161032L;

    private boolean modeForceAllStatus;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliation();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + AFAffiliation.TABLE_NAME);

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(AFAffiliation.FIELDNAME_NUMERO_IDE + " <> ''");
        sqlWhere.append(" AND ");
        sqlWhere.append(AFAffiliation.FIELDNAME_NUMERO_IDE + " IS NOT NULL");
        if (!modeForceAllStatus) {
            sqlWhere.append(" AND ( ");
            sqlWhere.append(AFAffiliation.FIELDNAME_STATUT_IDE + " = 0");
            sqlWhere.append(" OR ");
            sqlWhere.append(AFAffiliation.FIELDNAME_STATUT_IDE + " IS NULL");
            sqlWhere.append(" )");
        }

        return sqlWhere.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return AFAffiliation.FIELDNAME_STATUT_IDE + " ASC";
    }

    public void setModeForceAllStatus(boolean modeForceAllStatus) {
        this.modeForceAllStatus = modeForceAllStatus;
    }
}
