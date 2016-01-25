/**
 *
 */
package globaz.osiris.db.statofas;

import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.aquila.api.ICOSequenceConstante;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAJournal;

/**
 * @author SEL
 */
public class CAStatOfasPoursuiteAquilaManager extends BManager {

    private static final long serialVersionUID = -2902629838552793936L;
    private static final String AND = " AND ";
    private static final String DATE_ZERO = "0000";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String ON = " ON ";

    int forAnnee = 0;
    private String forCsEtape = "";
    private String forIdSequence = "";

    /**
     * <pre>
     * SELECT SUM(OEMSOL) FROM WEBAVSCIAM.COHISTP
     *      inner join WEBAVSCIAM.COETAPP on WEBAVSCIAM.COETAPP.ODIETA=WEBAVSCIAM.COHISTP.ODIETA
     *      inner join webavsciam.COSEQP on webavsciam.COSEQP.ofiseq=WEBAVSCIAM.COETAPP.ofiseqd
     *      inner join CCJUWEB.CAJOURP on CCJUWEB.CAJOURP.IDJOURNAL=CCJUWEB.COHISTP.OEIJRN
     *      WHERE
     *      ODTETA = 5200001 AND OFTSEQ = 5100001
     *      AND dateValeurCG >= 20080000 AND dateValeurCG <= 20090000
     * </pre>
     */

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer();
        from.append(_getCollection() + ICOHistoriqueConstante.TABLE_NAME);
        from.append(CAStatOfasPoursuiteAquilaManager.INNER_JOIN);
        from.append(_getCollection() + ICOEtapeConstante.TABLE_NAME);
        from.append(CAStatOfasPoursuiteAquilaManager.ON);
        from.append(_getCollection() + ICOEtapeConstante.TABLE_NAME + "." + ICOEtapeConstante.FNAME_ID_ETAPE);
        from.append("=");
        from.append(_getCollection() + ICOHistoriqueConstante.TABLE_NAME + "." + ICOHistoriqueConstante.FNAME_ID_ETAPE);

        from.append(CAStatOfasPoursuiteAquilaManager.INNER_JOIN);
        from.append(_getCollection() + ICOSequenceConstante.TABLE_NAME);
        from.append(CAStatOfasPoursuiteAquilaManager.ON);
        from.append(_getCollection() + ICOSequenceConstante.TABLE_NAME + "." + ICOSequenceConstante.FNAME_ID_SEQUENCE);
        from.append("=");
        from.append(_getCollection() + ICOHistoriqueConstante.TABLE_NAME + "."
                + ICOHistoriqueConstante.FNAME_ID_SEQUENCE);

        from.append(CAStatOfasPoursuiteAquilaManager.INNER_JOIN);
        from.append(_getCollection() + CAJournal.TABLE_CAJOURP);
        from.append(CAStatOfasPoursuiteAquilaManager.ON);
        from.append(_getCollection() + CAJournal.TABLE_CAJOURP + "." + CAJournal.FIELD_IDJOURNAL);
        from.append("=");
        from.append(_getCollection() + ICOHistoriqueConstante.TABLE_NAME + "."
                + ICOHistoriqueConstante.FNAME_ID_JOURNAL);

        return from.toString();
    }

    /**
     * ODTETA = 5200001 AND OFTSEQ = 5100001 AND OEDEXE >= 20080000 AND OEDEXE <= 20090000
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(ICOEtapeConstante.FNAME_LIBETAPE);
        sqlWhere.append(" = ");
        sqlWhere.append(getForCsEtape());
        sqlWhere.append(CAStatOfasPoursuiteAquilaManager.AND);
        sqlWhere.append(ICOSequenceConstante.FNAME_LIB_SEQUENCE);
        sqlWhere.append(" = ");
        sqlWhere.append(getForIdSequence());
        sqlWhere.append(CAStatOfasPoursuiteAquilaManager.AND);
        sqlWhere.append(CAJournal.FIELD_DATEVALEURCG);
        sqlWhere.append(" >= ");
        sqlWhere.append(getForAnnee() + CAStatOfasPoursuiteAquilaManager.DATE_ZERO);
        sqlWhere.append(CAStatOfasPoursuiteAquilaManager.AND);
        sqlWhere.append(CAJournal.FIELD_DATEVALEURCG);
        sqlWhere.append(" <= ");
        sqlWhere.append((getForAnnee() + 1) + CAStatOfasPoursuiteAquilaManager.DATE_ZERO);

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
     * @return the forCsEtape
     */
    public String getForCsEtape() {
        return forCsEtape;
    }

    /**
     * @return the forIdSequence
     */
    public String getForIdSequence() {
        return forIdSequence;
    }

    /**
     * @param forAnnee
     *            the forAnnee to set
     */
    public void setForAnnee(int forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @param forCsEtape
     *            the forCsEtape to set
     */
    public void setForCsEtape(String forCsEtape) {
        this.forCsEtape = forCsEtape;
    }

    /**
     * @param forIdSequence
     *            the forIdSequence to set
     */
    public void setForIdSequence(String forIdSequence) {
        this.forIdSequence = forIdSequence;
    }
}