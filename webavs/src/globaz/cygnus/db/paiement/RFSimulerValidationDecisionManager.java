/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.db.paiement;

import globaz.corvus.db.lots.RELot;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.interfaces.tiers.IPRTiers;
import java.util.Set;

/**
 * 
 * @author fha
 */
public class RFSimulerValidationDecisionManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeQD = "";
    private String forCsEtatDecision = "";
    private Set<String> forCsEtatsLot = null;
    private String forCsLotOwner = "";
    private String forCsSexe = "";
    private String forCsTypeRelation = "";
    private String forDateNaissance = "";
    private String forIdDecision = "";
    private String forIdGestionnaireDecision = "";
    private String forIdGestionnaireLot = "";
    private String forIdLot = "";
    private String forNumeroDecision = "";
    private String forOrderBy = "";
    private String forPrepareFrom = "";
    private String forPreparePar = "";
    private String forValideFrom = "";
    private String forValidePar = "";
    private transient String fromClause = null;
    private String idTiersAdressePaiement = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFSimulerValidationDecisionManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFSimulerValidationDecision.createFromClause(_getCollection()));

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

        sqlOrder.append(RFSimulerValidationDecision.FIELDNAME_NOM + ", " + RFSimulerValidationDecision.FIELDNAME_PRENOM);

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
        // String schema = this._getCollection();

        if ((forCsEtatsLot != null) && (forCsEtatsLot.size() > 0)) {

            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RELot.FIELDNAME_ETAT);
            sqlWhere.append(" IN (");

            int inc = 0;
            for (String id : forCsEtatsLot) {
                if (!JadeStringUtil.isEmpty(id)) {
                    inc++;
                    if (forCsEtatsLot.size() != inc) {
                        sqlWhere.append(id + ",");
                    } else {
                        sqlWhere.append(id + ") ");
                    }
                }
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsLotOwner)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RELot.FIELDNAME_LOT_OWNER);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsLotOwner));
        }

        if (!JadeStringUtil.isIntegerEmpty(idTiersAdressePaiement)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPrestation.FIELDNAME_ID_ADRESSE_PAIEMENT);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), idTiersAdressePaiement));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdLot)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFLot.FIELDNAME_ID_LOT_RFM);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdLot));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdGestionnaireLot)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFLot.FIELDNAME_ID_GESTIONNAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGestionnaireLot));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdGestionnaireDecision)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFDecision.FIELDNAME_ID_GESTIONNAIRE);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteString(statement.getTransaction(), forIdGestionnaireDecision));
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsTypeRelation)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFAssQdDossier.FIELDNAME_TYPE_RELATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeRelation));
        }

        return sqlWhere.toString();

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Auto-generated method stub
        return new RFSimulerValidationDecision();
    }

    public String getForAnneeQD() {
        return forAnneeQD;
    }

    public String getForCsEtatDecision() {
        return forCsEtatDecision;
    }

    public Set<String> getForCsEtatsLot() {
        return forCsEtatsLot;
    }

    public String getForCsLotOwner() {
        return forCsLotOwner;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForCsTypeRelation() {
        return forCsTypeRelation;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdGestionnaireDecision() {
        return forIdGestionnaireDecision;
    }

    public String getForIdGestionnaireLot() {
        return forIdGestionnaireLot;
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

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
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

    public void setForCsEtatsLot(Set<String> forCsEtatsLot) {
        this.forCsEtatsLot = forCsEtatsLot;
    }

    public void setForCsLotOwner(String forCsLotOwner) {
        this.forCsLotOwner = forCsLotOwner;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForCsTypeRelation(String forCsTypeRelation) {
        this.forCsTypeRelation = forCsTypeRelation;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdGestionnaireDecision(String forIdGestionnaireDecision) {
        this.forIdGestionnaireDecision = forIdGestionnaireDecision;
    }

    public void setForIdGestionnaireLot(String forIdGestionnaire) {
        forIdGestionnaireLot = forIdGestionnaire;
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

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
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
