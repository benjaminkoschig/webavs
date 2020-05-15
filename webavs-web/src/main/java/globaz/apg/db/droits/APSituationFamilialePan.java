/*
 * Créé le 13 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.*;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * DOCUMENT ME!
 * 
 * @author vre
 */
public class APSituationFamilialePan extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int ALTERNATE_KEY_ID_DROIT_TYPE = 1;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /** DOCUMENT ME! */
    public static final String FIELDNAME_DATE_NAISSANCE = "VQDDNA";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_IDDROIT = "VQIDRM";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_IDSITFAM = "VQISIF";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_IS_ADOPTION = "VQBADP";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NO_AVS = "VQLAVS";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_NOM = "VQLNOM";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_PRENOM = "VQLPRE";

    /** DOCUMENT ME! */
    public static final String FIELDNAME_TYPE = "VQTTYP";

    private static final String PROPNAME_DATE_NAISSANCE = "dateNaissance";
    private static final String PROPNAME_IDDROIT = "idDroit";
    private static final String PROPNAME_IDSITFAM = "idSitFam";
    private static final String PROPNAME_IS_ADOPTION = "isAdoption";
    private static final String PROPNAME_NO_AVS = "noAVS";
    private static final String PROPNAME_NOM = "nom";
    private static final String PROPNAME_PRENOM = "prenom";
    private static final String PROPNAME_TYPE = "type";

    /** DOCUMENT ME! */
    public static final String TABLE_NAME = "APSIFMP";

    /** DOCUMENT ME! */
    protected String dateNaissance = "";

    /**
     */
    protected boolean deletePrestationsRequis = false;

    private transient APDroitPandemie droit = null; // transient pour ne pas
    // surcharger la session
    // http

    /** DOCUMENT ME! */
    protected String idDroit = "";

    /** DOCUMENT ME! */
    protected String idSitFam = "";

    /** DOCUMENT ME! */
    protected Boolean isAdoption = Boolean.FALSE;

    /** DOCUMENT ME! */
    protected String noAVS = "";

    /** DOCUMENT ME! */
    protected String nom = "";

    /** DOCUMENT ME! */
    protected String prenom = "";

    /** DOCUMENT ME! */
    protected String type = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APSituationFamilialePan.
     */
    public APSituationFamilialePan() {
    }

    /**
     * Crée une nouvelle instance de la classe APSituationFamilialePan.
     *
     * @param type
     *            DOCUMENT ME!
     */
    protected APSituationFamilialePan(String type) {
        this.type = type;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     *
     * @see BEntity#_afterAdd(BTransaction)
     *
     * @param transaction
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        super._afterAdd(transaction);
        deletePrestations(transaction);
        deletePrestationsRequis = false;
    }

    /**
     * (non-Javadoc)
     *
     * @see BEntity#_afterDelete(BTransaction)
     *
     * @param transaction
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);
        deletePrestations(transaction);
    }

    /**
     * (non-Javadoc)
     *
     * @see BEntity#_afterUpdate(BTransaction)
     *
     * @param transaction
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        super._afterUpdate(transaction);

        if (deletePrestationsRequis) {
            deletePrestations(transaction);
        }

        deletePrestationsRequis = false;
    }

    /**
     * @see BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param transaction
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdSitFam(_incCounter(transaction, "0"));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * DOCUMENT ME!
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSitFam = statement.dbReadNumeric(FIELDNAME_IDSITFAM);
        type = statement.dbReadNumeric(FIELDNAME_TYPE);
        idDroit = statement.dbReadNumeric(FIELDNAME_IDDROIT);
        nom = statement.dbReadString(FIELDNAME_NOM);
        prenom = statement.dbReadString(FIELDNAME_PRENOM);
        dateNaissance = statement.dbReadDateAMJ(FIELDNAME_DATE_NAISSANCE);
        noAVS = NSUtil.formatAVSUnknown(statement.dbReadNumeric(FIELDNAME_NO_AVS));
        isAdoption = statement.dbReadBoolean(FIELDNAME_IS_ADOPTION);
    }

    /**
     * @see BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case ALTERNATE_KEY_ID_DROIT_TYPE:
                statement.writeKey(FIELDNAME_IDDROIT,
                        _dbWriteNumeric(statement.getTransaction(), getIdDroit(), "idDroit"));
                statement.writeKey(FIELDNAME_TYPE, _dbWriteNumeric(statement.getTransaction(), getType(), "type"));
                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_IDSITFAM,
                _dbWriteNumeric(statement.getTransaction(), idSitFam, PROPNAME_IDSITFAM));
    }

    /**
     * DOCUMENT ME!
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_IDSITFAM,
                _dbWriteNumeric(statement.getTransaction(), idSitFam, PROPNAME_IDSITFAM));
        statement.writeField(FIELDNAME_TYPE, _dbWriteNumeric(statement.getTransaction(), type, PROPNAME_TYPE));
        statement.writeField(FIELDNAME_IDDROIT,
                _dbWriteNumeric(statement.getTransaction(), idDroit, PROPNAME_IDDROIT));
        statement.writeField(FIELDNAME_NOM, _dbWriteString(statement.getTransaction(), nom, PROPNAME_NOM));
        statement.writeField(FIELDNAME_PRENOM, _dbWriteString(statement.getTransaction(), prenom, PROPNAME_PRENOM));
        statement.writeField(FIELDNAME_DATE_NAISSANCE,
                _dbWriteDateAMJ(statement.getTransaction(), dateNaissance, PROPNAME_DATE_NAISSANCE));
        statement.writeField(FIELDNAME_NO_AVS,
                _dbWriteString(statement.getTransaction(), JAStringFormatter.deformatAvs(noAVS), PROPNAME_NO_AVS));
        statement.writeField(
                FIELDNAME_IS_ADOPTION,
                _dbWriteBoolean(statement.getTransaction(), isAdoption, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        PROPNAME_IS_ADOPTION));
    }

    private void deletePrestations(BTransaction transaction) throws Exception {
        // quand un droit est mis à jour, les prestations qui ont pu être
        // calculée pour ce droit n'ont plus aucun sens,
        // on les efface.
        if(!JadeStringUtil.isBlankOrZero(idDroit)) {
            APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(getSession());
            prestationManager.setForIdDroit(idDroit);
            prestationManager.find(transaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < prestationManager.size(); i++) {
                APPrestation prestation = (APPrestation) prestationManager.getEntity(i);
                prestation.delete(transaction);
            }
        }
    }

    /**
     * @param actionType
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int actionType) {
        APSituationFamilialePan clone = new APSituationFamilialePan();
        clone.setDateNaissance(getDateNaissance());
        clone.setNoAVS(getNoAVS());
        clone.setNom(getNom());
        clone.setPrenom(getPrenom());
        clone.setType(getType());

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);

        return clone;
    }

    /**
     * getter pour l'attribut date naissance
     * 
     * @return la valeur courante de l'attribut date naissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * getter pour l'attribut Id Droit
     * 
     * @return la valeur courante de l'attribut Id Droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * getter pour l'attribut Id Sit Fam
     * 
     * @return la valeur courante de l'attribut Id Sit Fam
     */
    public String getIdSitFam() {
        return idSitFam;
    }

    /**
     * @return
     */
    public Boolean getIsAdoption() {
        return isAdoption;
    }

    /**
     * getter pour l'attribut no AVS
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter pour l'attribut prenom
     * 
     * @return la valeur courante de l'attribut prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * getter pour l'attribut Type
     * 
     * @return la valeur courante de l'attribut Type
     */
    public String getType() {
        return type;
    }

    /**
     * getter pour l'attribut unique primary key
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdSitFam();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public APDroitPandemie loadDroit() throws Exception {
        // si le droit est null, instancier
        if (droit == null) {
            droit = new APDroitPandemie();
        }

        // on s'assure que la session est la bonne (pour les cas où on
        // chargerait le tiers...)
        droit.setSession(getSession());

        // si la demande est différente, charger la demande
        if (!idDroit.equals(droit.getIdDroit())) {
            droit.setIdDemande(idDroit);
            droit.retrieve();
        }

        return droit;
    }

    /**
     * setter pour l'attribut date naissance
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateNaissance(String string) {
        if (!string.equals(dateNaissance)) {
            deletePrestationsRequis = true;
        }

        dateNaissance = string;
    }

    /**
     * setter pour l'attribut Id Droit
     * 
     * @param idDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * setter pour l'attribut Id Sit Fam
     * 
     * @param isSitFam
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSitFam(String isSitFam) {
        idSitFam = isSitFam;
    }

    /**
     * @param boolean1
     */
    public void setIsAdoption(Boolean boolean1) {
        isAdoption = boolean1;
    }

    /**
     * setter pour l'attribut no AVS
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String string) {
        noAVS = string;
    }

    /**
     * setter pour l'attribut nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * setter pour l'attribut prenom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenom(String string) {
        prenom = string;
    }

    /**
     * setter pour l'attribut Type
     * 
     * @param type
     *            une nouvelle valeur pour cet attribut
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * setter pour l'attribut unique primary key
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdSitFam(pk);
    }

}
