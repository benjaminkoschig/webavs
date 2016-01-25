package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.hercule.db.groupement.CEGroupe;
import globaz.hercule.db.groupement.CEMembre;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;

/**
 * @author MMO
 * @since 3 aout. 2010
 */
public class CEEmployeurRadieManager extends BManager {

    private static final long serialVersionUID = -1356362562033234966L;
    public static final String AS = " AS ";
    public static final String F_AFF_DDEB = AFAffiliation.FIELDNAME_AFF_DDEBUT;
    public static final String F_AFF_DFIN = AFAffiliation.FIELDNAME_AFF_DFIN;
    public static final String F_AFF_MOTIF = "MATMOT";
    public static final String F_AFF_NUMAFFILIE = "MALNAF";
    public static final String F_AFF_TYPE = AFAffiliation.FIELDNAME_AFFILIATION_TYPE;
    public static final String F_CACPTA_NUMAFFILIE = "IDEXTERNEROLE";
    public static final String F_CACPTR_CUMULMASSE = "CUMULMASSE";

    public static final String F_CACPTR_IDRUBRIQUE = "IDRUBRIQUE";
    public static final String F_IDAFFILIATION = AFAffiliation.FIELDNAME_AFFILIATION_ID;
    public static final String F_IDCOMPTEANNEXE = "IDCOMPTEANNEXE";
    public static final String F_IDGROUPE = CEGroupe.FIELD_IDGROUPE;

    public static final String F_IDTIER = "HTITIE";
    public static final String F_NOMGROUPE = CEGroupe.FIELD_LIBELLE;
    public static final String F_TIER_COMPLEMENTNOM = "HTLDE2";

    public static final String F_TIER_NOM = "HTLDE1";
    public static final String FROM = " FROM ";
    public static final String GROUP_BY = " GROUP BY ";
    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String LEFT_JOIN = " LEFT JOIN ";
    public static final String ON = " ON ";

    public static final String SELECT = " SELECT ";

    public static final String T_AFF = AFAffiliation.TABLE_NAME;
    public static final String T_CACPTA = "CACPTAP";
    public static final String T_CACPTR = "CACPTRP";
    public static final String T_GROUPE = CEGroupe.TABLE_CEGRPP;
    public static final String T_MASSESALARIALE = "MASSESALARIALE";
    public static final String T_MEMBRE = CEMembre.TABLE_CEMEMP;
    public static final String T_TIER = "TITIERP";
    public static final String WHERE = " WHERE ";

    private String forMotifRadiation = "";

    private String fromDateRadiation = "";
    private String fromMasseSalariale = "";
    private String toDateRadiation = "";
    private String toMasseSalariale = "";

    @Override
    protected String _getFields(final BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        CEUtils.sqlAddField(sqlFields, _getCollection() + CEEmployeurRadieManager.T_AFF + "."
                + CEEmployeurRadieManager.F_IDAFFILIATION);
        CEUtils.sqlAddField(sqlFields, CEEmployeurRadieManager.F_AFF_NUMAFFILIE);
        CEUtils.sqlAddField(sqlFields, CEEmployeurRadieManager.F_AFF_MOTIF);
        CEUtils.sqlAddField(sqlFields, _getCollection() + CEEmployeurRadieManager.T_TIER + "."
                + CEEmployeurRadieManager.F_IDTIER);
        CEUtils.sqlAddField(sqlFields, CEEmployeurRadieManager.F_TIER_NOM);
        CEUtils.sqlAddField(sqlFields, CEEmployeurRadieManager.F_TIER_COMPLEMENTNOM);
        CEUtils.sqlAddField(sqlFields, CEEmployeurRadieManager.F_NOMGROUPE);
        CEUtils.sqlAddField(sqlFields, CEEmployeurRadieManager.F_AFF_DDEB);
        CEUtils.sqlAddField(sqlFields, CEEmployeurRadieManager.F_AFF_DFIN);
        CEUtils.sqlAddField(sqlFields, CEEmployeurRadieManager.T_MASSESALARIALE + "."
                + CEEmployeurRadieManager.F_CACPTR_CUMULMASSE);
        CEUtils.sqlAddField(sqlFields, "COUP.PCOUID AS CODESUVA");
        CEUtils.sqlAddField(sqlFields, "COUP.PCOLUT AS LIBELLESUVA");

        return sqlFields.toString();

    }

