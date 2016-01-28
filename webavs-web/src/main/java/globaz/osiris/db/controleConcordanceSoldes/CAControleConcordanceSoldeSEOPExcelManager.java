package globaz.osiris.db.controleConcordanceSoldes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntRole;

/**
 * @author sch
 */
public class CAControleConcordanceSoldeSEOPExcelManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {

        String select = "SELECT CA." + CACompteAnnexe.FIELD_IDROLE + ", CA." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE
                + ", CA." + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", SE." + CASection.FIELD_IDSECTION + ", SE."
                + CASection.FIELD_IDTYPESECTION + ", SE." + CASection.FIELD_IDTYPESECTION + ", SE."
                + CASection.FIELD_IDEXTERNE + ", SE." + CASection.FIELD_IDEXTERNE + ", SE." + CASection.FIELD_SOLDE
                + ", sum(OP." + CAOperation.FIELD_MONTANT + ") AS CUMULOPERATIONS";
        select += " FROM " + _getCollection() + CAOperation.TABLE_CAOPERP + " OP ";
        select += " INNER JOIN " + _getCollection() + CASection.TABLE_CASECTP + " SE ";
        select += " ON OP." + CAOperation.FIELD_IDSECTION + " = SE." + CASection.FIELD_IDSECTION;
        select += " INNER JOIN " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP + " CA ";
        select += " ON SE." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " = CA." + CASection.FIELD_IDCOMPTEANNEXE;
        select += " WHERE OP." + CAOperation.FIELD_ETAT + " IN (" + APIOperation.ETAT_COMPTABILISE + ", "
                + APIOperation.ETAT_PROVISOIRE + ") AND " + CAOperation.FIELD_IDTYPEOPERATION + " LIKE 'E%' AND "
                + CACompteAnnexe.FIELD_IDROLE + "<>" + IntRole.ROLE_ADMINISTRATEUR;
        select += " GROUP BY CA." + CACompteAnnexe.FIELD_IDROLE + ", CA." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE
                + ", CA." + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", SE." + CASection.FIELD_IDSECTION + ", SE."
                + CASection.FIELD_IDTYPESECTION + ", SE." + CASection.FIELD_IDTYPESECTION + ", SE."
                + CASection.FIELD_IDEXTERNE + ", SE." + CASection.FIELD_SOLDE;
        select += " HAVING SE." + CASection.FIELD_SOLDE + " <> sum(" + CAOperation.FIELD_MONTANT + ")";

        return select;

    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAControleConcordanceSoldeSEOPExcel();
    }
}
