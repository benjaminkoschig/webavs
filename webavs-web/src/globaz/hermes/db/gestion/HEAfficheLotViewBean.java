package globaz.hermes.db.gestion;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (19.03.2003 17:54:05)
 * 
 * @author: Administrator
 */
public class HEAfficheLotViewBean extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idLot = "";

    /**
     * Commentaire relatif au constructeur HEAfficheLotViewBean.
     */
    public HEAfficheLotViewBean() {
        super();
    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HELOTSP INNER JOIN " + _getCollection() + "HEANNOP ON " + _getCollection() + "HEANNOP.RMILOT="
                + _getCollection() + "HELOTSP.RMILOT";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données
     * 
     * @exception java.lang.Exception
     *                si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idLot = statement.dbReadNumeric("RMILOT");
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant une clé alternée
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     * @param alternateKey
     *            int le numéro de la clé alternée à utiliser
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        // Traitement par défaut : pas de clé alternée
        throw new Exception("Alternate key not implemented for this entity");
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant la clé primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + "HELOTSP.RMILOT",
                _dbWriteNumeric(statement.getTransaction(), getIdLot(), ""));
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données
     * 
     * @param statement
     *            l'instruction à utiliser
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 18:06:39)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdLot() {
        return idLot;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.03.2003 18:06:39)
     * 
     * @param newIdLot
     *            java.lang.String
     */
    public void setIdLot(java.lang.String newIdLot) {
        idLot = newIdLot;
    }
}
