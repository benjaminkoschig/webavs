package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Date de création : (24.01.2002 14:57:39)
 * 
 * @author: Administrator
 */
public class CAOperationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String AND = " AND ";
    public static final String GROUP_BY = " GROUP BY ";
    public static final String INNER_JOIN = " INNER JOIN ";
    public final static String INTERROGATION = "2000";
    public static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";
    public static final String LIKE = " LIKE ";
    public static final String ON = " ON ";
    public static final String OR = " OR ";
    public final static String ORDER_CA_SE_CC_DATE_RU = "1005";
    public final static String ORDER_CC_DATE = "1008";
    public final static String ORDER_CC_NUMOPP = "255003";

    public final static String ORDER_CC_RUB_DATE = "255002";

    public final static String ORDER_COMPTECOURANT_DATE_SECTION = "1011";
    public final static String ORDER_DATE_SECTION_IDOPERATION = "1009";
    public final static String ORDER_DATEOP = "1014";

    public final static String ORDER_DATEOP_DESC = "1015";
    public final static String ORDER_IDOPERATION = "1001";
    public static final String ORDER_IDOPERATION_DESC = "1013";
    // TODO Dal : A EFFACER des suppression ancien mode de calcul des intérêts
    // moratoires.
    public final static String ORDER_JRN_SEC = "1006";

    public final static String ORDER_RUBRIQUE_DATE = "1007";

    public final static String ORDER_RUBRIQUE_DATE_SECTION = "1010";
    public final static String ORDER_SECTION_IDOPERATION = "SECTION_IDOPERATION";
    public final static String SAISIE = "1000";

    private String afterIdOperation = "";
    private Boolean apercuJournal = new Boolean(false);
    private String beforeIdOperation = "";
    private String forAnneeCotisation = "";
    private String forCodeMaster = "";
    private String forDate = "";
    private String forDescription = "";
    private String forEtat = "";
    private List forEtatIn = new ArrayList();
    private List forEtatNotIn = new ArrayList();
    private String forIdCompte = "";
    private String forIdCompteAnnexe = "";
    private String forIdCompteCourant = "";
    private String forIdContrePartie = "";
    private String forIdExterneRole = "";
    private String forIdJournal = "";
    private String forIdOperation = "";
    private String forIdSection = "";
    private String forIdTypeOperation = "";
    private List forIdTypeOperationIn = new ArrayList();
    private ArrayList forIdTypeOperationLikeIn;
    private String forMontantABS = "";
    private String forMontantOperator = "";
    private List forRubriqueIn = new ArrayList();
    private String forSelectionRole = "";
    private String forSelectionSections = "";
    private String forSelectionTri = "";
    private String fromDate = "";
    private String fromDescription = "";
    private String fromIdExterne = "";
    private String fromIdExterneRole = "";
    private String fromIdOperation = "";
    private Boolean isCompteAnnexeBetween = new Boolean(false);
    private Boolean isSectionNonSoldee = new Boolean(false);

    public boolean isSortByCompteAnnexeAndVentile = false;

    private String likeIdTypeOperation = "";
    private Boolean montantNegatif = new Boolean(false);
    private String orderBy = "";
    private Boolean rechercheMontant = new Boolean(false);
    public boolean sortByEstVentileDesc = false;
    private String toIdExterneRole = "";
    private String untilDate = "";
    public Boolean vueOperationCaCcSe = new Boolean(false);
    private Boolean vueOperationCaSe = new Boolean(false);
    private Boolean vueOperationCpteAnnexe = new Boolean(false);
    private Boolean vueOperationRubCC = new Boolean(false);

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (getRechercheMontant().booleanValue()) {
            return _getCollection() + CAOperation.TABLE_CAOPERP + CAOperationManager.LEFT_OUTER_JOIN + _getCollection()
                    + CACompteAnnexe.TABLE_CACPTAP + CAOperationManager.ON + _getCollection()
                    + CAOperation.TABLE_CAOPERP + ".IDCOMPTEANNEXE=" + _getCollection() + "CACPTAP.IDCOMPTEANNEXE"
                    + CAOperationManager.LEFT_OUTER_JOIN + _getCollection() + "CARUBRP ON " + _getCollection()
                    + CAOperation.TABLE_CAOPERP + ".IDCOMPTE=" + _getCollection() + "CARUBRP.IDRUBRIQUE"
                    + CAOperationManager.INNER_JOIN + _getCollection() + "CASECTP ON " + _getCollection()
                    + CAOperation.TABLE_CAOPERP + ".IDSECTION=" + _getCollection() + "CASECTP.IDSECTION";
        } else if (getVueOperationCpteAnnexe().booleanValue()) {
            // return _getCollection()+"CAOPERV3";
            return _getCollection() + CAOperation.TABLE_CAOPERP + CAOperationManager.LEFT_OUTER_JOIN + _getCollection()
                    + CACompteAnnexe.TABLE_CACPTAP + CAOperationManager.ON + _getCollection()
                    + CAOperation.TABLE_CAOPERP + ".IDCOMPTEANNEXE=" + _getCollection() + "CACPTAP.IDCOMPTEANNEXE"
                    + CAOperationManager.LEFT_OUTER_JOIN + _getCollection() + "CARUBRP ON " + _getCollection()
                    + CAOperation.TABLE_CAOPERP + ".IDCOMPTE=" + _getCollection() + "CARUBRP.IDRUBRIQUE";
        } else if (getVueOperationRubCC().booleanValue()) {
            // return _getCollection()+"CAOPERV4";
            return _getCollection() + CAOperation.TABLE_CAOPERP + CAOperationManager.INNER_JOIN + _getCollection()
                    + "CARUBRP ON " + _getCollection() + CAOperation.TABLE_CAOPERP + ".IDCOMPTE=" + _getCollection()
                    + "CARUBRP.IDRUBRIQUE" + CAOperationManager.INNER_JOIN + _getCollection() + "CACPTCP ON "
                    + _getCollection() + CAOperation.TABLE_CAOPERP + ".IDCOMPTECOURANT=" + _getCollection()
                    + "CACPTCP.IDCOMPTECOURANT";
        } else if (getVueOperationCaCcSe().booleanValue()) {
            // return _getCollection()+"CAOPERV5";
            return _getCollection() + CAOperation.TABLE_CAOPERP + CAOperationManager.INNER_JOIN + _getCollection()
                    + "CARUBRP ON " + _getCollection() + CAOperation.TABLE_CAOPERP + ".IDCOMPTE=" + _getCollection()
                    + "CARUBRP.IDRUBRIQUE" + CAOperationManager.LEFT_OUTER_JOIN + _getCollection() + "CACPTCP ON "
                    + _getCollection() + CAOperation.TABLE_CAOPERP + ".IDCOMPTECOURANT=" + _getCollection()
                    + "CACPTCP.IDCOMPTECOURANT" + CAOperationManager.INNER_JOIN + _getCollection() + "CACPTAP ON "
                    + _getCollection() + CAOperation.TABLE_CAOPERP + ".IDCOMPTEANNEXE=" + _getCollection()
                    + "CACPTAP.IDCOMPTEANNEXE" + CAOperationManager.INNER_JOIN + _getCollection() + "CASECTP ON "
                    + _getCollection() + CAOperation.TABLE_CAOPERP + ".IDSECTION=" + _getCollection()
                    + "CASECTP.IDSECTION";
        } else if (getVueOperationCaSe().booleanValue()) {
            // return _getCollection()+"CAOPERV6";
            return _getCollection() + CAOperation.TABLE_CAOPERP + CAOperationManager.INNER_JOIN + _getCollection()
                    + "CASECTP ON " + _getCollection() + CAOperation.TABLE_CAOPERP + ".IDSECTION=" + _getCollection()
                    + "CASECTP.IDSECTION";
        } else if (isSortByEstVentileDesc() || isSortByCompteAnnexeAndVentile()) {
            return _getCollection() + CAOperation.TABLE_CAOPERP + CAOperationManager.LEFT_OUTER_JOIN + _getCollection()
                    + "CARUBRP ON " + _getCollection() + CAOperation.TABLE_CAOPERP + ".IDCOMPTE=" + _getCollection()
                    + "CARUBRP.IDRUBRIQUE";
        } else if (!getForRubriqueIn().isEmpty()) {
            return _getCollection() + CAOperation.TABLE_CAOPERP + CAOperationManager.INNER_JOIN + _getCollection()
                    + "CARUBRP ON " + _getCollection() + CAOperation.TABLE_CAOPERP + ".IDCOMPTE=" + _getCollection()
                    + "CARUBRP.IDRUBRIQUE";
        } else {
            return _getCollection() + CAOperation.TABLE_CAOPERP;
        }
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {

        String _order = "";
        if (getRechercheMontant().booleanValue()) {
            _order = _getCollection() + "CACPTAP.IDEXTERNEROLE, " + _getCollection() + "CASECTP.IDEXTERNE DESC, "
                    + _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE + " DESC";
        } else if (getVueOperationCpteAnnexe().booleanValue() && !getApercuJournal().booleanValue()) {
            if (getForSelectionTri().equalsIgnoreCase("2")) {
                _order = _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE;
            } else if (getForSelectionTri().equalsIgnoreCase("3")) {
                _order = _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE + ", "
                        + _getCollection() + "CACPTAP.IDEXTERNEROLE, " + _getCollection() + "CAOPERP.IDSECTION";
            } else if (getForSelectionTri().equalsIgnoreCase("4")) {
                _order = _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE;
            } else {
                _order = _getCollection() + "CACPTAP.IDEXTERNEROLE, " + _getCollection() + "CAOPERP.IDSECTION, "
                        + _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE;
            }
        } else if (getVueOperationRubCC().booleanValue()) {
            if (getForSelectionTri().equalsIgnoreCase(CAOperationManager.ORDER_CC_RUB_DATE)) {
                _order = _getCollection() + "CACPTCP.IDEXTERNE," + _getCollection() + "CARUBRP.IDEXTERNE, DATE";
            } else if (getForSelectionTri().equals(CAOperationManager.ORDER_RUBRIQUE_DATE)) {
                _order = _getCollection() + "CARUBRP.IDEXTERNE," + _getCollection() + CAOperation.TABLE_CAOPERP + "."
                        + CAOperation.FIELD_DATE;
            } else if (getForSelectionTri().equalsIgnoreCase(CAOperationManager.ORDER_CC_NUMOPP)) {
                _order = _getCollection() + "CACPTCP.IDEXTERNE," + _getCollection() + CAOperation.TABLE_CAOPERP + "."
                        + CAOperation.FIELD_IDOPERATION;
            } else {
                _order = _getCollection() + "CACPTCP.IDEXTERNE,DATE," + _getCollection() + "CARUBRP.IDEXTERNE";
            }
        } else if (isSortByEstVentileDesc()) {
            _order = _getCollection() + "CARUBRP.ESTVENTILEE desc" + ", IDSECTION, " + _getCollection()
                    + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE;
        } else if (isSortByCompteAnnexeAndVentile()) {
            _order = _getCollection() + "CARUBRP.ESTVENTILEE desc" + ", IDSECTION, " + _getCollection()
                    + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE;
        }

        else {
            // Tri pour l'ordre de l'aperçu du journal
            if (getVueOperationCpteAnnexe().booleanValue() && getApercuJournal().booleanValue()) {
                if (getForSelectionTri().equalsIgnoreCase("2")) { // date,
                    // numéro
                    _order = _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE + ", "
                            + _getCollection() + "CACPTAP.IDEXTERNEROLE, " + _getCollection() + "CAOPERP.IDSECTION";
                } else if (getForSelectionTri().equalsIgnoreCase("3")) { // nom
                    _order = "IDSECTION";
                } else if (getForSelectionTri().equalsIgnoreCase("4")) { // numero
                    _order = _getCollection() + "CACPTAP.IDEXTERNEROLE, " + _getCollection() + "CAOPERP.IDSECTION";
                } else if (getForSelectionTri().equalsIgnoreCase("30")) {
                    _order = CACompteAnnexe.FIELD_DESCUPCASE;
                } else if (getForSelectionTri().equalsIgnoreCase("1000")) { // date,
                    // nom
                    _order = _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDOPERATION
                            + " DESC";
                } else if (getForSelectionTri().equalsIgnoreCase("5")) { // date
                    // de
                    // saisie
                    _order = _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDOPERATION;
                } else {
                    _order = _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE + ", "
                            + CAOperation.FIELD_IDSECTION;
                }
            } else if (getRechercheMontant().booleanValue()) {
                if (getForSelectionTri().equalsIgnoreCase("1")) {
                    _order = "IDEXTERNE";
                } else {
                    _order = _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE
                            + ", IDEXTERNE";
                }
            } else if (getVueOperationCaSe().booleanValue()) {
                if (getForSelectionTri().equalsIgnoreCase("1")) {
                    _order = "IDEXTERNE";
                } else {
                    _order = _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE
                            + ", IDEXTERNE";
                }
            } else if (getVueOperationCaCcSe().booleanValue()) {
                if (getForSelectionTri().equalsIgnoreCase(CAOperationManager.ORDER_DATE_SECTION_IDOPERATION)) {
                    _order = _getCollection() + "CAOPERP.DATE, " + _getCollection() + "CASECTP.IDEXTERNE, IDOPERATION";
                } else if (getForSelectionTri().equals(CAOperationManager.ORDER_RUBRIQUE_DATE_SECTION)) {
                    _order = _getCollection() + "CARUBRP.IDEXTERNE," + _getCollection() + CAOperation.TABLE_CAOPERP
                            + "." + CAOperation.FIELD_DATE + ", " + _getCollection() + "CASECTP.IDEXTERNE";
                } else if (getForSelectionTri().equals(CAOperationManager.ORDER_COMPTECOURANT_DATE_SECTION)) {
                    _order = _getCollection() + "CACPTCP.IDEXTERNE," + _getCollection() + CAOperation.TABLE_CAOPERP
                            + "." + CAOperation.FIELD_DATE + ", " + _getCollection() + "CASECTP.IDEXTERNE";
                } else if (getForSelectionTri().equals(CAOperationManager.ORDER_RUBRIQUE_DATE)) {
                    _order = _getCollection() + "CARUBRP.IDEXTERNE," + _getCollection() + CAOperation.TABLE_CAOPERP
                            + "." + CAOperation.FIELD_DATE + ", " + _getCollection() + "CASECTP.IDEXTERNE";
                }
            } else {
                // Tri par id opération
                if (getOrderBy().equals(CAOperationManager.ORDER_IDOPERATION)) {
                    _order = "IDOPERATION";
                } else if (getOrderBy().equals(CAOperationManager.ORDER_CA_SE_CC_DATE_RU)) {
                    _order = _getCollection() + "CACPTAP.IDROLE," + _getCollection() + "CACPTAP.IDEXTERNEROLE,"
                            + _getCollection() + "CASECTP.IDEXTERNE," + _getCollection() + "CACPTCP.IDEXTERNE,"
                            + _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE + ","
                            + _getCollection() + "CARUBRP.IDEXTERNE";
                } else if (getOrderBy().equals(CAOperationManager.ORDER_JRN_SEC)) {
                    // TODO Dal : A EFFACER des suppression ancien mode de
                    // calcul des intérêts moratoires.
                    _order = "IDJOURNAL, IDSECTION";
                    // Tri par rubrique (idExterne) et date
                } else if (getOrderBy().equals(CAOperationManager.ORDER_RUBRIQUE_DATE)) {
                    _order = _getCollection() + "CARUBRP.IDEXTERNE," + _getCollection() + CAOperation.TABLE_CAOPERP
                            + "." + CAOperation.FIELD_DATE;
                } else if (getOrderBy().equals(CAOperationManager.ORDER_CC_DATE)) {
                    _order = _getCollection() + "CACPTCP.IDEXTERNE," + _getCollection() + CAOperation.TABLE_CAOPERP
                            + "." + CAOperation.FIELD_DATE;
                } else if (getOrderBy().equals(CAOperationManager.ORDER_SECTION_IDOPERATION)) {
                    _order = _getCollection() + "CACPTCP" + "." + CAOperation.FIELD_IDSECTION + "," + _getCollection()
                            + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDOPERATION;
                } else if (getOrderBy().equals(CAOperationManager.ORDER_IDOPERATION_DESC)) {
                    _order = "IDOPERATION DESC";
                } else if (getOrderBy().equals(CAOperationManager.ORDER_DATEOP)) {
                    _order = "DATE ASC";
                } else if (getOrderBy().equals(CAOperationManager.ORDER_DATEOP_DESC)) {
                    _order = "DATE DESC";
                }
            }
        }

        return _order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer("");

        // traitement du positionnement selon le rôle
        if ((getForSelectionRole().length() != 0) && !getForSelectionRole().equalsIgnoreCase("1000")) {
            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);
                addCondition(sqlWhere, "(" + _getCollection() + "CACPTAP.IDROLE IN (");

                for (int id = 0; id < roles.length; ++id) {
                    if (id > 0) {
                        sqlWhere.append(',');
                    }
                    sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), roles[id]));
                }
                sqlWhere.append(") OR " + _getCollection() + "CACPTAP.IDROLE IS NULL) ");
            } else {
                addCondition(
                        sqlWhere,
                        _getCollection() + "CACPTAP.IDROLE="
                                + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole()));
            }
        }
        // traitement du positionnement selon le numéro de journal
        if (getForIdJournal().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDJOURNAL
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal()));
        }

        // traitement du positionnement selon l'idRubrique
        if (getForIdCompte().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDCOMPTE
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCompte()));
        }

        // Traitement du positionnement selon la date
        if (getForDate().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE + "="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDate()));
        }

        // Traitement du positionnement selon la date
        if (getFromDate().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate()));
        }

        // Traitement du positionnement selon la date
        if (getUntilDate().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_DATE + "<="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getUntilDate()));
        }

        // traitement du positionnement selon l'année de cotisation
        if (getForAnneeCotisation().length() != 0) {
            addCondition(
                    sqlWhere,
                    _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_ANNEECOTISATION + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForAnneeCotisation()));
        }

        // Traitement du positionnement selon l'état
        if (getForEtat().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_ETAT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForEtat()));
        }

        // traitement du positionnement selon le numéro de section
        if (getForIdSection().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDSECTION
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSection()));
        }

        // traitement du positionnement selon le numéro de l'opération
        if (getFromIdOperation().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDOPERATION
                    + ">=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdOperation()));
        }

        // traitement du positionnement selon le numéro de l'opération
        if (getForIdOperation().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDOPERATION
                    + "=" + this._dbWriteNumeric(statement.getTransaction(), getForIdOperation()));
        }

        // traitement du positionnement selon le type d'opération pour autant
        // qu'on accède à la vue
        if (getVueOperationRubCC().booleanValue()) {
            if (getForIdTypeOperation().length() != 0) {
                if (!getForIdTypeOperation().equalsIgnoreCase("1000")) {
                    addCondition(sqlWhere,
                            _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDTYPEOPERATION
                                    + "=" + this._dbWriteString(statement.getTransaction(), getForIdTypeOperation())
                                    + "AND ETAT IN (" + APIOperation.ETAT_COMPTABILISE + ", "
                                    + APIOperation.ETAT_PROVISOIRE + ")");
                } else {
                    addCondition(sqlWhere, "ETAT IN (" + APIOperation.ETAT_COMPTABILISE + ", "
                            + APIOperation.ETAT_PROVISOIRE + ")");
                }
            }
        }

        // traitement du positionnement selon le type d'opération
        if (!JadeStringUtil.isBlank(getForIdTypeOperation()) && !getForIdTypeOperation().equals("1000")
                && !getVueOperationRubCC().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CAOperationManager.AND);
            }
            if (getForIdTypeOperation().equals(APIOperation.CAECRITURE)) {
                sqlWhere.append(" ( ");
            }

            sqlWhere.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDTYPEOPERATION
                    + "=" + this._dbWriteString(statement.getTransaction(), getForIdTypeOperation()));

            if (getForIdTypeOperation().equals(APIOperation.CAECRITURE)) {
                sqlWhere.append(CAOperationManager.OR);
                sqlWhere.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDTYPEOPERATION
                        + "=" + this._dbWriteString(statement.getTransaction(), APIOperation.CAECRITURECOMPENSATION));
                sqlWhere.append(" ) ");
            }
        }

        // traitement du positionnement selon le compte annexe
        if (!getVueOperationCpteAnnexe().booleanValue()) {
            if (getForIdCompteAnnexe().length() != 0) {
                addCondition(sqlWhere,
                        _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDCOMPTEANNEXE + "="
                                + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe()));
            }
        }

        // traitement du positionnement selon le compte courant
        if (getForIdCompteCourant().length() != 0) {
            addCondition(
                    sqlWhere,
                    _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDCOMPTECOURANT + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteCourant()));
        }

        // traitement du positionnement
        if (getLikeIdTypeOperation().length() != 0) {
            addCondition(
                    sqlWhere,
                    _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDTYPEOPERATION
                            + CAOperationManager.LIKE
                            + this._dbWriteString(statement.getTransaction(), getLikeIdTypeOperation() + "%"));
        }

        // traitement du positionnement selon le numéro de l'opération
        if (getAfterIdOperation().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDOPERATION
                    + ">" + this._dbWriteNumeric(statement.getTransaction(), getAfterIdOperation()));
        }
        // traitement du positionnement selon le numéro de l'opération
        if (getBeforeIdOperation().length() != 0) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDOPERATION
                    + "<" + this._dbWriteNumeric(statement.getTransaction(), getBeforeIdOperation()));
        }

        // traitement du code master
        if (getForCodeMaster().length() != 0) {
            if (getForCodeMaster().equalsIgnoreCase(CAOperationManager.SAISIE)) {
                addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "."
                        + CAOperation.FIELD_CODEMASTER + "<" + APIOperation.SLAVE);
            } else {
                addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "."
                        + CAOperation.FIELD_CODEMASTER + " IN (" + APIOperation.SINGLE + "," + APIOperation.SLAVE + ")");
            }
        }

        // traitement du positionnement selon l'idExterneRole du compte annexe
        // pour autant qu'on accède à la vue
        if (getVueOperationCpteAnnexe().booleanValue()) {
            if (getForIdExterneRole().length() != 0) {
                addCondition(sqlWhere,
                        "IDEXTERNEROLE=" + this._dbWriteString(statement.getTransaction(), getForIdExterneRole()));
            }
        }

        // traitement du positionnement depuis l'idExterneRole du compte annexe
        // pour autant qu'on accède à la vue
        if (getVueOperationCpteAnnexe().booleanValue()) {
            if (getFromIdExterneRole().length() != 0) {
                if (getForSelectionTri().equalsIgnoreCase("1") || getForSelectionTri().equalsIgnoreCase("3")) {
                    addCondition(sqlWhere,
                            "IDEXTERNEROLE>=" + this._dbWriteString(statement.getTransaction(), getFromIdExterneRole()));
                } else {
                    addCondition(
                            sqlWhere,
                            CACompteAnnexe.FIELD_DESCUPCASE
                                    + ">="
                                    + this._dbWriteString(statement.getTransaction(), JadeStringUtil
                                            .convertSpecialChars(getFromIdExterneRole()).toUpperCase()));
                }
            }
        }
        // traitement du positionnement jusqu'à l'idExterneRole du compte annexe
        // pour autant qu'on accède à la vue
        if (getCompteAnnexeBetween().booleanValue()) {
            if (getFromIdExterneRole().length() != 0) {
                addCondition(sqlWhere,
                        "IDEXTERNEROLE>=" + this._dbWriteString(statement.getTransaction(), getFromIdExterneRole()));
            }
            if (getToIdExterneRole().length() != 0) {
                addCondition(sqlWhere,
                        "IDEXTERNEROLE<=" + this._dbWriteString(statement.getTransaction(), getToIdExterneRole()));
            }
        }

        // traitement du positionnement à partir de la description (nom)
        if (getVueOperationCpteAnnexe().booleanValue()) {
            if (getFromDescription().length() != 0) {
                addCondition(
                        sqlWhere,
                        CACompteAnnexe.FIELD_DESCUPCASE
                                + ">="
                                + this._dbWriteString(statement.getTransaction(),
                                        JadeStringUtil.convertSpecialChars(getFromDescription()).toUpperCase()));
            }
        }
        // traitment du positionnement à partir de l'idExterne dans la vue
        // caoperv6
        if (getVueOperationCaSe().booleanValue()) {
            if (getFromIdExterne().length() != 0) {
                addCondition(
                        sqlWhere,
                        _getCollection() + "CASECTP.IDEXTERNE>="
                                + this._dbWriteString(statement.getTransaction(), getFromIdExterne()));
            }
        }
        // traitment du positionnement à partir de l'idExterne dans la vue
        // caoperv6
        if (getRechercheMontant().booleanValue()) {
            if (getFromIdExterne().length() != 0) {
                addCondition(
                        sqlWhere,
                        _getCollection() + "CASECTP.IDEXTERNE>="
                                + this._dbWriteString(statement.getTransaction(), getFromIdExterne()));
            }
        }
        // traitment du positionnement à partir du solde de la section dans la
        // vue caoperv6
        if (getVueOperationCaSe().booleanValue()) {
            if ((getForSelectionSections().length() != 0) && !getForSelectionSections().equalsIgnoreCase("1000")) {
                if (getForSelectionSections().equalsIgnoreCase("1")) {
                    addCondition(sqlWhere, _getCollection() + "CASECTP.SOLDE <> 0");
                } else {
                    addCondition(sqlWhere, _getCollection() + "CASECTP.SOLDE = 0");
                }
            }
        }
        // traitement du positionnement lister que les sections non soldées
        if (getForSectionNonSoldee().booleanValue()) {
            addCondition(sqlWhere, _getCollection() + "CASECTP.SOLDE <> 0");
        }

        if ((getForEtatNotIn() != null) && (getForEtatNotIn().size() > 0)) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_ETAT
                    + " NOT IN (");
            Iterator iter = getForEtatNotIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
                sqlWhere.append(element + ",");
            }
            sqlWhere.replace(sqlWhere.length() - 1, sqlWhere.length(), ")");
        }

        if ((getForEtatIn() != null) && (getForEtatIn().size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CAOperationManager.AND);
            }
            sqlWhere.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_ETAT + " IN (");
            Iterator iter = getForEtatIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
                sqlWhere.append(element + ",");
            }
            sqlWhere.replace(sqlWhere.length() - 1, sqlWhere.length(), ")");
        }
        if ((getForIdTypeOperationIn() != null) && (getForIdTypeOperationIn().size() > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(CAOperationManager.AND);
            }
            sqlWhere.append(_getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDTYPEOPERATION
                    + " IN (");
            Iterator iter = getForIdTypeOperationIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();
                sqlWhere.append("'" + element + "'" + ",");
            }
            sqlWhere.replace(sqlWhere.length() - 1, sqlWhere.length(), ")");
        }
        // traitement du positionnement montant négatif
        if (getMontantNegatif().booleanValue()) {
            addCondition(sqlWhere, _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_MONTANT
                    + " < 0 ");
        }
        if (getForIdTypeOperationLikeIn() != null) {
            String tmp = "";

            Iterator iter = getForIdTypeOperationLikeIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();

                if (tmp.length() != 0) {
                    tmp += CAOperationManager.OR;
                }

                tmp += _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDTYPEOPERATION
                        + CAOperationManager.LIKE + this._dbWriteString(statement.getTransaction(), element + "%");
            }

            if (!JadeStringUtil.isBlank(tmp)) {
                addCondition(sqlWhere, "(" + tmp + ")");
            }
        }

        if (getForRubriqueIn() != null) {
            String tmp = "";

            Iterator iter = getForRubriqueIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();

                if (tmp.length() != 0) {
                    tmp += CAOperationManager.OR;
                }
                tmp += _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_IDEXTERNE
                        + CAOperationManager.LIKE + this._dbWriteString(statement.getTransaction(), element + "%");
            }

            if (!JadeStringUtil.isBlank(tmp)) {
                addCondition(sqlWhere, "(" + tmp + ")");
            }
        }

        // Traitement du positionnement selon le montant absolu
        if (getForMontantABS().length() != 0) {
            if (getForMontantOperator().length() != 0) {
                addCondition(
                        sqlWhere,
                        "ABS(MONTANT)" + getForMontantOperator() + "ABS("
                                + this._dbWriteNumeric(statement.getTransaction(), getForMontantABS()) + ")");
            } else {
                addCondition(sqlWhere,
                        "ABS(MONTANT)=" + "ABS(" + this._dbWriteNumeric(statement.getTransaction(), getForMontantABS())
                                + ")");
            }
        }

        if (!JadeStringUtil.isEmpty(forIdContrePartie)) {
            addCondition(
                    sqlWhere,
                    CAOperation.FIELD_IDCONTREPARTIE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), forIdContrePartie));
        }

        // traitement du positionnement selon l'idCompteAnnexe
        if (getForIdCompteAnnexe().length() != 0) {
            addCondition(
                    sqlWhere,
                    _getCollection() + CAOperation.TABLE_CAOPERP + "." + CAOperation.FIELD_IDCOMPTEANNEXE + "="
                            + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe()));
        }

        return sqlWhere.toString();
    }

    /**
     * new entity
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAOperation();
    }

    /**
     * Permet l'ajout d'une condition dans la clause WHERE. <br>
     * 
     * @param sqlWhere
     * @param condition
     *            à ajouter au where
     */
    private void addCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(CAOperationManager.AND);
        }
        sqlWhere.append(condition);
    }

    /**
     * Date de création : (29.01.2002 10:18:25)
     * 
     * @return String
     */
    public String getAfterIdOperation() {
        return afterIdOperation;
    }

    /**
     * Date de création : (07.05.2002 14:01:45)
     * 
     * @return Boolean
     */
    public Boolean getApercuJournal() {
        return apercuJournal;
    }

    public String getBeforeIdOperation() {
        return beforeIdOperation;
    }

    /**
     * @return
     */
    public Boolean getCompteAnnexeBetween() {
        return isCompteAnnexeBetween;
    }

    /**
     * @return
     */
    public String getForAnneeCotisation() {
        return forAnneeCotisation;
    }

    /**
     * Date de création : (11.03.2002 11:49:58)
     * 
     * @return String
     */
    public String getForCodeMaster() {
        return forCodeMaster;
    }

    /**
     * Date de création : (22.02.2002 13:10:14)
     * 
     * @return String
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * Date de création : (06.05.2002 15:13:28)
     * 
     * @return String
     */
    public String getForDescription() {
        return forDescription;
    }

    /**
     * Date de création : (22.02.2002 14:01:13)
     * 
     * @return String
     */
    public String getForEtat() {
        return forEtat;
    }

    /**
     * @return
     */
    public List getForEtatIn() {
        return forEtatIn;
    }

    /**
     * Returns the forEtatNotIn.
     * 
     * @return List
     */
    public List getForEtatNotIn() {
        return forEtatNotIn;
    }

    /**
     * Date de création : (06.02.2002 14:04:05)
     * 
     * @return String
     */
    public String getForIdCompte() {
        return forIdCompte;
    }

    /**
     * Date de création : (06.02.2002 14:03:50)
     * 
     * @return String
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /**
     * Date de création : (06.02.2002 14:07:19)
     * 
     * @return String
     */
    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    public String getForIdContrePartie() {
        return forIdContrePartie;
    }

    /**
     * Date de création : (02.04.2002 16:15:16)
     * 
     * @return String
     */
    public String getForIdExterneRole() {
        return forIdExterneRole;
    }

    /**
     * Date de création : (24.01.2002 14:59:53)
     * 
     * @return String
     */
    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * @return the forIdOperation
     */
    public String getForIdOperation() {
        return forIdOperation;
    }

    /**
     * Date de création : (06.02.2002 15:13:11)
     * 
     * @return String
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * Date de création : (06.02.2002 13:51:35)
     * 
     * @return String
     */
    public String getForIdTypeOperation() {
        return forIdTypeOperation;
    }

    /**
     * @return the forIdTypeOperationIn
     */
    public List getForIdTypeOperationIn() {
        return forIdTypeOperationIn;
    }

    /**
     * @return
     */
    public ArrayList getForIdTypeOperationLikeIn() {
        return forIdTypeOperationLikeIn;
    }

    /**
     * @return
     */
    public String getForMontantABS() {
        return forMontantABS;
    }

    public String getForMontantOperator() {
        return forMontantOperator;
    }

    /**
     * @return the forRubriqueIn
     */
    public List getForRubriqueIn() {
        return forRubriqueIn;
    }

    /**
     * @return
     */
    public Boolean getForSectionNonSoldee() {
        return isSectionNonSoldee;
    }

    /**
     * peut contenir des ','
     * 
     * @see #setForSelectionRole(String)
     * @return String
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Date de création : (29.10.2002 07:49:07)
     * 
     * @return String
     */
    public String getForSelectionSections() {
        return forSelectionSections;
    }

    /**
     * Date de création : (04.03.2002 15:37:10)
     * 
     * @return String
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * Date de création : (22.02.2002 13:10:52)
     * 
     * @return String
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Date de création : (06.05.2002 15:14:45)
     * 
     * @return String
     */
    public String getFromDescription() {
        return fromDescription;
    }

    /**
     * Date de création : (29.10.2002 07:49:48)
     * 
     * @return String
     */
    public String getFromIdExterne() {
        return fromIdExterne;
    }

    /**
     * Date de création : (27.02.2002 11:26:23)
     * 
     * @return String
     */
    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    /**
     * Date de création : (24.01.2002 15:32:36)
     * 
     * @return String
     */
    public String getFromIdOperation() {
        return fromIdOperation;
    }

    /**
     * Date de création : (06.02.2002 13:52:41)
     * 
     * @return String
     */
    public String getLikeIdTypeOperation() {
        return likeIdTypeOperation;
    }

    /**
     * @return the montantNegatif
     */
    public Boolean getMontantNegatif() {
        return montantNegatif;
    }

    /**
     * Date de création : (24.01.2002 17:25:06)
     * 
     * @return String
     */
    public String getOrderBy() {
        return orderBy;
    }

    public Boolean getRechercheMontant() {
        return rechercheMontant;
    }

    /**
     * @return the toIdExterneRole
     */
    public String getToIdExterneRole() {
        return toIdExterneRole;
    }

    /**
     * Date de création : (22.02.2002 13:10:41)
     * 
     * @return String
     */
    public String getUntilDate() {
        return untilDate;
    }

    /**
     * Date de création : (14.03.2002 11:15:30)
     * 
     * @return Boolean
     */
    public Boolean getVueOperationCaCcSe() {
        return vueOperationCaCcSe;
    }

    /**
     * Date de création : (29.10.2002 07:47:53)
     * 
     * @return Boolean
     */
    public Boolean getVueOperationCaSe() {
        return vueOperationCaSe;
    }

    /**
     * Date de création : (27.02.2002 08:45:58)
     * 
     * @return Boolean
     */
    public Boolean getVueOperationCpteAnnexe() {
        return vueOperationCpteAnnexe;
    }

    /**
     * Date de création : (27.02.2002 08:45:58)
     * 
     * @return Boolean
     */
    public Boolean getVueOperationRubCC() {
        return vueOperationRubCC;
    }

    public boolean isSortByCompteAnnexeAndVentile() {
        return isSortByCompteAnnexeAndVentile;
    }

    /**
     * @return
     */
    public boolean isSortByEstVentileDesc() {
        return sortByEstVentileDesc;
    }

    /**
     * Date de création : (29.01.2002 10:18:25)
     * 
     * @param newAfterIdOperation
     *            String
     */
    public void setAfterIdOperation(String newAfterIdOperation) {
        afterIdOperation = newAfterIdOperation;
    }

    /**
     * Date de création : (07.05.2002 14:01:45)
     * 
     * @param newApercuJournal
     *            String
     */
    public void setApercuJournal(String newApercuJournal) {
        try {
            apercuJournal = Boolean.valueOf(newApercuJournal);
        } catch (Exception ex) {
            apercuJournal = new Boolean(false);
        }
    }

    public void setBeforeIdOperation(String newBeforeIdOperation) {
        beforeIdOperation = newBeforeIdOperation;
    }

    public void setCompteAnnexeBetween(Boolean value) {
        try {
            isCompteAnnexeBetween = Boolean.valueOf(value);
        } catch (Exception ex) {
            isCompteAnnexeBetween = new Boolean(false);
        }
    }

    /**
     * @param string
     */
    public void setForAnneeCotisation(String string) {
        forAnneeCotisation = string;
    }

    /**
     * Date de création : (11.03.2002 11:49:58)
     * 
     * @param newForCodeMaster
     *            String
     */
    public void setForCodeMaster(String newForCodeMaster) {
        forCodeMaster = newForCodeMaster;
    }

    /**
     * Date de création : (22.02.2002 13:10:14)
     * 
     * @param newForDate
     *            String
     */
    public void setForDate(String newForDate) {
        forDate = newForDate;
    }

    /**
     * Date de création : (06.05.2002 15:13:28)
     * 
     * @param newForDescription
     *            String
     */
    public void setForDescription(String newForDescription) {
        forDescription = newForDescription;
    }

    /**
     * Date de création : (22.02.2002 14:01:13)
     * 
     * @param newForEtat
     *            String
     */
    public void setForEtat(String newForEtat) {
        forEtat = newForEtat;
    }

    /**
     * @param list
     */
    public void setForEtatIn(List list) {
        forEtatIn = list;
    }

    /**
     * Sets the forEtatNotIn.
     * 
     * @param forEtatNotIn
     *            The forEtatNotIn to set
     */
    public void setForEtatNotIn(List forEtatNotIn) {
        this.forEtatNotIn = forEtatNotIn;
    }

    /**
     * Date de création : (06.02.2002 14:04:05)
     * 
     * @param newForIdCompte
     *            String
     */
    public void setForIdCompte(String newForIdCompte) {
        forIdCompte = newForIdCompte;
    }

    /**
     * Date de création : (06.02.2002 14:03:50)
     * 
     * @param newForIdCompteAnnexe
     *            String
     */
    public void setForIdCompteAnnexe(String newForIdCompteAnnexe) {
        forIdCompteAnnexe = newForIdCompteAnnexe;
    }

    /**
     * Date de création : (06.02.2002 14:07:19)
     * 
     * @param newForIdCompteCourant
     *            String
     */
    public void setForIdCompteCourant(String newForIdCompteCourant) {
        forIdCompteCourant = newForIdCompteCourant;
    }

    public void setForIdContrePartie(String string) {
        forIdContrePartie = string;
    }

    /**
     * Date de création : (02.04.2002 16:15:16)
     * 
     * @param newForIdExterneRole
     *            String
     */
    public void setForIdExterneRole(String newForIdExterneRole) {
        forIdExterneRole = newForIdExterneRole;
    }

    /**
     * Date de création : (24.01.2002 14:59:53)
     * 
     * @param newForIdJournal
     *            String
     */
    public void setForIdJournal(String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    /**
     * @param forIdOperation
     *            the forIdOperation to set
     */
    public void setForIdOperation(String forIdOperation) {
        this.forIdOperation = forIdOperation;
    }

    /**
     * Date de création : (06.02.2002 15:13:11)
     * 
     * @param newForIdSection
     *            String
     */
    public void setForIdSection(String newForIdSection) {
        forIdSection = newForIdSection;
    }

    /**
     * Date de création : (06.02.2002 13:51:35)
     * 
     * @param newForIdTypeOperation
     *            String
     */
    public void setForIdTypeOperation(String newForIdTypeOperation) {
        forIdTypeOperation = newForIdTypeOperation;
    }

    /**
     * @param forIdTypeOperationIn
     *            the forIdTypeOperationIn to set
     */
    public void setForIdTypeOperationIn(List forIdTypeOperationIn) {
        this.forIdTypeOperationIn = forIdTypeOperationIn;
    }

    /**
     * @param list
     */
    public void setForIdTypeOperationLikeIn(ArrayList list) {
        forIdTypeOperationLikeIn = list;
    }

    /**
     * @param string
     */
    public void setForMontantABS(String string) {
        forMontantABS = string;
    }

    /**
     * @param forRubriqueIn
     *            the forRubriqueIn to set
     */
    public void setForRubriqueIn(List forRubriqueIn) {
        this.forRubriqueIn = forRubriqueIn;
    }

    /**
     * @param boolean1
     */
    public void setForSectionNonSoldee(String value) {
        try {
            isSectionNonSoldee = Boolean.valueOf(value);
        } catch (Exception ex) {
            isSectionNonSoldee = new Boolean(false);
        }
    }

    /**
     * newForSelectionRole peut contenir des ',' séparant plusieurs id de role, dans ce cas, le manager retourne toutes
     * les operations pour un compte annexe dont l'id du role est l'un de ceux transmis dans newForSelectionRole.
     * 
     * @param newForSelectionRole
     *            String
     */
    public void setForSelectionRole(String newForSelectionRole) {
        forSelectionRole = newForSelectionRole;
    }

    /**
     * Date de création : (29.10.2002 07:49:07)
     * 
     * @param newForSelectionSections
     *            String
     */
    public void setForSelectionSections(String newForSelectionSections) {
        forSelectionSections = newForSelectionSections;
    }

    /**
     * Date de création : (04.03.2002 15:37:10)
     * 
     * @param newForSelectionTri
     *            String
     */
    public void setForSelectionTri(String newForSelectionTri) {
        forSelectionTri = newForSelectionTri;
    }

    public void setForSoldeOperator(String forMontantOperator) {
        this.forMontantOperator = forMontantOperator;
    }

    /**
     * Date de création : (22.02.2002 13:10:52)
     * 
     * @param newFromDate
     *            String
     */
    public void setFromDate(String newFromDate) {
        fromDate = newFromDate;
    }

    /**
     * Date de création : (06.05.2002 15:14:45)
     * 
     * @param newFromDescription
     *            String
     */
    public void setFromDescription(String newFromDescription) {
        fromDescription = newFromDescription;
    }

    /**
     * Date de création : (29.10.2002 07:49:48)
     * 
     * @param newFromIdExterne
     *            String
     */
    public void setFromIdExterne(String newFromIdExterne) {
        fromIdExterne = newFromIdExterne;
    }

    /**
     * Date de création : (27.02.2002 11:26:23)
     * 
     * @param newFromIdExterneRole
     *            String
     */
    public void setFromIdExterneRole(String newFromIdExterneRole) {
        fromIdExterneRole = newFromIdExterneRole;
    }

    /**
     * Date de création : (24.01.2002 15:32:36)
     * 
     * @param newFromIdOperation
     *            String
     */
    public void setFromIdOperation(String newFromIdOperation) {
        fromIdOperation = newFromIdOperation;
    }

    /**
     * Date de création : (06.02.2002 13:52:41)
     * 
     * @param newLikeIdTypeOperation
     *            String
     */
    public void setLikeIdTypeOperation(String newLikeIdTypeOperation) {
        likeIdTypeOperation = newLikeIdTypeOperation;
    }

    /**
     * @param montantNegatif
     *            the montantNegatif to set
     */
    public void setMontantNegatif(Boolean montantNegatif) {
        this.montantNegatif = montantNegatif;
    }

    /**
     * Date de création : (24.01.2002 17:25:06)
     * 
     * @param newOrdreBy
     *            String
     */
    public void setOrderBy(String newOrderBy) {
        orderBy = newOrderBy;
    }

    public void setRechercheMontant(String newRechercheMontant) {
        try {
            rechercheMontant = Boolean.valueOf(newRechercheMontant);
        } catch (Exception ex) {
            rechercheMontant = new Boolean(false);
        }
    }

    public void setSortByCompteAnnexeAndVentile(boolean isSortByCompteAnnexeAndVentile) {
        this.isSortByCompteAnnexeAndVentile = isSortByCompteAnnexeAndVentile;
    }

    /**
     * @param b
     */
    public void setSortByEstVentileDesc(boolean b) {
        sortByEstVentileDesc = b;
    }

    /**
     * @param toIdExterneRole
     *            the toIdExterneRole to set
     */
    public void setToIdExterneRole(String toIdExterneRole) {
        this.toIdExterneRole = toIdExterneRole;
    }

    /**
     * Date de création : (22.02.2002 13:10:41)
     * 
     * @param newUntilDate
     *            String
     */
    public void setUntilDate(String newUntilDate) {
        untilDate = newUntilDate;
    }

    /**
     * Date de création : (14.03.2002 11:15:30)
     * 
     * @param newVueOperationCaCcSe
     *            Boolean
     */
    public void setVueOperationCaCcSe(Boolean newVueOperationCaCcSe) {
        vueOperationCaCcSe = newVueOperationCaCcSe;
    }

    /**
     * Date de création : (29.10.2002 07:47:53)
     * 
     * @param newVueOperationCaSe
     *            Boolean
     */
    public void setVueOperationCaSe(Boolean newVueOperationCaSe) {
        vueOperationCaSe = newVueOperationCaSe;
    }

    /**
     * Date de création : (27.02.2002 08:45:58)
     * 
     * @param newVueOperationCpteAnnexe
     *            Boolean
     */
    public void setVueOperationCpteAnnexe(String newVueOperationCpteAnnexe) {
        try {
            vueOperationCpteAnnexe = Boolean.valueOf(newVueOperationCpteAnnexe);
        } catch (Exception ex) {
            vueOperationCpteAnnexe = new Boolean(false);
        }
    }

    /**
     * Date de création : (27.02.2002 08:45:58)
     * 
     * @param newVueOperationCpteAnnexe
     *            Boolean
     */
    public void setVueOperationRubCC(String newVueOperationRubCC) {
        try {
            vueOperationRubCC = Boolean.valueOf(newVueOperationRubCC);
        } catch (Exception ex) {
            vueOperationRubCC = new Boolean(false);
        }
    }

}
