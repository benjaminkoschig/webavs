package globaz.osiris.db.comptes;

/**
 * Classe : type_conteneur Description : Date de création: 3 août 04
 * 
 * @author sch
 */
public class CASoldeSectionUntilDate extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String soldeSection = null;

    /**
     * Constructor for CASoldeSectionUntil.
     */
    public CASoldeSectionUntilDate() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return null;// unused
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        soldeSection = statement.dbReadNumeric("SOLDESSECTION");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // unused, voir description de la classe
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        // unused, voir description de la classe
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        // unused, voir description de la classe
    }

    /**
     * Returns the soldeSection.
     * 
     * @return String
     */
    public String getSoldeSection() {
        return soldeSection;
    }

    /**
     * Sets the soldeSection.
     * 
     * @param soldeSection
     *            The soldeSection to set
     */
    public void setSoldeSection(String soldeSection) {
        this.soldeSection = soldeSection;
    }
}
