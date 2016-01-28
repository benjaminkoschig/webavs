package globaz.pavo.process;

import globaz.globall.db.BStatement;
import globaz.jade.log.JadeLogger;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIEcritureFastManagerPrep extends CIEcritureFastManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for CIEcritureFastManagerPrep.
     */
    public CIEcritureFastManagerPrep() {
        super();
    }

    @Override
    public java.lang.String _getSql(BStatement statement) {
        try {
            StringBuffer sql = new StringBuffer("SELECT KBIECR,KBITIE FROM ");
            sql.append(_getCollection());
            sql.append("CIECRIP WHERE ");
            sql.append(_getCollection());
            sql.append("CIECRIP.KAIIND=? AND ");
            sql.append(_getCollection());
            sql.append("CIECRIP.KBTCPT IN (303001,303004,303007) ORDER BY ");
            sql.append("KBNANN DESC, ");
            sql.append("KBNMOF DESC");

            return sql.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }
}
