package globaz.osiris.db.interet.util.planparsection;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.interets.CARubriqueSoumiseInteret;

/**
 * Recherche des secteurs soumis à intérêt de la section dont le montant (du secteur) <= 0.
 * 
 * SQL : select im.idplacalint from webavs.caoperp a, webavs.cacptcp b, webavs.caimrsp im where a.idsection = 288632 and
 * (a.etat = 205002 or a.etat = 205004) and a.idtypeoperation like 'E%' and a.idcomptecourant = b.idcomptecourant and
 * b.idrubrique = im.idrubrique group by im.idplacalint having sum(a.montant) <= 0
 * 
 * @author DDA
 * 
 */
public class CAPlanParSectionManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPlan;
    private String forIdSection;

    private boolean montantCumuleNotPositive = true;

    /**
     * see globaz.globall.db.BManager#_beforeFind(globaz.globall.db.BStatement)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        // TODO Dal Implement validation rules HERE !
        super._beforeFind(transaction);
    }

    /**
     * see globaz.globall.db.BManager#_getSqlCount(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "im." + CARubriqueSoumiseInteret.FIELD_IDPLACALINT;
    }

    /**
     * see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " a, " + _getCollection() + CACompteCourant.TABLE_CACPTCP
                + " b, " + _getCollection() + CARubriqueSoumiseInteret.TABLE_CAIMRSP + " im";
    }

    /**
     * see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer();
        where.append("a." + CAOperation.FIELD_IDSECTION + " = " + getForIdSection() + " and ");
        where.append("(a." + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE + " or a."
                + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_PROVISOIRE + ") and ");
        where.append("a." + CAOperation.FIELD_IDTYPEOPERATION + " like '" + APIOperation.CAECRITURE + "%' and ");
        where.append("a." + CAOperation.FIELD_IDCOMPTECOURANT + " = b." + CACompteCourant.FIELD_IDCOMPTECOURANT
                + " and ");
        where.append("b." + CACompteCourant.FIELD_IDRUBRIQUE + " = im." + CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE
                + " ");

        if (!JadeStringUtil.isIntegerEmpty(getForIdPlan())) {
            where.append("and im." + CARubriqueSoumiseInteret.FIELD_IDPLACALINT + " = " + getForIdPlan() + " ");
        }

        where.append("group by im." + CARubriqueSoumiseInteret.FIELD_IDPLACALINT + " ");

        if (isMontantCumuleNotPositive()) {
            where.append("having sum(a." + CAOperation.FIELD_MONTANT + ") <= 0");
        }

        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAPlanParSection();
    }

    public String getForIdPlan() {
        return forIdPlan;
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public boolean isMontantCumuleNotPositive() {
        return montantCumuleNotPositive;
    }

    public void setForIdPlan(String forIdPlan) {
        this.forIdPlan = forIdPlan;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    public void setMontantCumuleNotPositive(boolean montantCumuleNotPositive) {
        this.montantCumuleNotPositive = montantCumuleNotPositive;
    }
}
