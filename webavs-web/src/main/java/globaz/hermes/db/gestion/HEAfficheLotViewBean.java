package globaz.hermes.db.gestion;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (19.03.2003 17:54:05)
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
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la base de donn�es
     * 
     * @exception java.lang.Exception
     *                si la lecture des propri�t�s a �chou�e
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idLot = statement.dbReadNumeric("RMILOT");
    }

    /**
     * Valide le contenu de l'entit� (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� composant une cl� altern�e
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     * @param alternateKey
     *            int le num�ro de la cl� altern�e � utiliser
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        // Traitement par d�faut : pas de cl� altern�e
        throw new Exception("Alternate key not implemented for this entity");
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� composant la cl� primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + "HELOTSP.RMILOT",
                _dbWriteNumeric(statement.getTransaction(), getIdLot(), ""));
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� dans la base de donn�es
     * 
     * @param statement
     *            l'instruction � utiliser
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.03.2003 18:06:39)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdLot() {
        return idLot;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.03.2003 18:06:39)
     * 
     * @param newIdLot
     *            java.lang.String
     */
    public void setIdLot(java.lang.String newIdLot) {
        idLot = newIdLot;
    }
}
