package globaz.lynx.db.recherche;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.utils.LXConstants;

public class LXRechercheGeneraleManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_BASE = "BASE";
    public static final String FIELD_ESTBLOQUEOPERATION = "OPERATIONESTBLOQUE";
    public static final String FIELD_IDEXTERNESECTION = "IDEXTERNESECTION";
    public static final String FIELD_IDREFERENCEEXTERNE = "REFERENCEEXTERNE";
    public static final String FIELD_IDSECTIONBASE = "IDSECTIONBASE";
    public static final String FIELD_IDSECTIONMOUVEMENT = "IDSECTIONMOUVEMENT";
    public static final String FIELD_MOUVEMENT = "MOUVEMENT";
    public static final String FIELD_SOLDE = "SOLDE";

    public static final String ORDER_BY_BASE = "order_by_base";
    public static final String ORDER_BY_DATESECTION = "order_by_datesection";
    public static final String ORDER_BY_FOURNISSEUR = "order_by_fournisseur";
    public static final String ORDER_BY_IDEXTERNE = "order_by_idexterne";
    public static final String ORDER_BY_IDREFERENCEEXTERNE = "order_by_idreferenceexterne";
    public static final String ORDER_BY_MOUVEMENT = "order_by_mouvement";
    public static final String ORDER_BY_SOLDE = "order_by_solde";

    public static final String SUBTABLE_BASEMOUVEMENT = "BASEMOUVEMENT";
    public static final String SUBTABLE_FOURNISSEUR = "FOURNISSEUR";
    public static final String SUBTABLE_TIERS = "TIERS";

    private String forCsTypeSection;
    private String forDebutDateCreation;
    private String forEtat;
    private String forFinDateCreation;
    private String forIdFournisseur;
    private String forIdSection;
    private String forIdSociete;
    private String forMontantMaxi;
    private String forMontantMini;
    private String forSelection;
    private String forTri;
    private String likeIdExterne;

    private String likeIdReferenceExterne;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append("(");
        sqlFrom.append("SELECT ").append(LXRechercheGeneraleManager.FIELD_BASE).append(", ")
                .append(LXRechercheGeneraleManager.FIELD_MOUVEMENT).append(", COALESCE(")
                .append(LXRechercheGeneraleManager.FIELD_BASE).append(",0)+COALESCE(")
                .append(LXRechercheGeneraleManager.FIELD_MOUVEMENT).append(",0) as ")
                .append(LXRechercheGeneraleManager.FIELD_SOLDE).append(", ")
                .append(LXRechercheGeneraleManager.FIELD_IDSECTIONBASE).append(", ")
                .append(LXRechercheGeneraleManager.FIELD_IDSECTIONMOUVEMENT).append(", ")
                .append(LXSection.FIELD_IDSOCIETE).append(", ").append(LXSection.FIELD_IDFOURNISSEUR).append(", ")
                .append(LXSection.FIELD_CSTYPESECTION).append(", ").append(LXSection.FIELD_DATESECTION).append(", ")
                .append(LXRechercheGeneraleManager.FIELD_IDEXTERNESECTION).append(", ")
                .append(LXRechercheGeneraleManager.FIELD_IDREFERENCEEXTERNE).append(", ")
                .append(LXOperation.FIELD_ESTBLOQUE).append(" as ")
                .append(LXRechercheGeneraleManager.FIELD_ESTBLOQUEOPERATION).append("  FROM (");

        getTableBase(statement, sqlFrom);

        sqlFrom.append(" LEFT OUTER JOIN ");

        getTableMouvement(statement, sqlFrom);

        sqlFrom.append(")) ").append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(", ");
        sqlFrom.append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(" ")
                .append(LXRechercheGeneraleManager.SUBTABLE_FOURNISSEUR).append(", ");
        sqlFrom.append(_getCollection()).append("TITIERP").append(" ")
                .append(LXRechercheGeneraleManager.SUBTABLE_TIERS).append(" ");

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer ordreBy = new StringBuffer();

        if (LXRechercheGeneraleManager.ORDER_BY_FOURNISSEUR.equals(getForTri())) {
            ordreBy.append(LXRechercheGeneraleManager.SUBTABLE_TIERS + ".HTLDE1 ASC");
        } else if (LXRechercheGeneraleManager.ORDER_BY_DATESECTION.equals(getForTri())) {
            ordreBy.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT + "." + LXSection.FIELD_DATESECTION
                    + " DESC");
        } else if (LXRechercheGeneraleManager.ORDER_BY_IDEXTERNE.equals(getForTri())) {
            ordreBy.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT + "."
                    + LXRechercheGeneraleManager.FIELD_IDEXTERNESECTION + " DESC");
        } else if (LXRechercheGeneraleManager.ORDER_BY_BASE.equals(getForTri())) {
            ordreBy.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT + "."
                    + LXRechercheGeneraleManager.FIELD_BASE + " DESC");
        } else if (LXRechercheGeneraleManager.ORDER_BY_MOUVEMENT.equals(getForTri())) {
            ordreBy.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT + "."
                    + LXRechercheGeneraleManager.FIELD_MOUVEMENT + " DESC");
        } else if (LXRechercheGeneraleManager.ORDER_BY_SOLDE.equals(getForTri())) {
            ordreBy.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT + "."
                    + LXRechercheGeneraleManager.FIELD_SOLDE + " DESC");
        } else if (LXRechercheGeneraleManager.ORDER_BY_IDREFERENCEEXTERNE.equals(getForTri())) {
            ordreBy.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT + "."
                    + LXRechercheGeneraleManager.FIELD_IDREFERENCEEXTERNE + " DESC");
        }

        return ordreBy.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR).append(" = ")
                .append(LXRechercheGeneraleManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXSection.FIELD_IDFOURNISSEUR);
        sqlWhere.append(" AND ");
        sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_FOURNISSEUR).append(".IDTIERS = ")
                .append(LXRechercheGeneraleManager.SUBTABLE_TIERS).append(".").append("HTITIE");

        if (!JadeStringUtil.isIntegerEmpty(getForCsTypeSection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".")
                    .append(LXSection.FIELD_CSTYPESECTION).append("=").append(getForCsTypeSection());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".")
                    .append(LXSection.FIELD_IDSOCIETE).append("=").append(getForIdSociete());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdFournisseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".")
                    .append(LXSection.FIELD_IDFOURNISSEUR).append("=").append(getForIdFournisseur());
        }

        if (!JadeStringUtil.isBlank(getLikeIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".")
                    .append(LXRechercheGeneraleManager.FIELD_IDEXTERNESECTION).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeIdExterne() + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMontantMini())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".").append("solde")
                    .append(" >= ").append(getForMontantMini());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMontantMaxi())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".").append("solde")
                    .append(" <= ").append(getForMontantMaxi());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForFinDateCreation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".")
                    .append(LXSection.FIELD_DATESECTION).append(" <= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForFinDateCreation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDebutDateCreation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".")
                    .append(LXSection.FIELD_DATESECTION).append(" >= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDebutDateCreation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".")
                    .append(LXRechercheGeneraleManager.FIELD_IDSECTIONBASE).append("=").append(getForIdSection());
        }

        if (!JadeStringUtil.isBlank(getLikeIdReferenceExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".")
                    .append(LXRechercheGeneraleManager.FIELD_IDREFERENCEEXTERNE).append(" like ")
                    .append(this._dbWriteString(statement.getTransaction(), "%" + getLikeIdReferenceExterne() + "%"));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForSelection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            if (LXConstants.SELECTION_OUVERT.equals(getForSelection())) {
                sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".solde").append(" <> 0");
            } else if (LXConstants.SELECTION_SOLDE.equals(getForSelection())) {
                sqlWhere.append(LXRechercheGeneraleManager.SUBTABLE_BASEMOUVEMENT).append(".solde").append(" = 0");
            }
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXRechercheGenerale();
    }

    public String getForCsTypeSection() {
        return forCsTypeSection;
    }

    public String getForDebutDateCreation() {
        return forDebutDateCreation;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForFinDateCreation() {
        return forFinDateCreation;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    public String getForMontantMaxi() {
        return forMontantMaxi;
    }

    public String getForMontantMini() {
        return forMontantMini;
    }

    public String getForSelection() {
        return forSelection;
    }

    public String getForTri() {
        return forTri;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    public String getLikeIdReferenceExterne() {
        return likeIdReferenceExterne;
    }

    /**
     * Return le sous-select qui recherche les totaux de base.
     * 
     * @param statement
     * @param sqlFrom
     */
    private void getTableBase(BStatement statement, StringBuffer sqlFrom) {
        // (select sum(b.MONTANT) as base, a.IDSECTION as IDSECTIONBASE,
        // a.IDSOCIETE, a.IDFOURNISSEUR, a.CSTYPESECTION, a.DATESECTION,
        // a.IDEXTERNE from WEBAVSCIAM.LXSECTP a, WEBAVSCIAM.LXOPERP b where
        // a.IDSECTION = b.IDSECTION and b.CSTYPEOPERATION in (7700001, 7700006,
        // 7700007, 7700008, 7700009, 7700010) AND (b.CSETATOPERATION=7800004 OR
        // b.CSETATOPERATION=7800005 OR b.CSETATOPERATION=7800006) group by
        // a.IDSECTION, a.IDSOCIETE, a.IDFOURNISSEUR, a.CSTYPESECTION,
        // a.DATESECTION, a.IDEXTERNE as IDEXTERNESECTION) table1

        sqlFrom.append("(SELECT SUM(b.").append(LXOperation.FIELD_MONTANT).append(") as ")
                .append(LXRechercheGeneraleManager.FIELD_BASE).append(", b.")
                .append(LXOperation.FIELD_REFERENCEEXTERNE).append(" as ")
                .append(LXRechercheGeneraleManager.FIELD_IDREFERENCEEXTERNE).append(", a.")
                .append(LXSection.FIELD_IDSECTION).append(" as ")
                .append(LXRechercheGeneraleManager.FIELD_IDSECTIONBASE).append(", a.")
                .append(LXSection.FIELD_IDSOCIETE).append(", a.").append(LXSection.FIELD_IDFOURNISSEUR).append(", a.")
                .append(LXSection.FIELD_CSTYPESECTION).append(", a.").append(LXSection.FIELD_DATESECTION)
                .append(", a.").append(LXSection.FIELD_IDEXTERNE).append(" as ")
                .append(LXRechercheGeneraleManager.FIELD_IDEXTERNESECTION).append(", b.")
                .append(LXOperation.FIELD_ESTBLOQUE);
        sqlFrom.append(" FROM ").append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(" a,")
                .append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(" b");
        sqlFrom.append(" WHERE a.").append(LXSection.FIELD_IDSECTION).append(" = b.")
                .append(LXOperation.FIELD_IDSECTION).append(" and b.").append(LXOperation.FIELD_CSTYPEOPERATION)
                .append(" in (").append(LXOperation.CS_TYPE_FACTURE_BVR_ORANGE).append(", ")
                .append(LXOperation.CS_TYPE_FACTURE_BVR_ROUGE).append(", ")
                .append(LXOperation.CS_TYPE_FACTURE_VIREMENT).append(", ").append(LXOperation.CS_TYPE_FACTURE_LSV)
                .append(", ").append(LXOperation.CS_TYPE_FACTURE_CAISSE).append(", ")
                .append(LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE).append(")");
        sqlFrom.append(getWhereEtat(sqlFrom, "b"));
        sqlFrom.append(" GROUP BY a.").append(LXSection.FIELD_IDSECTION).append(", a.")
                .append(LXSection.FIELD_IDSOCIETE).append(", a.").append(LXSection.FIELD_IDFOURNISSEUR).append(", a.")
                .append(LXSection.FIELD_CSTYPESECTION).append(", a.").append(LXSection.FIELD_DATESECTION)
                .append(", a.").append(LXSection.FIELD_IDEXTERNE).append(", b.")
                .append(LXOperation.FIELD_REFERENCEEXTERNE).append(", b.").append(LXOperation.FIELD_ESTBLOQUE)
                .append(") table1");
    }

    /**
     * Return le sous-select qui recherche les totaux de mouvement.
     * 
     * @param statement
     * @param sqlFrom
     */
    private void getTableMouvement(BStatement statement, StringBuffer sqlFrom) {
        // (select sum(b.MONTANT) as mouvement, a.IDSECTION as
        // IDSECTIONMOUVEMENT from WEBAVSCIAM.LXSECTP a, WEBAVSCIAM.LXOPERP b
        // where a.IDSECTION = b.IDSECTION and b.CSTYPEOPERATION in (7700002,
        // 7700011, 7700012, 7700013, 7700014, 7700003, 7700004, 7700005) AND
        // (b.CSETATOPERATION=7800004 OR b.CSETATOPERATION=7800005 OR
        // b.CSETATOPERATION=7800006) group by a.IDSECTION) table2 on
        // table1.IDSECTIONBASE = table2.IDSECTIONMOUVEMENT

        sqlFrom.append(" (SELECT SUM(b.").append(LXOperation.FIELD_MONTANT).append(") as ")
                .append(LXRechercheGeneraleManager.FIELD_MOUVEMENT).append(", a.").append(LXSection.FIELD_IDSECTION)
                .append(" as ").append(LXRechercheGeneraleManager.FIELD_IDSECTIONMOUVEMENT);
        sqlFrom.append(" FROM ").append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(" a, ")
                .append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(" b");
        sqlFrom.append(" WHERE a.").append(LXSection.FIELD_IDSECTION).append(" = b.")
                .append(LXOperation.FIELD_IDSECTION).append(" and b.").append(LXOperation.FIELD_CSTYPEOPERATION)
                .append(" in (").append(LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE).append(", ")
                .append(LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE).append(", ")
                .append(LXOperation.CS_TYPE_PAIEMENT_VIREMENT).append(", ").append(LXOperation.CS_TYPE_PAIEMENT_LSV)
                .append(", ").append(LXOperation.CS_TYPE_PAIEMENT_CAISSE).append(", ")
                .append(LXOperation.CS_TYPE_ESCOMPTE).append(", ").append(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE)
                .append(", ").append(LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE).append(", ")
                .append(LXOperation.CS_TYPE_EXTOURNE).append(")");
        sqlFrom.append(getWhereEtat(sqlFrom, "b"));
        sqlFrom.append(" GROUP BY a.").append(LXSection.FIELD_IDSECTION).append(") table2 on table1.")
                .append(LXRechercheGeneraleManager.FIELD_IDSECTIONBASE).append(" = table2.")
                .append(LXRechercheGeneraleManager.FIELD_IDSECTIONMOUVEMENT);
    }

    /**
     * Création de la partie "etat" de la clause "where"
     * 
     * @param sqlWhere
     * @param prefix
     * @return
     */
    private String getWhereEtat(StringBuffer sqlWhere, String prefix) {
        StringBuffer where = new StringBuffer();

        if (sqlWhere.length() != 0) {
            where.append(" AND ");
        }

        if (JadeStringUtil.isBlankOrZero(getForEtat()) || LXConstants.ETAT_DEFINITIF.equals(getForEtat())) {
            where.append("(");
            where.append(prefix).append(".").append(LXOperation.FIELD_CSETATOPERATION).append("=")
                    .append(LXOperation.CS_ETAT_COMPTABILISE);
            where.append(" OR ");
            where.append(prefix).append(".").append(LXOperation.FIELD_CSETATOPERATION).append("=")
                    .append(LXOperation.CS_ETAT_PREPARE);
            where.append(" OR ");
            where.append(prefix).append(".").append(LXOperation.FIELD_CSETATOPERATION).append("=")
                    .append(LXOperation.CS_ETAT_SOLDE);
            where.append(")");
        } else {

            where.append(prefix).append(".").append(LXOperation.FIELD_CSETATOPERATION).append(" <> ")
                    .append(LXOperation.CS_ETAT_ANNULE);

        }

        return where.toString();
    }

    public void setForCsTypeSection(String forCsTypeSection) {
        this.forCsTypeSection = forCsTypeSection;
    }

    public void setForDebutDateCreation(String forDebutDateCreation) {
        this.forDebutDateCreation = forDebutDateCreation;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public void setForFinDateCreation(String forFinDateCreation) {
        this.forFinDateCreation = forFinDateCreation;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }

    public void setForMontantMaxi(String forMontantMaxi) {
        this.forMontantMaxi = forMontantMaxi;
    }

    public void setForMontantMini(String forMontantMini) {
        this.forMontantMini = forMontantMini;
    }

    public void setForSelection(String forSelection) {
        this.forSelection = forSelection;
    }

    public void setForTri(String forTri) {
        this.forTri = forTri;
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

    public void setLikeIdReferenceExterne(String likeIdReferenceExterne) {
        this.likeIdReferenceExterne = likeIdReferenceExterne;
    }
}
