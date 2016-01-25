package globaz.osiris.db.contentieux;

import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.log.JadeLogger;
import globaz.norma.db.fondation.IntTranslatable;
import globaz.norma.db.fondation.PATraductionHelper;
import globaz.osiris.api.APIEtape;

/*
 * Insérez la description du type ici. Date de création : (17.12.2001 08:40:16)
 * 
 * @author: Administrator
 */
public class CAEtape extends globaz.globall.db.BEntity implements java.io.Serializable, IntTranslatable, APIEtape {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // code système types étapes
    private FWParametersSystemCode csTypeEtape = null;
    private FWParametersSystemCodeManager csTypeEtapes = null;
    private java.lang.String idEtape = new String();
    private java.lang.String idTraduction = new String();
    private PATraductionHelper trLibelles = null;
    private java.lang.String typeEtape = new String();

    /**
     * Commentaire relatif au constructeur CAEtape
     */
    public CAEtape() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Mise à jour des libellés
        getTraductionHelper().add(transaction);
    }

    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Suppression de tous les libellés
        getTraductionHelper().delete(transaction);
    }

    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
        trLibelles = null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Mise à jour des libellés
        getTraductionHelper().update(transaction);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CAETCTP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idEtape = statement.dbReadNumeric("IDETAPE");
        typeEtape = statement.dbReadNumeric("TYPEETAPE");
        idTraduction = statement.dbReadNumeric("IDTRADUCTION");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdEtape(), getSession().getLabel("7180"));
        _propertyMandatory(statement.getTransaction(), getTypeEtape(), getSession().getLabel("7181"));

        // Vérifier le type d'étape
        if (getCsTypeEtapes().getCodeSysteme(getTypeEtape()) == null) {
            _addError(statement.getTransaction(), getSession().getLabel("7182"));
        }

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDETAPE", this._dbWriteNumeric(statement.getTransaction(), getIdEtape(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDETAPE", this._dbWriteNumeric(statement.getTransaction(), getIdEtape(), "idEtape"));
        statement
                .writeField("TYPEETAPE", this._dbWriteNumeric(statement.getTransaction(), getTypeEtape(), "typeEtape"));
        statement.writeField("IDTRADUCTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTraduction(), "idTraduction"));
    }

    public FWParametersSystemCode getCsTypeEtape() {

        if (csTypeEtape == null) {
            // liste pas encore chargee, on la charge
            csTypeEtape = new FWParametersSystemCode();
            csTypeEtape.getCode(getTypeEtape());
        }
        return csTypeEtape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2001 11:19:02)
     * 
     * @return globaz.bambou.db.AJCodeSystemeManager
     */
    public FWParametersSystemCodeManager getCsTypeEtapes() {
        // liste déjà chargée ?
        if (csTypeEtapes == null) {
            // liste pas encore chargée, on la charge
            csTypeEtapes = new FWParametersSystemCodeManager();
            csTypeEtapes.setSession(getSession());
            csTypeEtapes.getListeCodesSup("OSITYPETA", getSession().getIdLangue());
        }
        return csTypeEtapes;
    }

    @Override
    public String getDescription() {

        String iso = "FR";
        try {
            iso = getSession().getIdLangueISO();
        } catch (Exception e) {
            System.out.println("Exception in CAEtape.getDescription()");
            JadeLogger.error(this, e);
        }

        // Description dans la langue de l'utilisateur
        return this.getDescription(iso);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDescription(String codeIsoLangue) {

        String s = "";
        try {
            s = PATraductionHelper.translate(getSession(), getIdTraduction(), codeIsoLangue);
        } catch (Exception e) {
            _addError(null, e.toString());
        }
        return s;
    }

    @Override
    public String getIdentificationSource() {
        return _getTableName();
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdEtape() {
        return idEtape;
    }

    @Override
    public java.lang.String getIdTraduction() {
        return idTraduction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 14:57:23)
     * 
     * @return globaz.norma.db.fondation.PATraductionHelper
     */
    private PATraductionHelper getTraductionHelper() {
        if (trLibelles == null) {
            try {
                trLibelles = new PATraductionHelper(this);
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }

        return trLibelles;
    }

    @Override
    public java.lang.String getTypeEtape() {
        return typeEtape;
    }

    @Override
    public void setDescription(String newDescription) throws Exception {
        this.setDescription(newDescription, null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            java.lang.String
     */
    @Override
    public void setDescription(String newDescription, String codeISOLangue) {
        getTraductionHelper().setDescription(newDescription, codeISOLangue);
        if (getTraductionHelper().getError() != null) {
            _addError(null, getTraductionHelper().getError().getMessage());
        }
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    @Override
    public void setDescriptionDe(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "DE");
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    @Override
    public void setDescriptionFr(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "FR");
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    @Override
    public void setDescriptionIt(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "IT");
    }

    /**
     * Setter
     */
    @Override
    public void setIdEtape(java.lang.String newIdEtape) {
        idEtape = newIdEtape;
    }

    @Override
    public void setIdTraduction(java.lang.String newIdTraduction) {
        idTraduction = newIdTraduction;
    }

    @Override
    public void setTypeEtape(java.lang.String newTypeEtape) {
        typeEtape = newTypeEtape;
    }
}
