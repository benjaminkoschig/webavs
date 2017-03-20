package globaz.apg.db.prestation;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.lots.IAPLot;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.lots.APCompensation;
import globaz.apg.db.lots.APCompensationManager;
import globaz.apg.db.lots.APLot;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRHierarchique;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.math.BigDecimal;
import java.util.Hashtable;

/**
 * Descpription.
 * 
 * @author scr Date de création 31 mai 05
 */
public class APRepartitionPaiements extends BEntity implements PRHierarchique {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEVALEUR = "VIDVAL";
    public static final String FIELDNAME_IDAFFILIE = "VIIAFF";
    public static final String FIELDNAME_IDAFFILIEADRESSEPAIEMENT = "VIIAAP";
    public static final String FIELDNAME_IDCOMPENSATION = "VIICOM";
    public static final String FIELDNAME_IDDOMAINE = "VIIDOP";
    public static final String FIELDNAME_IDINSCRIPTIONCI = "VIIICI";
    public static final String FIELDNAME_IDPARENT = "VIIPAR";
    public static final String FIELDNAME_IDPRESTATIONAPG = "VIIPRA";
    public static final String FIELDNAME_IDREPARTITIONBENEFPAIEMENT = "VIIRBP";
    public static final String FIELDNAME_IDSITUATIONPROFESSIONNELLE = "VIISIT";
    public static final String FIELDNAME_IDTIERS = "VIITIE";
    public static final String FIELDNAME_IDTIERSADRESSEPAIEMENT = "VIITAP";
    public static final String FIELDNAME_MONTANTBRUT = "VIMMOB";
    public static final String FIELDNAME_MONTANTNET = "VIMMON";
    public static final String FIELDNAME_MONTANTVENTILE = "VIMMOV";
    public static final String FIELDNAME_NOM = "VILNOM";
    public static final String FIELDNAME_REFERENCE_INTERNE = "VILREI";
    public static final String FIELDNAME_TAUXRJM = "VIMTRJ";
    public static final String FIELDNAME_TYPE_ASSOCIATION_ASSURANCE = "VITAAS";
    public static final String FIELDNAME_TYPEPAIEMENT = "VITTPM";
    public static final String FIELDNAME_TYPEPRESTATION = "VITTPR";
    public static final String TABLE_NAME = "APREPAP";

    private String dateValeur = "";
    protected String idAffilie = "";
    private String idAffilieAdrPmt = "";
    private String idCompensation = "";
    private String idDomaineAdressePaiement = "";
    private String idInscriptionCI = "";
    private String idParent = "";
    protected String idPrestationApg = "";
    private String idRepartitionBeneficiairePaiement = "";
    private String idSituationProfessionnelle = "";
    protected String idTiers = "";
    private String idTiersAdressePaiement = "";
    private boolean miseAJourLot = true;
    private String montantBrut = "";
    private String montantNet = "";
    private String montantVentile = "";
    private String nom = "";
    private String referenceInterne = "";
    private transient APSituationProfessionnelle sitPro;
    private String tauxRJM = "";
    private String typeAssociationAssurance = "";
    private String typePaiement = "";
    private String typePrestation = "";

