package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;

public class CPEcritureMemeTiersManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private String forIdTiers = "";
    private String forNotNumAffilie = "";

    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = _getCollection() + "CIECRIP AS CIECRIP INNER JOIN " + _getCollection()
                + "AFAFFIP AS AFAFFIP ON (CIECRIP.KBITIE=AFAFFIP.maiaff)";

        return sqlFrom;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        // TODO Auto-generated method stub
        return "MALNAF";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = " (KBTGEN IN (310003, 310004, 310009, 310002) OR KBTGEN =  310007 and KBTSPE IN (312002,312001,0,312004))";

        // traitement du positionnement
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KBNANN=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        // traitement du positionnement
        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }
        if (getForNotNumAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF<>" + this._dbWriteString(statement.getTransaction(), getForNotNumAffilie());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPEcritureMemeTiers();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForNotNumAffilie() {
        return forNotNumAffilie;
    }

    public Boolean hasEcritureCloturer(String idCI, String Annee, BTransaction transaction) throws Exception {
        int entier;
        BStatement statement = new BStatement(transaction);
        statement.createStatement();
        java.sql.ResultSet res = statement.executeQuery("SELECT COUNT(*) FROM " + _getCollection()
                + "CIRAOUP where kkdclo >=" + Annee + "1231 and kkdrev = 0 and kaiind =" + idCI);
        res.next();
        entier = JadeStringUtil.toInt(res.getObject(1).toString()) * -1;
        statement.closeStatement();
        if (entier == 0) {
            return new Boolean(false);
        } else {
            return new Boolean(true);
        }
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForNotNumAffilie(String forNotNumAffilie) {
        this.forNotNumAffilie = forNotNumAffilie;
    }
}
