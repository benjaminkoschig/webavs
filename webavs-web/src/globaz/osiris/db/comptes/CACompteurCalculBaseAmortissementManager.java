package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIRubrique;
import java.io.Serializable;
import java.util.List;

public class CACompteurCalculBaseAmortissementManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCompteAnnexe = new String();
    private List<String> forInAnnees;

    @Override
    protected String _getSql(BStatement statement) {

        StringBuilder sql = new StringBuilder(
                "SELECT cpt.ANNEE as anneeAmortissement, sum(cpt.CUMULCOTISATION) as cumulCotisationsForAnnee FROM "
                        + _getCollection() + CACompteur.TABLE_CACPTRP + " cpt inner join " + _getCollection()
                        + CARubrique.TABLE_CARUBRP + " ru  on cpt." + CACompteur.FIELD_IDRUBRIQUE + " = ru."
                        + CARubrique.FIELD_IDRUBRIQUE + " where cpt." + CACompteur.FIELD_IDCOMPTEANNEXE + " = "
                        + getForIdCompteAnnexe() + " and ru." + CARubrique.FIELD_NATURERUBRIQUE + " IN ( "
                        + APIRubrique.AMORTISSEMENT + ", " + APIRubrique.RECOUVREMENT + ")");

        if ((getForInAnnees() != null) && (getForInAnnees().size() > 0)) {

            sql.append(" and cpt.").append(CACompteur.FIELD_ANNEE).append(" IN ( ");

            for (int i = 0; i < getForInAnnees().size(); i++) {

                sql.append(getForInAnnees().get(i));

                if (i != (getForInAnnees().size() - 1)) {
                    sql.append(",");
                }
            }

            sql.append(")");
        }

        sql.append(" group by cpt.").append(CACompteur.FIELD_ANNEE).append(" order by cpt.")
                .append(CACompteur.FIELD_ANNEE);

        return sql.toString();
    }

    /**
     * @see BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CACompteurCalculBaseAmortissement();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 10:56:32)
     * 
     * @return String
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    public List<String> getForInAnnees() {
        return forInAnnees;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 10:56:32)
     * 
     * @param newForIdCompteAnnexe
     *            String
     */
    public void setForIdCompteAnnexe(String newForIdCompteAnnexe) {
        forIdCompteAnnexe = newForIdCompteAnnexe;
    }

    public void setForInAnnees(List<String> forInAnnees) {
        this.forInAnnees = forInAnnees;
    }
}
