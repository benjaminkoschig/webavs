/**
 *
 */
package globaz.osiris.db.statofas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.contentieux.CAEvenementContentieux;

/**
 * @author SEL
 */
public class CAStatOfasPoursuiteOsirisSumManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String DATE_DEBUT_JANVIER = "0101";
    private static final String DATE_EXECUTION = "dateExecution";
    private static final String DATE_FIN_DECEMBRE = "1231";
    private static final String FROM = " FROM ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String LIKE = " LIKE ";
    private static final String ON = " ON ";
    private static final String SELECT = "SELECT ";
    private static final String TABLE_CAETCTP = "CAETCTP";
    private static final String TABLE_CAPECTP = "CAPECTP";
    private static final String WHERE = " WHERE ";

    int forAnnee = 0;
    String forCsTypeEtape = "";

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer();

        from.append("(");
        from.append(getSqlFrom());
        from.append(") a");

        from.append(CAStatOfasPoursuiteOsirisSumManager.INNER_JOIN);
        from.append(_getCollection() + CAOperation.TABLE_CAOPERP + " op");
        from.append(CAStatOfasPoursuiteOsirisSumManager.ON);
        from.append("a.idSection");
        from.append("=");
        from.append("op." + CAOperation.FIELD_IDSECTION);
        from.append(CAStatOfasPoursuiteOsirisSumManager.AND);
        from.append("op." + CAOperation.FIELD_DATE);
        from.append("<=");
        from.append("a.dateExecution");
        from.append(CAStatOfasPoursuiteOsirisSumManager.AND);
        from.append(CAOperation.FIELD_ETAT);
        from.append("=");
        from.append(APIOperation.ETAT_COMPTABILISE);
        from.append(CAStatOfasPoursuiteOsirisSumManager.AND);
        from.append(CAOperation.FIELD_IDTYPEOPERATION);
        from.append(CAStatOfasPoursuiteOsirisSumManager.LIKE);
        from.append("'E%'");

        return from.toString();
    }

    /**
     * <pre>
     * SELECT SUM(MONTANT) FROM (
     * 			SELECT idSection, dateExecution FROM CCJUWEB.CAEVCTP
     * 			INNER JOIN CCJUWEB.CAPECTP ON CCJUWEB.CAPECTP.IDPARAMETREETAPE=CCJUWEB.CAEVCTP.IDPARAMETREETAPE
     * 			INNER JOIN CCJUWEB.CAETCTP ON CCJUWEB.CAETCTP.IDETAPE=CCJUWEB.CAPECTP.IDETAPE
     * 			WHERE dateExecution >= 20080101 AND dateExecution <= 20081231
     * 			AND TYPEETAPE = 216003
     * 		) a
     * 		INNER JOIN CCJUWEB.CAOPERP op ON a.idSection=op.IDSECTION
     * 			AND op.DATE<=a.dateExecution
     * 			AND ETAT=205002 AND IDTYPEOPERATION LIKE 'E%'
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
     * <pre>
     * SELECT idSection, dateExecution FROM CCJUWEB.CAEVCTP
     * 		INNER JOIN CCJUWEB.CAPECTP ON CCJUWEB.CAPECTP.IDPARAMETREETAPE=CCJUWEB.CAEVCTP.IDPARAMETREETAPE
     * 		INNER JOIN CCJUWEB.CAETCTP ON CCJUWEB.CAETCTP.IDETAPE=CCJUWEB.CAPECTP.IDETAPE
     * 		WHERE dateExecution >= 20080101 AND dateExecution <= 20081231
     * 		AND TYPEETAPE = 216003
     * </pre>
     * 
     * @return
     */
    private String getSqlFrom() {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.SELECT);
        sqlFrom.append("idSection, dateExecution");
        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.FROM);
        sqlFrom.append(_getCollection() + CAEvenementContentieux.TABLE_CAEVCTP);

        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.INNER_JOIN);
        sqlFrom.append(_getCollection() + CAStatOfasPoursuiteOsirisSumManager.TABLE_CAPECTP);
        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.ON);
        sqlFrom.append(_getCollection() + CAStatOfasPoursuiteOsirisSumManager.TABLE_CAPECTP);
        sqlFrom.append(".IDPARAMETREETAPE");
        sqlFrom.append("=");
        sqlFrom.append(_getCollection() + CAEvenementContentieux.TABLE_CAEVCTP);
        sqlFrom.append(".IDPARAMETREETAPE");
        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.INNER_JOIN);
        sqlFrom.append(_getCollection() + CAStatOfasPoursuiteOsirisSumManager.TABLE_CAETCTP);
        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.ON);
        sqlFrom.append(_getCollection() + CAStatOfasPoursuiteOsirisSumManager.TABLE_CAETCTP);
        sqlFrom.append(".IDETAPE");
        sqlFrom.append("=");
        sqlFrom.append(_getCollection() + CAStatOfasPoursuiteOsirisSumManager.TABLE_CAPECTP);
        sqlFrom.append(".IDETAPE");

        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.WHERE);
        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.DATE_EXECUTION);
        sqlFrom.append(" >= ");
        sqlFrom.append(getForAnnee() + CAStatOfasPoursuiteOsirisSumManager.DATE_DEBUT_JANVIER);
        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.AND);
        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.DATE_EXECUTION);
        sqlFrom.append(" <= ");
        sqlFrom.append(getForAnnee() + CAStatOfasPoursuiteOsirisSumManager.DATE_FIN_DECEMBRE);
        sqlFrom.append(CAStatOfasPoursuiteOsirisSumManager.AND);
        sqlFrom.append("TYPEETAPE");
        sqlFrom.append(" = ");
        sqlFrom.append(getForCsTypeEtape());

        return sqlFrom.toString();
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
