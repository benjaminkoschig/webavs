/**
 * 
 */
package globaz.external.tucana.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author sel
 * 
 */
public class CABouclementAlfaNewManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forAssuranceFFPP = false;
    private String forDatePeriodeBegin;
    private String forDatePeriodeEnd;
    // private String forExclureCanton;
    private String forGenre = null;
    private String forIdExterneRubrique = null;
    private Boolean groupByAnneeCotisation = false;

    @Override
    protected String _getSql(BStatement statement) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(op.montant) MONTANT, sum(op.masse) MASSE, csCa.PCOUID");
        if (getGroupByAnneeCotisation()) {
            sql.append(", op." + CAOperation.FIELD_ANNEECOTISATION);
        }
        sql.append(" from ").append(_getCollection()).append("caoperp op");
        sql.append(" inner join ").append(_getCollection()).append("cajourp jo on (op.idjournal=jo.idjournal)");
        sql.append(" inner join ").append(_getCollection()).append("afassup ass on (op.idcompte = ass.mbirub)");
        sql.append(" inner join ").append(_getCollection()).append("carubrp ru on (op.idcompte=ru.idrubrique)");
        sql.append(" inner join ").append(_getCollection())
                .append("fwcoup csCa on (csCa.pcosid = ass.mbtcan and csCa.plaide = 'F')");
        sql.append(" where jo.datevaleurcg between ").append(getForDatePeriodeBegin()).append(" and ")
                .append(getForDatePeriodeEnd());

        sql.append(" and ass.mbttyp");
        if (getForAssuranceFFPP()) {
            sql.append(" IN (");
            sql.append(CodeSystem.TYPE_ASS_FFPP);
            sql.append(",");
            sql.append(CodeSystem.TYPE_ASS_FFPP_MASSE);
            sql.append(")");
        } else {
            sql.append("=").append(CodeSystem.TYPE_ASS_COTISATION_AF);
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdExterneRubrique())) {
            sql.append(" and ");
            sql.append("ru." + CARubrique.FIELD_IDEXTERNE);
            sql.append("='");
            sql.append(getForIdExterneRubrique());
            sql.append("'");
        }

        if (!JadeStringUtil.isBlank(getForGenre())) {
            sql.append(" and ass.mbtgen=").append(getForGenre()); // 801001 PAR ; 801002 IND
        }
        sql.append(" and op.etat=205002");
        sql.append(" and ru.idexterne <> '5500.4030.0000' and  ru.idexterne <> '5500.4030.6000'");

        // if (this.forAssuranceFFPP && !JadeStringUtil.isBlank(this.forExclureCanton)) {
        // sql.append(" and csCa.pcosid not in (");
        // sql.append(this.forExclureCanton);
        // sql.append(")");
        // }

        sql.append(" group by csCa.PCOUID");

        if (getGroupByAnneeCotisation()) {
            sql.append(", op." + CAOperation.FIELD_ANNEECOTISATION);
        }

        return sql.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CABouclementAlfa();
    }

    /**
     * @return the forAssuranceFFPP
     */
    public Boolean getForAssuranceFFPP() {
        return forAssuranceFFPP;
    }

    /**
     * @return the forDatePeriodeBegin
     */
    public String getForDatePeriodeBegin() {
        return forDatePeriodeBegin;
    }

    /**
     * @return the forDatePeriodeEnd
     */
    public String getForDatePeriodeEnd() {
        return forDatePeriodeEnd;
    }

    // /**
    // * @return the forExclureCanton
    // */
    // public String getForExclureCanton() {
    // return this.forExclureCanton;
    // }

    /**
     * @return the forGenre
     */
    public String getForGenre() {
        return forGenre;
    }

    /**
     * @return the forIdExterneRubrique
     */
    public String getForIdExterneRubrique() {
        return forIdExterneRubrique;
    }

    /**
     * @return the groupByAnneeCotisation
     */
    public Boolean getGroupByAnneeCotisation() {
        return groupByAnneeCotisation;
    }

    /**
     * @param forAssuranceFFPP
     *            the forAssuranceFFPP to set
     */
    public void setForAssuranceFFPP(Boolean forAssuranceFFPP) {
        this.forAssuranceFFPP = forAssuranceFFPP;
    }

    /**
     * @param forDatePeriodeBegin
     *            the forDatePeriodeBegin to set
     */
    public void setForDatePeriodeBegin(String forDatePeriodeBegin) {
        this.forDatePeriodeBegin = forDatePeriodeBegin;
    }

    // /**
    // * @param forExclureCanton
    // * the forExclureCanton to set
    // */
    // public void setForExclureCanton(String forExclureCanton) {
    // this.forExclureCanton = forExclureCanton;
    // }

    /**
     * @param forDatePeriodeEnd
     *            the forDatePeriodeEnd to set
     */
    public void setForDatePeriodeEnd(String forDatePeriodeEnd) {
        this.forDatePeriodeEnd = forDatePeriodeEnd;
    }

    /**
     * @param forGenre
     *            the forGenre to set
     */
    public void setForGenre(String forGenre) {
        this.forGenre = forGenre;
    }

    /**
     * @param forIdExterneRubrique
     *            the forIdExterneRubrique to set
     */
    public void setForIdExterneRubrique(String forIdExterneRubrique) {
        this.forIdExterneRubrique = forIdExterneRubrique;
    }

    /**
     * @param groupByAnneeCotisation
     *            the groupByAnneeCotisation to set
     */
    public void setGroupByAnneeCotisation(Boolean groupByAnneeCotisation) {
        this.groupByAnneeCotisation = groupByAnneeCotisation;
    }

}
