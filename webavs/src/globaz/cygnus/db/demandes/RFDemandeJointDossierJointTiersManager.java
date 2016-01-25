/*
 * Créé le 7 janvier 2009
 */
package globaz.cygnus.db.demandes;

import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.cygnus.utils.RFUtils;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRAbstractManagerHierarchique;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author jje
 */
public class RFDemandeJointDossierJointTiersManager extends PRAbstractManagerHierarchique/* BManager */{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeSousTypeDeSoinList = "";
    private String codeTypeDeSoinList = "";
    private String forCsEtatDemande = "";
    private String forCsSexe = "";
    private String forCsStatutDemande = "";
    private String forDateNaissance = "";
    private Boolean forDevis = Boolean.FALSE;
    private String forIdDecision = "";
    private String forIdDemande = "";
    private String forIdDossiers = "";
    private String forIdGestionnaire = "";
    private String forIdQdPrincipale = "";
    private String forIdTiers = "";
    private String forNumeroDecision = "";
    private String forOrderBy = "";
    private transient String fromClause = null;
    private String fromDateDebut = "";
    private String fromDateFin = "";
    private boolean hasJointureMotifDeRefusDepQd = false;
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemandeJointDossierJointTiersManager
     */
    public RFDemandeJointDossierJointTiersManager(boolean hasJointureMotifDeRefusDepQd) {
        super();
        wantCallMethodBeforeFind(true);
        this.hasJointureMotifDeRefusDepQd = hasJointureMotifDeRefusDepQd;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        try {
            if (fromClause == null) {
                StringBuffer from = new StringBuffer(RFDemandeJointDossierJointTiers.createFromClause(
                        _getCollection(),
                        hasJointureMotifDeRefusDepQd ? RFUtils.getIdsMotifDeRefusSysteme(getSession(), null).get(
                                IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)[0] : ""));

                if ((!JadeStringUtil.isBlankOrZero(likeNumeroAVSNNSS) && !JadeStringUtil.isEmpty(getLikeNumeroAVS()))) {
                    from.append(" LEFT JOIN " + _getCollection() + IPRTiers.TABLE_AVS_HIST + " AS "
                            + IPRTiers.TABLE_AVS_HIST + " ON (" + _getCollection() + IPRTiers.TABLE_AVS + "."
                            + IPRTiers.FIELD_TI_IDTIERS + " = " + IPRTiers.TABLE_AVS_HIST + "."
                            + IPRTiers.FIELD_TI_IDTIERS + ")");
                }

                fromClause = from.toString();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
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

            sqlWhere.append(schema
                    + RFDemandeJointDossierJointTiers.TABLE_TIERS
                    + "."
                    + RFDemandeJointDossierJointTiers.FIELDNAME_NOM_FOR_SEARCH
                    + " LIKE "
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(likeNom) + "%"));
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema
                    + RFDemandeJointDossierJointTiers.TABLE_TIERS
                    + "."
                    + RFDemandeJointDossierJointTiers.FIELDNAME_PRENOM_FOR_SEARCH
                    + " LIKE "
                    + this._dbWriteString(statement.getTransaction(),
                            PRStringUtils.upperCaseWithoutSpecialChars(likePrenom) + "%"));
        }

