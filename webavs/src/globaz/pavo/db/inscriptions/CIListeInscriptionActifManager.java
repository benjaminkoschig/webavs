package globaz.pavo.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CIListeInscriptionActifManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forAnnee = "";

    private transient String fromClause = null;
    private boolean isRevNull = false;
    private String untilAnnee = "";

    @Override
    protected String _getFields(BStatement statement) {

        // String sqlFields = super._getFields(statement);
        String sqlFields = "";

        sqlFields = " KANAVS" + ",KALNOM" + ",KADNAI" + ",KATSEX" + ",KAIPAY" + ",KBNANN" + ",KBMMON";

        // TODO Auto-generated method stub
        return sqlFields;
    }

    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            CIListeInscriptionActif liste = new CIListeInscriptionActif();
            StringBuffer from = new StringBuffer(liste.createFromClause(_getCollection()));
            fromClause = from.toString();
        }
        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer order = new StringBuffer();
        order.append(_getCollection() + "CIINDIP.KANAVS ASC ");
        return order.toString();
    }

    @Override
    protected String _getSql(BStatement statement) {
        return super._getSql(statement);
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // -- composant de la requete initialises avec les options par defaut
        // String sqlWhere = super._getWhere(statement);
        String sqlWhere = "";

        // AJOUT
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KBNANN >=" + getForAnnee();
        }

        if (getUntilAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KBNANN <=" + getUntilAnnee();
        }
        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += "(KKTARC = 81 OR KKTARC = 71) AND KKTENR IN(318000, 318001) ";
        sqlWhere += " AND KBNANN > (KKDCLO/10000) ";
        sqlWhere += " AND KKDREV = 0 ";
        sqlWhere += "AND ( (KBNANN - CAST((KADNAI/10000)AS INT)) <60)";

        return sqlWhere;

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIListeInscriptionActif();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getUntilAnnee() {
        return untilAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setUntilAnnee(String untilAnnee) {
        this.untilAnnee = untilAnnee;
    }

}
