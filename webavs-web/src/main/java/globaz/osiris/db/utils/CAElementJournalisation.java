package globaz.osiris.db.utils;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 09:10:06)
 * 
 * @author: Administrator
 */
public class CAElementJournalisation extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String date = new String();
    private java.lang.String heure = new String();
    private java.lang.String idDomaineJournalisation = new String();
    private java.lang.String idElementJournalisation = new String();
    private java.lang.String idMotifJournalisation = new String();
    private java.lang.String idPosteJournalisation = new String();
    private globaz.globall.parameters.FWParametersSystemCode motifJournalisation = null;
    private java.lang.String texte = new String();
    private java.lang.String user = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CAPosteJournalisation
     */
    public CAElementJournalisation() {
        super();
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entité dans la BD
     * <p>
     * L'exécution de l'ajout n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente le prochain numéro
        setIdElementJournalisation(this._incCounter(transaction, idElementJournalisation));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CAEJOUP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idElementJournalisation = statement.dbReadNumeric("IDELEJOU");
        idPosteJournalisation = statement.dbReadNumeric("IDPOSJOU");
        date = statement.dbReadDateAMJ("DATE");
        heure = statement.dbReadNumeric("HEURE");
        texte = statement.dbReadString("TEXTE");
        idMotifJournalisation = statement.dbReadNumeric("IDMOTJOU");
        idDomaineJournalisation = statement.dbReadNumeric("IDDOMJOU");
        user = statement.dbReadString("USER");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdPosteJournalisation(), getSession().getLabel("7223"));
        _propertyMandatory(statement.getTransaction(), getIdElementJournalisation(), getSession().getLabel("7270"));
        _propertyMandatory(statement.getTransaction(), getDate(), getSession().getLabel("7271"));
        _propertyMandatory(statement.getTransaction(), getHeure(), getSession().getLabel("7272"));
        _propertyMandatory(statement.getTransaction(), getIdMotifJournalisation(), getSession().getLabel("7273"));
        _propertyMandatory(statement.getTransaction(), getIdDomaineJournalisation(), getSession().getLabel("7274"));
        _propertyMandatory(statement.getTransaction(), getUser(), getSession().getLabel("7275"));
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDELEJOU",
                this._dbWriteNumeric(statement.getTransaction(), getIdElementJournalisation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDELEJOU",
                this._dbWriteNumeric(statement.getTransaction(), getIdElementJournalisation(), "idEleJou"));
        statement.writeField("IDPOSJOU",
                this._dbWriteNumeric(statement.getTransaction(), getIdPosteJournalisation(), "idPosJou"));
        statement.writeField("DATE", this._dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField("HEURE", this._dbWriteNumeric(statement.getTransaction(), getHeure(), "heure"));
        statement.writeField("TEXTE", this._dbWriteString(statement.getTransaction(), getTexte(), "texte"));
        statement.writeField("IDMOTJOU",
                this._dbWriteNumeric(statement.getTransaction(), getIdMotifJournalisation(), "idMotJou"));
        statement.writeField("IDDOMJOU",
                this._dbWriteNumeric(statement.getTransaction(), getIdDomaineJournalisation(), "idDomJou"));
        statement.writeField("USER", this._dbWriteString(statement.getTransaction(), getUser(), "user"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:27:59)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDate() {
        return date;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:28:17)
     * 
     * @return java.lang.String
     */
    public java.lang.String getHeure() {
        return heure;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:31:44)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDomaineJournalisation() {
        return idDomaineJournalisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:20:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdElementJournalisation() {
        return idElementJournalisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:31:09)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdMotifJournalisation() {
        return idMotifJournalisation;
    }

    /**
     * Getter
     */
    public java.lang.String getIdPosteJournalisation() {
        return idPosteJournalisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.06.2002 11:22:45)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCode
     */
    public globaz.globall.parameters.FWParametersSystemCode getMotifJournalisation() {
        if (motifJournalisation == null) {
            // liste pas encore chargee, on la charge
            motifJournalisation = new globaz.globall.parameters.FWParametersSystemCode();
            motifJournalisation.setSession(getSession());
            motifJournalisation.getCode(getIdMotifJournalisation());
        }
        return motifJournalisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:29:00)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTexte() {
        return texte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:32:15)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUser() {
        return user;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:27:59)
     * 
     * @param newDate
     *            java.lang.String
     */
    public void setDate(java.lang.String newDate) {
        date = newDate;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:28:17)
     * 
     * @param newHeure
     *            java.lang.String
     */
    public void setHeure(java.lang.String newHeure) {
        heure = newHeure;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:31:44)
     * 
     * @param newIdDomaineJournalisation
     *            java.lang.String
     */
    public void setIdDomaineJournalisation(java.lang.String newIdDomaineJournalisation) {
        idDomaineJournalisation = newIdDomaineJournalisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:20:07)
     * 
     * @param newIdElementJournalisation
     *            java.lang.String
     */
    public void setIdElementJournalisation(java.lang.String newIdElementJournalisation) {
        idElementJournalisation = newIdElementJournalisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:31:09)
     * 
     * @param newIdMotifJournalisation
     *            java.lang.String
     */
    public void setIdMotifJournalisation(java.lang.String newIdMotifJournalisation) {
        idMotifJournalisation = newIdMotifJournalisation;
    }

    /**
     * Setter
     */
    public void setIdPosteJournalisation(java.lang.String newIdPosteJournalisation) {
        idPosteJournalisation = newIdPosteJournalisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.06.2002 11:22:45)
     * 
     * @param newMotifJournalisation
     *            globaz.globall.parameters.FWParametersSystemCode
     */
    public void setMotifJournalisation(globaz.globall.parameters.FWParametersSystemCode newMotifJournalisation) {
        motifJournalisation = newMotifJournalisation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:29:00)
     * 
     * @param newTexte
     *            java.lang.String
     */
    public void setTexte(java.lang.String newTexte) {
        texte = newTexte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.06.2002 14:32:15)
     * 
     * @param newUser
     *            java.lang.String
     */
    public void setUser(java.lang.String newUser) {
        user = newUser;
    }
}
