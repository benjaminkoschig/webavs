package globaz.perseus.vb.qd;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import ch.globaz.perseus.business.constantes.CSEtatFacture;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.models.informationfacture.InformationFactureException;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.informationfacture.InformationFacture;
import ch.globaz.perseus.business.models.informationfacture.InformationFactureSearchModel;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * Viewbean permettant l'affichage des détails d'une facture.
 * 
 * @author JSI
 * 
 */
public class PFFactureViewBean extends BJadePersistentObjectViewBean {

    public static String getLabel(String code) throws RemoteException {
        String output = BSessionUtil.getSessionFromThreadContext().getLabel(code);

        return '"' + output + '"';
    }

    private AdresseTiersDetail adresseCourrierAssure = null;
    private AdresseTiersDetail adressePaiementAssure = null;
    private Dossier dossier = null;
    private Facture facture = null;
    private InformationFactureSearchModel searchModel;
    private InformationFacture informationFacture = null;
    private String forcerAcceptation = null;
    private Float limiteAvertissement = new Float(0);
    private Vector listQd;
    private String idDossier = null;
    private boolean modificationFacture = false;
    private String typeVersement = null;
    private Facture factureAModifier;
    private FWCurrency ancienMontantFacture = new FWCurrency(0.0);
    private FWCurrency ancienMontantARembourserFacture = new FWCurrency(0.0);
    private String hygienisteDentaire = null;
    private String casDeRigueur = null;

    // private FWCurrency ancienMontantDepassantFacture = new FWCurrency(0.0);

    public String getHygienisteDentaire() {
        return hygienisteDentaire;
    }

    public void setHygienisteDentaire(String hygienisteDentaire) {
        this.hygienisteDentaire = hygienisteDentaire;
    }

    public String getCasDeRigueur() {
        return casDeRigueur;
    }

    public void setCasDeRigueur(String casDeRigueur) {
        this.casDeRigueur = casDeRigueur;
    }

    public boolean getModificationFacture() {
        return modificationFacture;
    }

    public void setModificationFacture(boolean isModificationFacture) {
        modificationFacture = isModificationFacture;
    }

    public FWCurrency getAncienMontantFacture() {
        return ancienMontantFacture;
    }

    public void setAncienMontantFacture(double ancienMontantFacture) {
        this.ancienMontantFacture = new FWCurrency(ancienMontantFacture);
    }

    public void setAncienMontantARembourserFacture(double ancienMontantARembourserFacture) {
        this.ancienMontantARembourserFacture = new FWCurrency(ancienMontantARembourserFacture);
    }

    public FWCurrency getAncienMontantARembourserFacture() {
        return ancienMontantARembourserFacture;
    }

    public String getIdFacture() {
        return facture.getId();
    }

    public void setIdFacture(String idFacture) {
        facture.setId(idFacture);

    }

    public String getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public PFFactureViewBean() {
        super();
        dossier = new Dossier();
        facture = new Facture();
        factureAModifier = new Facture();
        informationFacture = new InformationFacture();
        listQd = new Vector();
    }

    public PFFactureViewBean(Facture facture) {
        this();
        this.facture = facture;

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
        facture.getSimpleFacture().setCsEtat(CSEtatFacture.ENREGISTRE.getCodeSystem());
        facture.getSimpleFacture().setHygienisteDentaire("on".equals(hygienisteDentaire));
        facture.getSimpleFacture().setCasDeRigueur("on".equals(casDeRigueur));
        PerseusServiceLocator.getFactureService().create(facture, "on".equals(forcerAcceptation));

        if ((!informationFacture.getSimpleInformationFacture().getDescription().isEmpty())
                || (!informationFacture.getSimpleInformationFacture().getDate().isEmpty())) {
            informationFacture.getSimpleInformationFacture().setIdDossier(dossier.getId());
            PerseusServiceLocator.getInformationFactureService().create(informationFacture);
        }
    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getFactureService().delete(facture);
    }

    public void deleteFactureAModifier() throws Exception {
        factureAModifier = PerseusServiceLocator.getFactureService().delete(factureAModifier);
    }

