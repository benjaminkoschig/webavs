package globaz.naos.db.taxeCo2;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

public class AFFigerTaxeCo2Manager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String TABLE_FIELDS = "AF." + AFFigerTaxeCo2.FIELDNAME_NUM_AFFILIE + ", AF."
            + AFFigerTaxeCo2.FIELDNAME_AFFILIATION_ID + ", COM." + AFFigerTaxeCo2.FIELDNAME_ANNEE + ", COM."
            + AFFigerTaxeCo2.FIELDNAME_MASSE + ", AF." + AFFigerTaxeCo2.FIELDNAME_MOTIF_FIN + ", COM."
            + AFFigerTaxeCo2.FIELDNAME_ID_RUBRIQUE;
    private String forAffiliationId = new String();
    private String forAnneeMasse = new String();
    private String forMasse = new String();
    private String forMotifFin = new String();
    private String forTaxeCo2Id = new String();
    private String fromAffiliationId = new String();

    private String order = new String();

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return AFFigerTaxeCo2Manager.TABLE_FIELDS;
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFAFFIP AS AF INNER JOIN " + _getCollection()
                + "CACPTAP AS CA ON (AF.MALNAF=CA.IDEXTERNEROLE AND AF.HTITIE=CA.IDTIERS " + "AND CA.IDROLE IN("
                + CodeSystem.ROLE_AFFILIE + ", " + CodeSystem.ROLE_AFFILIE_PARITAIRE + ")) INNER JOIN "
                + _getCollection() + "CACPTRP AS COM ON (CA.IDCOMPTEANNEXE=COM.IDCOMPTEANNEXE) LEFT JOIN "
                + _getCollection() + "AFPARTP AS PAR ON (AF.MAIAFF=PAR.MAIAFF AND PAR.MFTPAR="
                + CodeSystem.PARTIC_AFFILIE_EXEMPTION_CO2 + " AND (MFDFIN=0 OR MFDFIN>" + getForAnneeMasse()
                + "0101) and mfddeb<=" + getForAnneeMasse() + "1231)";
    }

    // select af.malnaf, af.maiaff, com.annee, com.cumulmasse, com.idrubrique,
    // af.matmot from webavsciam.afaffip af
    // inner join webavsciam.cacptap ca on(af.malnaf=ca.idexternerole and
    // af.htitie=ca.idtiers and ca.IDROLE in(517002, 517039))
    // inner join webavsciam.cacptrp com on
    // (ca.idcompteannexe=com.idcompteannexe)
    // left join webavsciam.afpartp par on (af.maiaff = par.maiaff and
    // par.mftpar=818021 and (mfdfin=0 or mfdfin>20050101) and mfddeb<=20051231)
    // where com.annee=2005 and com.cumulmasse>0 and com.idrubrique in (select
    // mbirub from webavsciam.AFASSUP where mbtgen=801001 and mbttyp=812001)
    // and (af.madfin=0 or af.madfin>20050101)
    // and af.maddeb<=20051231
    // and af.mattaf in(804002, 804005, 804010, 804012)
    // and par.mfipar is null
    // order by af.malnaf

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (JadeStringUtil.isEmpty(getOrder())) {
            return "AF.MAIAFF";
        } else {
            return getOrder();
        }
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut

        // where com.annee=2005 and com.cumulmasse>0 and com.idrubrique in
        // (select mbirub from webavsciam.AFASSUP where mbtgen=801001 and
        // mbttyp=812001)
        // and (af.madfin=0 or af.madfin>20050101)
        // and af.maddeb<=20051231
        // and af.mattaf in(804002, 804005, 804010, 804012)
        // and par.mfipar is null

        String sqlWhere = "";

        if (getForAnneeMasse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "COM.ANNEE=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeMasse());
        }

        if (getFromAffiliationId().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "AF.MAIAFF >" + this._dbWriteNumeric(statement.getTransaction(), getFromAffiliationId());
        }

        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += "COM.CUMULMASSE>0 AND COM.IDRUBRIQUE IN (SELECT MBIRUB FROM " + _getCollection()
                + "AFASSUP WHERE MBTGEN=" + CodeSystem.GENRE_ASS_PARITAIRE + " AND MBTTYP="
                + CodeSystem.TYPE_ASS_COTISATION_AVS_AI + ")" + " AND (AF.MADFIN=0 OR AF.MADFIN>"
                + this._dbWriteNumeric(statement.getTransaction(), getForAnneeMasse()) + "0101) AND AF.MADDEB<="
                + this._dbWriteNumeric(statement.getTransaction(), getForAnneeMasse()) + "1231 "
                + " AND AF.MATTAF IN (" + CodeSystem.TYPE_AFFILI_EMPLOY + ", " + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY
                + ", " + CodeSystem.TYPE_AFFILI_LTN + ", " + CodeSystem.TYPE_AFFILI_EMPLOY_D_F
                + ") AND PAR.MFIPAR IS NULL";

        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFFigerTaxeCo2();
    }

    // ***********************************************
    // Getter
    // ***********************************************

    public String getForAffiliationId() {
        return forAffiliationId;
    }

    public String getForAnneeMasse() {
        return forAnneeMasse;
    }

    public String getForMasse() {
        return forMasse;
    }

    public String getForMotifFin() {
        return forMotifFin;
    }

    public String getForTaxeCo2Id() {
        return forTaxeCo2Id;
    }

    public String getFromAffiliationId() {
        return fromAffiliationId;
    }

    public String getOrder() {
        return order;
    }

    public void setForAffiliationId(java.lang.String forAffiliationId) {
        this.forAffiliationId = forAffiliationId;
    }

    public void setForAnneeMasse(String forAnneeMasse) {
        this.forAnneeMasse = forAnneeMasse;
    }

    public void setForMasse(String forMasse) {
        this.forMasse = forMasse;
    }

    public void setForMotifFin(String forMotifFin) {
        this.forMotifFin = forMotifFin;
    }

    public void setForTaxeCo2Id(String forTaxeCo2Id) {
        this.forTaxeCo2Id = forTaxeCo2Id;
    }

    public void setFromAffiliationId(String fromAffiliationId) {
        this.fromAffiliationId = fromAffiliationId;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
