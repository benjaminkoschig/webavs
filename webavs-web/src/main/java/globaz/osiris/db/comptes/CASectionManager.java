package globaz.osiris.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public class CASectionManager extends BManager implements Serializable {

    private static final long serialVersionUID = -5677184261759612633L;
    public final static String ALIAS_TABLE_SECTION = CASection.TABLE_CASECTP;
    public final static int INT_ORDER_DATE_DESCEND = 233002;
    public final static String ORDER_DATE = "233004";
    public final static String ORDER_DATE_DESCEND = "233002";
    public final static String ORDER_IDEXTERNE = "233003";
    public final static String ORDER_IDEXTERNE_DATE_ASC = "233008";
    public final static String ORDER_IDEXTERNE_DESCEND = "233001";
    public final static String ORDER_IDSECTION = "233007";
    public final static String ORDER_SOLDE = "233005";
    public final static String ORDER_SOLDE_DESCEND = "233006";
    public final static String SOLDE_ALL = "1000";
    public final static String SOLDE_CLOSED = "2";
    public final static String SOLDE_OPEN = "1";
    private final static String EXCLUDE_CODE900_MORATOIRE_LIKE = "'%900'";

    private String afterIdSection = "";
    private String forCategorieSection = "";
    private Collection<String> forCategorieSectionIn = null;
    private boolean forContentieuxEstSuspendu = false;
    private Boolean forExcludeCode900Moratoire = Boolean.FALSE;

    public Boolean getForExcludeCode900Moratoire() {
        return forExcludeCode900Moratoire;
    }

    public void setForExcludeCode900Moratoire(Boolean forExcludeCode900Moratoire) {
        this.forExcludeCode900Moratoire = forExcludeCode900Moratoire;
    }

    private String forIdCompteAnnexe = "";
    private String forEBillTransactionID = "";
    private String forIdExterne = "";
    private String forIdJournal = "";
    private String forIdPassageComp = "";
    private String forIdPlanRecouvrement = "";
    private String forIdSection = "";
    private Collection<String> forIdSectionIn = null;
    private String forIdSectionPrinc = "";
    private String forIdTypeSection = "";
    private String forModeCompensationEquals = "";
    private boolean forModeCompensationIsReport = false;
    private String forModeCompensationNotEquals = "";
    private String forMontantMinime = "";
    private String forSelectionSections = "";
    private String forSelectionTri = "";;
    private String forSolde = "";
    private boolean forSoldeEgalTaxes = false;
    private String forSoldeNot = "";
    private Boolean forSoldeSmallerThanZero = new Boolean(false);

    private String fromDate = "";

    private String fromIdExterne = "";
    private String fromPositionnement = "";
    private String lastEtatAquilaNotIn = "";
    private String likeIdExterne = "";
    private String modeCompensationNotIn = "";
    private String orderBy = "";
    private String untilDate = "";
    private String untilIdExterne = "";
    private String fromDateEcheance = "";
    private String untilDateEcheance = "";

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder from = new StringBuilder();

        from.append(_getCollection()).append(CASection.TABLE_CASECTP).append(" ")
                .append(CASectionManager.ALIAS_TABLE_SECTION);

        return from.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuilder orderByBuilder = new StringBuilder();

        if ((getForSelectionTri().length() != 0)
                && !getForSelectionTri().equals(CASectionManager.ORDER_IDEXTERNE_DESCEND)) {
            orderByBuilder.append(CASection.FIELD_DATESECTION).append(" DESC");
        } else if (getOrderBy().equals(CASectionManager.ORDER_IDSECTION)) {
            orderByBuilder.append(CASection.FIELD_IDSECTION);
        } else if (getOrderBy().equals(CASectionManager.ORDER_DATE)) {
            orderByBuilder.append(CASection.FIELD_DATESECTION).append(",");
            orderByBuilder.append(CASection.FIELD_IDEXTERNE);
        } else if (getOrderBy().equals(CASectionManager.ORDER_DATE_DESCEND)) {
            orderByBuilder.append(CASection.FIELD_DATESECTION).append(" DESC,");
            orderByBuilder.append(CASection.FIELD_IDEXTERNE).append(" DESC");
        } else if (getOrderBy().equals(CASectionManager.ORDER_SOLDE)) {
            orderByBuilder.append(CASection.FIELD_SOLDE);
        } else if (getOrderBy().equals(CASectionManager.ORDER_SOLDE_DESCEND)) {
            orderByBuilder.append(CASection.FIELD_SOLDE).append(" DESC");
        } else if (getOrderBy().equals(CASectionManager.ORDER_IDEXTERNE_DATE_ASC)) {
            orderByBuilder.append(CASection.FIELD_IDEXTERNE).append(" ASC,");
            orderByBuilder.append(CASection.FIELD_DATESECTION).append(" ASC");
        } else {
            orderByBuilder.append(CASection.FIELD_IDEXTERNE).append(" DESC");
        }

        return orderByBuilder.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isBlank(getForIdCompteAnnexe())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDCOMPTEANNEXE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe()));
        }

        if (!JadeStringUtil.isBlank(getForEBillTransactionID())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_EBILL_TRANSACTION_ID);
            sql.append("=");
            sql.append(this._dbWriteString(statement.getTransaction(), getForEBillTransactionID()));
        }

        if (!JadeStringUtil.isBlank(getForIdSection())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDSECTION);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        if (!JadeStringUtil.isBlank(getForIdJournal())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDJOURNAL);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdJournal()));
        }

        if (!JadeStringUtil.isBlank(getAfterIdSection())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDSECTION);
            sql.append(">");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getAfterIdSection()));
        }

        if (!JadeStringUtil.isBlank(getForIdExterne())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDEXTERNE);
            sql.append("=");
            sql.append(this._dbWriteString(statement.getTransaction(), getForIdExterne()));
        }

        if (!JadeStringUtil.isBlank(getForIdTypeSection()) && !"1000".equals(getForIdTypeSection())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDTYPESECTION);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdTypeSection()));
        }

        if (!JadeStringUtil.isBlank(getForSelectionSections()) && !"1000".equals(getForSelectionSections())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }

            switch (JadeStringUtil.toIntMIN(getForSelectionSections())) {
                case 1:
                    sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
                    sql.append("<>");
                    sql.append("0");
                    break;
                case 2:
                    sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
                    sql.append("=");
                    sql.append("0");
                    break;
                case 3:
                    sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
                    sql.append(">");
                    sql.append("0");
                    break;
                case 4:
                    sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
                    sql.append("<");
                    sql.append("0");
                    break;
                default:
                    break;
            }

            if ("5".equals(getForSelectionSections())) {
                sql.append("( ");

                sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
                sql.append("<>");
                sql.append("0");

                sql.append(" OR ");

                sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATESECTION);
                sql.append(">=");
                sql.append(this._dbWriteDateAMJ(statement.getTransaction(), "01.01."
                        + (JACalendar.today().getYear() - 2)));

                sql.append(" ) ");
            }
        }

        if (!JadeStringUtil.isBlank(getFromPositionnement())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            switch (JadeStringUtil.toIntMIN(getForSelectionTri())) {
                case CASectionManager.INT_ORDER_DATE_DESCEND:
                    sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATESECTION);
                    sql.append("<=");
                    sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getFromPositionnement()));
                    break;
                default:
                    sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDEXTERNE);
                    sql.append(" LIKE ");
                    sql.append(this._dbWriteString(statement.getTransaction(), "%" + getFromPositionnement() + "%"));
                    break;
            }
        }

        if (!JadeStringUtil.isBlank(getForSolde())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForSolde()));
        }
        if (isForSoldeSmallerThanZero()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
            sql.append("< 0");
        }

        if (!JadeStringUtil.isBlank(getForSoldeNot())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
            sql.append("<>");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForSoldeNot()));
        }

        if (!JadeStringUtil.isBlank(getUntilDate())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATESECTION);
            sql.append("<=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getUntilDate()));
        }

        if (!JadeStringUtil.isBlank(getFromDate())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATESECTION);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getFromDate()));
        }

        if (!JadeStringUtil.isBlank(getUntilDateEcheance())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATEECHEANCE);
            sql.append("<=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getUntilDateEcheance()));
        }

        if (!JadeStringUtil.isBlank(getFromDateEcheance())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_DATEECHEANCE);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), getFromDateEcheance()));
        }

        if (!JadeStringUtil.isBlank(getUntilIdExterne())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDEXTERNE);
            sql.append("<");
            sql.append(this._dbWriteString(statement.getTransaction(), getUntilIdExterne()));
        }

        if (!JadeStringUtil.isBlank(getFromIdExterne())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDEXTERNE);
            sql.append(">");
            sql.append(this._dbWriteString(statement.getTransaction(), getFromIdExterne()));
        }

        if (!JadeStringUtil.isBlank(getLikeIdExterne())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDEXTERNE);
            sql.append(" LIKE ");
            sql.append(this._dbWriteString(statement.getTransaction(), getLikeIdExterne() + "%"));
        }

        if ((getForCategorieSectionIn() != null) && (getForCategorieSectionIn().size() > 0)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_CATEGORIESECTION);
            sql.append(" IN (");

            Iterator<String> iter = getForCategorieSectionIn().iterator();
            boolean isFirstElement = true;
            while (iter.hasNext()) {
                if (isFirstElement) {
                    isFirstElement = false;
                } else {
                    sql.append(",");
                }
                sql.append(iter.next());
            }
            sql.append(")");
        }

        if (!JadeStringUtil.isBlank(getForCategorieSection())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_CATEGORIESECTION);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForCategorieSection()));
        }

        if (!JadeStringUtil.isBlank(getForMontantMinime())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
            sql.append("<>");
            sql.append("0");

            sql.append(" AND ");

            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
            sql.append(" BETWEEN ");
            sql.append("-").append(this._dbWriteNumeric(statement.getTransaction(), getForMontantMinime()));
            sql.append(" AND ");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForMontantMinime()));
        }

        if (!JadeStringUtil.isBlank(getForIdSectionPrinc())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDSECTIONPRINC);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdSectionPrinc()));
        }

        if (isForSoldeEgalTaxes()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_SOLDE);
            sql.append("=");
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_TAXES);
        }

        if (isForContentieuxEstSuspendu()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_CONTENTIEUXESTSUS);
            sql.append(" LIKE ");
            sql.append("'").append(BConstants.DB_BOOLEAN_TRUE).append("'");
        }

        if (!JadeStringUtil.isBlank(getForModeCompensationNotEquals())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append("(");

            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDMODECOMPENSATION);
            sql.append(" IS NULL");

            sql.append(" OR ");

            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDMODECOMPENSATION);
            sql.append("<>");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForModeCompensationNotEquals()));

            sql.append(")");
        }

        if (!JadeStringUtil.isBlank(getModeCompensationNotIn())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append("(");

            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDMODECOMPENSATION);
            sql.append(" IS NULL");

            sql.append(" OR ");

            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDMODECOMPENSATION);
            sql.append(" NOT IN (" + getModeCompensationNotIn() + ")");

            sql.append(")");
        }

        if (!JadeStringUtil.isBlank(getForModeCompensationEquals())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDMODECOMPENSATION);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForModeCompensationEquals()));
        }

        if (isForModeCompensationIsReport()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append("(");

            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDMODECOMPENSATION);
            sql.append(" IS NOT NULL");

            sql.append(" AND ");

            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDMODECOMPENSATION);
            sql.append("<>");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), APISection.MODE_COMPENSATION_STANDARD));

            sql.append(" AND ");

            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDMODECOMPENSATION);
            sql.append("<>");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), APISection.MODE_BLOQUER_COMPENSATION));

            sql.append(" AND ");

            // ne pas reporter les sections en mode MODE_COMP_DEC_PERIODIQUE
            // elles sont en effet réservées pour la compensation des décommptes périodiques (InfoRom 384)
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDMODECOMPENSATION);
            sql.append("<>");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), APISection.MODE_COMP_DEC_PERIODIQUE));

            sql.append(")");
        }

        if (!JadeStringUtil.isBlank(getLastEtatAquilaNotIn())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDLASTETATAQUILA);
            sql.append(" IS NOT NULL");

            sql.append(" AND ");

            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDLASTETATAQUILA);
            sql.append(" NOT IN (");
            sql.append(getLastEtatAquilaNotIn());
            sql.append(")");
        }

        if (!JadeStringUtil.isBlank(getForIdPlanRecouvrement())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDPLANRECOUVREMENT);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdPlanRecouvrement()));
        }

        if (!JadeStringUtil.isBlank(getForIdPassageComp())) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDPASSAGECOMP);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), getForIdPassageComp()));
        }

        if (getForExcludeCode900Moratoire()) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append("TRIM(");
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDEXTERNE);
            sql.append(")");
            sql.append(" NOT LIKE ");
            sql.append(EXCLUDE_CODE900_MORATOIRE_LIKE);
        }

        if ((getForIdSectionIn() != null) && (getForIdSectionIn().size() > 0)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(CASectionManager.ALIAS_TABLE_SECTION).append(".").append(CASection.FIELD_IDSECTION);
            sql.append(" IN (");

            Iterator<String> iter = getForIdSectionIn().iterator();
            boolean isFirstElement = true;
            while (iter.hasNext()) {
                if (isFirstElement) {
                    isFirstElement = false;
                } else {
                    sql.append(",");
                }
                sql.append(iter.next());
            }

            sql.append(")");
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CASection();
    }

    public String getAfterIdSection() {
        return afterIdSection;
    }

    public String getForCategorieSection() {
        return forCategorieSection;
    }

    public Collection<String> getForCategorieSectionIn() {
        return forCategorieSectionIn;
    }

    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    public String getForEBillTransactionID() {
        return forEBillTransactionID;
    }

    public String getForIdExterne() {
        return forIdExterne;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getForIdPassageComp() {
        return forIdPassageComp;
    }

    public String getForIdPlanRecouvrement() {
        return forIdPlanRecouvrement;
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public Collection<String> getForIdSectionIn() {
        return forIdSectionIn;
    }

    public String getForIdSectionPrinc() {
        return forIdSectionPrinc;
    }

    public String getForIdTypeSection() {
        return forIdTypeSection;
    }

    public String getForModeCompensationEquals() {
        return forModeCompensationEquals;
    }

    public String getForModeCompensationNotEquals() {
        return forModeCompensationNotEquals;
    }

    public String getForMontantMinime() {
        return forMontantMinime;
    }

    /**
     * <H1>Permet d'effectuer une sélection du solde de la section</H1>
     * 
     * <Pre>
     * 1 --&gt; solde &lt;&gt; 0
     * 2 --&gt; solde = 0
     * 3 --&gt; solde &gt; 0
     * 4 --&gt; solde &lt; 0
     * </pre>
     * 
     * @return
     */
    public String getForSelectionSections() {
        return forSelectionSections;
    }

    public String getForSelectionTri() {
        return forSelectionTri;
    }

    public String getForSolde() {
        return forSolde;
    }

    public String getForSoldeNot() {
        return forSoldeNot;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getFromIdExterne() {
        return fromIdExterne;
    }

    public String getFromPositionnement() {
        return fromPositionnement;
    }

    public String getLastEtatAquilaNotIn() {
        return lastEtatAquilaNotIn;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    public String getModeCompensationNotIn() {
        return modeCompensationNotIn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getUntilDate() {
        return untilDate;
    }

    public String getUntilIdExterne() {
        return untilIdExterne;
    }

    public boolean isForContentieuxEstSuspendu() {
        return forContentieuxEstSuspendu;
    }

    public boolean isForModeCompensationIsReport() {
        return forModeCompensationIsReport;
    }

    public boolean isForSoldeEgalTaxes() {
        return forSoldeEgalTaxes;
    }

    /**
     * @return the forSoldeSmallerThanZero
     */
    public Boolean isForSoldeSmallerThanZero() {
        return forSoldeSmallerThanZero;
    }

    public void setAfterIdSection(String newAfterIdSection) {
        afterIdSection = newAfterIdSection;
    }

    public void setForCategorieSection(String forCategorieSection) {
        this.forCategorieSection = forCategorieSection;
    }

    public void setForCategorieSectionIn(Collection<String> forCategorieSectionIn) {
        this.forCategorieSectionIn = forCategorieSectionIn;
    }

    public void setForContentieuxEstSuspendu(boolean b) {
        forContentieuxEstSuspendu = b;
    }

    public void setForIdCompteAnnexe(String newForIdCompteAnnexe) {
        forIdCompteAnnexe = newForIdCompteAnnexe;
    }

    public void setForEBillTransactionID(String newForEbillTransactionId) {
        forEBillTransactionID = newForEbillTransactionId;
    }

    public void setForIdExterne(String newForIdExterne) {
        forIdExterne = newForIdExterne;
    }

    public void setForIdJournal(String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    public void setForIdPassageComp(String forIdPassageComp) {
        this.forIdPassageComp = forIdPassageComp;
    }

    public void setForIdPlanRecouvrement(String forIdPlanRecouvrement) {
        this.forIdPlanRecouvrement = forIdPlanRecouvrement;
    }

    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    public void setForIdSectionIn(Collection<String> forIdSectionIn) {
        this.forIdSectionIn = forIdSectionIn;
    }

    public void setForIdSectionPrinc(String string) {
        forIdSectionPrinc = string;
    }

    public void setForIdTypeSection(String newForIdTypeSection) {
        forIdTypeSection = newForIdTypeSection;
    }

    public void setForModeCompensationEquals(String forModeCompensationEquals) {
        this.forModeCompensationEquals = forModeCompensationEquals;
    }

    public void setForModeCompensationIsReport(boolean forModeCompensationIsReport) {
        this.forModeCompensationIsReport = forModeCompensationIsReport;
    }

    public void setForModeCompensationNotEquals(String forModeCompensationNotEquals) {
        this.forModeCompensationNotEquals = forModeCompensationNotEquals;
    }

    public void setForMontantMinime(String forMontantMinime) {
        this.forMontantMinime = forMontantMinime;
    }

    /**
     * <H1>Permet d'effectuer une sélection du solde de la section</H1>
     * 
     * <Pre>
     * 1 --&gt; solde &lt;&gt; 0
     * 2 --&gt; solde = 0
     * 3 --&gt; solde &gt; 0
     * 4 --&gt; solde &lt; 0
     * </pre>
     * 
     * @param newForSelectionSections
     */
    public void setForSelectionSections(String newForSelectionSections) {
        forSelectionSections = newForSelectionSections;
    }

    public void setForSelectionTri(String newForSelectionTri) {
        forSelectionTri = newForSelectionTri;
    }

    public void setForSolde(String newForSolde) {
        forSolde = newForSolde;
    }

    public void setForSoldeEgalTaxes(boolean b) {
        forSoldeEgalTaxes = b;
    }

    public void setForSoldeNot(String newForSoldeNot) {
        forSoldeNot = newForSoldeNot;
    }

    /**
     * @param forSoldeSmallerThanZero
     *            the forSoldeSmallerThanZero to set
     */
    public void setForSoldeSmallerThanZero(Boolean forSoldeSmallerThanZero) {
        this.forSoldeSmallerThanZero = forSoldeSmallerThanZero;
    }

    public void setFromDate(String string) {
        fromDate = string;
    }

    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    public void setFromPositionnement(String newFromPositionnement) {
        fromPositionnement = newFromPositionnement;
    }

    public void setLastEtatAquilaNotIn(String lastEtatAquilaNotIn) {
        this.lastEtatAquilaNotIn = lastEtatAquilaNotIn;
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

    public void setModeCompensationNotIn(String modeCompensationNotIn) {
        this.modeCompensationNotIn = modeCompensationNotIn;
    }

    public void setOrderBy(String newOrderBy) {
        orderBy = newOrderBy;
    }

    public void setUntilDate(String newUntilDate) {
        untilDate = newUntilDate;
    }

    public void setUntilIdExterne(String newUntilIdExterne) {
        untilIdExterne = newUntilIdExterne;
    }

    public String getFromDateEcheance() {
        return fromDateEcheance;
    }

    public void setFromDateEcheance(String fromDateEcheance) {
        this.fromDateEcheance = fromDateEcheance;
    }

    public String getUntilDateEcheance() {
        return untilDateEcheance;
    }

    public void setUntilDateEcheance(String untilDateEcheance) {
        this.untilDateEcheance = untilDateEcheance;
    }
}
