/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.ij.api.lots.IIJLot;
import globaz.ij.api.prestations.IIJRepartitionPaiements;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.lots.IJCompensation;
import globaz.ij.db.lots.IJCompensationManager;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRHierarchique;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRepartitionPaiements extends BEntity implements PRHierarchique, IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     */
    public static final String FIELDNAME_DATEVALEUR = "XRDVAL";

    /**
     */
    public static final String FIELDNAME_IDAFFILIE = "XRIAFF";

    /**
	 */
    public static final String FIELDNAME_IDAFFILIEADRESSEPPAIEMENT = "XRIAAP";

    /**
     */
    public static final String FIELDNAME_IDAGENTEXECUTION = "XRIAGE";

    /**
     */
    public static final String FIELDNAME_IDCOMPENSATION = "XRICOM";

    /**
     */
    public static final String FIELDNAME_IDDOMAINEADRESSEPAIEMENT = "XRIDAP";

    /**
     */
    public static final String FIELDNAME_IDINSCRIPTIONCI = "XRIICI";

    /**
     */
    public static final String FIELDNAME_IDPARENT = "XRIPAR";

    /**
     */
    public static final String FIELDNAME_IDPRESTATION = "XRIPRE";

    /**
     */
    public static final String FIELDNAME_IDREPARTITION_PAIEMENT = "XRIREP";

    /**
     */
    public static final String FIELDNAME_IDSITUATIONPROFESSIONNELLE = "XRISIT";

    /**
     */
    public static final String FIELDNAME_IDTIERS = "XRITIE";

    /**
     */
    public static final String FIELDNAME_IDTIERSADRESSEPAIEMENT = "XRITAP";

    /**
     */
    public static final String FIELDNAME_MONTANTBRUT = "XRMMBR";

    /**
     */
    public static final String FIELDNAME_MONTANTNET = "XRMMNE";

    /**
     */
    public static final String FIELDNAME_MONTANTVENTILE = "XRMMVE";

    /**
     */
    public static final String FIELDNAME_NOM = "XRLNOM";

    /**
	 */
    public static final String FIELDNAME_REFERENCE_INTERNE = "XRLREI";

    /**
     */
    public static final String FIELDNAME_TYPEPAIEMENT = "XRTTPM";

    /**
     */
    public static final String TABLE_NAME = "IJREPARP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateValeur = "";
    private String idAffilie = "";
    private String idAffilieAdrPmt = "";
    private String idAgentExecution = "";
    private String idCompensation = "";
    private String idDomaineAdressePaiement = "";
    private String idInscriptionCI = "";
    private String idParent = "";
    private String idPrestation = "";
    private String idRepartitionPaiement = "";
    private String idSituationProfessionnelle = "";
    private String idTiers = "";
    private String idTiersAdressePaiement = "";
    private boolean miseAJourLot = false;
    private String montantBrut = "";
    private String montantNet = "";
    private String montantVentile = "";
    private String nom = "";
    private String referenceInterne = "";

    private transient IJSituationProfessionnelle situationProfessionnelle;

    private String typePaiement = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        remetLotEnOuvertEtEffaceCompensations(transaction);
    }

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        // effacement des assurances
        IJCotisationManager assuranceManager = new IJCotisationManager();

        assuranceManager.setSession(getSession());
        assuranceManager.setForIdRepartitionPaiements(idRepartitionPaiement);
        assuranceManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < assuranceManager.size(); i++) {
            IJCotisation assurance = (IJCotisation) assuranceManager.getEntity(i);

            assurance.wantMiseAJourMontantRepartition(false);
            assurance.delete(transaction);
        }

        // effacement de l'inscription aux CI
        if (!JadeStringUtil.isIntegerEmpty(idInscriptionCI)) {
            IJInscriptionCI inscriptionCI = new IJInscriptionCI();

            inscriptionCI.setSession(getSession());
            inscriptionCI.setIdInscriptionCI(idInscriptionCI);
            inscriptionCI.retrieve(transaction);
            inscriptionCI.delete(transaction);
        }

        remetLotEnOuvertEtEffaceCompensations(transaction);
    }

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        remetLotEnOuvertEtEffaceCompensations(transaction);
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
        setIdRepartitionPaiement(_incCounter(transaction, "0"));

        // chercher adresse de paiement
        if (JadeStringUtil.isIntegerEmpty(idTiersAdressePaiement) && !JadeStringUtil.isIntegerEmpty(idTiers)) {

            setAdressePaiement(PRTiersHelper.getAdressePaiementData(getSession(), transaction, idTiers,
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI, idAffilieAdrPmt,
                    JACalendar.todayJJsMMsAAAA()));
        }
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
        idRepartitionPaiement = statement.dbReadNumeric(FIELDNAME_IDREPARTITION_PAIEMENT);
        idTiers = statement.dbReadNumeric(FIELDNAME_IDTIERS);
        nom = statement.dbReadString(FIELDNAME_NOM);
        montantBrut = statement.dbReadNumeric(FIELDNAME_MONTANTBRUT, 2);
        montantNet = statement.dbReadNumeric(FIELDNAME_MONTANTNET, 2);
        idPrestation = statement.dbReadNumeric(FIELDNAME_IDPRESTATION);
        dateValeur = statement.dbReadDateAMJ(FIELDNAME_DATEVALEUR);
        typePaiement = statement.dbReadNumeric(FIELDNAME_TYPEPAIEMENT);
        idAffilie = statement.dbReadNumeric(FIELDNAME_IDAFFILIE);
        idParent = statement.dbReadNumeric(FIELDNAME_IDPARENT);
        montantVentile = statement.dbReadNumeric(FIELDNAME_MONTANTVENTILE, 2);
        idInscriptionCI = statement.dbReadNumeric(FIELDNAME_IDINSCRIPTIONCI);
        idTiersAdressePaiement = statement.dbReadNumeric(FIELDNAME_IDTIERSADRESSEPAIEMENT);
        idCompensation = statement.dbReadNumeric(FIELDNAME_IDCOMPENSATION);
        idDomaineAdressePaiement = statement.dbReadNumeric(FIELDNAME_IDDOMAINEADRESSEPAIEMENT);
        idSituationProfessionnelle = statement.dbReadNumeric(FIELDNAME_IDSITUATIONPROFESSIONNELLE);
        idAgentExecution = statement.dbReadNumeric(FIELDNAME_IDAGENTEXECUTION);
        referenceInterne = statement.dbReadString(FIELDNAME_REFERENCE_INTERNE);
        idAffilieAdrPmt = statement.dbReadNumeric(FIELDNAME_IDAFFILIEADRESSEPPAIEMENT);
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
        // le tiers est obligatoire
        _propertyMandatory(statement.getTransaction(), idTiers, getSession().getLabel("ERREUR_CHARGEMENT_TIERS"));

        // le total des montants ventiles ne doit pas depasser le total de cette
        // repartition
        if (!JadeStringUtil.isDecimalEmpty(montantVentile)) {
            IJRepartitionPaiementsManager repartitionPaiementsManager = new IJRepartitionPaiementsManager();

            repartitionPaiementsManager.setForIdParent(idParent);
            repartitionPaiementsManager.setNotForIdRepartitionPaiement(idRepartitionPaiement);
            repartitionPaiementsManager.setSession(getSession());

            BigDecimal montantsVentiles = repartitionPaiementsManager.getSum(FIELDNAME_MONTANTVENTILE).add(
                    new BigDecimal(montantVentile));

            if (montantsVentiles.compareTo(new BigDecimal(montantNet)) > 0) {
                _addError(statement.getTransaction(), getSession().getLabel("MONTANT_VENTILE_TROP_GRAND"));
            }

            // si c'est une ventilation, le bénéficiaire ne peut pas être
            // l'assuré
            String idTiersDroit = "";

            IJPrestation prest = new IJPrestation();
            prest.setSession(getSession());
            prest.setIdPrestation(getIdPrestation());
            prest.retrieve();

            IJBaseIndemnisation bi = new IJBaseIndemnisation();
            bi.setSession(getSession());
            bi.setIdBaseIndemisation(prest.getIdBaseIndemnisation());
            bi.retrieve();

            IJPrononce prononce = new IJPrononce();
            prononce.setSession(getSession());
            prononce.setIdPrononce(bi.getIdPrononce());
            prononce.retrieve();

            PRDemande demande = new PRDemande();
            demande.setSession(getSession());
            demande.setIdDemande(prononce.getIdDemande());
            demande.retrieve();

            idTiersDroit = demande.getIdTiers();

            if (idTiersDroit.equals(getIdTiers())) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_REP_EQUAL_BENEF_DROIT"));
            }

        }

        // S'assurer que l'idTiers de la répartition est égal à l'idTiers de
        // l'adresse paiement
        if (!JadeStringUtil.isBlankOrZero(idTiers) && !JadeStringUtil.isBlankOrZero(idTiersAdressePaiement)) {
            if (!idTiers.equals(idTiersAdressePaiement)) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEF_DIFF_PROP_ADR_PMT"));
            }
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
        statement.writeKey(FIELDNAME_IDREPARTITION_PAIEMENT,
                _dbWriteNumeric(statement.getTransaction(), idRepartitionPaiement, "idRepartitionPaiement"));
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
        statement.writeField(FIELDNAME_IDREPARTITION_PAIEMENT,
                _dbWriteNumeric(statement.getTransaction(), idRepartitionPaiement, "idRepartitionPaiement"));
        statement.writeField(FIELDNAME_IDTIERS, _dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(FIELDNAME_NOM, _dbWriteString(statement.getTransaction(), getNom(), "nom"));
        statement.writeField(FIELDNAME_MONTANTBRUT,
                _dbWriteNumeric(statement.getTransaction(), montantBrut, "montantBrut"));
        statement.writeField(FIELDNAME_MONTANTNET,
                _dbWriteNumeric(statement.getTransaction(), montantNet, "montantNet"));
        statement.writeField(FIELDNAME_IDPRESTATION,
                _dbWriteNumeric(statement.getTransaction(), idPrestation, "idPrestation"));
        statement.writeField(FIELDNAME_DATEVALEUR,
                _dbWriteDateAMJ(statement.getTransaction(), dateValeur, "dateValeur"));
        statement.writeField(FIELDNAME_TYPEPAIEMENT,
                _dbWriteNumeric(statement.getTransaction(), typePaiement, "typePaiement"));
        statement.writeField(FIELDNAME_IDAFFILIE, _dbWriteNumeric(statement.getTransaction(), idAffilie, "idAffilie"));
        statement.writeField(FIELDNAME_IDPARENT, _dbWriteNumeric(statement.getTransaction(), idParent, "idParent"));
        statement.writeField(FIELDNAME_MONTANTVENTILE,
                _dbWriteNumeric(statement.getTransaction(), montantVentile, "montantVentile"));
        statement.writeField(FIELDNAME_IDINSCRIPTIONCI,
                _dbWriteNumeric(statement.getTransaction(), idInscriptionCI, "idInscriptionCI"));
        statement.writeField(FIELDNAME_IDTIERSADRESSEPAIEMENT,
                _dbWriteNumeric(statement.getTransaction(), idTiersAdressePaiement, "idTiersAdressePaiement"));
        statement.writeField(FIELDNAME_IDCOMPENSATION,
                _dbWriteNumeric(statement.getTransaction(), idCompensation, "idCompensation"));
        statement.writeField(FIELDNAME_IDDOMAINEADRESSEPAIEMENT,
                _dbWriteNumeric(statement.getTransaction(), idDomaineAdressePaiement, "idDomaineAdressePaiement"));
        statement.writeField(FIELDNAME_IDSITUATIONPROFESSIONNELLE,
                _dbWriteNumeric(statement.getTransaction(), idSituationProfessionnelle, "idSituationProfessionnelle"));
        statement.writeField(FIELDNAME_IDAGENTEXECUTION,
                _dbWriteNumeric(statement.getTransaction(), idAgentExecution, "idAgentExecution"));
        statement.writeField(FIELDNAME_REFERENCE_INTERNE,
                _dbWriteString(statement.getTransaction(), getReferenceInterne(), "referenceInterne"));
        statement.writeField(FIELDNAME_IDAFFILIEADRESSEPPAIEMENT,
                _dbWriteNumeric(statement.getTransaction(), idAffilieAdrPmt, "idAffilieAdrPmt"));
    }

    /**
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        IJRepartitionPaiements clone = new IJRepartitionPaiements();

        clone.setDateValeur(getDateValeur());
        clone.setIdAffilie(getIdAffilie());
        clone.setIdCompensation(getIdCompensation());
        clone.setIdDomaineAdressePaiement(getIdDomaineAdressePaiement());
        clone.setIdInscriptionCI(getIdInscriptionCI());
        clone.setIdParent(getIdParent());
        clone.setIdPrestation(getIdPrestation());
        clone.setIdSituationProfessionnelle(getIdSituationProfessionnelle());
        clone.setIdTiers(getIdTiers());
        clone.setIdTiersAdressePaiement(getIdTiersAdressePaiement());
        clone.setMontantBrut(getMontantBrut());
        clone.setMontantNet(getMontantNet());
        clone.setMontantVentile(getMontantVentile());
        clone.setNom(getNom());
        clone.setTypePaiement(getTypePaiement());
        clone.setIdAgentExecution(idAgentExecution);
        clone.setReferenceInterne(getReferenceInterne());
        clone.setIdAffilieAdrPmt(getIdAffilieAdrPmt());

        clone.wantCallValidate(false);
        clone.wantMiseAJourLot(false);

        return clone;
    }

    /**
     * getter pour l'attribut date valeur.
     * 
     * @return la valeur courante de l'attribut date valeur
     */
    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * getter pour l'attribut id affilie.
     * 
     * @return la valeur courante de l'attribut id affilie
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdAffilieAdrPmt() {
        return idAffilieAdrPmt;
    }

    /**
     * getter pour l'attribut id agent execution.
     * 
     * @return la valeur courante de l'attribut id agent execution
     */
    public String getIdAgentExecution() {
        return idAgentExecution;
    }

    /**
     * getter pour l'attribut id compensation.
     * 
     * @return la valeur courante de l'attribut id compensation
     */
    public String getIdCompensation() {
        return idCompensation;
    }

    /**
     * getter pour l'attribut id domaine adresse paiement.
     * 
     * @return la valeur courante de l'attribut id domaine adresse paiement
     */
    public String getIdDomaineAdressePaiement() {
        return idDomaineAdressePaiement;
    }

    /**
     * getter pour l'attribut id inscription CI.
     * 
     * @return la valeur courante de l'attribut id inscription CI
     */
    public String getIdInscriptionCI() {
        return idInscriptionCI;
    }

    /**
     * getter pour l'attribut id majeur.
     * 
     * @return la valeur courante de l'attribut id majeur
     */
    @Override
    public String getIdMajeur() {
        return getIdRepartitionPaiement();
    }

    /**
     * getter pour l'attribut id parent.
     * 
     * @return la valeur courante de l'attribut id parent
     */
    @Override
    public String getIdParent() {
        return idParent;
    }

    /**
     * getter pour l'attribut id decompte global.
     * 
     * @return la valeur courante de l'attribut id decompte global
     */
    public String getIdPrestation() {
        return idPrestation;
    }

    /**
     * getter pour l'attribut id decompte beneficiaire paiement.
     * 
     * @return la valeur courante de l'attribut id decompte beneficiaire paiement
     */
    public String getIdRepartitionPaiement() {
        return idRepartitionPaiement;
    }

    /**
     * getter pour l'attribut id situation professionnelle.
     * 
     * @return la valeur courante de l'attribut id situation professionnelle
     */
    public String getIdSituationProfessionnelle() {
        return idSituationProfessionnelle;
    }

    /**
     * getter pour l'attribut id tiers.
     * 
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut id adresse paiement.
     * 
     * @return la valeur courante de l'attribut id adresse paiement
     */
    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    /**
     * getter pour l'attribut montant brut.
     * 
     * @return la valeur courante de l'attribut montant brut
     */
    public String getMontantBrut() {
        return montantBrut;
    }

    /**
     * getter pour l'attribut montant net.
     * 
     * @return la valeur courante de l'attribut montant net
     */
    public String getMontantNet() {
        return montantNet;
    }

    /**
     * getter pour l'attribut montant restant.
     * 
     * @return la valeur courante de l'attribut montant restant
     */
    public String getMontantRestant() {
        FWCurrency retValue = new FWCurrency(montantNet);

        retValue.sub(montantVentile);

        return retValue.toString();
    }

    /**
     * getter pour l'attribut montant ventile.
     * 
     * @return la valeur courante de l'attribut montant ventile
     */
    public String getMontantVentile() {
        return montantVentile;
    }

    /**
     * getter pour l'attribut nom.
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        // Max 40 pos en DB
        if (nom != null && nom.length() > 40) {
            return nom.substring(0, 39);
        } else {
            return nom;
        }
    }

    /**
     * @return
     */
    public String getReferenceInterne() {
        return referenceInterne;
    }

    /**
     * getter pour l'attribut type paiement.
     * 
     * @return la valeur courante de l'attribut type paiement
     */
    public String getTypePaiement() {
        return typePaiement;
    }

    /**
     * getter pour l'attribut unique primary key.
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdRepartitionPaiement();
    }

    /**
     * retourne vrai si le beneficiaire du paiement est un employeur, faux si le beneficiaire est un assure.
     * 
     * @return la valeur courante de l'attribut beneficiaire employeur
     */
    public boolean isBeneficiaireEmployeur() {
        return IIJRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR.equals(typePaiement);
    }

    /**
     * getter pour l'attribut mise AJour lot.
     * 
     * @return la valeur courante de l'attribut mise AJour lot
     */
    public boolean isMiseAJourLot() {
        return miseAJourLot;
    }

    /**
     * charge l'adresse de paiement.
     * 
     * @return une adresse de paiement ou null.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public TIAdressePaiementData loadAdressePaiement(String dateValeurCompta) throws Exception {

        TIAdressePaiementData adressePmt = PRTiersHelper.getAdressePaiementData(getSession(), getSession()
                .getCurrentThreadTransaction(), idTiersAdressePaiement, getIdDomaineAdressePaiement(),
                getIdAffilieAdrPmt(), dateValeurCompta);

        if (adressePmt.isNew()) {
            return null;
        } else {
            return adressePmt;
        }

    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJSituationProfessionnelle loadSituationProfessionnelle() throws Exception {
        if ((situationProfessionnelle == null) && !JadeStringUtil.isIntegerEmpty(idSituationProfessionnelle)) {
            situationProfessionnelle = new IJSituationProfessionnelle();
            situationProfessionnelle.setIdSituationProfessionnelle(idSituationProfessionnelle);
            situationProfessionnelle.setSession(getSession());
            situationProfessionnelle.retrieve();
        }

        return situationProfessionnelle;
    }

    private void remetLotEnOuvertEtEffaceCompensations(BTransaction transaction) throws Exception {
        // remise en état ouvert du lot et effacement des compensations(S'il est
        // compensé, les compensations sont
        // maintenant incohérentes)
        if (miseAJourLot) {
            IJPrestation prestation = new IJPrestation();

            prestation.setSession(getSession());
            prestation.setIdPrestation(idPrestation);
            prestation.retrieve(transaction);

            IJLot lot = new IJLot();

            lot.setSession(getSession());
            lot.setIdLot(prestation.getIdLot());
            lot.retrieve(transaction);

            if (lot.getCsEtat().equals(IIJLot.CS_COMPENSE)) {
                lot.setCsEtat(IIJLot.CS_OUVERT);
                lot.update(transaction);

                IJCompensationManager compensationManager = new IJCompensationManager();

                compensationManager.setSession(getSession());
                compensationManager.setForIdLot(lot.getIdLot());
                compensationManager.find(transaction);

                for (int i = 0; i < compensationManager.size(); i++) {
                    ((IJCompensation) (compensationManager.getEntity(i))).delete(transaction);
                }
            }
        }
    }

    /**
     * setter pour l'attribut adresse paiement.
     * 
     * @param adressePaiement
     *            une nouvelle valeur pour cet attribut
     * @throws Exception
     */
    public void setAdressePaiement(TIAdressePaiementData adressePaiement) throws Exception {
        if (adressePaiement != null) {
            idTiersAdressePaiement = adressePaiement.getIdTiers();
            idDomaineAdressePaiement = adressePaiement.getIdApplication();

            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(getSession(),
                    adressePaiement.getIdExterneAvoirPaiement());
            if (affilie != null) {
                idAffilieAdrPmt = affilie.getIdAffilie();
            }

        } else {
            idTiersAdressePaiement = "";
            idDomaineAdressePaiement = "";
            idAffilieAdrPmt = "";
        }
    }

    /**
     * setter pour l'attribut date valeur.
     * 
     * @param dateValeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    /**
     * setter pour l'attribut id affilie.
     * 
     * @param idAffilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdAffilieAdrPmt(String idAffilieAdrPmt) {
        this.idAffilieAdrPmt = idAffilieAdrPmt;
    }

    /**
     * setter pour l'attribut id agent execution.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAgentExecution(String string) {
        idAgentExecution = string;
    }

    /**
     * setter pour l'attribut id compensation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCompensation(String string) {
        idCompensation = string;
    }

    /**
     * setter pour l'attribut id domaine adresse paiement.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDomaineAdressePaiement(String string) {
        idDomaineAdressePaiement = string;
    }

    /**
     * setter pour l'attribut id inscription CI.
     * 
     * @param idInscriptionCI
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdInscriptionCI(String idInscriptionCI) {
        this.idInscriptionCI = idInscriptionCI;
    }

    /**
     * setter pour l'attribut id parent.
     * 
     * @param idParent
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    /**
     * setter pour l'attribut id decompte global.
     * 
     * @param idDecompteGlobal
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrestation(String idDecompteGlobal) {
        idPrestation = idDecompteGlobal;
    }

    /**
     * setter pour l'attribut id decompte beneficiaire paiement.
     * 
     * @param idDecompteBeneficiairePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRepartitionPaiement(String idDecompteBeneficiairePaiement) {
        idRepartitionPaiement = idDecompteBeneficiairePaiement;
    }

    /**
     * setter pour l'attribut id situation professionnelle.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationProfessionnelle(String string) {
        idSituationProfessionnelle = string;
    }

    /**
     * setter pour l'attribut id tiers.
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * setter pour l'attribut id adresse paiement.
     * 
     * @param idAdressePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersAdressePaiement(String idAdressePaiement) {
        idTiersAdressePaiement = idAdressePaiement;
    }

    /**
     * setter pour l'attribut mise AJour lot.
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setMiseAJourLot(boolean b) {
        miseAJourLot = b;
    }

    /**
     * setter pour l'attribut montant brut.
     * 
     * @param montantBrut
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    /**
     * setter pour l'attribut montant net.
     * 
     * @param montantNet
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantNet(String montantNet) {
        this.montantNet = montantNet;
    }

    /**
     * setter pour l'attribut montant ventile.
     * 
     * @param montantVentile
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantVentile(String montantVentile) {
        this.montantVentile = montantVentile;
    }

    /**
     * setter pour l'attribut nom.
     * 
     * @param nom
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @param string
     */
    public void setReferenceInterne(String string) {
        referenceInterne = string;
    }

    /**
     * setter pour l'attribut type paiement.
     * 
     * @param typePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    /**
     * setter pour l'attribut unique primary key.
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdRepartitionPaiement(pk);
    }

    /**
     * @param b
     *            DOCUMENT ME!
     */
    public void wantMiseAJourLot(boolean b) {
        miseAJourLot = b;
    }

}
