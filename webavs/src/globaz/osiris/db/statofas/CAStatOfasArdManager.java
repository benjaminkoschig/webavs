/**
 * 
 */
package globaz.osiris.db.statofas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntRole;

// SELECT sum(d.montanttot)
// select count(*)
// --SELECT c.idexternerole, c.idrole, c.description, s.idexterne, s.datesection, s.solde, d.montanttot
// FROM webavsciam.CASECTP s
// inner join webavsciam.CAcptap c on c.idcompteannexe = s.idcompteannexe and idrole <> 517041
// inner join (
// SELECT IDSECTION, sum(montant) montanttot FROM webavsciam.CAOPERP
// --inner join webavsciam.cajourp on webavsciam.cajourp.idjournal=webavsciam.CAOPERP.idjournal
// --and webavsciam.cajourp.datevaleurcg >= 20100101 AND webavsciam.cajourp.datevaleurcg <= 20101231
// INNER JOIN webavsciam.CARUBRP ON webavsciam.CARUBRP.IDRUBRIQUE=webavsciam.CAOPERP.IDCOMPTE
// WHERE webavsciam.CARUBRP.IDEXTERNE like '%.2740.%'
// AND webavsciam.CAOPERP.ETAT = 205002 and webavsciam.CAOPERP.idtypeoperation like 'E%'
// AND webavsciam.CAOPERP.DATE >= 20100101 AND webavsciam.CAOPERP.DATE <= 20101231
// group by idsection
// ) d on s.IDSECTION=d.idsection
// --order by c.idexternerole, s.idexterne

/**
 * @author sel
 * 
 */
public class CAStatOfasArdManager extends BManager {

    private static final long serialVersionUID = 8682939337443146731L;
    public static final String ALIAS_MONTANTTOT = "montanttot";
    private static final String AND = " AND ";
    private static final String DATE_DEBUT_JANVIER = "0101";
    private static final String DATE_FIN_DECEMBRE = "1231";
    private static final String FROM = " FROM ";
    private static final String GROUP_BY = " GROUP BY ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String LIKE = " like ";
    private static final String ON = " ON ";
    private static final String SECTEUR = "'%.2740.%'";
    private static final String SELECT = "SELECT ";
    private static final String WHERE = " WHERE ";

    int forAnnee = 0;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String tSection = _getCollection() + CASection.TABLE_CASECTP;
        String tCompteAnnexe = _getCollection() + CACompteAnnexe.TABLE_CACPTAP;

        StringBuffer from = new StringBuffer(tSection);

        from.append(CAStatOfasArdManager.INNER_JOIN).append(tCompteAnnexe);
        from.append(CAStatOfasArdManager.ON);
        from.append(tCompteAnnexe).append(".").append(CACompteAnnexe.FIELD_IDCOMPTEANNEXE);
        from.append("=");
        from.append(tSection).append(".").append(CASection.FIELD_IDCOMPTEANNEXE);
        from.append(CAStatOfasArdManager.AND).append(CACompteAnnexe.FIELD_IDROLE);
        from.append("<>").append(IntRole.ROLE_ADMINISTRATEUR);

        from.append(CAStatOfasArdManager.INNER_JOIN).append("(");
        from.append(generateSubQuery());
        from.append(") d").append(CAStatOfasArdManager.ON);
        from.append(tSection).append(".").append(CASection.FIELD_IDSECTION).append(" = ").append("d.")
                .append(CASection.FIELD_IDSECTION);

        return from.toString();
    }

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
     * @return
     */
    private String generateSubQuery() {
        // SELECT IDSECTION, sum(montant) montanttot FROM webavsciam.CAOPERP
        // --inner join webavsciam.cajourp on webavsciam.cajourp.idjournal=webavsciam.CAOPERP.idjournal
        // --and webavsciam.cajourp.datevaleurcg >= 20100101 AND webavsciam.cajourp.datevaleurcg <= 20101231
        // INNER JOIN webavsciam.CARUBRP ON webavsciam.CARUBRP.IDRUBRIQUE=webavsciam.CAOPERP.IDCOMPTE
        // WHERE webavsciam.CARUBRP.IDEXTERNE like '%.2740.%'

        // AND webavsciam.CAOPERP.ETAT = 205002 and webavsciam.CAOPERP.idtypeoperation like 'E%'
        // AND webavsciam.CAOPERP.DATE >= 20100101 AND webavsciam.CAOPERP.DATE <= 20101231
        // group by idsection

        String tOperation = _getCollection() + CAOperation.TABLE_CAOPERP;
        String tRubrique = _getCollection() + CARubrique.TABLE_CARUBRP;
        String tJournal = _getCollection() + CAJournal.TABLE_CAJOURP;

        StringBuffer subQuery = new StringBuffer();
        subQuery.append(CAStatOfasArdManager.SELECT).append(CASection.FIELD_IDSECTION).append(", ").append("SUM(")
                .append(CAOperation.FIELD_MONTANT).append(") ").append(CAStatOfasArdManager.ALIAS_MONTANTTOT);
        subQuery.append(CAStatOfasArdManager.FROM).append(tOperation);
        subQuery.append(CAStatOfasArdManager.INNER_JOIN).append(tRubrique);
        subQuery.append(CAStatOfasArdManager.ON);
        subQuery.append(tRubrique).append(".").append(CARubrique.FIELD_IDRUBRIQUE);
        subQuery.append("=");
        subQuery.append(tOperation).append(".").append(CAOperation.FIELD_IDCOMPTE);

        subQuery.append(CAStatOfasArdManager.INNER_JOIN).append(tJournal);
        subQuery.append(CAStatOfasArdManager.ON);
        subQuery.append(tJournal).append(".").append(CAJournal.FIELD_IDJOURNAL);
        subQuery.append("=");
        subQuery.append(tOperation).append(".").append(CAOperation.FIELD_IDJOURNAL);

        subQuery.append(CAStatOfasArdManager.WHERE);
        subQuery.append(tRubrique).append(".").append(CARubrique.FIELD_IDEXTERNE);
        subQuery.append(" like ").append(CAStatOfasArdManager.SECTEUR);
        subQuery.append(CAStatOfasArdManager.AND).append(tOperation).append(".").append(CAOperation.FIELD_ETAT)
                .append(" = ").append(APIOperation.ETAT_COMPTABILISE);
        subQuery.append(CAStatOfasArdManager.AND).append(tOperation).append(".")
                .append(CAOperation.FIELD_IDTYPEOPERATION).append(" like 'E%'");
        subQuery.append(CAStatOfasArdManager.AND).append(tJournal).append(".").append(CAJournal.FIELD_DATEVALEURCG)
                .append(" >= ");
        subQuery.append(getForAnnee()).append(CAStatOfasArdManager.DATE_DEBUT_JANVIER);
        subQuery.append(CAStatOfasArdManager.AND).append(tJournal).append(".").append(CAJournal.FIELD_DATEVALEURCG)
                .append(" <= ");
        subQuery.append(getForAnnee()).append(CAStatOfasArdManager.DATE_FIN_DECEMBRE);
        subQuery.append(CAStatOfasArdManager.GROUP_BY).append(CAOperation.FIELD_IDSECTION);

        return subQuery.toString();
    }

    /**
     * @return the forAnnee
     */
    public int getForAnnee() {
        return forAnnee;
    }

    /**
     * @param forAnnee
     *            the forAnnee to set
     */
    public void setForAnnee(int forAnnee) {
        this.forAnnee = forAnnee;
    }
}
