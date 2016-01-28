package globaz.pavo.db.comparaison;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CISqlCorrectionAnomalie extends BEntity {

    private static final long serialVersionUID = -3958603642936182198L;

    public CISqlCorrectionAnomalie() {
        super();
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * Méthode qui retourne la chaine SQL pour la mise à jour des CIs
     * 
     * @param typeAnomalie
     * @return
     */
    public String getSql(String typeAnomalie) {
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE ");
        sql.append(_getCollection());
        sql.append("CIINDIP SET ");
        if (CIAnomalieCI.CS_NOM.equals(typeAnomalie)) {
            sql.append(" KALNOM = ?");
        } else if (CIAnomalieCI.CS_ANNEE_OUVERTURE.equals(typeAnomalie)) {
            sql.append(" KANOUV = ? ");
        } else if (CIAnomalieCI.CS_CI_PRESENT_CLOTURE.equals(typeAnomalie)) {
            sql.append(" KABOUV = ? ");
        } else if (CIAnomalieCI.CS_NATIONNALITE.equals(typeAnomalie)) {
            sql.append(" KAIPAY = ? ");
        } else if (CIAnomalieCI.CS_NUMERO_AVS_ANCIEN.equals(typeAnomalie)) {
            sql.append(" KANAVP = ? ");
        } else if (CIAnomalieCI.CS_MOTIF_OUVERTURE.equals(typeAnomalie)) {
            sql.append(" KAOARC = ? ");
        } else {
            // ne devrait pas
            return "";
        }

        sql.append(" WHERE KAIIND = ?");
        return sql.toString();

    }

    public String getSqlForRaou() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append(_getCollection());
        sql.append("CIRAOUP WHERE KAIIND=? ORDER BY KKDCLO DESC");
        return sql.toString();

    }

    public String getSqlTraite() {
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE ");
        sql.append(_getCollection());
        sql.append("CIANOMP SET KTIETA=? ");
        sql.append(" WHERE KTID=?");
        return sql.toString();
    }
}
