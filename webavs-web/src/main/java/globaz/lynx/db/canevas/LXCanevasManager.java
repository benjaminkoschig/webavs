package globaz.lynx.db.canevas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

public class LXCanevasManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String TABLE_TIERS = "TITIERP";

    private ArrayList<String> forCsTypeOperationIn;
    private String forIdFournisseur;
    private String forIdOrganeExecutionOrVide;
    private String forIdSociete;
    private String likeIdExterne;
    private String likeIdFournisseur;
    private String likeLibelle;
    private String likeReferenceExterne;
    private String numTVA;

    private String withoutCsTypeOperation;

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        sqlFields.append(_getCollection()).append(LXCanevasSection.TABLE_LXCANSP).append(".")
                .append(LXCanevasSection.FIELD_IDSECTIONCANEVAS).append(", ");
        sqlFields.append(_getCollection()).append(LXCanevasSection.TABLE_LXCANSP).append(".")
                .append(LXCanevasSection.FIELD_IDEXTERNE).append(", ");
        sqlFields.append(_getCollection()).append(LXCanevasSection.TABLE_LXCANSP).append(".")
                .append(LXCanevasSection.FIELD_IDFOURNISSEUR).append(", ");
        sqlFields.append(_getCollection()).append(LXCanevasSection.TABLE_LXCANSP).append(".")
                .append(LXCanevasSection.FIELD_IDSOCIETE).append(", ");
        sqlFields.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                .append(LXCanevasOperation.FIELD_IDOPERATIONCANEVAS).append(", ");

        sqlFields.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                .append(LXCanevasOperation.FIELD_REFERENCEEXTERNE).append(", ");
        sqlFields.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                .append(LXCanevasOperation.FIELD_MONTANT).append(", ");
        sqlFields.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                .append(LXCanevasOperation.FIELD_MOTIF).append(", ");
        sqlFields.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                .append(LXCanevasOperation.FIELD_LIBELLE).append(", ");

        sqlFields.append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(".")
                .append(LXFournisseur.FIELD_IDEXTERNE).append(" AS ").append(LXCanevas.IDEXTERNEFOURNISSEUR)
                .append(", ");

        sqlFields.append(_getCollection()).append(LXCanevasManager.TABLE_TIERS).append(".HTLDE1, ");
        sqlFields.append(_getCollection()).append(LXCanevasManager.TABLE_TIERS).append(".HTLDE2 ");

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

        tmp.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(" INNER JOIN ")
                .append(_getCollection()).append(LXCanevasSection.TABLE_LXCANSP);
        tmp.append(" ON " + _getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                .append(LXCanevasOperation.FIELD_IDSECTIONCANEVAS).append("=").append(_getCollection())
                .append(LXCanevasSection.TABLE_LXCANSP).append(".").append(LXCanevasSection.FIELD_IDSECTIONCANEVAS);
        tmp.append(" INNER JOIN ").append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP);
        tmp.append(" ON ").append(_getCollection()).append(LXCanevasSection.TABLE_LXCANSP).append(".")
                .append(LXCanevasSection.FIELD_IDFOURNISSEUR + "=").append(_getCollection())
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

        tmp.append(_getCollection()).append("TITIERP.HTLDU1, ");
        tmp.append(_getCollection()).append("TITIERP.HTLDU2");

        return tmp.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        return _setWhereCommonPart(statement);
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXCanevas();
    }

    /**
     * Permet la création de la partie commune des conditions de la requete
     * 
     * @param statement
     * @return
     */
    protected String _setWhereCommonPart(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete()) && JadeStringUtil.isDigit(getForIdSociete())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasSection.TABLE_LXCANSP).append(".")
                    .append(LXCanevasSection.FIELD_IDSOCIETE).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdSociete()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdFournisseur()) && JadeStringUtil.isDigit(getForIdFournisseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasSection.TABLE_LXCANSP).append(".")
                    .append(LXCanevasSection.FIELD_IDFOURNISSEUR).append(" = ")
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
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_REFERENCEEXTERNE).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeReferenceExterne() + "%"));
        }

        if (!JadeStringUtil.isBlank(getLikeIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasSection.TABLE_LXCANSP).append(".")
                    .append(LXCanevasSection.FIELD_IDEXTERNE).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeIdExterne() + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getWithoutCsTypeOperation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_CSTYPEOPERATION).append(" != ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getWithoutCsTypeOperation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdOrganeExecutionOrVide())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("( ").append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_IDORGANEEXECUTION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOrganeExecutionOrVide()));
            sqlWhere.append(" OR ").append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_IDORGANEEXECUTION).append(" = ")
                    .append(this._dbWriteNumeric(statement.getTransaction(), "0")).append(" )");
        }

        if (getForCsTypeOperationIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXUtils.getWhereValueMultiple(LXCanevasOperation.FIELD_CSTYPEOPERATION,
                    getForCsTypeOperationIn()));
        }

        if (!JadeStringUtil.isBlank(getLikeLibelle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(_getCollection()).append(LXCanevasOperation.TABLE_LXCANOP).append(".")
                    .append(LXCanevasOperation.FIELD_LIBELLE).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeLibelle() + "%"));
        }

        return sqlWhere.toString();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public ArrayList<String> getForCsTypeOperationIn() {
        return forCsTypeOperationIn;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdOrganeExecutionOrVide() {
        return forIdOrganeExecutionOrVide;
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    public String getLikeIdFournisseur() {
        return likeIdFournisseur;
    }

    public String getLikeLibelle() {
        return likeLibelle;
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

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForCsTypeOperationIn(ArrayList<String> forCsTypeOperationIn) {
        this.forCsTypeOperationIn = forCsTypeOperationIn;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdOrganeExecutionOrVide(String forIdOrganeExecutionOrVide) {
        this.forIdOrganeExecutionOrVide = forIdOrganeExecutionOrVide;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }

    public void setLikeIdExterne(String forIdExterne) {
        likeIdExterne = forIdExterne;
    }

    public void setLikeIdFournisseur(String likeIdFournisseur) {
        this.likeIdFournisseur = likeIdFournisseur;
    }

    public void setLikeLibelle(String likeLibelle) {
        this.likeLibelle = likeLibelle;
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