    @Override
    protected String _getFrom(final BStatement statement) {

        StringBuffer sqlFrom = new StringBuffer();
        sqlFrom.append(_getCollection() + CEEmployeurRadieManager.T_AFF);
        sqlFrom.append(CEEmployeurRadieManager.INNER_JOIN);
        sqlFrom.append(_getCollection() + CEEmployeurRadieManager.T_TIER);
        sqlFrom.append(CEEmployeurRadieManager.ON);
        sqlFrom.append(_getCollection() + CEEmployeurRadieManager.T_AFF + "." + CEEmployeurRadieManager.F_IDTIER + "="
                + _getCollection() + CEEmployeurRadieManager.T_TIER + "." + CEEmployeurRadieManager.F_IDTIER);
        sqlFrom.append(CEEmployeurRadieManager.LEFT_JOIN);
        sqlFrom.append(_getCollection() + CEEmployeurRadieManager.T_CACPTA);
        sqlFrom.append(CEEmployeurRadieManager.ON);
        sqlFrom.append("(" + _getCollection() + CEEmployeurRadieManager.T_CACPTA + "."
                + CEEmployeurRadieManager.F_CACPTA_NUMAFFILIE + "=" + _getCollection() + CEEmployeurRadieManager.T_AFF
                + "." + CEEmployeurRadieManager.F_AFF_NUMAFFILIE + " AND " + _getCollection()
                + CEEmployeurRadieManager.T_CACPTA + ".IDROLE = "
                + CEAffiliationService.getRoleForAffilieParitaire(getSession()) + ")");

        try {
            sqlFrom.append(CEEmployeurRadieManager.LEFT_JOIN);
            sqlFrom.append(giveTableMasseSalariale(new JADate(getFromDateRadiation()).getYear() - 1)
                    + CEEmployeurRadieManager.AS + CEEmployeurRadieManager.T_MASSESALARIALE);
            sqlFrom.append(CEEmployeurRadieManager.ON);
            sqlFrom.append(CEEmployeurRadieManager.T_MASSESALARIALE + "." + CEEmployeurRadieManager.F_IDCOMPTEANNEXE
                    + "=" + _getCollection() + CEEmployeurRadieManager.T_CACPTA + "."
                    + CEEmployeurRadieManager.F_IDCOMPTEANNEXE);
        } catch (JAException e) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_RADIE_ERREUR_EXTRACTION_ANNEE_RADIATION"));
        }

        sqlFrom.append(CEEmployeurRadieManager.LEFT_JOIN);
        sqlFrom.append(_getCollection() + CEEmployeurRadieManager.T_MEMBRE);
        sqlFrom.append(CEEmployeurRadieManager.ON);
        sqlFrom.append(_getCollection() + CEEmployeurRadieManager.T_MEMBRE + "."
                + CEEmployeurRadieManager.F_IDAFFILIATION + "=" + _getCollection() + CEEmployeurRadieManager.T_AFF
                + "." + CEEmployeurRadieManager.F_IDAFFILIATION);
        sqlFrom.append(CEEmployeurRadieManager.LEFT_JOIN);
        sqlFrom.append(_getCollection() + CEEmployeurRadieManager.T_GROUPE);
        sqlFrom.append(CEEmployeurRadieManager.ON);
        sqlFrom.append(_getCollection() + CEEmployeurRadieManager.T_GROUPE + "." + CEEmployeurRadieManager.F_IDGROUPE
                + "=" + _getCollection() + CEEmployeurRadieManager.T_MEMBRE + "." + CEEmployeurRadieManager.F_IDGROUPE);
        sqlFrom.append(" LEFT JOIN " + _getCollection() + "FWCOUP as coup on (" + _getCollection()
                + "AFAFFIP.MATSUV = coup.pcosid AND coup.PLAIDE='" + getSession().getIdLangue() + "') ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(final BStatement statement) {
        return CEEmployeurRadieManager.F_AFF_NUMAFFILIE;
    }

