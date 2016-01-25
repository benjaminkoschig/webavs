/*
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.batch.manager;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

/**
 * @author spa Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CAMiseAJourEcritureFacturationExterneManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final String FROM = " FROM ";
    protected static final String GROUP_BY = " GROUP BY ";
    protected static final String ORDER_BY = " ORDER BY ";
    protected static final String SELECT = "SELECT ";
    protected static final String WHERE = " WHERE ";

    /**
	 *
	 */
    public CAMiseAJourEcritureFacturationExterneManager() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        return "FA.FANAFF, FA.FATROL, FA.FANPER, " + "SE." + CASection.FIELD_IDSECTION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CASection.TABLE_CASECTP + " SE INNER JOIN " + _getCollection()
                + CACompteAnnexe.TABLE_CACPTAP + " CA ON " + " SE." + CASection.FIELD_IDCOMPTEANNEXE + " = " + " CA."
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " INNER JOIN (SELECT FANAFF, FATROL, FANPER FROM "
                + _getCollection() + "FAFAEXT";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer(CAMiseAJourEcritureFacturationExterneManager.SELECT);
            String sqlFields = _getFields(statement);
            if ((sqlFields != null) && (sqlFields.trim().length() != 0)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }
            sqlBuffer.append(CAMiseAJourEcritureFacturationExterneManager.FROM);
            sqlBuffer.append(_getFrom(statement));
            String sqlWhere = _getWhere(statement);
            if ((sqlWhere != null) && (sqlWhere.trim().length() != 0)) {
                sqlBuffer.append(CAMiseAJourEcritureFacturationExterneManager.WHERE);
                sqlBuffer.append(sqlWhere);
            }
            String sqlOrder = _getOrder(statement);
            if ((sqlOrder != null) && (sqlOrder.trim().length() != 0)) {
                sqlBuffer.append(CAMiseAJourEcritureFacturationExterneManager.ORDER_BY);
                sqlBuffer.append(sqlOrder);
            }
            return sqlBuffer.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // Montant < 0
        sqlWhere += "FANPAS <> 0 AND FALTFA = 'E' AND FAMCPA = 0";

        sqlWhere += getGroupBy(statement);

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAMiseAJourEcritureFacturationExterne();
    }

    private String getGroupBy(BStatement statement) {
        return CAMiseAJourEcritureFacturationExterneManager.GROUP_BY + "FANAFF, FATROL, FANPER) FA ON SE."
                + CASection.FIELD_IDEXTERNE + " = FA.FANPER AND CA." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                + " = FA.FANAFF AND CA." + CACompteAnnexe.FIELD_IDROLE + " = FA.FATROL WHERE SE."
                + CASection.FIELD_SOLDE + " = 0";
    }

}
