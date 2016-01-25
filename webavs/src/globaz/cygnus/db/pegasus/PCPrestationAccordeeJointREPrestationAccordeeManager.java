/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.pegasus;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.paiement.RFPrestationAccordee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * 
 * @author fha
 */
public class PCPrestationAccordeeJointREPrestationAccordeeManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String forAnnee = "";
    private String forCsSourcePrestationsAccordees = "";
    private String forInIdTiers = "";

    private String forOrderBy = "";

    private transient String fromClause = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public PCPrestationAccordeeJointREPrestationAccordeeManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(
                    PCPrestationAccordeeJointREPrestationAccordee.createFromClause(_getCollection()));

            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        // if (!JadeStringUtil.isBlankOrZero(this.forInIdTiers)) {
        sqlWhere.append(String.format("(%s IN (%s))", REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE,
                forInIdTiers));
        // }

        if (!JadeStringUtil.isEmpty(forCsSourcePrestationsAccordees)) {
            sqlWhere.append(String.format(" AND (%s = %s)", RFPrestationAccordee.FIELDNAME_CS_SOURCE,
                    forCsSourcePrestationsAccordees));
        }

        if (!JadeStringUtil.isBlankOrZero(forAnnee)) {

            final String dateDebutAnnee = "01.01." + forAnnee;
            final String dateFinAnnee = "31.12." + forAnnee;

            // request SQL à composer :
            // AND ((RFDemande.dateDebut >= 01.01.anneeDemande AND RFDemande.dateDebut<=31.12.anneeDemande) OR
            // (RFDemande.dateFacture >= 01.01.anneeDemande AND RFDemande.dateFacture<=31.12.anneeDemande))
            sqlWhere.append(String.format(
                    " AND ((%3$s >= %1$s AND %3$s <= %2$s) OR (%3$s IS NULL AND %4$s >= %1$s AND %4$s <= %2$s))",
                    this._dbWriteDateAMJ(statement.getTransaction(), dateDebutAnnee),
                    this._dbWriteDateAMJ(statement.getTransaction(), dateFinAnnee),
                    REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT, REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new PCPrestationAccordeeJointREPrestationAccordee();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForCsSourcePrestationsAccordees() {
        return forCsSourcePrestationsAccordees;
    }

    public String getForInIdTiers() {
        return forInIdTiers;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromClause() {
        return fromClause;
    }

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------
    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForCsSourcePrestationsAccordees(String forCsSourcePrestationsAccordees) {
        this.forCsSourcePrestationsAccordees = forCsSourcePrestationsAccordees;
    }

    public void setForInIdTiers(String forInIdTiers) {
        this.forInIdTiers = forInIdTiers;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
