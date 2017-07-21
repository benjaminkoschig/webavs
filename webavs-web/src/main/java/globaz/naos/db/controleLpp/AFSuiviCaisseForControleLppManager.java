package globaz.naos.db.controleLpp;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class AFSuiviCaisseForControleLppManager extends BManager {

    private static final long serialVersionUID = 1L;

    private int forAnnee;

    public AFSuiviCaisseForControleLppManager() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        return "MAIAFF, MYDDEB, MYDFIN, MYTMOT";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFSUAFP";
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sqlWhere = new StringBuilder();

        sqlWhere.append("MYTGEN = 830003 ");
        sqlWhere.append("AND (( MYDFIN > ").append(forAnnee).append("0101 ");
        sqlWhere.append("AND MYDDEB < ").append(forAnnee).append("1231) ");
        sqlWhere.append("OR (MYDDEB < ").append(forAnnee).append("1231 AND MYDFIN = 0)) ");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFSuiviCaisseForControleLpp();
    }

    public int getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(int forAnnee) {
        this.forAnnee = forAnnee;
    }

}
