/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.paiement;

import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * 
 * @author fha
 */
public class RFPrestationAccordeeJointTiersManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeQD = "";
    private String forCsEtatDecision = "";
    private String forCsEtatsPrestationsAccordees = "";
    private String forCsGenresPrestationsAccordees = "";
    private String forCsSexe = "";
    private String forCsTypesPrestationsAccordees = "";
    private String forDateNaissance = "";

    private String forIdDecision = "";
    private String forIdGestionnaire = "";
    private String forNumeroDecision = "";
    private String forOrderBy = "";
    private String forPrepareFrom = "";

    private String forPreparePar = "";
    private String forValideFrom = "";
    private String forValidePar = "";

    private transient String fromClause = null;
    private String likeNom = "";
    private String likeNumeroAVS = "";

    private String likeNumeroAVSNNSS = "";

    private String likePrenom = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFPrestationAccordeeJointTiersManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFPrestationAccordeeJointTiers.createFromClause(_getCollection()));

            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
                from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS "
                        + IPRTiers.TABLE_AVS_HIST + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "."
                        + IPRTiers.FIELD_TI_IDTIERS + " = " + IPRTiers.TABLE_AVS_HIST + "." + IPRTiers.FIELD_TI_IDTIERS
                        + ")");
            }

            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();
        if (!JadeStringUtil.isEmpty(forOrderBy)) {
            sqlOrder.append(forOrderBy);
        }
        return sqlOrder.toString();
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

        if (!JadeStringUtil.isEmpty(forIdGestionnaire)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_GESTIONNAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGestionnaire));
        }

        if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {

            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(PRNSSUtil.getWhereNSS(_getCollection(), getLikeNumeroAVS(), getLikeNumeroAVSNNSS()));
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + "TITIERP." + RFPrestationAccordeeJointTiers.FIELDNAME_NOM_FOR_SEARCH);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema + "TITIERP." + RFPrestationAccordeeJointTiers.FIELDNAME_PRENOM_FOR_SEARCH);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestationAccordeeJointTiers.FIELDNAME_DATENAISSANCE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
        }

        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestationAccordeeJointTiers.FIELDNAME_SEXE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
        }

        if (!JadeStringUtil.isEmpty(forIdDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if (!JadeStringUtil.isEmpty(forNumeroDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_NUMERO_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forNumeroDecision));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDecision.TABLE_NAME + "." + RFDecision.FIELDNAME_ETAT_DECISION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatDecision));
        }

        if (!JadeStringUtil.isEmpty(forPreparePar)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_PREPARE_PAR);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forPreparePar));
        }

        if (!JadeStringUtil.isEmpty(forValidePar)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_VALIDE_PAR);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forValidePar));
        }

        if (!JadeStringUtil.isEmpty(forAnneeQD)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ANNEE_QD);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forAnneeQD));
        }

        if (!JadeStringUtil.isEmpty(forValideFrom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_DATE_VALIDATION);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forValideFrom));
        }

        if (!JadeStringUtil.isEmpty(forPrepareFrom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_DATE_PREPARATION);
            sqlWhere.append(" >= ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forPrepareFrom));
        }

        if (!JadeStringUtil.isEmpty(forCsGenresPrestationsAccordees)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFPrestationAccordee.TABLE_NAME_RFM_ACCORDEE + "."
                    + RFPrestationAccordee.FIELDNAME_CS_TYPE_RFA + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsGenresPrestationsAccordees));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatsPrestationsAccordees)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_CS_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatsPrestationsAccordees));
        }

        if (!JadeStringUtil.isEmpty(forCsTypesPrestationsAccordees)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES + "."
                    + REPrestationsAccordees.FIELDNAME_GENRE_PRESTATION_ACCORDEE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsTypesPrestationsAccordees));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public String getForAnneeQD() {
        return forAnneeQD;
    }

    public String getForCsEtatDecision() {
        return forCsEtatDecision;
    }

    public String getForCsEtatsPrestationsAccordees() {
        return forCsEtatsPrestationsAccordees;
    }

    public String getForCsGenresPrestationsAccordees() {
        return forCsGenresPrestationsAccordees;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsTypesPrestationsAccordees() {
        return forCsTypesPrestationsAccordees;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public String getForNumeroDecision() {
        return forNumeroDecision;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getForPrepareFrom() {
        return forPrepareFrom;
    }

    public String getForPreparePar() {
        return forPreparePar;
    }

    public String getForValideFrom() {
        return forValideFrom;
    }

    public String getForValidePar() {
        return forValidePar;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    // ~ Methods
    // ---------------------------------------------------------------------------------------------------
    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setForAnneeQD(String forAnneeQD) {
        this.forAnneeQD = forAnneeQD;
    }

    public void setForCsEtatDecision(String forCsEtatDecision) {
        this.forCsEtatDecision = forCsEtatDecision;
    }

    public void setForCsEtatsPrestationsAccordees(String forCsEtatsPrestationsAccordees) {
        this.forCsEtatsPrestationsAccordees = forCsEtatsPrestationsAccordees;
    }

    public void setForCsGenresPrestationsAccordees(String forCsGenresPrestationsAccordees) {
        this.forCsGenresPrestationsAccordees = forCsGenresPrestationsAccordees;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForCsTypesPrestationsAccordees(String forCsTypesPrestationsAccordees) {
        this.forCsTypesPrestationsAccordees = forCsTypesPrestationsAccordees;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    public void setForNumeroDecision(String forNumeroDecision) {
        this.forNumeroDecision = forNumeroDecision;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setForPrepareFrom(String forPrepareFrom) {
        this.forPrepareFrom = forPrepareFrom;
    }

    public void setForPreparePar(String forPreparePar) {
        this.forPreparePar = forPreparePar;
    }

    public void setForValideFrom(String forValideFrom) {
        this.forValideFrom = forValideFrom;
    }

    public void setForValidePar(String forValidePar) {
        this.forValidePar = forValidePar;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

}
