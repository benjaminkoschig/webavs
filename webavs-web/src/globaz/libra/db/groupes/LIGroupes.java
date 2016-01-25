package globaz.libra.db.groupes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.libra.db.utilisateurs.LIUtilisateurs;
import globaz.libra.db.utilisateurs.LIUtilisateursManager;
import java.util.Iterator;

/**
 * 
 * @author HPE
 * 
 */
public class LIGroupes extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_DOMAINE = "AEIDOM";
    public static final String FIELDNAME_ID_GROUPE = "AEIGRO";
    public static final String FIELDNAME_LIBELLE_GROUPE = "AELLIB";

    public static final String TABLE_NAME = "LIGROUP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idDomaine = new String();
    private String idGroupe = new String();
    private String libelleGroupe = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LIGroupes.
     */
    public LIGroupes() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdGroupe(_incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des groupes
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des groupes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idGroupe = statement.dbReadNumeric(FIELDNAME_ID_GROUPE);
        libelleGroupe = statement.dbReadString(FIELDNAME_LIBELLE_GROUPE);
        idDomaine = statement.dbReadNumeric(FIELDNAME_ID_DOMAINE);

    }

    /**
     * Méthode de validation des groupes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * Définition de la clé primaire de la table des groupes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(FIELDNAME_ID_GROUPE, _dbWriteNumeric(statement.getTransaction(), idGroupe, "idGroupe"));

    }

    /**
     * Méthode d'écriture des champs dans la table des groupes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(FIELDNAME_ID_GROUPE, _dbWriteNumeric(statement.getTransaction(), idGroupe, "idGroupe"));
        statement.writeField(FIELDNAME_LIBELLE_GROUPE,
                _dbWriteString(statement.getTransaction(), libelleGroupe, "libelleGroupe"));
        statement.writeField(FIELDNAME_ID_DOMAINE, _dbWriteNumeric(statement.getTransaction(), idDomaine, "idDomaine"));

    }

    public String getIdDomaine() {
        return idDomaine;
    }

    public String getIdGroupe() {
        return idGroupe;
    }

    public String getLibelleGroupe() {
        return libelleGroupe;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public boolean isUserInGroup(String idUserExterne) throws Exception {

        LIUtilisateursManager usrMgr = new LIUtilisateursManager();
        usrMgr.setSession(getSession());
        usrMgr.setForIdUserExterne(idUserExterne);
        usrMgr.find();

        for (Iterator iterator = usrMgr.iterator(); iterator.hasNext();) {
            LIUtilisateurs user = (LIUtilisateurs) iterator.next();

            if (user.getIdGroupe().equals(getIdGroupe())) {
                return true;
            }

        }
        return false;

    }

    public void setIdDomaine(String idDomaine) {
        this.idDomaine = idDomaine;
    }

    public void setIdGroupe(String idGroupe) {
        this.idGroupe = idGroupe;
    }

    public void setLibelleGroupe(String libelleGroupe) {
        this.libelleGroupe = libelleGroupe;
    }

}
