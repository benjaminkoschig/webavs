/**
 *
 */
package globaz.helios.db.classifications;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGSolde;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author sel
 * 
 *         Manager faisant la jointure entre les tables Comptes (CGCOMTP) et Soldes (CGSOLDP)
 * 
 *         Utilisé dans les listes : Balance des comtpes, bilan, pertes et profits, plan comptable, analyse budgetaire
 */
public class CGExtendedCompteSoldeManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FIELDS = "CGCLCOP.idclasseCompte, idexterne, idnature, iddomaine, idgenre, codeisomonnaie, ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String ON = " ON ";

    /**
     * Détermine ou on va cherche le montant du budget ; soit dans la table principale CGSOLDP soit dans la jointure sur
     * la sous requete.
     */
    private String aliasProvenanceBudget;
    protected String forCodePeriode;
    protected String forIdCentreCharge;
    protected String forIdClassification;
    protected String forIdDomaine;
    protected String forIdExerciceComptable;
    protected String forIdMandat;
    protected String forNotCodePeriode;
    protected String forNumeroCompteMax;
    protected String forNumeroCompteMin;
    protected Boolean inclurePeriode = true;
    protected Boolean withPeriode = true;

    public CGExtendedCompteSoldeManager() {
        this(true);
    }

    public CGExtendedCompteSoldeManager(Boolean withPeriode) {
        super();
        this.withPeriode = withPeriode;
        if (withPeriode) {
            aliasProvenanceBudget = "cgsoldeTemp.";
        } else {
            // initialise la valeur par défaut ; table principale.
            aliasProvenanceBudget = _getCollection() + CGSolde.TABLE_NAME + ".";
        }

    }

    @Override
    protected String _getFields(BStatement statement) {
        String schema = _getCollection();

        StringBuilder fields = new StringBuilder();

        fields.append("sum(doit) doit, sum(avoir) avoir, sum(solde) solde, sum(doitprovisoire) doitprovisoire, sum(avoirprovisoire) avoirprovisoire, sum(soldeprovisoire) soldeprovisoire, sum(doitmonnaie) doitmonnaie, sum(avoirmonnaie) avoirmonnaie, sum(soldemonnaie) soldemonnaie, sum(doitprovimonnaie) doitprovisoiremonnaie, sum(avoirprovimonnaie) avoirprovisoiremonnaie, sum(soldeprovimonnaie) soldeprovisoiremonnaie,");
        fields.append(schema).append(CGExtendedCompteSoldeManager.FIELDS);
        fields.append(aliasProvenanceBudget).append("BUDGET,");
        fields.append(schema).append("CGCOMTP.idcompte, ");
        fields.append(schema).append("cgplanp.libelleFr LIBFRPLANCOMPTABLE, ");
        fields.append(schema).append("cgplanp.libelleDe LIBDEPLANCOMPTABLE, ");
        fields.append(schema).append("cgplanp.libelleIt LIBITPLANCOMPTABLE");
        return fields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sqlFrom = new StringBuilder();
        String SCHEMA = _getCollection();

        sqlFrom.append(SCHEMA).append(CGSolde.TABLE_NAME);

        sqlFrom.append(CGExtendedCompteSoldeManager.INNER_JOIN).append(SCHEMA).append("CGCOMTP")
                .append(CGExtendedCompteSoldeManager.ON).append(SCHEMA).append("CGCOMTP.IDCOMPTE").append("=")
                .append(SCHEMA).append(CGSolde.TABLE_NAME).append(".IDCOMPTE AND ").append(SCHEMA)
                .append(CGSolde.TABLE_NAME).append(".IDEXERCOMPTABLE=").append(getForIdExerciceComptable());

        if (!JadeStringUtil.isBlank(getForIdDomaine()) && !CGCompte.CS_COMPTE_TOUS.equals(getForIdDomaine())) {
            sqlFrom.append(" AND ").append(SCHEMA).append("CGCOMTP").append(".iddomaine=").append(getForIdDomaine());
        }

        sqlFrom.append(addInnerjoin(
                SCHEMA,
                "CGCPGRP",
                (new StringBuilder(SCHEMA)).append("CGCPGRP.IDCOMPTE").append("=").append(SCHEMA)
                        .append("CGCOMTP.IDCOMPTE")));

        sqlFrom.append(CGExtendedCompteSoldeManager.INNER_JOIN).append(SCHEMA).append("CGPLANP")
                .append(CGExtendedCompteSoldeManager.ON).append(SCHEMA).append("CGPLANP.IDCOMPTE").append("=")
                .append(SCHEMA).append("CGCOMTP.IDCOMPTE AND ").append(SCHEMA).append("CGPLANP.IDEXERCOMPTABLE=")
                .append(SCHEMA).append(CGSolde.TABLE_NAME).append(".IDEXERCOMPTABLE");
        sqlFrom.append(CGExtendedCompteSoldeManager.INNER_JOIN).append(SCHEMA).append("CGEXERP")
                .append(CGExtendedCompteSoldeManager.ON).append(SCHEMA).append("CGEXERP.idExerComptable").append("=")
                .append(SCHEMA).append("CGPLANP.idExerComptable");
        sqlFrom.append(CGExtendedCompteSoldeManager.INNER_JOIN).append(SCHEMA).append("CGCLCOP")
                .append(CGExtendedCompteSoldeManager.ON).append(SCHEMA).append("CGCPGRP.idClasseCompte").append("=")
                .append(SCHEMA).append("CGCLCOP.idClasseCompte AND IDCLASSIFICATION=").append(getForIdClassification());

        if (withPeriode) {
            sqlFrom.append(CGExtendedCompteSoldeManager.INNER_JOIN).append(SCHEMA).append("CGPERIP")
                    .append(CGExtendedCompteSoldeManager.ON).append(SCHEMA).append("CGPERIP.IDPERIODECOMPTABLE")
                    .append("=").append(SCHEMA).append(CGSolde.TABLE_NAME).append(".idperiodecomptable");

            if (!JadeStringUtil.isBlankOrZero(getForCodePeriode())) {
                sqlFrom.append(" AND ").append(SCHEMA).append("cgperip.CODE").append(inclurePeriode ? "<='" : "='")
                        .append(getForCodePeriode()).append("'");
            }

            if (!JadeStringUtil.isBlankOrZero(getForNotCodePeriode())) {
                sqlFrom.append(" AND ").append(SCHEMA).append("cgperip.CODE").append("<>'")
                        .append(getForNotCodePeriode()).append("'");
            }

            sqlFrom.append(CGExtendedCompteSoldeManager.INNER_JOIN)
                    .append("(select idcompte, sum(budget) budget from ").append(SCHEMA).append(CGSolde.TABLE_NAME)
                    .append(" group by idcompte) cgsoldeTemp on cgsoldeTemp.idcompte=").append(SCHEMA)
                    .append(CGSolde.TABLE_NAME).append(".idcompte ");
        }

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuilder order = new StringBuilder();
        order.append("idExterne");
        return order.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String SCHEMA = _getCollection();

        StringBuilder sqlWhere = new StringBuilder();

        if (!JadeStringUtil.isBlank(getForIdMandat())) {
            addCondition(sqlWhere, SCHEMA + CGSolde.TABLE_NAME + ".idMandat=" + getForIdMandat());
        }

        if (!JadeStringUtil.isBlank(getForIdCentreCharge())) {
            addCondition(sqlWhere, SCHEMA + CGSolde.TABLE_NAME + ".IDCENTRECHARGE=" + getForIdCentreCharge());
        }

        if (!JadeStringUtil.isBlank(getForNumeroCompteMin())) {
            addCondition(sqlWhere, _getCollection() + "CGPLANP.idExterne >= '" + getForNumeroCompteMin() + "' ");
        }
        if (!JadeStringUtil.isBlank(getForNumeroCompteMax())) {
            addCondition(sqlWhere, _getCollection() + "CGPLANP.idExterne <= '" + getForNumeroCompteMax() + "' ");
        }

        if (!withPeriode) {
            addCondition(sqlWhere, _getCollection() + "CGSOLDP.idperiodecomptable=0 ");
        }

        // Group by
        sqlWhere.append(" GROUP BY ");
        sqlWhere.append(SCHEMA).append(CGExtendedCompteSoldeManager.FIELDS).append(aliasProvenanceBudget)
                .append("BUDGET,").append(SCHEMA).append("CGCOMTP.idcompte, ").append(SCHEMA)
                .append("cgplanp.libelleFr, ").append(SCHEMA).append("cgplanp.libelleDe, ").append(SCHEMA)
                .append("cgplanp.libelleIt");

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CGExtendedCompteSolde();
    }

    /**
     * Permet l'ajout d'une condition dans la clause WHERE. <br>
     * 
     * @param sqlWhere
     * @param condition
     *            à ajouter au where
     */
    protected void addCondition(StringBuilder sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
        sqlWhere.append(condition);
    }

    /**
     * @param sqlFrom
     * @param SCHEMA
     */
    private String addInnerjoin(String SCHEMA, String table, StringBuilder condition) {
        StringBuilder sql = new StringBuilder();
        sql.append(CGExtendedCompteSoldeManager.INNER_JOIN).append(SCHEMA).append(table)
                .append(CGExtendedCompteSoldeManager.ON).append(condition);

        return sql.toString();
    }

    /**
     * @return the forCodePeriode
     */
    public String getForCodePeriode() {
        return forCodePeriode;
    }

    /**
     * @return the forIdCentreCharge
     */
    public String getForIdCentreCharge() {
        return forIdCentreCharge;
    }

    /**
     * @return the forIdClassification
     */
    public String getForIdClassification() {
        return forIdClassification;
    }

    /**
     * @return the forIdDomaine
     */
    public String getForIdDomaine() {
        return forIdDomaine;
    }

    /**
     * @return the forIdExerciceComptable
     */
    public String getForIdExerciceComptable() {
        return forIdExerciceComptable;
    }

    /**
     * @return the forIdMandat
     */
    public String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * @return the forNotCodePeriode
     */
    public String getForNotCodePeriode() {
        return forNotCodePeriode;
    }

    /**
     * @return the forNumeroCompteMax
     */
    public String getForNumeroCompteMax() {
        return forNumeroCompteMax;
    }

    /**
     * @return the forNumeroCompteMin
     */
    public String getForNumeroCompteMin() {
        return forNumeroCompteMin;
    }

    /**
     * @return the inclurePeriode
     */
    public Boolean getInclurePeriode() {
        return inclurePeriode;
    }

    /**
     * @return the withPeriode
     */
    public Boolean getWithPeriode() {
        return withPeriode;
    }

    /**
     * @param forCodePeriode
     *            the forCodePeriode to set
     */
    public void setForCodePeriode(String forCodePeriode) {
        this.forCodePeriode = forCodePeriode;
    }

    /**
     * @param forIdCentreCharge
     *            the forIdCentreCharge to set
     */
    public void setForIdCentreCharge(String forIdCentreCharge) {
        this.forIdCentreCharge = forIdCentreCharge;
    }

    /**
     * @param forIdClassification
     *            the forIdClassification to set
     */
    public void setForIdClassification(String forIdClassification) {
        this.forIdClassification = forIdClassification;
    }

    /**
     * @param forIdDomaine
     *            the forIdDomaine to set
     */
    public void setForIdDomaine(String forIdDomaine) {
        this.forIdDomaine = forIdDomaine;
    }

    /**
     * @param forIdExerciceComptable
     *            the forIdExerciceComptable to set
     */
    public void setForIdExerciceComptable(String forIdExerciceComptable) {
        this.forIdExerciceComptable = forIdExerciceComptable;
    }

    /**
     * @param forIdMandat
     *            the forIdMandat to set
     */
    public void setForIdMandat(String forIdMandat) {
        this.forIdMandat = forIdMandat;
    }

    /**
     * @param forNotCodePeriode
     *            the forNotCodePeriode to set
     */
    public void setForNotCodePeriode(String forNotCodePeriode) {
        this.forNotCodePeriode = forNotCodePeriode;
    }

    /**
     * @param forNumeroCompteMax
     *            the forNumeroCompteMax to set
     */
    public void setForNumeroCompteMax(String forNumeroCompteMax) {
        this.forNumeroCompteMax = forNumeroCompteMax;
    }

    /**
     * @param forNumeroCompteMin
     *            the forNumeroCompteMin to set
     */
    public void setForNumeroCompteMin(String forNumeroCompteMin) {
        this.forNumeroCompteMin = forNumeroCompteMin;
    }

    /**
     * @param inclurePeriode
     *            the inclurePeriode to set
     */
    public void setInclurePeriode(Boolean inclurePeriode) {
        this.inclurePeriode = inclurePeriode;
    }

    /**
     * @param withPeriode
     *            the withPeriode to set
     */
    public void setWithPeriode(Boolean withPeriode) {
        this.withPeriode = withPeriode;
    }
}
