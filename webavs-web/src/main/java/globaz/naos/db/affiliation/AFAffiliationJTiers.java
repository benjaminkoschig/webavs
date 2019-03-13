package globaz.naos.db.affiliation;

import globaz.globall.db.BStatement;

public class AFAffiliationJTiers extends AFAffiliation {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String createFromClause(String collection) {
        StringBuilder fromClause = new StringBuilder();

        fromClause.append(collection);
        fromClause.append("AFAFFIP INNER JOIN ");
        fromClause.append(collection);
        fromClause.append("TITIERP ON ");
        fromClause.append(collection);
        fromClause.append("AFAFFIP.HTITIE=");
        fromClause.append(collection);
        fromClause.append("TITIERP.HTITIE");

        return fromClause.toString();
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String nom;
    private String designation1;
    private String designation2;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return AFAffiliationJTiersCI.createFromClause(_getCollection());
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        nom = statement.dbReadString("HTLDE1");
        designation1 = statement.dbReadString("HTLDE1");
        designation2 = statement.dbReadString("HTLDE2");
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDesignation1() {
        return designation1;
    }

    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }


}
