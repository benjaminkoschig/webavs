package globaz.lynx.db.facture;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXFactureManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_TIERS = "TITIERP";

    private boolean checkExtourne = false;
    private ArrayList<String> forCsEtatIn;
    private String forCsEtatOperation;
    private ArrayList<String> forCsTypeOperationIn;
    private String forDateEcheance;
    private String forDateEcheanceInferieure;
    private String forDateFacture;
    private String forDateFactureInferieure;
    private String forIdFournisseur;
    private String forIdJournal;
    private String forIdOperationNot;
    private String forIdOrganeExecutionOrVide;
    private String forIdSociete;
    private String forReferenceExterne;

    private String likeIdExterne;
    private String likeIdFournisseur;

    private String likeReferenceExterne;
    private String numTVA;
    private String withoutCsTypeOperation;

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        sqlFields.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(".")
                .append(LXSection.FIELD_IDSECTION).append(", ");
        sqlFields.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(".")
                .append(LXSection.FIELD_IDEXTERNE).append(", ");
        sqlFields.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_IDOPERATION).append(", ");
        sqlFields.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_DATEOPERATION).append(", ");
        sqlFields.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_DATEECHEANCE).append(", ");

        sqlFields.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_REFERENCEEXTERNE).append(", ");
        sqlFields.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_MONTANT).append(", ");
        sqlFields.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_MOTIF).append(", ");
        sqlFields.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_LIBELLE).append(", ");
        sqlFields.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_CSETATOPERATION).append(", ");
        sqlFields.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_ESTBLOQUE).append(", ");

        sqlFields.append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(".")
                .append(LXFournisseur.FIELD_IDEXTERNE).append(" AS ").append(LXFacture.IDEXTERNEFOURNISSEUR)
                .append(", ");

        sqlFields.append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(".")
                .append(LXFournisseur.FIELD_IDTIERS).append(" AS ").append(LXFacture.IDTIERSFOURNISSEUR).append(", ");

        sqlFields.append(_getCollection()).append(LXFactureManager.TABLE_TIERS).append(".HTLDE1, ");
        sqlFields.append(_getCollection()).append(LXFactureManager.TABLE_TIERS).append(".HTLDE2 ");

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        /*
         * Select from LXOPERP INNER JOIN LXSECTP ON LXOPERP.IDSECTION = LXSECTP.IDSECTION INNER JOIN LXFOURP ON
         * LXSECTP.IDFOURNISSEUR = LXFOURP.IDFOURNISSEUR INNER JOIN TITIERP ON LXFOURP.IDTIERS = TITIERP.HTITIE
         */
        StringBuffer tmp = new StringBuffer();

        tmp.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(" INNER JOIN ").append(_getCollection())
                .append(LXSection.TABLE_LXSECTP);
        tmp.append(" ON " + _getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_IDSECTION).append("=").append(_getCollection())
                .append(LXSection.TABLE_LXSECTP).append(".").append(LXSection.FIELD_IDSECTION);
        tmp.append(" INNER JOIN ").append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP);
        tmp.append(" ON ").append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR + "=").append(_getCollection())
                .append(LXFournisseur.TABLE_LXFOURP).append(".").append(LXFournisseur.FIELD_IDFOURNISSEUR);
        tmp.append(" INNER JOIN ").append(_getCollection()).append("TITIERP");
        tmp.append(" ON ").append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(".")
                .append(LXFournisseur.FIELD_IDTIERS).append("=").append(_getCollection()).append("TITIERP.HTITIE");

        return tmp.toString();
    }

    /**
     * Renvoie la clause de tri
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer tmp = new StringBuffer();

        tmp.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                .append(LXOperation.FIELD_DATEOPERATION).append(", ");
        tmp.append(_getCollection()).append("TITIERP.HTLDU1, ");
        tmp.append(_getCollection()).append("TITIERP.HTLDU2");

        return tmp.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_CSTYPESECTION + " = "
                + this._dbWriteNumeric(statement.getTransaction(), LXSection.CS_TYPE_FACTURE);

        if (isCheckExtourne()) {
            // ccvdqua.LXSECTP.IDSECTION NOT IN (SELECT IDSECTION FROM ccvdqua.LXOPERP
            // WHERE CSTYPEOPERATION=7700015 AND CSETATOPERATION in (7800004, 7800005) )
            sqlWhere += " AND " + _getCollection() + LXSection.TABLE_LXSECTP + "." + LXSection.FIELD_IDSECTION
                    + " NOT IN (SELECT " + LXSection.FIELD_IDSECTION + " FROM " + _getCollection()
                    + LXOperation.TABLE_LXOPERP + " WHERE " + LXOperation.FIELD_CSTYPEOPERATION + "="
                    + LXOperation.CS_TYPE_EXTOURNE + " AND " + LXOperation.FIELD_CSETATOPERATION + " IN ("
                    + LXOperation.CS_ETAT_COMPTABILISE + ", " + LXOperation.CS_ETAT_PREPARE + "))";
        }

        sqlWhere += " AND (";

        sqlWhere += _getCollection() + LXOperation.TABLE_LXOPERP + "." + LXOperation.FIELD_CSTYPEOPERATION + " = "
                + this._dbWriteNumeric(statement.getTransaction(), LXOperation.CS_TYPE_FACTURE_BVR_ORANGE);
        sqlWhere += " OR " + _getCollection() + LXOperation.TABLE_LXOPERP + "." + LXOperation.FIELD_CSTYPEOPERATION
                + " = " + this._dbWriteNumeric(statement.getTransaction(), LXOperation.CS_TYPE_FACTURE_BVR_ROUGE);
        sqlWhere += " OR " + _getCollection() + LXOperation.TABLE_LXOPERP + "." + LXOperation.FIELD_CSTYPEOPERATION
                + " = " + this._dbWriteNumeric(statement.getTransaction(), LXOperation.CS_TYPE_FACTURE_CAISSE);
        sqlWhere += " OR " + _getCollection() + LXOperation.TABLE_LXOPERP + "." + LXOperation.FIELD_CSTYPEOPERATION
                + " = " + this._dbWriteNumeric(statement.getTransaction(), LXOperation.CS_TYPE_FACTURE_LSV);
        sqlWhere += " OR " + _getCollection() + LXOperation.TABLE_LXOPERP + "." + LXOperation.FIELD_CSTYPEOPERATION
                + " = " + this._dbWriteNumeric(statement.getTransaction(), LXOperation.CS_TYPE_FACTURE_VIREMENT);

        sqlWhere += ") AND " + _setWhereCommonPart(statement);

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXFacture();
    }

    /**
     * Permet la création de la partie commune des conditions de la requete
     * 
     * @param statement
     * @return
     */
    protected String _setWhereCommonPart(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdJournal()) && JadeStringUtil.isDigit(getForIdJournal())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDJOURNAL).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdJournal()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete()) && JadeStringUtil.isDigit(getForIdSociete())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(".")
                    .append(LXSection.FIELD_IDSOCIETE).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdSociete()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdFournisseur()) && JadeStringUtil.isDigit(getForIdFournisseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(".")
                    .append(LXSection.FIELD_IDFOURNISSEUR).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdFournisseur()));
        }

        if (!JadeStringUtil.isBlank(getLikeIdFournisseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            String nomFormatte = JadeStringUtil.convertSpecialChars(getLikeIdFournisseur());
            nomFormatte = JadeStringUtil.toUpperCase(nomFormatte);

            sqlWhere.append("(").append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(".")
                    .append(LXFournisseur.FIELD_IDEXTERNE).append(" = ")
                    .append(this._dbWriteString(statement.getTransaction(), getLikeIdFournisseur()));
            sqlWhere.append(" OR ");
            sqlWhere.append(_getCollection()).append("TITIERP.HTLDU1 like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + nomFormatte + "%")).append(" OR ")
                    .append(_getCollection()).append("TITIERP.HTLDU2 like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + nomFormatte + "%"));
            sqlWhere.append(")");
        }

        if (!JadeStringUtil.isBlank(getNumTVA())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(".")
                    .append(LXFournisseur.FIELD_NOTVA).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getNumTVA()));
        }

        if (!JadeStringUtil.isBlank(getLikeReferenceExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_REFERENCEEXTERNE).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeReferenceExterne() + "%"));
        }

        if (!JadeStringUtil.isBlank(getLikeIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(".")
                    .append(LXSection.FIELD_IDEXTERNE).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeIdExterne() + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateFacture())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_DATEOPERATION).append(" >= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFacture()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateEcheance())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_DATEECHEANCE).append(" >= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateEcheance()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getWithoutCsTypeOperation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_CSTYPEOPERATION).append(" != ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getWithoutCsTypeOperation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtatOperation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_CSETATOPERATION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtatOperation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateEcheanceInferieure())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_DATEECHEANCE).append(" <= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateEcheanceInferieure()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateFactureInferieure())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_DATEOPERATION).append(" <= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFactureInferieure()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOrganeExecutionOrVide())
                && JadeStringUtil.isDigit(getForIdOrganeExecutionOrVide())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("( ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDORGANEEXECUTION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOrganeExecutionOrVide()));
            sqlWhere.append(" OR ").append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDORGANEEXECUTION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), "0")).append(" )");
        }

        if (getForCsTypeOperationIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSTYPEOPERATION, getForCsTypeOperationIn()));
        }

        if (getForCsEtatIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSETATOPERATION, getForCsEtatIn()));
        }

        if (!JadeStringUtil.isBlank(getForReferenceExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_REFERENCEEXTERNE).append(" = ")
                    .append(this._dbWriteString(statement.getTransaction(), getForReferenceExterne()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOperationNot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(".")
                    .append(LXOperation.FIELD_IDOPERATION).append(" <> ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOperationNot()));
        }

        return sqlWhere.toString();
    }

    public ArrayList<String> getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForCsEtatOperation() {
        return forCsEtatOperation;
    }

    public ArrayList<String> getForCsTypeOperationIn() {
        return forCsTypeOperationIn;
    }

    public String getForDateEcheance() {
        return forDateEcheance;
    }

    public String getForDateEcheanceInferieure() {
        return forDateEcheanceInferieure;
    }

    public String getForDateFacture() {
        return forDateFacture;
    }

    public String getForDateFactureInferieure() {
        return forDateFactureInferieure;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdOperationNot() {
        return forIdOperationNot;
    }

    public String getForIdOrganeExecutionOrVide() {
        return forIdOrganeExecutionOrVide;
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    public String getForReferenceExterne() {
        return forReferenceExterne;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    public String getLikeIdFournisseur() {
        return likeIdFournisseur;
    }

    public String getLikeReferenceExterne() {
        return likeReferenceExterne;
    }

    public String getNumTVA() {
        return numTVA;
    }

    public String getWithoutCsTypeOperation() {
        return withoutCsTypeOperation;
    }

    /**
     * @return the checkExtourne
     */
    public boolean isCheckExtourne() {
        return checkExtourne;
    }

    /**
     * @param checkExtourne
     *            the checkExtourne to set
     */
    public void setCheckExtourne(boolean checkExtourne) {
        this.checkExtourne = checkExtourne;
    }

    public void setForCsEtatIn(ArrayList<String> forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForCsEtatOperation(String forCsEtatOperation) {
        this.forCsEtatOperation = forCsEtatOperation;
    }

    public void setForCsTypeOperationIn(ArrayList<String> forCsTypeOperationIn) {
        this.forCsTypeOperationIn = forCsTypeOperationIn;
    }

    public void setForDateEcheance(String forDateEcheance) {
        this.forDateEcheance = forDateEcheance;
    }

    public void setForDateEcheanceInferieure(String forDateEcheanceInferieure) {
        this.forDateEcheanceInferieure = forDateEcheanceInferieure;
    }

    public void setForDateFacture(String forDateFacture) {
        this.forDateFacture = forDateFacture;
    }

    public void setForDateFactureInferieure(String forDateFactureInferieure) {
        this.forDateFactureInferieure = forDateFactureInferieure;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    public void setForIdOperationNot(String forIdOperationNot) {
        this.forIdOperationNot = forIdOperationNot;
    }

    public void setForIdOrganeExecutionOrVide(String forIdOrganeExecutionOrVide) {
        this.forIdOrganeExecutionOrVide = forIdOrganeExecutionOrVide;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }

    public void setForReferenceExterne(String forReferenceExterne) {
        this.forReferenceExterne = forReferenceExterne;
    }

    public void setLikeIdExterne(String forIdExterne) {
        likeIdExterne = forIdExterne;
    }

    public void setLikeIdFournisseur(String likeIdFournisseur) {
        this.likeIdFournisseur = likeIdFournisseur;
    }

    public void setLikeReferenceExterne(String forRefFactureFournisseur) {
        likeReferenceExterne = forRefFactureFournisseur;
    }

    public void setNumTVA(String numTVA) {
        this.numTVA = numTVA;
    }

    public void setWithoutCsTypeOperation(String withoutCsTypeOperation) {
        this.withoutCsTypeOperation = withoutCsTypeOperation;
    }
}
