package globaz.osiris.db.interet.util.autremontantfacture;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import java.util.ArrayList;

/**
 * -- retrouver le montant facturé sur la section et le compte courant qui n'est pas cot pers -- si montant < 0 =>
 * intérêt a controler et pas proprata -- a.idcompte in (select idrubrique as idcompte from WEBAVS.CAIMRSP) and ???
 * select sum(a.montant) as montant from webavs.caoperp a, webavs.carubrp b where a.idsection in (23307, 23308, 23309,
 * 23310) and (a.etat = 205002 or a.etat = 205004) and a.idtypeoperation like 'E%' and a.idcompte not in (188, 28) and
 * a.idcompte = b.idrubrique and b.naturerubrique not in (200004, 200005, 200006, 200007) and a.idcomptecourant not in
 * (1 , 13) and a.montant > 0 group by a.idcompteannexe
 * 
 * @author DDA
 * 
 */
public class CAAutreMontantFactureManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList forIdCompteCourantIn;
    private String forIdPlan;

    private ArrayList forIdRubriqueNotIn;

    private String forIdSection;

    private ArrayList forIdSectionIn;

    /**
     * see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "sum(a." + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT;
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
        StringBuffer where = new StringBuffer();

        where.append(getWhereIdSection());

        where.append("(a." + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE + " or a."
                + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_PROVISOIRE + ") and ");
        where.append("a." + CAOperation.FIELD_IDTYPEOPERATION + " like '" + APIOperation.CAECRITURE + "%' and ");
        where.append("a." + CAOperation.FIELD_IDCOMPTE + " = b.IDRUBRIQUE and ");
        where.append("a." + CAOperation.FIELD_MONTANT + " > 0 and ");

        where.append("(");

        // WARN (dda) Remplacé pour cause de lenteur ... Attention à la plage
        // des codes systèmes...doit rester inchangé !
        // where.append("b." + CARubrique.FIELD_NATURERUBRIQUE + " in (" +
        // APIRubrique.COMPTE_FINANCIER + "," + APIRubrique.COMPTE_COMPENSATION
        // + "," + APIRubrique.COMPTE_COURANT_DEBITEUR + "," +
        // APIRubrique.COMPTE_COURANT_CREANCIER + ") and ");
        where.append("b." + CARubrique.FIELD_NATURERUBRIQUE + " < " + APIRubrique.COMPTE_FINANCIER + " or b."
                + CARubrique.FIELD_NATURERUBRIQUE + " > " + APIRubrique.COMPTE_COMPENSATION);

        where.append(") and ");

        where.append(getWhereNotIdRubrique());

        where.append(getWhereIdCompteCourant());

        where.append("group by a." + CAOperation.FIELD_IDCOMPTEANNEXE + " ");

        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAAutreMontantFacture();
    }

    public ArrayList getForIdCompteCourantIn() {
        return forIdCompteCourantIn;
    }

    public String getForIdPlan() {
        return forIdPlan;
    }

    public ArrayList getForIdRubriqueNotIn() {
        return forIdRubriqueNotIn;
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public ArrayList getForIdSectionIn() {
        return forIdSectionIn;
    }

    private String getWhereIdCompteCourant() {
        if (getForIdCompteCourantIn() != null) {
            StringBuffer tmp = new StringBuffer("a." + CAOperation.FIELD_IDCOMPTECOURANT + " in (");

            for (int i = 0; i < getForIdCompteCourantIn().size(); i++) {
                tmp.append("" + getForIdCompteCourantIn().get(i));

                if (i < getForIdCompteCourantIn().size() - 1) {
                    tmp.append(", ");
                }
            }

            tmp.append(") ");

            return tmp.toString();
        } else {
            return "";
        }
    }

    private String getWhereIdSection() {
        if (getForIdSectionIn() != null) {
            StringBuffer tmp = new StringBuffer("a." + CAOperation.FIELD_IDSECTION + " in (");

            for (int i = 0; i < getForIdSectionIn().size(); i++) {
                tmp.append("" + getForIdSectionIn().get(i));

                if (i < getForIdSectionIn().size() - 1) {
                    tmp.append(", ");
                }
            }

            tmp.append(") and ");

            return tmp.toString();
        } else if (getForIdSection() != null) {
            return "a." + CAOperation.FIELD_IDSECTION + " = " + getForIdSection() + " and ";
        } else {
            return "";
        }
    }

    private String getWhereNotIdRubrique() {
        if (getForIdRubriqueNotIn() != null) {
            StringBuffer tmp = new StringBuffer("a." + CAOperation.FIELD_IDCOMPTE + " not in (");

            for (int i = 0; i < getForIdRubriqueNotIn().size(); i++) {
                tmp.append("" + getForIdRubriqueNotIn().get(i));

                if (i < getForIdRubriqueNotIn().size() - 1) {
                    tmp.append(", ");
                }
            }

            tmp.append(") and ");

            return tmp.toString();
        } else {
            return "";
        }
    }

    public void setForIdCompteCourantIn(ArrayList forIdCompteCourantIn) {
        this.forIdCompteCourantIn = forIdCompteCourantIn;
    }

    public void setForIdPlan(String forIdPlan) {
        this.forIdPlan = forIdPlan;
    }

    public void setForIdRubriqueNotIn(ArrayList forIdRubriqueNotIn) {
        this.forIdRubriqueNotIn = forIdRubriqueNotIn;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    public void setForIdSectionIn(ArrayList forIdSectionIn) {
        this.forIdSectionIn = forIdSectionIn;
    }

}
