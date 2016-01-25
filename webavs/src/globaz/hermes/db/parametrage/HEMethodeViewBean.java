package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;

/**
 * Insérez la description du type ici. Date de création : (13.11.2002 13:36:58)
 * 
 * @author: ado
 */
public class HEMethodeViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // type code
    private final static String TYPE_CODE = "11100004";
    // code systeme
    private FWParametersSystemCode csIdLibelle = null;
    /** (RHTLIB) */
    private String idLibelle = new String();
    /** Fichier HEMETHP */
    /** (RHIMET) */
    private String idMethode = new String();
    private String newCode = "";
    private String newLibelle = "";
    /** (RHTNOM) */
    private String nom = new String();

    /**
     * Commentaire relatif au constructeur HEMethodeViewBean
     */
    public HEMethodeViewBean() {
        super();
    }

    /**
     * Effectue des traitements avant une mise à jour dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant la mise à jour de l'entité dans la BD
     * <p>
     * L'exécution de la mise à jour n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        csIdLibelle.setCodeUti(getNewCode());
        csIdLibelle.setLibelleUti(getNewLibelle());
        csIdLibelle.update(transaction);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HEMETHP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idMethode = statement.dbReadNumeric("RHIMET", 0);
        idLibelle = statement.dbReadNumeric("RHTLIB", 0);
        nom = statement.dbReadString("RHTNOM");
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
        statement.writeKey("RHIMET", _dbWriteNumeric(statement.getTransaction(), getIdMethode(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("RHIMET", _dbWriteNumeric(statement.getTransaction(), getIdMethode(), "IdMethode"));
        statement.writeField("RHTLIB", _dbWriteNumeric(statement.getTransaction(), getIdLibelle(), "IdLibelle"));
        statement.writeField("RHTNOM", _dbWriteString(statement.getTransaction(), getNom(), "nom"));
    }

    public FWParametersSystemCode getCsIdLibelle() {
        if (csIdLibelle == null) {
            // liste pas encore chargee, on la charge
            csIdLibelle = new FWParametersSystemCode();
            csIdLibelle.getCode(getIdLibelle());

        }
        csIdLibelle.setSession(getSession());
        csIdLibelle.setIdTypeCode(TYPE_CODE);
        try {
            csIdLibelle.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return csIdLibelle;
    }

    public String getCUCode() {
        return getCsIdLibelle().getCurrentCodeUtilisateur().getCodeUtilisateur();
    }

    public String getCULibelle() {
        return getCsIdLibelle().getCurrentCodeUtilisateur().getLibelle();
    }

    public String getIdLibelle() {
        return idLibelle;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdMethode() {
        return idMethode;
    }

    public String getLibelle() {
        return getCsIdLibelle().getCurrentCodeUtilisateur().getCodeUtiLib();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 17:13:46)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNewCode() {
        return newCode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 17:13:46)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNewLibelle() {
        return newLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 13:48:22)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNom() {
        return nom;
    }

    public void setIdLibelle(String newIdLibelle) {
        idLibelle = newIdLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setIdMethode(String newIdMethode) {
        idMethode = newIdMethode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 17:13:46)
     * 
     * @param newNewCode
     *            java.lang.String
     */
    public void setNewCode(java.lang.String newNewCode) {
        newCode = newNewCode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 17:13:46)
     * 
     * @param newNewLibelle
     *            java.lang.String
     */
    public void setNewLibelle(java.lang.String newNewLibelle) {
        newLibelle = newNewLibelle;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 13:48:22)
     * 
     * @param newNom
     *            java.lang.String
     */
    public void setNom(java.lang.String newNom) {
        nom = newNom;
    }
}
