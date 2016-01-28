package globaz.naos.db.lienAffiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.helper.IAFLienAffiliationHelper;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TITiers;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * La classe définissant l'entité LienAffiliation.
 * 
 * @author sau
 */
public class AFLienAffiliation extends BEntity implements Serializable {

    private static final long serialVersionUID = -8696097532273013155L;
    private AFAffiliation _affiliation = null;
    private AFAffiliation _lienAffiliation = null;
    private TITiers _lienTiers = null;
    private TITiers _tiers = null;
    private java.lang.String aff_AffiliationId = new String();
    private java.lang.String affiliationId = new String();
    private java.lang.String dateDebut = new String();
    private java.lang.String dateFin = new String();
    private java.lang.String lienAffiliationId = new String();
    private java.lang.String typeLien = new String();

    public AFLienAffiliation() {
        super();
        setMethodsToLoad(IAFLienAffiliationHelper.METHODS_TO_LOAD);
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setLienAffiliationId(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return "AFLIENP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        lienAffiliationId = statement.dbReadNumeric("MWILIE");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        aff_AffiliationId = statement.dbReadNumeric("AFA_MAIAFF");
        typeLien = statement.dbReadNumeric("MWTLIE");
        dateDebut = statement.dbReadDateAMJ("MWDDEB");
        dateFin = statement.dbReadDateAMJ("MWDFIN");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        validationOK &= _propertyMandatory(statement.getTransaction(), getDateDebut(), getSession().getLabel("20"));
        validationOK &= _propertyMandatory(statement.getTransaction(), this.getTypeLien(), getSession()
                .getLabel("1110"));
        validationOK &= _propertyMandatory(statement.getTransaction(), this.getAff_AffiliationId(), getSession()
                .getLabel("1100"));

        // Test validité des dates
        validationOK &= _checkRealDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("160"));

        if (validationOK) {

            // *******************************************************************
            // Date Debut
            // *******************************************************************

            // Contrôle que la date de début > 01.01.1948
            String dateLimiteInf = "01.01.1900";

            // Test Date Debut >= Date Limite Inferieur
            if (BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), dateLimiteInf)) {
                _addError(statement.getTransaction(), getSession().getLabel("60"));
                validationOK = false;
            }

            // *******************************************************************
            // Date Fin
            // *******************************************************************
            if (!JadeStringUtil.isBlankOrZero(getDateFin())) {
                // Validité Date
                validationOK &= _checkRealDate(statement.getTransaction(), getDateFin(), getSession().getLabel("180"));

                // Date Fin > Date Debut
                if (!BSessionUtil.compareDateFirstLower(getSession(), getDateDebut(), getDateFin())) {
                    _addError(statement.getTransaction(), getSession().getLabel("90"));
                    validationOK = false;
                }
            }

            // Un affiliation ne peut pas se lier sur-elle même
            if (this.getAffiliationId().equals(this.getAff_AffiliationId())) {
                _addError(statement.getTransaction(), getSession().getLabel("1990"));
                validationOK = false;
            }

            // ******************************************************************
            // Type de liens
            // ******************************************************************

