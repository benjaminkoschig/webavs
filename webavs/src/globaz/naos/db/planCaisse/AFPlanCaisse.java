/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.planCaisse;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.helper.IAFPlanCaisseHelper;
import globaz.naos.db.cotisation.AFCotisationListViewBean;
import globaz.naos.db.couverture.AFCouvertureListViewBean;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.io.Serializable;

/**
 * La classe définissant l'entité PlanCaisse.
 * 
 * @author sau
 */
public class AFPlanCaisse extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TIAdministrationViewBean _administration = null;
    // Fields
    private java.lang.String idTiers = new String();
    private java.lang.String libelleAllemand = new String();
    private java.lang.String libelleFrancais = new String();
    private java.lang.String libelleItalien = new String();
    // DB Table AFPLCAP
    // Primary Key
    private java.lang.String planCaisseId = new String();

    private java.lang.String typeAffiliation = new String();

    /**
     * Constructeur d'AFPlanCaisse.
     */
    public AFPlanCaisse() {
        super();
        setMethodsToLoad(IAFPlanCaisseHelper.METHODS_TO_LOAD);
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setPlanCaisseId(this._incCounter(transaction, "0"));
    }

    /**
     * Effectue des traitements avant une suppression de la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {

        AFCotisationListViewBean cotisationList = new AFCotisationListViewBean();
        cotisationList.setForPlanCaisseId(getPlanCaisseId());
        cotisationList.setSession(transaction.getSession());
        cotisationList.find();

        if (cotisationList.size() > 0) {
            _addError(transaction, transaction.getSession().getLabel("1250"));
        }

        AFCouvertureListViewBean couvertureList = new AFCouvertureListViewBean();
        couvertureList.setForPlanCaisseId(getPlanCaisseId());
        couvertureList.setSession(transaction.getSession());
        couvertureList.find();

        if (couvertureList.size() > 0) {
            _addError(transaction, transaction.getSession().getLabel("1260"));
        }
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFPLCAP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        planCaisseId = statement.dbReadNumeric("MSIPLC");
        idTiers = statement.dbReadNumeric("HTITIE");
        libelleFrancais = statement.dbReadString("MSLLIB");
        libelleAllemand = statement.dbReadString("MSLLIA");
        libelleItalien = statement.dbReadString("MSLLII");
        typeAffiliation = statement.dbReadString("MSTTAF");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        // Test que les champs obligatoires soit renseignés
        validationOK &= _propertyMandatory(statement.getTransaction(), getIdTiers(), getSession().getLabel("890"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getLibelle(), getSession().getLabel("900"));
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MSIPLC", this._dbWriteNumeric(statement.getTransaction(), getPlanCaisseId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MSIPLC",
                this._dbWriteNumeric(statement.getTransaction(), getPlanCaisseId(), "planCaisseId"));
        statement.writeField("HTITIE", this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), "idTiers"));
        statement.writeField("MSLLIB",
                this._dbWriteString(statement.getTransaction(), getLibelleFrancais(), "libelleFrancais"));
        statement.writeField("MSLLIA",
                this._dbWriteString(statement.getTransaction(), getLibelleAllemand(), "libelleAllemand"));
        statement.writeField("MSLLII",
                this._dbWriteString(statement.getTransaction(), getLibelleItalien(), "libelleItalien"));
        statement.writeField("MSTTAF",
                this._dbWriteNumeric(statement.getTransaction(), getTypeAffiliation(), "typeAffiliation"));
    }

    /**
     * Rechercher l'Administration (Tiers) du plan de caisse en fonction de son ID.
     * 
     * @return l'Administration (Tiers)
     */
    public TIAdministrationViewBean getAdministration() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdTiers())) {
            return null;
        }

        if (_administration == null) {

            _administration = new TIAdministrationViewBean();
            _administration.setSession(getSession());
            _administration.setIdTiersAdministration(getIdTiers());
            try {
                _administration.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _administration = null;
            }
        }
        return _administration;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Renvoie le Code de l'administration.
     * 
     * @return
     */
    public String getAdministrationNo() {
        TIAdministrationViewBean admin = getAdministration();
        if (admin != null) {
            return admin.getCodeAdministration();
        }
        return "";
    }

    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * Retourne le libelle du plan de caisse en fonction de la langue definie dans la session utilisateur.
     * 
     * @return le libelle du plan de caisse
     */
    public java.lang.String getLibelle() {
        String langue = getSession().getIdLangueISO();

        if (JACalendar.LANGUAGE_DE.equals(langue) && !("".equals(libelleAllemand))) {
            return libelleAllemand;
        } else if (JACalendar.LANGUAGE_IT.equals(langue) && !("".equals(libelleItalien))) {
            return libelleItalien;
        }
        return libelleFrancais;
    }

    public java.lang.String getLibelleAllemand() {
        return libelleAllemand;
    }

    public java.lang.String getLibelleFrancais() {
        return libelleFrancais;
    }

    public java.lang.String getLibelleItalien() {
        return libelleItalien;
    }

    public java.lang.String getPlanCaisseId() {
        return planCaisseId;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public java.lang.String getTypeAffiliation() {
        return typeAffiliation;
    }

    public void setIdTiers(java.lang.String string) {
        idTiers = string;
    }

    public void setLibelleAllemand(java.lang.String string) {
        libelleAllemand = string;
    }

    public void setLibelleFrancais(java.lang.String string) {
        libelleFrancais = string;
    }

    public void setLibelleItalien(java.lang.String string) {
        libelleItalien = string;
    }

    public void setPlanCaisseId(java.lang.String string) {
        planCaisseId = string;
    }

    public void setTypeAffiliation(java.lang.String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }
}
