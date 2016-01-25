package globaz.tucana.db.bouclement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.itucana.constantes.ITUCSRubriqueListeDesRubriques;
import globaz.tucana.constantes.ITUCSConstantes;
import globaz.tucana.db.bouclement.access.ITUDetailDefTable;
import globaz.tucana.db.bouclement.access.TUDetailManager;

/**
 * Classe TUDetailListViewBean liste les d�tails de bouclement
 * 
 * @author fgo date de cr�ation : 11 mai 06
 * @version : version 1.0
 * 
 */

public class TUDetailInsertionACMListViewBean extends TUDetailManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Table : TUBPDET */

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.db.bouclement.access.TUDetailManager#_getWhere(globaz.globall .db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer(super._getWhere(statement));
        // traitement du positionnement
        if (sqlWhere.toString().length() != 0) {
            sqlWhere.append(" AND ");
        }
        sqlWhere.append(ITUDetailDefTable.CS_APPLICATION).append("=")
                .append(_dbWriteNumeric(statement.getTransaction(), ITUCSConstantes.CS_APPLICATION_ACM));
        // traitement du positionnement
        sqlWhere.append(" AND (");
        sqlWhere.append(ITUDetailDefTable.CS_RUBRIQUE)
                .append("=")
                .append(_dbWriteNumeric(statement.getTransaction(),
                        ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_CARTES_MATERNITE_CAPG));
        sqlWhere.append(" OR ")
                .append(ITUDetailDefTable.CS_RUBRIQUE)
                .append("=")
                .append(_dbWriteNumeric(statement.getTransaction(),
                        ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_CARTES_SERVICE_MILITAIRE_CAPG));
        sqlWhere.append(" OR ")
                .append(ITUDetailDefTable.CS_RUBRIQUE)
                .append("=")
                .append(_dbWriteNumeric(statement.getTransaction(),
                        ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_JOURS_COMPENSES_MATERNITE_CAPG));
        sqlWhere.append(" OR ")
                .append(ITUDetailDefTable.CS_RUBRIQUE)
                .append("=")
                .append(_dbWriteNumeric(statement.getTransaction(),
                        ITUCSRubriqueListeDesRubriques.CS_NOMBRE_DE_JOURS_COMPENSES_SERVICE_MILITAIRE_CAPG));
        sqlWhere.append(")");

        return sqlWhere.toString();
    }

    /**
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new TUDetailViewBean();
    }

    /**
     * R�cup�re la requ�te sql ex�cut�e
     * 
     * @return
     */
    @Override
    public String getCurrentSqlQuery() {
        return _getCurrentSqlQuery();
    }
}
