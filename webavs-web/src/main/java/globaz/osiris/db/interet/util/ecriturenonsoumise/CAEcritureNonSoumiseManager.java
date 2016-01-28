package globaz.osiris.db.interet.util.ecriturenonsoumise;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.interets.CARubriqueSoumiseInteret;
import java.util.ArrayList;

/**
 * Liste des écritures d'un plan pour la section non soumises groupé par date => montant cumulé.
 * 
 * @author DDA SQL : select sum(a.montant) as montant, a.date from webavs.caoperp a, webavs.carubrp b where a.idsection
 *         = 288632 and (a.etat = 205002 or a.etat = 205004) and a.idtypeoperation like 'E%' and a.idcompte not in
 *         (select idrubrique as idcompte from WEBAVS.CAIMRSP) and a.idcompte = b.idrubrique and b.naturerubrique in
 *         (200004, 200005, 200006, 200007) and a.idcomptecourant in (select a.idcomptecourant from WEBAVS.CACPTCP a,
 *         WEBAVS.CAIMRSP b where a.IDRUBRIQUE = b.idrubrique and b.idplacalint = 1 ) group by a.date
 */
public class CAEcritureNonSoumiseManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateAfter;
    private String forDateBefore;

    private ArrayList<String> forIdCompteCourantIn;
    private String forIdPlan;

    private String forIdSection;
    private ArrayList<String> forIdSectionIn;

    /**
     * see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "sum(a." + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT + ", a."
                + CAOperation.FIELD_DATE;
    }

    /**
     * see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " a, " + _getCollection() + CARubrique.TABLE_CARUBRP
                + " b";
    }

    /**
     * see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = getWhereSql();
        where.append(getGroupBySql());
        where.append(getOrderSql());

        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAEcritureNonSoumise();
    }

    public String getForDateAfter() {
        return forDateAfter;
    }

    public String getForDateBefore() {
        return forDateBefore;
    }

    public ArrayList<String> getForIdCompteCourantIn() {
        return forIdCompteCourantIn;
    }

    public String getForIdPlan() {
        return forIdPlan;
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public ArrayList<String> getForIdSectionIn() {
        return forIdSectionIn;
    }

    /**
     * Return la partie group by
     * 
     * @return
     */
    protected StringBuffer getGroupBySql() {
        StringBuffer groupBy = new StringBuffer();

        groupBy.append(" group by a." + CAOperation.FIELD_DATE + " ");

        return groupBy;
    }

    /**
     * Return la partie order by.
     * 
     * @return
     */
    protected String getOrderSql() {
        return "order by a." + CAOperation.FIELD_DATE + " asc";
    }

    private String getWhereIdCompteCourant() {
        StringBuffer tmp = new StringBuffer("a." + CAOperation.FIELD_IDCOMPTECOURANT + " in (");

        for (int i = 0; i < getForIdCompteCourantIn().size(); i++) {
            tmp.append("" + getForIdCompteCourantIn().get(i));

            if (i < getForIdCompteCourantIn().size() - 1) {
                tmp.append(", ");
            }
        }

        tmp.append(") ");

        return tmp.toString();
    }

    private String getWhereIdSection() {
        StringBuffer tmp = new StringBuffer("a." + CAOperation.FIELD_IDSECTION + " in (");

        for (int i = 0; i < getForIdSectionIn().size(); i++) {
            tmp.append("" + getForIdSectionIn().get(i));

            if (i < getForIdSectionIn().size() - 1) {
                tmp.append(", ");
            }
        }

        tmp.append(") and ");

        return tmp.toString();
    }

    /**
     * Return la clause where.
     * 
     * @return
     */
    protected StringBuffer getWhereSql() {
        StringBuffer where = new StringBuffer();

        if (getForIdSectionIn() != null) {
            where.append(getWhereIdSection());
        } else {
            where.append("a." + CAOperation.FIELD_IDSECTION + " = " + getForIdSection() + " and ");
        }

        where.append("(a." + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE + " or a."
                + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_PROVISOIRE + ") and ");
        where.append("a." + CAOperation.FIELD_IDTYPEOPERATION + " like '" + APIOperation.CAECRITURE + "%' and ");
        where.append("a." + CAOperation.FIELD_IDCOMPTE + " = b.IDRUBRIQUE and ");

        where.append("((");

        // WARN (dda) Remplacé pour cause de lenteur ... Attention à la plage
        // des codes systèmes...doit rester inchangé !
        // where.append("b." + CARubrique.FIELD_NATURERUBRIQUE + " in (" +
        // APIRubrique.COMPTE_FINANCIER + "," + APIRubrique.COMPTE_COMPENSATION
        // + "," + APIRubrique.COMPTE_COURANT_DEBITEUR + "," +
        // APIRubrique.COMPTE_COURANT_CREANCIER + ") and ");
        where.append("b." + CARubrique.FIELD_NATURERUBRIQUE + " >= " + APIRubrique.COMPTE_FINANCIER + " and b."
                + CARubrique.FIELD_NATURERUBRIQUE + " <= " + APIRubrique.COMPTE_COMPENSATION);

        where.append(") or (");
        where.append("(a." + CAOperation.FIELD_MONTANT + " < 0) and ");
        where.append("(b." + CARubrique.FIELD_NATURERUBRIQUE + " >= " + APIRubrique.STANDARD + " and b."
                + CARubrique.FIELD_NATURERUBRIQUE + " <= " + APIRubrique.COTISATION_SANS_MASSE + ")");
        where.append(")) and ");

        where.append("a." + CAOperation.FIELD_IDCOMPTE + " not in (select " + CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE
                + " as idcompte from " + _getCollection() + CARubriqueSoumiseInteret.TABLE_CAIMRSP + " where "
                + CARubriqueSoumiseInteret.FIELD_IDPLACALINT + " = " + getForIdPlan() + ") and ");

        if (getForIdCompteCourantIn() != null) {
            where.append(getWhereIdCompteCourant());
        } else {
            where.append("a." + CAOperation.FIELD_IDCOMPTECOURANT + " in (select a.idcomptecourant from "
                    + _getCollection() + CACompteCourant.TABLE_CACPTCP + " a, " + _getCollection()
                    + CARubriqueSoumiseInteret.TABLE_CAIMRSP + " b where a." + CACompteCourant.FIELD_IDRUBRIQUE
                    + " = b." + CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE + " and b."
                    + CARubriqueSoumiseInteret.FIELD_IDPLACALINT + " = " + getForIdPlan() + " ) ");
        }

        if (!JadeStringUtil.isBlank(getForDateAfter())) {
            where.append("and a." + CAOperation.FIELD_DATE + " > " + getForDateAfter() + " ");
        }

        if (!JadeStringUtil.isBlank(getForDateBefore())) {
            where.append("and a." + CAOperation.FIELD_DATE + " < " + getForDateBefore() + " ");
        }

        return where;
    }

    public void setForDateAfter(String forDateAfter) {
        this.forDateAfter = forDateAfter;
    }

    public void setForDateBefore(String forDateBefore) {
        this.forDateBefore = forDateBefore;
    }

    public void setForIdCompteCourantIn(ArrayList<String> forIdCompteCourantIn) {
        this.forIdCompteCourantIn = forIdCompteCourantIn;
    }

    public void setForIdPlan(String forIdPlan) {
        this.forIdPlan = forIdPlan;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    public void setForIdSectionIn(ArrayList<String> forIdSectionIn) {
        this.forIdSectionIn = forIdSectionIn;
    }

}
