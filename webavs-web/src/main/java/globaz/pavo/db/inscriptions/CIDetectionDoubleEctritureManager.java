package globaz.pavo.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CIDetectionDoubleEctritureManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private String montantMinimum = "4500";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = "";
        from += "( SELECT EC1.KAIIND FROM ";
        from += _getCollection() + "CIECRIP EC1 JOIN ";
        from += _getCollection() + "CIECRIP EC2 ON EC1.KAIIND = EC2.KAIIND";
        from += " WHERE EC1.KBNANN = " + forAnnee + " AND EC2.KBNANN = " + forAnnee + " AND ";
        from += " (EC1.KBTGEN = 310001 OR (EC1.KBTGEN = 310007 AND EC1.KBTSPE = 312003 ))	AND (EC2.KBTGEN = 310004 "
                + "OR (EC2.KBTGEN = 310007 AND EC2.KBTSPE in (0,312004) )) AND EC1.KBIECR <> EC2.KBIECR"
                + " GROUP BY EC1.KAIIND";
        from += " UNION ";
        from += " SELECT EC1.KAIIND FROM ";
        from += _getCollection() + "CIECRIP EC1 JOIN ";
        from += _getCollection() + "CIECRIP EC2 ON EC1.KAIIND = EC2.KAIIND";
        from += " WHERE EC1.KBNANN = " + forAnnee + " AND EC2.KBNANN = " + forAnnee + " AND ";
        from += " (EC1.KBTGEN = 310003 OR (EC1.KBTGEN = 310007 AND EC1.KBTSPE = 312002 ))	AND (EC2.KBTGEN = 310004 "
                + "OR (EC2.KBTGEN = 310007 AND EC2.KBTSPE in (0,312004) )) AND EC1.KBIECR <> EC2.KBIECR"
                + " GROUP BY EC1.KAIIND";
        from += ") T JOIN ";
        from += _getCollection() + "CIINDIP CI ON CI.KAIIND = T.KAIIND";

        return from;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new CIDetectionDoubleEcriture();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

}
