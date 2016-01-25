/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.lots;

import globaz.globall.api.BIEntity;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.ij.application.IJApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementDataManager;
import globaz.pyxis.util.TIAdressePmtResolver;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJFactureACompenser extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_DOMAINEAPPLICATION = "XQTDOA";

    /**
     */
    public static final String FIELDNAME_IDADRESSEPAIEMENT = "XQIADP";

    /**
     */
    public static final String FIELDNAME_IDAFFILIE = "XQIAFF";

    /**
     */
    public static final String FIELDNAME_IDCOMPENSATIONPARENTE = "XQICOM";

    /**
     */
    public static final String FIELDNAME_IDFACTUREACOMPENSER = "XQIFAC";

    /**
     */
    public static final String FIELDNAME_IDFACTURECOMPTA = "XQIFCO";

    /**
     */
    public static final String FIELDNAME_IDTIERS = "XQITIE";

    /**
     */
    public static final String FIELDNAME_ISCOMPENSE = "XQBCOM";

    /**
     */
    public static final String FIELDNAME_MONTANT = "XQMMON";

    public static final String FIELDNAME_NOFACTURE = "XQNNFA";

    /**
     */
    public static final String TABLE_NAME = "IJFACTAC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient TIAdressePaiementData adressePaiement = null;
    /**
     */
    protected transient IPRAffilie affilie = null;
    private transient IJCompensation compensation = null;
    private String domaineApplication = "";
    private String idAdressePaiement = "";
    private String idAffilie = "";
    private String idCompensationParente = "";
    private String idFactureACompenser = "";
    private String idFactureCompta = "";
    private String idTiers = "";

    private Boolean isCompense = Boolean.FALSE;

    private String montant = "";

    private String noFacture = "";
    /**
     */
    protected transient PRTiersWrapper tiers = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdFactureACompenser(this._incCounter(transaction, "0"));
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return IJFactureACompenser.TABLE_NAME;
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
        idFactureACompenser = statement.dbReadNumeric(IJFactureACompenser.FIELDNAME_IDFACTUREACOMPENSER);
        montant = statement.dbReadNumeric(IJFactureACompenser.FIELDNAME_MONTANT, 2);
        idTiers = statement.dbReadNumeric(IJFactureACompenser.FIELDNAME_IDTIERS);
        idAffilie = statement.dbReadNumeric(IJFactureACompenser.FIELDNAME_IDAFFILIE);
        idAdressePaiement = statement.dbReadNumeric(IJFactureACompenser.FIELDNAME_IDADRESSEPAIEMENT);
        domaineApplication = statement.dbReadNumeric(IJFactureACompenser.FIELDNAME_DOMAINEAPPLICATION);
        idFactureCompta = statement.dbReadNumeric(IJFactureACompenser.FIELDNAME_IDFACTURECOMPTA);
        idCompensationParente = statement.dbReadNumeric(IJFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE);
        isCompense = statement.dbReadBoolean(IJFactureACompenser.FIELDNAME_ISCOMPENSE);
        noFacture = statement.dbReadNumeric(IJFactureACompenser.FIELDNAME_NOFACTURE);
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

        // pas de compensations sur factures futures pour les non-affilies
        if (JadeStringUtil.isIntegerEmpty(getNoFacture()) && JadeStringUtil.isIntegerEmpty(idAffilie)) {
            _addError(statement.getTransaction(), getSession().getLabel("COMP_FACT_FUTURE_POUR_NON_AFFILIES"));
        }
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(IJFactureACompenser.FIELDNAME_IDFACTUREACOMPENSER,
                this._dbWriteNumeric(statement.getTransaction(), idFactureACompenser, "idFactureACompenser"));
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
        statement.writeField(IJFactureACompenser.FIELDNAME_IDFACTUREACOMPENSER,
                this._dbWriteNumeric(statement.getTransaction(), idFactureACompenser, "idFactureACompenser"));
        statement.writeField(IJFactureACompenser.FIELDNAME_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), montant, "montant"));
        statement.writeField(IJFactureACompenser.FIELDNAME_IDTIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(IJFactureACompenser.FIELDNAME_IDAFFILIE,
                this._dbWriteNumeric(statement.getTransaction(), idAffilie, "idAffilie"));
        statement.writeField(IJFactureACompenser.FIELDNAME_IDADRESSEPAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idAdressePaiement, "idAdressePaiement"));
        statement.writeField(IJFactureACompenser.FIELDNAME_DOMAINEAPPLICATION,
                this._dbWriteNumeric(statement.getTransaction(), domaineApplication, "domaineApplication"));
        statement.writeField(IJFactureACompenser.FIELDNAME_IDFACTURECOMPTA,
                this._dbWriteNumeric(statement.getTransaction(), idFactureCompta, "idFactureCompta"));
        statement.writeField(IJFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE,
                this._dbWriteNumeric(statement.getTransaction(), idCompensationParente, "idCompensationParente"));
        statement.writeField(IJFactureACompenser.FIELDNAME_ISCOMPENSE, this._dbWriteBoolean(statement.getTransaction(),
                isCompense, globaz.globall.db.BConstants.DB_TYPE_BOOLEAN_CHAR, "isCompense"));
        statement.writeField(IJFactureACompenser.FIELDNAME_NOFACTURE,
                this._dbWriteNumeric(statement.getTransaction(), noFacture, "noFacture"));
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @deprecated Utilisée ??
     */
    @Deprecated
    public void chercherAdressePaiement(BTransaction transaction) throws Exception {

        setAdressePaiement(PRTiersHelper.getAdressePaiementData(getSession(), transaction, idTiers,
                IJApplication.CS_DOMAINE_ADRESSE_IJAI, getIdAffilie(), JACalendar.todayJJsMMsAAAA()));

    }

    /**
     * getter pour l'attribut domaine application
     * 
     * @return la valeur courante de l'attribut domaine application
     */
    public String getDomaineApplication() {
        return domaineApplication;
    }

    /**
     * getter pour l'attribut id adresse paiement
     * 
     * @return la valeur courante de l'attribut id adresse paiement
     */
    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    /**
     * getter pour l'attribut id affilie
     * 
     * @return la valeur courante de l'attribut id affilie
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * getter pour l'attribut id compensation
     * 
     * @return la valeur courante de l'attribut id compensation
     */
    public String getIdCompensationParente() {
        return idCompensationParente;
    }

    /**
     * getter pour l'attribut id facture ACompenser
     * 
     * @return la valeur courante de l'attribut id facture ACompenser
     */
    public String getIdFactureACompenser() {
        return idFactureACompenser;
    }

    /**
     * getter pour l'attribut id facture compta
     * 
     * @return la valeur courante de l'attribut id facture compta
     */
    public String getIdFactureCompta() {
        return idFactureCompta;
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
     * getter pour l'attribut is compense
     * 
     * @return la valeur courante de l'attribut is compense
     */
    public Boolean getIsCompense() {
        return isCompense;
    }

    /**
     * getter pour l'attribut montant
     * 
     * @return la valeur courante de l'attribut montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * getter pour l'attribut no facture
     * 
     * @return la valeur courante de l'attribut no facture
     */
    public String getNoFacture() {
        return noFacture;
    }

    /**
     * 
     * @return
     */
    public String getNumAffilie() {
        String numAffilie = "";

        if (!JadeStringUtil.isIntegerEmpty(getIdCompensationParente())) {
            try {
                IJCompensation compensation = new IJCompensation();
                compensation.setSession(getSession());
                compensation.setIdCompensation(getIdCompensationParente());
                compensation.retrieve();

                if (!JadeStringUtil.isIntegerEmpty(compensation.getIdAffilie())) {
                    IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getSession()
                            .newTransaction(), compensation.getIdAffilie(), compensation.getIdTiers());
                    numAffilie = affilie.getNumAffilie();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return numAffilie;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public TIAdressePaiementData loadAdressePaiement() throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getIdAdressePaiement())
                && ((adressePaiement == null) || !adressePaiement.getIdAdressePaiement().equals(getIdAdressePaiement()) || !adressePaiement
                        .getIdApplication().equals(getDomaineApplication()))) {

            TIAdressePaiementDataManager mgr = new TIAdressePaiementDataManager();

            mgr.setSession(getSession());
            mgr.setForIdAdressePaiement(getIdAdressePaiement());
            mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
            mgr.find(BManager.SIZE_NOLIMIT);

            java.util.Collection<BIEntity> c = TIAdressePmtResolver.resolve(mgr, getDomaineApplication(), "");

            if (c.size() == 1) {
                setAdressePaiement((TIAdressePaiementData) mgr.get(0));
            } else {
                setAdressePaiement(new TIAdressePaiementData());
            }
        }

        return adressePaiement;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJCompensation loadCompensation() throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getIdCompensationParente())
                && ((compensation == null) || !compensation.getIdCompensation().equals(getIdCompensationParente()))) {
            compensation = new IJCompensation();

            if (getSession() != null) {
                compensation.setSession(getSession());
                compensation.setIdCompensation(getIdCompensationParente());
                compensation.retrieve(getSession().getCurrentThreadTransaction());
            }
        }

        return compensation;
    }

    private void setAdressePaiement(TIAdressePaiementData adresse) {
        setIdAdressePaiement(adresse.getIdAdressePaiement());
        setDomaineApplication(adresse.getIdApplication());
        adressePaiement = adresse;
    }

    /**
     * setter pour l'attribut domaine application
     * 
     * @param domaineApplication
     *            une nouvelle valeur pour cet attribut
     */
    public void setDomaineApplication(String domaineApplication) {
        this.domaineApplication = domaineApplication;
        adressePaiement = null;
    }

    /**
     * setter pour l'attribut id adresse paiement
     * 
     * @param idAdressePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
        adressePaiement = null;
    }

    /**
     * setter pour l'attribut id affilie
     * 
     * @param idAffilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    /**
     * setter pour l'attribut id compensation
     * 
     * @param idCompensation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCompensationParente(String idCompensation) {
        idCompensationParente = idCompensation;
    }

    /**
     * setter pour l'attribut id facture ACompenser
     * 
     * @param idFactureACompenser
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdFactureACompenser(String idFactureACompenser) {
        this.idFactureACompenser = idFactureACompenser;
    }

    /**
     * setter pour l'attribut id facture compta
     * 
     * @param idFactureCompta
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdFactureCompta(String idFactureCompta) {
        this.idFactureCompta = idFactureCompta;
    }

    /**
     * setter pour l'attribut id tiers
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut is compense
     * 
     * @param isCompense
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsCompense(Boolean isCompense) {
        this.isCompense = isCompense;
    }

    /**
     * setter pour l'attribut montant
     * 
     * @param montant
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * setter pour l'attribut no facture
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoFacture(String string) {
        noFacture = string;
    }
}
