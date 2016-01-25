package globaz.naos.db.affiliation;

import globaz.globall.db.BStatement;
import globaz.naos.db.cotisation.AFCotisation;

/**
 * @author cuva Created on Jul 13, 2006
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class AFAffiliationUnionTierCountByCanton extends AFAffiliation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected final static String CS_COURRIER = "508001";
    protected final static String CS_DOMICILE = "508008";
    protected final static String CS_SEXE_FEMME = "516002";
    protected final static String CS_SEXE_HOMME = "516001";
    /**
	 * 
	 */
    public static final String FIELDNAME_CANTON = "HJICAN";
    /**
     * ID Adresse Interne Unique
     */
    public static final String FIELDNAME_ID_ADDR_ID = "HAIADR";
    /**
     * ID Adresse Interne
     */
    public static final String FIELDNAME_ID_ADDR_INT = "HEIADR";
    /**
     * ID Adresse Interne Unique
     */
    public static final String FIELDNAME_ID_ADDR_INTU = "HEIAAU";
    /**
     * ID Localite
     */
    public static final String FIELDNAME_ID_LOC_ID = "HJILOC";
    /**
	 * 
	 */
    public static final String FIELDNAME_ID_TIERS_ID = "HTITIE";
    /**
	 * 
	 */
    public static final String FIELDNAME_NB_AFFILIE = "NBAFF";
    /**
	 * 
	 */
    public static final String FIELDNAME_PLAN_ID = "MUIPLA";
    public static final String FIELDNAME_PLAN_INACTIF = "MUBINA";
    /**
	 * 
	 */
    public static final String TABLE_ = "TILOCAP";
    /**
	 * 
	 */
    public static final String TABLE_AVS = "TIPAVSP";
    /**
	 * 
	 */
    public static final String TABLE_PLAN = "AFPLAFP";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    /**
	 * 
	 */
    public static final String TABLE_TIERS = "TITIERP";

    /**
     * table tier adresse
     */
    public static final String TABLE_TIERS_ADREP = "TIADREP";
    /**
     * table tier avoir adresse
     */
    public static final String TABLE_TIERS_ADRP = "TIAADRP";
    /**
	 * 
	 */
    public static final String TABLE_TIERS_LOC = "TILOCAP";
    /**
	 * 
	 */
    public static final String TABLE_TIERS_PERS = "TIPERSP";

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String and = " AND ";
        String point = ".";
        String egal = "=";
        String bFalse = "2";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS);

        // Jointure entre table tier et personne
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_PERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_PERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_TIERS_ID);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_TIERS_ID);

        // Jointure entre table tier et avs
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_TIERS_ID);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_TIERS_ID);

        // Jointure gauche entre table tier avoir adresse et tier
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADRP);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADRP);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_TIERS_ID);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_TIERS_ID);
        fromClauseBuffer.append(and);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADRP);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_ADDR_INT);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADRP);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_ADDR_INTU);

        // Jointure gauche entre table tier adresse et tier avoir adresse
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADREP);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADREP);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_ADDR_ID);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADRP);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_ADDR_ID);

        // Jointure gauche entre table tier adresse et tier localite
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_LOC);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_ADREP);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_LOC_ID);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_LOC);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_LOC_ID);

        // Jointure entre table affcoti et tier
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliation.FIELDNAME_TIER_ID);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_PERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_ID_TIERS_ID);

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_PLAN);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append("(");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_PLAN);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_PLAN_ID);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliation.FIELDNAME_AFFILIATION_ID);
        fromClauseBuffer.append(and);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_PLAN);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_PLAN_INACTIF);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(bFalse);
        fromClauseBuffer.append(")");

        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFCotisation.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFCotisation.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFCotisation.FIELDNAME_PLANAFFILIATION_ID);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.TABLE_PLAN);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_PLAN_ID);

        return fromClauseBuffer.toString();
    }

    private String canton = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String nbAffilie = "";

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sbFields = new StringBuffer(50);
        sbFields.append(AFAffiliationUnionTierCountByCanton.TABLE_TIERS_LOC + ".");
        sbFields.append(AFAffiliationUnionTierCountByCanton.FIELDNAME_CANTON);
        sbFields.append(", COUNT(*) AS " + AFAffiliationUnionTierCountByCanton.FIELDNAME_NB_AFFILIE);
        return sbFields.toString();
    }

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     **/
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
     * @throws Exception
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     **/
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        canton = statement.dbReadString(AFAffiliationUnionTierCountByCanton.FIELDNAME_CANTON);
        nbAffilie = statement.dbReadNumeric(AFAffiliationUnionTierCountByCanton.FIELDNAME_NB_AFFILIE);

        try {

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * Getter attribut Canton courrant
     * 
     * @return canton courrant
     */
    public String getCanton() {
        return canton;
    }

    /**
     * Getter attribut Nb d'Affilie pour un canton courrant
     * 
     * @return Nb d'Affilie pour un canton courrant
     */
    public String getNbAffilie() {
        return nbAffilie;
    }

    /**
     * Setter attribut Canton courrant
     * 
     * @param string
     *            - canton
     */
    public void setCanton(String string) {
        canton = string;
    }

    /**
     * Setter attribut Nb d'Affilie pour un canton courrant
     * 
     * @param string
     *            Nb d'Affilie
     */
    public void setNbAffilie(String string) {
        nbAffilie = string;
    }

}
