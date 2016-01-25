package globaz.campus.db.annonces;

import globaz.campus.db.lots.GELots;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class GEAnnoncesManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String ORDER_BY_PAR_DEFAUT = GEAnnonces.FIELDNAME_ID_LOT + ", " + GEAnnonces.FIELDNAME_NUM_AVS
            + ", " + GEAnnonces.FIELDNAME_NUM_IMMATRICULATION_TRANSMIS + ", " + GEAnnonces.FIELDNAME_NOM + ", "
            + GEAnnonces.FIELDNAME_PRENOM;;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean forAnnoncesATraiterErreurOuArchivee = new Boolean(false);
    private Boolean forAnnoncesUniquement = new Boolean(false);
    private Boolean forAnnonceValideeOuComptabilisee = new Boolean(false);
    private String forCsEtatAnnonce = null;
    private String forIdAnnonce = null;
    private String forIdAnnonceParent = null;
    private String forIdDecision = null;
    private String forIdEtudiant = null;
    private String forIdLot = null;
    private String forIdTiersEcole = "";
    private Boolean forImputationsUniquement = new Boolean(false);
    private String forNomLike = null;
    private String forNumAvs = null;
    private String forNumAvsLike = null;
    private String forNumImmatriculationTransmis = null;
    private String forPrenomLike = null;
    private Boolean forSansPostgradesEtDoctorants = new Boolean(false);
    private boolean isForValidationAnnonce = false;
    private String orderBy = ORDER_BY_PAR_DEFAUT;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        if (isForValidationAnnonce()) {
            return _getCollection() + GEAnnonces.TABLE_NAME_ANNONCE + " LEFT OUTER JOIN " + _getCollection()
                    + GELots.TABLE_NAME_LOT + " ON (" + _getCollection() + GEAnnonces.TABLE_NAME_ANNONCE + "."
                    + GEAnnonces.FIELDNAME_ID_LOT + " = " + _getCollection() + GELots.TABLE_NAME_LOT + "."
                    + GELots.FIELDNAME_ID_LOT + ")";
        } else {
            return _getCollection() + GEAnnonces.TABLE_NAME_ANNONCE;
        }
    }

    @Override
    protected String _getOrder(BStatement statement) {
        if (isForValidationAnnonce()) {
            return " YBIETU DESC";
        } else {
            return orderBy;
        }
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isBlank(getForIdAnnonce())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_ID_TIERS_ECOLE + "="
                    + _dbWriteString(statement.getTransaction(), getForIdTiersEcole());
        }
        if (!JadeStringUtil.isBlank(getForIdAnnonce())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_ID_ANNONCE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonce());
        }
        if (!JadeStringUtil.isBlank(getForIdEtudiant())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_ID_ETUDIANT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdEtudiant());
        }
        if (!JadeStringUtil.isBlank(getForIdLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_ID_LOT + "=" + _dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }
        if (!JadeStringUtil.isBlank(getForIdDecision())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_ID_DECISION + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }
        if (!JadeStringUtil.isBlank(getForCsEtatAnnonce())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_ETAT_ANNONCE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForCsEtatAnnonce());
        }
        if (!JadeStringUtil.isBlank(getForNumAvs())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_NUM_AVS + "=" + _dbWriteString(statement.getTransaction(), getForNumAvs());
        }
        if (!JadeStringUtil.isBlank(getForNumImmatriculationTransmis())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_NUM_IMMATRICULATION_TRANSMIS + "="
                    + _dbWriteString(statement.getTransaction(), getForNumImmatriculationTransmis());
        }
        if (!JadeStringUtil.isBlank(getForNomLike())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER (" + GEAnnonces.FIELDNAME_NOM + ") like UPPER("
                    + _dbWriteString(statement.getTransaction(), "%" + getForNomLike() + "%") + ")";
        }
        if (!JadeStringUtil.isBlank(getForPrenomLike())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER (" + GEAnnonces.FIELDNAME_PRENOM + ") like UPPER("
                    + _dbWriteString(statement.getTransaction(), "%" + getForPrenomLike() + "%") + ")";
        }
        if (forAnnonceValideeOuComptabilisee.booleanValue()) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + GEAnnonces.FIELDNAME_ETAT_ANNONCE + "="
                    + _dbWriteNumeric(statement.getTransaction(), GEAnnonces.CS_ETAT_VALIDE) + " OR "
                    + GEAnnonces.FIELDNAME_ETAT_ANNONCE + "="
                    + _dbWriteNumeric(statement.getTransaction(), GEAnnonces.CS_ETAT_COMPTABILISE) + ")";
        }
        if (forAnnoncesATraiterErreurOuArchivee.booleanValue()) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(" + GEAnnonces.FIELDNAME_ETAT_ANNONCE + "="
                    + _dbWriteNumeric(statement.getTransaction(), GEAnnonces.CS_ETAT_A_TRAITER) + " OR "
                    + GEAnnonces.FIELDNAME_ETAT_ANNONCE + "="
                    + _dbWriteNumeric(statement.getTransaction(), GEAnnonces.CS_ETAT_ERREUR) + " OR "
                    + GEAnnonces.FIELDNAME_ETAT_ANNONCE + "="
                    + _dbWriteNumeric(statement.getTransaction(), GEAnnonces.CS_ETAT_ERREUR_ARCHIVEE) + ")";
        }
        if (!JadeStringUtil.isBlank(getForIdAnnonceParent())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_ID_ANNONCE_PARENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdAnnonceParent());
        }
        // Que les annonces
        if (getForAnnoncesUniquement().booleanValue()) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_IS_IMPUTATION + "="
                    + _dbWriteBoolean(statement.getTransaction(), new Boolean(false), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Que les imputations
        if (getForImputationsUniquement().booleanValue()) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_IS_IMPUTATION + "="
                    + _dbWriteBoolean(statement.getTransaction(), new Boolean(true), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }
        // Sans les postgrades et doctocant
        if (getForSansPostgradesEtDoctorants().booleanValue()) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEAnnonces.FIELDNAME_CODE_DOCTORANT + "=" + _dbWriteNumeric(statement.getTransaction(), "0");
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GEAnnonces();
    }

    public Boolean getForAnnoncesATraiterErreurOuArchivee() {
        return forAnnoncesATraiterErreurOuArchivee;
    }

    public Boolean getForAnnoncesUniquement() {
        return forAnnoncesUniquement;
    }

    public Boolean getForAnnonceValideeOuComptabilisee() {
        return forAnnonceValideeOuComptabilisee;
    }

    public String getForCsEtatAnnonce() {
        return forCsEtatAnnonce;
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public String getForIdAnnonceParent() {
        return forIdAnnonceParent;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdEtudiant() {
        return forIdEtudiant;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForIdTiersEcole() {
        return forIdTiersEcole;
    }

    public Boolean getForImputationsUniquement() {
        return forImputationsUniquement;
    }

    public String getForNomLike() {
        return forNomLike;
    }

    public String getForNumAvs() {
        return forNumAvs;
    }

    public String getForNumAvsLike() {
        return forNumAvsLike;
    }

    public String getForNumImmatriculationTransmis() {
        return forNumImmatriculationTransmis;
    }

    public String getForPrenomLike() {
        return forPrenomLike;
    }

    public Boolean getForSansPostgradesEtDoctorants() {
        return forSansPostgradesEtDoctorants;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isForValidationAnnonce() {
        return isForValidationAnnonce;
    }

    public void orderByAnnoncesImputations() {
        if (JadeStringUtil.isEmpty(getOrderBy())) {
            setOrderBy(GEAnnonces.FIELDNAME_IS_IMPUTATION + " DESC");
        } else {
            setOrderBy(getOrderBy() + ", " + GEAnnonces.FIELDNAME_IS_IMPUTATION + " DESC");
        }
    }

    public void setForAnnoncesATraiterErreurOuArchivee(Boolean forAnnoncesATraiterErreurOuArchivee) {
        this.forAnnoncesATraiterErreurOuArchivee = forAnnoncesATraiterErreurOuArchivee;
    }

    public void setForAnnoncesUniquement(Boolean forAnnoncesUniquement) {
        this.forAnnoncesUniquement = forAnnoncesUniquement;
    }

    public void setForAnnonceValideeOuComptabilisee(Boolean forAnnonceValideeOuComptabilisee) {
        this.forAnnonceValideeOuComptabilisee = forAnnonceValideeOuComptabilisee;
    }

    public void setForCsEtatAnnonce(String forCsEtatAnnonce) {
        this.forCsEtatAnnonce = forCsEtatAnnonce;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    public void setForIdAnnoncesParent(String forIdAnnonceParent) {
        this.forIdAnnonceParent = forIdAnnonceParent;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdEtudiant(String forIdEtudiant) {
        this.forIdEtudiant = forIdEtudiant;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForIdTiersEcole(String forIdTiersEcole) {
        this.forIdTiersEcole = forIdTiersEcole;
    }

    public void setForImputationsUniquement(Boolean forImputationsUniquement) {
        this.forImputationsUniquement = forImputationsUniquement;
    }

    public void setForNomLike(String forNomLike) {
        this.forNomLike = forNomLike;
    }

    public void setForNumAvs(String forNumAvs) {
        this.forNumAvs = forNumAvs;
    }

    public void setForNumAvsLike(String forNumAvsLike) {
        this.forNumAvsLike = forNumAvsLike;
    }

    public void setForNumImmatriculationTransmis(String forNumImmatriculationTransmis) {
        this.forNumImmatriculationTransmis = forNumImmatriculationTransmis;
    }

    public void setForPrenomLike(String forPrenomLike) {
        this.forPrenomLike = forPrenomLike;
    }

    public void setForSansPostgradesEtDoctorants(Boolean forSansPostgradesEtDoctorants) {
        this.forSansPostgradesEtDoctorants = forSansPostgradesEtDoctorants;
    }

    public void setForValidationAnnonce(boolean isForValidationAnnonce) {
        this.isForValidationAnnonce = isForValidationAnnonce;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
