/**
 *
 */
package globaz.aquila.db.access.poursuite;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.common.COBManager;
import globaz.aquila.db.access.batch.COTransition;
import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import java.util.List;

/**
 * @author SEL
 * 
 */
public class COEffetcuerTransitionsManager extends COContentieuxManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> forIdsEtapeshistorique;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.db.access.poursuite.COContentieuxManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer("");

        sqlFields.append(" DISTINCT ");
        sqlFields.append(super._getFields(statement));

        return sqlFields.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.db.access.poursuite.COContentieuxManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer(super._getFrom(statement));

        // inner join db2cott.cohistp h1 on h1.OAICON= c.OAICON and h1.oebann='2'
        from.append(COBManager.INNER_JOIN).append(_getCollection()).append(ICOHistoriqueConstante.TABLE_NAME)
                .append(" h1");
        from.append(COBManager.ON).append("h1.").append(ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX);
        from.append("=").append(super._getTableName() + "." + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
        from.append(COBManager.AND).append("h1.").append(ICOHistoriqueConstante.FNAME_EST_ANNULE).append("=")
                .append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);

        // inner join db2cott.cohistp h2 on h2.OAICON= c.OAICON and h2.oebann='2'
        from.append(COBManager.INNER_JOIN).append(_getCollection()).append(ICOHistoriqueConstante.TABLE_NAME)
                .append(" h2");
        from.append(COBManager.ON).append("h2.").append(ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX);
        from.append("=").append(super._getTableName() + "." + ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);
        from.append(COBManager.AND).append("h2.").append(ICOHistoriqueConstante.FNAME_EST_ANNULE).append("=")
                .append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);

        return from.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.aquila.db.access.poursuite.COContentieuxManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer(super._getWhere(statement));

        // Etapes suivantes
        if (getForIdsEtapeshistorique() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(COBManager.AND);
            }

            /*
             * <code> SELECT ODIETA FROM WEBAVSCIAM.COTRANP WHERE OGBAUT='1' AND OGIESU IN (1,43,31) ORDER BY ODIETA
             * </code>
             */
            sqlWhere.append("h1.").append(ICOContentieuxConstante.FNAME_ID_ETAPE).append(" IN (");
            sqlWhere.append(COBManager.SELECT);
            sqlWhere.append(COTransition.FNAME_ID_ETAPE);
            sqlWhere.append(COBManager.FROM);
            sqlWhere.append(_getCollection() + COTransition.TABLE_NAME);
            sqlWhere.append(COBManager.WHERE);
            sqlWhere.append(COTransition.FNAME_AUTO + COBManager.EGAL + BConstants.DB_BOOLEAN_TRUE_DELIMITED);
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(COTransition.FNAME_ID_ETAPE_SUIVANTE);
            sqlWhere.append(COBManager.IN + "(");

            for (int i = 0; i < getForIdsEtapeshistorique().size(); i++) {
                sqlWhere.append(getForIdsEtapeshistorique().get(i));
                if (i != getForIdsEtapeshistorique().size() - 1) {
                    sqlWhere.append(", ");
                }
            }
            sqlWhere.append(")");
            sqlWhere.append(")");
        }

        for (int i = 0; i < getForIdsEtapeshistorique().size(); i++) {
            sqlWhere.append(COBManager.AND);
            sqlWhere.append("h2.").append(ICOHistoriqueConstante.FNAME_ID_ETAPE).append("<>")
                    .append(getForIdsEtapeshistorique().get(i));
        }

        return sqlWhere.toString();
    }

    /**
     * @return the forIdsEtapeshistorique
     */
    public List<String> getForIdsEtapeshistorique() {
        return forIdsEtapeshistorique;
    }

    /**
     * @param forIdsEtapeshistorique
     *            the forIdsEtapeshistorique to set
     */
    public void setForIdsEtapeshistorique(List<String> forIdsEtapeshistorique) {
        this.forIdsEtapeshistorique = forIdsEtapeshistorique;
    }
}
