package globaz.aquila.db.access.poursuite;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.api.ICOSequenceConstante;
import globaz.aquila.common.COBManager;
import globaz.aquila.db.access.batch.COTransition;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CASection;

public class CODossierEtapeExacteManager extends COContentieuxManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtape = "";
    private String csSequence = "";

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer(super._getWhere(statement));

        if (getCsEtape().length() != 0) {
            // ctx.ODIETA IN (SELECT WEBAVS.COTRANP.ODIETA FROM WEBAVS.COTRANP
            // WHERE WEBAVS.COTRANP.OGIESU=40)
            if (sqlWhere.length() != 0) {
                sqlWhere.append(COBManager.AND);
            }
            sqlWhere.append(_getCollection() + CASection.TABLE_CASECTP + "." + CASection.FIELD_SOLDE + ">"
                    + COBManager.ZERO);
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(_getTableName() + "." + ICOContentieuxConstante.FNAME_ID_ETAPE);
            sqlWhere.append(" IN (");
            sqlWhere.append(COBManager.SELECT + _getCollection() + ICOEtapeConstante.TABLE_NAME + "."
                    + COTransition.FNAME_ID_ETAPE);
            sqlWhere.append(COBManager.FROM + _getCollection() + ICOEtapeConstante.TABLE_NAME);
            sqlWhere.append(COBManager.WHERE + _getCollection() + ICOEtapeConstante.TABLE_NAME + "."
                    + ICOEtapeConstante.FNAME_LIBETAPE + " = " + getCsEtape());
            sqlWhere.append(COBManager.AND);
            sqlWhere.append(_getTableName() + "." + ICOEtapeConstante.FNAME_ID_SEQUENCE);
            // SELECT ODIETA FROM WEBAVS.COETAPP WHERE ODTETA = 5300031 AND
            // OFISEQ = 1
            sqlWhere.append(COBManager.EGAL + getIdSequence());
            sqlWhere.append(")");
        }

        return sqlWhere.toString();
    }

    /**
     * @return the csEtape
     */
    public String getCsEtape() {
        return csEtape;
    }

    /**
     * @return the csSequence
     */
    public String getCsSequence() {
        return csSequence;
    }

    /**
     * Retourne l'idSequence pour une sequence donnée en CS.<br>
     * Exemple :
     * 
     * <pre>
     * SELECT OFISEQ FROM WEBAVS.COSEQP WHERE OFTSEQ = 1
     * </pre>
     * 
     * @param csSequence
     * @return la requete renvoyant l'idSequence
     */
    private String getIdSequence() {
        StringBuffer sql = new StringBuffer("(");
        sql.append(COBManager.SELECT + ICOSequenceConstante.FNAME_ID_SEQUENCE);
        sql.append(COBManager.FROM + _getCollection() + ICOSequenceConstante.TABLE_NAME);
        sql.append(COBManager.WHERE + ICOSequenceConstante.FNAME_LIB_SEQUENCE + COBManager.EGAL + getCsSequence());
        sql.append(")");
        return sql.toString();
    }

    /**
     * @param idEtape
     *            the idEtape to set
     */
    public void setCsEtape(String idEtape) {
        csEtape = idEtape;
    }

    /**
     * @param csSequence
     *            the csSequence to set
     */
    public void setCsSequence(String csSequence) {
        this.csSequence = csSequence;
    }
}
