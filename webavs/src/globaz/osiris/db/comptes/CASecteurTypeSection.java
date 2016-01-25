package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 08:57:02)
 * 
 * @author: Administrator
 */
public class CASecteurTypeSection extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idCompteCourant = new String();
    private java.lang.String idSecteur = new String();
    private java.lang.String idTypeSection = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CASecteurTypeSection
     */
    public CASecteurTypeSection() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CASETSP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTypeSection = statement.dbReadNumeric("IDTYPESECTION");
        idSecteur = statement.dbReadNumeric("IDSECTEUR");
        idCompteCourant = statement.dbReadNumeric("IDCOMPTECOURANT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDTYPESECTION", this._dbWriteNumeric(statement.getTransaction(), getIdTypeSection(), ""));
        statement.writeKey("IDSECTEUR", this._dbWriteNumeric(statement.getTransaction(), getIdSecteur(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDTYPESECTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeSection(), "idTypeSection"));
        statement
                .writeField("IDSECTEUR", this._dbWriteNumeric(statement.getTransaction(), getIdSecteur(), "idSecteur"));
        statement.writeField("IDCOMPTECOURANT",
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteCourant(), "idCompteCourant"));
    }

    public java.lang.String getIdCompteCourant() {
        return idCompteCourant;
    }

    public java.lang.String getIdSecteur() {
        return idSecteur;
    }

    /**
     * Getter
     */
    public java.lang.String getIdTypeSection() {
        return idTypeSection;
    }

    public void setIdCompteCourant(java.lang.String newIdCompteCourant) {
        idCompteCourant = newIdCompteCourant;
    }

    public void setIdSecteur(java.lang.String newIdSecteur) {
        idSecteur = newIdSecteur;
    }

    /**
     * Setter
     */
    public void setIdTypeSection(java.lang.String newIdTypeSection) {
        idTypeSection = newIdTypeSection;
    }
}
