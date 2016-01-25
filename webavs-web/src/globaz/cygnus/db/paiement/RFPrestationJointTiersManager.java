/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.paiement;

import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import ch.globaz.pegasus.business.constantes.IPCDroits;

/**
 * 
 * @author fha
 * @revision jje
 */
public class RFPrestationJointTiersManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeQD = "";
    private String forCsEtatDecision = "";
    private String forCsSexe = "";

    private String forDateNaissance = "";
    private String forIdDecision = "";
    private String forIdGestionnaire = "";
    private String forIdLot = "";
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
    public RFPrestationJointTiersManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer fields = new StringBuffer();

        // infos prestations

        fields.append(RFPrestation.FIELDNAME_ID_PRESTATION).append(",");
        fields.append(RFPrestation.FIELDNAME_DATE_MOIS_ANNEE).append(",");
        fields.append(RFPrestation.FIELDNAME_MONTANT_TOTAL).append(",");
        fields.append(RFPrestation.FIELDNAME_ETAT).append(",");
        fields.append(RFPrestation.FIELDNAME_ID_DECISION).append(",");
        fields.append(RFPrestation.FIELDNAME_ID_LOT).append(",");
        fields.append(RFPrestation.FIELDNAME_ID_ADRESSE_PAIEMENT).append(",");
        fields.append(RFPrestation.FIELDNAME_TYPE_PRESTATION).append(",");
        fields.append(RFPrestation.FIELDNAME_REFERENCE_PAIEMENT).append(",");
        fields.append(RFPrestation.FIELDNAME_REMBOURSEMENT_REQUERANT).append(",");
        fields.append(RFPrestation.FIELDNAME_REMBOURSEMENT_CONJOINT).append(",");
        fields.append(RFPrestation.FIELDNAME_IS_RI).append(",");
        fields.append(RFPrestation.FIELDNAME_IS_LAPRAMS).append(",");

        // infos bénéficiaire

        fields.append(
                RFPrestationJointTiers.aliasTipavspBeneficiaire + "." + RFPrestationJointTiers.FIELDNAME_NUM_AVS + " "
                        + RFPrestationJointTiers.FIELD_NSSBENEFICIAIRE).append(",");
        fields.append(
                RFPrestationJointTiers.aliasTiperspBeneficiaire + "." + RFPrestationJointTiers.FIELDNAME_DATEDECES
                        + " " + RFPrestationJointTiers.FIELD_DATEDECESBENEFICIAIRE).append(",");
        fields.append(
                RFPrestationJointTiers.aliasTiperspBeneficiaire + "." + RFPrestationJointTiers.FIELDNAME_SEXE + " "
                        + RFPrestationJointTiers.FIELD_CSSEXEBENEFICIAIRE).append(",");
        fields.append(
                RFPrestationJointTiers.aliasTitierpBeneficiaire + "." + RFPrestationJointTiers.FIELDNAME_NOM + " "
                        + RFPrestationJointTiers.FIELD_NOMBENEFICIAIRE).append(",");
        fields.append(
                RFPrestationJointTiers.aliasTitierpBeneficiaire + "." + RFPrestationJointTiers.FIELDNAME_PRENOM + " "
                        + RFPrestationJointTiers.FIELD_PRENOMBENEFICIAIRE).append(",");
        fields.append(
                RFPrestationJointTiers.aliasTitierpBeneficiaire + "." + RFPrestationJointTiers.FIELDNAME_ID_TIERS_TI
                        + " " + RFPrestationJointTiers.FIELD_IDTIERSBENEFICIAIRE).append(",");

        fields.append(
                RFPrestationJointTiers.aliasTitierpBeneficiaire + "." + RFPrestationJointTiers.FIELDNAME_NATIONALITE
                        + " " + RFPrestationJointTiers.FIELD_CSNATIONALITEPAIEMENTBENEFICIAIRE).append(",");

        fields.append(
                RFPrestationJointTiers.aliasTiperspBeneficiaire + "." + RFPrestationJointTiers.FIELDNAME_DATENAISSANCE
                        + " " + RFPrestationJointTiers.FIELD_DATENAISSANCEBENEFICIAIRE).append(",");

        // détail paiement

        fields.append(
                RFPrestationJointTiers.aliasTitierpDomaineStandard + "." + RFPrestationJointTiers.FIELDNAME_NOM + " "
                        + RFPrestationJointTiers.FIELD_NOMPAIEMENTDOMAINESTANDARD).append(",");
        fields.append(
                RFPrestationJointTiers.aliasTitierpDomaineStandard + "." + RFPrestationJointTiers.FIELDNAME_PRENOM
                        + " " + RFPrestationJointTiers.FIELD_PRENOMPAIEMENTDOMAINESTANDARD).append(",");
        fields.append(
                RFPrestationJointTiers.aliasTipavspDomaineStandard + "." + RFPrestationJointTiers.FIELDNAME_NUM_AVS
                        + " " + RFPrestationJointTiers.FIELD_NSSPAIEMENTDOMAINESTANDARD).append(",");
        fields.append(
                RFPrestationJointTiers.aliasTiperspDomaineStandard + "."
                        + RFPrestationJointTiers.FIELDNAME_DATENAISSANCE + " "
                        + RFPrestationJointTiers.FIELD_DATENAISSANCEPAIEMENTDOMAINESTANDARD).append(",");
        fields.append(
                RFPrestationJointTiers.aliasTiperspDomaineStandard + "." + RFPrestationJointTiers.FIELDNAME_DATEDECES
                        + " " + RFPrestationJointTiers.FIELD_DATEDECESPAIEMENTDOMAINESTANDARD).append(",");
        fields.append(
                RFPrestationJointTiers.aliasTiperspDomaineStandard + "." + RFPrestationJointTiers.FIELDNAME_SEXE + " "
                        + RFPrestationJointTiers.FIELD_CSSEXEPAIEMENTDOMAINESTANDARD).append(",");

        fields.append(RFPrestationJointTiers.aliasTitierpDomaineStandard + "."
                + RFPrestationJointTiers.FIELDNAME_NATIONALITE + " "
                + RFPrestationJointTiers.FIELD_CSNATIONALITEPAIEMENTSTANDARD);// .append(",");

        // domaine rente paiement

        /*
         * fields.append( RFPrestationJointTiers.aliasTitierpDomaineRente + "." + RFPrestationJointTiers.FIELDNAME_NOM +
         * " " + RFPrestationJointTiers.FIELD_NOMPAIEMENTDOMAINERENTE).append(","); fields.append(
         * RFPrestationJointTiers.aliasTitierpDomaineRente + "." + RFPrestationJointTiers.FIELDNAME_PRENOM + " " +
         * RFPrestationJointTiers.FIELD_PRENOMPAIEMENTDOMAINERENTE).append(","); fields.append(
         * RFPrestationJointTiers.aliasTipavspDomaineRente + "." + RFPrestationJointTiers.FIELDNAME_NUM_AVS + " " +
         * RFPrestationJointTiers.FIELD_NSSPAIEMENTDOMAINERENTE).append(","); fields.append(
         * RFPrestationJointTiers.aliasTiperspDomaineRente + "." + RFPrestationJointTiers.FIELDNAME_DATENAISSANCE + " "
         * + RFPrestationJointTiers.FIELD_DATENAISSANCEPAIEMENTDOMAINERENTE).append(","); fields.append(
         * RFPrestationJointTiers.aliasTiperspDomaineRente + "." + RFPrestationJointTiers.FIELDNAME_DATEDECES + " " +
         * RFPrestationJointTiers.FIELD_DATEDECESPAIEMENTDOMAINERENTE).append(",");
         * fields.append(RFPrestationJointTiers.aliasTiperspDomaineRente + "." + RFPrestationJointTiers.FIELDNAME_SEXE +
         * " " + RFPrestationJointTiers.FIELD_CSSEXEPAIEMENTDOMAINERENTE);// .append(",");
         */

        /*
         * fields.append(RFPrestationJointTiers.aliasTiperspDomaineRente + "." +
         * RFPrestationJointTiers.FIELDNAME_NATIONALITE + " " +
         * RFPrestationJointTiers.FIELD_CSNATIONALITEPAIEMENTRENTE);
         */

        return fields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFPrestationJointTiers.createFromClause(_getCollection()));

            if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
                from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS "
                        + IPRTiers.TABLE_AVS_HIST + " ON (" + RFPrestationJointTiers.aliasTipavspBeneficiaire + "."
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

        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }

        // on prend que le requerant
        sqlWhere.append("(");
        sqlWhere.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION);
        sqlWhere.append(" = ");
        sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
        sqlWhere.append(" OR ");
        sqlWhere.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION);
        sqlWhere.append(" IS NULL ");
        sqlWhere.append(")");

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

            sqlWhere.append(RFPrestationJointTiers.aliasTitierpBeneficiaire + ".");
            sqlWhere.append(RFPrestationJointTiers.FIELDNAME_NOM_FOR_SEARCH);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationJointTiers.aliasTitierpBeneficiaire + ".");
            sqlWhere.append(RFPrestationJointTiers.FIELDNAME_PRENOM_FOR_SEARCH);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(),
                    PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationJointTiers.aliasTiperspBeneficiaire + ".");
            sqlWhere.append(RFPrestationJointTiers.FIELDNAME_DATENAISSANCE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
        }

        if (!JadeStringUtil.isEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestationJointTiers.aliasTiperspBeneficiaire + ".");
            sqlWhere.append(RFPrestationJointTiers.FIELDNAME_SEXE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatDecision)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFPrestation.FIELDNAME_ETAT);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsEtatDecision));
        }

        if (!JadeStringUtil.isEmpty(forIdLot)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestation.FIELDNAME_ID_LOT);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdLot));
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFPrestationJointTiers();
    }

    public String getForAnneeQD() {
        return forAnneeQD;
    }

    public String getForCsEtatDecision() {
        return forCsEtatDecision;
    }

    public String getForCsSexe() {
        return forCsSexe;
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

    public String getForIdLot() {
        return forIdLot;
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

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
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

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
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
