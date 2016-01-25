/*
 * Créé le 27 mai 05
 */
package globaz.apg.db.droits;

import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APEnfantAPG extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** la colonne dateDebutDroit */
    public static final String FIELDNAME_DATEDEBUTDROIT = "VEDDED";

    /** la colonne dateFinDroit */
    public static final String FIELDNAME_DATEFINDROIT = "VEDFID";

    /** la colonne id */
    public static final String FIELDNAME_IDENFANTAPG = "VEIENA";

    /** la colonne idSituationFamiliale */
    public static final String FIELDNAME_IDSITUATIONFAM = "VEISIF";

    /** la colonne idTiers */
    public static final String FIELDNAME_IDTIERS = "VEITIE";

    /** Le nom de la table */
    public static final String TABLE_NAME = "APENFAP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // private transient PRCiWrapper tiers = null; // transient pour ne pas
    // surcharger la
    // session http

    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private boolean deletePrestationsRequis = false;
    private String idEnfant = "";
    private String idSituationFamiliale = "";
    private String idTiers = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
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
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
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
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
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
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdEnfant(_incCounter(transaction, "0"));
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
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idEnfant = statement.dbReadNumeric(FIELDNAME_IDENFANTAPG);
        idSituationFamiliale = statement.dbReadNumeric(FIELDNAME_IDSITUATIONFAM);
        idTiers = statement.dbReadNumeric(FIELDNAME_IDTIERS);
        dateDebutDroit = statement.dbReadDateAMJ(FIELDNAME_DATEDEBUTDROIT);
        dateFinDroit = statement.dbReadDateAMJ(FIELDNAME_DATEFINDROIT);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();
        BSession session = getSession();
        _propertyMandatory(transaction, getDateDebutDroit(), session.getLabel("DATE_DEBUT_OBLIGATOIRE"));
        _checkDate(transaction, getDateDebutDroit(), session.getLabel("DATE_DEBUT_INCORRECTE"));
        _checkDate(transaction, getDateFinDroit(), session.getLabel("DATE_FIN_INCORRECTE"));

        if (!JAUtil.isDateEmpty(getDateFinDroit())
                && !BSessionUtil.compareDateFirstLowerOrEqual(session, getDateDebutDroit(), getDateFinDroit())) {
            _addError(transaction, session.getLabel("DATE_DEBUT_DROIT_SUP_DATE_FIN_DROIT"));
        }
    }

    // /** * getter pour l'attribut tiers
    //
    // * @return la valeur courante de l'attribut tiers */ public PRCiWrapper
    // getTiers() { return tiers;
    // }

    // /** * Charge le tiers associé à cet enfant.
    //
    // * <p>Le tiers est automatiquement rechargé si (et seulement si) le champ
    // idTiers de ce bean est modifiée.</p>
    //
    // * @return une instance de PRCiWrapper ou null s'il n'existe pas de tiers
    // avec cet identifiant.
    //
    // * @throws Exception si la recherche du tiers échoue. */ public
    // PRCiWrapper loadTiers() throws Exception
    // { if ((tiers == null) ||
    // !idTiers.equals(tiers.getProperty(PRCiWrapper.PROPERTY_ID_TIERS))) {
    // tiers = PRTiersHelper.getTiersAdresseParId(getSession(), idTiers); }
    //
    // return tiers; }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_IDENFANTAPG, _dbWriteNumeric(statement.getTransaction(), idEnfant, "idEnfant"));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_IDENFANTAPG, _dbWriteNumeric(statement.getTransaction(), idEnfant, "idEnfant"));
        statement.writeField(FIELDNAME_IDSITUATIONFAM,
                _dbWriteNumeric(statement.getTransaction(), idSituationFamiliale, "idSituationFamiliale"));
        statement.writeField(FIELDNAME_IDTIERS, _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(FIELDNAME_DATEDEBUTDROIT,
                _dbWriteDateAMJ(statement.getTransaction(), dateDebutDroit, "dateDebutDroit"));
        statement.writeField(FIELDNAME_DATEFINDROIT,
                _dbWriteDateAMJ(statement.getTransaction(), dateFinDroit, "dateFinDroit"));
    }

    private void deletePrestations(BTransaction transaction) throws Exception {
        // quand un droit est mis à jour, les prestations qui ont pu être
        // calculée pour ce droit n'ont plus aucun sens,
        // on les efface.
        APDroitAPGManager droitAPGManager = new APDroitAPGManager();
        droitAPGManager.setSession(getSession());
        droitAPGManager.setForIdSituationFamiliale(idSituationFamiliale);
        droitAPGManager.find(transaction);

        if (droitAPGManager.size() != 0) {
            String idDroit = null;
            APDroitAPG droitAPG = (APDroitAPG) droitAPGManager.getEntity(0);
            idDroit = droitAPG.getIdDroit();

            if (idDroit != null) {
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
    }

    /**
     * DOCUMENT ME!
     * 
     * @param actionType
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int actionType) {
        APEnfantAPG clone = new APEnfantAPG();
        clone.setDateDebutDroit(getDateDebutDroit());
        clone.setDateFinDroit(getDateFinDroit());
        clone.setIdTiers(getIdTiers());

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);

        return clone;
    }

    /**
     * getter pour l'attribut date debut droit
     * 
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * getter pour l'attribut date fin droit
     * 
     * @return la valeur courante de l'attribut date fin droit
     */
    public String getDateFinDroit() {
        return dateFinDroit;
    }

    /**
     * getter pour l'attribut id enfant
     * 
     * @return la valeur courante de l'attribut id enfant
     */
    public String getIdEnfant() {
        return idEnfant;
    }

    /**
     * getter pour l'attribut id situation familiale
     * 
     * @return la valeur courante de l'attribut id situation familiale
     */
    public String getIdSituationFamiliale() {
        return idSituationFamiliale;
    }

    /**
     * getter pour l'attribut id tiers
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut unique primary key
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdEnfant();
    }

    /**
     * setter pour l'attribut date debut droit
     * 
     * @param dateDebutDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutDroit(String dateDebutDroit) {
        if (!dateDebutDroit.equals(this.dateDebutDroit)) {
            deletePrestationsRequis = true;
        }

        this.dateDebutDroit = dateDebutDroit;
    }

    /**
     * setter pour l'attribut date fin droit
     * 
     * @param dateFinDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinDroit(String dateFinDroit) {
        if (!dateFinDroit.equals(this.dateFinDroit)) {
            deletePrestationsRequis = true;
        }

        this.dateFinDroit = dateFinDroit;
    }

    /**
     * setter pour l'attribut id enfant
     * 
     * @param idEnfant
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEnfant(String idEnfant) {
        this.idEnfant = idEnfant;
    }

    /**
     * setter pour l'attribut id situation familiale
     * 
     * @param idSituationFamiliale
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationFamiliale(String idSituationFamiliale) {
        this.idSituationFamiliale = idSituationFamiliale;
    }

    /**
     * setter pour l'attribut id tiers
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String s) {
        idTiers = s;

        if (s == null && idTiers == null) {
            return;
        }

        if ((s == null && idTiers != null) || (idTiers == null && s != null) || (!s.equals(idTiers))) {
            deletePrestationsRequis = true;
        }
    }

    /**
     * setter pour l'attribut unique primary key
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdEnfant(pk);
    }
}
