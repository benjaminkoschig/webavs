package globaz.naos.db.affiliation;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.osiris.db.comptes.CASection;

/**
 * <H1>Description</H1>
 * 
 * Un value BEntity servant à relier les affilies et leurs sections en compta.
 * 
 * Il est interdit d'ajouter, d'effacer ou de modifier cet entity.
 * 
 * @author vre
 */
public class AFAffiliationJSection extends AFAffiliation {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 7077598677540821360L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String DateDebutParticularite;

    private String DateFinParticularite;
    private String idSection;

    private CASection section = new CASection();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);
        section.setIdSection(idSection);
        section.setSession(getSession());
        section.retrieve(transaction);
    }

    /**
     * @return false
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @return false
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @return false
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idSection = statement.dbReadNumeric(CASection.FIELD_IDSECTION);
        DateDebutParticularite = statement.dbReadDateAMJ("MFDDEB");
        DateFinParticularite = statement.dbReadDateAMJ("MFDFIN");
    }

    /**
     * @return
     */
    public String getDateDebutParticularite() {
        return DateDebutParticularite;
    }

    /**
     * @return
     */
    public String getDateFinParticularite() {
        return DateFinParticularite;
    }

    /**
     * getter pour l'attribut id section.
     * 
     * @return la valeur courante de l'attribut id section
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * getter pour l'attribut section.
     * 
     * @return la valeur courante de l'attribut section
     */
    public CASection getSection() {
        return section;
    }

    /**
     * @param string
     */
    public void setDateDebutParticularite(String string) {
        DateDebutParticularite = string;
    }

    /**
     * @param string
     */
    public void setDateFinParticularite(String string) {
        DateFinParticularite = string;
    }

    /**
     * setter pour l'attribut id section.
     * 
     * @param idSection
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

}
