package globaz.norma.db.fondation;

import globaz.jade.client.util.JadeStringUtil;

/**
 * Insérez la description du type ici. Date de création : (18.12.2001 14:50:42)
 * 
 * @author: Administrator
 */
public class PATraduction extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String codeISOLangue = new String();
    private java.lang.String entiteSource = new String();
    private java.lang.String idTraduction = new String();
    private java.lang.String libelle = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur PATraduction
     */
    public PATraduction() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.01.2002 15:43:05)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente le prochain numéro
        if (JadeStringUtil.isIntegerEmpty(getIdTraduction())) {
            setIdTraduction(_incCounter(transaction, idTraduction));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "PMTRADP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        codeISOLangue = statement.dbReadString("CODEISOLANGUE");
        entiteSource = statement.dbReadString("ENTITESOURCE");
        idTraduction = statement.dbReadNumeric("IDTRADUCTION");
        libelle = statement.dbReadString("LIBELLE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdTraduction(), getSession().getLabel("7200"));
        _propertyMandatory(statement.getTransaction(), getCodeISOLangue(), getSession().getLabel("7201"));
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("CODEISOLANGUE", _dbWriteString(statement.getTransaction(), getCodeISOLangue(), ""));
        statement.writeKey("IDTRADUCTION", _dbWriteNumeric(statement.getTransaction(), getIdTraduction(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("CODEISOLANGUE",
                _dbWriteString(statement.getTransaction(), getCodeISOLangue(), "codeISOLangue"));
        statement.writeField("ENTITESOURCE",
                _dbWriteString(statement.getTransaction(), getEntiteSource(), "entiteSource"));
        statement.writeField("IDTRADUCTION",
                _dbWriteNumeric(statement.getTransaction(), getIdTraduction(), "idTraduction"));
        statement.writeField("LIBELLE", _dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
    }

    /**
     * Getter
     */
    public java.lang.String getCodeISOLangue() {
        return codeISOLangue;
    }

    public java.lang.String getEntiteSource() {
        return entiteSource;
    }

    public java.lang.String getIdTraduction() {
        return idTraduction;
    }

    public java.lang.String getLibelle() {
        return libelle;
    }

    /**
     * Setter
     */
    public void setCodeISOLangue(java.lang.String newCodeISOLangue) {
        codeISOLangue = newCodeISOLangue.toUpperCase();
    }

    public void setEntiteSource(java.lang.String newEntiteSource) {
        entiteSource = newEntiteSource;
    }

    public void setIdTraduction(java.lang.String newIdTraduction) {
        idTraduction = newIdTraduction;
    }

    public void setLibelle(java.lang.String newLibelle) {
        libelle = newLibelle;
    }
}