        if (!JAUtil.isDateEmpty(forDateNaissance)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDemandeJointDossierJointTiers.TABLE_PERSONNE + "."
                    + RFDemandeJointDossierJointTiers.FIELDNAME_DATENAISSANCE + "="
                    + this._dbWriteDateAMJ(statement.getTransaction(), forDateNaissance));
        }

        if (!JAUtil.isDateEmpty(forCsSexe)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDemandeJointDossierJointTiers.TABLE_PERSONNE + "."
                    + RFDemandeJointDossierJointTiers.FIELDNAME_SEXE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsSexe));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDemande.TABLE_NAME + "." + RFDemande.FIELDNAME_CS_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsEtatDemande));
        }

        if (!JadeStringUtil.isEmpty(forCsStatutDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + RFDemande.TABLE_NAME + "." + RFDemande.FIELDNAME_CS_STATUT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forCsStatutDemande));
        }

        if (!JadeStringUtil.isEmpty(fromDateDebut)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(" + RFDemande.FIELDNAME_DATE_DEBUT_TRAITEMENT + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDateDebut) + ")");
        }

        if (!JadeStringUtil.isEmpty(fromDateFin)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append("(" + RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT + "<="
                    + this._dbWriteDateAMJ(statement.getTransaction(), fromDateFin) + " AND ("
                    + RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT + " <> 0 AND  " + RFDemande.FIELDNAME_DATE_FIN_TRAITEMENT
                    + " IS NOT NULL))");
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiers)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(PRDemande.FIELDNAME_IDTIERS);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdTiers));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdGestionnaire)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_GESTIONNAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGestionnaire));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDecision)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdQdPrincipale)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_QD_PRINCIPALE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdQdPrincipale));
        }

        if (!JadeStringUtil.isIntegerEmpty(forNumeroDecision)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_NUMERO_DECISION);
            sqlWhere.append(" LIKE ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forNumeroDecision + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDemande)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDemande.FIELDNAME_ID_DEMANDE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdDemande));
        }

        /*
         * if (!JadeStringUtil.isIntegerEmpty(codeTypeDeSoinList) && !JadeStringUtil.isIntegerEmpty(codeSousTypeDeSoin))
         * { if (sqlWhere.length() > 0) { sqlWhere.append(" AND "); }
         * 
         * sqlWhere.append(" ( " + RFTypeDeSoin.FIELDNAME_CODE); sqlWhere.append(" = ");
         * sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), codeTypeDeSoinList));
         * 
         * sqlWhere.append(" AND ");
         * 
         * sqlWhere.append(RFSousTypeDeSoin.FIELDNAME_CODE); sqlWhere.append(" = ");
         * sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), codeSousTypeDeSoin)); sqlWhere.append(" ) ");
         * 
         * }else
         */

        if (!JadeStringUtil.isIntegerEmpty(codeTypeDeSoinList)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(" ( " + RFTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), codeTypeDeSoinList));
            sqlWhere.append(" ) ");
        }

        if (forDevis) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(" ( " + RFTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = '");
            sqlWhere.append(IRFCodeTypesDeSoins.CODE_TYPE_DE_SOIN_DEVIS_DENTAIRE);
            sqlWhere.append("' ) ");
        }

        if (!JadeStringUtil.isIntegerEmpty(codeSousTypeDeSoinList)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(" ( " + RFSousTypeDeSoin.FIELDNAME_CODE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), codeSousTypeDeSoinList));
            sqlWhere.append(" ) ");
        }

        return sqlWhere.toString();
    }

    /**
     * Définition de l'entité (RFDemandeJointDossierJointTiers)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDemandeJointDossierJointTiers();
    }

    public String getCodeSousTypeDeSoinList() {
        return codeSousTypeDeSoinList;
    }

    public String getCodeTypeDeSoinList() {
        return codeTypeDeSoinList;
    }

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsStatutDemande() {
        return forCsStatutDemande;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public Boolean getForDevis() {
        return forDevis;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdDossiers() {
        return forIdDossiers;
    }

    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public String getForIdQdPrincipale() {
        return forIdQdPrincipale;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForNumeroDecision() {
        return forNumeroDecision;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public String getFromDateFin() {
        return fromDateFin;
    }

    @Override
    public String getHierarchicalOrderBy() {
        // TODO Auto-generated method stub
        return null;
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

    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setCodeSousTypeDeSoinList(String codeSousTypeDeSoinList) {
        this.codeSousTypeDeSoinList = codeSousTypeDeSoinList;
    }

    public void setCodeTypeDeSoinList(String codeTypeDeSoinList) {
        this.codeTypeDeSoinList = codeTypeDeSoinList;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForCsStatutDemande(String forCsStatutDemande) {
        this.forCsStatutDemande = forCsStatutDemande;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForDevis(Boolean forDevis) {
        this.forDevis = forDevis;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdDossiers(String forIdDossiers) {
        this.forIdDossiers = forIdDossiers;
    }

    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    public void setForIdQdPrincipale(String forIdQdPrincipale) {
        this.forIdQdPrincipale = forIdQdPrincipale;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForNumeroDecision(String forNumeroDecision) {
        this.forNumeroDecision = forNumeroDecision;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public void setFromDateFin(String fromDateFin) {
        this.fromDateFin = fromDateFin;
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
