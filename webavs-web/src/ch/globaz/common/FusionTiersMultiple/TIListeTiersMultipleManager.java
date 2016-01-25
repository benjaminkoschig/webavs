package ch.globaz.common.FusionTiersMultiple;

import globaz.globall.db.BStatement;

public class TIListeTiersMultipleManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateNaissance = null;
    private String forNomTiers = null;

    private String forPrenomTiers = null;

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "ti.htitie,ti.htldu1,ti.htldu2,per.hpdnai,hxnavs,aff.malnaf,aff.maddeb,aff.madfin,count(pr.waitie) as PREST";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return " (select htldu1,htldu2,pers.hpdnai from " + _getCollection() + "titierp as ti " + "inner join "
                + _getCollection() + "tipersp as pers on (ti.htitie=pers.htitie) "
                + "where ti.HTINAC = '2' and ti.HTPPHY = '1' "
                + "group by htldu1,htldu2,pers.HPDNAI having count(*) > 1) as temp" + " INNER JOIN " + _getCollection()
                + "titierp as ti on (ti.htldu1 = temp.htldu1 and ti.htldu2 = temp.htldu2)" + " INNER JOIN "
                + _getCollection() + "tipersp as per on (ti.htitie=per.htitie and per.hpdnai=temp.hpdnai)"
                + " INNER JOIN " + _getCollection() + "tipavsp as pav on (ti.htitie=pav.htitie)" + " LEFT JOIN "
                + _getCollection() + "afaffip as aff on (ti.htitie=aff.htitie)" + " LEFT JOIN " + _getCollection()
                + "alalloc as al on (al.htitie=ti.htitie)" + " LEFT JOIN " + _getCollection()
                + "aldos as do on (al.BID = do.BID)" + " LEFT JOIN " + _getCollection()
                + "prdemap as pr on (pr.waitie=ti.htitie and pr.wattde=52201004) ";
    }

    /**
     * @see globaz.globall.db.BManager#_getGroupBy(BStatement)
     */
    @Override
    protected String _getGroupBy(BStatement statement) {
        String result = " GROUP BY ";
        return result + "ti.htitie,ti.htldu1,ti.htldu2,per.hpdnai,hxnavs,aff.malnaf,aff.maddeb,aff.madfin";

    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        // Tri par défaut
        return "ti.htldu1, ti.htldu2, per.hpdnai, hxnavs desc, aff.malnaf desc, aff.maddeb desc";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForNomTiers() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " ti.htldu1 = " + this._dbWriteString(statement.getTransaction(), getForNomTiers());
        }

        if (getForPrenomTiers() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " ti.htldu2 = " + this._dbWriteString(statement.getTransaction(), getForPrenomTiers());
        }

        if (getForDateNaissance() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " per.hpdnai = " + this._dbWriteNumeric(statement.getTransaction(), getForDateNaissance());
        }
        return sqlWhere + " " + _getGroupBy(statement);
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new TIListeTiersMultiple();
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForNomTiers() {
        return forNomTiers;
    }

    public String getForPrenomTiers() {
        return forPrenomTiers;
    }

    public void setForDateNaissance(String dateNaissance) {
        forDateNaissance = dateNaissance;
    }

    public void setForNomTiers(String nomTiers) {
        forNomTiers = nomTiers;
    }

    public void setForPrenomTiers(String prenomTiers) {
        forPrenomTiers = prenomTiers;
    }

}