    public AdresseTiersDetail getAdresseCourrierAssure() {
        try {
            if (facture != null
                    && JadeStringUtil.isBlankOrZero(facture.getSimpleFacture().getIdApplicationAdresseCourrier()) == false) {
                adresseCourrierAssure = PFUserHelper.getAdresseAssure(facture.getSimpleFacture()
                        .getIdTiersAdresseCourrier(), IConstantes.CS_AVOIR_ADRESSE_COURRIER, facture.getSimpleFacture()
                        .getIdApplicationAdresseCourrier(), JACalendar.todayJJsMMsAAAA());
            } else {
                adresseCourrierAssure = PFUserHelper.getAdresseAssure(getDossier().getDemandePrestation()
                        .getPersonneEtendue().getTiers().getIdTiers(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        JACalendar.todayJJsMMsAAAA());
            }
        } catch (Exception e) {
            JadeLogger.warn(adresseCourrierAssure, "Probleme survenu");
        }

        return adresseCourrierAssure;
    }

    /**
     * @return the adressePaiementAssure
     */
    public AdresseTiersDetail getAdressePaiementAssure() {
        try {
            if (facture != null
                    && JadeStringUtil.isBlankOrZero(facture.getSimpleFacture().getIdApplicationAdressePaiement()) == false) {
                adressePaiementAssure = PFUserHelper.getAdressePaiementAssure(facture.getSimpleFacture()
                        .getIdTiersAdressePaiement(), facture.getSimpleFacture().getIdApplicationAdressePaiement(),
                        JACalendar.todayJJsMMsAAAA());
            } else {
                adressePaiementAssure = PFUserHelper.getAdressePaiementAssure(getDossier().getDemandePrestation()
                        .getPersonneEtendue().getTiers().getIdTiers(), JACalendar.todayJJsMMsAAAA());
            }
        } catch (Exception e) {
            JadeLogger.warn(adressePaiementAssure, "Probleme survenu");
        }

        return adressePaiementAssure;
    }

    /**
     * @return the dossier
     */
    public Dossier getDossier() {
        return dossier;
    }

    /**
     * @return the facture
     */
    public Facture getFacture() {
        return facture;
    }

    public InformationFacture getInformationFacture() {
        return informationFacture;
    }

    /**
     * @return the forcerAcceptation
     */
    public String getForcerAcceptation() {
        return forcerAcceptation;
    }

    @Override
    public String getId() {
        return facture.getId();
    }

    public float getLimiteAvertissement() {
        return limiteAvertissement;
    }

    /**
     * @return the listQd
     */
    public Vector getListQd() {
        return listQd;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(facture.getSpy());
    }

    /**
     * @return the typeVersement
     */
    public String getTypeVersement() {
        return typeVersement;
    }

    public void init(BISession session) throws Exception {
        setDossier(PerseusServiceLocator.getDossierService().read(dossier.getId()));

        getISession().setAttribute("likeNss",
                getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());

        // Chargement des QDs
        listQd.add(new String[] { "", "" });
        initListQd(session, CSTypeQD.FRAIS_GARDE, "");
        initListQd(session, CSTypeQD.FRAIS_MALADIE, "");

        VariableMetier limiteAvertissementVM = PerseusServiceLocator.getVariableMetierService().getFromCS(
                CSVariableMetier.FACTURES_LIMITE_AVERTISSEMENT.getCodeSystem(), Calendar.getInstance().getTime());
        limiteAvertissement = limiteAvertissementVM.getMontant();
    }

    private void initListQd(BISession session, CSTypeQD qd, String indent) throws Exception {
        if (!CSTypeQD.FRAIS_MALADIE.equals(qd)) {
            listQd.add(new String[] { qd.getCodeSystem(), session.getCodeLibelle(qd.getCodeSystem()) });
        }
        List<CSTypeQD> qdTries = qd.getListChild();
        Collections.sort(qdTries);
        for (CSTypeQD qdEnfant : qdTries) {
            initListQd(session, qdEnfant, indent + "&nbsp;&nbsp;&nbsp;");
        }
    }

    @Override
    public void retrieve() throws Exception {
        facture = PerseusServiceLocator.getFactureService().read(getId());

        setDossier(PerseusServiceLocator.getDossierService().read(dossier.getId()));

        getISession().setAttribute("likeNss",
                getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());

        // Chargement des QDs
        listQd.add(new String[] { "", "" });
        initListQd(getISession(), CSTypeQD.FRAIS_GARDE, "");
        initListQd(getISession(), CSTypeQD.FRAIS_MALADIE, "");

        // Chargement de l'adresse de paiement du requérant
        adressePaiementAssure = getAdressePaiementAssure();
        adresseCourrierAssure = getAdresseCourrierAssure();

        VariableMetier limiteAvertissementVM = PerseusServiceLocator.getVariableMetierService().getFromCS(
                CSVariableMetier.FACTURES_LIMITE_AVERTISSEMENT.getCodeSystem(), Calendar.getInstance().getTime());
        limiteAvertissement = limiteAvertissementVM.getMontant();

        if (JadeStringUtil.isEmpty(factureAModifier.getId())) {
            // Clone de la facture
            factureAModifier = (Facture) JadePersistenceUtil.clone(facture);

            // set des champs de la facture.simpleFacture
            factureAModifier.getSimpleFacture().setMontant(facture.getSimpleFacture().getMontant());
            factureAModifier.getSimpleFacture().setMontantDepassant(facture.getSimpleFacture().getMontantDepassant());
            factureAModifier.getSimpleFacture().setMontantRembourse(facture.getSimpleFacture().getMontantRembourse());
            factureAModifier.getSimpleFacture().setSpy(facture.getSimpleFacture().getSpy());
            factureAModifier.getSimpleFacture().setId(facture.getSimpleFacture().getId());

            // set des champs de la facture.Qd.QdAnnuelle.SimpleQDAnnuelle
            factureAModifier.getQd().getQdAnnuelle().getSimpleQDAnnuelle()
                    .setIdQDAnnuelle(facture.getQd().getQdAnnuelle().getSimpleQDAnnuelle().getIdQDAnnuelle());
            factureAModifier.getQd().getQdAnnuelle().getSimpleQDAnnuelle()
                    .setSpy(facture.getQd().getQdAnnuelle().getSimpleQDAnnuelle().getSpy());

            // set des champs de la facture.simpleFacture.Qd.QdParente.QdAnnuelle.Dossier.Dossier
            factureAModifier
                    .getQd()
                    .getQdParente()
                    .getQdAnnuelle()
                    .getDossier()
                    .getDossier()
                    .setIdDossier(
                            facture.getQd().getQdParente().getQdAnnuelle().getDossier().getDossier().getIdDossier());
            factureAModifier.getQd().getQdParente().getQdAnnuelle().getDossier().getDossier()
                    .setSpy(facture.getQd().getQdParente().getQdAnnuelle().getDossier().getDossier().getSpy());

            // set des champs de la facture.simpleFacture.Qd.QdParente.QdAnnuelle.SimpleQDAnnuelle
            factureAModifier
                    .getQd()
                    .getQdParente()
                    .getQdAnnuelle()
                    .getSimpleQDAnnuelle()
                    .setIdQDAnnuelle(
                            facture.getQd().getQdParente().getQdAnnuelle().getSimpleQDAnnuelle().getIdQDAnnuelle());
            factureAModifier.getQd().getQdParente().getQdAnnuelle().getSimpleQDAnnuelle()
                    .setSpy(facture.getQd().getQdParente().getQdAnnuelle().getSimpleQDAnnuelle().getSpy());

            // set des champs de la facture.Qd.QdParente.simplpeQd
            factureAModifier.getQd().getQdParente().getSimpleQD()
                    .setIdQD(facture.getQd().getQdParente().getSimpleQD().getIdQD());
            factureAModifier.getQd().getQdParente().getSimpleQD()
                    .setSpy(facture.getQd().getQdParente().getSimpleQD().getSpy());

            // set des champs de la facture.SimpleQd
            factureAModifier.getQd().getSimpleQD().setSpy(facture.getQd().getSimpleQD().getSpy());
            factureAModifier.getQd().getSimpleQD().setId(facture.getQd().getSimpleQD().getId());
            factureAModifier.getQd().getSimpleQD().setMontantUtilise(facture.getQd().getSimpleQD().getMontantUtilise());

            // String spy = factureAModifier.getQd().getSpy();
            // SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
            // Date now = new Date();

            // factureAModifier.getQd().getSimpleQD().setSpy(spy.substring(0, 8) + sdfTime.format(now) +
            // spy.substring(14));

        }

    }

    public void setAdresseCourrierAssure(AdresseTiersDetail adresseCourrierAssure) {
        this.adresseCourrierAssure = adresseCourrierAssure;
    }

    /**
     * @param adressePaiementAssure
     *            the adressePaiementAssure to set
     */
    public void setAdressePaiementAssure(AdresseTiersDetail adressePaiementAssure) {
        this.adressePaiementAssure = adressePaiementAssure;
    }

    /**
     * @param dossier
     *            the dossier to set
     */
    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    /**
     * @param facture
     *            the facture to set
     */
    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    /**
     * 
     * @param InfoFacture
     */
    public void setInformationFacture(InformationFacture InfoFacture) {
        informationFacture = InfoFacture;
    }

    /**
     * @param forcerAcceptation
     *            the forcerAcceptation to set
     */
    public void setForcerAcceptation(String forcerAcceptation) {
        this.forcerAcceptation = forcerAcceptation;
    }

    @Override
    public void setId(String newId) {
        facture.setId(newId);
    }

    public void setLimiteAvertissement(Float limiteAvertissement) {
        this.limiteAvertissement = limiteAvertissement;
    }

    /**
     * @param listQd
     *            the listQd to set
     */
    public void setListQd(Vector listQd) {
        this.listQd = listQd;
    }

    /**
     * @param typeVersement
     *            the typeVersement to set
     */
    public void setTypeVersement(String typeVersement) {
        this.typeVersement = typeVersement;
    }

    @Override
    public void update() throws Exception {
        // PerseusServiceLocator.getFactureService().update(this.facture, "on".equals(this.forcerAcceptation));
    }

}
