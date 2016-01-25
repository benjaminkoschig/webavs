package globaz.osiris.db.interet.tardif.montantsoumis;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.interets.CARubriqueSoumiseInteret;
import java.util.ArrayList;

/**
 * Pour chaque secteur somme le montant des écritures soumis à intérêt groupé par plan.
 * 
 * SQL : select sum(montant) as summontant from webavs.caoperp a where a.idsection = 288129 and (a.etat = 205002 or
 * a.etat = 205004) and a.idtypeoperation like 'E%' and a.idcompte in (select b.idrubrique from webavs.carubrp b,
 * webavs.caimrsp im where b.idrubrique = im.idrubrique and (b.naturerubrique < 200005 or b.naturerubrique > 200006) and
 * im.idplacalint = 2 order by b.idrubrique)
 * 
 * @author DDA
 * 
 */
public class CASumMontantSoumisParPlanManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList forAnneesCotisationsIn;
    private String forIdPlan;

    private String forIdSection;

    /**
     * see globaz.globall.db.BManager#_beforeFind(globaz.globall.db.BStatement)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        // TODO Dal Implement validation rules HERE !
        super._beforeFind(transaction);
    }

    /**
     * see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "sum(" + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT;
    }

    /**
     * see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " a ";
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
        where.append("a." + CAOperation.FIELD_IDTYPEOPERATION + " >= '" + APIOperation.CAECRITURE + "' and ");

        where.append("a." + CAOperation.FIELD_IDCOMPTE + " in (");

        where.append("select b." + CARubrique.FIELD_IDRUBRIQUE + " from " + _getCollection() + CARubrique.TABLE_CARUBRP
                + " b, " + _getCollection() + CARubriqueSoumiseInteret.TABLE_CAIMRSP + " im where ");
        where.append("b." + CARubrique.FIELD_IDRUBRIQUE + " = im." + CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE
                + " and ");
        where.append("(b." + CARubrique.FIELD_NATURERUBRIQUE + " < " + APIRubrique.COMPTE_COURANT_DEBITEUR + " or b."
                + CARubrique.FIELD_NATURERUBRIQUE + " > " + APIRubrique.COMPTE_COURANT_CREANCIER + ") ");

        if (!JadeStringUtil.isBlank(getForIdPlan())) {
            where.append(" and im." + CARubriqueSoumiseInteret.FIELD_IDPLACALINT + " = " + getForIdPlan() + " ");
        }

        where.append(")");

        if ((getForAnneesCotisationsIn() != null) && !getForAnneesCotisationsIn().isEmpty()) {
            where.append(" AND ");
            where.append(getWhereAnneesCotisationsIn());
        }

        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASumMontantSoumisParPlan();
    }

    public ArrayList getForAnneesCotisationsIn() {
        return forAnneesCotisationsIn;
    }

    public String getForIdPlan() {
        return forIdPlan;
    }

    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * Return la partie du where relative aux types d'opérations.
     * 
     * @return
     */
    private String getWhereAnneesCotisationsIn() {
        StringBuffer tmp = new StringBuffer("a." + CAOperation.FIELD_ANNEECOTISATION + " in (");

        for (int i = 0; i < getForAnneesCotisationsIn().size(); i++) {
            tmp.append("" + getForAnneesCotisationsIn().get(i));

            if (i < getForAnneesCotisationsIn().size() - 1) {
                tmp.append(", ");
            }
        }

        tmp.append(") ");

        return tmp.toString();
    }

    public void setForAnneesCotisationsIn(ArrayList forAnneesCotisationsIn) {
        this.forAnneesCotisationsIn = forAnneesCotisationsIn;
    }

    public void setForIdPlan(String forIdPlan) {
        this.forIdPlan = forIdPlan;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

}
