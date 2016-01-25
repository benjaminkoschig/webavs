package globaz.pavo.process;

import globaz.globall.db.BStatement;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CICompteIndividuelManager;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CICompteIndividuelPreparedManager extends CICompteIndividuelManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for CICompteIndividuelPreparedManager.
     */
    public CICompteIndividuelPreparedManager() {
        super();
    }

    @Override
    public java.lang.String _getSql(BStatement statement) {
        try {
            StringBuffer sql = new StringBuffer("UPDATE ");
            sql.append(_getCollection());
            sql.append("CIINDIP SET KAIEMP=? ");
            sql.append("WHERE KAIIND = ?");
            return sql.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }

    /**
     * @param statement
     * @return
     */
    public String getSqlForCopy(BStatement statement) {
        try {
            StringBuffer sql = new StringBuffer("INSERT INTO ");
            sql.append(_getCollection());
            sql.append("CIIABGP (KSIIND, KSIINR, KSNAVS, KSLNOM, KSIPAY, KSDNAI, KSTSEX, KSNOUV, KSLREF, KSTSEC, KSBOUV, KSIARC, KSICAI, KSBFUS, KSIEMP, KSNAVA, KSNAVP, KSIREG, KSDCRE, KSLUSC, PSPY, KSDCLO, KSOARC, KSCAIT)");
            sql.append(" SELECT KAIIND, KAIINR, KANAVS,KALNOM, KAIPAY, KADNAI, KATSEX, KANOUV, KALREF, KATSEC, KABOUV, KAIARC, KAICAI, KABFUS, KAIEMP, KANAVA, KANAVP, KAIREG, KADCRE, KALUSC, PSPY, KADCLO, KAOARC, KACAIT FROM ");
            sql.append(_getCollection());
            sql.append("CIINDIP WHERE KAIREG = ? AND KABOUV = ? ");
            return sql.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }

    }
}
