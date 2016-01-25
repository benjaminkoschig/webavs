package globaz.perseus.vb.rentepont;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import globaz.pyxis.constantes.IConstantes;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import ch.globaz.perseus.business.constantes.CSEtatFacture;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.models.informationfacture.InformationFactureException;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.informationfacture.InformationFacture;
import ch.globaz.perseus.business.models.informationfacture.InformationFactureSearchModel;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.models.rentepont.QDRentePont;
import ch.globaz.perseus.business.models.rentepont.QDRentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

public class PFFactureRentePontViewBean extends BJadePersistentObjectViewBean {

    public static String getLabel(String code) throws RemoteException {
        String output = BSessionUtil.getSessionFromThreadContext().getLabel(code);

        return '"' + output + '"';
    }

    private String adresseCourrierAssure = null;
    private String adressePaiementAssure = null;
    private String allSousTypesInJson;
    private Dossier dossier = null;
    private String etatComptabilisation = "";
    private FactureRentePont factureRentePont = null;
    private FactureRentePont factureAEnregistrerApresModification = null;
    private FactureRentePont factureASupprimerApresModification = null;
    private String acceptationForcee = null;
    private String idRentePont = null;
    private boolean isPaiementOKPourValidation = true;
    private Boolean isRestitutionPossible = false;
    private Float limiteAvertissement = new Float(0);
    private Vector<String[]> listMembresFamille = null;
    private double montantMaxCompense = 0;
    private Float montantMaxValidationUser;
    private InformationFactureSearchModel searchModel;
    private InformationFacture informationFacture = null;
    private RentePont rentePont = null;
    private HashMap<String, String> typesSoins;
    private String typeVersement = null;
    private Boolean modificationFacture = false;
    private FWCurrency ancienMontantFacture = new FWCurrency(0.0);
    private FWCurrency ancienMontantARembourserFacture = new FWCurrency(0.0);
    private String hygienisteDentaire = null;
    private String casDeRigueur = null;
    private String idGestionnaire = null;
    private String idTiersMembreFamille = null;

    public String getIdTiersMembreFamille() {
        return idTiersMembreFamille;
    }

