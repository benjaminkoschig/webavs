/**
 *
 */
package globaz.osiris.db.statofas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;

/**
 * @author SEL
 */
public class CAStatOfasSursisPmtManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String DATE_ZERO = "0000";
    private static final String IN = " IN ";
    private static final String INNER_JOIN = " inner join ";
    private static final String ON = " on ";

    int forAnnee = 0;
    boolean forSum = false;

    /**
     * Sursis au paiement
     * 
     * <pre>
     * 056 Montant (CHF)
     * 		select sum(montant) from SCHEMA.caechpp
     * 		inner join SCHEMA.caplarp on SCHEMA.caplarp.IDPLANRECOUVREMENT=SCHEMA.caechpp.IDPLANRECOUVREMENT
     * 		where idetat in (226001, 226003) and SCHEMA.caplarp.date>=20080000 and SCHEMA.caplarp.date<=20090000
     * 		and idmoderecouvrement=223001
     * 
     * 055 Nombre
     * 		select count (*) from SCHEMA.caplarp
     * 		where idetat in (226001, 226003) and SCHEMA.caplarp.date>=20080000 and SCHEMA.caplarp.date<20090000
     * 		and idmoderecouvrement=223001
     * </pre>
     */

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer(_getCollection() + CAPlanRecouvrement.TABLE_CAPLARP);

        if (isForSum()) {
            from.append(CAStatOfasSursisPmtManager.INNER_JOIN);
            from.append(_getCollection() + CAEcheancePlan.TABLE_NAME);
            from.append(CAStatOfasSursisPmtManager.ON);
            from.append(_getCollection() + CAPlanRecouvrement.TABLE_CAPLARP);
            from.append("." + CAPlanRecouvrement.FIELD_IDPLANRECOUVREMENT);
            from.append(" = ");
            from.append(_getCollection() + CAEcheancePlan.TABLE_NAME);
            from.append("." + CAEcheancePlan.FIELD_IDPLANRECOUVREMENT);
        }

        return from.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(CAPlanRecouvrement.FIELD_IDETAT);
        sqlWhere.append(CAStatOfasSursisPmtManager.IN);
        sqlWhere.append("(" + CAPlanRecouvrement.CS_ACTIF + ", " + CAPlanRecouvrement.CS_SOLDE + ")");
        sqlWhere.append(CAStatOfasSursisPmtManager.AND);
        sqlWhere.append(_getCollection() + CAPlanRecouvrement.TABLE_CAPLARP);
        sqlWhere.append("." + CAPlanRecouvrement.FIELD_DATE);
        sqlWhere.append(">=");
        sqlWhere.append(getForAnnee() + CAStatOfasSursisPmtManager.DATE_ZERO);
        sqlWhere.append(CAStatOfasSursisPmtManager.AND);
        sqlWhere.append(_getCollection() + CAPlanRecouvrement.TABLE_CAPLARP);
        sqlWhere.append("." + CAPlanRecouvrement.FIELD_DATE);
        sqlWhere.append("<");
        sqlWhere.append((getForAnnee() + 1) + CAStatOfasSursisPmtManager.DATE_ZERO);
        sqlWhere.append(CAStatOfasSursisPmtManager.AND);
        sqlWhere.append(_getCollection() + CAPlanRecouvrement.TABLE_CAPLARP);
        sqlWhere.append("." + CAPlanRecouvrement.FIELD_IDMODERECOUVREMENT);
        sqlWhere.append("=");
        sqlWhere.append(CAPlanRecouvrement.CS_BVR);

        return sqlWhere.toString();
    }

    /**
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
     * @return the forSum
     */
    public boolean isForSum() {
        return forSum;
    }

    /**
     * @param forAnnee
     *            the forAnnee to set
     */
    public void setForAnnee(int forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @param forSum
     *            the forSum to set
     */
    public void setForSum(boolean forSum) {
        this.forSum = forSum;
    }

}
