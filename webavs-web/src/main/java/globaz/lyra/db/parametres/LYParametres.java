/*
 * Créé le 13 oct. 06
 */
package globaz.lyra.db.parametres;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author hpe
 * 
 *         BEntity des paramètres
 */
public class LYParametres extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String FIELDNAME_DEFAULTVALUE = "WNLDEF";

    public static final String FIELDNAME_IDECHEANCES = "WNIECH";
    public static final String FIELDNAME_IDPARAMETRES = "WNIPAR";
    public static final String FIELDNAME_LIBELLEPARAMETRE_DE = "WNLLID";
    public static final String FIELDNAME_LIBELLEPARAMETRE_FR = "WNLLIF";
    public static final String FIELDNAME_LIBELLEPARAMETRE_IT = "WNLLII";
    public static final String FIELDNAME_NOMGROUPEPARAMETRE = "WNLNGR";
    public static final String FIELDNAME_NOMPARAMETRE = "WNLNOM";
    public static final String FIELDNAME_TYPEPARAMETRE = "WNTTYP";
    private static final long serialVersionUID = -2880414244047132272L;

    public static final String TABLE_NAME = "LYPARAME";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String defaultValue = "";
    private String idEcheances = "";
    private String idParametres = "";
    private String libelleParametreDE = "";
    private String libelleParametreFR = "";
    private String libelleParametreIT = "";
    private String nomGroupeParametre = "";
    private String nomParametre = "";
    private String typeParametre = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe LYParametres.
     */
    public LYParametres() {
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
        setIdParametres(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des paramètres
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return LYParametres.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des paramètres des échéances
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idParametres = statement.dbReadNumeric(LYParametres.FIELDNAME_IDPARAMETRES);
        idEcheances = statement.dbReadNumeric(LYParametres.FIELDNAME_IDECHEANCES);
        nomParametre = statement.dbReadString(LYParametres.FIELDNAME_NOMPARAMETRE);
        typeParametre = statement.dbReadNumeric(LYParametres.FIELDNAME_TYPEPARAMETRE);
        nomGroupeParametre = statement.dbReadString(LYParametres.FIELDNAME_NOMGROUPEPARAMETRE);
        defaultValue = statement.dbReadString(LYParametres.FIELDNAME_DEFAULTVALUE);
        libelleParametreDE = statement.dbReadString(LYParametres.FIELDNAME_LIBELLEPARAMETRE_DE);
        libelleParametreFR = statement.dbReadString(LYParametres.FIELDNAME_LIBELLEPARAMETRE_FR);
        libelleParametreIT = statement.dbReadString(LYParametres.FIELDNAME_LIBELLEPARAMETRE_IT);

    }

    /**
     * Méthode de validation des paramètres
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    /**
     * Définition de la clé primaire de la table des paramètres
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey(LYParametres.FIELDNAME_IDPARAMETRES,
                this._dbWriteNumeric(statement.getTransaction(), idParametres, "idParametres"));

    }

    /**
     * Méthode d'écriture des champs dans la table des paramètres
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(LYParametres.FIELDNAME_IDPARAMETRES,
                this._dbWriteNumeric(statement.getTransaction(), idParametres, "idParametres"));
        statement.writeField(LYParametres.FIELDNAME_IDECHEANCES,
                this._dbWriteNumeric(statement.getTransaction(), idEcheances, "idEcheances"));
        statement.writeField(LYParametres.FIELDNAME_NOMPARAMETRE,
                this._dbWriteString(statement.getTransaction(), nomParametre, "nomParametre"));
        statement.writeField(LYParametres.FIELDNAME_TYPEPARAMETRE,
                this._dbWriteNumeric(statement.getTransaction(), typeParametre, "typeParametre"));
        statement.writeField(LYParametres.FIELDNAME_NOMGROUPEPARAMETRE,
                this._dbWriteString(statement.getTransaction(), nomGroupeParametre, "nomGroupeParametre"));
        statement.writeField(LYParametres.FIELDNAME_DEFAULTVALUE,
                this._dbWriteString(statement.getTransaction(), defaultValue, "defaultValue"));
        statement.writeField(LYParametres.FIELDNAME_LIBELLEPARAMETRE_DE,
                this._dbWriteString(statement.getTransaction(), libelleParametreDE, "libelleParametreDE"));
        statement.writeField(LYParametres.FIELDNAME_LIBELLEPARAMETRE_FR,
                this._dbWriteString(statement.getTransaction(), libelleParametreFR, "libelleParametreFR"));
        statement.writeField(LYParametres.FIELDNAME_LIBELLEPARAMETRE_IT,
                this._dbWriteString(statement.getTransaction(), libelleParametreIT, "libelleParametreIT"));

    }

    /**
     * getter pour l'attribut defaultValue
     * 
     * @return la valeur courante de l'attribut defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * getter pour l'attribut idEcheances
     * 
     * @return la valeur courante de l'attribut idEcheances
     */
    public String getIdEcheances() {
        return idEcheances;
    }

    /**
     * getter pour l'attribut idParametres
     * 
     * @return la valeur courante de l'attribut idParametres
     */
    public String getIdParametres() {
        return idParametres;
    }

    /**
     * getter pour l'attribut libelleParametreDE
     * 
     * @return la valeur courante de l'attribut libelleParametreDE
     */
    public String getLibelleParametreDE() {
        return libelleParametreDE;
    }

    /**
     * getter pour l'attribut libelleParametreFR
     * 
     * @return la valeur courante de l'attribut libelleParametreFR
     */
    public String getLibelleParametreFR() {
        return libelleParametreFR;
    }

    /**
     * getter pour l'attribut libelleParametreIT
     * 
     * @return la valeur courante de l'attribut libelleParametreIT
     */
    public String getLibelleParametreIT() {
        return libelleParametreIT;
    }

    /**
     * getter pour l'attribut typeParametre
     * 
     * @return la valeur courante de l'attribut typeParametre
     */
    public String getNomGroupeParametre() {
        return nomGroupeParametre;
    }

    /**
     * getter pour l'attribut nomParametre
     * 
     * @return la valeur courante de l'attribut nomParametre
     */
    public String getNomParametre() {
        return nomParametre;
    }

    /**
     * getter pour l'attribut typeParametre
     * 
     * @return la valeur courante de l'attribut typeParametre
     */
    public String getTypeParametre() {
        return typeParametre;
    }

    /**
     * setter pour l'attribut defaultValue
     * 
     * @param defaultValue
     *            une nouvelle valeur pour cet attribut
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * setter pour l'attribut idEcheances
     * 
     * @param idEcheances
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEcheances(String idEcheances) {
        this.idEcheances = idEcheances;
    }

    /**
     * setter pour l'attribut idParametres
     * 
     * @param idParametres
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParametres(String idParametres) {
        this.idParametres = idParametres;
    }

    /**
     * setter pour l'attribut libelleParametreDE
     * 
     * @param libelleParametreDE
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibelleParametreDE(String libelleParametreDE) {
        this.libelleParametreDE = libelleParametreDE;
    }

    /**
     * setter pour l'attribut libelleParametreFR
     * 
     * @param libelleParametreFR
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibelleParametreFR(String libelleParametreFR) {
        this.libelleParametreFR = libelleParametreFR;
    }

    /**
     * setter pour l'attribut libelleParametreIT
     * 
     * @param libelleParametreIT
     *            une nouvelle valeur pour cet attribut
     */
    public void setLibelleParametreIT(String libelleParametreIT) {
        this.libelleParametreIT = libelleParametreIT;
    }

    /**
     * setter pour l'attribut typeParametre
     * 
     * @param typeParametre
     *            une nouvelle valeur pour cet attribut
     */
    public void setNomGroupeParametre(String nomGroupeParametre) {
        this.nomGroupeParametre = nomGroupeParametre;
    }

    /**
     * setter pour l'attribut nomParametre
     * 
     * @param nomParametre
     *            une nouvelle valeur pour cet attribut
     */
    public void setNomParametre(String nomParametre) {
        this.nomParametre = nomParametre;
    }

    /**
     * setter pour l'attribut typeParametre
     * 
     * @param typeParametre
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeParametre(String typeParametre) {
        this.typeParametre = typeParametre;
    }

}
