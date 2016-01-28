package globaz.osiris.db.interet.util.sectionfacturee;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAOperation;
import java.util.ArrayList;

/**
 * select a.idsection, a.idcomptecourant from webavs.caoperp a, webavs.cacptap b where a.idcompteannexe =
 * b.idcompteannexe and b.idexternerole = '163.1248' and b.idrole = 517002 and a.idcompte in (188, 28) and
 * a.anneecotisation = 2002 and (a.etat = 205002 or a.etat = 205004) and a.idtypeoperation like 'E%' group by
 * a.idsection, a.idcomptecourant
 * 
 * @author DDA
 * 
 */
public class CASectionFactureeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeCotisation;
    private String forIdExterneRole;

    private String forIdRole;

    private ArrayList forIdRubriqueIn;

    /**
     * see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "sum(a." + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT + ", a."
                + CAOperation.FIELD_IDSECTION + ", a." + CAOperation.FIELD_IDCOMPTECOURANT;
    }

    /**
     * see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAOperation.TABLE_CAOPERP + " a, " + _getCollection() + CACompteAnnexe.TABLE_CACPTAP
                + " b ";
    }

    /**
     * see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer();
        where.append(getWhere());
        where.append(getGroupBy());

        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASectionFacturee();
    }

    public String getForAnneeCotisation() {
        return forAnneeCotisation;
    }

    public String getForIdExterneRole() {
        return forIdExterneRole;
    }

    public String getForIdRole() {
        return forIdRole;
    }

    public ArrayList getForIdRubriqueIn() {
        return forIdRubriqueIn;
    }

    private String getGroupBy() {
        StringBuffer groupBy = new StringBuffer();
        groupBy.append(" group by a." + CAOperation.FIELD_IDSECTION + ", a." + CAOperation.FIELD_IDCOMPTECOURANT);

        return groupBy.toString();
    }

    protected String getWhere() {
        StringBuffer where = new StringBuffer();
        where.append("a." + CAOperation.FIELD_IDCOMPTEANNEXE + " = b." + CACompteAnnexe.FIELD_IDCOMPTEANNEXE + " and ");
        where.append("b." + CACompteAnnexe.FIELD_IDEXTERNEROLE + " = '" + getForIdExterneRole() + "' and ");
        where.append("b." + CACompteAnnexe.FIELD_IDROLE + " = " + getForIdRole() + " and ");
        where.append(getWhereIdRubrique());
        where.append("a." + CAOperation.FIELD_ANNEECOTISATION + " = " + getForAnneeCotisation() + " and ");
        where.append("(a." + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_COMPTABILISE + " or a."
                + CAOperation.FIELD_ETAT + " = " + APIOperation.ETAT_PROVISOIRE + ") and ");
        where.append("a." + CAOperation.FIELD_IDTYPEOPERATION + " >= '" + APIOperation.CAECRITURE + "' ");

        return where.toString();
    }

    private String getWhereIdRubrique() {
        if (getForIdRubriqueIn() != null) {
            StringBuffer tmp = new StringBuffer("a." + CAOperation.FIELD_IDCOMPTE + " in (");

            for (int i = 0; i < getForIdRubriqueIn().size(); i++) {
                tmp.append("" + getForIdRubriqueIn().get(i));

                if (i < getForIdRubriqueIn().size() - 1) {
                    tmp.append(", ");
                }
            }

            tmp.append(") and ");

            return tmp.toString();
        } else {
            return "";
        }
    }

    public void setForAnneeCotisation(String forAnneeCotisation) {
        this.forAnneeCotisation = forAnneeCotisation;
    }

    public void setForIdExterneRole(String forIdExterneRole) {
        this.forIdExterneRole = forIdExterneRole;
    }

    public void setForIdRole(String forIdRole) {
        this.forIdRole = forIdRole;
    }

    public void setForIdRubriqueIn(ArrayList forIdRubriqueIn) {
        this.forIdRubriqueIn = forIdRubriqueIn;
    }

}
