/**
 * 
 */
package globaz.naos.db.controleLpp;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.naos.util.AFUtil;

/**
 * @author est
 * 
 */
public class AFExtraitDSManager extends BManager {

    private static final long serialVersionUID = -6026946115048721302L;
    private int forAnnee;
    private String forIdAffilie;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFExtraitDS();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sqlFrom = new StringBuilder();

        sqlFrom.append(_getCollection() + "CIECRIP ecr ");

        sqlFrom.append("INNER JOIN " + _getCollection() + "AFAFFIP af on ecr.KBITIE = af.MAIAFF ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "CIINDIP ci on ecr.KAIIND = ci.KAIIND ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sqlWhere = new StringBuilder();

        // WHERE af.MAIAFF = [idAffilie]
        AFUtil.sqlAddCondition(sqlWhere, "af.MAIAFF = " + getForIdAffilie());

        // AND ecr.KBNANN = [annee]
        AFUtil.sqlAddCondition(sqlWhere, "ecr.KBNANN = " + getForAnnee());

        return sqlWhere.toString();
    }

    public int getForAnnee() {
        return forAnnee;
    }

    public String getForIdAffilie() {
        return forIdAffilie;
    }

    public void setForAnnee(int forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdAffilie(String forIdAffilie) {
        this.forIdAffilie = forIdAffilie;
    }
}
