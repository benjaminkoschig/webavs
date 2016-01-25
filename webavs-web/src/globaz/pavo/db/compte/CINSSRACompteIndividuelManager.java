package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author: mmo
 */
public class CINSSRACompteIndividuelManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer("");

        sqlFields.append(" CI.KAIIND AS " + CINSSRACompteIndividuel.COLUMN_NAME_ID_CI + ",");
        sqlFields.append(" CI.KANAVS AS " + CINSSRACompteIndividuel.COLUMN_NAME_NUMERO_AVS + ",");
        sqlFields.append(" CI.KALNOM AS " + CINSSRACompteIndividuel.COLUMN_NAME_NOM_PRENOM + ",");
        sqlFields.append(" NS.CDMUT AS " + CINSSRACompteIndividuel.COLUMN_NAME_CODE_MUTATION + ",");
        sqlFields.append(" CI.KABINA AS " + CINSSRACompteIndividuel.COLUMN_NAME_CI_INACTIF + ",");
        sqlFields.append(" CI.KABINV AS " + CINSSRACompteIndividuel.COLUMN_NAME_CI_INVALIDE + ",");
        sqlFields.append(" CI.KABOUV AS " + CINSSRACompteIndividuel.COLUMN_NAME_CI_OUVERT);

        return sqlFields.toString();

    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer("");

        from.append(_getCollection() + "NSSRA AS NS ");
        from.append(" INNER JOIN ");
        from.append(_getCollection() + "CIINDIP AS CI ");
        from.append(" ON (CAST(NS.NAVS AS CHAR(13)) = CI.KANAVS ) ");

        return from.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer("");

        sqlWhere.append(" NS.CDMUT in (2,3) ");
        sqlWhere.append(" AND ");
        sqlWhere.append(" CI.KAIREG = 309001 ");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CINSSRACompteIndividuel();
    }

}
