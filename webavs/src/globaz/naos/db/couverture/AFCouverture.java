/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.couverture;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.helper.IAFCouvertureHelper;
import globaz.naos.db.assurance.AFAssurance;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * La classe définissant l'entité Couverture.
 * 
 * @author sau
 */
public class AFCouverture extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAssurance _assurance = null;
    private java.lang.String assuranceId = new String();
    // DB Table AFCOUVP
    // Primary Key
    private java.lang.String couvertureId = new String();
    // Fields
    private java.lang.String dateDebut = new String();
    private java.lang.String dateFin = new String();

    // Foreign Key
    private java.lang.String planCaisseId = new String();

    /**
     * Constructeur d'AFCouverture.
     */
    public AFCouverture() {
        super();
        setMethodsToLoad(IAFCouvertureHelper.METHODS_TO_LOAD);
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {

        setCouvertureId(this._incCounter(transaction, "0"));
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFCOUVP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        couvertureId = statement.dbReadNumeric("MTICOU");
        planCaisseId = statement.dbReadNumeric("MSIPLC");
        assuranceId = statement.dbReadNumeric("MBIASS");
        dateDebut = statement.dbReadDateAMJ("MTDDEB");
        dateFin = statement.dbReadDateAMJ("MTDFIN");
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
        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceId(), getSession().getLabel("520"));
        validationOK &= _propertyMandatory(statement.getTransaction(), getDateDebut(), getSession().getLabel("20"));

        // Test validité des dates
        validationOK &= _checkRealDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("160"));

        if (!JadeStringUtil.isIntegerEmpty(getDateFin())) {
            validationOK &= _checkRealDate(statement.getTransaction(), getDateFin(), getSession().getLabel("180"));

            if (validationOK) {
                // Test date de Début < date de Fin
                try {
                    // String dateDebutP1 =
                    // getSession().getApplication().getCalendar().addDays(dateDebut,
                    // 1);
                    if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getDateDebut(), getDateFin())) {
                        _addError(statement.getTransaction(), getSession().getLabel("550"));
                        validationOK = false;
                    }
                } catch (Exception e) {
                    _addError(statement.getTransaction(), getSession().getLabel("200"));
                    validationOK = false;
                }
            }
        }

        // Controle si il n'y a pas déjà une assurance du meme type pour ce plan
        // de caisse
        AFCouvertureListViewBean couv = new AFCouvertureListViewBean();
        couv.setForPlanCaisseId(getPlanCaisseId());
        couv.setForAssuranceId(getAssuranceId());
        couv.setSession(getSession());
        couv.find(statement.getTransaction());

        for (int i = 0; i < couv.size(); i++) {
            AFCouverture couverture = (AFCouverture) couv.getEntity(i);
            // Ne pas tester une Couverture avec elle meme
            if (!couverture.getCouvertureId().equalsIgnoreCase(getCouvertureId())) {
                // Si la couverture n'a pas de date de fin ERREUR
                if (JadeStringUtil.isBlankOrZero(couverture.getDateFin())) {
                    // Si la couverture n'a pas de date de fin ERREUR
                    // if (couverture.getAssuranceId().equalsIgnoreCase(this.getAssuranceId())) {
                    _addError(statement.getTransaction(), getSession().getLabel("1140"));
                    validationOK = false;
                    // }
                } else {
                    // Si la couverture a une date de fin qui est plus grande ou égale à
                    // la date de début de la nouvelle ERREUR
                    if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), couverture.getDateFin(),
                            getDateDebut())) {
                        _addError(statement.getTransaction(), getSession().getLabel("1140"));
                        validationOK = false;
                    }
                }
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
        statement.writeKey("MTICOU", this._dbWriteNumeric(statement.getTransaction(), getCouvertureId(), ""));

    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MTICOU",
                this._dbWriteNumeric(statement.getTransaction(), getCouvertureId(), "couvertureId"));
        statement.writeField("MSIPLC",
                this._dbWriteNumeric(statement.getTransaction(), getPlanCaisseId(), "planCaisseId"));
        statement.writeField("MBIASS",
                this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), "assuranceId"));
        statement.writeField("MTDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField("MTDFIN", this._dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
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
                Method m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }
        manager.find();
        return manager;
    }

    /**
     * Rechercher l'assurance pour la couverture en fonction de son ID.
     * 
     * @return l'assurance
     */
    public AFAssurance getAssurance() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAssuranceId())) {
            return null;
        }

        if (_assurance == null) {

            _assurance = new AFAssurance();
            _assurance.setSession(getSession());
            _assurance.setAssuranceId(getAssuranceId());
            try {
                _assurance.retrieve();
                /*
                 * if (_assurance.getSession().hasErrors()) _assurance = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _assurance = null;
            }
        }
        return _assurance;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getAssuranceId() {
        return assuranceId;
    }

    public java.lang.String getCouvertureId() {
        return couvertureId;
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFCouvertureManager();
    }

    public java.lang.String getPlanCaisseId() {
        return planCaisseId;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAssuranceId(java.lang.String string) {
        assuranceId = string;
    }

    public void setCouvertureId(java.lang.String string) {
        couvertureId = string;
    }

    public void setDateDebut(java.lang.String string) {
        dateDebut = string;
    }

    public void setDateFin(java.lang.String string) {
        dateFin = string;
    }

    public void setPlanCaisseId(java.lang.String string) {
        planCaisseId = string;
    }
}
