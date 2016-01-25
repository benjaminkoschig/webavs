/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.nombreAssures;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.helper.IAFNombreAssuresHelper;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.pyxis.db.tiers.TITiers;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * La classe définissant l'entité NombreAssures.
 * 
 * @author sau
 */
public class AFNombreAssures extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAffiliation _affiliation = null;
    private AFAssurance _assurance = null;
    private TITiers _tiers = null;
    // Foreign Key
    private java.lang.String affiliationId = new String();
    // Fields
    private java.lang.String annee = new String();

    private java.lang.String assuranceId = new String();
    private java.lang.String nbrAssures = new String();
    // DB Table AFNASSP
    // Primary Key
    private java.lang.String nbrAssuresId = new String();

    private boolean noCallExternal = false;

    /**
     * Constructeur d'AFNombreAssures.
     */
    public AFNombreAssures() {
        super();
        setMethodsToLoad(IAFNombreAssuresHelper.METHODS_TO_LOAD);
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {

        setNbrAssuresId(this._incCounter(transaction, "0"));
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFNASSP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nbrAssuresId = statement.dbReadNumeric("MVINAS");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        assuranceId = statement.dbReadNumeric("MBIASS");
        annee = statement.dbReadNumeric("MVNANN");
        nbrAssures = statement.dbReadNumeric("MVNNBR");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        // Contrôle que les champs obligatoires soient renseignés
        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceId(), getSession().getLabel("520"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getAnnee(), getSession().getLabel("910"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getNbrAssures(), getSession().getLabel("920"));

        if (validationOK) {
            if (JadeStringUtil.parseInt(getAnnee(), -1) == -1) {
                _addError(statement.getTransaction(), getSession().getLabel("1080"));
                validationOK = false;
            }
            if (JadeStringUtil.parseInt(JANumberFormatter.deQuote(nbrAssures), -1) == -1) {
                _addError(statement.getTransaction(), getSession().getLabel("1090"));
                validationOK = false;
            }
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MVINAS", this._dbWriteNumeric(statement.getTransaction(), getNbrAssuresId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MVINAS",
                this._dbWriteNumeric(statement.getTransaction(), getNbrAssuresId(), "nbrAssuresId"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "affiliationId"));
        statement.writeField("MBIASS",
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), "assuranceId"));
        statement.writeField("MVNANN", this._dbWriteNumeric(statement.getTransaction(), getAnnee(), "annee"));
        statement.writeField("MVNNBR", this._dbWriteNumeric(statement.getTransaction(), getNbrAssures(), "nbAssures"));
    }

    /**
     * Methode utilisée par les API.
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public BManager find(Hashtable params) throws Exception {
        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                String value = (String) params.get(methodName);
                Method m = null;
                if ("changeManagerSize".equals(methodName)) {
                    manager.changeManagerSize(Integer.parseInt(value));

                } else {
                    m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                    if (m != null) {
                        m.invoke(manager, new Object[] { value });
                    }
                }
            }
        }

        manager.find();
        return manager;
    }

    /**
     * Rechercher l'affiliation du nombre d'Assuré en fonction de son ID.
     * 
     * @return l'affiliation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        if (_affiliation == null) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(getAffiliationId());
            try {
                _affiliation.retrieve();
                /*
                 * if (_affiliation.hasErrors()) _affiliation = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    public java.lang.String getAnnee() {
        return annee;
    }

    /**
     * Rechercher l'assurance du nombre d'Assuré en fonction de son ID.
     * 
     * @return l'assurance
     */
    public AFAssurance getAssurance() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAssuranceId())) {
            return null;
        }

        if ((_assurance == null) || (!assuranceId.equals(_assurance.getAssuranceId()))) {

            _assurance = new AFAssurance();
            _assurance.setSession(getSession());
            _assurance.setAssuranceId(getAssuranceId());
            try {
                _assurance.retrieve();
                /*
                 * if (_assurance.hasErrors()) _assurance = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _assurance = null;
            }
        }
        return _assurance;
    }

    public java.lang.String getAssuranceId() {
        return assuranceId;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFNombreAssuresManager();
    }

    public java.lang.String getNbrAssures() {
        /*
         * try { return JANumberFormatter.fmt(nbrAssures,true,false,true,2); } catch (Exception ex) { return nbrAssures;
         * }
         */
        return nbrAssures;

    }

    public java.lang.String getNbrAssuresId() {
        return nbrAssuresId;
    }

    /**
     * Rechercher le tiers du nombre d'Assuré en fonction affiliation.
     * 
     * @return le tiers
     */
    public TITiers getTiers() {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                getAffiliation();
                if (_affiliation == null) {
                    return null;
                }
            }
            _tiers = new TITiers();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());
            try {
                _tiers.retrieve();
                /*
                 * if (_tiers.getSession().hasErrors()) _tiers = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    /**
     * @return
     */
    public boolean isCallExternal() {
        return !noCallExternal;
    }

    public void setAffiliationId(java.lang.String string) {
        affiliationId = string;
    }

    public void setAnnee(java.lang.String string) {
        annee = string;
    }

    public void setAssuranceId(java.lang.String string) {
        assuranceId = string;
    }

    public void setNbrAssures(java.lang.String string) {
        nbrAssures = JANumberFormatter.deQuote(string);
    }

    public void setNbrAssuresId(java.lang.String string) {
        nbrAssuresId = string;
    }

    /**
     * @param b
     */
    public void setNoCallExternal(boolean b) {
        noCallExternal = b;
    }

}
