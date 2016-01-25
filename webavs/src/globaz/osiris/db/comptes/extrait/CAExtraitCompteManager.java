package globaz.osiris.db.comptes.extrait;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CATypeOperation;
import globaz.osiris.db.comptes.CATypeOperationManager;
import globaz.osiris.db.ordres.CAOrdreVersement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * @author dda
 */
public class CAExtraitCompteManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final String AND_DB_OPERAND = " AND ";
    private static Hashtable descTypeOperationCache = null;
    public static final String FOR_ALL_IDTYPEOPERATION = "all";

    protected static final String FROM = " " + "FROM" + " ";
    protected static final String GROUP_BY = " GROUP BY ";
    private static final String LANGUAGE_DE = "de";

    private static final String LANGUAGE_FR = "fr";
    private static final String LANGUAGE_IT = "it";
    protected static final String LIKE_DB_OPERAND = " like ";
    protected static final String OR_DB_OPERAND = " OR ";
    protected static final String ORDER_BY = " ORDER BY ";

    public static final String ORDER_BY_DATE_COMPTABLE = "order_by_date";
    public static final String ORDER_BY_DATE_VALEUR = "order_by_date_val";
    public static final String ORDER_BY_IDSECTION = "order_by_idsection";
    protected static final String SELECT = "SELECT" + " ";

    public static final String SOLDE_ALL = "all";
    public static final String SOLDE_CLOSED = "soldees";
    public static final String SOLDE_OPEN = "open";

    private static final String UNION_ALL = " UNION ALL ";

    protected static final String WHERE = " " + "WHERE" + " ";
    private String forIdSection;

    private String fromDate;
    private String fromIdExterne;

    private String idCompteAnnexe;
    private String likeIdTypeOperation;
    private String order = CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE;
    protected PreparedStatement select;
    private String solde = CAExtraitCompteManager.SOLDE_ALL;

    // private List forCsReferenceRubrique = null;

    private String untilDate;
    private String untilIdExterne;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAExtraitCompte();
    }

    /**
     * Si la clause WHERE n'est pas null ajout de l'opérande AND.
     * 
     * @param statement
     * @param where
     * @return
     */
    protected String addAndToWhere(String where) {
        if (!JadeStringUtil.isBlank(where)) {
            where += CAExtraitCompteManager.AND_DB_OPERAND;
        }
        return where;
    }

    protected String addForIdSectionToWhere(String where) {
        if (!JadeStringUtil.isBlank(getForIdSection())) {
            where = addAndToWhere(where);
            where += "b." + CASection.FIELD_IDSECTION + "=" + "?";
        }
        return where;
    }

    /**
     * Si fromDate n'est pas null => ajout d'un clause pour fromDate à WHERE.
     * 
     * @param statement
     * @param where
     * @return
     */
    private String addFromDateToWhere(String where) {
        if (!JadeStringUtil.isBlank(getFromDate())) {
            where = addAndToWhere(where);
            where += "a." + CAOperation.FIELD_DATE + ">=" + "?";
        }
        return where;
    }

    /**
     * Si fromIdExtern n'est pas null => ajout d'un clause pour fromIdExtern à WHERE.
     * 
     * @param statement
     * @param where
     * @return
     */
    protected String addFromIdExterneToWhere(String where) {
        if (!JadeStringUtil.isBlank(getFromIdExterne())) {
            where = addAndToWhere(where);
            where += "b." + CASection.FIELD_IDEXTERNE + ">=" + "?";
        }
        return where;
    }

    /**
     * Si idCompteAnnexe n'est pas null => ajout d'un clause pour idCompteAnnexe à WHERE.
     * 
     * @param statement
     * @param where
     * @return
     */
    private String addIdCompteAnnexeToWhere(String where) {
        if (!JadeStringUtil.isBlank(getIdCompteAnnexe())) {
            where = addAndToWhere(where);
            where += "a." + CAOperation.FIELD_IDCOMPTEANNEXE + " = " + "?";
        }
        return where;
    }

    /**
     * Ajout du lien de table à WHERE (CAOPERP.IDJOURNAL = CAJOURP.IDSECTION).
     * 
     * @param statement
     * @param where
     * @return
     */
    protected String addIdJournalBindToWhere(String where) {
        where = addAndToWhere(where);
        where += "a." + CAOperation.FIELD_IDJOURNAL + " = d." + CAJournal.FIELD_IDJOURNAL;
        return where;
    }

    /**
     * @param where
     * @param groupWithIdRubrique
     * @return
     */
    protected String addIdRubriqueBindToWhere(String where, boolean groupWithIdRubrique) {
        where = addAndToWhere(where);

        if (groupWithIdRubrique) {
            where += "a." + CAOperation.FIELD_IDCOMPTE + " = c." + CARubrique.FIELD_IDRUBRIQUE;
        } else {
            where += "(a." + CAOperation.FIELD_IDCOMPTE + " is NULL or a." + CAOperation.FIELD_IDCOMPTE + " = 0)";
        }

        return where;
    }

    /**
     * Ajout du lien de table à WHERE (CAOPERP.IDSECTION = CASECTP.IDSECTION).
     * 
     * @param statement
     * @param where
     * @return
     */
    protected String addIdSectionBindToWhere(String where) {
        where = addAndToWhere(where);
        where += "a." + CAOperation.FIELD_IDSECTION + " = b." + CASection.FIELD_IDSECTION;
        return where;
    }

    /**
     * Si idTypeOperation n'est pas null => ajout d'un clause pour idTypeOperation à WHERE.
     * 
     * @param statement
     * @param where
     * @return
     */
    private String addLikeIdTypeOperationToWhere(String where) {
        if (!JadeStringUtil.isBlank(getLikeIdTypeOperation())) {
            where = addAndToWhere(where);
            where += "a." + CAOperation.FIELD_IDTYPEOPERATION + CAExtraitCompteManager.LIKE_DB_OPERAND + "?";
        }
        return where;
    }

    protected String addOrdreVersementPasBloqueToWhere(String where) {
        where = addAndToWhere(where);
        where += "(ov." + CAOrdreVersement.FIELD_ESTBLOQUE + "<>" + BConstants.DB_BOOLEAN_TRUE_DELIMITED + " or ov."
                + CAOrdreVersement.FIELD_ESTBLOQUE + " is null) ";
        return where;
    }

    /**
     * Si solde n'est pas null => ajout d'un clause pour solde à WHERE.
     * 
     * @param statement
     * @param where
     * @return
     */
    protected String addSoldeTowhere(BTransaction transaction, String where) {
        if (solde.equals(CAExtraitCompteManager.SOLDE_OPEN)) {
            where = addAndToWhere(where);
            where += "b." + CASection.FIELD_SOLDE + " <> " + this._dbWriteNumeric(transaction, "0");
        } else if (solde.equals(CAExtraitCompteManager.SOLDE_CLOSED)) {
            where = addAndToWhere(where);
            where += "b." + CASection.FIELD_SOLDE + " = " + this._dbWriteNumeric(transaction, "0");
        }
        return where;
    }

    /**
     * Si state n'est pas null => ajout d'un clause pour state à WHERE.
     * 
     * @param statement
     * @param where
     * @return
     */
    private String addStateToWhere(String where) {
        where = addAndToWhere(where);
        where += "(a." + CAOperation.FIELD_ETAT + " = " + "?" + CAExtraitCompteManager.OR_DB_OPERAND + "a."
                + CAOperation.FIELD_ETAT + " = " + "?" + ") ";

        return where;
    }

    /**
     * Si untilDate n'est pas null => ajout d'un clause pour untilDate à WHERE.
     * 
     * @param statement
     * @param where
     * @return
     */
    private String addUntilDateToWhere(String where) {
        if (!JadeStringUtil.isBlank(getUntilDate())) {
            where = addAndToWhere(where);
            where += "a." + CAOperation.FIELD_DATE + "<=" + "?";
        }
        return where;
    }

    protected String addUntilIdExterneToWhere(String where) {
        if (!JadeStringUtil.isBlank(getUntilIdExterne())) {
            where = addAndToWhere(where);
            where += "b." + CASection.FIELD_IDEXTERNE + "<=" + "?";
        }
        return where;
    }

    /**
     * Exécute la query SELECT et ajoute au manager les extait de compte trouvés.
     * 
     * @return
     * @throws SQLException
     */
    public void executeQuery(BTransaction transaction) throws Exception {
        ResultSet result = select.executeQuery();
        while (result.next()) {
            CAExtraitCompte ca = (CAExtraitCompte) _createNewEntity();
            ca.setDate(JACalendar.format(new JADate(result.getBigDecimal(CAOperation.FIELD_DATE))));
            ca.setIdSection(result.getString(CAOperation.FIELD_IDSECTION).trim());
            ca.setIdTypeOperation(result.getString(CAOperation.FIELD_IDTYPEOPERATION).trim());
            ca.setSum(result.getDouble(CAOperation.FIELD_MONTANT));
            ca.setIdExterne(result.getString(CASection.FIELD_IDEXTERNE).trim());
            ca.setIdTypeSection(result.getString(CASection.FIELD_IDTYPESECTION).trim());
            ca.setSectionDate(result.getString(CAExtraitCompte.SECTIONDATE_FIELD).trim());
            ca.setLibelleExtraitCompte(result.getString(CARubrique.FIELD_LIBELLEEXTRAIT));
            ca.setProvenancePmt(result.getString(CAOperation.FIELD_PROVENANCEPMT));
            ca.setDateJournal(JACalendar.format(new JADate(result.getBigDecimal(CAJournal.FIELD_DATEVALEURCG))));
            ca.setIdSectionCompensation(result.getString(CAOperation.FIELD_IDSECTION_COMPENSATION));
            ca.setSectionCompensationDeSur(result.getString(CAOperation.FIELD_SECTION_COMPENSATION_DE_SUR));
            _getContainer().add(ca);
        }
        setNoError();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.osiris.db.comptes.extrait.manager.CAExtraitCompteManager# fillSelectVariables()
     */
    public void fillSelectVariables() throws SQLException, JAException {
        select.clearParameters();

        int index = 1;

        index = fillSelectVariablesIntoUnion(index);
        fillSelectVariablesIntoUnion(index);
    }

    /**
     * @param index
     * @return
     * @throws SQLException
     * @throws JAException
     */
    private int fillSelectVariablesIntoUnion(int index) throws SQLException, JAException {
        if (!JadeStringUtil.isBlank(getIdCompteAnnexe())) {
            select.setInt(index, JadeStringUtil.parseInt(getIdCompteAnnexe(), 0));
            index++;
        }

        select.setInt(index, JadeStringUtil.parseInt(APIOperation.ETAT_COMPTABILISE, 0));
        index++;
        select.setInt(index, JadeStringUtil.parseInt(APIOperation.ETAT_PROVISOIRE, 0));
        index++;

        if (!JadeStringUtil.isBlank(getFromDate())) {
            JADate myDate = new JADate(getFromDate());
            select.setInt(index, JadeStringUtil.parseInt(myDate.toAMJ().toString(), 0));
            index++;
        }

        if (!JadeStringUtil.isBlank(getUntilDate())) {
            JADate myDate = new JADate(getUntilDate());
            select.setInt(index, JadeStringUtil.parseInt(myDate.toAMJ().toString(), 0));
            index++;
        }

        if (!JadeStringUtil.isBlank(getLikeIdTypeOperation())) {
            select.setString(index, getLikeIdTypeOperation() + "%");
            index++;
        }

        if (!JadeStringUtil.isBlank(getFromIdExterne())) {
            select.setString(index, getFromIdExterne());
            index++;
        }

        if (!JadeStringUtil.isBlank(getUntilIdExterne())) {
            select.setString(index, getUntilIdExterne());
            index++;
        }

        if (!JadeStringUtil.isBlank(getForIdSection())) {
            select.setString(index, getForIdSection());
            index++;
        }

        return index;
    }

    /**
     * Return la description commune de l'écriture.
     * 
     * @param extraitCompte
     * @return
     */
    protected String getCommonDescription(CAExtraitCompte extraitCompte, String language) {
        return getSectionDescriptor(extraitCompte).getDescription(language);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.osiris.db.comptes.extrait.manager.CAExtraitCompteManager#
     * getDescription(globaz.osiris.db.comptes.extrait.CAExtraitCompte, globaz.globall.db.BTransaction)
     */
    public String getDescription(CAExtraitCompte extraitCompte, BTransaction transaction, String language,
            boolean modeScreen) {

        if (!JadeStringUtil.isIntegerEmpty(extraitCompte.getIdSectionCompensation())) {

            String idSectionComp = extraitCompte.getIdSectionCompensation();
            String compensationDeSur = extraitCompte.getSectionCompensationDeSur();
            CASection secCmp = new CASection();
            secCmp.setIdSection(idSectionComp);
            secCmp.setSession(extraitCompte.getSession());

            String deSur = "-";

            /*
             * OCA - Le code ci-dessous est juste, mais il semble de l'information de sur qui est fournit depuis la
             * factu ne soit pas toujours renseigné juste. (incohérence entre compensation normal et compensation
             * APG...) En attendant de statuer sur ce point, je laisse cette information vide.
             */
            // if ("S".equals(compensationDeSur)) { // le code peut ête soit 'D'
            // => report Depuis soit 'S' => report Sur
            // if ("fr".equalsIgnoreCase(language)) {
            // deSur="sur";
            // } else {
            // deSur="-";
            // }
            // } else if ("D".equals(compensationDeSur)) {
            // if ("fr".equalsIgnoreCase(language)) {
            // deSur="de";
            // } else {
            // deSur="-";
            // }
            // }
            // sinon on laisse blanc

            try {
                secCmp.retrieve();
            } catch (Exception e) {
                return getTypeOperationDescriptionFromCache(APIOperation.CAECRITURECOMPENSATION, language);
            }
            return getTypeOperationDescriptionFromCache(APIOperation.CAECRITURECOMPENSATION, language) + " " + deSur
                    + " " + secCmp.getIdExterne() + " - " + secCmp.getDescription(language);

        } else if (!JadeStringUtil.isIntegerEmpty(extraitCompte.getLibelleExtraitCompte())) {
            FWParametersSystemCode csLibelleExtraitCompte = new FWParametersSystemCode();
            csLibelleExtraitCompte.setSession(getSession());
            csLibelleExtraitCompte.getCode(extraitCompte.getLibelleExtraitCompte());

            return csLibelleExtraitCompte.getCodeUtilisateur(language.toUpperCase().substring(0, 1)).getLibelle();
        } else {
            if (extraitCompte.getIdTypeOperation().equalsIgnoreCase(APIOperation.CAECRITURE)) {
                return getCommonDescription(extraitCompte, language);
            } else {
                return getTypeOperationDescriptionFromCache(extraitCompte.getIdTypeOperation(), language);
            }
        }
    }

    /**
     * @return
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @param groupWithIdRubrique
     * @return
     */
    private String getFrom(boolean groupWithIdRubrique) {
        StringBuffer from = new StringBuffer(CAExtraitCompteManager.FROM + _getCollection() + CAOperation.TABLE_CAOPERP
                + " a left outer join " + _getCollection() + CAOrdreVersement.TABLE_CAOPOVP + " ov on a."
                + CAOperation.FIELD_IDOPERATION + " = ov." + CAOperation.FIELD_IDORDRE + ", " + _getCollection()
                + CASection.TABLE_CASECTP + " b ");

        if (groupWithIdRubrique) {
            from.append(", " + _getCollection() + CARubrique.TABLE_CARUBRP + " c");
        }

        from.append(", " + _getCollection() + CAJournal.TABLE_CAJOURP + " d");

        return from.toString();
    }

    /**
     * @return
     */
    protected String getFromDate() {
        return fromDate;
    }

    /**
     * @return
     */
    protected String getFromIdExterne() {
        return fromIdExterne;
    }

    /**
     * _getGroupBy n'est pas surchargé. Cette méthode sera ajouté au SELECT directement à la suite de _getWhere.
     * 
     * @return
     */
    private String getGroupBy(boolean groupWithIdRubrique) {
        String groupBy = " " + CAExtraitCompteManager.GROUP_BY + " d." + CAJournal.FIELD_DATEVALEURCG + ",a."
                + CAOperation.FIELD_DATE + ",a." + CAOperation.FIELD_IDSECTION + ",a."
                + CAOperation.FIELD_IDTYPEOPERATION + ",b." + CASection.FIELD_IDEXTERNE + ",b."
                + CASection.FIELD_IDTYPESECTION + ",b." + CASection.FIELD_DATESECTION + ",a."
                + CAOperation.FIELD_IDJOURNAL + ",a." + CAOperation.FIELD_PROVENANCEPMT;

        groupBy += ",a." + CAOperation.FIELD_IDSECTION_COMPENSATION;
        groupBy += ",a." + CAOperation.FIELD_SECTION_COMPENSATION_DE_SUR;

        if (groupWithIdRubrique) {
            groupBy += ",c." + CARubrique.FIELD_LIBELLEEXTRAIT;
        }

        return groupBy;
    }

    /**
     * @return
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return
     */
    public String getLikeIdTypeOperation() {
        return likeIdTypeOperation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.extrait.manager.CAExtraitCompteManager#getOrder ()
     */
    protected String getOrder() {
        if (isOrderedByDateComptable()) {
            return CAExtraitCompteManager.ORDER_BY + CAJournal.FIELD_DATEVALEURCG + ", " + CAOperation.FIELD_DATE
                    + ", " + CAOperation.FIELD_IDTYPEOPERATION;
        } else if (isOrderedByDateValeur()) {
            return CAExtraitCompteManager.ORDER_BY + CAOperation.FIELD_DATE + "," + CAJournal.FIELD_DATEVALEURCG;
        } else if (isOrderedByIdSection()) {
            return CAExtraitCompteManager.ORDER_BY + CASection.FIELD_IDEXTERNE + "," + CAOperation.FIELD_IDSECTION
                    + "," + CAJournal.FIELD_DATEVALEURCG + ", " + CAOperation.FIELD_DATE + ","
                    + CAOperation.FIELD_IDTYPEOPERATION;
        } else {
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.extrait.manager.CAExtraitCompteManager#getSqlQuery (globaz.globall.db.BTransaction)
     */
    public String getPreparedSqlQuery(BTransaction transaction) {
        StringBuffer sql = new StringBuffer(CAExtraitCompteManager.SELECT);
        sql.append("a." + CAOperation.FIELD_DATE + ",a." + CAOperation.FIELD_IDSECTION + ",a."
                + CAOperation.FIELD_IDTYPEOPERATION + ",a." + CAOperation.FIELD_MONTANT + ",a."
                + CASection.FIELD_IDEXTERNE + ",a." + CASection.FIELD_IDTYPESECTION + ",a."
                + CAExtraitCompte.SECTIONDATE_FIELD + ",a." + CARubrique.FIELD_LIBELLEEXTRAIT + ",a."
                + CAOperation.FIELD_PROVENANCEPMT + ",a." + CAOperation.FIELD_IDSECTION_COMPENSATION + ",a."
                + CAOperation.FIELD_SECTION_COMPENSATION_DE_SUR + ",a." + CAJournal.FIELD_DATEVALEURCG);

        sql.append(CAExtraitCompteManager.FROM + " ( ");

        sql.append(CAExtraitCompteManager.SELECT + "a." + CAOperation.FIELD_DATE + ",a." + CAOperation.FIELD_IDSECTION
                + ",a." + CAOperation.FIELD_IDTYPEOPERATION + ",sum(a." + CAOperation.FIELD_MONTANT + ") "
                + CAOperation.FIELD_MONTANT + ",b." + CASection.FIELD_IDEXTERNE + ",b." + CASection.FIELD_IDTYPESECTION
                + ",b." + CASection.FIELD_DATESECTION + " " + CAExtraitCompte.SECTIONDATE_FIELD + ",c."
                + CARubrique.FIELD_LIBELLEEXTRAIT + ",a." + CAOperation.FIELD_PROVENANCEPMT + ",a."
                + CAOperation.FIELD_IDSECTION_COMPENSATION + ",a." + CAOperation.FIELD_SECTION_COMPENSATION_DE_SUR +

                ",d." + CAJournal.FIELD_DATEVALEURCG);

        sql.append(getFrom(true));
        sql.append(CAExtraitCompteManager.WHERE);
        sql.append(getWhere(transaction, true));
        sql.append(getGroupBy(true));

        sql.append(CAExtraitCompteManager.UNION_ALL);

        sql.append(CAExtraitCompteManager.SELECT + "a." + CAOperation.FIELD_DATE + ",a." + CAOperation.FIELD_IDSECTION
                + ",a." + CAOperation.FIELD_IDTYPEOPERATION + ",sum(a." + CAOperation.FIELD_MONTANT + ") "
                + CAOperation.FIELD_MONTANT + ",b." + CASection.FIELD_IDEXTERNE + ",b." + CASection.FIELD_IDTYPESECTION
                + ",b." + CASection.FIELD_DATESECTION + " " + CAExtraitCompte.SECTIONDATE_FIELD + ", 0 as "
                + CARubrique.FIELD_LIBELLEEXTRAIT + ", a." + CAOperation.FIELD_PROVENANCEPMT + ",a."
                + CAOperation.FIELD_IDSECTION_COMPENSATION + ",a." + CAOperation.FIELD_SECTION_COMPENSATION_DE_SUR +

                ", d." + CAJournal.FIELD_DATEVALEURCG);
        sql.append(getFrom(false));
        sql.append(CAExtraitCompteManager.WHERE);
        sql.append(getWhere(transaction, false));
        sql.append(getGroupBy(false));

        sql.append(" ) a " + getOrder());

        return sql.toString();
    }

    /**
     * Return le descripteur de section. Singleton Pattern.
     * 
     * @param extraitCompte
     * @return
     */
    private APISectionDescriptor getSectionDescriptor(CAExtraitCompte extraitCompte) {
        APISectionDescriptor sectionDescriptor = null;
        try {
            // Récupérer le descripteur selon le type de section
            Class cl = Class.forName(extraitCompte.getTypeSection().getNomClasse());
            sectionDescriptor = (APISectionDescriptor) cl.newInstance();
            // Passer la section
            sectionDescriptor.setISession(getSession());
            sectionDescriptor.setSection(extraitCompte.getIdExterne(), extraitCompte.getIdTypeSection(), null,
                    extraitCompte.getSectionDate(), null, null);
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }
        // Retourne le descripteur
        return sectionDescriptor;
    }

    protected String getTypeOperationDescriptionFromCache(String typeOperation, String language) {
        if (CAExtraitCompteManager.descTypeOperationCache == null) {
            try {
                CATypeOperationManager manager = new CATypeOperationManager();
                manager.setSession(getSession());
                manager.find();
                CAExtraitCompteManager.descTypeOperationCache = new Hashtable();
                for (int i = 0; i < manager.size(); i++) {
                    CATypeOperation type = (CATypeOperation) manager.getEntity(i);
                    CAExtraitCompteManager.descTypeOperationCache.put(type.getIdTypeOperation()
                            + CAExtraitCompteManager.LANGUAGE_FR,
                            type.getDescription(CAExtraitCompteManager.LANGUAGE_FR));
                    CAExtraitCompteManager.descTypeOperationCache.put(type.getIdTypeOperation()
                            + CAExtraitCompteManager.LANGUAGE_DE,
                            type.getDescription(CAExtraitCompteManager.LANGUAGE_DE));
                    CAExtraitCompteManager.descTypeOperationCache.put(type.getIdTypeOperation()
                            + CAExtraitCompteManager.LANGUAGE_IT,
                            type.getDescription(CAExtraitCompteManager.LANGUAGE_IT));
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        if (CAExtraitCompteManager.descTypeOperationCache != null) {
            return (String) CAExtraitCompteManager.descTypeOperationCache.get(typeOperation + language.toLowerCase());
        } else {
            CATypeOperation cacheTypeOperation = new CATypeOperation();
            cacheTypeOperation.setISession(getSession());
            cacheTypeOperation.setIdTypeOperation(typeOperation);
            try {
                cacheTypeOperation.retrieve();
                if (cacheTypeOperation.isNew()) {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
            return cacheTypeOperation.getDescription(language);
        }
    }

    /**
     * @return
     */
    public String getUntilDate() {
        return untilDate;
    }

    /**
     * @return
     */
    public String getUntilIdExterne() {
        return untilIdExterne;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    protected String getWhere(BTransaction transaction, boolean groupWithIdRubrique) {
        String where = "";

        where = addIdCompteAnnexeToWhere(where);
        where = addStateToWhere(where);
        where = addFromDateToWhere(where);
        where = addUntilDateToWhere(where);
        where = addLikeIdTypeOperationToWhere(where);

        where = addSoldeTowhere(transaction, where);
        where = addFromIdExterneToWhere(where);
        where = addUntilIdExterneToWhere(where);
        where = addForIdSectionToWhere(where);

        where = addIdSectionBindToWhere(where);
        where = addIdRubriqueBindToWhere(where, groupWithIdRubrique);
        where = addIdJournalBindToWhere(where);
        where = addOrdreVersementPasBloqueToWhere(where);

        return where;
    }

    /**
     * Initialise le Prepared Statement et reset le Entities container.
     */
    public void init(BTransaction transaction) throws SQLException {
        if (select == null) {
            select = transaction.getConnection().prepareStatement(getPreparedSqlQuery(transaction));
        }
        _getContainer().removeAllElements();
    }

    /**
     * Should the query be ordered by Date First ?
     * 
     * @return
     */
    protected boolean isOrderedByDateComptable() {
        return (order.equals(CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE));
    }

    /**
     * Should the query be ordered by Date First ?
     * 
     * @return
     */
    protected boolean isOrderedByDateValeur() {
        return (order.equals(CAExtraitCompteManager.ORDER_BY_DATE_VALEUR));
    }

    /**
     * Should the query be ordered by IdSection First ?
     * 
     * @return
     */
    protected boolean isOrderedByIdSection() {
        return (order.equals(CAExtraitCompteManager.ORDER_BY_IDSECTION));
    }

    /**
     * @param string
     */
    public void setForIdSection(String string) {
        forIdSection = string;
    }

    /**
     * @param string
     */
    public void setFromDate(String string) {
        fromDate = string;
    }

    /**
     * @param string
     */
    public void setFromIdExterne(String string) {
        fromIdExterne = string;
    }

    /**
     * @param string
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setLikeIdTypeOperation(String string) {
        likeIdTypeOperation = string;
    }

    /**
     * @param string
     */
    public void setOrder(String string) {
        order = string;
    }

    /**
     * @param string
     */
    public void setSolde(String string) {
        solde = string;
    }

    /**
     * @param string
     */
    public void setUntilDate(String s) {
        untilDate = s;
    }

    /**
     * @param string
     */
    public void setUntilIdExterne(String string) {
        untilIdExterne = string;
    }
}
