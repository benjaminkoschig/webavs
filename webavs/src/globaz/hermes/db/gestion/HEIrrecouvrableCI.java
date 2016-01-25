package globaz.hermes.db.gestion;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.pavo.util.CIUtil;

public class HEIrrecouvrableCI extends HEOutputAnnonceViewBean {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String TABLE_AVS = "HEINCOP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append("HEANNOP AS HEANNOP");

        // jointure entre table des demandes et table des tiers
        fromClauseBuffer.append(" JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append("CIINDIP");
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append("CIINDIP");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append("KANAVS");
        fromClauseBuffer.append(egal);
        // fromClauseBuffer.append(schema);
        fromClauseBuffer.append("HEANNOP");
        fromClauseBuffer.append(point);
        fromClauseBuffer.append("RNAVS");

        return fromClauseBuffer.toString();
    }

    private String dateNaissance = "";
    private String nom = "";
    // Autres champs nécessaires
    private String numAvs = "";
    private String pays = "";

    private String sexe = "";

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception, HEOutputAnnonceException {

    }

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        numAvs = statement.dbReadString("KANAVS");
        nom = statement.dbReadString("KALNOM");
        dateNaissance = CIUtil.formatDate(statement.dbReadNumeric("KADNAI"));
        sexe = statement.dbReadNumeric("KATSEX");
        pays = statement.dbReadString("KAIPAY");
    }

    public String getDateNaissance() {

        return dateNaissance;
    }

    public String getNom() {
        return nom;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getPays() {
        return pays;
    }

    public String getSexe() {
        return sexe;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
}
