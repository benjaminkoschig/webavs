package globaz.corvus.db.demandes;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAbstractManagerHierarchique;

public class RERenteAccordeeJointSituationFamilialeManager extends PRAbstractManagerHierarchique {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forAnnee = "";

    private String forMois = "";
    private transient String fromClause = null;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            fromClause = RERenteAccordeeJointSituationFamiliale.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @param (BStatement)statement
     * 
     * @return la valeur courante de l'attribut whereClause selon les différents paramètres choisis
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForMois()) && !JadeStringUtil.isIntegerEmpty(getForAnnee())) {

            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REDemandeRente.FIELDNAME_DATE_DEBUT);
            whereClause.append("=");

            if (getForMois().length() < 2) {
                whereClause.append(getForAnnee() + "0" + getForMois() + "01");
            } else {
                whereClause.append(getForAnnee() + getForMois() + "01");
            }
        }

        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }

        whereClause.append("(" + IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_DATEDECES);
        whereClause.append(" is null ");
        whereClause.append(" OR ");
        whereClause.append(IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_DATEDECES);
        whereClause.append("=");
        whereClause.append("0)");

        return whereClause.toString();

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteAccordeeJointSituationFamiliale();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForMois() {
        return forMois;
    }

    @Override
    public String getHierarchicalOrderBy() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public String getOrderByDefaut() {
        // Auto-generated method stub
        return null;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForMois(String forMois) {
        this.forMois = forMois;
    }

}