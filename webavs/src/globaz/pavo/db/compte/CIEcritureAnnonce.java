package globaz.pavo.db.compte;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CIEcritureAnnonce extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEcritureAssure = new String();
    private String idEcritureConjoint = new String();
    private String idRassemblement = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CFCAffiliation
     */
    public CIEcritureAnnonce() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CILRAEP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRassemblement = statement.dbReadNumeric("KPIRAO", 0);
        idEcritureAssure = statement.dbReadNumeric("KPIECA", 0);
        idEcritureConjoint = statement.dbReadNumeric("KPIECC", 0);

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**

 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KPIRAO", _dbWriteNumeric(statement.getTransaction(), getIdRassemblement(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("KPIRAO",
                _dbWriteNumeric(statement.getTransaction(), getIdRassemblement(), "idRassemblement"));
        statement.writeField("KPIECA",
                _dbWriteNumeric(statement.getTransaction(), getIdEcritureAssure(), "idEcritureAssure"));
        statement.writeField("KPIECC",
                _dbWriteNumeric(statement.getTransaction(), getIdEcritureConjoint(), "idEcritureConjoint"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.04.2003 15:44:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdEcritureAssure() {
        return idEcritureAssure;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.04.2003 15:44:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdEcritureConjoint() {
        return idEcritureConjoint;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.04.2003 15:44:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdRassemblement() {
        return idRassemblement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.04.2003 15:44:40)
     * 
     * @param newIdEcritureAssure
     *            java.lang.String
     */
    public void setIdEcritureAssure(java.lang.String newIdEcritureAssure) {
        idEcritureAssure = newIdEcritureAssure;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.04.2003 15:44:40)
     * 
     * @param newIdEcritureConjoint
     *            java.lang.String
     */
    public void setIdEcritureConjoint(java.lang.String newIdEcritureConjoint) {
        idEcritureConjoint = newIdEcritureConjoint;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.04.2003 15:44:40)
     * 
     * @param newIdRassemblement
     *            java.lang.String
     */
    public void setIdRassemblement(java.lang.String newIdRassemblement) {
        idRassemblement = newIdRassemblement;
    }
}
