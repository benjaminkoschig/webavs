package globaz.osiris.db.remboursementauto;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;

/**
 * <pre>
 * select a.idcompteannexe, a.idsection, a.idcomptecourant, sum(a.montant), b.idexternerole, b.idrole from webavs.caoperp a, webavs.cacptap b where
 * a.idcompteannexe = b.idcompteannexe and b.idrole = 517002 and a.idcomptecourant = 1 and
 * (a.ETAT = 205002 or a.ETAT = 205004)
 * group by a.idcompteannexe, a.idsection, a.idcomptecourant, b.idexternerole, b.idrole
 * having sum(a.montant) < -50.00
 * </pre>
 * 
 * @author dda
 */
public class CARemboursementAutomatiqueManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCompteCourant;
    private String forIdRole;

    private String forMontantLimit;

    /**
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        StringBuffer select = new StringBuffer("SELECT sum(a." + CAOperation.FIELD_MONTANT + ") as "
                + CAOperation.FIELD_MONTANT + ", a." + CAOperation.FIELD_IDSECTION + ", b."
                + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + ", b." + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", b."
                + CACompteAnnexe.FIELD_IDROLE + " ");
        select.append(" from " + _getCollection() + CAOperation.TABLE_CAOPERP + " a, " + _getCollection()
                + CACompteAnnexe.TABLE_CACPTAP + " b ");
        select.append(" where ");
        select.append("a." + CAOperation.FIELD_IDCOMPTEANNEXE + " = b." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " and ");

        if (!JadeStringUtil.isBlank(getForIdRole())) {
            select.append("b." + CACompteAnnexe.FIELD_IDROLE + " = " + getForIdRole() + " and ");
        }

        if (!JadeStringUtil.isBlank(getForIdCompteCourant())) {
            select.append("a." + CAOperation.FIELD_IDCOMPTECOURANT + " = " + getForIdCompteCourant() + " and ");
        }

        select.append("(a." + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE + " or a."
                + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_PROVISOIRE + ") ");

        select.append("group by a." + CAOperation.FIELD_IDSECTION + ", b." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE
                + ", b." + CACompteAnnexe.FIELD_IDEXTERNEROLE + ", b." + CACompteAnnexe.FIELD_IDROLE + " ");

        select.append("having sum(a." + CAOperation.FIELD_MONTANT + ") < " + getForMontantLimit());

        return select.toString();

    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CARemboursementAutomatique();
    }

    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    public String getForIdRole() {
        return forIdRole;
    }

    public String getForMontantLimit() {
        return forMontantLimit;
    }

    public void setForIdCompteCourant(String forIdCompteCourant) {
        this.forIdCompteCourant = forIdCompteCourant;
    }

    public void setForIdRole(String forIdRole) {
        this.forIdRole = forIdRole;
    }

    public void setForMontantLimit(String forMontantLimit) {
        this.forMontantLimit = forMontantLimit;
    }

}
