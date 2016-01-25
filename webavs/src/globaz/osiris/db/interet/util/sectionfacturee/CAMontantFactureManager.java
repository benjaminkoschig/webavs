package globaz.osiris.db.interet.util.sectionfacturee;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import java.util.ArrayList;

/**
 * select sum(a.montant) as summontant from webavs.caoperp a, webavs.cacptap b where a.idcompteannexe = b.idcompteannexe
 * and b.idexternerole = '163.1248' and b.idrole = 517002 and a.idcompte in (28,188) and a.anneecotisation = 2002 and
 * (a.etat = 205002 or a.etat = 205004) and a.idtypeoperation like 'E%' and idsection in (23307, 23308, 23309, 23310,
 * 305708) a.idcomptecourant in (1 , 13) group by a.idcompteannexe
 * 
 * @author DDA
 * 
 */
public class CAMontantFactureManager extends CASectionFactureeManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList forIdCompteCourantIn;
    private ArrayList forIdSectionIn;

    /**
     * see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "sum(a." + CAOperation.FIELD_MONTANT + ") as " + CAOperation.FIELD_MONTANT;
    }

    /**
     * see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer();
        where.append(getWhere());
        where.append(getWhereIdSection());
        where.append(getWhereIdCompteCourant());
        where.append(" group by a." + CAOperation.FIELD_IDCOMPTEANNEXE);

        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAMontantFacture();
    }

    public ArrayList getForIdCompteCourantIn() {
        return forIdCompteCourantIn;
    }

    public ArrayList getForIdSectionIn() {
        return forIdSectionIn;
    }

    private String getWhereIdCompteCourant() {
        if (getForIdCompteCourantIn() != null) {
            StringBuffer tmp = new StringBuffer(" and a." + CAOperation.FIELD_IDCOMPTECOURANT + " in (");

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
        StringBuffer tmp = new StringBuffer(" and a." + CAOperation.FIELD_IDSECTION + " in (");

        for (int i = 0; i < getForIdSectionIn().size(); i++) {
            tmp.append("" + getForIdSectionIn().get(i));

            if (i < getForIdSectionIn().size() - 1) {
                tmp.append(", ");
            }
        }

        tmp.append(") ");

        return tmp.toString();
    }

    public void setForIdCompteCourantIn(ArrayList forIdCompteCourantIn) {
        this.forIdCompteCourantIn = forIdCompteCourantIn;
    }

    public void setForIdSectionIn(ArrayList forIdSectionIn) {
        this.forIdSectionIn = forIdSectionIn;
    }

}
