package globaz.helios.db.classifications;

public class CGLiaisonListeClasse extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idClasseCompte = new String();
    private java.lang.String idDefinitionListe = new String();
    private java.lang.String idPoliceDetail = new String();
    private java.lang.String idPoliceTitre = new String();
    private java.lang.String idPoliceTotal = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CGLiaisonListeClasse
     */
    public CGLiaisonListeClasse() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGLICLP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idClasseCompte = statement.dbReadNumeric("IDCLASSECOMPTE");
        idDefinitionListe = statement.dbReadNumeric("IDDEFINITIONLISTE");
        idPoliceTitre = statement.dbReadNumeric("IDPOLICETITRE");
        idPoliceTotal = statement.dbReadNumeric("IDPOLICETOTAL");
        idPoliceDetail = statement.dbReadNumeric("IDPOLICEDETAIL");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDCLASSECOMPTE", _dbWriteNumeric(statement.getTransaction(), getIdClasseCompte(), ""));
        statement
                .writeKey("IDDEFINITIONLISTE", _dbWriteNumeric(statement.getTransaction(), getIdDefinitionListe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDCLASSECOMPTE",
                _dbWriteNumeric(statement.getTransaction(), getIdClasseCompte(), "idClasseCompte"));
        statement.writeField("IDDEFINITIONLISTE",
                _dbWriteNumeric(statement.getTransaction(), getIdDefinitionListe(), "idDefinitionListe"));
        statement.writeField("IDPOLICETITRE",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTitre(), "idPoliceTitre"));
        statement.writeField("IDPOLICETOTAL",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceTotal(), "idPoliceTotal"));
        statement.writeField("IDPOLICEDETAIL",
                _dbWriteNumeric(statement.getTransaction(), getIdPoliceDetail(), "idPoliceDetail"));
    }

    /**
     * Getter
     */
    public java.lang.String getIdClasseCompte() {
        return idClasseCompte;
    }

    public java.lang.String getIdDefinitionListe() {
        return idDefinitionListe;
    }

    public java.lang.String getIdPoliceDetail() {
        return idPoliceDetail;
    }

    public java.lang.String getIdPoliceTitre() {
        return idPoliceTitre;
    }

    public java.lang.String getIdPoliceTotal() {
        return idPoliceTotal;
    }

    /**
     * Setter
     */
    public void setIdClasseCompte(java.lang.String newIdClasseCompte) {
        idClasseCompte = newIdClasseCompte;
    }

    public void setIdDefinitionListe(java.lang.String newIdDefinitionListe) {
        idDefinitionListe = newIdDefinitionListe;
    }

    public void setIdPoliceDetail(java.lang.String newIdPoliceDetail) {
        idPoliceDetail = newIdPoliceDetail;
    }

    public void setIdPoliceTitre(java.lang.String newIdPoliceTitre) {
        idPoliceTitre = newIdPoliceTitre;
    }

    public void setIdPoliceTotal(java.lang.String newIdPoliceTotal) {
        idPoliceTotal = newIdPoliceTotal;
    }
}
