package globaz.osiris.db.sections;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;

public class CASectionsOuvertesManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDate;
    private String forSelectionRole;

    /**
     * select * from webavs.casectp a, webavs.cacptap b where a.idcompteannexe = b.idcompteannexe and a.date <= 20051231
     * and a.solde <> 0 order by b.idexternerole, a.date
     */
    @Override
    protected String _getSql(BStatement statement) {
        String sql = "select * ";

        sql += "from " + _getCollection() + CASection.TABLE_CASECTP + " a, " + _getCollection()
                + CACompteAnnexe.TABLE_CACPTAP + " b ";
        sql += "where ";
        sql += "a." + CASection.FIELD_IDCOMPTEANNEXE + " = b." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " and ";

        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            sql += "b." + CACompteAnnexe.FIELD_IDROLE + " in (" + getForSelectionRole() + ") and ";
        }

        if (!JadeStringUtil.isBlank(getForDate())) {
            sql += "a." + CASection.FIELD_DATESECTION + " <= "
                    + JACalendar.format(getForDate(), JACalendar.FORMAT_YYYYMMDD) + " and ";
        } else {
            sql += "a." + CASection.FIELD_DATESECTION + " <= " + JACalendar.today().toStrAMJ() + " and ";
        }

        sql += "a." + CASection.FIELD_SOLDE + " <> 0 ";

        sql += "order by b." + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", a." + CASection.FIELD_IDEXTERNE + ", a."
                + CASection.FIELD_DATESECTION;

        return sql;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASectionsOuvertes();
    }

    public String getForDate() {
        return forDate;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    public void setForDate(String forDateValeur) {
        forDate = forDateValeur;
    }

    public void setForSelectionRole(String forSelectionRole) {
        this.forSelectionRole = forSelectionRole;
    }

}
