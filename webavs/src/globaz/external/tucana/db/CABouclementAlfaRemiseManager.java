/**
 * 
 */
package globaz.external.tucana.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;

/**
 * @author sel
 * 
 */
public class CABouclementAlfaRemiseManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDatePeriodeBegin;
    private String forDatePeriodeEnd;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        // select cs.PCOUID, sum(op.montant) MONTANT, sum(op.masse) MASSE, op.ANNEECOTISATION, ru.IDEXTERNE
        // from h51xweb.caoperp op
        // inner join h51xweb.cajourp jo on (op.idjournal=jo.idjournal)
        // inner join h51xweb.carubrp ru on (op.idcompte=ru.idrubrique)
        // inner join h51xweb.FWCOUP cs on cs.pcosid = ((CAST(SUBSTR(ru.IDEXTERNE, 13, 2) as NUMERIC)) + 505000)
        // where jo.datevaleurcg between 20120501 and 20120531
        // and op.etat=205002
        // and ru.idexterne like '5500.3320.%'
        // and cs.PLAIDE = 'F'
        // group by cs.PCOUID, ru.IDEXTERNE, op.ANNEECOTISATION

        StringBuilder sql = new StringBuilder();

        sql.append("select sum(op.montant) MONTANT, sum(op.masse) MASSE, cs.PCOUID");
        sql.append(", op." + CAOperation.FIELD_ANNEECOTISATION);
        sql.append(" from ").append(_getCollection()).append("caoperp op");
        sql.append(" inner join ").append(_getCollection()).append("cajourp jo on (op.idjournal=jo.idjournal)");
        sql.append(" inner join ").append(_getCollection()).append("carubrp ru on (op.idcompte=ru.idrubrique)");
        sql.append(" inner join ").append(_getCollection())
                .append("fwcoup cs on (cs.pcosid=((CAST(SUBSTR(ru.IDEXTERNE, 13, 2) as NUMERIC)) + 505000))");
        sql.append(" where jo.datevaleurcg between ").append(getForDatePeriodeBegin()).append(" and ")
                .append(getForDatePeriodeEnd());
        sql.append(" and op.etat=205002");
        sql.append(" and ru.idexterne like '5500.3320.%'");
        sql.append(" and cs.PLAIDE = 'F'");
        sql.append(" group by cs.PCOUID");
        sql.append(", op." + CAOperation.FIELD_ANNEECOTISATION);

        return sql.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CABouclementAlfa();
    }

    /**
     * @return the forDatePeriodeBegin
     */
    public String getForDatePeriodeBegin() {
        return forDatePeriodeBegin;
    }

    /**
     * @return the forDatePeriodeEnd
     */
    public String getForDatePeriodeEnd() {
        return forDatePeriodeEnd;
    }

    /**
     * @param forDatePeriodeBegin
     *            the forDatePeriodeBegin to set
     */
    public void setForDatePeriodeBegin(String forDatePeriodeBegin) {
        this.forDatePeriodeBegin = forDatePeriodeBegin;
    }

    /**
     * @param forDatePeriodeEnd
     *            the forDatePeriodeEnd to set
     */
    public void setForDatePeriodeEnd(String forDatePeriodeEnd) {
        this.forDatePeriodeEnd = forDatePeriodeEnd;
    }

}
