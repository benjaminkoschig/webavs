package globaz.osiris.db.contentieux;

import globaz.globall.db.BTransaction;
import globaz.norma.db.fondation.IntTranslatable;
import globaz.norma.db.fondation.PATraductionHelper;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 09:04:36)
 * 
 * @author: Administrator
 */
public class CASequenceContentieux extends globaz.globall.db.BEntity implements java.io.Serializable, IntTranslatable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idSequenceContentieux = new String();
    private java.lang.String idTraduction = new String();
    private PATraductionHelper trLibelles = null;

    // code systeme

    /**
     * Commentaire relatif au constructeur CASequenceContentieux
     */
    public CASequenceContentieux() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Mise à jour des libellés
        getTraductionHelper().add(transaction);
    }

    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Suppression de tous les libellés
        getTraductionHelper().delete(transaction);
    }

    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
        trLibelles = null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Mise à jour des libellés
        getTraductionHelper().update(transaction);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CASQCTP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idSequenceContentieux = statement.dbReadNumeric("IDSEQCON");
        idTraduction = statement.dbReadNumeric("IDTRADUCTION");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdSequenceContentieux(), getSession().getLabel("7235"));
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement
                .writeKey("IDSEQCON", this._dbWriteNumeric(statement.getTransaction(), getIdSequenceContentieux(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDSEQCON",
                this._dbWriteNumeric(statement.getTransaction(), getIdSequenceContentieux(), "idSeqCon"));
        statement.writeField("IDTRADUCTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTraduction(), "idTraduction"));
    }

    @Override
    public String getDescription() {
        // Description dans la langue de l'utilisateur
        return this.getDescription(null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDescription(String codeIsoLangue) {

        String s = "";
        try {
            s = PATraductionHelper.translate(getSession(), getIdTraduction(), codeIsoLangue);
        } catch (Exception e) {
            _addError(null, e.toString());
        }
        return s;
    }

    @Override
    public String getIdentificationSource() {
        return _getTableName();
    }

    /**
     * Getter
     */
    public java.lang.String getIdSequenceContentieux() {
        return idSequenceContentieux;
    }

    @Override
    public java.lang.String getIdTraduction() {
        return idTraduction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 14:57:23)
     * 
     * @return globaz.norma.db.fondation.PATraductionHelper
     */
    private PATraductionHelper getTraductionHelper() {
        if (trLibelles == null) {
            try {
                trLibelles = new PATraductionHelper(this);
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }

        return trLibelles;
    }

    @Override
    public void setDescription(String newDescription) throws Exception {
        this.setDescription(newDescription, null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            java.lang.String
     */
    @Override
    public void setDescription(String newDescription, String codeISOLangue) {
        getTraductionHelper().setDescription(newDescription, codeISOLangue);
        if (getTraductionHelper().getError() != null) {
            _addError(null, getTraductionHelper().getError().getMessage());
        }
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionDe(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "DE");
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionFr(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "FR");
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionIt(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "IT");
    }

    /**
     * Setter
     */
    public void setIdSequenceContentieux(java.lang.String newIdSequenceContentieux) {
        idSequenceContentieux = newIdSequenceContentieux;
    }

    @Override
    public void setIdTraduction(java.lang.String newIdTraduction) {
        idTraduction = newIdTraduction;
    }
}
