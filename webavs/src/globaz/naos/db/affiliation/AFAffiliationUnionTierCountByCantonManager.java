/*
 * Created on Jul 13, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.naos.db.affiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;

/**
 * @author cuva Created on Jul 13, 2006
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class AFAffiliationUnionTierCountByCantonManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Date de début relation
     */
    private static final String FIELDNAME_TIERS_ADDR_DDEB = "HEDDAD";
    /**
     * Date de fin relation
     */
    private static final String FIELDNAME_TIERS_ADDR_DFIN = "HEDFAD";
    /**
     * Type d'adresse
     */
    private static final String FIELDNAME_TIERS_ADDR_TYPE = "HETTAD";
    /**
     * TIERS INACTIF ?
     */
    private static final String FIELDNAME_TIERS_INACTIF = "HTINAC";
    /**
     * Localite Pays
     */
    private static final String FIELDNAME_TIERS_LOC_PAYS = "HNIPAY";
    /**
     * Personne Décès
     */
    private static final String FIELDNAME_TIERS_PERS_DEC = "HPDDEC";
    /**
     * Personne date naissance
     */
    private static final String FIELDNAME_TIERS_PERS_NAISSANCE = "HPDNAI";
    /**
     * Personne Sex
     */
    private static final String FIELDNAME_TIERS_PERS_SEX = "HPTSEX";
    /**
	 * 
	 */
    private static final String TABLE_AVS = "TIPAVSP";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    /**
	 * 
	 */
    private static final String TABLE_TIERS = "TITIERP";
    /**
     * table tier adresse
     */
    private static final String TABLE_TIERS_ADREP = "TIADREP";
    /**
     * table tier avoir adresse
     */
    private static final String TABLE_TIERS_ADRP = "TIAADRP";
    /**
	 * 
	 */
    private static final String TABLE_TIERS_LOC = "TILOCAP";
    /**
	 * 
	 */
    private static final String TABLE_TIERS_PERS = "TIPERSP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forAdresseType = AFAffiliationUnionTierCountByCanton.CS_DOMICILE;

    private String forAssuranceId = "110";
    private String forEntreYear;
    private String forInnactif = "2";
    private transient String fromClause = null;

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sbFields = new StringBuffer(50);
        sbFields.append(_getCollection() + AFAffiliationUnionTierCountByCanton.TABLE_TIERS_LOC + ".");
        sbFields.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_CANTON);
        sbFields.append(", COUNT(*) AS " + AFAffiliationUnionTierCountByCanton.FIELDNAME_NB_AFFILIE);
        return sbFields.toString();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Surcharge
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = AFAffiliationUnionTierCountByCanton.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BManager#_getGroupBy(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getGroupBy(BStatement statement) {
        StringBuffer sqlGroupBy = new StringBuffer(30);
        sqlGroupBy.append(_getCollection() + AFAffiliationUnionTierCountByCanton.TABLE_TIERS_LOC + ".");
        sqlGroupBy.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_CANTON);

        return sqlGroupBy.toString();
    }

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getOrder(BStatement statement) {
        return AFAffiliationUnionTierCountByCanton.FIELDNAME_CANTON;
    }

    /**
     * Surcharge Renvoie la requête SQL à exécuter. <i>
     * <p>
     * Construit un requête SELECT * FROM ... WHERE ... ORDER BY ... </i>
     * 
     * @return la requête SQL
     */
    @Override
    protected java.lang.String _getSql(BStatement statement) {
        try {
            StringBuffer sqlBuffer = new StringBuffer("SELECT ");
            String sqlFields = _getFields(statement);
            if (!JadeStringUtil.isEmpty(sqlFields)) {
                sqlBuffer.append(sqlFields);
            } else {
                sqlBuffer.append("*");
            }
            sqlBuffer.append(" FROM ");
            sqlBuffer.append(_getFrom(statement));
            //
            String sqlWhere = _getWhere(statement);
            if (!JadeStringUtil.isEmpty(sqlWhere)) {
                sqlBuffer.append(" WHERE ");
                sqlBuffer.append(sqlWhere);
            }
            String sqlGroupeBy = _getGroupBy(statement);
            if (!JadeStringUtil.isEmpty(sqlGroupeBy)) {
                sqlBuffer.append(" GROUP BY ");
                sqlBuffer.append(sqlGroupeBy);
            }
            String sqlOrder = _getOrder(statement);
            if (!JadeStringUtil.isEmpty(sqlOrder)) {
                sqlBuffer.append(" ORDER BY ");
                sqlBuffer.append(sqlOrder);
            }
            return sqlBuffer.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSql() (" + e.toString() + ")");
            return "";
        }
    }

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sbWhere = new StringBuffer(50);
        if (!JadeStringUtil.isIntegerEmpty(getForEntreYear())) {
            int year = Integer.parseInt(getForEntreYear());

            sbWhere.append("((" + year + "1231 between " + _getCollection()
                    + AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADRP);
            sbWhere.append("." + AFAffiliationUnionTierCountByCantonManager.FIELDNAME_TIERS_ADDR_DDEB + " and "
                    + _getCollection() + AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADRP);
            sbWhere.append("." + AFAffiliationUnionTierCountByCantonManager.FIELDNAME_TIERS_ADDR_DFIN + ")");
            sbWhere.append(" or (" + _getCollection() + AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADRP + "."
                    + AFAffiliationUnionTierCountByCantonManager.FIELDNAME_TIERS_ADDR_DFIN + "=0");
            sbWhere.append(" and " + _getCollection() + AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADRP + "."
                    + AFAffiliationUnionTierCountByCantonManager.FIELDNAME_TIERS_ADDR_DFIN + "<=" + year + "9999))");
            sbWhere.append(" and " + _getCollection() + AFCotisation.TABLE_NAME + "." + AFCotisation.FIELDNAME_DATE_DEB
                    + "<" + (year + 1) + "0000");
            sbWhere.append(" and (" + _getCollection() + AFCotisation.TABLE_NAME + "."
                    + AFCotisation.FIELDNAME_DATE_FIN + "=0");
            sbWhere.append(" or " + _getCollection() + AFCotisation.TABLE_NAME + "." + AFCotisation.FIELDNAME_DATE_FIN
                    + ">=" + year + "1231)");
        }

        sbWhere.append(" and " + _getCollection() + AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADRP + "."
                + AFAffiliationUnionTierCountByCantonManager.FIELDNAME_TIERS_ADDR_TYPE + "=" + getForAdresseType());
        sbWhere.append(" and " + _getCollection() + AFAffiliationUnionTierCountByCanton.TABLE_TIERS + "."
                + AFAffiliationUnionTierCountByCantonManager.FIELDNAME_TIERS_INACTIF + "='" + getForInnactif() + "'");
        sbWhere.append(" and (" + _getCollection() + AFCotisation.TABLE_NAME + "."
                + AFCotisation.FIELDNAME_ASSURANCE_ID + "=4");
        sbWhere.append(" or  " + _getCollection() + AFCotisation.TABLE_NAME + "." + AFCotisation.FIELDNAME_ASSURANCE_ID
                + "=110)");
        sbWhere.append(" and " + _getCollection() + AFAffiliation.TABLE_NAME + "."
                + AFAffiliation.FIELDNAME_AFFILIATION_TYPE + " <> 804005");
        sbWhere.append(" and " + _getCollection() + AFAffiliation.TABLE_NAME + "."
                + AFAffiliation.FIELDNAME_AFFILIATION_TYPE + " <> " + CodeSystem.AVEC_BENIFICIARE);
        sbWhere.append(" and " + _getCollection() + AFCotisation.TABLE_NAME + "." + AFCotisation.FIELDNAME_MAISON_MERE
                + "<> 1 ");

        return sbWhere.toString();
    }

    /**
     * Surcharge.
     * 
     * @return
     * @throws java.lang.Exception
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     **/
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAffiliationUnionTierCountByCanton();
    }

    /**
     * @return
     */
    public String getForAdresseType() {
        return forAdresseType;
    }

    /**
     * @return
     */
    public String getForAssuranceId() {
        return forAssuranceId;
    }

    /**
     * Getter attribut entre année courrant.
     * 
     * @return attribut entre année courrant
     */
    public String getForEntreYear() {
        return forEntreYear;
    }

    /**
     * @return
     */
    public String getForInnactif() {
        return forInnactif;
    }

    /**
     * @param string
     */
    public void setForAdresseType(String string) {
        forAdresseType = string;
    }

    /**
     * @param string
     */
    public void setForAssuranceId(String string) {
        forAssuranceId = string;
    }

    /**
     * Setter attribut entre année.
     * 
     * @param string
     *            - attribut entre année.
     */
    public void setForEntreYear(String string) {
        forEntreYear = string;
    }

    /**
     * @param string
     */
    public void setForInnactif(String string) {
        forInnactif = string;
    }
}