    @Override
    protected String _getWhere(final BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        // affiliés employeurs
        CEUtils.sqlAddCondition(sqlWhere, CEEmployeurRadieManager.F_AFF_TYPE + " IN (" + CodeSystem.TYPE_AFFILI_EMPLOY
                + ", " + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + ", " + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + ")");

        /**
         * affiliations radiées le contenu de getFromDateRadiation() et de getToDateRadiation() est validé en amont
         * premièrement dans employeurRadie_de.jsp deuxièmement dans protected void _validate() du process
         * CEEmployeurRadieProcess.java
         */
        CEUtils.sqlAddCondition(sqlWhere, CEEmployeurRadieManager.F_AFF_DFIN + " <> 0");
        CEUtils.sqlAddCondition(
                sqlWhere,
                CEEmployeurRadieManager.F_AFF_DFIN + " >= "
                        + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateRadiation()));
        CEUtils.sqlAddCondition(
                sqlWhere,
                CEEmployeurRadieManager.F_AFF_DFIN + " <= "
                        + this._dbWriteDateAMJ(statement.getTransaction(), getToDateRadiation()));

        if (!JadeStringUtil.isBlank(getFromMasseSalariale())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    CEEmployeurRadieManager.T_MASSESALARIALE + "." + CEEmployeurRadieManager.F_CACPTR_CUMULMASSE + ">="
                            + this._dbWriteNumeric(statement.getTransaction(), getFromMasseSalariale()));
        }

        if (!JadeStringUtil.isBlank(getToMasseSalariale())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    CEEmployeurRadieManager.T_MASSESALARIALE + "." + CEEmployeurRadieManager.F_CACPTR_CUMULMASSE + "<="
                            + this._dbWriteNumeric(statement.getTransaction(), getToMasseSalariale()));
        }

        if (!JadeStringUtil.isBlank(getForMotifRadiation())) {
            CEUtils.sqlAddCondition(sqlWhere, CEEmployeurRadieManager.F_AFF_MOTIF + "=" + getForMotifRadiation());
        }

        CEUtils.sqlAddCondition(sqlWhere, CEEmployeurRadieManager.F_AFF_DDEB + " <> "
                + CEEmployeurRadieManager.F_AFF_DFIN);

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEEmployeurRadie();
    }

    public String getForMotifRadiation() {
        return forMotifRadiation;
    }

    public String getFromDateRadiation() {
        return fromDateRadiation;
    }

    public String getFromMasseSalariale() {
        return fromMasseSalariale;
    }

    public String getToDateRadiation() {
        return toDateRadiation;
    }

    public String getToMasseSalariale() {
        return toMasseSalariale;
    }

    private String giveTableMasseSalariale(final int annee) {

        String idRubrique = CEUtils.getIdRubrique(getSession());

        return "(SELECT SUM(" + CEEmployeurRadieManager.F_CACPTR_CUMULMASSE + ") " + CEEmployeurRadieManager.AS
                + CEEmployeurRadieManager.F_CACPTR_CUMULMASSE + ", " + CEEmployeurRadieManager.F_IDCOMPTEANNEXE
                + CEEmployeurRadieManager.FROM + _getCollection() + CEEmployeurRadieManager.T_CACPTR + " "
                + CEEmployeurRadieManager.WHERE + "ANNEE = " + String.valueOf(annee) + " AND "
                + CEEmployeurRadieManager.F_CACPTR_IDRUBRIQUE + " IN( " + idRubrique + ")"
                + CEEmployeurRadieManager.GROUP_BY + CEEmployeurRadieManager.F_IDCOMPTEANNEXE + ")";
    }

    public void setForMotifRadiation(final String forMotifRadiation) {
        this.forMotifRadiation = forMotifRadiation;
    }

    public void setFromDateRadiation(final String newFromDateRadiation) {
        fromDateRadiation = newFromDateRadiation;
    }

    public void setFromMasseSalariale(final String newFromMasseSalariale) {
        fromMasseSalariale = newFromMasseSalariale;
    }

    public void setToDateRadiation(final String newToDateRadiation) {
        toDateRadiation = newToDateRadiation;
    }

    public void setToMasseSalariale(final String newToMasseSalariale) {
        toMasseSalariale = newToMasseSalariale;
    }

}
