package globaz.aquila.db.access.batch;

import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.norma.db.fondation.IntTranslatable;
import globaz.norma.db.fondation.PATraductionHelper;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;

/**
 * Date de création : (17.12.2001 09:55:51)
 */
public class COCalculTaxe extends globaz.globall.db.BEntity implements java.io.Serializable, IntTranslatable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String AMENDE_STATUTAIRE_AVS = "5120002";

    public static final String AMENDE_STATUTAIRE_AVS_PARITAIRE = "5120006";
    public static final String AMENDE_STATUTAIRE_PS = "5120004";
    public static final String AMENDE_STATUTAIRE_PS_PARITAIRE = "5120007";
    public static final String AVANCE_DE_FRAIS = "5120003";
    public final static java.lang.String COMPTE_COURANT = "220002";
    public static final String FNAME_BASE_TAXE = "OITBTX";
    public static final String FNAME_ID_CALCUL_TAXE = "OIICTX";

    public static final String FNAME_ID_RUBRIQUE = "OIIRUB";
    public static final String FNAME_ID_TRADUCTION = "OIITRA";
    public static final String FNAME_MONTANT_FIXE = "OIMFIX";
    public static final String FNAME_TYPE_TAXE = "OITTYP";
    public static final String FNAME_TYPE_TAXE_ETAPE = "OITTTE";
    public final static java.lang.String MASSE = "220004";
    public final static java.lang.String MONTANT = "219002";
    public final static java.lang.String MONTANT_TAUX = "219003";
    public final static java.lang.String RUBRIQUE = "220003";

    // code systeme

    public final static java.lang.String SECTION = "220001";
    public static final String TABLE_NAME = "COTXCTP";
    public final static java.lang.String TAUX = "219001";
    public static final String TAXE_DE_SOMMATION = "5120001";

    public static final String TAXE_DE_SOMMATION_AF = "5120009";
    public static final String TAXE_DE_SOMMATION_AF_PARITAIRE = "5120011";
    public static final String TAXE_DE_SOMMATION_AVS = "5120008";
    public static final String TAXE_DE_SOMMATION_AVS_PARITAIRE = "5120010";
    public static final String TAXE_DE_SOMMATION_PARITAIRE = "5120005";
    private globaz.osiris.db.comptes.CARubrique _rubrique;
    private java.lang.String baseTaxe = new String();

    private COParametreTaxeManager cacheParametreTaxeManager = null;
    private COTrancheTaxeManager cacheTrancheTaxeManager = null;
    private FWParametersSystemCode csBaseTaxe = null;
    private FWParametersSystemCodeManager csBaseTaxes = null;
    private FWParametersSystemCode csTypeTaxe = null;
    private FWParametersSystemCodeManager csTypeTaxes = null;
    private java.lang.String idCalculTaxe = new String();
    private java.lang.String idRubrique = new String();
    private java.lang.String idTraduction = new String();
    private java.lang.String montantFixe = new String();
    private transient PATraductionHelper trLibelles = null;

    private java.lang.String typeTaxe = new String();
    private String typeTaxeEtape = "";

    /**
     * Commentaire relatif au constructeur CACalculTaxe
     */
    public COCalculTaxe() {
        super();
    }

    /**
     * Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        setIdCalculTaxe(this._incCounter(transaction, "0"));
        // Mise à jour des libellés
        getTraductionHelper().add(transaction);
    }

    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Suppression de tous les libellés
        getTraductionHelper().delete(transaction);
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
        return COCalculTaxe.TABLE_NAME;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        typeTaxe = statement.dbReadNumeric(COCalculTaxe.FNAME_TYPE_TAXE);
        montantFixe = statement.dbReadNumeric(COCalculTaxe.FNAME_MONTANT_FIXE, 2);
        baseTaxe = statement.dbReadNumeric(COCalculTaxe.FNAME_BASE_TAXE);
        idCalculTaxe = statement.dbReadNumeric(COCalculTaxe.FNAME_ID_CALCUL_TAXE);
        idRubrique = statement.dbReadNumeric(COCalculTaxe.FNAME_ID_RUBRIQUE);
        idTraduction = statement.dbReadNumeric(COCalculTaxe.FNAME_ID_TRADUCTION);
        typeTaxeEtape = statement.dbReadNumeric(COCalculTaxe.FNAME_TYPE_TAXE_ETAPE);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(COCalculTaxe.FNAME_ID_CALCUL_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(COCalculTaxe.FNAME_TYPE_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getTypeTaxe(), "typeTaxe"));
        statement.writeField(COCalculTaxe.FNAME_MONTANT_FIXE,
                this._dbWriteNumeric(statement.getTransaction(), getMontantFixe(), "montantFixe"));
        statement.writeField(COCalculTaxe.FNAME_BASE_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getBaseTaxe(), "baseTaxe"));
        statement.writeField(COCalculTaxe.FNAME_ID_CALCUL_TAXE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), "idCalculTaxe"));
        statement.writeField(COCalculTaxe.FNAME_ID_RUBRIQUE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField(COCalculTaxe.FNAME_ID_TRADUCTION,
                this._dbWriteNumeric(statement.getTransaction(), getIdTraduction(), "idTraduction"));
        statement.writeField(COCalculTaxe.FNAME_TYPE_TAXE_ETAPE,
                this._dbWriteNumeric(statement.getTransaction(), typeTaxeEtape, "typeTaxeEtape"));
    }

    public java.lang.String getBaseTaxe() {
        return baseTaxe;
    }

    public FWParametersSystemCode getCsBaseTaxe() {

        if (csBaseTaxe == null) {
            // liste pas encore chargee, on la charge
            csBaseTaxe = new FWParametersSystemCode();
            csBaseTaxe.getCode(getBaseTaxe());
        }
        return csBaseTaxe;
    }

    /**
     * Date de création : (13.12.2001 11:19:02) <br />
     * 
     * @return globaz.bambou.db.AJCodeSystemeManager
     */
    public FWParametersSystemCodeManager getCsBaseTaxes() {
        // liste déjà chargée ?
        if (csBaseTaxes == null) {
            // liste pas encore chargée, on la charge
            csBaseTaxes = new FWParametersSystemCodeManager();
            csBaseTaxes.setSession(getSession());
            csBaseTaxes.getListeCodesSup("OSIBASTAX", getSession().getIdLangue());
        }
        return csBaseTaxes;
    }

    public FWParametersSystemCode getCsTypeTaxe() {

        if (csTypeTaxe == null) {
            // liste pas encore chargee, on la charge
            csTypeTaxe = new FWParametersSystemCode();
            csTypeTaxe.getCode(getTypeTaxe());
        }
        return csTypeTaxe;
    }

    /**
     * Date de création : (13.12.2001 11:19:02) <br />
     * 
     * @return globaz.bambou.db.AJCodeSystemeManager
     */
    public FWParametersSystemCodeManager getCsTypeTaxes() {
        // liste déjà chargée ?
        if (csTypeTaxes == null) {
            // liste pas encore chargée, on la charge
            csTypeTaxes = new FWParametersSystemCodeManager();
            csTypeTaxes.setSession(getSession());
            csTypeTaxes.getListeCodesSup("OSITYPTAX", getSession().getIdLangue());
        }
        return csTypeTaxes;
    }

    @Override
    public String getDescription() {
        // Description dans la langue de l'utilisateur
        return this.getDescription(null);
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

    public java.lang.String getIdCalculTaxe() {
        return idCalculTaxe;
    }

    @Override
    public String getIdentificationSource() {
        return _getTableName();
    }

    public java.lang.String getIdRubrique() {
        return idRubrique;
    }

    @Override
    public java.lang.String getIdTraduction() {
        return idTraduction;
    }

    public java.lang.String getMontantFixe() {
        return montantFixe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 08:52:50)
     * 
     * @return globaz.osiris.db.contentieux.CAParametreTaxeManager
     */
    public COParametreTaxeManager getParametreTaxes() {
        // liste déjà chargée ?
        if (cacheParametreTaxeManager == null) {
            // liste pas encore chargée, on la charge
            cacheParametreTaxeManager = new COParametreTaxeManager();
            cacheParametreTaxeManager.setSession(getSession());
            cacheParametreTaxeManager.setForIdCalculTaxe(getIdCalculTaxe());
            try {
                cacheParametreTaxeManager.find();
                if (cacheParametreTaxeManager.isEmpty()) {
                    cacheParametreTaxeManager = null;
                }

            } catch (Exception e) {
                cacheParametreTaxeManager = null;
            }
        }
        return cacheParametreTaxeManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public globaz.osiris.db.comptes.CARubrique getRubrique() {

        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdRubrique())) {
            return new CARubrique();
        }

        // Si log pas déjà chargé
        if (_rubrique == null) {
            // Instancier un nouveau LOG
            _rubrique = new globaz.osiris.db.comptes.CARubrique();
            _rubrique.setSession(getSession());

            // Récupérer le log en question
            _rubrique.setIdRubrique(getIdRubrique());
            try {
                _rubrique.retrieve();
                if (_rubrique.isNew()) {
                    _rubrique = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _rubrique = null;
            }
        }

        return _rubrique != null ? _rubrique : new CARubrique();
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

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 07:42:50)
     * 
     * @return globaz.osiris.db.contentieux.CATrancheTaxeManager
     */
    public COTrancheTaxeManager getTrancheTaxes() {
        // liste déjà chargée ?
        if (cacheTrancheTaxeManager == null) {
            // liste pas encore chargée, on la charge
            cacheTrancheTaxeManager = new COTrancheTaxeManager();
            cacheTrancheTaxeManager.setSession(getSession());
            cacheTrancheTaxeManager.setForIdCalculTaxe(getIdCalculTaxe());
            try {
                cacheTrancheTaxeManager.find();
                if (cacheTrancheTaxeManager.isEmpty()) {
                    cacheTrancheTaxeManager = null;
                }

            } catch (Exception e) {
                cacheTrancheTaxeManager = null;
            }
        }
        return cacheTrancheTaxeManager;
    }

    /**
     * Getter
     */
    public java.lang.String getTypeTaxe() {
        return typeTaxe;
    }

    public String getTypeTaxeEtape() {
        return typeTaxeEtape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.07.2002 07:53:19)
     * 
     * @return boolean
     */
    public boolean isFraisPoursuite() {
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdRubrique(getIdRubrique());
        try {
            rubrique.retrieve();
            if (rubrique.getNatureRubrique().equals(APIRubrique.FRAIS_POURSUITES)) {
                return true;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    public void setBaseTaxe(java.lang.String newBaseTaxe) {
        baseTaxe = newBaseTaxe;
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

    /**
     * Description dans la langue fournie<br />
     * Date de création : (19.12.2001 10:56:02) <br />
     * 
     * @param newDescription
     *            java.lang.String <br />
     * @param codeISOLangue
     *            java.lang.String
     */
    public void setDescriptionDe(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "DE");
    }

    /**
     * Description dans la langue fournie <br />
     * Date de création : (19.12.2001 10:56:02) <br />
     * 
     * @param newDescription
     *            java.lang.String <br />
     * @param codeISOLangue
     *            java.lang.String
     */
    public void setDescriptionFr(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "FR");
    }

    /**
     * Description dans la langue fournie <br />
     * Date de création : (19.12.2001 10:56:02) <br />
     * 
     * @param newDescription
     *            java.lang.String <br />
     * @param codeISOLangue
     *            java.lang.String
     */
    public void setDescriptionIt(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "IT");
    }

    public void setIdCalculTaxe(java.lang.String newIdCalculTaxe) {
        idCalculTaxe = newIdCalculTaxe;
    }

    public void setIdRubrique(java.lang.String newIdRubrique) {
        idRubrique = newIdRubrique;

    }

    @Override
    public void setIdTraduction(java.lang.String newIdTraduction) {
        idTraduction = newIdTraduction;
    }

    public void setMontantFixe(java.lang.String newMontantFixe) {
        montantFixe = newMontantFixe;
    }

    /**
     * Setter
     */
    public void setTypeTaxe(java.lang.String newTypeTaxe) {
        typeTaxe = newTypeTaxe;
    }

    public void setTypeTaxeEtape(String string) {
        typeTaxeEtape = string;
    }

}
