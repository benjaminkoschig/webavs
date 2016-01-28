package globaz.osiris.db.interet.analytique;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.interets.CARubriqueSoumiseInteret;

/**
 * 
 //SELECT c.IDSECTION, c.MONTANT, INTERET.MONTANTINTERET, c.IDCOMPTECOURANT, c.IDCOMPTE, c.FANCAI //FROM
 * WEBAVSCIAM.CAOPERP c, WEBAVSCIAM.CAIMRSP d, (
 * 
 * getInteretSubTable()
 * 
 * ) INTERET //WHERE c.IDSECTION = INTERET.IDSECTION //AND c.ETAT IN (205002, 205004) AND c.IDCOMPTE = d.IDRUBRIQUE
 * //ORDER BY c.IDSECTION, c.FANCAI, c.IDCOMPTECOURANT, c.IDCOMPTE
 * 
 * @author DDA
 * 
 */
public abstract class CAInteretAnalytiqueManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String SUBTABLE_INTERET = "INTERET";

    private String forDateDebut;
    private String forDateFin;

    private boolean sumMontantSoumis = false;

    /**
     * see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT c.").append(CAOperation.FIELD_IDSECTION).append(", ");

        if (!isSumMontantSoumis()) {
            sql.append("sum (c.").append(CAOperation.FIELD_MONTANT).append(") as ").append(CAOperation.FIELD_MONTANT)
                    .append(", ").append(CAInteretAnalytiqueManager.SUBTABLE_INTERET).append(".")
                    .append(CAInteretAnalytique.FIELD_MONTANTINTERET).append(", c.").append(CAOperation.FIELD_IDCOMPTE)
                    .append(", c.").append(CAOperation.FIELD_IDCAISSEPROFESSIONNELLE);
        } else {
            sql.append("sum (c.").append(CAOperation.FIELD_MONTANT).append(") as ").append(CAOperation.FIELD_MONTANT);
        }

        sql.append(" FROM ").append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(" c, ")
                .append(_getCollection()).append(CARubriqueSoumiseInteret.TABLE_CAIMRSP).append(" d, ");
        sql.append(" ( ");
        getInteretSubTable(statement, sql);
        sql.append(" ) ").append(CAInteretAnalytiqueManager.SUBTABLE_INTERET);
        sql.append(" WHERE ");
        sql.append("c.").append(CAOperation.FIELD_IDSECTION).append(" = ")
                .append(CAInteretAnalytiqueManager.SUBTABLE_INTERET).append(".").append(CAOperation.FIELD_IDSECTION);
        sql.append(" AND ");
        sql.append("c.").append(CAOperation.FIELD_ETAT).append(" IN (").append(APIOperation.ETAT_COMPTABILISE)
                .append(", ").append(APIOperation.ETAT_PROVISOIRE).append(")");
        sql.append(" AND ");
        sql.append("c.").append(CAOperation.FIELD_IDCOMPTE).append(" = d.")
                .append(CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE);

        if (isSumMontantSoumis()) {
            sql.append(" GROUP BY c.").append(CAOperation.FIELD_IDSECTION);
        } else {
            sql.append(" GROUP BY c.").append(CAOperation.FIELD_IDSECTION).append(", ")
                    .append(CAInteretAnalytiqueManager.SUBTABLE_INTERET).append(".")
                    .append(CAInteretAnalytique.FIELD_MONTANTINTERET).append(", c.").append(CAOperation.FIELD_IDCOMPTE)
                    .append(", c.").append(CAOperation.FIELD_IDCAISSEPROFESSIONNELLE);
        }

        sql.append(" ORDER BY c.").append(CAOperation.FIELD_IDSECTION);

        if (!isSumMontantSoumis()) {
            sql.append(", c.").append(CAOperation.FIELD_IDCAISSEPROFESSIONNELLE).append(", c.")
                    .append(CAOperation.FIELD_IDCOMPTE);
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAInteretAnalytique();
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * Return la sous-table contenant la liste des sections facturés ainsi que le montant de l'intérêt.
     * 
     * @param statement
     * @param sql
     */
    protected abstract void getInteretSubTable(BStatement statement, StringBuffer sql);

    public boolean isSumMontantSoumis() {
        return sumMontantSoumis;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setSumMontantSoumis(boolean sumMontantSoumis) {
        this.sumMontantSoumis = sumMontantSoumis;
    }

}