            // Plausibilités sur les liens de type "déclaré"
            if (CodeSystem.TYPE_LIEN_PERSONNEL_DECLARE.equals(this.getTypeLien())) {

                // Interdit de créer un lien "Déclaré par" sur une affiliation
                // de type indépendant ou NA
                if (this.getAffiliation().getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP)
                        || this.getAffiliation().getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)) {
                    _addError(statement.getTransaction(), getSession().getLabel("1980"));
                    validationOK = false;
                }
            }

            // Plausibilités sur les liens de type "Taxé sous"
            if (CodeSystem.TYPE_LIEN_TAXE_SOUS.equals(this.getTypeLien())) {

                // Interdit de créer un lien "Taxé sous" ailleur que sur une
                // affiliation de type indépendant
                // ou employeur avec une personalité juridique RI
                if (!this.getAffiliation().getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP)
                        && !this.getAffiliation().getPersonnaliteJuridique()
                                .equals(CodeSystem.PERS_JURIDIQUE_RAISON_INDIVIDUELLE)) {
                    _addError(statement.getTransaction(), getSession().getLabel("2000"));
                    validationOK = false;
                }
            }

        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MWILIE", this._dbWriteNumeric(statement.getTransaction(), getLienAffiliationId(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MWILIE",
                this._dbWriteNumeric(statement.getTransaction(), getLienAffiliationId(), "lienAffiliationId"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), this.getAffiliationId(), "affiliationId"));
        statement.writeField("AFA_MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), this.getAff_AffiliationId(), "aff_AffiliationId"));
        statement
                .writeField("MWTLIE", this._dbWriteNumeric(statement.getTransaction(), this.getTypeLien(), "typeLien"));
        statement.writeField("MWDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField("MWDFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
    }

    /**
     * Methode utilisée par les API.
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public BManager find(Hashtable<?, ?> params) throws Exception {

        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration<?> methods = params.keys();
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

    public java.lang.String getAff_AffiliationId() {
        return aff_AffiliationId;
    }

    public java.lang.String getAff_AffiliationId(String idAffiliation) {
        if (aff_AffiliationId.equals(idAffiliation)) {
            return affiliationId;
        } else {
            return aff_AffiliationId;
        }
    }

    /**
     * Rechercher de l'affilation du lien d'affiliation en fonction de son ID.
     * 
     * @return l'affilation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(this.getAffiliationId())) {
            return null;
        }

        if (_affiliation == null) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(this.getAffiliationId());
            try {
                _affiliation.retrieve();

            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    public AFAffiliation getAffiliation(String idAffiliation) {
        if (aff_AffiliationId.equals(idAffiliation)) {
            return this.getLienAffiliation();
        } else {
            return this.getAffiliation();
        }
    }

    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    public java.lang.String getAffiliationId(String idAffiliation) {
        if (aff_AffiliationId.equals(idAffiliation)) {
            return aff_AffiliationId;
        } else {
            return affiliationId;
        }
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    /**
     * Rechercher de l'affilation liée du lien d'affiliation en fonction de son ID.
     * 
     * @return l'affilation liée
     */
    public AFAffiliation getLienAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(this.getAff_AffiliationId())) {
            return null;
        }

        if (_lienAffiliation == null) {

            _lienAffiliation = new AFAffiliation();
            _lienAffiliation.setSession(getSession());
            _lienAffiliation.setAffiliationId(this.getAff_AffiliationId());
            try {
                _lienAffiliation.retrieve();
                /*
                 * if (_lienAffiliation.hasErrors()) _lienAffiliation = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _lienAffiliation = null;
            }
        }
        return _lienAffiliation;
    }

    // *******************************************************
    // Getter pour les cherches de relations inverses: enfant<-->parent
    // pour l'id affilié donné retourne le fils ou l'enfant
    // si idAffiliation est vide, on prend la valeur retournée par le getter
    // standard
    // *******************************************************

    public AFAffiliation getLienAffiliation(String idAffiliation) {
        if (aff_AffiliationId.equals(idAffiliation)) {
            return this.getAffiliation();
        } else {
            return this.getLienAffiliation();
        }
    }

    public java.lang.String getLienAffiliationId() {
        return lienAffiliationId;
    }

    /**
     * Rechercher le tiers de l'affiliation liée en fonction de l'affiliation liée
     * 
     * @return le tiers de l'affiliation liée
     */
    public TITiers getLienTiers() {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_lienAffiliation == null) {
                this.getLienAffiliation();
                if (_lienAffiliation == null) {
                    return null;
                }
            }
            _lienTiers = new TITiers();
            _lienTiers.setSession(getSession());
            _lienTiers.setIdTiers(_lienAffiliation.getIdTiers());
            try {
                _lienTiers.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _lienTiers = null;
            }
        }
        return _lienTiers;
    }

    public TITiers getLienTiers(String idAffiliation) {
        if (aff_AffiliationId.equals(idAffiliation)) {
            return this.getTiers();
        } else {
            return this.getLienTiers();
        }
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFLienAffiliationManager();
    }

    /**
     * Rechercher le tiers de l'affiliation en fonction de l'affiliation
     * 
     * @return le tiers
     */
    public TITiers getTiers() {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                this.getAffiliation();
                if (_affiliation == null) {
                    return null;
                }
            }
            _tiers = new TITiers();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());
            try {
                _tiers.retrieve();

            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    public TITiers getTiers(String idAffiliation) {
        if (aff_AffiliationId.equals(idAffiliation)) {
            return this.getLienTiers();
        } else {
            return this.getTiers();
        }
    }

    public java.lang.String getTypeLien() {
        return typeLien;
    }

    /**
     * Renvoie le type de lien entre parent-enfant et enfant-parent
     */
    public java.lang.String getTypeLien(String idAffiliation) {
        if (aff_AffiliationId.equals(idAffiliation)) {
            // enfant-parent --> relation inverse
            if (typeLien.equals(CodeSystem.TYPE_LIEN_SUCCURSALE)) {
                return CodeSystem.TYPE_LIEN_SUCCURSALE_INV;
            } else if (typeLien.equals(CodeSystem.TYPE_LIEN_PERSONNEL_DECLARE)) {
                return CodeSystem.TYPE_LIEN_PERSONNEL_DECLARE_INV;
            } else if (typeLien.equals(CodeSystem.TYPE_LIEN_PERSONNEL_MAISON)) {
                return CodeSystem.TYPE_LIEN_PERSONNEL_MAISON_INV;
            } else if (typeLien.equals(CodeSystem.TYPE_LIEN_REGIE)) {
                return CodeSystem.TYPE_LIEN_REGIE_INV;
            } else if (typeLien.equals(CodeSystem.TYPE_LIEN_TAXE_SOUS)) {
                return CodeSystem.TYPE_LIEN_TAXE_SOUS_INV;
            } else if (typeLien.equals(CodeSystem.TYPE_LIEN_DOMICILE_CHEZ)) {
                return CodeSystem.TYPE_LIEN_DOMICILE_CHEZ_INV;
            } else {
                // si une traduction vers le lien inverse n'a pas été faite
                return typeLien;
            }
        } else {
            // parent-enfant --> relations prédéfinies
            return typeLien;
        }
    }

    public void setAff_AffiliationId(java.lang.String string) {
        aff_AffiliationId = string;
    }

    public void setAffiliationId(java.lang.String string) {
        affiliationId = string;
    }

    public void setDateDebut(java.lang.String string) {
        dateDebut = string;
    }

    public void setDateFin(java.lang.String string) {
        dateFin = string;
    }

    public void setLienAffiliationId(java.lang.String string) {
        lienAffiliationId = string;
    }

    public void setTypeLien(java.lang.String string) {
        typeLien = string;
    }
}
