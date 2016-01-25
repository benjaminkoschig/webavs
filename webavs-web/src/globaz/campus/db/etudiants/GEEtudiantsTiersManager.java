package globaz.campus.db.etudiants;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.util.Vector;

public class GEEtudiantsTiersManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final Vector getIdsEtNomsEcole(BSession session) throws Exception {
        TIAdministrationViewBean admin = null;
        Vector ecole = new Vector();
        TIAdministrationManager adminMng = new TIAdministrationManager();
        adminMng.setSession(session);
        adminMng.setForGenreAdministration(GEEtudiants.CS_ADMINISTRATION_ECOLE);
        adminMng.orderByDesignation();
        adminMng.changeManagerSize(0);
        adminMng.find();
        ecole.add(new String[] { "", "" });
        for (int i = 0; i < adminMng.size(); i++) {
            admin = (TIAdministrationViewBean) adminMng.getEntity(i);
            ecole.add(new String[] { admin.getIdTiersAdministration(),
                    (admin.getDesignation1() + " " + admin.getDesignation2()).trim() });
        }

        return ecole;
    }

    private String forIdEtudiant = null;
    private String forIdTiersEcole = null;
    private String forIdTiersEtudiant = null;
    private String forNomLike = null;
    private String forNumAvs = null;
    private String forNumAvsLike = null;
    private String forNumImmatriculation = null;
    private String forPrenomLike = null;
    private String fromDateNaissance = null;

    private String fromNumImmatriculation = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String orderBy = GEEtudiants.FIELDNAME_NUM_IMMATRICULATION;

    @Override
    protected String _getFields(BStatement statement) {
        return GEEtudiants.FIELDNAME_ID_ETUDIANT + ", " + GEEtudiants.FIELDNAME_ID_TIERS_ETUDIANT + ", "
                + GEEtudiants.FIELDNAME_ID_TIERS_ECOLE + ", " + GEEtudiants.FIELDNAME_NUM_IMMATRICULATION + ", "
                + GEEtudiantsTiers.FIELDNAME_NOM_ETUDIANT + " AS " + GEEtudiantsTiers.ALIASNAME_NOM_ETUDIANT + ", "
                + GEEtudiantsTiers.FIELDNAME_PRENOM_ETUDIANT + " AS " + GEEtudiantsTiers.ALIASNAME_PRENOM_ETUDIANT
                + ", " + GEEtudiantsTiers.FIELDNAME_NOM_ECOLE + " AS " + GEEtudiantsTiers.ALIASNAME_NOM_ECOLE + ", "
                + GEEtudiantsTiers.FIELDNAME_NUM_AVS;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + GEEtudiants.TABLE_NAME_ETUDIANT + " INNER JOIN " + _getCollection()
                + GEEtudiantsTiers.TABLE_NAME_TIERS + " AS " + GEEtudiantsTiers.ALIAS_NAME_TIERS_A + " ON "
                + _getCollection() + GEEtudiants.TABLE_NAME_ETUDIANT + "." + GEEtudiants.FIELDNAME_ID_TIERS_ETUDIANT
                + "=" + GEEtudiantsTiers.ALIAS_NAME_TIERS_A + ".HTITIE INNER JOIN " + _getCollection()
                + GEEtudiantsTiers.TABLE_NAME_PERSONNE_AVS + " ON " + _getCollection()
                + GEEtudiantsTiers.TABLE_NAME_PERSONNE_AVS + ".HTITIE=" + GEEtudiantsTiers.ALIAS_NAME_TIERS_A
                + ".HTITIE INNER JOIN " + _getCollection() + GEEtudiantsTiers.TABLE_NAME_TIERS + " AS "
                + GEEtudiantsTiers.ALIAS_NAME_TIERS_B + " ON " + _getCollection() + GEEtudiants.TABLE_NAME_ETUDIANT
                + "." + GEEtudiants.FIELDNAME_ID_TIERS_ECOLE + "=" + GEEtudiantsTiers.ALIAS_NAME_TIERS_B + ".HTITIE";
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isBlank(getForIdEtudiant())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiants.FIELDNAME_ID_ETUDIANT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdEtudiant());
        }
        if (!JadeStringUtil.isBlank(getForIdTiersEtudiant())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiants.FIELDNAME_ID_TIERS_ETUDIANT + ">="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTiersEtudiant());
        }
        if (!JadeStringUtil.isBlank(getForIdTiersEcole())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiants.FIELDNAME_ID_TIERS_ECOLE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTiersEcole());
        }
        if (!JadeStringUtil.isBlank(getForNumImmatriculation())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiants.FIELDNAME_NUM_IMMATRICULATION + "="
                    + _dbWriteString(statement.getTransaction(), getForNumImmatriculation());
        }
        if (!JadeStringUtil.isBlank(getFromNumImmatriculation())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiants.FIELDNAME_NUM_IMMATRICULATION + ">="
                    + _dbWriteString(statement.getTransaction(), getFromNumImmatriculation());
        }
        if (!JadeStringUtil.isBlank(getForNumAvs())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiantsTiers.FIELDNAME_NUM_AVS + "="
                    + _dbWriteString(statement.getTransaction(), getForNumAvs());
        }
        if (getForNumAvsLike().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += GEEtudiantsTiers.FIELDNAME_NUM_AVS + " LIKE "
                    + _dbWriteString(statement.getTransaction(), getForNumAvsLike() + "%");
        }
        if (!JadeStringUtil.isBlank(getForNomLike())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER (" + GEEtudiantsTiers.FIELDNAME_NOM_ETUDIANT + ") like UPPER("
                    + _dbWriteString(statement.getTransaction(), "%" + getForNomLike() + "%") + ")";
        }
        if (!JadeStringUtil.isBlank(getForPrenomLike())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER (" + GEEtudiantsTiers.FIELDNAME_PRENOM_ETUDIANT + ") like UPPER("
                    + _dbWriteString(statement.getTransaction(), "%" + getForPrenomLike() + "%") + ")";
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GEEtudiantsTiers();
    }

    public String getForIdEtudiant() {
        return forIdEtudiant;
    }

    public String getForIdTiersEcole() {
        return forIdTiersEcole;
    }

    public String getForIdTiersEtudiant() {
        return forIdTiersEtudiant;
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

    public String getForNumImmatriculation() {
        return forNumImmatriculation;
    }

    public String getForPrenomLike() {
        return forPrenomLike;
    }

    public String getFromDateNaissance() {
        return fromDateNaissance;
    }

    public String getFromNumImmatriculation() {
        return fromNumImmatriculation;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setForIdEtudiant(String forIdEtudiant) {
        this.forIdEtudiant = forIdEtudiant;
    }

    public void setForIdTiersEcole(String forIdTiersEcole) {
        this.forIdTiersEcole = forIdTiersEcole;
    }

    public void setForIdTiersEtudiant(String forIdTiersEtudiant) {
        this.forIdTiersEtudiant = forIdTiersEtudiant;
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

    public void setForNumImmatriculation(String forNumImmatriculation) {
        this.forNumImmatriculation = forNumImmatriculation;
    }

    public void setForPrenomLike(String forPrenomLike) {
        this.forPrenomLike = forPrenomLike;
    }

    public void setFromDateNaissance(String fromDateNaissance) {
        this.fromDateNaissance = fromDateNaissance;
    }

    public void setFromNumImmatriculation(String fromNumImmatriculation) {
        this.fromNumImmatriculation = fromNumImmatriculation;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
