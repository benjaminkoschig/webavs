package globaz.osiris.db.comptes;

import globaz.norma.db.fondation.IntTranslatable;
import globaz.norma.db.fondation.PATraductionHelper;

/**
 * Insérez la description du type ici. Date de création : (11.12.2001 13:21:27)
 * 
 * @author: Administrator
 */
public class CASecteur extends globaz.globall.db.BEntity implements java.io.Serializable, IntTranslatable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idSecteur = new String();
    private java.lang.String idTraduction = new String();
    private PATraductionHelper trLibelles = null;

    // code systeme

    /**
     * Commentaire relatif au constructeur CASecteur
     */
    public CASecteur() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 13:36:49)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Mise à jour des libellés
        getTraductionHelper().add(transaction);

    }

    /**
     * Après supression
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Suppression de tous les libellés
        getTraductionHelper().delete(transaction);
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
        return "CASECOP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idSecteur = statement.dbReadNumeric("IDSECTEUR");
        idTraduction = statement.dbReadNumeric("IDTRADUCTION");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdSecteur(), getSession().getLabel("7165"));
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDSECTEUR", this._dbWriteNumeric(statement.getTransaction(), getIdSecteur(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement
                .writeField("IDSECTEUR", this._dbWriteNumeric(statement.getTransaction(), getIdSecteur(), "idSecteur"));
        statement.writeField("IDTRADUCTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTraduction(), "idTraduction"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.12.2001 15:16:55)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDescription() {
        // Description dans la langue de l'utilisateur
        return this.getDescription(null);
    }

    /**
     * Récupérer la description selon le code langue Date de création : (18.12.2001 15:16:55)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDescription(String codeIsoLangue) {

        String s = "";
        try {
            s = PATraductionHelper.translate(getSession(), getIdTraduction(), codeIsoLangue);
        } catch (Exception e) {
            _addError(null, e.toString());
        }
        return s;

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.12.2001 11:02:39)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdentificationSource() {
        return _getTableName();
    }

    /**
     * Getter
     */
    public java.lang.String getIdSecteur() {
        return idSecteur;
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

    /**
     * Description dans la langue de l'utilisateur Date de création : (19.12.2001 10:55:21)
     * 
     * @param newDescription
     *            java.lang.String
     */
    @Override
    public void setDescription(String newDescription) throws Exception {
        this.setDescription(newDescription, null);
    }

    /**
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    @Override
    public void setDescription(String newDescription, String codeISOLangue) throws Exception {
        getTraductionHelper().setDescription(newDescription, codeISOLangue);
        if (getTraductionHelper().getError() != null) {
            _addError(null, getTraductionHelper().getError().getMessage());
        }
    }

    /**
     * Setter
     */
    public void setIdSecteur(java.lang.String newIdSecteur) {
        idSecteur = newIdSecteur;
    }

    @Override
    public void setIdTraduction(java.lang.String newIdTraduction) {
        idTraduction = newIdTraduction;
    }
}
