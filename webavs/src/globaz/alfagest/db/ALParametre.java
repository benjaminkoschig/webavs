package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Créé le 19 janv. 06
 * 
 * @author dch
 * 
 *         Représente un paramètre (JAFPPRM)
 */
public class ALParametre extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String identificationApplication = "";
    private String provenanceActeur = "";
    private String identificationActeur = "";
    private String typeParametre = "";
    private String dateValeur = "";
    private String plageValeurDe = "";
    private String plageValeurA = "";
    private String valeurAlphanumerique = "";
    private String valeurNumerique = "";
    private String designation = "";

    /**
     * Renvoie le nom de la table.
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "JAFPPRM";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données.
     * 
     * @exception java.lang.Exception si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        identificationApplication = statement.dbReadString("OAPPLI");
        provenanceActeur = statement.dbReadString("OPROV");
        identificationActeur = statement.dbReadNumeric("OIDACT");
        typeParametre = statement.dbReadString("OTPARM");
        dateValeur = statement.dbReadNumeric("ODVAL");
        plageValeurDe = statement.dbReadNumeric("OPLDE");
        plageValeurA = statement.dbReadNumeric("OPLA");
        valeurAlphanumerique = statement.dbReadString("OVALAN");
        valeurNumerique = statement.dbReadNumeric("OVALNU");
        designation = statement.dbReadString("ODESI");
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires).
     * 
     * @exception java.lang.Exception en cas d'erreur
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant la clé primaire.
     * 
     * @exception java.lang.Exception si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("OAPPLI", _dbWriteString(statement.getTransaction(), identificationApplication, ""));
        statement.writeKey("OPROV", _dbWriteString(statement.getTransaction(), provenanceActeur, ""));
        statement.writeKey("OIDACT", _dbWriteNumeric(statement.getTransaction(), identificationActeur, ""));
        statement.writeKey("OTPARM", _dbWriteString(statement.getTransaction(), typeParametre, ""));
        statement.writeKey("ODVAL", _dbWriteNumeric(statement.getTransaction(), dateValeur, ""));
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données.
     * 
     * @param statement l'instruction à utiliser
     * @exception java.lang.Exception si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("OAPPLI", _dbWriteString(statement.getTransaction(), identificationApplication, ""));
        statement.writeField("OPROV", _dbWriteString(statement.getTransaction(), provenanceActeur, ""));
        statement.writeField("OIDACT", _dbWriteNumeric(statement.getTransaction(), identificationActeur, ""));
        statement.writeField("OTPARM", _dbWriteString(statement.getTransaction(), typeParametre, ""));
        statement.writeField("ODVAL", _dbWriteNumeric(statement.getTransaction(), dateValeur, ""));
        statement.writeField("OPLDE", _dbWriteNumeric(statement.getTransaction(), plageValeurDe, ""));
        statement.writeField("OPLA", _dbWriteNumeric(statement.getTransaction(), plageValeurA, ""));
        statement.writeField("OVALAN", _dbWriteString(statement.getTransaction(), valeurAlphanumerique, ""));
        statement.writeField("OVALNU", _dbWriteNumeric(statement.getTransaction(), valeurNumerique, ""));
        statement.writeField("ODESI", _dbWriteString(statement.getTransaction(), designation, ""));
    }

    /**
     * Renvoie si l'entité contient un espion.
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * @return
     */
    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * @return
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * @return
     */
    public String getIdentificationActeur() {
        return identificationActeur;
    }

    /**
     * @return
     */
    public String getIdentificationApplication() {
        return identificationApplication;
    }

    /**
     * @return
     */
    public String getPlageValeurA() {
        return plageValeurA;
    }

    /**
     * @return
     */
    public String getPlageValeurDe() {
        return plageValeurDe;
    }

    /**
     * @return
     */
    public String getProvenanceActeur() {
        return provenanceActeur;
    }

    /**
     * @return
     */
    public String getTypeParametre() {
        return typeParametre;
    }

    /**
     * @return
     */
    public String getValeurAlphanumerique() {
        return valeurAlphanumerique;
    }

    /**
     * @return
     */
    public String getValeurNumerique() {
        return valeurNumerique;
    }

    /**
     * @param string
     */
    public void setDateValeur(String string) {
        dateValeur = string;
    }

    /**
     * @param string
     */
    public void setDesignation(String string) {
        designation = string;
    }

    /**
     * @param string
     */
    public void setIdentificationActeur(String string) {
        identificationActeur = string;
    }

    /**
     * @param string
     */
    public void setIdentificationApplication(String string) {
        identificationApplication = string;
    }

    /**
     * @param string
     */
    public void setPlageValeurA(String string) {
        plageValeurA = string;
    }

    /**
     * @param string
     */
    public void setPlageValeurDe(String string) {
        plageValeurDe = string;
    }

    /**
     * @param string
     */
    public void setProvenanceActeur(String string) {
        provenanceActeur = string;
    }

    /**
     * @param string
     */
    public void setTypeParametre(String string) {
        typeParametre = string;
    }

    /**
     * @param string
     */
    public void setValeurAlphanumerique(String string) {
        valeurAlphanumerique = string;
    }

    /**
     * @param string
     */
    public void setValeurNumerique(String string) {
        valeurNumerique = string;
    }

}