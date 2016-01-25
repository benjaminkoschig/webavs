package globaz.osiris.db.controleConcordanceSoldes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntRole;

/**
 * @author sch
 */
public class CAControleConcordanceSoldeCASEExcelManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {

        String select = "SELECT CA." + CACompteAnnexe.FIELD_IDROLE + ", CA." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                + ", CA." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ", CA." + CACompteAnnexe.FIELD_SOLDE + ", sum(SE."
                + CASection.FIELD_SOLDE + ") AS CUMULSECTIONS";

        select += " FROM " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " CA ";
        select += " INNER JOIN " + _getCollection() + CASection.TABLE_CASECTP + " SE ";
        select += " ON (CA." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " = SE." + CASection.FIELD_IDCOMPTEANNEXE;
        select += ") WHERE CA." + CACompteAnnexe.FIELD_IDROLE + " <> " + IntRole.ROLE_ADMINISTRATEUR;
        select += " GROUP BY CA." + CACompteAnnexe.FIELD_IDROLE + ", CA." + CACompteAnnexe.FIELD_IDEXTERNEROLE
                + ", CA." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ", CA." + CACompteAnnexe.FIELD_SOLDE;
        select += " HAVING CA." + CACompteAnnexe.FIELD_SOLDE + " <> sum(SE." + CASection.FIELD_SOLDE + ")";

        return select;

    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAControleConcordanceSoldeCASEExcel();
    }
}
