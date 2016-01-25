/*
 * Créé le 28 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.lots;

import globaz.apg.application.APApplication;
import globaz.globall.api.BIEntity;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementDataManager;
import globaz.pyxis.util.TIAdressePmtResolver;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APFactureACompenser extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 3640110237117867792L;

    /**
     */
    public static final String FIELDNAME_DOMAINEADRESSE = "VOIDOP";

    /**
     */
    public static final String FIELDNAME_IDADRESSE = "VOIADP";

    /**
     */
    public static final String FIELDNAME_IDAFFILIE = "VOIAFF";

    /**
     */
    public static final String FIELDNAME_IDCOMPENSATIONPARENTE = "VOICOM";

    /**
     */
    public static final String FIELDNAME_IDFACTACOMPENSER = "VOIFAC";

    /**
     */
    public static final String FIELDNAME_IDFACTURE = "VOIFCT";

    /**
     */
    public static final String FIELDNAME_IDTIERS = "VOITIE";

    /**
     */
    public static final String FIELDNAME_ISCOMPENSER = "VOBCOM";

    /**
     */
    public static final String FIELDNAME_MONTANTACOMPENSER = "VOMMON";

    public static final String FIELDNAME_NO_FACTURE = "VONNFA";

    /**
     */
    public static final String TABLE_NAME = "APFAACP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient TIAdressePaiementData adressePaiement = null;

    /**
     */
    protected transient IPRAffilie affilie = null;

    private transient APCompensation compensation = null;

    /**
     */
    protected String domaineAdresse = "";

    /**
     */
    protected String idAdresse = "";

    /**
     */
    protected String idAffilie = "";

    /**
     */
    protected String idCompensationParente = "";

    /**
     */
    protected String idFacture = "";

    /**
     */
    protected String idFactureACompenser = "";

    /**
     */
    protected String idTiers = "";
    /**
     */
    protected Boolean isCompenser = Boolean.FALSE;
    /**
     */
    protected String montant = "";

    protected String noFacture = "";

    /**
     */
    protected transient PRTiersWrapper tiers = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (!isLoadedFromManager()) {
            if (JadeStringUtil.isIntegerEmpty(idAffilie)) {
                if (!JadeStringUtil.isIntegerEmpty(idTiers)) {
                    tiers = PRTiersHelper.getTiersAdresseParId(getSession(), idTiers);
                }
            } else {
                affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), transaction, idAffilie, idTiers);
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdFactureACompenser(this._incCounter(transaction, "0"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return APFactureACompenser.TABLE_NAME;
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
        idFactureACompenser = statement.dbReadNumeric(APFactureACompenser.FIELDNAME_IDFACTACOMPENSER);
        montant = statement.dbReadNumeric(APFactureACompenser.FIELDNAME_MONTANTACOMPENSER, 2);
        idFacture = statement.dbReadNumeric(APFactureACompenser.FIELDNAME_IDFACTURE);
        idCompensationParente = statement.dbReadNumeric(APFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE);
        isCompenser = statement.dbReadBoolean(APFactureACompenser.FIELDNAME_ISCOMPENSER);
        idTiers = statement.dbReadNumeric(APFactureACompenser.FIELDNAME_IDTIERS);
        idAffilie = statement.dbReadNumeric(APFactureACompenser.FIELDNAME_IDAFFILIE);
        idAdresse = statement.dbReadNumeric(APFactureACompenser.FIELDNAME_IDADRESSE);
        domaineAdresse = statement.dbReadNumeric(APFactureACompenser.FIELDNAME_DOMAINEADRESSE);
        noFacture = statement.dbReadNumeric(APFactureACompenser.FIELDNAME_NO_FACTURE);
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
        // on ne veut pas que le total des montants compensé soit supérieur au
        // montant total des apg
        String errorMessage = "";
        String infoCompensation = "";
        APCompensation compensation = null;
        if (!JadeStringUtil.isIntegerEmpty(idCompensationParente)) {
            APFactureACompenserManager mgr = new APFactureACompenserManager();
            compensation = loadCompensation();

            mgr.setForIdCompensationParente(idCompensationParente);
            mgr.setExceptForIdFactureACompenser(idFactureACompenser);
            mgr.setSession(getSession());
            mgr.setForIsCompenser(Boolean.TRUE);

            // Il faut prendre les valeurs absolues pour la comparaison des
            // montants.
            // Cas standard :
            // - montant total de la compensation == montant de la prestation :
            // 1000.-
            // - montant à compenser (dette) == 300.-
            // Si 1000 > 300 --> OK
            //
            // Cas prestation de restitution :
            // - montant total de la compensation == montant de la prestation :
            // -1000.-
            // - montant à compenser == -300.-
            // Si -1000 > -300 --> NOT OK si on ne prend pas en comptes les
            // valeurs absolues !!!
            //
            // La proposition de compensation, ou montants à compenser sont
            // toujours du même signe que le montant de la prestation.
            double totalCompense = Math.abs(mgr.getSum(APFactureACompenser.FIELDNAME_MONTANTACOMPENSER)
                    .add(new BigDecimal(montant)).doubleValue());
            double totalApg = Math.abs(Double.parseDouble(compensation.getMontantTotal()));

            if (totalCompense > totalApg) {
                errorMessage += getSession().getLabel("MONTANT_COMPENSE_TROP_GRAND");
                infoCompensation += " / " + getSession().getLabel("JSP_MONTANT_APG") + " "
                        + compensation.getMontantTotal();
                infoCompensation += " / " + getSession().getLabel("TOTAL_A_COMPENSER") + " " + totalCompense;
            }
        }

        // id tiers ou affilié obligatoire
        if (JadeStringUtil.isIntegerEmpty(idTiers) && JadeStringUtil.isIntegerEmpty(idAffilie)) {
            errorMessage += getSession().getLabel("TIERS_OU_AFFILIE_OBLIGATOIRE");
        }

        // pas de compensations sur factures futures pour les non-affilies
        if (JadeStringUtil.isIntegerEmpty(getNoFacture()) && JadeStringUtil.isIntegerEmpty(idAffilie)) {
            errorMessage += getSession().getLabel("COMP_FACT_FUTURE_POUR_NON_AFFILIES");
        }

        if (!JadeStringUtil.isEmpty(errorMessage)) {
            if (compensation != null) {
                // On suit la même logique que dans l'écran des compensation (PAP0026), on affiche le n° affilié si il
                // est existant sinon le nom et prénom du bénéficiaire de base
                String infoAffilie = "";
                if (!JadeStringUtil.isIntegerEmpty(compensation.getIdAffilie())) {
                    IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(), getSession()
                            .newTransaction(), compensation.getIdAffilie(), compensation.getIdTiers());
                    infoAffilie += " / " + getSession().getLabel("JSP_NO_AFFILIE") + " " + affilie.getNumAffilie();
                }
                if (JadeStringUtil.isEmpty(infoAffilie) && !JadeStringUtil.isIntegerEmpty(compensation.getIdTiers())) {
                    PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), compensation.getIdTiers());
                    infoAffilie += " / " + getSession().getLabel("JSP_BENEFICIAIRE") + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
                    if (!JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM))) {
                        infoAffilie += ", " + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    }
                }
                errorMessage += infoAffilie;
            }
            if (!JadeStringUtil.isIntegerEmpty(infoCompensation)) {
                errorMessage += infoCompensation;
            }
            _addError(statement.getTransaction(), errorMessage);
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
        statement.writeKey(APFactureACompenser.FIELDNAME_IDFACTACOMPENSER,
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
        BTransaction transaction = statement.getTransaction();

        statement.writeField(APFactureACompenser.FIELDNAME_DOMAINEADRESSE,
                this._dbWriteNumeric(transaction, domaineAdresse, "domaineAdresse"));
        statement.writeField(APFactureACompenser.FIELDNAME_IDADRESSE,
                this._dbWriteNumeric(transaction, idAdresse, "idAdresse"));
        statement.writeField(APFactureACompenser.FIELDNAME_IDAFFILIE,
                this._dbWriteNumeric(transaction, idAffilie, "idAffilie"));
        statement.writeField(APFactureACompenser.FIELDNAME_IDCOMPENSATIONPARENTE,
                this._dbWriteNumeric(transaction, idCompensationParente, "idCompensationParente"));
        statement.writeField(APFactureACompenser.FIELDNAME_IDFACTACOMPENSER,
                this._dbWriteNumeric(transaction, idFactureACompenser, "idFactureACompenser"));
        statement.writeField(APFactureACompenser.FIELDNAME_IDFACTURE,
                this._dbWriteNumeric(transaction, idFacture, "idFacture"));
        statement.writeField(APFactureACompenser.FIELDNAME_IDTIERS,
                this._dbWriteNumeric(transaction, idTiers, "idTiers"));
        statement.writeField(APFactureACompenser.FIELDNAME_ISCOMPENSER,
                this._dbWriteBoolean(transaction, isCompenser, BConstants.DB_TYPE_BOOLEAN_CHAR, "isCompenser"));
        statement.writeField(APFactureACompenser.FIELDNAME_MONTANTACOMPENSER,
                this._dbWriteNumeric(transaction, montant, "montant"));
        statement.writeField(APFactureACompenser.FIELDNAME_NO_FACTURE,
                this._dbWriteNumeric(transaction, noFacture, "noFacture"));
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
                APApplication.CS_DOMAINE_ADRESSE_APG, getIdAffilie(), JACalendar.todayJJsMMsAAAA()));

    }

    /**
     * getter pour l'attribut domaine adresse
     * 
     * @return la valeur courante de l'attribut domaine adresse
     */
    public String getDomaineAdresse() {
        return domaineAdresse;
    }

    /**
     * getter pour l'attribut id adresse
     * 
     * @return la valeur courante de l'attribut id adresse
     */
    public String getIdAdresse() {
        return idAdresse;
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
     * getter pour l'attribut id compensation parente
     * 
     * @return la valeur courante de l'attribut id compensation parente
     */
    public String getIdCompensationParente() {
        return idCompensationParente;
    }

    /**
     * getter pour l'attribut id facture
     * 
     * @return la valeur courante de l'attribut id facture
     */
    public String getIdFacture() {
        return idFacture;
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
     * getter pour l'attribut id tiers
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut is compenser
     * 
     * @return la valeur courante de l'attribut is compenser
     */
    public Boolean getIsCompenser() {
        return isCompenser;
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
                APCompensation compensation = new APCompensation();
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
        if (!JadeStringUtil.isIntegerEmpty(getIdAdresse())
                && ((adressePaiement == null) || !adressePaiement.getIdAdressePaiement().equals(getIdAdresse()) || !adressePaiement
                        .getIdApplication().equals(getDomaineAdresse()))) {

            TIAdressePaiementDataManager mgr = new TIAdressePaiementDataManager();

            mgr.setSession(getSession());
            mgr.setForIdAdressePaiement(getIdAdresse());
            mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
            mgr.find(BManager.SIZE_NOLIMIT);

            java.util.Collection<BIEntity> c = TIAdressePmtResolver.resolve(mgr, getDomaineAdresse(), "");

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
    public APCompensation loadCompensation() throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getIdCompensationParente())
                && ((compensation == null) || !compensation.getIdCompensation().equals(getIdCompensationParente()))) {
            compensation = new APCompensation();

            if (getSession() != null) {
                compensation.setSession(getSession());
                compensation.setIdCompensation(getIdCompensationParente());
                compensation.retrieve(getSession().getCurrentThreadTransaction());
            }
        }

        return compensation;
    }

    private void setAdressePaiement(TIAdressePaiementData adresse) {
        setIdAdresse(adresse.getIdAdressePaiement());
        setDomaineAdresse(adresse.getIdApplication());
        adressePaiement = adresse;
    }

    /**
     * setter pour l'attribut domaine adresse
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDomaineAdresse(String string) {
        domaineAdresse = string;
        adressePaiement = null;
    }

    /**
     * setter pour l'attribut id adresse
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAdresse(String string) {
        idAdresse = string;
        adressePaiement = null;
    }

    /**
     * setter pour l'attribut id affilie
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    /**
     * setter pour l'attribut id compensation parente
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCompensationParente(String string) {
        idCompensationParente = string;
    }

    /**
     * setter pour l'attribut id facture
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdFacture(String string) {
        idFacture = string;
    }

    /**
     * setter pour l'attribut id facture ACompenser
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdFactureACompenser(String string) {
        idFactureACompenser = string;
    }

    /**
     * setter pour l'attribut id tiers
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * setter pour l'attribut is compenser
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsCompenser(Boolean boolean1) {
        isCompenser = boolean1;
    }

    /**
     * setter pour l'attribut montant
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontant(String string) {
        montant = string;
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