    public void setIdTiersMembreFamille(String idTiersMembreFamille) {
        this.idTiersMembreFamille = idTiersMembreFamille;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public String getCasDeRigueur() {
        return casDeRigueur;
    }

    public void setCasDeRigueur(String casDeRigueur) {
        this.casDeRigueur = casDeRigueur;
    }

    public String getHygienisteDentaire() {
        return hygienisteDentaire;
    }

    public void setHygienisteDentaire(String hygienisteDentaire) {
        this.hygienisteDentaire = hygienisteDentaire;
    }

    public void setAncienMontantARembourserFacture(double ancienMontantARembourserFacture) {
        this.ancienMontantARembourserFacture = new FWCurrency(ancienMontantARembourserFacture);
    }

    public FWCurrency getAncienMontantARembourserFacture() {
        return ancienMontantARembourserFacture;
    }

    public FWCurrency getAncienMontantFacture() {
        return ancienMontantFacture;
    }

    public void setAncienMontantFacture(double ancienMontantFacture) {
        this.ancienMontantFacture = new FWCurrency(ancienMontantFacture);
    }

    public PFFactureRentePontViewBean() {
        super();
        setFactureRentePont(new FactureRentePont());
        dossier = new Dossier();
        informationFacture = new InformationFacture();
    }

    public PFFactureRentePontViewBean(FactureRentePont factureRentePont) {
        super();
        setFactureRentePont(factureRentePont);
    }

    public Boolean getModificationFacture() {
        return modificationFacture;
    }

    public void setModificationFacture(Boolean isModificationFacture) {
        modificationFacture = isModificationFacture;
    }

    public void chercherInformationFacture() {
        try {
            searchModel = new InformationFactureSearchModel();
            searchModel.setForIdDossier(getDossier().getDossier().getIdDossier());
            searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchModel = PerseusServiceLocator.getInformationFactureService().search(searchModel);
        } catch (InformationFactureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadeApplicationServiceNotAvailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JadePersistenceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public InformationFacture getUneInformationFacture(int i) {
        return (InformationFacture) searchModel.getSearchResults()[i];
    }

    public int getSizeInformationFacture() {
        return searchModel.getSize();
    }

    @Override
    public void add() throws Exception {
        factureRentePont.getSimpleFactureRentePont().setAcceptationForcee("on".equals(acceptationForcee));
        factureRentePont.getSimpleFactureRentePont().setHygienisteDentaire("on".equals(hygienisteDentaire));
        factureRentePont.getSimpleFactureRentePont().setCasDeRigueur("on".equals(casDeRigueur));
        factureRentePont.getSimpleFactureRentePont().setIdGestionnaire(idGestionnaire);
        factureRentePont.getSimpleFactureRentePont().setCsEtat(CSEtatFacture.ENREGISTRE.getCodeSystem());

        factureRentePont = PerseusServiceLocator.getFactureRentePontService().create(factureRentePont);

        if ((!informationFacture.getSimpleInformationFacture().getDescription().isEmpty())
                || (!informationFacture.getSimpleInformationFacture().getDate().isEmpty())) {
            informationFacture.getSimpleInformationFacture().setIdDossier(dossier.getId());
            PerseusServiceLocator.getInformationFactureService().create(informationFacture);
        }
    }

    public void addFactureModifie() throws Exception {

        // ----------------------------------------------------------------------------------------------------

        factureAEnregistrerApresModification = (FactureRentePont) JadePersistenceUtil.clone(factureRentePont);

        factureAEnregistrerApresModification.getSimpleFactureRentePont().setAcceptationForcee(
                "on".equals(acceptationForcee));

        factureAEnregistrerApresModification.getSimpleFactureRentePont().setCasDeRigueur("on".equals(casDeRigueur));
        factureAEnregistrerApresModification.getSimpleFactureRentePont().setHygienisteDentaire(
                "on".equals(hygienisteDentaire));

        factureAEnregistrerApresModification.getSimpleFactureRentePont().setIdGestionnaire(idGestionnaire);

        factureAEnregistrerApresModification.getSimpleFactureRentePont().setCsMotif(
                factureRentePont.getSimpleFactureRentePont().getCsMotif());
        factureAEnregistrerApresModification.getSimpleFactureRentePont().setCsSousTypeSoinRentePont(
                factureRentePont.getSimpleFactureRentePont().getCsSousTypeSoinRentePont());
        factureAEnregistrerApresModification.getSimpleFactureRentePont().setCsTypeSoinRentePont(
                factureRentePont.getSimpleFactureRentePont().getCsTypeSoinRentePont());
        factureAEnregistrerApresModification.getSimpleFactureRentePont().setIdTiersMembreFamille(
                factureRentePont.getSimpleFactureRentePont().getIdTiersMembreFamille());

        factureAEnregistrerApresModification.getSimpleFactureRentePont().setCsEtat(
                CSEtatFacture.ENREGISTRE.getCodeSystem());
        factureAEnregistrerApresModification.getSimpleFactureRentePont().setCreationSpy("");
        factureAEnregistrerApresModification.getSimpleFactureRentePont().setIdFactureRentePont(new String());

        factureAEnregistrerApresModification.getQdRentePont().setSpy(factureRentePont.getQdRentePont().getSpy());
        factureAEnregistrerApresModification.getQdRentePont().setId(factureRentePont.getQdRentePont().getId());
        factureAEnregistrerApresModification.getQdRentePont()
                .setDossier(factureRentePont.getQdRentePont().getDossier());

        factureAEnregistrerApresModification.getQdRentePont().getSimpleQDRentePont()
                .setMontantUtilise(factureRentePont.getQdRentePont().getSimpleQDRentePont().getMontantUtilise());
        factureAEnregistrerApresModification.getQdRentePont().getSimpleQDRentePont()
                .setMontantLimite(factureRentePont.getQdRentePont().getSimpleQDRentePont().getMontantLimite());
        factureAEnregistrerApresModification.getQdRentePont().getSimpleQDRentePont()
                .setId(factureRentePont.getQdRentePont().getSimpleQDRentePont().getId());
        factureAEnregistrerApresModification.getQdRentePont().getSimpleQDRentePont()
                .setIdQDRentePont(factureRentePont.getQdRentePont().getSimpleQDRentePont().getIdQDRentePont());
        factureAEnregistrerApresModification.getQdRentePont().getSimpleQDRentePont()
                .setIdDossier(factureRentePont.getQdRentePont().getSimpleQDRentePont().getIdDossier());
        factureAEnregistrerApresModification.getQdRentePont().getSimpleQDRentePont()
                .setExcedantRevenu(factureRentePont.getQdRentePont().getSimpleQDRentePont().getExcedantRevenu());
        factureAEnregistrerApresModification
                .getQdRentePont()
                .getSimpleQDRentePont()
                .setExcedantRevenuCompense(
                        factureRentePont.getQdRentePont().getSimpleQDRentePont().getExcedantRevenuCompense());
        factureAEnregistrerApresModification.getQdRentePont().getSimpleQDRentePont()
                .setSpy(factureRentePont.getQdRentePont().getSimpleQDRentePont().getSpy());

        factureAEnregistrerApresModification = PerseusServiceLocator.getFactureRentePontService().create(
                factureAEnregistrerApresModification);

        if ((!informationFacture.getSimpleInformationFacture().getDescription().isEmpty())
                || (!informationFacture.getSimpleInformationFacture().getDate().isEmpty())) {
            informationFacture.getSimpleInformationFacture().setIdDossier(dossier.getId());
            PerseusServiceLocator.getInformationFactureService().create(informationFacture);
        }
    }

    public void calculeMontantMaxCompense() {
        double montantLimite = JadeStringUtil.parseDouble(factureRentePont.getQdRentePont().getSimpleQDRentePont()
                .getMontantLimite(), 0);
        double montantUtilise = JadeStringUtil.parseDouble(factureRentePont.getQdRentePont().getSimpleQDRentePont()
                .getMontantUtilise(), 0);
        montantMaxCompense = (montantLimite - montantUtilise)/* - (excedantRevenu - excedantRevenuCompense) */;
    }

    private void definAdresseCourrier() throws Exception {
        AdresseTiersDetail adresse = null;
        try {
            if (factureRentePont != null
                    && JadeStringUtil.isBlankOrZero(factureRentePont.getSimpleFactureRentePont()
                            .getIdApplicationAdresseCourrier()) == false) {
                adresse = PFUserHelper.getAdresseAssure(factureRentePont.getSimpleFactureRentePont()
                        .getIdTiersAdresseCourrier(), IConstantes.CS_AVOIR_ADRESSE_COURRIER, factureRentePont
                        .getSimpleFactureRentePont().getIdApplicationAdresseCourrier(), JACalendar.todayJJsMMsAAAA());
            } else {
                adresse = PFUserHelper.getAdresseAssure(getDossier().getDemandePrestation().getPersonneEtendue()
                        .getTiers().getIdTiers(), IConstantes.CS_AVOIR_ADRESSE_COURRIER, JACalendar.todayJJsMMsAAAA());
            }
        } catch (Exception e) {
            JadeLogger.warn(adresseCourrierAssure, "Probleme survenu");
        }

        adresseCourrierAssure = adresse.getFields() != null ? adresse.getAdresseFormate() : "";
    }

    private void definAdressePaiement() throws Exception {
        AdresseTiersDetail adresse = null;
        try {
            if (factureRentePont != null
                    && JadeStringUtil.isBlankOrZero(factureRentePont.getSimpleFactureRentePont()
                            .getIdApplicationAdressePaiement()) == false) {
                adresse = PFUserHelper.getAdressePaiementAssure(factureRentePont.getSimpleFactureRentePont()
                        .getIdTiersAdressePaiement(), factureRentePont.getSimpleFactureRentePont()
                        .getIdApplicationAdressePaiement(), JACalendar.todayJJsMMsAAAA());
            } else {
                adresse = PFUserHelper.getAdressePaiementAssure(getDossier().getDemandePrestation()
                        .getPersonneEtendue().getTiers().getIdTiers(), JACalendar.todayJJsMMsAAAA());
            }
        } catch (Exception e) {
            JadeLogger.warn(adressePaiementAssure, "Probleme survenu");
        }

        adressePaiementAssure = adresse.getFields() != null ? adresse.getAdresseFormate() : "";
    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getFactureRentePontService().delete(factureRentePont);
    }

    public void deleteFactureAModifier() throws Exception {
        factureASupprimerApresModification = PerseusServiceLocator.getFactureRentePontService().read(getId());

        String spy = factureASupprimerApresModification.getQdRentePont().getSpy();
        SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
        Date now = new Date();

        factureASupprimerApresModification.getQdRentePont().getSimpleQDRentePont()
                .setSpy(spy.substring(0, 8) + sdfTime.format(now) + spy.substring(14));

        // -------------------------------------------------------------------------------------

        factureASupprimerApresModification.getQdRentePont().setSpy(
                factureAEnregistrerApresModification.getQdRentePont().getSpy());

        PerseusServiceLocator.getFactureRentePontService().delete(factureASupprimerApresModification);
    }

    public String getAdresseCourrierAssure() throws Exception {
        return adresseCourrierAssure;
    }

    public String getAdressePaiementAssure() throws Exception {
        return adressePaiementAssure;
    }

    public String getAllSousTypesInJson() {
        return allSousTypesInJson;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public String getEtatComptabilisation() {
        return etatComptabilisation;
    }

    public FactureRentePont getFactureRentePont() {
        return factureRentePont;
    }

    public String getAcceptationForcee() {
        return acceptationForcee;
    }

    @Override
    public String getId() {
        return factureRentePont.getId();
    }

    public String getIdRentePont() {
        return idRentePont;
    }

    public InformationFacture getInformationFacture() {
        return informationFacture;
    }

    /**
     * @return the isRestitutionPossible
     */
    public Boolean getIsRestitutionPossible() {
        return isRestitutionPossible;
    }

    public float getLimiteAvertissement() {
        return limiteAvertissement;
    }

    public Vector<String[]> getListMembresFamille() {
        return listMembresFamille;
    }

    public double getMontantMaxCompense() {
        return montantMaxCompense;
    }

    public Float getMontantMaxValidationUser() {
        return montantMaxValidationUser;
    }

    public RentePont getRentePont() {
        return rentePont;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(factureRentePont.getSpy());
    }

    public HashMap<String, String> getTypesSoins() {
        return typesSoins;
    }

    public String getTypeVersement() {
        return typeVersement;
    }

    public boolean hasDateValidation() {
        String dateValidation = getFactureRentePont().getSimpleFactureRentePont().getDateValidation();

        return ((dateValidation != null) && (dateValidation.length() > 0));
    }

    public void init() throws Exception {
        // Types et sous-types
        typesSoins = PerseusServiceLocator.getTypesSoinsRentePontService().getMapSurTypes(getISession());
        allSousTypesInJson = PerseusServiceLocator.getTypesSoinsRentePontService().getAllSousTypesInJson(getISession());

        if (JadeStringUtil.isEmpty(factureRentePont.getId())) {
            setDossier(PerseusServiceLocator.getDossierService().read(dossier.getId()));

            // Recherche de la demande de rente pont
            setRentePont(PerseusServiceLocator.getRentePontService().read(idRentePont));

            if (null == getRentePont()) {
                throw new Exception("Impossible de charger la demande pour la recherche de la QD.");
            }

            QDRentePontSearchModel qdRentePontSearchModel = new QDRentePontSearchModel();
            qdRentePontSearchModel.setForIdDossier(dossier.getId());
            qdRentePontSearchModel.setForAnnee(getRentePont().getSimpleRentePont().getDateDebut().substring(6));
            qdRentePontSearchModel = PerseusServiceLocator.getQDRentePontService().search(qdRentePontSearchModel);
            if (qdRentePontSearchModel.getSize() == 1) {
                getFactureRentePont().setQdRentePont((QDRentePont) qdRentePontSearchModel.getSearchResults()[0]);
                getFactureRentePont().getSimpleFactureRentePont().setIdQDRentePont(
                        getFactureRentePont().getQdRentePont().getId());
            } else {
                throw new Exception("Essai d'ajouter une FactureRentePont sans QDRentePont dans le Dossier.");
            }
        }
        Vector<String[]> v = new Vector<String[]>();
        for (MembreFamille mf : PerseusServiceLocator.getDossierService().getListAllMembresFamilleRentePont(
                getDossier().getId())) {
            if (!JadeStringUtil.isBlank(mf.getId())) {
                v.add(new String[] {
                        mf.getSimpleMembreFamille().getIdTiers(),
                        mf.getPersonneEtendue().getTiers().getDesignation1() + " "
                                + mf.getPersonneEtendue().getTiers().getDesignation2() });
            }
        }

        if (!JadeStringUtil.isEmpty(factureRentePont.getSimpleFactureRentePont().getIdTiersMembreFamille())) {
            idTiersMembreFamille = factureRentePont.getSimpleFactureRentePont().getIdTiersMembreFamille();
        }

        if (v.size() == 0) {
            v.add(new String[] { "0", "" });
        }
        setListMembresFamille(v);
        calculeMontantMaxCompense();

        VariableMetier limiteAvertissementVM = PerseusServiceLocator.getVariableMetierService().getFromCS(
                CSVariableMetier.FACTURES_LIMITE_AVERTISSEMENT.getCodeSystem(), Calendar.getInstance().getTime());
        limiteAvertissement = limiteAvertissementVM.getMontant();

        definAdressePaiement();
        definAdresseCourrier();
    }

    public boolean isPaiementOKPourValidation() {
        return isPaiementOKPourValidation;
    }

    @Override
    public void retrieve() throws Exception {
        factureRentePont = PerseusServiceLocator.getFactureRentePontService().read(getId());

        setPaiementOKPourValidation(PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise());

        setDossier(factureRentePont.getQdRentePont().getDossier());

        init();

        VariableMetier montantMaxValidationUserVM = PerseusServiceLocator.getVariableMetierService().getFromCS(
                CSVariableMetier.FACTURES_LIMITE_VALIDATION_GESTIONNAIRE.getCodeSystem(),
                Calendar.getInstance().getTime());

        setMontantMaxValidationUser(montantMaxValidationUserVM.getMontant());

        isRestitutionPossible = false;
        // Récupérer le lot contenant la facture et retourner son état, sa date si comptabilisé et le numéro du
        Lot lot = PerseusServiceLocator.getLotService().getLotForFactureRP(factureRentePont.getId());
        if (lot != null) {
            if (!JadeStringUtil.isEmpty(lot.getSimpleLot().getDateEnvoi())
                    && !CSEtatFacture.RESTITUE.getCodeSystem().equals(
                            factureRentePont.getSimpleFactureRentePont().getCsEtat())) {
                isRestitutionPossible = true;
            }
            // Récupère l'état du lot
            etatComptabilisation += getISession().getCodeLibelle(lot.getSimpleLot().getEtatCs());
            etatComptabilisation += " (" + lot.getSimpleLot().getDescription() + ")";
        } else {
            etatComptabilisation += "-";
        }
    }

    public void setAdresseCourrierAssure(String adresseCourrierAssure) {
        this.adresseCourrierAssure = adresseCourrierAssure;
    }

    public void setAdressePaiementAssure(String adressePaiementAssure) {
        this.adressePaiementAssure = adressePaiementAssure;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public void setFactureRentePont(FactureRentePont factureRentePont) {
        this.factureRentePont = factureRentePont;
    }

    public void setAcceptationForcee(String forcerAcceptation) {
        acceptationForcee = forcerAcceptation;
    }

    @Override
    public void setId(String newId) {
        factureRentePont.setId(newId);
    }

    public void setIdRentePont(String idRentePont) {
        this.idRentePont = idRentePont;
    }

    public void setInformationFacture(InformationFacture InfoFacture) {
        informationFacture = InfoFacture;
    }

    public void setListMembresFamille(Vector<String[]> listMembresFamille) {
        this.listMembresFamille = listMembresFamille;
    }

    public void setMontantMaxCompense(double montantMaxCompense) {
        this.montantMaxCompense = montantMaxCompense;
    }

    public void setMontantMaxValidationUser(Float montantMaxValidationUser) {
        this.montantMaxValidationUser = montantMaxValidationUser;
    }

    public void setPaiementOKPourValidation(boolean isPaiementOKPourValidation) {
        this.isPaiementOKPourValidation = isPaiementOKPourValidation;
    }

    public void setRentePont(RentePont rentePont) {
        this.rentePont = rentePont;
    }

    public void setTypeVersement(String typeVersement) {
        this.typeVersement = typeVersement;
    }

    @Override
    public void update() throws Exception {
        if (CSEtatFacture.VALIDE.getCodeSystem().equals(factureRentePont.getSimpleFactureRentePont().getCsEtat())) {
            PerseusServiceLocator.getFactureRentePontService().restituer(factureRentePont);
        } else if (CSEtatFacture.ENREGISTRE.getCodeSystem().equals(
                factureRentePont.getSimpleFactureRentePont().getCsEtat())) {
            PerseusServiceLocator.getFactureRentePontService().validate(factureRentePont);
        }
    }

}
