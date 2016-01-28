package globaz.pavo.db.upi;

import globaz.commons.nss.db.NSSinfoManager;
import globaz.globall.db.BEntity;

public class CIUpiPreparedNssra extends NSSinfoManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSqlNssra() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT NSS, NOM, SEXE,DNAISS,NATION,SOURCE FROM ");
        sql.append(_getCollection());
        sql.append("NSSRA WHERE NSS = ? AND CDMUT = 0 AND VALID =1");
        return sql.toString();
    }

    /**
     * code sql prepared - 2 inactivés - 3 invalidés
     * 
     * @return le code sql pour interroger les inv. et inact.
     */
    public String getSqlNssraForInactivesOrInvalidate() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT(NAVS), NOM, SEXE,DNAISS,NATION,SOURCE FROM ");
        sql.append(_getCollection());
        sql.append("NSSRA WHERE CDMUT = ?");
        return sql.toString();
    }

    /**
     * 1 - NOM 2 - DATE NAISSANCE 3 - PAYS 4 - KATSEX 5 - SRC 6 - ID CI
     * 
     * @return sqlUpdate
     */
    public String getSqlUpdateCI() {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append(_getCollection());
        sql.append("CIINDIP SET KALNOM = ?,KADNAI=?,KAIPAY=?,KATSEX=?,KANSRC=? WHERE KAIIND = ?");
        return sql.toString();

    }

}