    /**
     * (non-Javadoc).
     * 
     * @see globaz.globall.db.BEntity#_afterAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        super._afterAdd(transaction);
        remetLotEnOuvertEtEffaceCompensations(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // effacement des assurances
        APCotisationManager mgr = new APCotisationManager();

        mgr.setSession(getSession());
        mgr.setForIdRepartitionBeneficiairePaiement(idRepartitionBeneficiairePaiement);
        mgr.find(transaction);

        for (int idAssurance = 0; idAssurance < mgr.size(); ++idAssurance) {
            APCotisation assurance = (APCotisation) mgr.get(idAssurance);

            // Lors de la supression d'une cotisation, la methode afterDelete
            // remet à jours
            // le montant net de la répartition.
            // Dans le cas présent, on ne veut pas de mise à jours de cette
            // répartion, car elle
            // est supprimée.
            assurance.wantCallMethodAfter(false);
            assurance.setSession(getSession());
            assurance.delete(transaction);
        }

        remetLotEnOuvertEtEffaceCompensations(transaction);
    }

    /**
     * (non-Javadoc).
     * 
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        super._afterUpdate(transaction);
        remetLotEnOuvertEtEffaceCompensations(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdRepartitionBeneficiairePaiement(this._incCounter(transaction, "0"));
        // on recherche l'adresse de paiement pour cette application ou
        // l'adresse standard
        if (JadeStringUtil.isBlankOrZero(idParent)) {
            chercherAdressePaiement(transaction, idSituationProfessionnelle);
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APRepartitionPaiements.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDTIERS);
        nom = statement.dbReadString(APRepartitionPaiements.FIELDNAME_NOM);
        montantBrut = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_MONTANTBRUT);
        montantNet = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_MONTANTNET);
        idPrestationApg = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG);
        dateValeur = statement.dbReadDateAMJ(APRepartitionPaiements.FIELDNAME_DATEVALEUR);
        typePaiement = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_TYPEPAIEMENT);
        idCompensation = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDCOMPENSATION);
        idAffilie = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDAFFILIE);
        idParent = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDPARENT);
        montantVentile = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_MONTANTVENTILE);
        idInscriptionCI = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDINSCRIPTIONCI);
        idRepartitionBeneficiairePaiement = statement
                .dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT);
        typePrestation = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_TYPEPRESTATION);
        idTiersAdressePaiement = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDTIERSADRESSEPAIEMENT);
        idDomaineAdressePaiement = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDDOMAINE);
        tauxRJM = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_TAUXRJM);
        idSituationProfessionnelle = statement
                .dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDSITUATIONPROFESSIONNELLE);
        referenceInterne = statement.dbReadString(APRepartitionPaiements.FIELDNAME_REFERENCE_INTERNE);
        idAffilieAdrPmt = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_IDAFFILIEADRESSEPAIEMENT);

        typeAssociationAssurance = statement.dbReadNumeric(APRepartitionPaiements.FIELDNAME_TYPE_ASSOCIATION_ASSURANCE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), idTiers, getSession().getLabel("TIERS_INTROUVABLE"));

        // si c'est une ventilation, on ne veut pas que la somme de tous les
        // montants ventilés soit supérieur au montant net du parent
        if (!JadeStringUtil.isIntegerEmpty(montantVentile)) {
            FWCurrency total = new FWCurrency(montantTotalVentilation(statement.getTransaction()).toString());

            total.add(montantVentile);

            if (total.compareTo(new FWCurrency(montantNet)) > 0) {
                _addError(statement.getTransaction(), getSession().getLabel("MONTANT_VENTILE_TROP_GRAND"));
            }

            // si on a pas tout ventilé, on doit avoir une adresse de paiement
            if (total.compareTo(new FWCurrency(montantNet)) < 0) {
                String msg = getSession().getLabel("ADRESSE_PAIMEMENT_REQUISE") + " : " + getNom()
                        + " - idPrestation/idRepartition = " + getIdPrestationApg() + "/"
                        + getIdRepartitionBeneficiairePaiement();

                if (JadeStringUtil.isBlankOrZero(idTiersAdressePaiement)) {
                    try {
                        msg = getSession().getLabel("ADRESSE_PAIMEMENT_REQUISE");
                        APPrestation prst = new APPrestation();
                        prst.setSession(getSession());
                        prst.setIdPrestationApg(getIdPrestationApg());
                        prst.retrieve();
                        msg += " : " + getNom() + " - idDroit/idPrestation/idRepartition = " + prst.getIdDroit() + "/"
                                + getIdPrestationApg() + "/" + getIdRepartitionBeneficiairePaiement();
                    } catch (Exception e) {
                        ;
                    }
                }
                _propertyMandatory(statement.getTransaction(), idTiersAdressePaiement, msg);
            }

            // si c'est une ventilation, le bénéficiaire ne peut pas être
            // l'assuré
            String idTiersDroit = "";

            APPrestation prest = new APPrestation();
            prest.setSession(getSession());
            prest.setIdPrestationApg(getIdPrestationApg());
            prest.retrieve();

            APDroitLAPG droit = new APDroitLAPG();
            droit.setSession(getSession());
            droit.setIdDroit(prest.getIdDroit());
            droit.retrieve();

            PRDemande demande = new PRDemande();
            demande.setSession(getSession());
            demande.setIdDemande(droit.getIdDemande());
            demande.retrieve();

            idTiersDroit = demande.getIdTiers();

            if (idTiersDroit.equals(getIdTiers())) {
                _addError(statement.getTransaction(), getSession().getLabel("BENEFICIAIRE_REP_EQUAL_BENEF_DROIT"));
            }
        }
        // si c'est pas une ventilation
        else {
            // si le montant brut est différent de zéro
            if (!JadeStringUtil.isBlankOrZero(montantBrut)) {

                // on check l'adresse de paiement, seulement si le montant net -
                // les ventilations donnent un reste
                FWCurrency totalVentilations = new FWCurrency(montantTotalVentilation(statement.getTransaction())
                        .toString());
                FWCurrency montantCtrl = new FWCurrency(montantNet);
                montantCtrl.sub(totalVentilations);

                if (montantCtrl.isPositive()) {

                    String msg = getSession().getLabel("ADRESSE_PAIMEMENT_REQUISE") + " : " + getNom()
                            + " - idPrestation/idRepartition = " + getIdPrestationApg() + "/"
                            + getIdRepartitionBeneficiairePaiement();

                    if (JadeStringUtil.isBlankOrZero(idTiersAdressePaiement)) {
                        try {
                            msg = getSession().getLabel("ADRESSE_PAIMEMENT_REQUISE");
                            APPrestation prst = new APPrestation();
                            prst.setSession(getSession());
                            prst.setIdPrestationApg(getIdPrestationApg());
                            prst.retrieve();
                            msg += " : " + getNom() + " - idDroit/idPrestation/idRepartition = " + prst.getIdDroit()
                                    + "/" + getIdPrestationApg() + "/" + getIdRepartitionBeneficiairePaiement();
                        } catch (Exception e) {
                            ;
                        }
                    }
                    _propertyMandatory(statement.getTransaction(), idTiersAdressePaiement, msg);

                }
            }
        }

        // S'assurer que l'idTiers de la répartition est égal à l'idTiers de
        // l'adresse paiement
        if (!JadeStringUtil.isBlankOrZero(idTiers) && !JadeStringUtil.isBlankOrZero(idTiersAdressePaiement)) {
            if (!idTiers.equals(idTiersAdressePaiement)) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("ERROR_BENEFICIAIRE_DIFF_PROPR_ADRESSE_PAIEMENT"));
            }
        }

    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT, this._dbWriteNumeric(
                statement.getTransaction(), idRepartitionBeneficiairePaiement, "idRepartitionBeneficiairePaiement"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDTIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_NOM,
                this._dbWriteString(statement.getTransaction(), getNom(), "nom"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_MONTANTBRUT,
                this._dbWriteNumeric(statement.getTransaction(), montantBrut, "montantBrut"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_MONTANTNET,
                this._dbWriteNumeric(statement.getTransaction(), montantNet, "montantNet"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDPRESTATIONAPG,
                this._dbWriteNumeric(statement.getTransaction(), idPrestationApg, "idPrestationApg"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_DATEVALEUR,
                this._dbWriteDateAMJ(statement.getTransaction(), dateValeur, "dateValeur"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_TYPEPAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), typePaiement, "typePaiement"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDCOMPENSATION,
                this._dbWriteNumeric(statement.getTransaction(), idCompensation, "idCompensation"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDAFFILIE,
                this._dbWriteNumeric(statement.getTransaction(), idAffilie, "idAffilie"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDPARENT,
                this._dbWriteNumeric(statement.getTransaction(), idParent, "idParent"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_MONTANTVENTILE,
                this._dbWriteNumeric(statement.getTransaction(), montantVentile, "montantVentile"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDINSCRIPTIONCI,
                this._dbWriteNumeric(statement.getTransaction(), idInscriptionCI, "idInscriptionCI"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDREPARTITIONBENEFPAIEMENT, this._dbWriteNumeric(
                statement.getTransaction(), idRepartitionBeneficiairePaiement, "idRepartitionBeneficiairePaiement"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_TYPEPRESTATION,
                this._dbWriteNumeric(statement.getTransaction(), typePrestation, "typePrestation"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDTIERSADRESSEPAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idTiersAdressePaiement, "idTiersAdressePaiement"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDDOMAINE,
                this._dbWriteNumeric(statement.getTransaction(), idDomaineAdressePaiement, "idDomaineAdressePaiement"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_TAUXRJM,
                this._dbWriteNumeric(statement.getTransaction(), tauxRJM, "tauxRJM"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDSITUATIONPROFESSIONNELLE, this._dbWriteNumeric(
                statement.getTransaction(), idSituationProfessionnelle, "idSituationProfessionnelle"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_REFERENCE_INTERNE,
                this._dbWriteString(statement.getTransaction(), getReferenceInterne(), "referenceInterne"));
        statement.writeField(APRepartitionPaiements.FIELDNAME_IDAFFILIEADRESSEPAIEMENT,
                this._dbWriteNumeric(statement.getTransaction(), idAffilieAdrPmt, "idAffilieAdrPmt"));

        statement.writeField(APRepartitionPaiements.FIELDNAME_TYPE_ASSOCIATION_ASSURANCE,
                this._dbWriteNumeric(statement.getTransaction(), typeAssociationAssurance, "typeAssociationAssurance"));
    }

    public void chercherAdressePaiement(BTransaction transaction, String idSituationProf) throws Exception {

        String idDomainePaiement = null;
        String idTiersPaiement = idTiers;

        if (!JadeStringUtil.isBlankOrZero(idSituationProf)) {
            APSituationProfessionnelle situation = new APSituationProfessionnelle();
            situation.setSession(getSession());
            situation.setId(idSituationProf);
            situation.retrieve();

            if (!situation.isNew()) {
                idDomainePaiement = situation.getIdDomainePaiementEmployeur();
                idTiersPaiement = situation.getIdTiersPaiementEmployeur();
            }
        } else {
            // chercher adresse de paiement
            APPrestation prestation = new APPrestation();
            prestation.setSession(getSession());
            prestation.setIdPrestationApg(getIdPrestationApg());
            prestation.retrieve(transaction);

            if (IAPDroitMaternite.CS_REVISION_MATERNITE_2005.equals(prestation.getNoRevision())) {
                idDomainePaiement = IPRConstantesExternes.TIERS_CS_DOMAINE_MATERNITE;
            } else {
                idDomainePaiement = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG;
            }
        }

        setAdressePaiement(PRTiersHelper.getAdressePaiementData(getSession(), transaction, idTiersPaiement,
                idDomainePaiement, idAffilie, JACalendar.todayJJsMMsAAAA()));
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
     * @see globaz.prestation.tools.PRHierarchique#getIdMajeur()
     */
    @Override
    public String getIdMajeur() {
        return idRepartitionBeneficiairePaiement;
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
     * getter pour l'attribut id prestation apg.
     * 
     * @return la valeur courante de l'attribut id prestation apg
     */
    public String getIdPrestationApg() {
        return idPrestationApg;
    }

    /**
     * getter pour l'attribut id repartition beneficiaire paiement.
     * 
     * @return la valeur courante de l'attribut id repartition beneficiaire paiement
     */
    public String getIdRepartitionBeneficiairePaiement() {
        return idRepartitionBeneficiairePaiement;
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
        if ((nom != null) && (nom.length() > 40)) {
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

    public APSituationProfessionnelle getSituatuionPro() {
        return sitPro;
    }

    /**
     * getter pour l'attribut taux RJM.
     * 
     * @return la valeur courante de l'attribut taux RJM
     */
    public String getTauxRJM() {
        return tauxRJM;
    }

    public String getTypeAssociationAssurance() {
        return typeAssociationAssurance;
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
     * getter pour l'attribut type prestation.
     * 
     * @return la valeur courante de l'attribut type prestation
     */
    public String getTypePrestation() {
        return typePrestation;
    }

    /**
     * retourne vrai si le beneficiaire de cette repartition de paiement est un employeur, faux si le benenficiaire est
     * un assure.
     * 
     * @return la valeur courante de l'attribut beneficiaire employeur
     */
    public boolean isBeneficiaireEmployeur() {
        return IAPRepartitionPaiements.CS_PAIEMENT_EMPLOYEUR.equals(typePaiement);
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
     * getter pour l'attribut tiers set.
     * 
     * @return la valeur courante de l'attribut tiers set
     */
    public boolean isTiersSet() {
        return !JadeStringUtil.isEmpty(idTiers);
    }

    /**
     * charge l'adresse de paiement.
     * 
     * @return une adresse de paiement ou null.
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

    public APSituationProfessionnelle loadSituationProfessionnelle() throws Exception {
        if ((sitPro == null) && !JadeStringUtil.isIntegerEmpty(idSituationProfessionnelle)) {
            sitPro = new APSituationProfessionnelle();
            sitPro.setIdSituationProf(idSituationProfessionnelle);
            sitPro.setSession(getSession());
            sitPro.retrieve();
        }

        return sitPro;
    }

    protected BigDecimal montantTotalVentilation(BTransaction transaction) throws Exception {
        APRepartitionPaiementsManager repartitions = new APRepartitionPaiementsManager();
        repartitions.setForIdParent(getIdRepartitionBeneficiairePaiement());
        repartitions.setNotForIdRepartition(idRepartitionBeneficiairePaiement);
        repartitions.setSession(getSession());
        return repartitions.getSum(APRepartitionPaiements.FIELDNAME_MONTANTVENTILE, transaction);
    }

    private void remetLotEnOuvertEtEffaceCompensations(BTransaction transaction) throws Exception {
        // remise en état ouvert du lot et effacement des compensations(S'il est
        // compensé, les compensations sont
        // maintenant incohérentes)
        if (miseAJourLot) {
            APPrestation prestation = new APPrestation();

            prestation.setSession(getSession());
            prestation.setIdPrestationApg(idPrestationApg);
            prestation.retrieve(transaction);

            APLot lot = new APLot();

            lot.setSession(getSession());
            lot.setIdLot(prestation.getIdLot());
            lot.retrieve(transaction);

            if (lot.getEtat().equals(IAPLot.CS_COMPENSE)) {
                lot.setEtat(IAPLot.CS_OUVERT);
                lot.update(transaction);

                APCompensationManager compensationManager = new APCompensationManager();

                compensationManager.setSession(getSession());
                compensationManager.setForIdLot(lot.getIdLot());
                compensationManager.find(transaction);

                for (int i = 0; i < compensationManager.size(); i++) {
                    ((APCompensation) (compensationManager.getEntity(i))).delete(transaction);
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

            if (!JadeStringUtil.isBlankOrZero(adressePaiement.getIdExterneAvoirPaiement())) {
                // setter l'idAffilie dans le BEntity
                IAFAffiliation affiliationIfc = (IAFAffiliation) getSession().getAPIFor(IAFAffiliation.class);
                Hashtable criteres = new Hashtable();
                criteres.put(IAFAffiliation.FIND_FOR_NOAFFILIE, adressePaiement.getIdExterneAvoirPaiement());
                IAFAffiliation[] result = affiliationIfc.findAffiliation(criteres);
                idAffilieAdrPmt = result[0].getAffiliationId();
            } else {
                idAffilieAdrPmt = "";
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
     * setter pour l'attribut id compensation.
     * 
     * @param idCompensation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCompensation(String idCompensation) {
        this.idCompensation = idCompensation;
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
     * setter pour l'attribut id prestation apg.
     * 
     * @param idPrestationApg
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrestationApg(String idPrestationApg) {
        this.idPrestationApg = idPrestationApg;
    }

    /**
     * setter pour l'attribut id repartition beneficiaire paiement.
     * 
     * @param idRepartitionBeneficiairePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRepartitionBeneficiairePaiement(String idRepartitionBeneficiairePaiement) {
        this.idRepartitionBeneficiairePaiement = idRepartitionBeneficiairePaiement;
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
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersAdressePaiement(String string) {
        idTiersAdressePaiement = string;
    }

    /**
     * setter pour l'attribut montant brut.
     * 
     * @param montantBrut
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrut(String montantBrut) {
        this.montantBrut = JANumberFormatter.deQuote(montantBrut);
    }

    /**
     * setter pour l'attribut montant net.
     * 
     * @param montantNet
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantNet(String montantNet) {
        this.montantNet = JANumberFormatter.deQuote(montantNet);
    }

    /**
     * setter pour l'attribut montant ventile.
     * 
     * @param montantVentile
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantVentile(String montantVentile) {
        this.montantVentile = JANumberFormatter.deQuote(montantVentile);
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
     * setter pour l'attribut taux RJM.
     * 
     * @param tauxRJM
     *            une nouvelle valeur pour cet attribut
     */
    public void setTauxRJM(String tauxRJM) {
        this.tauxRJM = tauxRJM;
    }

    public void setTypeAssociationAssurance(String typeAssociationAssurance) {
        this.typeAssociationAssurance = typeAssociationAssurance;
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
     * setter pour l'attribut type prestation.
     * 
     * @param typePrestation
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

    public String toMyString() {
        return getIdRepartitionBeneficiairePaiement();
    }

    public void wantMiseAJourLot(boolean b) {
        miseAJourLot = b;
    }

}
