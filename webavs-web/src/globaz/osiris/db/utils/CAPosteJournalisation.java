package globaz.osiris.db.utils;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (12.12.2001 09:10:06)
 * 
 * @author: Administrator
 */
public class CAPosteJournalisation extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idPosteJournalisation = new String();
    private java.lang.String reference = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CAPosteJournalisation
     */
    public CAPosteJournalisation() {
        super();
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entit� dans la BD
     * <p>
     * L'ex�cution de l'ajout n'est pas effectu�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incr�mente le prochain num�ro
        setIdPosteJournalisation(this._incCounter(transaction, idPosteJournalisation));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 17:06:20)
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws Exception {

        // Laisser la superclasse effectuer son traitement
        super._beforeDelete(transaction);

        // Supprimer l'�l�ment de journalisation
        CAElementJournalisationManager eleJourMan = new CAElementJournalisationManager();
        eleJourMan.setSession(getSession());
        eleJourMan.setForIdPosteJournalisation(getIdPosteJournalisation());
        eleJourMan.find(transaction);

        if (!transaction.hasErrors()) {
            // Parcourir et supprimer les �l�ments de journalisation
            for (int i = 0; i < eleJourMan.size(); i++) {
                CAElementJournalisation elem = (CAElementJournalisation) eleJourMan.getEntity(i);
                elem.delete(transaction);
                if (transaction.hasErrors()) {
                    _addError(transaction, getSession().getLabel("7280"));
                    break;
                }
            }
        } else {
            _addError(transaction, getSession().getLabel("7281"));
        }

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CAPJOUP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idPosteJournalisation = statement.dbReadNumeric("IDPOSJOU");
        reference = statement.dbReadString("REFERENCE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdPosteJournalisation(), getSession().getLabel("7223"));
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement
                .writeKey("IDPOSJOU", this._dbWriteNumeric(statement.getTransaction(), getIdPosteJournalisation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDPOSJOU",
                this._dbWriteNumeric(statement.getTransaction(), getIdPosteJournalisation(), "idPosJou"));
        statement.writeField("REFERENCE", this._dbWriteString(statement.getTransaction(), getReference(), "reference"));
    }

    /**
     * Getter
     */
    public java.lang.String getIdPosteJournalisation() {
        return idPosteJournalisation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.06.2002 15:40:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReference() {
        return reference;
    }

    /**
     * Setter
     */
    public void setIdPosteJournalisation(java.lang.String newIdPosteJournalisation) {
        idPosteJournalisation = newIdPosteJournalisation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.06.2002 15:40:43)
     * 
     * @param newReference
     *            java.lang.String
     */
    public void setReference(java.lang.String newReference) {
        reference = newReference;
    }
}
