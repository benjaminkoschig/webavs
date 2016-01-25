/**
 *
 */
package globaz.osiris.db.statofas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.db.contentieux.CAEvenementContentieux;

/**
 * @author SEL
 */
public class CAStatOfasPoursuiteOsirisCountManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String DATE_DEBUT_JANVIER = "0101";
    private static final String DATE_EXECUTION = "dateExecution";
    private static final String DATE_FIN_DECEMBRE = "1231";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String ON = " ON ";
    private static final String TABLE_CAETCTP = "CAETCTP";
    private static final String TABLE_CAPECTP = "CAPECTP";

    int forAnnee = 0;
    String forCsTypeEtape = "";

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer(_getCollection() + CAEvenementContentieux.TABLE_CAEVCTP);
        from.append(CAStatOfasPoursuiteOsirisCountManager.INNER_JOIN);
        from.append(_getCollection() + CAStatOfasPoursuiteOsirisCountManager.TABLE_CAPECTP);
        from.append(CAStatOfasPoursuiteOsirisCountManager.ON);
        from.append(_getCollection() + CAStatOfasPoursuiteOsirisCountManager.TABLE_CAPECTP);
        from.append(".IDPARAMETREETAPE");
        from.append("=");
        from.append(_getCollection() + CAEvenementContentieux.TABLE_CAEVCTP);
        from.append(".IDPARAMETREETAPE");
        from.append(CAStatOfasPoursuiteOsirisCountManager.INNER_JOIN);
        from.append(_getCollection() + CAStatOfasPoursuiteOsirisCountManager.TABLE_CAETCTP);
        from.append(CAStatOfasPoursuiteOsirisCountManager.ON);
        from.append(_getCollection() + CAStatOfasPoursuiteOsirisCountManager.TABLE_CAETCTP);
        from.append(".IDETAPE");
        from.append("=");
        from.append(_getCollection() + CAStatOfasPoursuiteOsirisCountManager.TABLE_CAPECTP);
        from.append(".IDETAPE");

        return from.toString();
    }

    /**
     * <pre>
     * where typeetape=216003 and dateExecution >= 20080000 and dateExecution < 20090000
     * </pre>
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append("TYPEETAPE");
        sqlWhere.append(" = ");
        sqlWhere.append(getForCsTypeEtape());
        sqlWhere.append(CAStatOfasPoursuiteOsirisCountManager.AND);
        sqlWhere.append(CAStatOfasPoursuiteOsirisCountManager.DATE_EXECUTION);
        sqlWhere.append(" >= ");
        sqlWhere.append(getForAnnee() + CAStatOfasPoursuiteOsirisCountManager.DATE_DEBUT_JANVIER);
        sqlWhere.append(CAStatOfasPoursuiteOsirisCountManager.AND);
        sqlWhere.append(CAStatOfasPoursuiteOsirisCountManager.DATE_EXECUTION);
        sqlWhere.append(" <= ");
        sqlWhere.append(getForAnnee() + CAStatOfasPoursuiteOsirisCountManager.DATE_FIN_DECEMBRE);

        return sqlWhere.toString();
    }

    /**
     * <pre>
     * 057 Nombre
     * 		select count(*) from ccjuweb.caevctp
     * 		INNER JOIN CCJUWEB.CAPECTP ON CCJUWEB.CAPECTP.IDPARAMETREETAPE=CCJUWEB.CAEVCTP.IDPARAMETREETAPE
     * 		INNER JOIN CCJUWEB.CAETCTP ON CCJUWEB.CAETCTP.IDETAPE=CCJUWEB.CAPECTP.IDETAPE
     * 		where typeetape=216003 and dateExecution >= 20080000 and dateExecution < 20090000
     * </pre>
     */

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        throw new Exception("NOT TO USE !!");
    }

    /**
     * @return the forAnnee
     */
    public int getForAnnee() {
        return forAnnee;
    }

    /**
     * @return the typeEtape
     */
    public String getForCsTypeEtape() {
        return forCsTypeEtape;
    }

    /**
     * @param forAnnee
     *            the forAnnee to set
     */
    public void setForAnnee(int forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @param typeEtape
     *            the typeEtape to set
     */
    public void setForCsTypeEtape(String typeEtape) {
        forCsTypeEtape = typeEtape;
    }

}
