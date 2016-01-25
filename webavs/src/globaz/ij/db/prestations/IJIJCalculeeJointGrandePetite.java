/*
 * Créé le 22 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJIJCalculeeJointGrandePetite extends IJIJCalculee {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJIJCalculee.TABLE_NAME_IJ_CALCULEE);

        // jointure entre table des petitesIJcalculees et table des IJCalculees
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPetiteIJCalculee.TABLE_NAME_PETITE_IJ_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJIJCalculee.TABLE_NAME_IJ_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPetiteIJCalculee.TABLE_NAME_PETITE_IJ_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPetiteIJCalculee.FIELDNAME_ID_IJ_CALCULEE_PETITE);

        // jointure entre table des grandesIJcalculees et table des IJCalculees
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJGrandeIJCalculee.TABLE_NAME_GRANDE_IJ_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJIJCalculee.TABLE_NAME_IJ_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJGrandeIJCalculee.TABLE_NAME_GRANDE_IJ_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJGrandeIJCalculee.FIELDNAME_ID_GRANDE_IJ_CALCULEE);

        return fromClauseBuffer.toString();
    }

    private String csModeCalcul = "";
    private String fromClause = null;
    private String montantIndemniteAssistance = "";

    private String montantIndemniteEnfant = "";

    private String montantIndemniteExploitation = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String nbEnfants = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        montantIndemniteEnfant = statement.dbReadNumeric(IJGrandeIJCalculee.FIELDNAME_MONTANT_INDEMNITE_ENFANTS, 2);
        nbEnfants = statement.dbReadNumeric(IJGrandeIJCalculee.FIELDNAME_NB_ENFANTS);
        montantIndemniteAssistance = statement.dbReadNumeric(IJGrandeIJCalculee.FIELDNAME_MONTANT_INDEMNITE_ASSISTANCE,
                2);
        montantIndemniteExploitation = statement.dbReadNumeric(
                IJGrandeIJCalculee.FIELDNAME_MONTANT_INDEMNITE_EXPLOITATION, 2);
        csModeCalcul = statement.dbReadNumeric(IJPetiteIJCalculee.FIELDNAME_CS_MODE_CALCUL);
    }

    /**
     * getter pour l'attribut cs mode calcul
     * 
     * @return la valeur courante de l'attribut cs mode calcul
     */
    public String getCsModeCalcul() {
        return csModeCalcul;
    }

    /**
     * getter pour l'attribut montant indemnite assistance
     * 
     * @return la valeur courante de l'attribut montant indemnite assistance
     */
    public String getMontantIndemniteAssistance() {
        return montantIndemniteAssistance;
    }

    /**
     * getter pour l'attribut montant indemnite enfant
     * 
     * @return la valeur courante de l'attribut montant indemnite enfant
     */
    public String getMontantIndemniteEnfant() {
        return montantIndemniteEnfant;
    }

    /**
     * getter pour l'attribut montant indemnite exploitation
     * 
     * @return la valeur courante de l'attribut montant indemnite exploitation
     */
    public String getMontantIndemniteExploitation() {
        return montantIndemniteExploitation;
    }

    /**
     * getter pour l'attribut nb enfants
     * 
     * @return la valeur courante de l'attribut nb enfants
     */
    public String getNbEnfants() {
        return nbEnfants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasSpy()
     */
    @Override
    public boolean hasSpy() {
        return super.hasSpy();
    }

    /**
     * setter pour l'attribut cs mode calcul
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsModeCalcul(String string) {
        csModeCalcul = string;
    }

    /**
     * setter pour l'attribut montant indemnite assistance
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantIndemniteAssistance(String string) {
        montantIndemniteAssistance = string;
    }

    /**
     * setter pour l'attribut montant indemnite enfant
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantIndemniteEnfant(String string) {
        montantIndemniteEnfant = string;
    }

    /**
     * setter pour l'attribut montant indemnite exploitation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantIndemniteExploitation(String string) {
        montantIndemniteExploitation = string;
    }

    /**
     * setter pour l'attribut nb enfants
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbEnfants(String string) {
        nbEnfants = string;
    }

}
